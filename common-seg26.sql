use seguridadVeintiseis;
SET FOREIGN_KEY_CHECKS=0;
Drop TABLE IF EXISTS session;
Drop TABLE IF EXISTS account;
Drop TABLE IF EXISTS admin;
Drop TABLE IF EXISTS category;
Drop TABLE IF EXISTS incidence;
Drop TABLE IF EXISTS password_reset;
Drop TABLE IF EXISTS report;
Drop TABLE IF EXISTS role;
Drop TABLE IF EXISTS user;
SET FOREIGN_KEY_CHECKS=1;

SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE account;
SET FOREIGN_KEY_CHECKS=1;

INSERT INTO `seguridadVeintiseis`.`account` (`email`, `fist_name`, `last_name`, `password`) VALUES ('rodrigo.120894@gmail.com', 'Rodrigo', 'De la Flor', '$2y$04$VI0QajfBRqH2Vyes6sORfu/Z6sgIOh22rFjXMbeJK5GZyy75GwSCi');
INSERT INTO `seguridadVeintiseis`.`account` (`email`, `fist_name`, `last_name`, `password`) VALUES ('leo22_07@hotmail.com', 'Andy', 'Brown', '$2y$04$VI0QajfBRqH2Vyes6sORfu/Z6sgIOh22rFjXMbeJK5GZyy75GwSCi');

INSERT INTO `seguridadVeintiseis`.`user` (`id`, `address`, `phone`, `type`, `account_id`) VALUES ('1', 'Address 1', '98654321', 'AGENT', '1');
INSERT INTO `seguridadVeintiseis`.`user` (`id`, `address`, `phone`, `type`, `account_id`) VALUES ('2', 'Address2', '987656785', 'CITIZEN', '2');

INSERT INTO `seguridadVeintiseis`.`admin` (`id`, `created_at`, `account_id`, `role_id`) VALUES ('1', NULL, '1', NULL);

INSERT INTO `seguridadVeintiseis`.`category` (`description`, `short_title`, `title`) VALUES ('Categoria 1', 'Cat 1', 'Categoria 1');
INSERT INTO `seguridadVeintiseis`.`category` (`description`, `short_title`, `title`) VALUES ('Categoria 2', 'Cat 2', 'Categoria 2');

INSERT INTO `seguridadVeintiseis`.`incidence` (`description`, `severity`, `short_title`, `title`, `category_id`) VALUES ('Incidencia 1', '3', 'icd 1', 'Incidencia 1', '1');
INSERT INTO `seguridadVeintiseis`.`incidence` (`description`, `severity`, `short_title`, `title`, `category_id`) VALUES ('Incidencia 2', '1', 'icd 2', 'Incidencia 2', '2');
