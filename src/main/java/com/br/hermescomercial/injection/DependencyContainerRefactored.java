package com.br.hermescomercial.injection;

import com.br.hermescomercial.repository.*;
import com.br.hermescomercial.service.*;
import com.br.hermescomercial.event.EventSystem;
import com.br.hermescomercial.config.ConfigurationManager;
import com.br.hermescomercial.cache.CacheManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Container de Injeção de Dependências Refatorado
 * Implementa injeção robusta com suporte a singleton, scoped e transient
 * Versão 2.0.0 - Refactoring Implementation
 */
public class DependencyContainerRefactored {
    
    private static volatile DependencyContainerRefactored instance;
    private static final Object lock = new Object();
    
    // Tipos de ciclo de vida
    public enum Lifetime {
        SINGLETON,    // Uma instância por container
        SCOPED,       // Uma instância por scope/request
        TRANSIENT     // Nova instância a cada requisição
    }
    
    // Registro de dependências
    private final Map<Class<?>, DependencyDescriptor<?>> dependencies = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> singletonInstances = new ConcurrentHashMap<>();
    private final ThreadLocal<Map<Class<?>, Object>> scopedInstances = ThreadLocal.withInitial(HashMap::new);
    
    // Interfaces para callbacks
    public interface DependencyFactory<T> {
        T create(DependencyContainerRefactored container);
    }
    
    // Descriptor de dependência
    private static class DependencyDescriptor<T> {
        final Lifetime lifetime;
        final Class<T> interfaceType;
        final Class<? extends T> implementationType;
        final DependencyFactory<T> factory;
        final Supplier<T> supplier;
        
        DependencyDescriptor(Lifetime lifetime, Class<T> interfaceType, 
                           Class<? extends T> implementationType) {
            this.lifetime = lifetime;
            this.interfaceType = interfaceType;
            this.implementationType = implementationType;
            this.factory = null;
            this.supplier = null;
        }
        
        DependencyDescriptor(Lifetime lifetime, Class<T> interfaceType, 
                           DependencyFactory<T> factory) {
            this.lifetime = lifetime;
            this.interfaceType = interfaceType;
            this.implementationType = null;
            this.factory = factory;
            this.supplier = null;
        }
        
        DependencyDescriptor(Lifetime lifetime, Class<T> interfaceType, 
                           Supplier<T> supplier) {
            this.lifetime = lifetime;
            this.interfaceType = interfaceType;
            this.implementationType = null;
            this.factory = null;
            this.supplier = supplier;
        }
    }
    
    // Construtor privado para Singleton
    private DependencyContainerRefactored() {
        configureCoreServices();
    }
    
