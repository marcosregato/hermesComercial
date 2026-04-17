-- V3__criar_grupos_impostos.sql
-- Script de migração para criar grupos de impostos necessários
-- Data: 2024-04-17
-- Autor: Sistema Hermes Comercial

-- Criar grupos de impostos necessários para os produtos
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

-- Log da migração
INSERT INTO CONFIG_PDV (CHAVE, VALOR, DESCRICAO, TIPO) VALUES
('MIGRATION_V3_EXECUTADA', 'TRUE', 'Script V3 de criação de grupos executado com sucesso', 'BOOLEAN');

COMMIT;
