package com.br.hermescomercial.injection;

import com.br.hermescomercial.repository.*;
import com.br.hermescomercial.repository.impl.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Container de Injeção de Dependências
 * Implementa injeção com suporte a singleton, scoped e transient
 * Segue padrões de arquitetura definidos em docs/ARQUITETURA.md
 */
public class DependencyContainer {
    
    private static final Logger logger = LogManager.getLogger(DependencyContainer.class);
    private static volatile DependencyContainer instance;
    private static final Object lock = new Object();
    
    // Tipos de ciclo de vida
    public enum Lifetime {
        SINGLETON,    // Uma instância por container
        TRANSIENT     // Nova instância a cada requisição
    }
    
    // Registro de dependências
    private final Map<Class<?>, DependencyDescriptor<?>> dependencies = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> singletonInstances = new ConcurrentHashMap<>();
    
    // Interface para callbacks
    public interface DependencyFactory<T> {
        T create(DependencyContainer container);
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
    private DependencyContainer() {
        configureCoreServices();
    }
    
    /**
     * Obtém instância singleton do container
     */
    public static DependencyContainer getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DependencyContainer();
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
        logger.debug("Registrado singleton: {} -> {}", interfaceType.getSimpleName(), implementationType.getSimpleName());
    }
    
    /**
     * Registra uma dependência com ciclo de vida transient
     */
    public <T> void registerTransient(Class<T> interfaceType, Class<? extends T> implementationType) {
        dependencies.put(interfaceType, new DependencyDescriptor<>(Lifetime.TRANSIENT, interfaceType, implementationType));
        logger.debug("Registrado transient: {} -> {}", interfaceType.getSimpleName(), implementationType.getSimpleName());
    }
    
    /**
     * Registra uma dependência com factory personalizado
     */
    public <T> void registerFactory(Class<T> interfaceType, DependencyFactory<T> factory, Lifetime lifetime) {
        dependencies.put(interfaceType, new DependencyDescriptor<>(lifetime, interfaceType, factory));
        logger.debug("Registrado factory: {} (lifetime: {})", interfaceType.getSimpleName(), lifetime);
    }
    
    /**
     * Registra uma dependência com supplier personalizado
     */
    public <T> void registerSupplier(Class<T> interfaceType, Supplier<T> supplier, Lifetime lifetime) {
        dependencies.put(interfaceType, new DependencyDescriptor<>(lifetime, interfaceType, supplier));
        logger.debug("Registrado supplier: {} (lifetime: {})", interfaceType.getSimpleName(), lifetime);
    }
    
    /**
     * Resolve uma dependência
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        DependencyDescriptor<T> descriptor = (DependencyDescriptor<T>) dependencies.get(type);
        
        if (descriptor == null) {
            throw new DependencyNotFoundException("Dependência não registrada: " + type.getName());
        }
        
        return createInstance(descriptor);
    }
    
    /**
     * Resolve uma dependência opcional
     */
    public <T> java.util.Optional<T> getOptional(Class<T> type) {
        try {
            return java.util.Optional.of(get(type));
        } catch (DependencyNotFoundException e) {
            return java.util.Optional.empty();
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
    private <T> T createInstanceWithInjection(Class<? extends T> type) {
        try {
            // Busca construtor sem parâmetros
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new DependencyCreationException("Falha na criação de instância para: " + type.getName(), e);
        }
    }
    
    /**
     * Configura serviços core do sistema
     */
    private void configureCoreServices() {
        logger.info("Configurando serviços core do container");
        
        // Repositories Singleton - implementações movidas para repository/impl/
        registerSingleton(ClienteRepository.class, ClienteRepositoryImpl.class);
        registerSingleton(ProdutoRepository.class, ProdutoRepositoryImpl.class);
        registerSingleton(UsuarioRepository.class, UsuarioRepositoryImpl.class);
        
        logger.info("Serviços core configurados com sucesso");
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
    public java.util.Set<Class<?>> getRegisteredDependencies() {
        return new java.util.HashSet<>(dependencies.keySet());
    }
    
    /**
     * Limpa todas as dependências (para testes)
     */
    public void clear() {
        logger.warn("Limpando todas as dependências do container");
        dependencies.clear();
        singletonInstances.clear();
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
