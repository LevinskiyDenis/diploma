create table file
(
    id  bigserial primary key,
    name varchar(255) not null unique,
    size bigint not null,
    mimetype varchar(255) not null,
    lastEdited timestamp not null,
    file bytea not null,
    user_credentials_id bigint not null,
    constraint fk_user_credentials foreign key (user_credentials_id) references user_credentials (id)
);