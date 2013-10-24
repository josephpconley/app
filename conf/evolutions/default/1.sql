# --- !Ups

create table "test_user" ("first_name" varchar(50) NOT NULL,"last_name" varchar(50) NOT NULL,"id" SERIAL NOT NULL PRIMARY KEY);
insert into test_user (first_name, last_name) values ('Joe', 'Conley');

# --- !Downs

drop table "test_user";

