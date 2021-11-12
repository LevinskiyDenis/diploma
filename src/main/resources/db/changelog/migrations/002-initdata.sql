insert into role(rolename)
values ('USER');
insert into role(rolename)
values ('ADMIN');

insert into user_credentials(password, username, role_id)
VALUES ('$2a$10$16B7PJ4TXPHpFXl4pOmI2.aL/3RmDdP6WDdO2HHCjI2ui7Xzng3wC', 'denis',
        (select id from role where rolename = 'ADMIN'));
/*пароль 12345*/

insert into user_credentials(password, username, role_id)
VALUES ('$2a$10$Cr40PFWIYPkfdmMjoHMNyetkPivLtYdyD9qQXL1wRlNOcM7r3yrRy', 'julia',
        (select id from role where rolename = 'USER'));
/*пароль qwerty*/