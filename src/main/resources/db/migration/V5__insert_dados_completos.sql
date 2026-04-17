-- V5__insert_dados_completos.sql
-- Script de inserção de dados completos para todas as tabelas do sistema PDV Hermes Comercial
-- Data: 2024-04-17
-- Autor: Sistema Hermes Comercial

-- =================================================================
-- INSERÇÃO DE USUÁRIOS (CLIENTES, FUNCIONÁRIOS E FORNECEDORES)
-- =================================================================

-- Inserção de Clientes (TIPO_USUARIO = 'CLIENTE')
INSERT INTO USUARIO (NOME, TIPO_USUARIO, TIPO_PESSOA, CPF, RG, DATA_NASCIMENTO, CNPJ, NOME_FANTASIA, INSCRICAO_ESTADUAL, TELEFONE, CELULAR, WHATSAPP, EMAIL, ENDERECO, NUMERO, COMPLEMENTO, BAIRRO, CIDADE, ESTADO, CEP, OBSERVACOES) VALUES
('João Silva', 'CLIENTE', 'F', '123.456.789-00', 'MG-12.345.678', '1985-03-15', NULL, NULL, NULL, '(31) 3333-4444', '(31) 99999-8888', '(31) 99999-8888', 'joao.silva@email.com', 'Rua das Flores', '123', 'Apto 201', 'Centro', 'Belo Horizonte', 'MG', '30100-000', 'Cliente regular'),
('Maria Santos', 'CLIENTE', 'F', '987.654.321-00', 'MG-98.765.432', '1990-07-22', NULL, NULL, NULL, '(31) 5555-6666', '(31) 97777-6666', '(31) 97777-6666', 'maria.santos@email.com', 'Avenida Brasil', '456', 'Casa 2', 'Savassi', 'Belo Horizonte', 'MG', '30140-000', 'Cliente VIP'),
('Pedro Oliveira', 'CLIENTE', 'J', NULL, NULL, NULL, '12.345.678/0001-90', 'Oliveira & Cia Ltda', 'MG-12.345.678', '(31) 7777-8888', '(31) 96666-5555', '(31) 96666-5555', 'pedro@oliveira.com.br', 'Rua Comércio', '789', 'Sala 305', 'Funcionários', 'Belo Horizonte', 'MG', '30130-010', 'Empresa cliente'),
('Ana Costa', 'CLIENTE', 'F', '456.789.123-00', 'MG-45.678.912', '1988-11-30', NULL, NULL, NULL, '(31) 9999-0000', '(31) 95555-4444', '(31) 95555-4444', 'ana.costa@email.com', 'Rua das Árvores', '321', NULL, 'Lourdes', 'Belo Horizonte', 'MG', '30160-000', 'Cliente desde 2020'),
('Carlos Ferreira', 'CLIENTE', 'F', '789.123.456-00', 'MG-78.123.456', '1975-05-10', NULL, NULL, NULL, '(31) 8888-9999', '(31) 94444-3333', '(31) 94444-3333', 'carlos.ferreira@email.com', 'Alameda Paulista', '654', 'Bloco B', 'Sion', 'Belo Horizonte', 'MG', '30140-070', 'Cliente corporativo');

-- Inserção de Funcionários (TIPO_USUARIO = 'FUNCIONARIO')
INSERT INTO USUARIO (NOME, TIPO_USUARIO, TIPO_PESSOA, CPF, RG, DATA_NASCIMENTO, CNPJ, NOME_FANTASIA, INSCRICAO_ESTADUAL, TELEFONE, CELULAR, WHATSAPP, EMAIL, ENDERECO, NUMERO, COMPLEMENTO, BAIRRO, CIDADE, ESTADO, CEP, OBSERVACOES) VALUES
('Roberto Administrador', 'FUNCIONARIO', 'F', '111.222.333-44', 'MG-11.222.333', '1980-01-15', NULL, NULL, NULL, '(31) 3333-1111', '(31) 98888-7777', '(31) 98888-7777', 'roberto@hermes.com.br', 'Rua Principal', '100', NULL, 'Centro', 'Belo Horizonte', 'MG', '30100-001', 'Administrador do sistema'),
('Juliana Vendedora', 'FUNCIONARIO', 'F', '555.666.777-88', 'MG-55.666.777', '1992-08-20', NULL, NULL, NULL, '(31) 4444-2222', '(31) 97777-6666', '(31) 97777-6666', 'juliana@hermes.com.br', 'Avenida Central', '200', 'Apto 501', 'Barro Preto', 'Belo Horizonte', 'MG', '30220-000', 'Vendedora sênior'),
('Marcos Caixa', 'FUNCIONARIO', 'F', '999.888.777-66', 'MG-99.888.777', '1987-12-05', NULL, NULL, NULL, '(31) 5555-3333', '(31) 96666-5555', '(31) 96666-5555', 'marcos@hermes.com.br', 'Rua das Pedras', '300', 'Casa', 'Santa Tereza', 'Belo Horizonte', 'MG', '30240-000', 'Operador de caixa');

