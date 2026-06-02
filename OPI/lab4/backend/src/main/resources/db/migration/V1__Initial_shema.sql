create table users
(
    id       serial
        primary key,
    password varchar(512),
    username varchar(255)
        unique
);

create table refresh_tokens
(
    id      serial
        primary key,
    revoked boolean      not null,
    token   varchar(750) not null,
    user_id integer      not null
        constraint fk_refresh_tokens_user_id
            references users
);

create table results
(
    id          serial
        primary key,
    currenttime timestamp,
    r           numeric(38),
    result      boolean,
    time        bigint,
    x           numeric(38),
    y           numeric(38),
    user_id     integer
        constraint fk_results_user_id
            references users
);

create table table_meta
(
    table_name    varchar(255) not null
        primary key,
    last_modified timestamp
);

create table users_results
(
    userentity_id integer not null
        constraint fk_users_results_userentity_id
            references users,
    results_id    integer not null
        constraint fk_users_results_results_id
            references results,
    primary key (userentity_id, results_id)
);