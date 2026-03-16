CREATE TABLE pessoa (
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
    dataCompra DATE,
    id_grupo_imposto INT,
    FOREIGN KEY (id_grupo_imposto) REFERENCES grupo_impostos(id)
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
    fk_pessoa INTEGER,
    login VARCHAR(50),
    senha VARCHAR(50),
    FOREIGN KEY (fk_pessoa) REFERENCES pessoa(id)
);

CREATE TABLE imposto (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_grupo_imposto INT,
    origem_mercadoria FLOAT,
    cst_icms VARCHAR,
    csosn VARCHAR,
    aliquota_icms FLOAT,
    aliquota_cofins FLOAT,
    aliquota_ipi FLOAT,
    cfop FLOAT,
    reducao_base FLOAT,
    FOREIGN KEY (id_grupo_imposto) REFERENCES grupo_impostos(id)
);

CREATE TABLE grupo_impostos(
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_grupo VARCHAR,
    descricao VARCHAR,
    ativo BOOLEAN,
    data_criacao VARCHAR
);

CREATE TABLE despesa (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tipo VARCHAR(50),
    valor FLOAT
);

-- tabela fornecedor (necessária para o FK)
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
