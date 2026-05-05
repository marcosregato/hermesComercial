package com.br.hermescomercial.injection;

import com.br.hermescomercial.repository.*;
import com.br.hermescomercial.dao.*;
import com.br.hermescomercial.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Container simples de injeção de dependências
 * Implementa um contêiner básico para gerenciar dependências
 */
public class DependencyContainer {
    
    private static final Logger logger = LogManager.getLogger(DependencyContainer.class);
    private static DependencyContainer instance;
    private final Map<Class<?>, Object> dependencies = new HashMap<>();
    
    private DependencyContainer() {
        initializeDependencies();
    }
    
    /**
     * Obtém a instância singleton do container
     * @return Instância do container
     */
    public static synchronized DependencyContainer getInstance() {
        if (instance == null) {
            instance = new DependencyContainer();
        }
        return instance;
    }
    
    /**
     * Inicializa as dependências do sistema
     */
    private void initializeDependencies() {
        logger.info("Inicializando container de dependências");
        
        // Registrando implementações dos repositories
        ClienteRepository clienteRepository = new ClienteRepositoryImpl();
        ProdutoRepository produtoRepository = new ProdutoRepositoryImpl();
        UsuarioRepository usuarioRepository = new UsuarioRepositoryImpl();
        
        dependencies.put(ClienteRepository.class, clienteRepository);
        dependencies.put(ProdutoRepository.class, produtoRepository);
        dependencies.put(UsuarioRepository.class, usuarioRepository);
        
        logger.info("Dependências inicializadas com sucesso");
    }
    
    /**
     * Obtém uma dependência pelo tipo
     * @param <T> Tipo da dependência
     * @param type Classe da dependência
     * @return Instância da dependência
     * @throws IllegalArgumentException Se a dependência não for encontrada
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        Object dependency = dependencies.get(type);
        if (dependency == null) {
            throw new IllegalArgumentException("Dependência não encontrada: " + type.getName());
        }
        return (T) dependency;
    }
    
    /**
     * Registra uma dependência manualmente
     * @param <T> Tipo da dependência
     * @param type Classe da dependência
     * @param implementation Implementação da dependência
     */
    public <T> void register(Class<T> type, T implementation) {
        logger.debug("Registrando dependência: {}", type.getName());
        dependencies.put(type, implementation);
    }
    
    /**
     * Limpa todas as dependências (para testes)
     */
    public void clear() {
        logger.warn("Limpando todas as dependências do container");
        dependencies.clear();
    }
}

/**
 * Implementação do ClienteRepository usando ClienteDao existente
 */
class ClienteRepositoryImpl implements ClienteRepository {
    
    private final ClienteDao clienteDao = new ClienteDao();
    