-- Inserção de Fornecedores (TIPO_USUARIO = 'FORNECEDOR')
INSERT INTO USUARIO (NOME, TIPO_USUARIO, TIPO_PESSOA, CPF, RG, DATA_NASCIMENTO, CNPJ, NOME_FANTASIA, INSCRICAO_ESTADUAL, TELEFONE, CELULAR, WHATSAPP, EMAIL, ENDERECO, NUMERO, COMPLEMENTO, BAIRRO, CIDADE, ESTADO, CEP, OBSERVACOES) VALUES
('Distribuidora ABC Ltda', 'FORNECEDOR', 'J', NULL, NULL, NULL, '34.567.890/0001-23', 'ABC Distribuidora', 'MG-345.678.901', '(31) 3333-4444', '(31) 98888-7777', '(31) 98888-7777', 'vendas@abc.com.br', 'Rua Industrial', '1000', 'Galpão A', 'Industrial', 'Belo Horizonte', 'MG', '30800-000', 'Fornecedor de eletrônicos'),
('Tech Solutions S.A.', 'FORNECEDOR', 'J', NULL, NULL, NULL, '45.678.901/0002-34', 'Tech Solutions', 'MG-456.789.012', '(31) 4444-5555', '(31) 97777-6666', '(31) 97777-6666', 'contato@techsolutions.com.br', 'Avenida Tecnologia', '500', 'Sala 1000', 'Cidade Industrial', 'Belo Horizonte', 'MG', '30810-000', 'Fornecedor de informática'),
('Paper Importações Ltda', 'FORNECEDOR', 'J', NULL, NULL, NULL, '56.789.012/0003-45', 'Paper Import', 'MG-567.890.123', '(31) 5555-6666', '(31) 96666-5555', '(31) 96666-5555', 'comercial@paper.com.br', 'Rua do Comércio', '750', 'Loja B', 'Centro', 'Belo Horizonte', 'MG', '30100-000', 'Fornecedor de papelaria');

-- =================================================================
-- INSERÇÃO DE GRUPOS DE IMPOSTOS
-- =================================================================

INSERT INTO GRUPO_IMPOSTOS (NOME_GRUPO, DESCRICAO, ATIVO, DATA_CRIACAO) VALUES
('Eletrônicos', 'Grupo de impostos para produtos eletrônicos', TRUE, '2024-04-17'),
('Informática', 'Grupo de impostos para produtos de informática', TRUE, '2024-04-17'),
('Games', 'Grupo de impostos para jogos e games', TRUE, '2024-04-17'),
('Livros', 'Grupo de impostos para livros e publicações', TRUE, '2024-04-17'),
('Papelaria', 'Grupo de impostos para produtos de papelaria', TRUE, '2024-04-17'),
('Escritório', 'Grupo de impostos para móveis de escritório', TRUE, '2024-04-17'),
('Alimentos', 'Grupo de impostos para alimentos e bebidas', TRUE, '2024-04-17'),
('Limpeza', 'Grupo de impostos para produtos de limpeza', TRUE, '2024-04-17'),
('Ferramentas', 'Grupo de impostos para ferramentas em geral', TRUE, '2024-04-17'),
('Isentos', 'Grupo de impostos para produtos isentos', TRUE, '2024-04-17');

-- =================================================================
-- INSERÇÃO DE PRODUTOS
-- =================================================================

