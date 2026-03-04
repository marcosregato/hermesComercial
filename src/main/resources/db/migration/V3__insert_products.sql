INSERT INTO produto (nome, categoria, subCategoria, codigo, marca, dataCompra) VALUES ('Coca-Cola 2L', 'Bebidas', 'Refrigerantes', '12345', 'Coca-Cola', '2023-10-27');
INSERT INTO estoque (fk_produto, quantidade, maximo, minimo) VALUES (1, '100', 200, 20);

INSERT INTO produto (nome, categoria, subCategoria, codigo, marca, dataCompra) VALUES ('Salgadinho Cheetos', 'Salgadinhos', 'Milho', '54321', 'Elma Chips', '2023-10-27');
INSERT INTO estoque (fk_produto, quantidade, maximo, minimo) VALUES (2, '50', 100, 10);
