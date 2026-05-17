package com.br.hermescomercial.async;

import com.br.hermescomercial.config.ConfigurationManager;
import com.br.hermescomercial.event.EventSystem;
import com.br.hermescomercial.logging.LoggerManager;

import java.util.concurrent.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.function.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.LocalDateTime;

/**
 * Gerenciador de Tarefas Assíncronas - Versão Refatorada
 * Implementa Async Pattern para operações longas do sistema
 * Versão 2.0.0 - Async Pattern Implementation
 */
public class AsyncTaskManager {
    
    private static volatile AsyncTaskManager instance;
    private static final Object lock = new Object();
    
    // Tipos de tarefas
    public enum TaskType {
        DATABASE("Banco de Dados"),
        FILE_IO("Operações de Arquivo"),
        NETWORK("Operações de Rede"),
        REPORT("Geração de Relatórios"),
        BACKUP("Backup/Restore"),
        IMPORT_EXPORT("Importação/Exportação"),
        CALCULATION("Cálculos Complexos"),
        NOTIFICATION("Notificações");
        
        private final String description;
        
        TaskType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    // Prioridade de tarefas
    public enum TaskPriority {
        LOW(1),
        NORMAL(5),
        HIGH(8),
        CRITICAL(10);
        
        private final int value;
        