-- Eletrônicos (ID_GRUPO_IMPOSTO = 1)
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('Smartphone Samsung Galaxy A54', 'Eletrônicos', 'Celulares', 'CEL001', 'Samsung', '7890123456789012', 1899.99, 1200.00, 'UN', 1),
('Notebook Dell Inspiron 15', 'Eletrônicos', 'Notebooks', 'NOT001', 'Dell', '7890123456789015', 3499.99, 2800.00, 'UN', 1),
('Mouse Gamer Logitech G502', 'Eletrônicos', 'Acessórios', 'MOU001', 'Logitech', '7890123456789023', 189.99, 120.00, 'UN', 1),
('Teclado Mecânico Redragon K552', 'Eletrônicos', 'Acessórios', 'TEC001', 'Redragon', '7890123456789024', 289.99, 180.00, 'UN', 1),
('Monitor LG 27" 4K', 'Eletrônicos', 'Monitores', 'MON001', 'LG', '7890123456789025', 2299.99, 1800.00, 'UN', 1),
('Headset Bluetooth JBL Tune 750', 'Eletrônicos', 'Áudio', 'AUD001', 'JBL', '7890123456789026', 399.99, 250.00, 'UN', 1),
('Webcam Full HD 1080p', 'Eletrônicos', 'Acessórios', 'WEB001', 'Genérico', '7890123456789027', 129.99, 80.00, 'UN', 1),
('SSD 500GB NVMe', 'Eletrônicos', 'Armazenamento', 'SSD001', 'Kingston', '7890123456789028', 249.99, 180.00, 'UN', 1),
('Memória RAM 16GB DDR4', 'Eletrônicos', 'Acessórios', 'RAM001', 'Corsair', '7890123456789029', 189.99, 120.00, 'UN', 1),
('Carregador Portátil Universal', 'Eletrônicos', 'Acessórios', 'CAR001', 'Genérico', '7890123456789030', 59.99, 35.00, 'UN', 1);

-- Informática (ID_GRUPO_IMPOSTO = 2)
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('Windows 11 Pro', 'Software', 'Sistemas Operacionais', 'SO001', 'Microsoft', '7890123456789031', 899.99, 450.00, 'UN', 2),
('Microsoft Office 365', 'Software', 'Suíte Office', 'OFF001', 'Microsoft', '7890123456789032', 699.99, 350.00, 'UN', 2),
('Antivírus Kaspersky', 'Software', 'Segurança', 'SEG001', 'Kaspersky', '7890123456789033', 199.99, 80.00, 'UN', 2),
('Impressora Multifuncional HP', 'Informática', 'Impressoras', 'IMP001', 'HP', '7890123456789034', 1299.99, 900.00, 'UN', 2),
('Roteador Wi-Fi TP-Link', 'Informática', 'Rede', 'ROT001', 'TP-Link', '7890123456789035', 289.99, 180.00, 'UN', 2),
('Switch 8 Portas Gigabit', 'Informática', 'Rede', 'SWI001', 'TP-Link', '7890123456789036', 399.99, 250.00, 'UN', 2),
('Nobreak 600VA', 'Informática', 'Energia', 'NOB001', 'APC', '7890123456789037', 449.99, 320.00, 'UN', 2),
('HD Externo 1TB', 'Informática', 'Armazenamento', 'HDE001', 'Seagate', '7890123456789039', 349.99, 250.00, 'UN', 2),
('Placa Mãe Gamer', 'Informática', 'Hardware', 'PLA001', 'ASUS', '7890123456789040', 899.99, 650.00, 'UN', 2);

-- Games (ID_GRUPO_IMPOSTO = 3)
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('PlayStation 5', 'Games', 'Consoles', 'CON001', 'Sony', '7890123456789041', 3999.99, 3200.00, 'UN', 3),
('Xbox Series X', 'Games', 'Consoles', 'CON002', 'Microsoft', '7890123456789042', 3999.99, 3200.00, 'UN', 3),
('Nintendo Switch OLED', 'Games', 'Consoles', 'CON003', 'Nintendo', '7890123456789043', 2799.99, 2200.00, 'UN', 3),
('The Last of Us Part I', 'Games', 'PC Games', 'PCG001', 'Epic Games', '7890123456789044', 199.99, 120.00, 'UN', 3),
('FIFA 24 Standard', 'Games', 'PC Games', 'PCG002', 'EA Sports', '7890123456789045', 299.99, 180.00, 'UN', 3),
('Call of Duty Modern Warfare III', 'Games', 'PC Games', 'PCG003', 'Activision', '7890123456789046', 349.99, 210.00, 'UN', 3),
('Controle DualSense PS5', 'Games', 'Acessórios', 'ACO001', 'Sony', '7890123456789047', 399.99, 280.00, 'UN', 3),
('Headset Gamer Razer BlackShark', 'Games', 'Acessórios', 'ACO002', 'Razer', '7890123456789048', 599.99, 380.00, 'UN', 3),
('Mouse Gamer Razer Viper', 'Games', 'Acessórios', 'ACO003', 'Razer', '7890123456789049', 399.99, 250.00, 'UN', 3),
('Steam Gift Card R$50', 'Games', 'Gift Cards', 'GIF001', 'Steam', '7890123456789050', 50.00, 45.00, 'UN', 3);

