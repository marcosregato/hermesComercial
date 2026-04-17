-- V4__insert_produtos_massa.sql
-- Script de migração para inserir 50 produtos de exemplo no sistema
-- Data: 2024-04-17
-- Autor: Sistema Hermes Comercial

-- Inserção de 50 produtos para testes e demonstração do sistema PDV
-- Produtos de diversas categorias para testar funcionalidades completas

-- Inserção de Eletrônicos (usando IDs dos grupos criados: Eletrônicos=1, Informática=2, Games=3, Livros=4, Papelaria=5, Escritório=6, Alimentos=7, Limpeza=8, Ferramentas=9, Isentos=10)
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

-- Inserção de Informática
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('Windows 11 Pro', 'Software', 'Sistemas Operacionais', 'SO001', 'Microsoft', '7890123456789031', 899.99, 450.00, 'UN', 2),
('Microsoft Office 365', 'Software', 'Suíte Office', 'OFF001', 'Microsoft', '7890123456789032', 699.99, 350.00, 'UN', 2),
('Antivírus Kaspersky', 'Software', 'Segurança', 'SEG001', 'Kaspersky', '7890123456789033', 199.99, 80.00, 'UN', 2),
('Impressora Multifuncional HP', 'Informática', 'Impressoras', 'IMP001', 'HP', '7890123456789034', 1299.99, 900.00, 'UN', 2),
('Roteador Wi-Fi TP-Link', 'Informática', 'Rede', 'ROT001', 'TP-Link', '7890123456789035', 289.99, 180.00, 'UN', 2),
('Switch 8 Portas Gigabit', 'Informática', 'Rede', 'SWI001', 'TP-Link', '7890123456789036', 399.99, 250.00, 'UN', 2),
('Nobreak 600VA', 'Informática', 'Energia', 'NOB001', 'APC', '7890123456789037', 449.99, 320.00, 'UN', 2),
('Cabo USB 2.0', 'Informática', 'Acessórios', 'CAB001', 'Genérico', '7890123456789038', 19.99, 8.00, 'UN', 2),
('HD Externo 1TB', 'Informática', 'Armazenamento', 'HDE001', 'Seagate', '7890123456789039', 349.99, 250.00, 'UN', 2);

-- Inserção de Games
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('PlayStation 5', 'Games', 'Consoles', 'CON001', 'Sony', '7890123456789040', 3999.99, 3200.00, 'UN', 3),
('Xbox Series X', 'Games', 'Consoles', 'CON002', 'Microsoft', '7890123456789041', 3999.99, 3200.00, 'UN', 3),
('Nintendo Switch OLED', 'Games', 'Consoles', 'CON003', 'Nintendo', '7890123456789042', 2799.99, 2200.00, 'UN', 3),
('The Last of Us Part I', 'Games', 'PC Games', 'PCG001', 'Epic Games', '7890123456789043', 199.99, 120.00, 'UN', 3),
('FIFA 24 Standard', 'Games', 'PC Games', 'PCG002', 'EA Sports', '7890123456789044', 299.99, 180.00, 'UN', 3),
('Call of Duty Modern Warfare III', 'Games', 'PC Games', 'PCG003', 'Activision', '7890123456789045', 349.99, 210.00, 'UN', 3),
('Controle DualSense PS5', 'Games', 'Acessórios', 'ACO001', 'Sony', '7890123456789046', 399.99, 280.00, 'UN', 3),
('Headset Gamer Razer BlackShark', 'Games', 'Acessórios', 'ACO002', 'Razer', '7890123456789047', 599.99, 380.00, 'UN', 3),
('Mouse Gamer Razer Viper', 'Games', 'Acessórios', 'ACO003', 'Razer', '7890123456789048', 399.99, 250.00, 'UN', 3),
('Steam Gift Card R$50', 'Games', 'Gift Cards', 'GIF001', 'Steam', '7890123456789049', 50.00, 45.00, 'UN', 3);

-- Inserção de Livraria
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('Livro - Programação Java', 'Livros', 'Técnicos', 'LIV001', 'Novatec', '78901234567890510', 89.99, 65.00, 'UN', 4),
('Livro - Design Patterns', 'Livros', 'Técnicos', 'LIV002', 'Novatec', '78901234567890511', 79.99, 55.00, 'UN', 4),
('Livro - Spring Boot', 'Livros', 'Técnicos', 'LIV003', 'Novatec', '78901234567890512', 99.99, 70.00, 'UN', 4),
('Livro - React Native', 'Livros', 'Técnicos', 'LIV004', 'Novatec', '78901234567890513', 109.99, 80.00, 'UN', 4),
('Livro - JavaScript Avançado', 'Livros', 'Técnicos', 'LIV005', 'Novatec', '78901234567890514', 89.99, 60.00, 'UN', 4),
('Kindle Paperwhite', 'Livros', 'E-readers', 'ERE001', 'Amazon', '78901234567890515', 599.99, 450.00, 'UN', 4),
('Caderno Universitário 200fl', 'Papelaria', 'Cadernos', 'CAD001', 'Tilibra', '78901234567890516', 29.99, 18.00, 'UN', 5),
('Caneta Esferográfica BIC', 'Papelaria', 'Canetas', 'CAN001', 'BIC', '78901234567890517', 3.99, 2.00, 'UN', 5),
('Dicionário Português', 'Livros', 'Referência', 'DIC001', 'Houaiss', '78901234567890518', 49.99, 35.00, 'UN', 4),
('Agenda 2024', 'Papelaria', 'Agendas', 'AGE001', 'Tilibra', '78901234567890519', 39.99, 25.00, 'UN', 5);

