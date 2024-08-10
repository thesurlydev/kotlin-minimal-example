-- drop table if exists people;

create table if not exists people
(
    id       uuid primary key default gen_random_uuid(),
    name     varchar(255) not null,
    age      int          not null
);