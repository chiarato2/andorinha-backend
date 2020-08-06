delete from comentario;
delete from tweet;
delete from usuario;

INSERT INTO usuario(id, nome) VALUES (1, 'Usu�rio 1');
INSERT INTO usuario(id, nome) VALUES (2, 'Usu�rio 2');
INSERT INTO usuario(id, nome) VALUES (3, 'Usu�rio 3');
INSERT INTO usuario(id, nome) VALUES (4, 'Usu�rio 4');
INSERT INTO usuario(id, nome) VALUES (5, 'Usu�rio 5');

INSERT INTO tweet(id, id_usuario, conteudo, data_postagem) VALUES (1, 1, 'Minha postagem de teste', '2020-04-08 15:45:20');
INSERT INTO tweet(id, id_usuario, conteudo, data_postagem) VALUES (2, 2, 'Minha postagem de teste 2', '2020-05-09 09:10:00');
INSERT INTO tweet(id, id_usuario, conteudo, data_postagem) VALUES (3, 3, 'Minha postagem de teste 3', '2020-01-01 12:55:15');

INSERT INTO comentario(id, id_usuario, id_tweet, conteudo, data_postagem) VALUES ( 1, 4, 1,  'Coment�rio 1', '2020-06-01 15:45:20');
INSERT INTO comentario(id, id_usuario, id_tweet, conteudo, data_postagem) VALUES ( 2, 4, 1,  'Coment�rio 2', '2020-06-02 15:45:20');
INSERT INTO comentario(id, id_usuario, id_tweet, conteudo, data_postagem) VALUES ( 3, 3, 1,  'Coment�rio 3', '2020-06-03 15:45:20');
INSERT INTO comentario(id, id_usuario, id_tweet, conteudo, data_postagem) VALUES ( 4, 2, 1,  'Coment�rio 4', '2020-06-04 15:45:20');
INSERT INTO comentario(id, id_usuario, id_tweet, conteudo, data_postagem) VALUES ( 5, 1, 2,  'Coment�rio 5', '2020-06-05 15:45:20');
INSERT INTO comentario(id, id_usuario, id_tweet, conteudo, data_postagem) VALUES ( 6, 4, 2,  'Coment�rio 6', '2020-06-06 15:45:20');
INSERT INTO comentario(id, id_usuario, id_tweet, conteudo, data_postagem) VALUES ( 7, 4, 3,  'Coment�rio 7', '2020-06-07 15:45:20');
INSERT INTO comentario(id, id_usuario, id_tweet, conteudo, data_postagem) VALUES ( 8, 3, 3,  'Coment�rio 8', '2020-06-08 15:45:20');
INSERT INTO comentario(id, id_usuario, id_tweet, conteudo, data_postagem) VALUES ( 9, 2, 3,  'Coment�rio 9', '2020-06-09 15:45:20');
INSERT INTO comentario(id, id_usuario, id_tweet, conteudo, data_postagem) VALUES (10, 1, 3,  'Coment�rio 10', '2020-06-10 15:45:20');

select setval('seq_usuario', (select max(id) from usuario) );
select setval('seq_tweet', (select max(id) from tweet) );
select setval('seq_comentario', (select max(id) from comentario) );