-- Livros (ID_GRUPO_IMPOSTO = 4)
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('Livro - Programação Java', 'Livros', 'Técnicos', 'LIV001', 'Novatec', '7890123456789051', 89.99, 65.00, 'UN', 4),
('Livro - Design Patterns', 'Livros', 'Técnicos', 'LIV002', 'Novatec', '7890123456789052', 79.99, 55.00, 'UN', 4),
('Livro - Spring Boot', 'Livros', 'Técnicos', 'LIV003', 'Novatec', '7890123456789053', 99.99, 70.00, 'UN', 4),
('Livro - React Native', 'Livros', 'Técnicos', 'LIV004', 'Novatec', '7890123456789054', 109.99, 80.00, 'UN', 4),
('Livro - JavaScript Avançado', 'Livros', 'Técnicos', 'LIV005', 'Novatec', '7890123456789055', 89.99, 60.00, 'UN', 4),
('Kindle Paperwhite', 'Livros', 'E-readers', 'ERE001', 'Amazon', '7890123456789056', 599.99, 450.00, 'UN', 4);

-- Papelaria (ID_GRUPO_IMPOSTO = 5)
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('Caderno Universitário 200fl', 'Papelaria', 'Cadernos', 'CAD001', 'Tilibra', '7890123456789057', 29.99, 18.00, 'UN', 5),
('Caneta Esferográfica BIC', 'Papelaria', 'Canetas', 'CAN001', 'BIC', '7890123456789058', 3.99, 2.00, 'UN', 5);

-- Escritório (ID_GRUPO_IMPOSTO = 6)
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('Cadeira Executiva Giratória', 'Móveis', 'Cadeiras', 'CAD002', 'Madeireira', '7890123456789059', 599.99, 380.00, 'UN', 6),
('Mesa Escritório 1,20x0,60m', 'Móveis', 'Mesas', 'MES002', 'Madeireira', '7890123456789060', 799.99, 500.00, 'UN', 6),
('Armário 4 Gavetas', 'Móveis', 'Armários', 'ARM002', 'Madeireira', '7890123456789061', 899.99, 600.00, 'UN', 6),
('Estante de Livros 5 Prateleiras', 'Móveis', 'Estantes', 'EST002', 'Madeireira', '7890123456789062', 449.99, 320.00, 'UN', 6),
('Luminária de Mesa LED', 'Móveis', 'Iluminação', 'LUM002', 'Genérico', '7890123456789063', 159.99, 90.00, 'UN', 6),
('Calculadora Científica', 'Escritório', 'Acessórios', 'CAL002', 'Casio', '7890123456789064', 89.99, 55.00, 'UN', 6),
('Organizador de Mesa', 'Escritório', 'Acessórios', 'ORG002', 'Genérico', '7890123456789065', 79.99, 45.00, 'UN', 6);

-- Alimentos (ID_GRUPO_IMPOSTO = 7)
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('Café 500g Torrado', 'Alimentos', 'Café', 'CAF002', 'Café Especial', '7890123456789066', 24.99, 15.00, 'UN', 7),
('Pão de Forma Integral', 'Alimentos', 'Padaria', 'PAD002', 'Panificadora', '7890123456789067', 8.99, 4.50, 'UN', 7),
('Água Mineral 500ml', 'Alimentos', 'Bebidas', 'BEB002', 'Crystal', '7890123456789068', 2.99, 1.20, 'UN', 7),
('Refrigerante Lata 350ml', 'Alimentos', 'Bebidas', 'BEB003', 'Coca-Cola', '7890123456789069', 4.99, 2.50, 'UN', 7),
('Chocolate ao Leite 90g', 'Alimentos', 'Doces', 'DOC002', 'Nestlé', '7890123456789070', 6.99, 4.00, 'UN', 7),
('Salgadinho Potato Chips 100g', 'Alimentos', 'Snacks', 'SNA002', 'Elma Chips', '7890123456789071', 8.99, 5.50, 'UN', 7),
('Biscoito Recheado 100g', 'Alimentos', 'Biscoitos', 'BIS002', 'Nestlé', '7890123456789072', 5.99, 3.50, 'UN', 7),
('Iogurte Natural 200g', 'Alimentos', 'Laticínios', 'LAT002', 'Nestlé', '7890123456789073', 7.99, 4.80, 'UN', 7),
('Queijo Mussarela 200g', 'Alimentos', 'Laticínios', 'QUE002', 'Nestlé', '7890123456789074', 12.99, 8.00, 'UN', 7),
('Presunto Cozido 500g', 'Alimentos', 'Frios', 'FRI002', 'Sadia', '7890123456789075', 18.99, 12.00, 'UN', 7);

