DROP DATABASE IF EXISTS UTILITIES_TEST;

CREATE DATABASE UTILITIES_TEST;

USE UTILITIES_TEST;

create table GENRE (
                       ID integer not null auto_increment,
                       DESCRIPTION varchar(255),
                       primary key (ID)
) engine=InnoDB;