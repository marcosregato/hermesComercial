package com.br.hermescomercial.pdv.patterns;

import com.br.hermescomercial.util.SystemLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DAO Pattern - Data Access Object
 * Abstrai o acesso a dados e permite diferentes implementações de persistência
 */
public class DAOPattern {
    
    /**
     * Interface genérica para DAO
     */
    public interface GenericDAO<T, K> {
        T save(T entity);
        T update(T entity);
        boolean delete(K id);
        T findById(K id);
        List<T> findAll();
        List<T> findByCriteria(Map<String, Object> criteria);
        boolean exists(K id);
        long count();
        void clear();
    }
    
    /**
     * Implementação em memória para DAO
     */
    public static class InMemoryDAO<T, K> implements GenericDAO<T, K> {
        private final Map<K, T> dataStore;
        private final Class<T> entityClass;
        
        public InMemoryDAO(Class<T> entityClass) {
            this.entityClass = entityClass;
            this.dataStore = new ConcurrentHashMap<>();
            SystemLogger.ui("InMemoryDAO criado para: " + entityClass.getSimpleName());
        }
        
        @Override
        public T save(T entity) {
            try {
                K id = extractId(entity);
                if (id == null) {
                    id = generateId();
                    setId(entity, id);
                }
                dataStore.put(id, entity);
                SystemLogger.ui("Entidade salva: " + entityClass.getSimpleName() + " ID: " + id);
                return entity;
            } catch (Exception e) {
                SystemLogger.error("Erro ao salvar entidade: " + entityClass.getSimpleName(), e);
                throw new RuntimeException("Falha ao salvar entidade", e);
            }
        }
        
        @Override
        public T update(T entity) {
            try {
                K id = extractId(entity);
                if (id == null) {
                    throw new IllegalArgumentException("Entidade sem ID não pode ser atualizada");
                }
                dataStore.put(id, entity);
                SystemLogger.ui("Entidade atualizada: " + entityClass.getSimpleName() + " ID: " + id);
                return entity;
            } catch (Exception e) {
                SystemLogger.error("Erro ao atualizar entidade: " + entityClass.getSimpleName(), e);
                throw new RuntimeException("Falha ao atualizar entidade", e);
            }
        }
        
        @Override
        public boolean delete(K id) {
            try {
                T removed = dataStore.remove(id);
                if (removed != null) {
                    SystemLogger.ui("Entidade excluída: " + entityClass.getSimpleName() + " ID: " + id);
                    return true;
                }
                return false;
            } catch (Exception e) {
                SystemLogger.error("Erro ao excluir entidade: " + entityClass.getSimpleName(), e);
                throw new RuntimeException("Falha ao excluir entidade", e);
            }
        }
        
        @Override
        public T findById(K id) {
            try {
                return dataStore.get(id);
            } catch (Exception e) {
                SystemLogger.error("Erro ao buscar entidade por ID: " + entityClass.getSimpleName(), e);
                return null;
            }
        }
        
        @Override
        public List<T> findAll() {
            try {
                return new ArrayList<>(dataStore.values());
            } catch (Exception e) {
                SystemLogger.error("Erro ao listar todas entidades: " + entityClass.getSimpleName(), e);
                return new ArrayList<>();
            }
        }
        
        @Override
        public List<T> findByCriteria(Map<String, Object> criteria) {
            try {
                List<T> results = new ArrayList<>();
                for (T entity : dataStore.values()) {
                    if (matchesCriteria(entity, criteria)) {
                        results.add(entity);
                    }
                }
                SystemLogger.ui("Busca por critério: " + entityClass.getSimpleName() + " - " + results.size() + " resultados");
                return results;
            } catch (Exception e) {
                SystemLogger.error("Erro na busca por critério: " + entityClass.getSimpleName(), e);
                return new ArrayList<>();
            }
        }
        
        @Override
        public boolean exists(K id) {
            return dataStore.containsKey(id);
        }
        
        @Override
        public long count() {
            return dataStore.size();
        }
        
        @Override
        public void clear() {
            dataStore.clear();
            SystemLogger.ui("Armazenamento limpo: " + entityClass.getSimpleName());
        }
        
        @SuppressWarnings("unchecked")
        private K extractId(T entity) {
            try {
                java.lang.reflect.Field idField = entity.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                return (K) idField.get(entity);
            } catch (Exception e) {
                return null;
            }
        }
        
