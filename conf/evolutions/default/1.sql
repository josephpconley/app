# --- !Ups

create table test_user (
    id SERIAL NOT NULL PRIMARY KEY,
    first_name varchar(50) NOT NULL,
    last_name varchar(50) NOT NULL,
    create_date date not null default sysdate
);

create table role(
    id SERIAL NOT NULL PRIMARY KEY,
    name varchar(50) not null
);

insert into test_user (first_name, last_name) values ('Joe', 'Conley');

insert into role("Admin")

# --- !Downs

drop table test_user;
drop table role;

