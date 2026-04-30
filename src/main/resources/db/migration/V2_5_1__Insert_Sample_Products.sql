-- Inserir dados de exemplo para produtos no sistema Hermes Comercial
-- Versão 2.5.1 - Dados de exemplo para teste e demonstração

-- Limpar produtos existentes (se houver)
DELETE FROM produto;

-- Inserir produtos de exemplo - Categoria Informática
INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes, ativo) VALUES
('INF001', 'Notebook Dell Inspiron 15', 3599.99, 15, 'Informática', 'Intel Core i5, 8GB RAM, 512GB SSD', TRUE),
('INF002', 'Mouse Logitech Wireless', 89.90, 50, 'Informática', 'Mouse sem fio 2.4GHz', TRUE),
('INF003', 'Teclado Mecânico Gamer', 259.99, 25, 'Informática', 'Switch RGB Blue, Backlight LED', TRUE),
('INF004', 'Monitor LG 27 IPS', 1299.99, 10, 'Informática', 'Full HD, 75Hz, HDMI/DisplayPort', TRUE),
('INF005', 'Webcam HD 1080p', 149.90, 30, 'Informática', 'Webcam USB, Microfone embutido', TRUE);

-- Inserir produtos de exemplo - Categoria Periféricos
INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes, ativo) VALUES
('PER001', 'Impressora HP DeskJet', 499.99, 8, 'Periféricos', 'Multifuncional, Wi-Fi, Colorida', TRUE),
('PER002', 'Scanner Epson V600', 899.99, 5, 'Periféricos', 'Scanner de documentos, 4800dpi', TRUE),
('PER003', 'Caixa de Som JBL', 199.90, 20, 'Periféricos', 'Bluetooth, USB, 20W', TRUE),
('PER004', 'Fone de Ouvido Sony', 349.99, 35, 'Periféricos', 'Over-ear, Noise Cancelling', TRUE);

-- Inserir produtos de exemplo - Categoria Monitores
INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes, ativo) VALUES
('MON001', 'Monitor Samsung 4K 28"', 2199.99, 6, 'Monitores', 'UHD 4K, 60Hz, HDMI/DP/USB-C', TRUE),
('MON002', 'Monitor Dell Ultrasharp', 1899.99, 8, 'Monitores', '27" QHD, IPS, 99% sRGB', TRUE),
('MON003', 'Monitor Gamer ASUS', 1599.99, 12, 'Monitores', '27" Full HD, 144Hz, G-Sync', TRUE);

-- Inserir produtos de exemplo - Categoria Acessórios
INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes, ativo) VALUES
('ACE001', 'Cabo HDMI 2m', 29.90, 100, 'Acessórios', 'HDMI 2.0, 4K@60Hz, Dourado', TRUE),
('ACE002', 'Extensão USB 3.0', 39.90, 80, 'Acessórios', '4 portas, 2.4A total', TRUE),
('ACE003', 'Suporte para Monitor', 129.99, 25, 'Acessórios', 'Ajustável, até 32"', TRUE),
('ACE004', 'Filtro de Linha', 59.90, 40, 'Acessórios', '6 tomadas, proteção contra surtos', TRUE);

-- Inserir produtos de exemplo - Categoria Móveis
INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes, ativo) VALUES
('MOV001', 'Mesa para Escritório', 899.99, 10, 'Móveis', '120x60cm, MDF, Preto', TRUE),
('MOV002', 'Cadeira Executiva', 699.99, 15, 'Móveis', 'Ergonômica, couro sintético', TRUE),
('MOV003', 'Armário de Arquivo', 499.99, 8, 'Móveis', '4 gavetas, aço, cinza', TRUE);

-- Inserir produtos de exemplo - Categoria Rede
INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes, ativo) VALUES
('RED001', 'Roteador Wi-Fi 6', 299.99, 20, 'Rede', 'AX1800, 4 portas Gigabit', TRUE),
('RED002', 'Switch 8 Portas', 199.99, 15, 'Rede', 'Gigabit, Desktop, Metal', TRUE),
('RED003', 'Cabo Ethernet 5m', 19.90, 60, 'Rede', 'Cat 6, U/UTP, Azul', TRUE);

-- Inserir produtos de exemplo - Categoria Armazenamento
INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes, ativo) VALUES
('ARM001', 'HD Externo 1TB', 349.99, 25, 'Armazenamento', 'USB 3.0, 2.5", Portátil', TRUE),
('ARM002', 'SSD 500GB NVMe', 299.99, 30, 'Armazenamento', 'M.2, PCIe 3.0, até 3500MB/s', TRUE),
('ARM003', 'Pen Drive 64GB', 79.90, 50, 'Armazenamento', 'USB 3.1, Metal', TRUE);

-- Inserir produtos de exemplo - Categoria Software
INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes, ativo) VALUES
('SOF001', 'Windows 11 Pro', 599.99, 100, 'Software', 'Licença digital, OEM', TRUE),
('SOF002', 'Office 365', 399.99, 100, 'Software', 'Assinatura anual, 1 usuário', TRUE),
('SOF003', 'Antivirus Kaspersky', 149.99, 100, 'Software', 'Licença 1 ano, 3 dispositivos', TRUE);

-- Inserir produtos de exemplo - Categoria Impressão
INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes, ativo) VALUES
('IMP001', 'Cartucho HP Preto', 89.90, 40, 'Impressão', 'HP 63, Original', TRUE),
('IMP002', 'Cartucho HP Colorido', 129.99, 30, 'Impressão', 'HP 63, Original, 3 cores', TRUE),
('IMP003', 'Papel A4 500 folhas', 29.90, 60, 'Impressão', '75g/m², branco', TRUE);

-- Inserir produtos de exemplo - Categoria Gamer
INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes, ativo) VALUES
('GAM001', 'RTX 3060 Ti', 2899.99, 5, 'Gamer', '8GB GDDR6X, PCI-e 4.0', TRUE),
('GAM002', 'Processador Ryzen 5 5600X', 899.99, 10, 'Gamer', '6 cores 12 threads, 3.7GHz', TRUE),
('GAM003', 'Memória RAM 16GB DDR4', 329.99, 20, 'Gamer', '3200MHz, CL16, Dual Channel', TRUE),
('GAM004', 'Gabinete Gamer RGB', 399.99, 12, 'Gamer', 'Vidro temperado, 3 fans RGB', TRUE);

-- Inserir alguns produtos descontinuados para teste
INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes, ativo) VALUES
('OLD001', 'Monitor CRT 17"', 99.99, 2, 'Monitores', 'Tubo, descontinuado', FALSE),
('OLD002', 'Mouse Serial PS/2', 19.99, 5, 'Periféricos', 'Conector antigo, descontinuado', FALSE);