    /**
     * Obtém instância singleton do container
     */
    public static DependencyContainerRefactored getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DependencyContainerRefactored();
                }
            }
        }
        return instance;
    }
    
    /**
     * Registra uma dependência com ciclo de vida singleton
     */
    public <T> void registerSingleton(Class<T> interfaceType, Class<? extends T> implementationType) {
        dependencies.put(interfaceType, new DependencyDescriptor<>(Lifetime.SINGLETON, interfaceType, implementationType));
    }
    
    /**
     * Registra uma dependência com ciclo de vida scoped
     */
    public <T> void registerScoped(Class<T> interfaceType, Class<? extends T> implementationType) {
        dependencies.put(interfaceType, new DependencyDescriptor<>(Lifetime.SCOPED, interfaceType, implementationType));
    }
    
    /**
     * Registra uma dependência com ciclo de vida transient
     */
    public <T> void registerTransient(Class<T> interfaceType, Class<? extends T> implementationType) {
        dependencies.put(interfaceType, new DependencyDescriptor<>(Lifetime.TRANSIENT, interfaceType, implementationType));
    }
    
    /**
     * Registra uma dependência com factory personalizado
     */
    public <T> void registerFactory(Class<T> interfaceType, DependencyFactory<T> factory, Lifetime lifetime) {
        dependencies.put(interfaceType, new DependencyDescriptor<>(lifetime, interfaceType, factory));
    }
    
    /**
     * Registra uma dependência com supplier personalizado
     */
    public <T> void registerSupplier(Class<T> interfaceType, Supplier<T> supplier, Lifetime lifetime) {
        dependencies.put(interfaceType, new DependencyDescriptor<>(lifetime, interfaceType, supplier));
    }
    
    /**
     * Resolve uma dependência
     */
    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        DependencyDescriptor<T> descriptor = (DependencyDescriptor<T>) dependencies.get(type);
        
        if (descriptor == null) {
            throw new DependencyNotFoundException("Dependência não registrada: " + type.getName());
        }
        
        return createInstance(descriptor);
    }
    
    /**
     * Resolve uma dependência opcional
     */
    public <T> Optional<T> resolveOptional(Class<T> type) {
        try {
            return Optional.of(resolve(type));
        } catch (DependencyNotFoundException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Cria instância baseada no descriptor
     */
    @SuppressWarnings("unchecked")
    private <T> T createInstance(DependencyDescriptor<T> descriptor) {
        switch (descriptor.lifetime) {
            case SINGLETON:
                return (T) singletonInstances.computeIfAbsent(descriptor.interfaceType, k -> {
                    return createNewInstance(descriptor);
                });
            case SCOPED:
                return (T) scopedInstances.get().computeIfAbsent(descriptor.interfaceType, k -> {
                    return createNewInstance(descriptor);
                });
            case TRANSIENT:
                return createNewInstance(descriptor);
            default:
                throw new IllegalStateException("Lifetime não suportado: " + descriptor.lifetime);
        }
    }
    
    /**
     * Cria nova instância da dependência
     */
    private <T> T createNewInstance(DependencyDescriptor<T> descriptor) {
        try {
            if (descriptor.supplier != null) {
                return (T) descriptor.supplier.get();
            }
            
            if (descriptor.factory != null) {
                return (T) descriptor.factory.create(this);
            }
            
            if (descriptor.implementationType != null) {
                return createInstanceWithInjection(descriptor.implementationType);
            }
            
            throw new DependencyCreationException("Não foi possível criar instância para: " + descriptor.interfaceType.getName());
        } catch (Exception e) {
            throw new DependencyCreationException("Erro ao criar dependência: " + descriptor.interfaceType.getName(), e);
        }
    }
    
    /**
     * Cria instância com injeção de construtor
     */
    @SuppressWarnings("unchecked")
    private <T> T createInstanceWithInjection(Class<? extends T> type) {
        try {
            // Busca construtor com mais parâmetros
            Constructor<?>[] constructors = type.getConstructors();
            Constructor<?> bestConstructor = null;
            int maxParams = -1;
            
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() > maxParams) {
                    maxParams = constructor.getParameterCount();
                    bestConstructor = constructor;
                }
            }
            
            if (bestConstructor == null) {
                return type.getDeclaredConstructor().newInstance();
            }
            
            // Injeta dependências nos parâmetros do construtor
            Class<?>[] parameterTypes = bestConstructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];
            
            for (int i = 0; i < parameterTypes.length; i++) {
                parameters[i] = resolve((Class<?>) parameterTypes[i]);
            }
            
            return (T) bestConstructor.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | 
                 InvocationTargetException | NoSuchMethodException e) {
            throw new DependencyCreationException("Falha na injeção de construtor para: " + type.getName(), e);
        }
    }
    
    /**
     * Configura serviços core do sistema
     */
    private void configureCoreServices() {
        // Services Singleton - usando instâncias existentes
        registerSingleton(EventSystem.class, EventSystem.class);
        registerSingleton(CacheManager.class, CacheManager.class);
        registerSingleton(ConfigurationManager.class, ConfigurationManager.class);
        
        // Repositories Singleton
        registerSingleton(ProdutoRepository.class, ProdutoRepositoryImpl.class);
        
        // Services Transient (nova instância a cada requisição)
        registerTransient(ProdutoService.class, ProdutoService.class);
        
        // Services com factory personalizado - simplificados
        registerSingleton(DatabaseService.class, DatabaseService.class);
        registerSingleton(NotificacaoService.class, NotificacaoService.class);
    }
    
    /**
     * Limpa instâncias scoped
     */
    public void clearScoped() {
        scopedInstances.get().clear();
    }
    
    /**
     * Verifica se uma dependência está registrada
     */
    public boolean isRegistered(Class<?> type) {
        return dependencies.containsKey(type);
    }
    
    /**
     * Lista todas as dependências registradas
     */
    public Set<Class<?>> getRegisteredDependencies() {
        return new HashSet<>(dependencies.keySet());
    }
    
    /**
     * Executa uma ação dentro de um scope
     */
    public <T> T executeInScope(Supplier<T> action) {
        try {
            clearScoped();
            return action.get();
        } finally {
            clearScoped();
        }
    }
    
    // Exceções personalizadas
    public static class DependencyNotFoundException extends RuntimeException {
        public DependencyNotFoundException(String message) {
            super(message);
        }
    }
    
    public static class DependencyCreationException extends RuntimeException {
        public DependencyCreationException(String message) {
            super(message);
        }
        
        public DependencyCreationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