-- Inserção de Escritório
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('Cadeira Executiva Giratória', 'Móveis', 'Cadeiras', 'CAD001', 'Madeireira', '78901234567890520', 599.99, 380.00, 'UN', 6),
('Mesa Escritório 1,20x0,60m', 'Móveis', 'Mesas', 'MES001', 'Madeireira', '78901234567890521', 799.99, 500.00, 'UN', 6),
('Armário 4 Gavetas', 'Móveis', 'Armários', 'ARM001', 'Madeireira', '78901234567890522', 899.99, 600.00, 'UN', 6),
('Estante de Livros 5 Prateleiras', 'Móveis', 'Estantes', 'EST001', 'Madeireira', '78901234567890523', 449.99, 320.00, 'UN', 6),
('Luminária de Mesa LED', 'Móveis', 'Iluminação', 'LUM001', 'Genérico', '78901234567890524', 159.99, 90.00, 'UN', 6),
('Calculadora Científica', 'Escritório', 'Acessórios', 'CAL001', 'Casio', '78901234567890525', 89.99, 55.00, 'UN', 6),
('Organizador de Mesa', 'Escritório', 'Acessórios', 'ORG001', 'Genérico', '78901234567890526', 79.99, 45.00, 'UN', 6),
('Suporte para Notebook', 'Escritório', 'Acessórios', 'SUP001', 'Genérico', '78901234567890527', 99.99, 60.00, 'UN', 6),
('Pasta de Documentos A4', 'Escritório', 'Acessórios', 'PAS001', 'Genérico', '78901234567890528', 29.99, 18.00, 'UN', 6);

-- Inserção de Alimentos
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('Café 500g Torrado', 'Alimentos', 'Café', 'CAF001', 'Café Especial', '78901234567890530', 24.99, 15.00, 'UN', 7),
('Pão de Forma Integral', 'Alimentos', 'Padaria', 'PAD001', 'Panificadora', '78901234567890531', 8.99, 4.50, 'UN', 7),
('Água Mineral 500ml', 'Alimentos', 'Bebidas', 'BEB001', 'Crystal', '78901234567890532', 2.99, 1.20, 'UN', 7),
('Refrigerante Lata 350ml', 'Alimentos', 'Bebidas', 'BEB002', 'Coca-Cola', '78901234567890533', 4.99, 2.50, 'UN', 7),
('Chocolate ao Leite 90g', 'Alimentos', 'Doces', 'DOC001', 'Nestlé', '78901234567890534', 6.99, 4.00, 'UN', 7),
('Salgadinho Potato Chips 100g', 'Alimentos', 'Snacks', 'SNA001', 'Elma Chips', '78901234567890535', 8.99, 5.50, 'UN', 7),
('Biscoito Recheado 100g', 'Alimentos', 'Biscoitos', 'BIS001', 'Nestlé', '78901234567890536', 5.99, 3.50, 'UN', 7),
('Iogurte Natural 200g', 'Alimentos', 'Laticínios', 'LAT001', 'Nestlé', '78901234567890537', 7.99, 4.80, 'UN', 7),
('Queijo Mussarela 200g', 'Alimentos', 'Laticínios', 'QUE001', 'Nestlé', '78901234567890538', 12.99, 8.00, 'UN', 7),
('Presunto Cozido 500g', 'Alimentos', 'Frios', 'FRI001', 'Sadia', '78901234567890539', 18.99, 12.00, 'UN', 7);

-- Inserção de Limpeza
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('Detergente Líquido 1L', 'Limpeza', 'Detergentes', 'DET001', 'Ypê', '78901234567890540', 12.99, 8.00, 'UN', 8),
('Sabão em Pó 1kg', 'Limpeza', 'Sabões', 'SAB001', 'Omo', '78901234567890541', 15.99, 10.00, 'UN', 8),
('Álcool 70% 1L', 'Limpeza', 'Álcool', 'ALC001', 'Genérico', '78901234567890542', 8.99, 5.00, 'UN', 8),
('Papel Higiênico 4 unidades', 'Limpeza', 'Papel Higiênico', 'PAH001', 'Genérico', '78901234567890543', 4.99, 2.50, 'UN', 8),
('Limpador Multiuso 500ml', 'Limpeza', 'Limpadores', 'LIM001', 'Cif', '78901234567890544', 9.99, 6.00, 'UN', 8),
('Vassoura e Pá', 'Limpeza', 'Vassouras', 'VAS001', 'Genérico', '78901234567890545', 19.99, 12.00, 'UN', 8),
('Balde 10L com Roda', 'Limpeza', 'Balde', 'BAL001', 'Genérico', '78901234567890546', 35.99, 22.00, 'UN', 8),
('Pano de Chão', 'Limpeza', 'Panos', 'PAN001', 'Genérico', '78901234567890547', 15.99, 9.00, 'UN', 8),
('Luvas de Látex', 'Limpeza', 'Luvas', 'LUV001', 'Genérico', '78901234567890548', 12.99, 8.00, 'UN', 8);

