CREATE DATABASE IF NOT EXISTS usuarios_db;
USE usuarios_db;

-- === Função para geração de ID customizado (dados críticos) ===
DROP FUNCTION IF EXISTS gerar_id_usuario;
DELIMITER $$
CREATE FUNCTION gerar_id_usuario() RETURNS BIGINT
DETERMINISTIC
BEGIN
  RETURN FLOOR(100000 + (RAND() * 900000)); -- gera número aleatório de 6 dígitos
END $$
DELIMITER ;

-- === Tabelas ===

CREATE TABLE grupos_usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(50) UNIQUE NOT NULL,
  descricao VARCHAR(100)
);

CREATE TABLE usuarios (
  id BIGINT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  senha VARCHAR(100) NOT NULL,
  grupo_id INT,
  data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  ativo BOOLEAN DEFAULT TRUE,
  FOREIGN KEY (grupo_id) REFERENCES grupos_usuarios(id)
);

-- === Índices ===
-- Justificativa: nome e email serão usados para busca e login.
CREATE INDEX idx_usuarios_nome ON usuarios(nome);
CREATE INDEX idx_usuarios_email ON usuarios(email);

-- === TRIGGERS ===

-- 1. Gera ID automático antes de inserir um usuário
DELIMITER $$
CREATE TRIGGER trg_gera_id_usuario
BEFORE INSERT ON usuarios
FOR EACH ROW
BEGIN
  IF NEW.id IS NULL THEN
    SET NEW.id = gerar_id_usuario();
  END IF;
END $$
DELIMITER ;

-- 2. Loga exclusões de usuários em uma tabela simples
CREATE TABLE log_exclusoes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT,
  data_exclusao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DELIMITER $$
CREATE TRIGGER trg_log_exclusao
AFTER DELETE ON usuarios
FOR EACH ROW
BEGIN
  INSERT INTO log_exclusoes (usuario_id) VALUES (OLD.id);
END $$
DELIMITER ;

-- === VIEWS ===

-- 1. Mostra nome do usuário + nome do grupo
CREATE VIEW vw_usuarios_grupos AS
SELECT u.id, u.nome, u.email, g.nome AS grupo
FROM usuarios u
LEFT JOIN grupos_usuarios g ON g.id = u.grupo_id;

-- 2. Mostra apenas usuários ativos
CREATE VIEW vw_usuarios_ativos AS
SELECT id, nome, email
FROM usuarios
WHERE ativo = TRUE;

-- === PROCEDURES / FUNCTIONS ===

-- Procedure para desativar usuário
DELIMITER $$
CREATE PROCEDURE desativar_usuario(IN p_id BIGINT)
BEGIN
  UPDATE usuarios SET ativo = FALSE WHERE id = p_id;
END $$
DELIMITER ;

-- Function para contar usuários ativos
DELIMITER $$
CREATE FUNCTION contar_ativos() RETURNS INT
DETERMINISTIC
BEGIN
  DECLARE total INT;
  SELECT COUNT(*) INTO total FROM usuarios WHERE ativo = TRUE;
  RETURN total;
END $$
DELIMITER ;

-- === USUÁRIOS E ACESSOS ===

-- Criar usuários de banco (sem root)
CREATE USER IF NOT EXISTS 'admin_app'@'%' IDENTIFIED BY 'admin123';
CREATE USER IF NOT EXISTS 'leitor_app'@'%' IDENTIFIED BY 'leitor123';

-- admin: pode tudo no schema
GRANT ALL PRIVILEGES ON usuarios_db.* TO 'admin_app'@'%';

-- leitor: só leitura
GRANT SELECT ON usuarios_db.* TO 'leitor_app'@'%';

FLUSH PRIVILEGES;

-- === Dados iniciais ===
INSERT INTO grupos_usuarios (nome, descricao) VALUES ('Admin', 'Administrador do sistema'), ('User', 'Usuário comum');
INSERT INTO usuarios (id, nome, email, senha, grupo_id) VALUES (gerar_id_usuario(), 'João Silva', 'joao@email.com', '1234', 1);
