-- =====================================================
-- HERMES COMERCIAL - MIGRAÇÃO v2.8.0
-- Criar tabelas para novos serviços implementados
-- Data: 2026-05-04
-- =====================================================

-- Tabela de Notificações
CREATE TABLE IF NOT EXISTS notificacao (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    mensagem TEXT NOT NULL,
    tipo VARCHAR(50) NOT NULL, -- ESTOQUE, VENDA, FINANCEIRO, SISTEMA
    prioridade VARCHAR(20) NOT NULL DEFAULT 'MEDIA', -- BAIXA, MEDIA, ALTA, URGENTE
    status VARCHAR(20) NOT NULL DEFAULT 'NAO_LIDA', -- LIDA, NAO_LIDA, ARQUIVADA
    usuario_destino VARCHAR(50),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_leitura TIMESTAMP,
    link_acao VARCHAR(500), -- Link para ação relacionada
    dados_adicionais JSONB -- Dados extras em formato JSON
);

-- Tabela de Fornecedores
CREATE TABLE IF NOT EXISTS fornecedor (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(20) UNIQUE,
    inscricao_estadual VARCHAR(20),
    telefone VARCHAR(20),
    email VARCHAR(255),
    endereco TEXT,
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(10),
    contato_nome VARCHAR(255),
    contato_telefone VARCHAR(20),
    contato_email VARCHAR(255),
    status VARCHAR(20) DEFAULT 'ATIVO', -- ATIVO, INATIVO, BLOQUEADO
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observacoes TEXT
);

-- Tabela de Pedidos a Fornecedores
CREATE TABLE IF NOT EXISTS pedido_fornecedor (
    id SERIAL PRIMARY KEY,
    fornecedor_id INTEGER REFERENCES fornecedor(id),
    numero_pedido VARCHAR(50) UNIQUE,
    data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_previsao_entrega DATE,
    status VARCHAR(20) DEFAULT 'ABERTO', -- ABERTO, ENVIADO, RECEBIDO, CANCELADO
    valor_total DECIMAL(10,2) NOT NULL DEFAULT 0,
    forma_pagamento VARCHAR(50),
    condicao_pagamento VARCHAR(100),
    observacoes TEXT,
    usuario_criacao VARCHAR(50) DEFAULT 'system'
);