        private void setId(T entity, K id) {
            try {
                java.lang.reflect.Field idField = entity.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(entity, id);
            } catch (Exception e) {
                SystemLogger.error("Erro ao definir ID da entidade", e);
            }
        }
        
        @SuppressWarnings("unchecked")
        private K generateId() {
            try {
                if (Long.class.isAssignableFrom(entityClass.getDeclaredField("id").getType())) {
                    return (K) Long.valueOf(System.currentTimeMillis());
                } else if (Integer.class.isAssignableFrom(entityClass.getDeclaredField("id").getType())) {
                    return (K) Integer.valueOf((int) (System.currentTimeMillis() % Integer.MAX_VALUE));
                } else {
                    return (K) (Object) System.currentTimeMillis();
                }
            } catch (Exception e) {
                return (K) (Object) System.currentTimeMillis();
            }
        }
        
        private boolean matchesCriteria(T entity, Map<String, Object> criteria) {
            if (criteria == null || criteria.isEmpty()) {
                return true;
            }
            
            try {
                for (Map.Entry<String, Object> criterion : criteria.entrySet()) {
                    String fieldName = criterion.getKey();
                    Object expectedValue = criterion.getValue();
                    
                    java.lang.reflect.Field field = entity.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object actualValue = field.get(entity);
                    
                    if (expectedValue != null && !expectedValue.equals(actualValue)) {
                        return false;
                    }
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
    
    /**
     * Fábrica de DAOs
     */
    public static class DAOFactory {
        private static final Map<Class<?>, Object> daoInstances = new ConcurrentHashMap<>();
        
        @SuppressWarnings("unchecked")
        public static <T, K> GenericDAO<T, K> getDAO(Class<T> entityClass) {
            return (GenericDAO<T, K>) daoInstances.computeIfAbsent(entityClass, 
                clazz -> new InMemoryDAO<>(entityClass));
        }
        
        /**
         * Obtém estatísticas dos DAOs
         */
        public static Map<String, Object> getDAOStats() {
            Map<String, Object> stats = new ConcurrentHashMap<>();
            for (Map.Entry<Class<?>, Object> entry : daoInstances.entrySet()) {
                String className = entry.getKey().getSimpleName();
                InMemoryDAO<?, ?> dao = (InMemoryDAO<?, ?>) entry.getValue();
                stats.put(className + "_count", dao.count());
                stats.put(className + "_class", dao.entityClass.getSimpleName());
            }
            return stats;
        }
        
        /**
         * Limpa todos os DAOs
         */
        public static void clearAllDAOs() {
            for (Object dao : daoInstances.values()) {
                if (dao instanceof InMemoryDAO) {
                    ((InMemoryDAO<?, ?>) dao).clear();
                }
            }
            SystemLogger.ui("Todos os DAOs foram limpos");
        }
    }
    
    /**
     * Utilitários para operações comuns em DAOs
     */
    public static class DAOUtils {
        
        /**
         * Cria critérios de busca
         */
        public static Map<String, Object> createCriteria() {
            return new ConcurrentHashMap<>();
        }
        
        public static Map<String, Object> createCriteria(String field, Object value) {
            Map<String, Object> criteria = new ConcurrentHashMap<>();
            criteria.put(field, value);
            return criteria;
        }
        
        public static Map<String, Object> addCriteria(Map<String, Object> criteria, String field, Object value) {
            if (criteria == null) {
                criteria = new ConcurrentHashMap<>();
            }
            criteria.put(field, value);
            return criteria;
        }
        
        /**
         * Valida se uma entidade tem ID
         */
        public static boolean hasId(Object entity) {
            try {
                java.lang.reflect.Field idField = entity.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                return idField.get(entity) != null;
            } catch (Exception e) {
                return false;
            }
        }
        
        /**
         * Obtém o ID de uma entidade
         */
        public static Object getId(Object entity) {
            try {
                java.lang.reflect.Field idField = entity.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                return idField.get(entity);
            } catch (Exception e) {
                return null;
            }
        }
        
        /**
         * Define o ID de uma entidade
         */
        public static void setId(Object entity, Object id) {
            try {
                java.lang.reflect.Field idField = entity.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(entity, id);
            } catch (Exception e) {
                SystemLogger.error("Erro ao definir ID da entidade", e);
            }
        }
        
        /**
         * Compara duas entidades por ID
         */
        public static boolean sameId(Object entity1, Object entity2) {
            Object id1 = getId(entity1);
            Object id2 = getId(entity2);
            if (id1 == null || id2 == null) {
                return false;
            }
            return id1.equals(id2);
        }
    }
}
