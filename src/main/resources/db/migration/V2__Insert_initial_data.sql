INSERT INTO usuario (nome, endereco, bairro, cidade, estado, cep, cnpj, cpf, email, tipoUsuario) VALUES
('João Silva', 'Rua A, 123', 'Centro', 'São Paulo', 'SP', '01000-000', NULL, '111.111.111-11', 'joao.silva@email.com', 'cliente'),
('Maria Santos', 'Av. B, 456', 'Jardins', 'São Paulo', 'SP', '01414-000', NULL, '222.222.222-22', 'maria.santos@email.com', 'cliente'),
('Pedro Oliveira', 'Rua C, 789', 'Vila Madalena', 'São Paulo', 'SP', '05445-000', NULL, '333.333.333-33', 'pedro.oliveira@email.com', 'cliente'),
('Ana Souza', 'Rua D, 101', 'Pinheiros', 'São Paulo', 'SP', '05413-000', NULL, '444.444.444-44', 'ana.souza@email.com', 'cliente'),
('Carlos Pereira', 'Av. E, 212', 'Moema', 'São Paulo', 'SP', '04513-000', NULL, '555.555.555-55', 'carlos.pereira@email.com', 'cliente'),
('Loja 1', 'Rua F, 313', 'Centro', 'Rio de Janeiro', 'RJ', '20000-000', '11.111.111/0001-11', NULL, 'loja1@email.com', 'fornecedor'),
('Loja 2', 'Av. G, 414', 'Copacabana', 'Rio de Janeiro', 'RJ', '22000-000', '22.222.222/0001-22', NULL, 'loja2@email.com', 'fornecedor'),
('Admin', 'Rua H, 515', 'Admin', 'Admin', 'AD', '00000-000', NULL, '000.000.000-00', 'admin@hermes.com', 'admin'),
('Funcionario 1', 'Rua I, 616', 'Centro', 'Belo Horizonte', 'MG', '30110-000', NULL, '666.666.666-66', 'func1@email.com', 'funcionario'),
('Funcionario 2', 'Av. J, 717', 'Savassi', 'Belo Horizonte', 'MG', '30140-000', NULL, '777.777.777-77', 'func2@email.com', 'funcionario');

INSERT INTO login (fk_usuario, login, senha) VALUES
(1, 'joao', '123'),
(2, 'maria', '123'),
(3, 'pedro', '123'),
(4, 'ana', '123'),
(5, 'carlos', '123'),
(6, 'loja1', '123'),
(7, 'loja2', '123'),
(8, 'admin', 'admin'),
(9, 'func1', '123'),
(10, 'func2', '123');

INSERT INTO produto (nome, categoria, subCategoria, codigo, marca, dataCompra) VALUES
('Coca-Cola 2L', 'Bebidas', 'Refrigerantes', '7894900011517', 'Coca-Cola', '2023-01-15'),
('Pão de Forma', 'Padaria', 'Pães', '7891025103504', 'Pullman', '2023-01-15'),
('Leite Integral', 'Laticínios', 'Leite', '7896004000418', 'Italac', '2023-01-15'),
('Arroz', 'Mercearia', 'Grãos', '7896006710015', 'Tio João', '2023-01-15'),
('Feijão', 'Mercearia', 'Grãos', '7896006710022', 'Camil', '2023-01-15'),
('Café', 'Mercearia', 'Café e Chás', '7891025103504', 'Pilão', '2023-01-15'),
('Sabão em Pó', 'Limpeza', 'Lava Roupas', '7891150000013', 'OMO', '2023-01-15'),
('Shampoo', 'Higiene', 'Cabelo', '7891025103504', 'Seda', '2023-01-15'),
('Cerveja', 'Bebidas', 'Alcoólicas', '7891149101319', 'Skol', '2023-01-15'),
('Biscoito Recheado', 'Mercearia', 'Biscoitos', '7896003700115', 'Trakinas', '2023-01-15');

INSERT INTO estoque (fk_produto, quantidade, maximo, minimo) VALUES
(1, 100, 200, 20),
(2, 50, 100, 10),
(3, 80, 150, 30),
(4, 120, 250, 40),
(5, 90, 180, 35),
(6, 60, 120, 25),
(7, 40, 80, 15),
(8, 70, 140, 28),
(9, 150, 300, 50),
(10, 200, 400, 60);

INSERT INTO fornecedor (nome) VALUES
('Coca-Cola'),
('Bimbo'),
('Italac'),
('Camil'),
('Unilever'),
('P&G'),
('Ambev'),
('Mondelez'),
('Nestlé'),
('Fornecedor Genérico');

INSERT INTO custo (fk_fornecedor, custounitario, custototal) VALUES
(1, 2.50, 250.00),
(2, 4.00, 200.00),
(3, 3.50, 280.00),
(4, 10.00, 1200.00),
(5, 8.00, 720.00),
(6, 5.00, 300.00),
(7, 15.00, 600.00),
(8, 6.00, 420.00),
(9, 3.00, 450.00),
(10, 2.00, 400.00);

INSERT INTO despesa (tipo, valor) VALUES
('Aluguel', 1500.00),
('Salários', 5000.00),
('Luz', 300.00),
('Água', 150.00),
('Internet', 100.00),
('Marketing', 500.00),
('Impostos', 1200.00),
('Manutenção', 250.00),
('Telefone', 80.00),
('Outras', 200.00);

INSERT INTO caixa (valorCaixa, valor, tipo) VALUES
(1000.00, 100.00, 'entrada'),
(1100.00, -50.00, 'saida'),
(1050.00, 200.00, 'entrada'),
(1250.00, -75.00, 'saida'),
(1175.00, 50.00, 'entrada'),
(1225.00, -25.00, 'saida'),
(1200.00, 300.00, 'entrada'),
(1500.00, -100.00, 'saida'),
(1400.00, 150.00, 'entrada'),
(1550.00, -50.00, 'saida');

INSERT INTO imposto (valorImposto) VALUES
(0.18),
(0.12),
(0.05),
(0.25),
(0.10),
(0.07),
(0.15),
(0.20),
(0.08),
(0.09);

INSERT INTO atributo (impostoFederal, impostoEstadual, impostoMunicipal) VALUES
(0.05, 0.12, 0.01),
(0.05, 0.12, 0.01),
(0.05, 0.12, 0.01),
(0.05, 0.12, 0.01),
(0.05, 0.12, 0.01),
(0.05, 0.12, 0.01),
(0.05, 0.12, 0.01),
(0.05, 0.12, 0.01),
(0.05, 0.12, 0.01),
(0.05, 0.12, 0.01);

INSERT INTO alertaEstoque (valor, tempoEstoque) VALUES
('Baixo', '1 semana'),
('Médio', '2 semanas'),
('Alto', '1 mês'),
('Baixo', '3 dias'),
('Médio', '1 semana'),
('Alto', '2 meses'),
('Baixo', '5 dias'),
('Médio', '10 dias'),
('Alto', '3 semanas'),
('Baixo', '2 dias');
