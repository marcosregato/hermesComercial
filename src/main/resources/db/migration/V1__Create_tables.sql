CREATE TABLE usuario (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(50),
    endereco VARCHAR(100),
    bairro VARCHAR(50),
    cidade VARCHAR(50),
    estado VARCHAR(2),
    cep VARCHAR(10),
    cnpj VARCHAR(20),
    cpf VARCHAR(14),
    email VARCHAR(100),
    tipoUsuario VARCHAR(20)
);

CREATE TABLE produto (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(50),
    categoria VARCHAR(50),
    subCategoria VARCHAR(50),
    codigo VARCHAR(50),
    marca VARCHAR(50),
    dataCompra DATE
);

CREATE TABLE estoque (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fk_produto INTEGER,
    quantidade INTEGER,
    maximo INTEGER,
    minimo INTEGER,
    FOREIGN KEY (fk_produto) REFERENCES produto(id)
);

CREATE TABLE login (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fk_usuario INTEGER,
    login VARCHAR(50),
    senha VARCHAR(50),
    FOREIGN KEY (fk_usuario) REFERENCES usuario(id)
);

CREATE TABLE imposto (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    valorImposto FLOAT
);

CREATE TABLE despesa (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tipo VARCHAR(50),
    valor FLOAT
);

CREATE TABLE fornecedor (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(100)
);

CREATE TABLE custo (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fk_fornecedor INTEGER,
    custounitario FLOAT,
    custototal FLOAT,
    FOREIGN KEY (fk_fornecedor) REFERENCES fornecedor(id)
);

CREATE TABLE caixa (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    valorCaixa FLOAT,
    valor FLOAT,
    tipo VARCHAR(20)
);

CREATE TABLE atributo (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    impostoFederal FLOAT,
    impostoEstadual FLOAT,
    impostoMunicipal FLOAT
);

CREATE TABLE alertaEstoque (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    valor VARCHAR(20),
    tempoEstoque VARCHAR(20)
);
