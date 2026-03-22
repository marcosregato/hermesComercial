create table itemvenda(
     id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     fk_produto int,
     quantidade int,
     FOREIGN KEY (fk_produto) REFERENCES produto(id)
);

ALTER TABLE estoque ADD COLUMN lote VARCHAR(20), ADD COLUMN dtVencimento VARCHAR(10);
ALTER TABLE estoque
ADD CONSTRAINT fk_produto_estoque
FOREIGN KEY (fk_produto)
REFERENCES produto (id);