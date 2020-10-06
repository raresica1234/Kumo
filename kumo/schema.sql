drop table if exists users;

create table users (
    id integer primary key AUTOINCREMENT,
    username text unique not null,
    password text not null
);
