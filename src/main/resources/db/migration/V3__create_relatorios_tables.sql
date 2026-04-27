-- =====================================================
-- HERMES COMERCIAL PDV v2.0 - TABELAS DE RELATÓRIOS
-- Versão: 3.0 - Tabelas Específicas para Relatórios
-- Data: 2026-04-27
-- Compatível: PostgreSQL, MySQL, H2
-- =====================================================

-- =====================================================
-- TABELA DE RELATÓRIOS DIÁRIOS
-- =====================================================

CREATE TABLE IF NOT EXISTS relatorio_diario (
    id SERIAL PRIMARY KEY,
    data_relatorio DATE NOT NULL,
    total_vendas INTEGER DEFAULT 0,
    valor_total DECIMAL(12,2) DEFAULT 0.00,
    ticket_medio DECIMAL(10,2) DEFAULT 0.00,
    produtos_vendidos INTEGER DEFAULT 0,
    clientes_atendidos INTEGER DEFAULT 0,
    vendas_vista DECIMAL(12,2) DEFAULT 0.00,
    vendas_cartao DECIMAL(12,2) DEFAULT 0.00,
    vendas_pix DECIMAL(12,2) DEFAULT 0.00,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_geracao VARCHAR(50) DEFAULT 'system',
    UNIQUE(data_relatorio)
);

-- =====================================================
-- TABELA DE RESUMO POR PERÍODO
-- =====================================================

CREATE TABLE IF NOT EXISTS resumo_periodo (
    id SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    periodo VARCHAR(50) NOT NULL, -- 'manhã', 'tarde', 'noite'
    tipo VARCHAR(20) DEFAULT 'VENDA',
    descricao VARCHAR(255),
    valor DECIMAL(12,2) DEFAULT 0.00,
    quantidade INTEGER DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(data, periodo)
);

-- =====================================================
-- TABELA DE MOVIMENTAÇÃO FINANCEIRA
-- =====================================================

CREATE TABLE IF NOT EXISTS movimentacao_financeira (
    id SERIAL PRIMARY KEY,
    data_movimento DATE NOT NULL,
    tipo VARCHAR(20) NOT NULL, -- 'RECEITA', 'DESPESA'
    categoria VARCHAR(100) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    valor DECIMAL(12,2) NOT NULL,
    forma_pagamento VARCHAR(50),
    status VARCHAR(20) DEFAULT 'CONCLUIDO',
    observacoes TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_movimento VARCHAR(50) DEFAULT 'system'
);

-- =====================================================
-- TABELA DE ESTATÍSTICAS DE PRODUTOS
-- =====================================================

CREATE TABLE IF NOT EXISTS estatistica_produto (
    id SERIAL PRIMARY KEY,
    data_referencia DATE NOT NULL,
    produto_codigo VARCHAR(20) REFERENCES produto(codigo),
    quantidade_vendida INTEGER DEFAULT 0,
    valor_total_vendido DECIMAL(12,2) DEFAULT 0.00,
    estoque_anterior INTEGER DEFAULT 0,
    estoque_atual INTEGER DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(data_referencia, produto_codigo)
);

-- =====================================================
-- ÍNDICES PARA PERFORMANCE DAS TABELAS DE RELATÓRIOS
-- =====================================================

-- Índices para relatorio_diario
CREATE INDEX IF NOT EXISTS idx_relatorio_diario_data ON relatorio_diario(data_relatorio);
CREATE INDEX IF NOT EXISTS idx_relatorio_diario_valor ON relatorio_diario(valor_total);
CREATE INDEX IF NOT EXISTS idx_relatorio_diario_vendas ON relatorio_diario(total_vendas);

-- Índices para resumo_periodo
CREATE INDEX IF NOT EXISTS idx_resumo_periodo_data ON resumo_periodo(data);
CREATE INDEX IF NOT EXISTS idx_resumo_periodo_periodo ON resumo_periodo(periodo);
CREATE INDEX IF NOT EXISTS idx_resumo_periodo_tipo ON resumo_periodo(tipo);

-- Índices para movimentacao_financeira
CREATE INDEX IF NOT EXISTS idx_movimentacao_data ON movimentacao_financeira(data_movimento);
CREATE INDEX IF NOT EXISTS idx_movimentacao_tipo ON movimentacao_financeira(tipo);
CREATE INDEX IF NOT EXISTS idx_movimentacao_categoria ON movimentacao_financeira(categoria);

-- Índices para estatistica_produto
CREATE INDEX IF NOT EXISTS idx_estatistica_data ON estatistica_produto(data_referencia);
CREATE INDEX IF NOT EXISTS idx_estatistica_produto ON estatistica_produto(produto_codigo);
CREATE INDEX IF NOT EXISTS idx_estatistica_quantidade ON estatistica_produto(quantidade_vendida);

-- =====================================================
-- VIEWS ESPECÍFICAS PARA RELATÓRIOS
-- =====================================================

