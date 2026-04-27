-- =====================================================
-- HERMES COMERCIAL PDV v2.0 - INSERÇÃO DE PRODUTOS
-- Versão: 2.0 - Dados Completos
-- Data: 2026-04-26
-- Total: 20 produtos variados
-- =====================================================

-- =====================================================
-- INSERÇÃO DE PRODUTOS COMPLETOS
-- =====================================================

INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes) VALUES
-- Informática
('016', 'Notebook Samsung Book E50', 2899.00, 8, 'Informática', 'Notebook Intel Celeron, 4GB RAM, 256GB SSD, 15.6"'),
('017', 'All-in-One HP 22" Touch', 4299.00, 5, 'Informática', 'Computador All-in-One com tela touch 22", Intel Core i3, 8GB RAM, 1TB HD'),

-- Periféricos
('018', 'Teclado Wireless Microsoft', 120.00, 35, 'Periféricos', 'Teclado sem fio compacto com layout ABNT2'),
('019', 'Mouse Gamer RGB Redragon', 75.00, 60, 'Periféricos', 'Mouse gamer com iluminação RGB, 7200 DPI'),

-- Monitores
('020', 'Monitor Curvo 27" LG UltraWide', 1899.00, 4, 'Monitores', 'Monitor curvo 27" UltraWide 2560x1080, 75Hz'),
('021', 'Monitor Gamer 24" 144Hz', 1299.00, 7, 'Monitores', 'Monitor gaming 24" Full HD 144Hz, 1ms response'),

-- Acessórios
('022', 'Microfone de Mesa USB', 89.00, 25, 'Acessórios', 'Microfone condenser com cancelamento de ruído'),
('023', 'Iluminador de Mesa LED', 55.00, 40, 'Acessórios', 'Luminador ajustável com 3 modos de luz'),

-- Móveis
('024', 'Suporte para Notebook Portátil', 65.00, 30, 'Móveis', 'Suporte ajustável para notebooks até 17"'),
('025', 'Gaveteiro para Escritório', 110.00, 12, 'Móveis', 'Gaveteiro móvel com 3 gavetas'),

-- Rede
('026', 'Placa de Rede WiFi USB', 45.00, 50, 'Rede', 'Adaptador WiFi USB 600Mbps dual band'),
('027', 'Cabo Ethernet Cat6 10m', 25.00, 80, 'Rede', 'Cabo de rede Cat6 blindado, 10 metros'),

-- Armazenamento
('028', 'Pendrive 128GB USB 3.0', 35.00, 100, 'Armazenamento', 'Pendrive metálico 128GB USB 3.0'),
('029', 'HD Externo Portátil 2TB', 220.00, 18, 'Armazenamento', 'HD externo portátil 2TB USB 3.0'),

-- Software
('030', 'Windows 11 Pro Original', 450.00, 15, 'Software', 'Licença Windows 11 Pro original'),
('031', 'Office 365 Personal', 290.00, 25, 'Software', 'Assinatura anual Office 365 Personal'),

-- Impressão
('032', 'Impressora Multifuncional HP', 399.00, 6, 'Impressão', 'Impressora multifuncional jato de tinta colorida'),
('033', 'Cartucho de Tinta HP Preto', 45.00, 45, 'Impressão', 'Cartucho de tinta preto para impressora HP'),

-- Gamer
('034', 'Controle Xbox Wireless', 280.00, 20, 'Gamer', 'Controle Xbox Series X sem fio Bluetooth'),
('035', 'Capture Card Elgato', 650.00, 8, 'Gamer', 'Placa de captura 1080p 60fps para streaming')

ON CONFLICT (codigo) DO NOTHING;

-- =====================================================
-- INSERÇÃO DE CLIENTES ADICIONAIS
-- =====================================================

