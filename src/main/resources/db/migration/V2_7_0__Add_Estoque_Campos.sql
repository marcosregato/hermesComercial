-- Hermes Comercial PDV v2.7.0 - Adicionar campos de estoque
-- Migração para adicionar campos avançados de controle de estoque

-- Adicionar novos campos à tabela produto
ALTER TABLE produto ADD COLUMN estoque_minimo INTEGER DEFAULT 5;
ALTER TABLE produto ADD COLUMN estoque_maximo INTEGER DEFAULT 100;
ALTER TABLE produto ADD COLUMN localizacao_estoque VARCHAR(100) DEFAULT 'A-01-01';
ALTER TABLE produto ADD COLUMN lote VARCHAR(50);
ALTER TABLE produto ADD COLUMN data_validade DATE;

-- Atualizar produtos existentes com valores padrão
UPDATE produto SET 
    estoque_minimo = 5,
    estoque_maximo = 100,
    localizacao_estoque = 'A-01-01'
WHERE estoque_minimo IS NULL OR estoque_maximo IS NULL OR localizacao_estoque IS NULL;

-- Criar índices para performance nos novos campos
CREATE INDEX IF NOT EXISTS idx_produto_estoque_minimo ON produto(estoque_minimo);
CREATE INDEX IF NOT EXISTS idx_produto_estoque_maximo ON produto(estoque_maximo);
CREATE INDEX IF NOT EXISTS idx_produto_localizacao ON produto(localizacao_estoque);
CREATE INDEX IF NOT EXISTS idx_produto_lote ON produto(lote);
CREATE INDEX IF NOT EXISTS idx_produto_validade ON produto(data_validade);

-- Criar trigger para alerta de estoque baixo (se suportado)
-- Note: SQLite não suporta triggers complexos, então será implementado na aplicação

-- Inserir alguns produtos de exemplo com dados de estoque completos
INSERT OR IGNORE INTO produto (
    descricao, categoria, codigo, preco, estoque, estoque_minimo, estoque_maximo, 
    localizacao_estoque, lote, data_validade, observacoes
) VALUES 
(
    'Notebook Dell Inspiron', 'Informática', 'NB001', 3500.00, 10, 5, 50, 
    'A-01-01', 'LOT2024001', '2024-12-31', 'Notebook Dell com processador Intel i5'
),
(
    'Mouse Wireless Logitech', 'Periféricos', 'MS001', 89.90, 25, 10, 100, 
    'B-02-03', 'LOT2024002', '2025-06-30', 'Mouse sem fio Logitech'
),
(
    'Teclado Mecânico Gamer', 'Gamer', 'KB001', 299.00, 15, 8, 75, 
    'C-01-02', 'LOT2024003', '2024-09-30', 'Teclado mecânico RGB'
);

-- Criar view para relatório de estoque
CREATE VIEW IF NOT EXISTS vw_estoque_status AS
SELECT 
    codigo,
    descricao,
    categoria,
    estoque,
    estoque_minimo,
    estoque_maximo,
    localizacao_estoque,
    lote,
    data_validade,
    CASE 
        WHEN estoque <= estoque_minimo THEN 'CRÍTICO'
        WHEN estoque <= estoque_minimo * 1.5 THEN 'BAIXO'
        WHEN estoque >= estoque_maximo THEN 'EXCESSO'
        ELSE 'NORMAL'
    END as status_estoque,
    (estoque_maximo - estoque) as espaco_disponivel,
    CASE 
        WHEN data_validade < date('now', '+30 days') THEN 'VENCIMENTO PRÓXIMO'
        WHEN data_validade < date('now', '+90 days') THEN 'ATENÇÃO'
        ELSE 'OK'
    END as status_validade
FROM produto
WHERE ativo = TRUE
ORDER BY 
    CASE 
        WHEN estoque <= estoque_minimo THEN 1
        WHEN estoque <= estoque_minimo * 1.5 THEN 2
        ELSE 3
    END,
    descricao;