-- Inserção de Ferramentas
INSERT INTO PRODUTO (NOME, CATEGORIA, SUBCATEGORIA, CODIGO, MARCA, CODIGO_BARRAS, PRECO_VENDA, PRECO_CUSTO, UNIDADE, ID_GRUPO_IMPOSTO) VALUES
('Chave de Fenda Phillips', 'Ferramentas', 'Ferramentas Manuais', 'FER001', 'Phillips', '78901234567890550', 89.99, 65.00, 'UN', 9),
('Chave de Boca Stanley', 'Ferramentas', 'Ferramentas Manuais', 'FER002', 'Stanley', '78901234567890551', 45.99, 32.00, 'UN', 9),
('Alicate Universal', 'Ferramentas', 'Ferramentas Manuais', 'FER003', 'Stanley', '78901234567890552', 59.99, 38.00, 'UN', 9),
('Martelo de Carpinteiro', 'Ferramentas', 'Ferramentas Manuais', 'FER004', 'Tramontina', '78901234567890553', 79.99, 55.00, 'UN', 9),
('Serra Circular Makita', 'Ferramentas', 'Ferramentas Elétricas', 'FER005', 'Makita', '78901234567890554', 599.99, 420.00, 'UN', 9),
('Furadeira Bosch', 'Ferramentas', 'Ferramentas Elétricas', 'FER006', 'Bosch', '78901234567890555', 449.99, 320.00, 'UN', 9),
('Parafusadeira 18V', 'Ferramentas', 'Ferramentas Elétricas', 'FER007', 'Bosch', '78901234567890556', 399.99, 280.00, 'UN', 9),
('Chave de Roda Automotiva', 'Ferramentas', 'Ferramentas Automotivas', 'FER008', 'Genérico', '78901234567890557', 39.99, 25.00, 'UN', 9),
('Jogo de Chaves Completo', 'Ferramentas', 'Ferramentas Manuais', 'FER009', 'Genérico', '78901234567890558', 129.99, 85.00, 'UN', 9),
('Fita Métrica 5m', 'Ferramentas', 'Medição', 'MED001', 'Stanley', '78901234567890559', 29.99, 18.00, 'UN', 9),
('Nível de Bolha', 'Ferramentas', 'Medição', 'MED002', 'Stanley', '78901234567890560', 45.99, 28.00, 'UN', 9);

-- Atualizar estoque inicial para todos os produtos inseridos
INSERT INTO ESTOQUE (FK_PRODUTO, QUANTIDADE, MAXIMO, MINIMO) 
SELECT p.ID, 100, 1000, 10 FROM PRODUTO p WHERE p.ID >= (SELECT COALESCE(MIN(ID), 1) FROM PRODUTO);

-- Inserção de fornecedores para os produtos
INSERT INTO FORNECEDOR (NOME) VALUES
('Samsung Electronics Brasil'),
('Microsoft Brasil'),
('Dell Computadores Brasil'),
('Logitech Brasil'),
('Redragon Brasil'),
('LG Electronics Brasil'),
('JBL Brasil'),
('Kingston Brasil'),
('Corsair Brasil'),
('Sony Brasil'),
('Nintendo Brasil'),
('Epic Games'),
('EA Sports Brasil'),
('Activision Brasil'),
('Razer Brasil'),
('Steam'),
('Novatec Editora'),
('Amazon Brasil'),
('Tilibra'),
('Houaiss'),
('Madeireira Nacional'),
('Casio Brasil'),
('Nestlé Brasil'),
('Panificadora Central'),
('Crystal Brasil'),
('Coca-Cola Brasil'),
('Elma Chips Brasil'),
('Sadia Alimentos'),
('Ypê Brasil'),
('Omo Brasil'),
('APC Brasil'),
('TP-Link Brasil'),
('Seagate Brasil'),
('Phillips Brasil'),
('Stanley Brasil'),
('Tramontina Brasil'),
('Makita Brasil'),
('Bosch Brasil'),
('Cif Brasil');

-- Log da migração
INSERT INTO CONFIG_PDV (CHAVE, VALOR, DESCRICAO, TIPO) VALUES
('MIGRATION_V4_EXECUTADA', 'TRUE', 'Script V4 de inserção de produtos executado com sucesso', 'BOOLEAN');

COMMIT;
