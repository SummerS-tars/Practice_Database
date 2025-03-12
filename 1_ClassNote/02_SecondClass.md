# Week 2 : Relational Model

- [1. Chapter 2 : Relational Database](#1-chapter-2--relational-database)
    - [1.1. Structure of Relational Database](#11-structure-of-relational-database)
    - [1.2. Relation Algebra](#12-relation-algebra)
    - [1.3. Modification of the Database](#13-modification-of-the-database)
- [2. HL 2 : 关系模型实例分析](#2-hl-2--关系模型实例分析)
    - [2.1. 数据需求和关系模式](#21-数据需求和关系模式)

## 1. Chapter 2 : Relational Database

### 1.1. Structure of Relational Database

接Week1[Relational Database](../1_ClassNote/01_FirstClass.md#3-chapter-2--relational-database)中的关于基础概念的介绍

**Relation Database is a collection of tables(表)**  
Every table has a unique name  

As we know, a table is composed of rows and cols  

**Table in Relational Model:**  

- Table: Relation(关系)
- Column: Attribute(属性)  
- Row: Tuple(元组)  

**Relation Schema and Relation Instance:**  

Like a class and an object in OOP  

### 1.2. Relation Algebra

可见实体书Chap6  
PPT lecture 2  

English Ver  


**Basic Operators:**  

1. Selection(选择) : $\sigma$  
    $$\sigma_{salary \geq 85000}(\text{Instructor})$$
2. Projection(投影) : $\Pi$
    $$\Pi_{id, salary}(\sigma_{salary \geq 85000}(\text{Instructor}))$$
3. Union(并) : $\cup$  
4. Difference(差) : $-$  
5. Cartesian Product(笛卡尔积) : $\times$  
6. Rename(重命名) : $\rho$  

**Additional Operators:**  

1. Intersection(交) : $\cap$  
2. natural-join(自然联结): $\bowtie$
3. Theta Join(θ联结) : $\bowtie_{\theta}$  
4. Division(除) : $\div$  
5. Assignment(赋值) : $\leftarrow$  
    *相当于将关系运算结果存储在一个临时变量之中*  

**Extended Operations:**  

1. Generalized Projection(泛化投影) : $\Pi_{F_1, F_2, \ldots, F_n}(R)$
2. Aggregation Functions(聚合函数) : $\text{sum, avg, count, max, min}$
3. Outer Join(外联结) : $\text{left outer join, right outer join, full outer join}$

outer join 外联结找非法数据

**Summary:**  

1. Basic  
    - $\sigma, \Pi, \cup, -, \times, \rho$  
    - 6 types  
    - 3 unary, 3 binary  
2. Additional  
    - $\cap, \bowtie, \bowtie_{\theta}, \div, \leftarrow$
    - Can be defined in terms of basic operators  
    - Simplify and rich the query language  
3. Extended  
    - $\Pi_{F_1, F_2, \ldots, F_n}(R)$  
    - Aggregation Functions  
    - Outer Join

### 1.3. Modification of the Database

**Operations:**  

- Insertion(插入) : $r \leftarrow r \cup \{t\}$
- Deletion(删除) : $r \leftarrow r - \{t\}$
- Updating(更新/修改) : $r \leftarrow r - \{t\} \cup \{t'\}$

## 2. HL 2 : 关系模型实例分析

作为实践前瞻  

### 2.1. 数据需求和关系模式

**数据需求：**  

- 哪些数据？
- 如何使用数据？  

**关系模式：**  

- 满足需求  
- 可产生多种设计结果  

***案例介绍：***  

1. 高考招生系统需求  

    - 功能
    - 数据量
    - 数据内容

    列多了查询慢  

2. 软件工程数据：静态缺陷追踪系统的数据库设计  

    - 主体数据：代码  
    - 过程数据
