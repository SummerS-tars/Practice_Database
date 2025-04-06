# Database Lab 1 : Usage of Connector of MySQL

## Background

1. Database Management System  
    1. Name : MySQL
    2. OS Platform : Ubuntu 24.04 LTS
    3. CPU architecture : x86_64
2. Connector Equipment
    1. OS Platform : Windows 11
    2. CPU architecture : x86_64  
    3. IDE : VS Code  
    4. Language : Java
        1. Version : 23  
    5. Connector  
        1. Name : MySQL Connector/J  
        2. Version : 9.2.0  
        3. Type : JDBC Driver  
    6. Package Controller  
        1. Name : Maven  

## Program

### Connect Method

1. SSH Tunnel
    use JSch to connect to SSH server of the host of MySQL  
2. Port Forwarding
    use JSch to forward the port of MySQL to local port  
    and then use JDBC to connect to local port to access MySQL  

to do this, we create several classes :  

1. ConfigLoader : load from config file  
2. SshTunnel : create SSH tunnel and port forwarding  

### Manipulate MySQL

#### SQL

1. Create a database and some tables  

    ```mysql
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
        primary key(registno),
        foreign key(kcno, ccno) references room(kcno, ccno));
    ```

2. Insert data to tables

    ```mysql
    INSERT INTO room(kdno, kcno, ccno, kdname, exptime, papername) VALUES (?, ?, ?, ?, ?, ?);
    ```

    ```mysql
    INSERT INTO student(registno, name, kdno, kcno, ccno, seat) VALUES (?, ?, ?, ?, ?, ?);
    ```

    将其中的`?`替换为数据表中的数据  
    采用非固定列顺序的方式，先读入首行的列名，  
    然后根据列名的顺序读取数据行，最后将数据行中的数据插入到表中  

## Some Test Results Presentation

### Create a database and some tables

```txt
SSH隧道建立成功
数据库连接成功，当前时间: 2025-04-06 22:18:24
开始执行SQL文件: e:\_ComputerLearning\6_Practice_Database\3_Lab\Lab1\jdbc_learning\jdbc-learning\src\main\resources\SQL\create_first_table.sql
共有 4 条SQL语句
执行第 1 条SQL: CREATE DATABASE IF NOT EXISTS test_room_assignment;
影响行数: 1
执行第 2 条SQL: USE test_room_assignment;
影响行数: 0
执行第 3 条SQL: CREATE TABLE IF NOT EXISTS room         (kdno int not null ,     kcno int not null ,     ccno int not null ,     kdname varchar(20) not null ,     exptime datetime not null ,     papername varchar(20) ,     primary key(kcno, ccno));      
影响行数: 0
执行第 4 条SQL: CREATE TABLE IF NOT EXISTS student      (registno char(7) not null,     name varchar(20) not null,     kdno int not null,     kcno int not null ,     ccno int not null ,     seat int not null ,     primary key(registno),     foreign key(kcno, ccno) references room(kcno, ccno));
影响行数: 0
SQL文件执行完成
```

### Insert data to tables

#### Rooms

```txt
SSH隧道建立成功
数据库连接成功，当前时间: 2025-04-06 23:18:10

开始导入Room表数据:
Room表导入完成
成功导入: 47 行
跳过行数: 0 行
Room表导入完成，共导入 47 行数据
```

#### Students

第一次尝试导入运行时，结果如下

```txt
SSH隧道建立成功
数据库连接成功，当前时间: 2025-04-06 23:19:28

开始导入Student表数据:
解析CSV标题行: "registno","name","kdno","kcno","ccno","seat"
解析到标题: [registno, name, kdno, kcno, ccno, seat]
列名: 'registno' 索引: 0
列名: 'name' 索引: 1
列名: 'kdno' 索引: 2
列名: 'kcno' 索引: 3
列名: 'ccno' 索引: 4
列名: 'seat' 索引: 5
找到列 'registno' 的别名: 'registno' 索引: 0
找到列 'name' 的别名: 'name' 索引: 1
找到列 'kdno' 的别名: 'kdno' 索引: 2
找到列 'kcno' 的别名: 'kcno' 索引: 3
找到列 'ccno' 的别名: 'ccno' 索引: 4
找到列 'seat' 的别名: 'seat' 索引: 5
已导入 100 行数据
已导入 200 行数据
已导入 300 行数据
已导入 400 行数据
警告: 行 406 违反完整性约束，跳过此行
错误详情: Duplicate entry '0317039' for key 'student.PRIMARY'
...
警告: 行 445 违反完整性约束，跳过此行
错误详情: Duplicate entry '0372323' for key 'student.PRIMARY'
已导入 500 行数据
...
已导入 1700 行数据
警告: 行 1814 违反完整性约束，跳过此行
错误详情: Cannot add or update a child row: a foreign key constraint fails (`test_room_assignment`.`student`, CONSTRAINT `student_ibfk_1` FOREIGN KEY (`kcno`, `ccno`) REFERENCES `room` (`kcno`, `ccno`))
警告: 行 1815 违反完整性约束，跳过此行
错误详情: Cannot add or update a child row: a foreign key constraint fails (`test_room_assignment`.`student`, CONSTRAINT `student_ibfk_1` FOREIGN KEY (`kcno`, `ccno`) REFERENCES `room` (`kcno`, `ccno`))
警告: 行 1816 违反完整性约束，跳过此行
错误详情: Cannot add or update a child row: a foreign key constraint fails (`test_room_assignment`.`student`, CONSTRAINT `student_ibfk_1` FOREIGN KEY (`kcno`, `ccno`) REFERENCES `room` (`kcno`, `ccno`))
Student表导入完成
成功导入: 1772 行
跳过行数: 43 行
Student表导入完成，共导入 1772 行数据
```