-- Limpeza (ID_GRUPO_IMPOSTO = 8)
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('Detergente Líquido 1L', 'Limpeza', 'Detergentes', 'DET002', 'Ypê', '7890123456789076', 12.99, 8.00, 'UN', 8),
('Sabão em Pó 1kg', 'Limpeza', 'Sabões', 'SAB002', 'Omo', '7890123456789077', 15.99, 10.00, 'UN', 8),
('Álcool 70% 1L', 'Limpeza', 'Álcool', 'ALC002', 'Genérico', '7890123456789078', 8.99, 5.00, 'UN', 8),
('Papel Higiênico 4 unidades', 'Limpeza', 'Papel Higiênico', 'PAH002', 'Genérico', '7890123456789079', 4.99, 2.50, 'UN', 8),
('Limpador Multiuso 500ml', 'Limpeza', 'Limpadores', 'LIM002', 'Cif', '7890123456789080', 9.99, 6.00, 'UN', 8),
('Vassoura e Pá', 'Limpeza', 'Vassouras', 'VAS002', 'Genérico', '7890123456789081', 19.99, 12.00, 'UN', 8),
('Balde 10L com Roda', 'Limpeza', 'Balde', 'BAL002', 'Genérico', '7890123456789082', 35.99, 22.00, 'UN', 8),
('Pano de Chão', 'Limpeza', 'Panos', 'PAN002', 'Genérico', '7890123456789083', 15.99, 9.00, 'UN', 8),
('Luvas de Látex', 'Limpeza', 'Luvas', 'LUV002', 'Genérico', '7890123456789084', 12.99, 8.00, 'UN', 8);

-- Ferramentas (ID_GRUPO_IMPOSTO = 9)
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('Chave de Fenda Phillips', 'Ferramentas', 'Ferramentas Manuais', 'FER010', 'Phillips', '7890123456789085', 89.99, 65.00, 'UN', 9),
('Chave de Boca Stanley', 'Ferramentas', 'Ferramentas Manuais', 'FER011', 'Stanley', '7890123456789086', 45.99, 32.00, 'UN', 9),
('Alicate Universal', 'Ferramentas', 'Ferramentas Manuais', 'FER012', 'Stanley', '7890123456789087', 59.99, 38.00, 'UN', 9),
('Martelo de Carpinteiro', 'Ferramentas', 'Ferramentas Manuais', 'FER013', 'Tramontina', '7890123456789088', 79.99, 55.00, 'UN', 9),
('Serra Circular Makita', 'Ferramentas', 'Ferramentas Elétricas', 'FER014', 'Makita', '7890123456789089', 599.99, 420.00, 'UN', 9),
('Furadeira Bosch', 'Ferramentas', 'Ferramentas Elétricas', 'FER015', 'Bosch', '7890123456789090', 449.99, 320.00, 'UN', 9),
('Parafusadeira 18V', 'Ferramentas', 'Ferramentas Elétricas', 'FER016', 'Bosch', '7890123456789091', 399.99, 280.00, 'UN', 9),
('Chave de Roda Automotiva', 'Ferramentas', 'Ferramentas Automotivas', 'FER017', 'Genérico', '7890123456789092', 39.99, 25.00, 'UN', 9),
('Jogo de Chaves Completo', 'Ferramentas', 'Ferramentas Manuais', 'FER018', 'Genérico', '7890123456789093', 129.99, 85.00, 'UN', 9),
('Fita Métrica 5m', 'Ferramentas', 'Medição', 'MED003', 'Stanley', '7890123456789094', 29.99, 18.00, 'UN', 9),
('Nível de Bolha', 'Ferramentas', 'Medição', 'MED004', 'Stanley', '7890123456789095', 45.99, 28.00, 'UN', 9);

-- =================================================================
-- INSERÇÃO DE ESTOQUE
-- =================================================================

INSERT INTO ESTOQUE (FK_PRODUTO, QUANTIDADE, MAXIMO, MINIMO) 
SELECT p.ID, 100, 1000, 10 FROM PRODUTO p WHERE p.ID >= (SELECT COALESCE(MIN(ID), 1) FROM PRODUTO);

-- =================================================================
-- INSERÇÃO DE LOGIN (FUNCIONÁRIOS)
-- =================================================================