-- View de resumo diário completo
CREATE OR REPLACE VIEW vw_relatorio_diario_completo AS
SELECT 
    rd.data_relatorio,
    rd.total_vendas,
    rd.valor_total,
    rd.ticket_medio,
    rd.produtos_vendidos,
    rd.clientes_atendidos,
    rd.vendas_vista,
    rd.vendas_cartao,
    rd.vendas_pix,
    rd.data_criacao,
    rd.usuario_geracao,
    -- Calcular percentuais
    CASE 
        WHEN rd.valor_total > 0 THEN 
            ROUND((rd.vendas_vista / rd.valor_total) * 100, 2)
        ELSE 0 
    END as percent_vista,
    CASE 
        WHEN rd.valor_total > 0 THEN 
            ROUND((rd.vendas_cartao / rd.valor_total) * 100, 2)
        ELSE 0 
    END as percent_cartao,
    CASE 
        WHEN rd.valor_total > 0 THEN 
            ROUND((rd.vendas_pix / rd.valor_total) * 100, 2)
        ELSE 0 
    END as percent_pix
FROM relatorio_diario rd
ORDER BY rd.data_relatorio DESC;

-- View de resumo por período
CREATE OR REPLACE VIEW vw_resumo_periodo_detalhado AS
SELECT 
    rp.data,
    rp.periodo,
    rp.tipo,
    rp.descricao,
    rp.valor,
    rp.quantidade,
    rp.data_criacao,
    -- Calcular valor médio por item
    CASE 
        WHEN rp.quantidade > 0 THEN 
            ROUND(rp.valor / rp.quantidade, 2)
        ELSE 0 
    END as valor_medio_item
FROM resumo_periodo rp
ORDER BY rp.data DESC, rp.periodo;

-- View de movimento financeiro consolidado
CREATE OR REPLACE VIEW vw_movimento_financeiro_consolidado AS
SELECT 
    mf.data_movimento,
    mf.tipo,
    mf.categoria,
    COUNT(*) as total_transacoes,
    SUM(mf.valor) as valor_total,
    AVG(mf.valor) as valor_medio,
    MIN(mf.valor) as valor_minimo,
    MAX(mf.valor) as valor_maximo
FROM movimentacao_financeira mf
WHERE mf.status = 'CONCLUIDO'
GROUP BY mf.data_movimento, mf.tipo, mf.categoria
ORDER BY mf.data_movimento DESC, mf.valor_total DESC;

-- =====================================================
-- FUNÇÕES PARA RELATÓRIOS
-- =====================================================

-- Função para gerar relatório diário
CREATE OR REPLACE FUNCTION fn_gerar_relatorio_diario(p_data DATE)
RETURNS VOID AS $$
DECLARE
    v_total_vendas INTEGER;
    v_valor_total DECIMAL(12,2);
    v_ticket_medio DECIMAL(10,2);
    v_produtos_vendidos INTEGER;
    v_clientes_atendidos INTEGER;
    v_vendas_vista DECIMAL(12,2);
    v_vendas_cartao DECIMAL(12,2);
    v_vendas_pix DECIMAL(12,2);
BEGIN
    -- Calcular totais do dia
    SELECT 
        COUNT(DISTINCT v.id),
        COALESCE(SUM(v.valor_total), 0),
        CASE 
            WHEN COUNT(DISTINCT v.id) > 0 THEN 
                COALESCE(AVG(v.valor_total), 0)
            ELSE 0 
        END,
        COALESCE(SUM(vi.quantidade), 0),
        COUNT(DISTINCT v.cliente_id),
        COALESCE(SUM(CASE WHEN v.forma_pagamento = 'DINHEIRO' THEN v.valor_total ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN v.forma_pagamento = 'CARTAO' THEN v.valor_total ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN v.forma_pagamento = 'PIX' THEN v.valor_total ELSE 0 END), 0)
    INTO 
        v_total_vendas, v_valor_total, v_ticket_medio, v_produtos_vendidos, 
        v_clientes_atendidos, v_vendas_vista, v_vendas_cartao, v_vendas_pix
    FROM venda v
    LEFT JOIN venda_item vi ON v.id = vi.venda_id
    WHERE DATE(v.data_venda) = p_data 
        AND v.status = 'CONCLUIDA';
    
    -- Inserir ou atualizar relatório diário
    INSERT INTO relatorio_diario (
        data_relatorio, total_vendas, valor_total, ticket_medio, 
        produtos_vendidos, clientes_atendidos, vendas_vista, 
        vendas_cartao, vendas_pix, usuario_geracao
    ) VALUES (
        p_data, v_total_vendas, v_valor_total, v_ticket_medio,
        v_produtos_vendidos, v_clientes_atendidos, v_vendas_vista,
        v_vendas_cartao, v_vendas_pix, CURRENT_USER
    )
    ON CONFLICT (data_relatorio) DO UPDATE SET
        total_vendas = v_total_vendas,
        valor_total = v_valor_total,
        ticket_medio = v_ticket_medio,
        produtos_vendidos = v_produtos_vendidos,
        clientes_atendidos = v_clientes_atendidos,
        vendas_vista = v_vendas_vista,
        vendas_cartao = v_vendas_cartao,
        vendas_pix = v_vendas_pix,
        data_atualizacao = CURRENT_TIMESTAMP,
        usuario_geracao = CURRENT_USER;