INSERT INTO cliente (nome, cpf_cnpj, email, telefone, endereco) VALUES
('Lucas Mendes', '65432198701', 'lucas.mendes@email.com', '(19) 98876-5432', 'Rua Amazonas, 789 - Campinas/SP'),
('Juliana Alves', '98712365409', 'juliana.alves@email.com', '(47) 97765-4321', 'Rua XV de Novembro, 123 - Florianópolis/SC'),
('Ricardo Dias', '32198765432', 'ricardo.dias@email.com', '(85) 96654-3210', 'Avenida Beira-Mar, 456 - Fortaleza/CE'),
('Fernanda Lima', '78965432109', 'fernanda.lima@email.com', '(51) 95543-2109', 'Rua dos Andradas, 789 - Porto Alegre/RS'),
('Marcos Pereira', '45678912301', 'marcos.pereira@email.com', '(71) 94432-1098', 'Rua da Bahia, 321 - Salvador/BA')

ON CONFLICT (cpf_cnpj) DO NOTHING;

-- =====================================================
-- INSERÇÃO DE USUÁRIOS ADICIONAIS
-- =====================================================

INSERT INTO usuario (nome, login, senha, email, perfil) VALUES
('Gerente Loja', 'gerente', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj6ukx.LrUpm', 'gerente@hermescomercial.com', 'GERENTE'),
('Operador Caixa', 'caixa', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj6ukx.LrUpm', 'caixa@hermescomercial.com', 'OPERADOR'),
('Estoque', 'estoque', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj6ukx.LrUpm', 'estoque@hermescomercial.com', 'USER')

ON CONFLICT (login) DO NOTHING;

-- =====================================================
-- INSERÇÃO DE VENDAS DE EXEMPLO
-- =====================================================

INSERT INTO venda (cliente_id, valor_total, forma_pagamento, status, observacoes, usuario_venda) VALUES
(1, 3589.90, 'Cartão Crédito', 'CONCLUIDA', 'Venda de notebook e acessórios', 'admin'),
(2, 1250.00, 'Dinheiro', 'CONCLUIDA', 'Compra à vista com desconto', 'admin'),
(3, 899.00, 'PIX', 'CONCLUIDA', 'Pagamento via PIX', 'gerente'),
(4, 450.00, 'Cartão Débito', 'CONCLUIDA', 'Venda de cadeira gamer', 'caixa'),
(5, 1899.00, 'Cartão Crédito', 'CONCLUIDA', 'Monitor UltraWide premium', 'admin')

ON CONFLICT (id) DO NOTHING;

-- =====================================================
-- INSERÇÃO DE ITENS DAS VENDAS
-- =====================================================

INSERT INTO venda_item (venda_id, produto_codigo, quantidade, preco_unitario, subtotal) VALUES
-- Venda 1 - Cliente João Silva
(1, '016', 1, 2899.00, 2899.00),
(1, '018', 1, 120.00, 120.00),
(1, '019', 1, 75.00, 75.00),
(1, '028', 1, 35.00, 35.00),
(1, '033', 1, 45.00, 45.00),

-- Venda 2 - Cliente Maria Santos
(2, '020', 1, 1899.00, 1899.00),

-- Venda 3 - Cliente Pedro Oliveira
(3, '021', 1, 1299.00, 1299.00),

-- Venda 4 - Cliente Ana Costa
(4, '006', 1, 450.00, 450.00),

-- Venda 5 - Cliente Carlos Ferreira
(5, '017', 1, 4299.00, 4299.00)

ON CONFLICT (id) DO NOTHING;

-- =====================================================
-- ATUALIZAÇÃO DE ESTOQUE APÓS VENDAS
-- =====================================================

-- Atualizar estoque dos produtos vendidos
UPDATE produto SET estoque = estoque - 1 WHERE codigo = '016'; -- Notebook Samsung
UPDATE produto SET estoque = estoque - 1 WHERE codigo = '018'; -- Teclado Microsoft
UPDATE produto SET estoque = estoque - 1 WHERE codigo = '019'; -- Mouse Gamer
UPDATE produto SET estoque = estoque - 1 WHERE codigo = '028'; -- Pendrive
UPDATE produto SET estoque = estoque - 1 WHERE codigo = '033'; -- Cartucho HP
UPDATE produto SET estoque = estoque - 1 WHERE codigo = '020'; -- Monitor Curvo
UPDATE produto SET estoque = estoque - 1 WHERE codigo = '021'; -- Monitor Gamer
UPDATE produto SET estoque = estoque - 1 WHERE codigo = '006'; -- Cadeira Gamer
UPDATE produto SET estoque = estoque - 1 WHERE codigo = '017'; -- All-in-One HP

-- =====================================================
-- ESTATÍSTICAS APÓS INSERÇÃO
-- =====================================================

-- Mostrar total de produtos por categoria
SELECT 
    p.categoria,
    COUNT(*) as total_produtos,
    SUM(p.estoque) as estoque_total,
    ROUND(SUM(p.preco * p.estoque), 2) as valor_total_estoque
FROM produto p
WHERE p.ativo = TRUE
GROUP BY p.categoria
ORDER BY valor_total_estoque DESC;

-- Mostrar produtos com estoque baixo
SELECT 
    codigo,
    descricao,
    categoria,
    estoque,
    preco
FROM produto 
WHERE estoque <= 10 AND ativo = TRUE
ORDER BY estoque ASC, descricao;

-- Mostrar resumo de vendas
SELECT 
    COUNT(*) as total_vendas,
    SUM(valor_total) as valor_total_vendido,
    AVG(valor_total) as ticket_medio,
    COUNT(DISTINCT cliente_id) as clientes_distintos
FROM venda 
WHERE status = 'CONCLUIDA';

-- Mostrar produtos mais vendidos
SELECT 
    p.codigo,
    p.descricao,
    p.categoria,
    COALESCE(SUM(vi.quantidade), 0) as total_vendido,
    COALESCE(SUM(vi.subtotal), 0) as valor_total
FROM produto p
LEFT JOIN venda_item vi ON p.codigo = vi.produto_codigo
LEFT JOIN venda v ON vi.venda_id = v.id AND v.status = 'CONCLUIDA'
WHERE p.ativo = TRUE
GROUP BY p.codigo, p.descricao, p.categoria
ORDER BY total_vendido DESC, valor_total DESC;

-- =====================================================
-- RESUMO FINAL
-- =====================================================

-- Total de registros em cada tabela
SELECT 
    'produto' as tabela, COUNT(*) as total_registros FROM produto
UNION ALL
SELECT 
    'categoria' as tabela, COUNT(*) as total_registros FROM categoria
UNION ALL
SELECT 
    'cliente' as tabela, COUNT(*) as total_registros FROM cliente
UNION ALL
SELECT 
    'venda' as tabela, COUNT(*) as total_registros FROM venda
UNION ALL
SELECT 
    'venda_item' as tabela, COUNT(*) as total_registros FROM venda_item
UNION ALL
SELECT 
    'usuario' as tabela, COUNT(*) as total_registros FROM usuario;

-- Estatísticas gerais
SELECT 
    'Produtos Ativos: ' || COUNT(*) FROM produto WHERE ativo = TRUE
UNION ALL
SELECT 
    'Estoque Total: ' || fn_estoque_total()
UNION ALL
SELECT 
    'Valor Total Estoque: R$ ' || fn_valor_total_estoque()
UNION ALL
SELECT 
    'Categorias Ativas: ' || COUNT(*) FROM categoria WHERE ativo = TRUE
UNION ALL
SELECT 
    'Clientes Cadastrados: ' || COUNT(*) FROM cliente WHERE ativo = TRUE
UNION ALL
SELECT 
    'Usuários Ativos: ' || COUNT(*) FROM usuario WHERE ativo = TRUE;

COMMIT;