INSERT INTO LOGIN (FK_USUARIO, LOGIN, SENHA) 
SELECT u.ID, 'admin', 'admin123' FROM USUARIO u WHERE u.NOME = 'Roberto Administrador' AND u.TIPO_USUARIO = 'FUNCIONARIO'
UNION ALL
SELECT u.ID, 'juliana', 'julia123' FROM USUARIO u WHERE u.NOME = 'Juliana Vendedora' AND u.TIPO_USUARIO = 'FUNCIONARIO'
UNION ALL
SELECT u.ID, 'marcos', 'marcos123' FROM USUARIO u WHERE u.NOME = 'Marcos Caixa' AND u.TIPO_USUARIO = 'FUNCIONARIO';

-- =================================================================
-- INSERÇÃO DE IMPOSTOS
-- =================================================================

INSERT INTO IMPOSTO (ID_GRUPO_IMPOSTO, ORIGEM_MERCADORIA, CST_ICMS, CSOSN, ALIQUOTA_ICMS, ALIQUOTA_COFINS, ALIQUOTA_IPI, ALIQUOTA_PIS, CFOP, REDUCAO_BASE) VALUES
(1, 0, '00', '102', 0.18, 0.03, 0.15, 0.0065, 5102, 0.00), -- Eletrônicos
(2, 0, '00', '102', 0.18, 0.03, 0.15, 0.0065, 5102, 0.00), -- Informática
(3, 0, '00', '102', 0.18, 0.03, 0.15, 0.0065, 5102, 0.00), -- Games
(4, 0, '00', '102', 0.00, 0.03, 0.00, 0.0065, 5102, 0.00), -- Livros (isento de ICMS)
(5, 0, '00', '102', 0.18, 0.03, 0.15, 0.0065, 5102, 0.00), -- Papelaria
(6, 0, '00', '102', 0.18, 0.03, 0.15, 0.0065, 5102, 0.00), -- Escritório
(7, 0, '00', '102', 0.18, 0.03, 0.15, 0.0065, 5102, 0.00), -- Alimentos
(8, 0, '00', '102', 0.18, 0.03, 0.15, 0.0065, 5102, 0.00), -- Limpeza
(9, 0, '00', '102', 0.18, 0.03, 0.15, 0.0065, 5102, 0.00), -- Ferramentas
(10, 0, '00', '102', 0.00, 0.03, 0.00, 0.0065, 5102, 0.00); -- Isentos

-- =================================================================
-- INSERÇÃO DE DESPESAS
-- =================================================================

INSERT INTO DESPESA (TIPO, VALOR) VALUES
('Aluguel', 2500.00),
('Água', 150.00),
('Luz', 450.00),
('Telefone', 120.00),
('Internet', 150.00),
('Material de Limpeza', 80.00),
('Segurança', 300.00),
('Manutenção', 200.00);

-- =================================================================
-- INSERÇÃO DE CUSTOS (FORNECEDORES)
-- =================================================================

INSERT INTO CUSTO (FK_FORNECEDOR, CUSTOUNITARIO, CUSTOTOTAL) VALUES
(9, 1000.00, 10000.00), -- Distribuidora ABC
(10, 500.00, 15000.00), -- Tech Solutions
(11, 50.00, 5000.00);   -- Paper Importações

-- =================================================================
-- INSERÇÃO DE CAIXA
-- =================================================================

INSERT INTO CAIXA (VALOR, TIPO) VALUES
(1000.00, 'ABERTURA'),
(500.00, 'SANGRIA'),
(200.00, 'SUPRIMENTO'),
(100.00, 'FECHAMENTO');

-- =================================================================
-- INSERÇÃO DE ATRIBUTOS
-- =================================================================

INSERT INTO ATRIBUTO (IMPOSTOFEDERAL, IMPOSTOESTADUAL, IMPOSTOMUNICIPAL) VALUES
(0.15, 0.18, 0.05);

-- =================================================================
-- INSERÇÃO DE ALERTA ESTOQUE
-- =================================================================

INSERT INTO ALERTAESTOQUE (VALOR, TEMPOESTOQUE) VALUES
('10', '7 dias'),
('5', '3 dias');

-- =================================================================
-- INSERÇÃO DE VENDAS PDV (EXEMPLOS)
-- =================================================================