显然主键设置不太合理，
因此将主键调整为`(kcno, ccno, registno)`，

```txt
SSH隧道建立成功
数据库连接成功，当前时间: 2025-04-06 23:27:57
已导入 100 行数据
已导入 200 行数据
已导入 300 行数据
已导入 400 行数据
警告: 行 406 违反完整性约束，跳过此行
错误详情: Duplicate entry '0317039-2-11' for key 'student.PRIMARY'
...
错误详情: Duplicate entry '0372323-2-11' for key 'student.PRIMARY'
已导入 500 行数据
...
已导入 1700 行数据
警告: 行 1814 违反完整性约束，跳过此行
错误详情: Cannot add or update a child row: a foreign key constraint fails (`test_room_assignment`.`student`, CONSTRAINT `student_ibfk_1` FOREIGN KEY (`kcno`, `ccno`) REFERENCES `room` (`kcno`, `ccno`))
警告: 行 1815 违反完整性约束，跳过此行
错误详情: Cannot add or update a child row: a foreign key constraint fails (`test_room_assignment`.`student`, CONSTRAINT `student_ibfk_1` FOREIGN KEY (`kcno`, `ccno`) REFERENCES `room` (`kcno`, `ccno`))
警告: 行 1816 违反完整性约束，跳过此行
错误详情: Cannot add or update a child row: a foreign key constraint fails (`test_room_assignment`.`student`, CONSTRAINT `student_ibfk_1` FOREIGN KEY (`kcno`, `ccno`) REFERENCES `room` (`kcno`, `ccno`))
Student表导入完成
成功导入: 1772 行
跳过行数: 43 行
Student表导入完成，共导入 1772 行数据
```

可以发现数据中有不合适的重复  
以及部分缺少room依赖的错误数据  

## Answers to Questions

### Data Persistence Framework

Data Persistence Framework(数据持久化框架)  
在基础数据库连接技术(例如JDBC)之上，提供了更高层次的抽象和功能，  
旨在简化开发人员与数据库的交互，提高开发效率并提供更多高级功能  

#### 常见持久化框架与JDBC对比

| 特性         | 直接JDBC(本项目使用)  | 持久化框架(如Hibernate/MyBatis) |
| ------------ | --------------------- | ------------------------------- |
| **开发效率** | 较低，需手写SQL和映射 | 较高，自动映射和代码生成        |
| **学习曲线** | 较低，基本SQL知识即可 | 较高，需要学习框架概念          |
| **代码量**   | 较多，重复代码多      | 较少，抽象程度高                |
| **控制粒度** | 极高，完全控制        | 较低，部分细节被框架封装        |
| **适用场景** | 小型项目、教学场景    | 企业级应用、大型项目            |

本项目使用JDBC直接操作数据库，有利于理解基本原理，但在实际生产环境中，通常会选择使用如MyBatis、Hibernate等成熟框架以提高开发效率和代码可维护性。

### Thinking Questions

1. 如果外部数据（CSV）中有不完整的数据活不一致，怎么办？  
前者，可以在读入时确定必须有的数据列，读入时就进行检查，然后抛弃不完整的数据行或进行记录提示  
后者，在通过数据库系统的约束检查可以返回异常  

2. 总结一下处理数据的原则？  
    1. 不要让少部分的错误数据阻塞了大部分正确的数据的导入  
    2. 对于错误数据，应该进行记录和提示，便于后续的处理  
    3. 部分明显的错误数据可以在读入数就进行检查和提示，不需要等到数据库系统的约束检查  
    4. 对于数据列，可以采用非固定列顺序的方式，先读入首行的列名，然后根据列名的顺序读取数据行，最后将数据行中的数据插入到表中
