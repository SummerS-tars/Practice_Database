# First Class

- [1. Class Information](#1-class-information)
- [2. Chapter 1 : Introduction](#2-chapter-1--introduction)
    - [2.1. Why Database?](#21-why-database)
    - [2.2. What is a Database?](#22-what-is-a-database)
    - [2.3. Who uses a Database?](#23-who-uses-a-database)
    - [2.4. How do we make it work？](#24-how-do-we-make-it-work)
    - [2.5. From Theory to Practice](#25-from-theory-to-practice)
- [3. Chapter 2 : Relational Database](#3-chapter-2--relational-database)
    - [3.1. Relation(关系)](#31-relation关系)
    - [3.2. Attributes(属性)](#32-attributes属性)
    - [3.3. key(键)](#33-key键)
- [4. Honor Class 1 : 初识数据库系统（以Oracle为例）](#4-honor-class-1--初识数据库系统以oracle为例)
    - [4.1. Oracle Database](#41-oracle-database)
    - [4.2. Task 1 : 开源和国产数据库调研](#42-task-1--开源和国产数据库调研)

## 1. Class Information

- Textbook  
    Database System Concept(7th ed.) English Ver.  
- Class  
    - Lab and 1 Project  
    - quizzes  
    - final exam  
- Points
    - Lab and Project : 35%  
    - Class and Quiz : 15%
    - Final Exam : 50%

## 2. Chapter 1 : Introduction

### 2.1. Why Database?

How to store and manage data?  

we can use the File System  
But there are many problems we may need to solve  

1. Data redundancy(冗余) and inconsistency  
2. Difficulty in accessing data  
3. Data isolation  
4. Integrity(完整性) problems  
5. Atomicity problems of updates  
    Atomicity 原子性(待补充)  
    概念源自于，某些情况下我们需要对多分存储的文件进行操作，而这些操作要求对所有数据都进行同样操作  
6. Concurrent access anomalies(异常)  
7. Security problems  
Problems is that we want to store data in a system that can solve the above problems  
This is why we need Database

### 2.2. What is a Database?

It may look like a table  
It consists of:  

- Tables  
- Attributes(属性)(Fields)  
- Tuples(元组)(Records)  
- Constraints(约束)(Logical relationship among data)  

**View of data:**  

- Physical level  
- Logical level  
    - data structure
    - data type  
- View level  

We(Database designers) pay most attention to the logical level  
Application developers pay most attention to the view level  

**How:**  

1. Data Abstraction  
    basic thoughts of database  
    - Physical  
    - Logical  
        data , and the relationship among data  
        *Similar to data structure*  
    - Basic Concepts  
        1. Schema(模式)  
            We usually care about the **logical structure** of the database  
            - Physical Schema  
            - Logical Schema
        2. Instance(实例)  
            The **actual content** of the database at a particular point in time  
            Analogous(类似) to hte value of a variable
2. Physical Data Independence  
    - 改变存储方式的时候，不会影响逻辑层  
        也就是通过抽象，使得逻辑稳定  
3. Storage Manager  
    - 提供接口(Interface)，在底层文件存储和应用程序和查询层之间进行通信  
    - 与file manager的Interaction  
    Design Issues of Storage Manager  
4. Query Processing  
    1. Parsing and translation  
        接受查询命令并解析  
        变为relational algebra expression  
    2. Optimization  
        通过statistics about data优化查询  
        得到execution plan  
    3. Evaluation  
5. Transaction Management(事务管理)  
    - Transaction-management component(事务管理组件)  
        - 数据库的健壮性  
    - Concurrency-control Management(并发控制)  

### 2.3. Who uses a Database?  

1. Database administrator  
2. Other users  
    - Naive users  
    - Application programmers  
    - Sophisticated users  
    - Specialized users  

### 2.4. How do we make it work？

1. DDL(Data-Definition Language)  
2. DML(Data-Manipulation Language)  
    Manipulation(操纵)  
    1. Procedural DML(过程式DML)  
        获取数据的方式是确定的  
    2. Declarative DML(声明式DML)  
        确定了哪些数据需要被获取，但不确定获取的方式  
        SQL语句偏向于声明式DML  

### 2.5. From Theory to Practice

1. Data model  
2. SQL  
3. Database Design  
    The process of designing the general structure of the database  
    - Logical Design
        - Business decision(业务决策)  
        - Computer science decision(计算机科学决策)  
            计算机、软件层面的决策  
    - Physical Design  
    Areas:  
    - Problems  
        - 什么样的才是好的？  
    - Solutions  
        - Normalization  
        - Design process(ERM)  
4. Application Design  
    - Client/Server Examples  
        - Two-tier architecture  

            ```mermaid
            flowchart TD
                A[user]-->B[application]-->C(database system)
            ```

        - Three-tier architecture  

            ```mermaid
            flowchart TD
                A[user]-->B[client application]-->C(server application)-->D(database system)
            ```

    - Browser/Server Examples  
        - Web-based application  

## 3. Chapter 2 : Relational Database

续[Relational Database](../1_ClassNote/02_SecondClass.md#chapter-2--relational-database)

### 3.1. Relation(关系)

1. Relation
    - Relation Instance  
    - Relation Schema  
        - instructor is a relation  
        - Corresponding relation schema  
            ```R = (id, name, dept_name, salary)```
        - instructor is a relation on relation schema `R`  
            instructor(`R`)  
2. 关系是什么？  
    关系是元组(tuple)的集合(set)  
    而set是无顺序的  

关系是元组(tuple)的集合  

### 3.2. Attributes(属性)

- Each attribute of a relation has a name  
- Domain of the attribute  
    - the set of allowed values for the attribute
- Atomicity(原子性)  

### 3.3. key(键)

The concept comes from the demand:  
We need to have a way to specify how tuples within a given relation are distinguished  

1. superkey(超键)  
    - Set of attributes
    - Values for which are sufficient to identify a **unique** tuple of each relation r(R)  
2. candidate key(候选键)  
    - one of superkeys  
    - **Minimal**(最小) superkey  
    a relation may have more than one candidate key  
    they must have no common attributes  
3. Primary key(主键) (also as primary key constraints)  
    - one of candidate keys  
    - **chosen** by designer as the principal means of identifying tuple within a relation  
    we usually use underline to denote the primary key in relation schema  
4. Foreign key(外键)  
    - attribute set, for example `A`  
    - `A` for each tuple in `r1` must also have the same value of **primary-key `B`** for some tuple in `r2`  
    - `A` --> a foreign key **from `r1`**, **referencing `r2`**  
        - `r1` referencing relation  
        - `r2` referenced relation  
    - **Attention:**  
        - `B` must be the primary key of `r2`  
        - **referential integrity constraint**: all the value of `A` must could be found in `B`  

*note:*  

Generally, we use several signs to denote some concepts in the database:  

1. `r` --> relation  
2. `R` --> set of attributes in the schema of `r`  
3. `t` --> tuple  
    1. `t(R) or t.R` --> attributes of `R` in tuple `t`  
4. `K` --> subset of `R`  

**Attention:**  
Primary key must be chosen with care  

## 4. Honor Class 1 : 初识数据库系统（以Oracle为例）

1. 课程内容
    1. 主题内容3分一致  
    2. 加深实践训练，提升系统能力1分  
2. 给分  
    1. 基本一致  
    2. 评价内容略有不同  
        - 实践部分PJ独立  
        - 试卷略有不同（附加题）  
            与扩展、实践相关  

### 4.1. Oracle Database

### 4.2. Task 1 : 开源和国产数据库调研  

1. postgresql
2. opengauss
3. e.t.c.

方面:
简单的调研表格  

1. 数据库名称  
2. 厂商
3. 是否开源（开源license）  
4. 市场应用情况  
5. 功能特点  
6. 系统间的亲缘关系  

*注：可以借助LLM辅助，但是需要注明，并且需要自己验证核实*  