INSERT INTO VENDA_PDV (NUMERO_CUPOM, FK_CLIENTE, FK_OPERADOR, FK_TERMINAL, VALOR_TOTAL, VALOR_FINAL, STATUS) 
SELECT 'CUPOM-2024-001', c.ID, o.ID, 1, 1899.99, 1899.99, 'CONCLUIDA' 
FROM USUARIO c, USUARIO o 
WHERE c.NOME = 'João Silva' AND c.TIPO_USUARIO = 'CLIENTE' 
AND o.NOME = 'Juliana Vendedora' AND o.TIPO_USUARIO = 'FUNCIONARIO'
UNION ALL
SELECT 'CUPOM-2024-002', c.ID, o.ID, 1, 3499.99, 3499.99, 'CONCLUIDA' 
FROM USUARIO c, USUARIO o 
WHERE c.NOME = 'Maria Santos' AND c.TIPO_USUARIO = 'CLIENTE' 
AND o.NOME = 'Juliana Vendedora' AND o.TIPO_USUARIO = 'FUNCIONARIO'
UNION ALL
SELECT 'CUPOM-2024-003', c.ID, o.ID, 1, 3999.99, 3999.99, 'CONCLUIDA' 
FROM USUARIO c, USUARIO o 
WHERE c.NOME = 'Pedro Oliveira' AND c.TIPO_USUARIO = 'CLIENTE' 
AND o.NOME = 'Marcos Caixa' AND o.TIPO_USUARIO = 'FUNCIONARIO'
UNION ALL
SELECT 'CUPOM-2024-004', c.ID, o.ID, 1, 89.99, 89.99, 'CONCLUIDA' 
FROM USUARIO c, USUARIO o 
WHERE c.NOME = 'Ana Costa' AND c.TIPO_USUARIO = 'CLIENTE' 
AND o.NOME = 'Juliana Vendedora' AND o.TIPO_USUARIO = 'FUNCIONARIO'
UNION ALL
SELECT 'CUPOM-2024-005', c.ID, o.ID, 1, 599.99, 599.99, 'CONCLUIDA' 
FROM USUARIO c, USUARIO o 
WHERE c.NOME = 'Carlos Ferreira' AND c.TIPO_USUARIO = 'CLIENTE' 
AND o.NOME = 'Marcos Caixa' AND o.TIPO_USUARIO = 'FUNCIONARIO';

-- =================================================================
-- INSERÇÃO DE ITENS DE VENDA
-- =================================================================

INSERT INTO ITEM_VENDA (FK_VENDA_PDV, FK_PRODUTO, QUANTIDADE, VALOR_UNITARIO, VALOR_FINAL) 
SELECT v.ID, p.ID, 1, 1899.99, 1899.99 
FROM VENDA_PDV v, PRODUTO p 
WHERE v.NUMERO_CUPOM = 'CUPOM-2024-001' AND p.CODIGO = 'CEL001'
UNION ALL
SELECT v.ID, p.ID, 1, 3499.99, 3499.99 
FROM VENDA_PDV v, PRODUTO p 
WHERE v.NUMERO_CUPOM = 'CUPOM-2024-002' AND p.CODIGO = 'NOT001'
UNION ALL
SELECT v.ID, p.ID, 1, 3999.99, 3999.99 
FROM VENDA_PDV v, PRODUTO p 
WHERE v.NUMERO_CUPOM = 'CUPOM-2024-003' AND p.CODIGO = 'CON001'
UNION ALL
SELECT v.ID, p.ID, 1, 89.99, 89.99 
FROM VENDA_PDV v, PRODUTO p 
WHERE v.NUMERO_CUPOM = 'CUPOM-2024-004' AND p.CODIGO = 'LIV001'
UNION ALL
SELECT v.ID, p.ID, 1, 599.99, 599.99 
FROM VENDA_PDV v, PRODUTO p 
WHERE v.NUMERO_CUPOM = 'CUPOM-2024-005' AND p.CODIGO = 'CAD002';

-- =================================================================
-- INSERÇÃO DE PAGAMENTOS
-- =================================================================

INSERT INTO PAGAMENTO (FK_VENDA_PDV, TIPO_PAGAMENTO, VALOR_PAGO, VALOR_TROCO, NUMERO_PARCELAS) 
SELECT v.ID, 'DINHEIRO', 2000.00, 100.01, 1 
FROM VENDA_PDV v WHERE v.NUMERO_CUPOM = 'CUPOM-2024-001'
UNION ALL
SELECT v.ID, 'CARTAO_CREDITO', 3499.99, 0.00, 12 
FROM VENDA_PDV v WHERE v.NUMERO_CUPOM = 'CUPOM-2024-002'
UNION ALL
SELECT v.ID, 'PIX', 3999.99, 0.00, 1 
FROM VENDA_PDV v WHERE v.NUMERO_CUPOM = 'CUPOM-2024-003'
UNION ALL
SELECT v.ID, 'DINHEIRO', 100.00, 10.01, 1 
FROM VENDA_PDV v WHERE v.NUMERO_CUPOM = 'CUPOM-2024-004'
UNION ALL
SELECT v.ID, 'CARTAO_DEBITO', 599.99, 0.00, 1 
FROM VENDA_PDV v WHERE v.NUMERO_CUPOM = 'CUPOM-2024-005';

