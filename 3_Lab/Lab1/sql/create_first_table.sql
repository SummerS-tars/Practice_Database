CREATE DATABASE IF NOT EXISTS test_room_assignment;
USE test_room_assignment;

CREATE TABLE room
	(kdno int not null ,
    kcno int not null ,
    ccno int not null ,
    kdname varchar(20) not null ,
    exptime datetime not null ,
    papername varchar(20) ,
    primary key(kcno, ccno));

CREATE TABLE student
	(registno char(7) not null,
    name varchar(20) not null,
    kdno int not null,
    kcno int not null ,
    ccno int not null ,
    seat int not null ,
    foreign key(kcno, ccno) references room);
