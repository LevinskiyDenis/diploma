create table jwt_black_list_entity
(
    id  bigserial primary key,
    jwt varchar(255) not null unique,
    exp bigint       not null
);

create table role
(
    id       bigserial primary key,
    rolename varchar(255) not null unique
);

create table user_credentials
(
    id                      bigserial primary key,
    username                varchar(255) not null unique,
    password                varchar(255) not null,
    role_id                 bigint       not null,
    constraint fk_role foreign key (role_id) references role (id),
    account_non_expired     boolean default true,
    account_non_locked      boolean default true,
    credentials_non_expired boolean default true,
    enabled                 boolean default true
);

create table file
(
    id                  bigserial primary key,
    name                varchar(255) not null unique,
    size                bigint       not null,
    mimetype            varchar(255) not null,
    lastEdited          timestamp    not null,
    file                bytea        not null,
    user_credentials_id bigint       not null,
    constraint fk_user_credentials foreign key (user_credentials_id) references user_credentials (id)
);