    @Override
    public boolean salvar(Cliente cliente) {
        try {
            return clienteDao.salvar(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar cliente", e);
        }
    }
    
    @Override
    public boolean atualizar(Cliente cliente) {
        try {
            return clienteDao.update(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar cliente", e);
        }
    }
    
    @Override
    public boolean excluir(Long id) {
        try {
            // ClienteDao não tem método excluir por ID, implementar workaround
            Cliente cliente = clienteDao.buscarPorId(id);
            if (cliente != null) {
                return clienteDao.remove(cliente.getNome());
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir cliente", e);
        }
    }
    
    @Override
    public Cliente buscarPorId(Long id) {
        try {
            return clienteDao.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar cliente por ID", e);
        }
    }
    
    @Override
    public java.util.List<Cliente> listar() {
        try {
            return clienteDao.listar();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar clientes", e);
        }
    }
    
    @Override
    public java.util.List<Cliente> buscarComFiltros(String nome, boolean ativos, boolean inativos, boolean ordenarPorNome) {
        try {
            return clienteDao.buscarComFiltros(nome, ativos, inativos, ordenarPorNome);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar clientes com filtros", e);
        }
    }
}

/**
 * Implementação do ProdutoRepository usando ProdutoDao existente
 */
class ProdutoRepositoryImpl implements ProdutoRepository {
    
    private final ProdutoDao produtoDao = new ProdutoDao();
    
    @Override
    public boolean salvar(Produto produto) {
        try {
            return produtoDao.salvar(produto);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar produto", e);
        }
    }
    
    @Override
    public boolean atualizar(Produto produto) {
        try {
            return produtoDao.update(produto);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar produto", e);
        }
    }
    
    @Override
    public boolean remover(Long id) {
        try {
            // ProdutoDao não tem método excluir por ID, implementar workaround
            List<Produto> produtos = produtoDao.listar();
            for (Produto produto : produtos) {
                if (produto.getId().equals(id)) {
                    return produtoDao.remove(produto.getNome());
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover produto", e);
        }
    }
    
    @Override
    public boolean excluir(String nome) {
        try {
            return produtoDao.remove(nome);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir produto", e);
        }
    }
    
    @Override
    public java.util.Optional<Produto> buscarPorId(Long id) {
        try {
            // ProdutoDao não tem método buscarPorId, implementar workaround
            List<Produto> produtos = produtoDao.listar();
            for (Produto produto : produtos) {
                if (produto.getId().equals(id)) {
                    return java.util.Optional.of(produto);
                }
            }
            return java.util.Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produto por ID", e);
        }
    }
    
    @Override
    public Produto buscarPorCodigoBarras(String codigoBarras) {
        try {
            return produtoDao.buscarPorCodigoBarras(codigoBarras);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produto por código de barras", e);
        }
    }
    
    @Override
    public java.util.List<Produto> buscarTodos() {
        try {
            return produtoDao.listar();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar produtos", e);
        }
    }
    
    @Override
    public java.util.List<Produto> listar() {
        try {
            return produtoDao.listar();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar produtos", e);
        }
    }
    
    @Override
    public java.util.List<Produto> buscarComFiltros(String nome, String categoria, String subCategoria, 
                                                   String codigoBarras, java.math.BigDecimal precoMin, java.math.BigDecimal precoMax, 
                                                   Integer estoqueMin, boolean ativos, boolean inativos) {
        try {
            return produtoDao.buscarComFiltros(nome, categoria, subCategoria, codigoBarras, 
                                              precoMin, precoMax, estoqueMin, ativos, inativos);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produtos com filtros", e);
        }
    }
    
    @Override
    public java.util.List<Produto> buscarPorNome(String nome) {
        try {
            List<Produto> todos = produtoDao.listar();
            List<Produto> filtrados = new ArrayList<>();
            for (Produto produto : todos) {
                if (produto.getNome() != null && produto.getNome().toLowerCase().contains(nome.toLowerCase())) {
                    filtrados.add(produto);
                }
            }
            return filtrados;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produtos por nome", e);
        }
    }
    
    @Override
    public java.util.List<Produto> buscarPorCategoria(String categoria) {
        try {
            List<Produto> todos = produtoDao.listar();
            List<Produto> filtrados = new ArrayList<>();
            for (Produto produto : todos) {
                if (produto.getCategoria() != null && produto.getCategoria().equalsIgnoreCase(categoria)) {
                    filtrados.add(produto);
                }
            }
            return filtrados;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produtos por categoria", e);
        }
    }
    
    @Override
    public java.util.List<Produto> buscarEstoqueBaixo() {
        try {
            List<Produto> todos = produtoDao.listar();
            List<Produto> filtrados = new ArrayList<>();
            for (Produto produto : todos) {
                // getEstoque() e getEstoqueMinimo() retornam int, não podem ser null
                if (produto.getEstoque() <= produto.getEstoqueMinimo()) {
                    filtrados.add(produto);
                }
            }
            return filtrados;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produtos com estoque baixo", e);
        }
    }
    
    @Override
    public boolean atualizarEstoque(Long id, int novaQuantidade) {
        try {
            List<Produto> produtos = produtoDao.listar();
            for (Produto produto : produtos) {
                if (produto.getId().equals(id)) {
                    produto.setEstoque(novaQuantidade);
                    return produtoDao.update(produto);
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar estoque do produto", e);
        }
    }

    @Override
    public java.util.List<Produto> buscarPorFaixaPreco(java.math.BigDecimal precoMin, java.math.BigDecimal precoMax) {
        try {
            List<Produto> todos = produtoDao.listar();
            List<Produto> filtrados = new ArrayList<>();
            for (Produto produto : todos) {
                if (produto.getPrecoVenda() != null && 
                    produto.getPrecoVenda().compareTo(precoMin) >= 0 && 
                    produto.getPrecoVenda().compareTo(precoMax) <= 0) {
                    filtrados.add(produto);
                }
            }
            return filtrados;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produtos por faixa de preço", e);
        }
    }
    
    @Override
    public java.util.List<Produto> buscarComEstoqueBaixo(int limite) {
        try {
            List<Produto> todos = produtoDao.listar();
            List<Produto> filtrados = new ArrayList<>();
            for (Produto produto : todos) {
                if (produto.getEstoque() < limite) {
                    filtrados.add(produto);
                }
            }
            return filtrados;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produtos com estoque baixo", e);
        }
    }
    
    @Override
    public java.util.List<Produto> buscarPorCodigo(String codigo) {
        try {
            List<Produto> todos = produtoDao.listar();
            List<Produto> filtrados = new ArrayList<>();
            for (Produto produto : todos) {
                if (produto.getCodigoBarras() != null && 
                    produto.getCodigoBarras().equals(codigo)) {
                    filtrados.add(produto);
                }
            }
            return filtrados;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produtos por código", e);
        }
    }
}

/**
 * Implementação do UsuarioRepository usando UsuarioDao existente
 */
class UsuarioRepositoryImpl implements UsuarioRepository {
    
    private final UsuarioDao usuarioDao = new UsuarioDao();
    
    @Override
    public void salvar(Usuario usuario) {
        try {
            usuarioDao.salvar(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar usuário", e);
        }
    }
    
    @Override
    public void remove(String nome) {
        try {
            usuarioDao.remove(nome);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover usuário", e);
        }
    }
    
    @Override
    public java.util.List<Usuario> listar() {
        try {
            return usuarioDao.listar();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar usuários", e);
        }
    }
    
    @Override
    public java.util.List<Usuario> buscarPorNome(String nome) {
        try {
            // UsuarioDao não tem método buscarPorNome genérico, usar buscarTodos e filtrar
            return usuarioDao.buscarTodos().stream()
                .filter(u -> u.getNome() != null && u.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuários por nome", e);
        }
    }
    
    @Override
    public java.util.List<Usuario> buscarPorCpfCnpj(String textoBusca) {
        try {
            // UsuarioDao não tem método buscarPorCpfCnpj genérico, usar buscarTodos e filtrar
            return usuarioDao.buscarTodos().stream()
                .filter(u -> u.getNumeroDocumeto() != null && u.getNumeroDocumeto().contains(textoBusca))
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuários por CPF/CNPJ", e);
        }
    }
    
    @Override
    public java.util.List<Usuario> buscarClientePorNome(String nome) {
        try {
            return usuarioDao.buscarClientePorNome(nome);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar clientes por nome", e);
        }
    }
    
    @Override
    public java.util.List<Usuario> buscarClientePorCpfCnpj(String textoBusca) {
        try {
            return usuarioDao.buscarClientePorCpfCnpj(textoBusca);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar clientes por CPF/CNPJ", e);
        }
    }
    
    @Override
    public java.util.List<Usuario> buscarClientePorNomeCpfCnpj(String textoBusca) {
        try {
            return usuarioDao.buscarClientePorNomeCpfCnpj(textoBusca);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar clientes por Nome/CPF/CNPJ", e);
        }
    }
    
    @Override
    public java.util.List<Usuario> buscarTodos() {
        try {
            return usuarioDao.buscarTodos();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar todos os usuários", e);
        }
    }
}
