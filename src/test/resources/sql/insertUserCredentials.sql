insert into role(rolename)
values ('admin');


insert into user_credentials(password, username, role_id)
VALUES ('$2a$10$16B7PJ4TXPHpFXl4pOmI2.aL/3RmDdP6WDdO2HHCjI2ui7Xzng3wC', 'denis', 1);

/*пароль 12345*/
-- update user_credentials set password = '$2a$10$16B7PJ4TXPHpFXl4pOmI2.aL/3RmDdP6WDdO2HHCjI2ui7Xzng3wC' where username = 'denis';
