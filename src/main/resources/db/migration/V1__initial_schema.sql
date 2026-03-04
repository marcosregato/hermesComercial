CREATE TABLE usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome VARCHAR(255),
    endereco VARCHAR(255),
    cnpj VARCHAR(20),
    cpf VARCHAR(20),
    email VARCHAR(255),
    tipo VARCHAR(50)
);

CREATE TABLE login (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fk_usuario INTEGER,
    login VARCHAR(50),
    senha VARCHAR(50),
    FOREIGN KEY (fk_usuario) REFERENCES usuario(id)
);

CREATE TABLE produto (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome VARCHAR(255),
    categoria VARCHAR(100),
    subCategoria VARCHAR(100),
    codigo VARCHAR(50),
    marca VARCHAR(100),
    dataCompra VARCHAR(20)
);

CREATE TABLE estoque (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fk_produto INTEGER,
    quantidade VARCHAR(10),
    maximo INTEGER,
    minimo INTEGER,
    FOREIGN KEY (fk_produto) REFERENCES produto(id)
);

CREATE TABLE custo (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fk_fornecedor INTEGER,
    custounitario REAL,
    custototal REAL
);

CREATE TABLE despesa (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo VARCHAR(100),
    valor REAL
);

CREATE TABLE caixa (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    valorCaixa REAL,
    valor REAL,
    tipo VARCHAR(50)
);

CREATE TABLE alertaEstoque (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    valor VARCHAR(10),
    tempoEstoque VARCHAR(10)
);

CREATE TABLE atributo (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    impostoFederal REAL,
    impostoEstadual REAL,
    impostoMunicipal REAL
);
