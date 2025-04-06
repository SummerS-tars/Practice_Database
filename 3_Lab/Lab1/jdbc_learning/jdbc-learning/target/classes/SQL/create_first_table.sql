CREATE DATABASE IF NOT EXISTS test_room_assignment;
USE test_room_assignment;

-- 创建room表（仅当不存在时）
CREATE TABLE IF NOT EXISTS room
	(kdno int not null ,
    kcno int not null ,
    ccno int not null ,
    kdname varchar(20) not null ,
    exptime datetime not null ,
    papername varchar(20) ,
    primary key(kcno, ccno));

-- 创建student表（仅当不存在时）
CREATE TABLE IF NOT EXISTS student
	(registno char(7) not null,
    name varchar(20) not null,
    kdno int not null,
    kcno int not null ,
    ccno int not null ,
    seat int not null ,
    primary key(registno,kcno,ccno),
    foreign key(kcno, ccno) references room(kcno, ccno));
