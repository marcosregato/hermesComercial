-- =====================================================
-- HERMES COMERCIAL - INSERÇÃO DE DADOS DE EXEMPLO v3.0.3
-- Dados iniciais para teste do sistema
-- Data: 06/05/2026
-- =====================================================

-- Inserir dados na tabela usuarios (completos)
INSERT INTO hermes_pdv.usuarios (id, username, password, nome_completo, email, ativo, data_criacao, data_ultima_atualizacao, endereco, bairro, cidade, estado, cep, telefone, numero_documento, whatsapp, tipo_documento, tipo_usuario) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Administrador do Sistema', 'admin@hermescomercial.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Rua Principal, 100', 'Centro', 'São Paulo', 'SP', '01310-100', '(11) 9999-8888', '123.456.789-00', '(11) 97777-6666', 'CPF', 'ADMIN'),
(2, 'vendedor1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Vendedor Principal', 'vendedor1@hermescomercial.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Avenida Comercial, 200', 'Centro', 'São Paulo', 'SP', '01210-200', '(11) 9888-7777', '456.789.123-00', '(11) 96666-5555', 'CPF', 'VENDEDOR'),
(3, 'caixa1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Operador de Caixa', 'caixa1@hermescomercial.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Rua das Lojas, 300', 'Shopping', 'São Paulo', 'SP', '01420-300', '(11) 9777-6666', '789.123.456-00', '(11) 95555-4444', 'CPF', 'CAIXA'),
(4, 'gerente1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Gerente de Loja', 'gerente1@hermescomercial.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Alameda dos Negócios, 400', 'Jardins', 'São Paulo', 'SP', '01540-400', '(11) 9666-5555', '321.654.987-00', '(11) 94444-3333', 'CPF', 'GERENTE')
ON CONFLICT (id) DO NOTHING;

