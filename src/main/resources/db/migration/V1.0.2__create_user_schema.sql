create table if not exists users (
    user_id bigint not null,
    email varchar(25) not null,
    password varchar(255) not null,
    user_role varchar(255) not null,
    provider varchar(255) not null,
    version int,
    avatar varchar(1000),
    created_at timestamptz not null default current_timestamp,
    created_by varchar(255),
    updated_at timestamptz not null default current_timestamp,
    updated_by varchar(255),
    primary key (user_id)
);

create index on users (email);