-- =================================================================
-- INSERÇÃO DE CARRINHO DE COMPRAS (EXEMPLOS)
-- =================================================================

INSERT INTO CARRINHO_COMPRA (FK_SESSAO, FK_PRODUTO, FK_CLIENTE, FK_OPERADOR, QUANTIDADE, VALOR_UNITARIO, VALOR_FINAL) 
SELECT 'SESSION-001', p.ID, c.ID, o.ID, 1, 1899.99, 1899.99 
FROM PRODUTO p, USUARIO c, USUARIO o 
WHERE p.CODIGO = 'CEL001' AND c.NOME = 'João Silva' AND c.TIPO_USUARIO = 'CLIENTE' 
AND o.NOME = 'Juliana Vendedora' AND o.TIPO_USUARIO = 'FUNCIONARIO'
UNION ALL
SELECT 'SESSION-002', p.ID, c.ID, o.ID, 1, 3499.99, 3499.99 
FROM PRODUTO p, USUARIO c, USUARIO o 
WHERE p.CODIGO = 'NOT001' AND c.NOME = 'Maria Santos' AND c.TIPO_USUARIO = 'CLIENTE' 
AND o.NOME = 'Juliana Vendedora' AND o.TIPO_USUARIO = 'FUNCIONARIO'
UNION ALL
SELECT 'SESSION-003', p.ID, c.ID, o.ID, 1, 3999.99, 3999.99 
FROM PRODUTO p, USUARIO c, USUARIO o 
WHERE p.CODIGO = 'CON001' AND c.NOME = 'Pedro Oliveira' AND c.TIPO_USUARIO = 'CLIENTE' 
AND o.NOME = 'Marcos Caixa' AND o.TIPO_USUARIO = 'FUNCIONARIO';

-- =================================================================
-- INSERÇÃO DE SESSÃO PDV
-- =================================================================

INSERT INTO SESSAO_PDV (FK_OPERADOR, FK_TERMINAL, VALOR_ABERTURA, STATUS) 
SELECT u.ID, 1, 1000.00, 'ABERTA' 
FROM USUARIO u WHERE u.NOME = 'Juliana Vendedora' AND u.TIPO_USUARIO = 'FUNCIONARIO'
UNION ALL
SELECT u.ID, 2, 500.00, 'ABERTA' 
FROM USUARIO u WHERE u.NOME = 'Marcos Caixa' AND u.TIPO_USUARIO = 'FUNCIONARIO';

-- =================================================================
-- INSERÇÃO DE CUPOM FISCAL
-- =================================================================

INSERT INTO CUPOM_FISCAL (FK_VENDA_PDV, NUMERO_CUPOM, CHAVE_ACESSO, PROTOCOLO_AUTORIZACAO, STATUS) 
SELECT v.ID, 'CUPOM-2024-001', '12345678901234567890123456789012345678901234', '123456789012345', 'EMITIDO' 
FROM VENDA_PDV v WHERE v.NUMERO_CUPOM = 'CUPOM-2024-001'
UNION ALL
SELECT v.ID, 'CUPOM-2024-002', '23456789012345678901234567890123456789012345', '234567890123456', 'EMITIDO' 
FROM VENDA_PDV v WHERE v.NUMERO_CUPOM = 'CUPOM-2024-002'
UNION ALL
SELECT v.ID, 'CUPOM-2024-003', '34567890123456789012345678901234567890123456', '345678901234567', 'EMITIDO' 
FROM VENDA_PDV v WHERE v.NUMERO_CUPOM = 'CUPOM-2024-003';

-- =================================================================
-- INSERÇÃO DE CONFIGURAÇÕES PDV ADICIONAIS
-- =================================================================

INSERT INTO CONFIG_PDV (CHAVE, VALOR, DESCRICAO, TIPO) VALUES
('EMPRESA_NOME', 'Hermes Comercial Ltda', 'Nome da empresa', 'STRING'),
('EMPRESA_TELEFONE', '(31) 3333-4444', 'Telefone da empresa', 'STRING'),
('EMPRESA_EMAIL', 'contato@hermescomercial.com.br', 'Email da empresa', 'STRING')
ON CONFLICT (CHAVE) DO NOTHING;

-- Log da migração
INSERT INTO CONFIG_PDV (CHAVE, VALOR, DESCRICAO, TIPO) VALUES
('MIGRATION_V5_EXECUTADA', 'TRUE', 'Script V5 de inserção de dados completos executado com sucesso', 'BOOLEAN');

COMMIT;
