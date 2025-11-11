CREATE DATABASE IF NOT EXISTS usuarios_db;
USE usuarios_db;

-- TABELAS

CREATE TABLE grupos_usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(50) UNIQUE NOT NULL,
  descricao VARCHAR(100)
);

CREATE TABLE usuarios (
  id VARCHAR(36) PRIMARY KEY, -- UUID gerado pelo backend
  nome VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  senha VARCHAR(100) NOT NULL,
  grupo_id INT,
  data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  ativo BOOLEAN DEFAULT TRUE,
  FOREIGN KEY (grupo_id) REFERENCES grupos_usuarios(id)
);

-- ÍNDICES
-- Justificativa: nome e email são campos usados para busca e login.
CREATE INDEX idx_usuarios_nome ON usuarios(nome);
CREATE INDEX idx_usuarios_email ON usuarios(email);


-- FUNÇÕES

-- contar quantos usuários estão ativos
DROP FUNCTION IF EXISTS contar_ativos;
DELIMITER $$
CREATE FUNCTION contar_ativos()
RETURNS INT
DETERMINISTIC
BEGIN
  DECLARE total INT;
  SELECT COUNT(*) INTO total FROM usuarios WHERE ativo = TRUE;
  RETURN total;
END $$
DELIMITER ;

-- calcular quantos dias desde o cadastro do usuário
DROP FUNCTION IF EXISTS calcular_dias_cadastro;
DELIMITER $$
CREATE FUNCTION calcular_dias_cadastro(p_id VARCHAR(36))
RETURNS INT
DETERMINISTIC
BEGIN
  DECLARE dias INT;
  SELECT DATEDIFF(NOW(), data_cadastro) INTO dias
  FROM usuarios
  WHERE id = p_id;
  RETURN dias;
END $$
DELIMITER ;


-- TRIGGERS

-- garantir que o campo "ativo" sempre venha TRUE por padrão
DELIMITER $$
CREATE TRIGGER trg_definir_ativo
BEFORE INSERT ON usuarios
FOR EACH ROW
BEGIN
  IF NEW.ativo IS NULL THEN
    SET NEW.ativo = TRUE;
  END IF;
END $$
DELIMITER ;

-- registrar alterações de grupo de usuários
CREATE TABLE IF NOT EXISTS log_alteracoes_grupo (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id VARCHAR(36),
  grupo_antigo INT,
  grupo_novo INT,
  data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DELIMITER $$
CREATE TRIGGER trg_log_alteracao_grupo
AFTER UPDATE ON usuarios
FOR EACH ROW
BEGIN
  IF OLD.grupo_id <> NEW.grupo_id THEN
    INSERT INTO log_alteracoes_grupo (usuario_id, grupo_antigo, grupo_novo)
    VALUES (OLD.id, OLD.grupo_id, NEW.grupo_id);
  END IF;
END $$
DELIMITER ;


-- VIEWS

-- mostra nome do usuário + nome do grupo
CREATE OR REPLACE VIEW vw_usuarios_grupos AS
SELECT u.id, u.nome, u.email, g.nome AS grupo
FROM usuarios u
LEFT JOIN grupos_usuarios g ON g.id = u.grupo_id;

-- 2️⃣ View: mostra apenas usuários ativos
CREATE OR REPLACE VIEW vw_usuarios_ativos AS
SELECT id, nome, email
FROM usuarios
WHERE ativo = TRUE;


-- PROCEDURES

-- Procedure: desativar um usuário
DELIMITER $$
CREATE PROCEDURE desativar_usuario(IN p_id VARCHAR(36))
BEGIN
  UPDATE usuarios SET ativo = FALSE WHERE id = p_id;
END $$
DELIMITER ;


-- USUÁRIOS DE BANCO E ACESSOS

-- Criar usuários de banco (sem root)
CREATE USER IF NOT EXISTS 'admin_app'@'%' IDENTIFIED BY 'admin123';
CREATE USER IF NOT EXISTS 'leitor_app'@'%' IDENTIFIED BY 'leitor123';

-- admin: pode tudo no schema
GRANT ALL PRIVILEGES ON usuarios_db.* TO 'admin_app'@'%';

-- leitor: só leitura
GRANT SELECT ON usuarios_db.* TO 'leitor_app'@'%';

FLUSH PRIVILEGES;

INSERT INTO grupos_usuarios (nome, descricao)
VALUES ('Admin', 'Administrador do sistema'),
       ('User', 'Usuário comum');


-- Ajuste de senha do root apenas localmente (para o Docker)
ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
