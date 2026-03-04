
CREATE TABLE estoque(
id INTEGER PRIMARY KEY AUTOINCREMENT,
fk_produto INTEGER,
quantidade VARCHAR(10),
maximo INTEGER,
minimo INTEGER,
FOREIGN KEY (fk_produto) REFERENCES id(produto)
),

CREATE TABLE usuario (

     id INTEGER PRIMARY KEY AUTOINCREMENT,
     nome VARCHAR(10),
     endereco VARCHAR(10),
     bairro VARCHAR(10),
     cidade VARCHAR(10),
     estado VARCHAR(10),
     cep VARCHAR(10),
     cnpj VARCHAR(10),
     cpf VARCHAR(10),
     email VARCHAR(10),
     tipousuario VARCHAR(10)

),
CREATE TABLE produto  (

id INTEGER PRIMARY KEY AUTOINCREMENT,
nome VARCHAR(10),
     categoria VARCHAR(10),
     subCategoria VARCHAR(10),
     codigo VARCHAR(10),
     marca VARCHAR(10),
     dataCompra VARCHAR(10)
),

CREATE TABLE login (

id INTEGER PRIMARY KEY AUTOINCREMENT,
fk_usuario INTEGER,
	login VARCHAR(10),
	senha VARCHAR(10),
	FOREIGN KEY (fk_usuario) REFERENCES id (usuario)
),

CREATE TABLE imposto (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	valorImposto FLOAT
),

CREATE TABLE despesa (

id INTEGER PRIMARY KEY AUTOINCREMENT,
tipo VARCHAR(10),
valor FLOAT
),

CREATE TABLE custo (
id INTEGER PRIMARY KEY AUTOINCREMENT,
fk_fornecedor INTEGER,
FOREIGN KEY (fk_fornecedor) REFERENCES id (fornecedor)

),

CREATE TABLE caixa (
id INTEGER PRIMARY KEY AUTOINCREMENT,
valorCaixa FLOAT,
valor FLOAT,
tipo VARCHAR(10)
),

CREATE TABLE atributo (
id INTEGER PRIMARY KEY AUTOINCREMENT,
impostoFederal FLOAT,
impostoEstadual FLOAT,
impostoMunicipal FLOAT
),


CREATE TABLE alertaEstoque (
id INTEGER PRIMARY KEY AUTOINCREMENT,
valor VARCHAR(10),
tempoEstoque VARCHAR(10)
),
