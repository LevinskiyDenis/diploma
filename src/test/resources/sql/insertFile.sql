insert into file(name, size, mimetype, lastedited, file, user_credentials_id)
values ('myimage', 222000, 'image/png', '2021-11-02 19:48:55.269648', '1234', (select id from user_credentials));

insert into file(name, size, mimetype, lastedited, file, user_credentials_id)
values ('imagetodelete', 222000, 'image/png', '2021-11-02 19:48:55.269648', '4567', (select id from user_credentials));

insert into file(name, size, mimetype, lastedited, file, user_credentials_id)
values ('mysecondimage', 222000, 'image/png', '2021-11-02 19:48:55.269648', '4567', (select id from user_credentials));