-- Inserir dados na tabela clientes (completos)
INSERT INTO hermes_pdv.clientes (id, nome, cpf_cnpj, tipo_pessoa, rg, data_nascimento, telefone, celular, email, cep, endereco, numero, complemento, bairro, cidade, estado, data_cadastro, data_ultima_atualizacao, ativo, observacao, nome_fantasia, inscricao_estadual) VALUES
(1, 'João Silva', '123.456.789-00', 'FISICA', 'MG-12.345.678', '1980-05-15', '(11) 3333-2222', '(11) 98888-7777', 'joao.silva@email.com', '01234-567', 'Rua das Flores, 123', '123', 'Apto 45', 'Vila Madalena', 'São Paulo', 'SP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'Cliente regular', NULL, NULL),
(2, 'Maria Santos', '987.654.321-00', 'FISICA', 'SP-98.765.432', '1985-08-22', '(11) 4444-3333', '(11) 97777-6666', 'maria.santos@email.com', '04567-890', 'Avenida Paulista, 456', '456', 'Cj 567', 'Bela Vista', 'São Paulo', 'SP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'Cliente VIP', NULL, NULL),
(3, 'Empresa ABC Ltda', '12.345.678/0001-90', 'JURIDICA', NULL, NULL, '(11) 5555-4444', '(11) 96666-5555', 'contato@empresaabc.com', '01345-678', 'Rua da Empresa, 789', '789', 'Sala 10', 'Centro', 'São Paulo', 'SP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'Cliente corporativo', 'ABC Comercial', '123.456.789.123'),
(4, 'Pedro Oliveira', '456.789.123-00', 'FISICA', 'RJ-45.678.912', '1975-12-10', '(11) 6666-5555', '(11) 95555-4444', 'pedro.oliveira@email.com', '05678-901', 'Alameda Santos, 321', '321', NULL, 'Jardins', 'São Paulo', 'SP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'Cliente desde 2020', NULL, NULL),
(5, 'Ana Costa', '789.123.456-00', 'FISICA', 'PR-78.912.345', '1990-03-25', '(11) 7777-6666', '(11) 94444-3333', 'ana.costa@email.com', '06789-012', 'Rua Verde, 654', '654', 'Casa 2', 'Moema', 'São Paulo', 'SP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true, 'Novo cliente', NULL, NULL)
ON CONFLICT (id) DO NOTHING;

-- Inserir dados na tabela categorias
INSERT INTO hermes_pdv.categorias (id, nome, descricao, ativa, data_criacao) VALUES
(1, 'Eletrônicos', 'Produtos eletrônicos e acessórios', true, CURRENT_TIMESTAMP),
(2, 'Roupas', 'Vestuário masculino e feminino', true, CURRENT_TIMESTAMP),
(3, 'Alimentos', 'Produtos alimentícios e bebidas', true, CURRENT_TIMESTAMP),
(4, 'Móveis', 'Móveis para casa e escritório', true, CURRENT_TIMESTAMP),
(5, 'Livros', 'Livros e material de escritório', true, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Inserir dados na tabela fornecedores
INSERT INTO hermes_pdv.fornecedores (id, nome, cnpj, telefone, email, endereco, ativo, data_cadastro) VALUES
(1, 'Tech Distribuidora', '12.345.678/0001-23', '(11) 3333-2222', 'carlos@techdistribuidora.com', 'Rua Tecnologia, 100', true, CURRENT_TIMESTAMP),
(2, 'Fashion Import', '98.765.432/0001-45', '(11) 4444-3333', 'maria@fashionimport.com', 'Avenida Moda, 200', true, CURRENT_TIMESTAMP),
(3, 'Food Supply', '45.678.912/0001-67', '(11) 5555-4444', 'pedro@foodsupply.com', 'Rua Alimentos, 300', true, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Inserir dados na tabela produtos (completos)
INSERT INTO hermes_pdv.produtos (id, nome, descricao, codigo_barras, preco_venda, preco_custo, estoque_atual, estoque_minimo, unidade_medida, id_categoria, id_fornecedor, ativo, data_cadastro, data_ultima_atualizacao) VALUES
(1, 'Smartphone Galaxy S21', 'Smartphone Samsung Galaxy S21 128GB', '7891234567890', 3500.00, 2800.00, 15, 5, 'UN', 1, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Notebook Dell Inspiron', 'Notebook Dell Inspiron 15.6" Intel i5', '7891234567891', 4200.00, 3500.00, 8, 3, 'UN', 1, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Camiseta Polo', 'Camiseta Polo 100% Algodão', '7891234567892', 89.90, 45.00, 50, 10, 'UN', 2, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Calça Jeans', 'Calça Jeans Masculina', '7891234567893', 159.90, 85.00, 30, 8, 'UN', 2, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Arroz 5kg', 'Arroz Branco Tipo 1 5kg', '7891234567894', 25.50, 18.00, 100, 20, 'UN', 3, 3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Feijão 1kg', 'Feijão Carioca 1kg', '7891234567895', 8.90, 5.50, 150, 30, 'UN', 3, 3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Mesa de Escritório', 'Mesa de escritório 1.20x0.60m', '7891234567896', 450.00, 320.00, 5, 2, 'UN', 4, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Cadeira Executiva', 'Cadeira executiva ergonômica', '7891234567897', 380.00, 280.00, 7, 2, 'UN', 4, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Livro - Programação Java', 'Livro: Programação em Java para Iniciantes', '7891234567898', 89.90, 55.00, 25, 5, 'UN', 5, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'Caderno 200 folhas', 'Caderno espiral 200 folhas', '7891234567899', 15.90, 8.50, 80, 15, 'UN', 5, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Inserir dados na tabela vendas
INSERT INTO hermes_pdv.vendas (id, numero_venda, id_cliente, valor_total, valor_desconto, valor_final, status, data_venda, usuario_vendedor, data_atualizacao) VALUES
(1, 'V001', 1, 3589.90, 100.00, 3489.90, 'CONCLUIDA', CURRENT_TIMESTAMP - INTERVAL '2 days', 'vendedor1', CURRENT_TIMESTAMP),
(2, 'V002', 2, 249.80, 0.00, 249.80, 'CONCLUIDA', CURRENT_TIMESTAMP - INTERVAL '1 day', 'vendedor1', CURRENT_TIMESTAMP),
(3, 'V003', 3, 830.00, 50.00, 780.00, 'CONCLUIDA', CURRENT_TIMESTAMP - INTERVAL '3 hours', 'caixa1', CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Inserir dados na tabela itens_venda
INSERT INTO hermes_pdv.itens_venda (id, id_venda, id_produto, quantidade, valor_unitario, valor_total, desconto, valor_final, observacao) VALUES
(1, 1, 1, 1, 3500.00, 3500.00, 100.00, 3400.00, 'Desconto especial'),
(2, 1, 10, 1, 15.90, 15.90, 0.00, 15.90, 'Acessório'),
(3, 2, 3, 2, 89.90, 179.80, 0.00, 179.80, NULL),
(4, 2, 4, 1, 70.00, 70.00, 0.00, 70.00, NULL),
(5, 3, 5, 2, 25.50, 51.00, 0.00, 51.00, NULL),
(6, 3, 6, 3, 8.90, 26.70, 0.00, 26.70, NULL),
(7, 3, 7, 1, 450.00, 450.00, 50.00, 400.00, 'Desconto de fidelidade'),
(8, 3, 8, 1, 380.00, 380.00, 0.00, 380.00, NULL)
ON CONFLICT (id) DO NOTHING;

-- Inserir dados na tabela pagamentos
INSERT INTO hermes_pdv.pagamentos (id, id_venda, valor_pago, forma_pagamento, status, data_pagamento, observacoes, valor_troco, numero_parcelas, bandeira_cartao, numero_autorizacao, nsu, cnpj_estabelecimento) VALUES
(1, 1, 3489.90, 'CARTAO_CREDITO', 'PAGO', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Pagamento em 3x', 0.00, '3', 'VISA', '123456', 'NSU123456', '12.345.678/0001-23'),
(2, 2, 249.80, 'DINHEIRO', 'PAGO', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Pagamento à vista', 0.20, NULL, NULL, NULL, NULL, NULL),
(3, 3, 780.00, 'PIX', 'PAGO', CURRENT_TIMESTAMP - INTERVAL '3 hours', 'Transferência instantânea', 0.00, NULL, NULL, NULL, NULL, NULL)
ON CONFLICT (id) DO NOTHING;

-- Inserir dados na tabela orcamentos
INSERT INTO hermes_pdv.orcamentos (id, numero_orcamento, id_cliente, valor_total, validade_dias, status, data_orcamento, data_expiracao, usuario_vendedor, usuario_aprovacao, data_aprovacao, motivo_rejeicao, data_atualizacao) VALUES
(1, 'O001', 4, 1250.00, 15, 'ABERTO', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP + INTERVAL '14 days', 'vendedor1', NULL, NULL, NULL, CURRENT_TIMESTAMP),
(2, 'O002', 5, 450.00, 7, 'APROVADO', CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP + INTERVAL '4 days', 'caixa1', 'gerente1', CURRENT_TIMESTAMP - INTERVAL '2 days', NULL, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Inserir dados na tabela itens_orcamento
INSERT INTO hermes_pdv.itens_orcamento (id, id_orcamento, id_produto, quantidade, valor_unitario, valor_total, desconto, valor_final, observacao) VALUES
(1, 1, 2, 1, 4200.00, 4200.00, 3000.00, 1200.00, 'Desconto promocional'),
(2, 1, 9, 1, 89.90, 89.90, 0.00, 89.90, NULL),
(3, 2, 7, 1, 450.00, 450.00, 0.00, 450.00, NULL)
ON CONFLICT (id) DO NOTHING;

-- Inserir dados na tabela movimentacoes_estoque
INSERT INTO hermes_pdv.movimentacoes_estoque (id, id_produto, tipo_movimentacao, quantidade, saldo_anterior, saldo_novo, motivo, documento_referencia, data_movimentacao, usuario_responsavel, localizacao, tipo_documento, numero_documento) VALUES
(1, 1, 'ENTRADA', 20, 0, 20, 'Compra inicial', 'NF-001', CURRENT_TIMESTAMP - INTERVAL '5 days', 'admin', 'Depósito Principal', 'NFE', '2023000001'),
(2, 1, 'SAIDA', 1, 20, 19, 'Venda V001', 'V001', CURRENT_TIMESTAMP - INTERVAL '2 days', 'vendedor1', 'Loja', 'NFE', '2023000002'),
(3, 3, 'ENTRADA', 100, 0, 100, 'Compra inicial', 'NF-002', CURRENT_TIMESTAMP - INTERVAL '4 days', 'admin', 'Depósito Principal', 'NFE', '2023000003'),
(4, 3, 'SAIDA', 2, 100, 98, 'Venda V002', 'V002', CURRENT_TIMESTAMP - INTERVAL '1 day', 'vendedor1', 'Loja', 'NFE', '2023000004'),
(5, 5, 'ENTRADA', 200, 0, 200, 'Compra inicial', 'NF-003', CURRENT_TIMESTAMP - INTERVAL '3 days', 'admin', 'Depósito Principal', 'NFE', '2023000005'),
(6, 5, 'SAIDA', 2, 200, 198, 'Venda V003', 'V003', CURRENT_TIMESTAMP - INTERVAL '3 hours', 'caixa1', 'Loja', 'NFE', '2023000006')
ON CONFLICT (id) DO NOTHING;

-- Inserir dados na tabela configuracoes
INSERT INTO hermes_pdv.configuracoes (id, chave, valor, tipo, descricao, modulo, ativa, data_criacao, data_atualizacao, usuario_atualizacao, valor_padrao, tipo_dado, obrigatorio) VALUES
(1, 'empresa.nome', 'Hermes Comercial', 'STRING', 'Nome da empresa', 'SISTEMA', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'Hermes Comercial', 'STRING', false),
(2, 'empresa.cnpj', '12.345.678/0001-90', 'STRING', 'CNPJ da empresa', 'SISTEMA', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', NULL, 'STRING', false),
(3, 'venda.desconto.maximo', '10.00', 'DECIMAL', 'Desconto máximo permitido (%)', 'VENDAS', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', '5.00', 'DECIMAL', false),
(4, 'estoque.avisar.minimo', 'true', 'BOOLEAN', 'Avisar quando estoque estiver abaixo do mínimo', 'ESTOQUE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'true', 'BOOLEAN', false),
(5, 'pdv.numero_terminal', '1', 'INTEGER', 'Número do terminal PDV', 'PDV', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', '1', 'INTEGER', false)
ON CONFLICT (id) DO NOTHING;

-- Inserir dados na tabela sistema_logs
INSERT INTO hermes_pdv.sistema_logs (id, nivel, modulo, mensagem, dados_adicionais, usuario_id, ip_address, data_registro, usuario_responsavel, ip_origem, dados_complementares) VALUES
(1, 'INFO', 'SISTEMA', 'Sistema inicializado com sucesso', '{"version": "3.0.2", "schema": "hermes_pdv"}', '1', '127.0.0.1', CURRENT_TIMESTAMP - INTERVAL '5 days', 'admin', '127.0.0.1', '{"action": "startup"}'),
(2, 'INFO', 'VENDAS', 'Venda V001 concluída com sucesso', '{"valor": 3489.90, "cliente": "João Silva"}', '2', '192.168.1.100', CURRENT_TIMESTAMP - INTERVAL '2 days', 'vendedor1', '192.168.1.100', '{"venda_id": 1}'),
(3, 'INFO', 'ESTOQUE', 'Movimentação de entrada registrada', '{"produto": "Smartphone Galaxy S21", "quantidade": 20}', '1', '127.0.0.1', CURRENT_TIMESTAMP - INTERVAL '5 days', 'admin', '127.0.0.1', '{"movimento_id": 1}'),
(4, 'WARN', 'ESTOQUE', 'Estoque baixo detectado', '{"produto": "Notebook Dell Inspiron", "estoque_atual": 8, "estoque_minimo": 3}', '1', '127.0.0.1', CURRENT_TIMESTAMP - INTERVAL '1 day', 'admin', '127.0.0.1', '{"alert_type": "low_stock"}')
ON CONFLICT (id) DO NOTHING;

COMMIT;