END;
$$ LANGUAGE plpgsql;

-- Função para buscar dados por data específica
CREATE OR REPLACE FUNCTION fn_buscar_dados_por_data(p_data DATE)
RETURNS TABLE (
    total_vendas INTEGER,
    valor_total DECIMAL(12,2),
    ticket_medio DECIMAL(10,2),
    produtos_vendidos INTEGER,
    clientes_atendidos INTEGER,
    vendas_vista DECIMAL(12,2),
    vendas_cartao DECIMAL(12,2),
    vendas_pix DECIMAL(12,2)
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        rd.total_vendas,
        rd.valor_total,
        rd.ticket_medio,
        rd.produtos_vendidos,
        rd.clientes_atendidos,
        rd.vendas_vista,
        rd.vendas_cartao,
        rd.vendas_pix
    FROM relatorio_diario rd
    WHERE rd.data_relatorio = p_data;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- TRIGGERS PARA TABELAS DE RELATÓRIOS
-- =====================================================

-- Trigger para atualizar data_atualizacao em relatorio_diario
CREATE OR REPLACE FUNCTION trg_relatorio_diario_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_relatorio_diario_atualizar_timestamp
    BEFORE UPDATE ON relatorio_diario
    FOR EACH ROW
    EXECUTE FUNCTION trg_relatorio_diario_atualizar_timestamp();

-- Trigger para atualizar data_atualizacao em resumo_periodo
CREATE OR REPLACE FUNCTION trg_resumo_periodo_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_resumo_periodo_atualizar_timestamp
    BEFORE UPDATE ON resumo_periodo
    FOR EACH ROW
    EXECUTE FUNCTION trg_resumo_periodo_atualizar_timestamp();

-- Trigger para atualizar data_atualizacao em movimentacao_financeira
CREATE OR REPLACE FUNCTION trg_movimentacao_financeira_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_movimentacao_financeira_atualizar_timestamp
    BEFORE UPDATE ON movimentacao_financeira
    FOR EACH ROW
    EXECUTE FUNCTION trg_movimentacao_financeira_atualizar_timestamp();

-- =====================================================
-- INSERÇÃO DE DADOS INICIAIS PARA RELATÓRIOS
-- =====================================================

-- Inserir dados de exemplo para relatório diário (hoje)
INSERT INTO relatorio_diario (
    data_relatorio, total_vendas, valor_total, ticket_medio,
    produtos_vendidos, clientes_atendidos, vendas_vista,
    vendas_cartao, vendas_pix, usuario_geracao
) VALUES (
    CURRENT_DATE, 23, 1234.56, 53.68, 67, 45, 890.34, 344.22, 0.00, 'system'
) ON CONFLICT (data_relatorio) DO NOTHING;

-- Inserir dados de exemplo para resumo por período
INSERT INTO resumo_periodo (
    data, periodo, tipo, descricao, valor, quantidade
) VALUES 
    (CURRENT_DATE, 'manhã', 'VENDA', 'Período da manhã', 567.89, 15),
    (CURRENT_DATE, 'tarde', 'VENDA', 'Período da tarde', 445.67, 18),
    (CURRENT_DATE, 'noite', 'VENDA', 'Período da noite', 221.00, 12)
ON CONFLICT (data, periodo) DO NOTHING;

-- Inserir movimentações financeiras de exemplo
INSERT INTO movimentacao_financeira (
    data_movimento, tipo, categoria, descricao, valor, forma_pagamento, status
) VALUES 
    (CURRENT_DATE, 'RECEITA', 'Vendas', 'Venda produtos dia', 1234.56, 'MISTO', 'CONCLUIDA'),
    (CURRENT_DATE, 'DESPESA', 'Aluguel', 'Aluguel loja', 500.00, 'TRANSFERENCIA', 'CONCLUIDA'),
    (CURRENT_DATE, 'DESPESA', 'Contas', 'Conta de luz', 150.00, 'BOLETO', 'CONCLUIDA')
ON CONFLICT DO NOTHING;

-- =====================================================
-- ESTATÍSTICAS DAS TABELAS DE RELATÓRIOS
-- =====================================================

-- Mostrar resumo das novas tabelas
SELECT 
    'relatorio_diario' as tabela, COUNT(*) as total_registros FROM relatorio_diario
UNION ALL
SELECT 
    'resumo_periodo' as tabela, COUNT(*) as total_registros FROM resumo_periodo
UNION ALL
SELECT 
    'movimentacao_financeira' as tabela, COUNT(*) as total_registros FROM movimentacao_financeira
UNION ALL
SELECT 
    'estatistica_produto' as tabela, COUNT(*) as total_registros FROM estatistica_produto;

COMMIT;