-- Tabela de Itens dos Pedidos a Fornecedores
CREATE TABLE IF NOT EXISTS item_pedido_fornecedor (
    id SERIAL PRIMARY KEY,
    pedido_id INTEGER REFERENCES pedido_fornecedor(id) ON DELETE CASCADE,
    produto_codigo VARCHAR(20),
    produto_descricao VARCHAR(255),
    quantidade INTEGER NOT NULL CHECK (quantidade > 0),
    preco_unitario DECIMAL(10,2) NOT NULL CHECK (preco_unitario >= 0),
    subtotal DECIMAL(10,2) NOT NULL CHECK (subtotal >= 0),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Contas a Pagar
CREATE TABLE IF NOT EXISTS conta_pagar (
    id SERIAL PRIMARY KEY,
    fornecedor_id INTEGER REFERENCES fornecedor(id),
    descricao VARCHAR(255) NOT NULL,
    valor_original DECIMAL(10,2) NOT NULL CHECK (valor_original >= 0),
    valor_pago DECIMAL(10,2) DEFAULT 0 CHECK (valor_pago >= 0),
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    status VARCHAR(20) DEFAULT 'ABERTA', -- ABERTA, PAGA, VENCIDA, CANCELADA
    categoria VARCHAR(100),
    forma_pagamento VARCHAR(50),
    numero_documento VARCHAR(50),
    observacoes TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_criacao VARCHAR(50) DEFAULT 'system'
);

-- Tabela de Contas a Receber
CREATE TABLE IF NOT EXISTS conta_receber (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER REFERENCES cliente(id),
    descricao VARCHAR(255) NOT NULL,
    valor_original DECIMAL(10,2) NOT NULL CHECK (valor_original >= 0),
    valor_recebido DECIMAL(10,2) DEFAULT 0 CHECK (valor_recebido >= 0),
    data_vencimento DATE NOT NULL,
    data_recebimento DATE,
    status VARCHAR(20) DEFAULT 'ABERTA', -- ABERTA, RECEBIDA, VENCIDA, CANCELADA
    categoria VARCHAR(100),
    forma_pagamento VARCHAR(50),
    numero_documento VARCHAR(50),
    observacoes TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_criacao VARCHAR(50) DEFAULT 'system'
);

-- Tabela de Fluxo de Caixa
CREATE TABLE IF NOT EXISTS fluxo_caixa (
    id SERIAL PRIMARY KEY,
    data_movimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo VARCHAR(20) NOT NULL, -- ENTRADA, SAIDA
    categoria VARCHAR(100) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    forma_pagamento VARCHAR(50),
    documento_referencia VARCHAR(50), -- ID de venda, conta, etc.
    usuario_criacao VARCHAR(50) DEFAULT 'system',
    observacoes TEXT
);

-- Tabela de Categorias Financeiras
CREATE TABLE IF NOT EXISTS categoria_financeira (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL,
    tipo VARCHAR(20) NOT NULL, -- ENTRADA, SAIDA
    descricao TEXT,
    cor VARCHAR(7), -- Código hex da cor para dashboard
    ativa BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Auditoria
CREATE TABLE IF NOT EXISTS auditoria (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER REFERENCES usuario(id),
    acao VARCHAR(100) NOT NULL, -- LOGIN, LOGOUT, CREATE, UPDATE, DELETE, VENDA, etc.
    modulo VARCHAR(50) NOT NULL, -- VENDAS, ESTOQUE, FINANCEIRO, etc.
    registro_id VARCHAR(50), -- ID do registro afetado
    tabela_afetada VARCHAR(50),
    dados_antigos JSONB,
    dados_novos JSONB,
    ip_address VARCHAR(45),
    user_agent TEXT,
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    nivel VARCHAR(20) DEFAULT 'INFO', -- INFO, WARNING, ERROR, CRITICAL
    mensagem TEXT
);

-- Tabela de Backup
CREATE TABLE IF NOT EXISTS backup_registro (
    id SERIAL PRIMARY KEY,
    tipo_backup VARCHAR(20) NOT NULL, -- COMPLETO, INCREMENTAL
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE', -- PENDENTE, EM_ANDAMENTO, CONCLUIDO, ERRO
    caminho_arquivo VARCHAR(500),
    tamanho_arquivo BIGINT,
    quantidade_arquivos INTEGER DEFAULT 0,
    data_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_fim TIMESTAMP,
    duracao_segundos INTEGER,
    mensagem TEXT,
    usuario_agendamento VARCHAR(50) DEFAULT 'system'
);

-- Tabela de Operações Offline Pendentes
CREATE TABLE IF NOT EXISTS operacao_pendente (
    id VARCHAR(50) PRIMARY KEY, -- ID único gerado pelo sistema
    tipo_operacao VARCHAR(50) NOT NULL, -- VENDA, ESTOQUE, CLIENTE, PRODUTO
    dados JSONB NOT NULL, -- Dados da operação em JSON
    status VARCHAR(20) DEFAULT 'PENDENTE', -- PENDENTE, EM_ANDAMENTO, SINCRONIZADO, ERRO, CONFLITO
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_sincronizacao TIMESTAMP,
    tentativas INTEGER DEFAULT 0,
    mensagem_erro TEXT,
    usuario_id VARCHAR(50)
);

-- Tabela de Eventos do Sistema (Observer Pattern)
CREATE TABLE IF NOT EXISTS evento_log (
    id SERIAL PRIMARY KEY,
    tipo_evento VARCHAR(100) NOT NULL, -- VENDA_REALIZADA, ESTOQUE_BAIXO, etc.
    dados JSONB, -- Dados do evento
    origem VARCHAR(100), -- Módulo que originou o evento
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id VARCHAR(50)
);

-- Tabela de Configurações de Impressora Fiscal
CREATE TABLE IF NOT EXISTS impressora_config (
    id SERIAL PRIMARY KEY,
    modelo VARCHAR(50) NOT NULL, -- DARUMA, BEMATECH, EPSON
    porta VARCHAR(50) NOT NULL, -- COM1, USB, etc.
    status VARCHAR(20) DEFAULT 'ATIVA', -- ATIVA, INATIVA, ERRO
    velocidade INTEGER DEFAULT 9600,
    bits_dados INTEGER DEFAULT 8,
    paridade VARCHAR(10) DEFAULT 'N',
    bits_parada INTEGER DEFAULT 1,
    timeout_conexao INTEGER DEFAULT 5000,
    data_configuracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_configuracao VARCHAR(50) DEFAULT 'system',
    observacoes TEXT
);

-- Tabela de Documentos Fiscais
CREATE TABLE IF NOT EXISTS documento_fiscal (
    id SERIAL PRIMARY KEY,
    tipo_documento VARCHAR(50) NOT NULL, -- CUPOM_FISCAL, NOTA_FISCAL, etc.
    numero_documento VARCHAR(50),
    serie_documento VARCHAR(20),
    data_emissao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10,2) NOT NULL,
    venda_id INTEGER REFERENCES venda(id),
    impressora_id INTEGER REFERENCES impressora_config(id),
    status VARCHAR(20) DEFAULT 'EMITIDO', -- EMITIDO, CANCELADO, ERRO
    dados_fiscais JSONB, -- Dados específicos do documento
    usuario_emissao VARCHAR(50) DEFAULT 'system'
);

-- Tabela de Logs de Importação/Exportação
CREATE TABLE IF NOT EXISTS importacao_exportacao_log (
    id SERIAL PRIMARY KEY,
    tipo_operacao VARCHAR(20) NOT NULL, -- IMPORTACAO, EXPORTACAO
    formato VARCHAR(20) NOT NULL, -- CSV, EXCEL, PDF
    tipo_dados VARCHAR(50), -- PRODUTOS, CLIENTES, VENDAS, etc.
    nome_arquivo VARCHAR(255),
    caminho_arquivo VARCHAR(500),
    status VARCHAR(20) DEFAULT 'EM_ANDAMENTO', -- EM_ANDAMENTO, CONCLUIDO, ERRO
    registros_processados INTEGER DEFAULT 0,
    registros_sucesso INTEGER DEFAULT 0,
    registros_erro INTEGER DEFAULT 0,
    data_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_fim TIMESTAMP,
    mensagem_erro TEXT,
    usuario_operacao VARCHAR(50) DEFAULT 'system'
);

-- =====================================================
-- ÍNDICES PARA NOVAS TABELAS
-- =====================================================

-- Índices para notificacao
CREATE INDEX IF NOT EXISTS idx_notificacao_usuario ON notificacao(usuario_destino);
CREATE INDEX IF NOT EXISTS idx_notificacao_status ON notificacao(status);
CREATE INDEX IF NOT EXISTS idx_notificacao_tipo ON notificacao(tipo);
CREATE INDEX IF NOT EXISTS idx_notificacao_prioridade ON notificacao(prioridade);
CREATE INDEX IF NOT EXISTS idx_notificacao_data_criacao ON notificacao(data_criacao);

-- Índices para fornecedor
CREATE INDEX IF NOT EXISTS idx_fornecedor_nome ON fornecedor(nome);
CREATE INDEX IF NOT EXISTS idx_fornecedor_cnpj ON fornecedor(cnpj);
CREATE INDEX IF NOT EXISTS idx_fornecedor_status ON fornecedor(status);

-- Índices para pedido_fornecedor
CREATE INDEX IF NOT EXISTS idx_pedido_fornecedor_fornecedor ON pedido_fornecedor(fornecedor_id);
CREATE INDEX IF NOT EXISTS idx_pedido_fornecedor_status ON pedido_fornecedor(status);
CREATE INDEX IF NOT EXISTS idx_pedido_fornecedor_data ON pedido_fornecedor(data_pedido);

-- Índices para conta_pagar
CREATE INDEX IF NOT EXISTS idx_conta_pagar_fornecedor ON conta_pagar(fornecedor_id);
CREATE INDEX IF NOT EXISTS idx_conta_pagar_vencimento ON conta_pagar(data_vencimento);
CREATE INDEX IF NOT EXISTS idx_conta_pagar_status ON conta_pagar(status);

-- Índices para conta_receber
CREATE INDEX IF NOT EXISTS idx_conta_receber_cliente ON conta_receber(cliente_id);
CREATE INDEX IF NOT EXISTS idx_conta_receber_vencimento ON conta_receber(data_vencimento);
CREATE INDEX IF NOT EXISTS idx_conta_receber_status ON conta_receber(status);

-- Índices para fluxo_caixa
CREATE INDEX IF NOT EXISTS idx_fluxo_caixa_data ON fluxo_caixa(data_movimento);
CREATE INDEX IF NOT EXISTS idx_fluxo_caixa_tipo ON fluxo_caixa(tipo);
CREATE INDEX IF NOT EXISTS idx_fluxo_caixa_categoria ON fluxo_caixa(categoria);

-- Índices para auditoria
CREATE INDEX IF NOT EXISTS idx_auditoria_usuario ON auditoria(usuario_id);
CREATE INDEX IF NOT EXISTS idx_auditoria_acao ON auditoria(acao);
CREATE INDEX IF NOT EXISTS idx_auditoria_data_hora ON auditoria(data_hora);
CREATE INDEX IF NOT EXISTS idx_auditoria_modulo ON auditoria(modulo);

-- Índices para backup_registro
CREATE INDEX IF NOT EXISTS idx_backup_tipo ON backup_registro(tipo_backup);
CREATE INDEX IF NOT EXISTS idx_backup_status ON backup_registro(status);
CREATE INDEX IF NOT EXISTS idx_backup_data_inicio ON backup_registro(data_inicio);

-- Índices para operacao_pendente
CREATE INDEX IF NOT EXISTS idx_operacao_pendente_status ON operacao_pendente(status);
CREATE INDEX IF NOT EXISTS idx_operacao_pendente_tipo ON operacao_pendente(tipo_operacao);
CREATE INDEX IF NOT EXISTS idx_operacao_pendente_data ON operacao_pendente(data_criacao);

-- Índices para evento_log
CREATE INDEX IF NOT EXISTS idx_evento_log_tipo ON evento_log(tipo_evento);
CREATE INDEX IF NOT EXISTS idx_evento_log_data_hora ON evento_log(data_hora);
CREATE INDEX IF NOT EXISTS idx_evento_log_origem ON evento_log(origem);

-- =====================================================
-- TRIGGERS PARA NOVAS TABELAS
-- =====================================================

-- Trigger para atualizar data_atualizacao da tabela fornecedor
CREATE OR REPLACE FUNCTION trg_fornecedor_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_fornecedor_atualizar_timestamp
    BEFORE UPDATE ON fornecedor
    FOR EACH ROW
    EXECUTE FUNCTION trg_fornecedor_atualizar_timestamp();

-- =====================================================
-- INSERÇÕES INICIAIS
-- =====================================================

-- Inserir categorias financeiras padrão
INSERT INTO categoria_financeira (nome, tipo, descricao, cor) VALUES
('Vendas', 'ENTRADA', 'Receitas de vendas de produtos', '#28a745'),
('Serviços', 'ENTRADA', 'Receitas de serviços prestados', '#17a2b8'),
('Compras', 'SAIDA', 'Compras de mercadorias', '#dc3545'),
('Aluguel', 'SAIDA', 'Pagamento de aluguel', '#fd7e14'),
('Salários', 'SAIDA', 'Pagamento de salários', '#6f42c1'),
('Contas', 'SAIDA', 'Contas diversas', '#e83e8c'),
('Impostos', 'SAIDA', 'Pagamento de impostos', '#20c997'),
('Outros', 'ENTRADA', 'Outras receitas', '#6c757d')
ON CONFLICT (nome) DO NOTHING;

-- Inserir configuração de impressora padrão
INSERT INTO impressora_config (modelo, porta, status, observacoes) VALUES
('GENERIC', 'USB', 'ATIVA', 'Impressora genérica não fiscal')
ON CONFLICT DO NOTHING;