        TaskPriority(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
    
    // Status da tarefa
    public enum TaskStatus {
        PENDING("Pendente"),
        RUNNING("Executando"),
        COMPLETED("Concluída"),
        FAILED("Falhou"),
        CANCELLED("Cancelada");
        
        private final String description;
        
        TaskStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    // Classe de tarefa
    public static class AsyncTask<T> {
        private final String id;
        private final String name;
        private final TaskType type;
        private final TaskPriority priority;
        private final Supplier<T> task;
        private final LocalDateTime createdAt;
        private volatile TaskStatus status;
        private volatile T result;
        private volatile Throwable error;
        private volatile LocalDateTime startedAt;
        private volatile LocalDateTime completedAt;
        private volatile long executionTime;
        private final Map<String, Object> metadata;
        
        public AsyncTask(String name, TaskType type, TaskPriority priority, Supplier<T> task) {
            this.id = java.util.UUID.randomUUID().toString();
            this.name = name;
            this.type = type;
            this.priority = priority;
            this.task = task;
            this.createdAt = LocalDateTime.now();
            this.status = TaskStatus.PENDING;
            this.metadata = new ConcurrentHashMap<>();
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public TaskType getType() { return type; }
        public TaskPriority getPriority() { return priority; }
        public TaskStatus getStatus() { return status; }
        public T getResult() { return result; }
        public Throwable getError() { return error; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getStartedAt() { return startedAt; }
        public LocalDateTime getCompletedAt() { return completedAt; }
        public long getExecutionTime() { return executionTime; }
        public Map<String, Object> getMetadata() { return metadata; }
        
        public void addMetadata(String key, Object value) {
            metadata.put(key, value);
        }
        
        public void setStatus(TaskStatus status) {
            this.status = status;
            if (status == TaskStatus.RUNNING) {
                this.startedAt = LocalDateTime.now();
            } else if (status == TaskStatus.COMPLETED || status == TaskStatus.FAILED || status == TaskStatus.CANCELLED) {
                this.completedAt = LocalDateTime.now();
                if (startedAt != null) {
                    this.executionTime = java.time.Duration.between(startedAt, completedAt).toMillis();
                }
            }
        }
        
        public void setResult(T result) {
            this.result = result;
            setStatus(TaskStatus.COMPLETED);
        }
        
        public void setError(Throwable error) {
            this.error = error;
            setStatus(TaskStatus.FAILED);
        }
        
        @Override
        public String toString() {
            return String.format("AsyncTask{id='%s', name='%s', type=%s, priority=%s, status=%s}", 
                id, name, type, priority, status);
        }
    }
    
    // Listener de eventos de tarefa
    public interface TaskListener<T> {
        void onTaskStarted(AsyncTask<T> task);
        void onTaskCompleted(AsyncTask<T> task);
        void onTaskFailed(AsyncTask<T> task, Throwable error);
        void onTaskCancelled(AsyncTask<T> task);
        void onTaskProgress(AsyncTask<T> task, double progress);
    }
    
    // Configurações
    private final ConfigurationManager configManager;
    private final EventSystem eventSystem;
    private final LoggerManager loggerManager;
    
    // Thread pools por tipo
    private final Map<TaskType, ExecutorService> threadPools = new ConcurrentHashMap<>();
    private final Map<TaskType, Integer> poolSizes = new ConcurrentHashMap<>();
    
    // Tarefas ativas
    private final Map<String, AsyncTask<?>> activeTasks = new ConcurrentHashMap<>();
    private final Map<String, List<TaskListener<?>>> taskListeners = new ConcurrentHashMap<>();
    
    // Configurações padrão
    private int defaultPoolSize = Runtime.getRuntime().availableProcessors();
    private long defaultTimeout = 300000; // 5 minutos
    private boolean enableMetrics = true;
    
    // Construtor privado para Singleton
    private AsyncTaskManager() {
        this.configManager = ConfigurationManager.getInstance();
        this.eventSystem = EventSystem.getInstance();
        this.loggerManager = LoggerManager.getInstance();
        
        initializeConfiguration();
        initializeThreadPools();
    }
    
    /**
     * Obtém instância singleton
     */
    public static AsyncTaskManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new AsyncTaskManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Inicializa configurações
     */
    private void initializeConfiguration() {
        defaultPoolSize = configManager.getInteger("async.default_pool_size", Runtime.getRuntime().availableProcessors());
        defaultTimeout = configManager.getInteger("async.default_timeout", 300000);
        enableMetrics = configManager.getBoolean("async.enable_metrics", true);
        
        // Configurar tamanhos de pool por tipo
        poolSizes.put(TaskType.DATABASE, configManager.getInteger("async.pool_size.database", 5));
        poolSizes.put(TaskType.FILE_IO, configManager.getInteger("async.pool_size.file_io", 3));
        poolSizes.put(TaskType.NETWORK, configManager.getInteger("async.pool_size.network", 10));
        poolSizes.put(TaskType.REPORT, configManager.getInteger("async.pool_size.report", 2));
        poolSizes.put(TaskType.BACKUP, configManager.getInteger("async.pool_size.backup", 1));
        poolSizes.put(TaskType.IMPORT_EXPORT, configManager.getInteger("async.pool_size.import_export", 2));
        poolSizes.put(TaskType.CALCULATION, configManager.getInteger("async.pool_size.calculation", 4));
        poolSizes.put(TaskType.NOTIFICATION, configManager.getInteger("async.pool_size.notification", 3));
    }
    
    /**
     * Inicializa thread pools
     */
    private void initializeThreadPools() {
        for (TaskType type : TaskType.values()) {
            int poolSize = poolSizes.getOrDefault(type, defaultPoolSize);
            
            ThreadFactory threadFactory = new ThreadFactory() {
                private final AtomicInteger counter = new AtomicInteger(0);
                
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "AsyncTask-" + type.name() + "-" + counter.incrementAndGet());
                    thread.setDaemon(true);
                    return thread;
                }
            };
            
            ExecutorService executor = new ThreadPoolExecutor(
                poolSize, poolSize,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory
            );
            
            threadPools.put(type, executor);
        }
    }
    
    /**
     * Executa tarefa assíncrona
     */
    public <T> CompletableFuture<T> executeAsync(AsyncTask<T> task) {
        return executeAsync(task, null);
    }
    
    /**
     * Executa tarefa assíncrona com listener
     */
    public <T> CompletableFuture<T> executeAsync(AsyncTask<T> task, TaskListener<T> listener) {
        CompletableFuture<T> future = new CompletableFuture<>();
        
        // Adicionar listener se fornecido
        if (listener != null) {
            addTaskListener(task.getId(), listener);
        }
        
        // Adicionar tarefa aos ativos
        activeTasks.put(task.getId(), task);
        
        // Publicar evento de criação
        eventSystem.publish(new com.br.hermescomercial.event.EventSystem.Event(
            "async.task.created", 
            task,
            "AsyncTaskManager"
        ));
        
        // Obter executor apropriado
        ExecutorService executor = threadPools.get(task.getType());
        
        // Executar tarefa
        CompletableFuture.runAsync(() -> {
            try {
                task.setStatus(TaskStatus.RUNNING);
                notifyTaskStarted(task);
                
                long startTime = System.currentTimeMillis();
                T result = task.task.get();
                long duration = System.currentTimeMillis() - startTime;
                
                task.setResult(result);
                future.complete(result);
                
                notifyTaskCompleted(task);
                
                // Log de performance
                if (enableMetrics) {
                    loggerManager.performance(task.getName(), duration, Map.of(
                        "taskId", task.getId(),
                        "taskType", task.getType().name(),
                        "priority", task.getPriority().name()
                    ));
                }
                
            } catch (Exception e) {
                task.setError(e);
                future.completeExceptionally(e);
                
                notifyTaskFailed(task, e);
                
                // Log de erro
                loggerManager.error(task.getName(), e, Map.of(
                    "taskId", task.getId(),
                    "taskType", task.getType().name()
                ));
                
            } finally {
                // Remover dos ativos
                activeTasks.remove(task.getId());
                taskListeners.remove(task.getId());
            }
            
        }, executor);
        
        return future;
    }
    
    /**
     * Executa tarefa simples
     */
    public <T> CompletableFuture<T> executeAsync(String name, TaskType type, Supplier<T> task) {
        return executeAsync(new AsyncTask<>(name, type, TaskPriority.NORMAL, task));
    }
    
    /**
     * Executa tarefa com prioridade
     */
    public <T> CompletableFuture<T> executeAsync(String name, TaskType type, TaskPriority priority, Supplier<T> task) {
        return executeAsync(new AsyncTask<>(name, type, priority, task));
    }
    
    /**
     * Executa tarefa com timeout
     */
    public <T> CompletableFuture<T> executeAsyncWithTimeout(AsyncTask<T> task, long timeoutMs) {
        CompletableFuture<T> future = executeAsync(task);
        
        return future.orTimeout(timeoutMs, TimeUnit.MILLISECONDS)
            .exceptionally(throwable -> {
                if (throwable instanceof TimeoutException) {
                    task.setStatus(TaskStatus.FAILED);
                    task.setError(new RuntimeException("Task timeout after " + timeoutMs + "ms"));
                    
                    loggerManager.error(task.getName() + " - Timeout", throwable, Map.of(
                        "taskId", task.getId(),
                        "timeoutMs", timeoutMs
                    ));
                    
                    throw new RuntimeException("Task timeout", throwable);
                }
                throw new RuntimeException(throwable);
            });
    }
    
    /**
     * Cancela tarefa
     */
    public boolean cancelTask(String taskId) {
        AsyncTask<?> task = activeTasks.get(taskId);
        if (task != null && task.getStatus() == TaskStatus.PENDING) {
            task.setStatus(TaskStatus.CANCELLED);
            activeTasks.remove(taskId);
            
            notifyTaskCancelled(task);
            
            return true;
        }
        return false;
    }
    
    /**
     * Obtém status da tarefa
     */
    public AsyncTask<?> getTask(String taskId) {
        return activeTasks.get(taskId);
    }
    
    /**
     * Lista todas as tarefas ativas
     */
    public List<AsyncTask<?>> getActiveTasks() {
        return new ArrayList<>(activeTasks.values());
    }
    
    /**
     * Obtém estatísticas
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Estatísticas gerais
        stats.put("activeTasks", activeTasks.size());
        stats.put("defaultPoolSize", defaultPoolSize);
        stats.put("defaultTimeout", defaultTimeout);
        stats.put("enableMetrics", enableMetrics);
        
        // Estatísticas por tipo
        Map<String, Object> typeStats = new HashMap<>();
        for (TaskType type : TaskType.values()) {
            ExecutorService executor = threadPools.get(type);
            if (executor instanceof ThreadPoolExecutor) {
                ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
                Map<String, Object> poolInfo = new HashMap<>();
                poolInfo.put("activeThreads", pool.getActiveCount());
                poolInfo.put("poolSize", pool.getPoolSize());
                poolInfo.put("queueSize", pool.getQueue().size());
                poolInfo.put("completedTasks", pool.getCompletedTaskCount());
                typeStats.put(type.name(), poolInfo);
            }
        }
        stats.put("threadPools", typeStats);
        
        // Estatísticas por status
        Map<String, Long> statusStats = new HashMap<>();
        for (AsyncTask<?> task : activeTasks.values()) {
            statusStats.merge(task.getStatus().name(), 1L, Long::sum);
        }
        stats.put("statusDistribution", statusStats);
        
        return stats;
    }
    
    /**
     * Adiciona listener de tarefa
     */
    public <T> void addTaskListener(String taskId, TaskListener<T> listener) {
        taskListeners.computeIfAbsent(taskId, k -> new ArrayList<>()).add(listener);
    }
    
    /**
     * Remove listener de tarefa
     */
    public void removeTaskListener(String taskId, TaskListener<?> listener) {
        List<TaskListener<?>> listeners = taskListeners.get(taskId);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }
    
    /**
     * Notifica listeners de início
     */
    @SuppressWarnings("unchecked")
    private <T> void notifyTaskStarted(AsyncTask<T> task) {
        List<TaskListener<?>> listeners = taskListeners.get(task.getId());
        if (listeners != null) {
            for (TaskListener<?> listener : listeners) {
                try {
                    ((TaskListener<T>) listener).onTaskStarted(task);
                } catch (Exception e) {
                    loggerManager.error("TaskListener error", e, Map.of(
                        "taskId", task.getId(),
                        "event", "onTaskStarted"
                    ));
                }
            }
        }
        
        eventSystem.publish(new com.br.hermescomercial.event.EventSystem.Event(
            "async.task.started", 
            task,
            "AsyncTaskManager"
        ));
    }
    
    /**
     * Notifica listeners de conclusão
     */
    @SuppressWarnings("unchecked")
    private <T> void notifyTaskCompleted(AsyncTask<T> task) {
        List<TaskListener<?>> listeners = taskListeners.get(task.getId());
        if (listeners != null) {
            for (TaskListener<?> listener : listeners) {
                try {
                    ((TaskListener<T>) listener).onTaskCompleted(task);
                } catch (Exception e) {
                    loggerManager.error("TaskListener error", e, Map.of(
                        "taskId", task.getId(),
                        "event", "onTaskCompleted"
                    ));
                }
            }
        }
        
        eventSystem.publish(new com.br.hermescomercial.event.EventSystem.Event(
            "async.task.completed", 
            task,
            "AsyncTaskManager"
        ));
    }
    
    /**
     * Notifica listeners de falha
     */
    @SuppressWarnings("unchecked")
    private <T> void notifyTaskFailed(AsyncTask<T> task, Throwable error) {
        List<TaskListener<?>> listeners = taskListeners.get(task.getId());
        if (listeners != null) {
            for (TaskListener<?> listener : listeners) {
                try {
                    ((TaskListener<T>) listener).onTaskFailed(task, error);
                } catch (Exception e) {
                    loggerManager.error("TaskListener error", e, Map.of(
                        "taskId", task.getId(),
                        "event", "onTaskFailed"
                    ));
                }
            }
        }
        
        eventSystem.publish(new com.br.hermescomercial.event.EventSystem.Event(
            "async.task.failed", 
            Map.of("task", task, "error", error.getMessage()),
            "AsyncTaskManager"
        ));
    }
    
    /**
     * Notifica listeners de cancelamento
     */
    @SuppressWarnings("unchecked")
    private <T> void notifyTaskCancelled(AsyncTask<T> task) {
        List<TaskListener<?>> listeners = taskListeners.get(task.getId());
        if (listeners != null) {
            for (TaskListener<?> listener : listeners) {
                try {
                    ((TaskListener<T>) listener).onTaskCancelled(task);
                } catch (Exception e) {
                    loggerManager.error("TaskListener error", e, Map.of(
                        "taskId", task.getId(),
                        "event", "onTaskCancelled"
                    ));
                }
            }
        }
        
        eventSystem.publish(new com.br.hermescomercial.event.EventSystem.Event(
            "async.task.cancelled", 
            task,
            "AsyncTaskManager"
        ));
    }
    
    /**
     * Shutdown do gerenciador
     */
    public void shutdown() {
        loggerManager.getLogger(AsyncTaskManager.class).info("Desligando AsyncTaskManager...");
        
        // Cancelar todas as tarefas ativas
        for (AsyncTask<?> task : activeTasks.values()) {
            if (task.getStatus() == TaskStatus.PENDING || task.getStatus() == TaskStatus.RUNNING) {
                task.setStatus(TaskStatus.CANCELLED);
            }
        }
        
        // Desligar thread pools
        for (ExecutorService executor : threadPools.values()) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        
        activeTasks.clear();
        taskListeners.clear();
        
        loggerManager.getLogger(AsyncTaskManager.class).info("AsyncTaskManager desligado com sucesso");
    }
}
