insert into role(rolename)
values ('admin');

insert into role(rolename)
values ('user');

insert into user_credentials(password, username, role_id)
VALUES ('$2a$10$16B7PJ4TXPHpFXl4pOmI2.aL/3RmDdP6WDdO2HHCjI2ui7Xzng3wC', 'denis', (select id from role where rolename = 'admin'));
/*пароль 12345*/

insert into user_credentials(password, username, role_id)
VALUES ('12345', 'julia', (select id from role where rolename = 'user'));
