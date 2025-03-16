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
Chapter 2.6 in P48  

**Contents:**  
consists a set of operations  
take one or two relations as input  
return a new relation as result  

**Basic Operators:**  

1. Selection(选择) : $\sigma$  
    $$\sigma_{salary \geq 85000}({Instructor})$$
    - *note:* we can use `AND`, `OR`, `NOT` to combine the conditions  
        in math expression, we use `∧`, `∨`, `¬`  
2. Projection(投影) : $\Pi$
    $$\Pi_{id, salary}(\sigma_{salary \geq 85000}({Instructor}))$$
3. Union(并) : $\cup$  
    $$\Pi_{course_id}(\sigma_{semester=\text{"Fall"}∧year=2017}(section))\cup\\\Pi_{course_id}(\sigma_{semester=\text{"Spring"}∧year=2018}(section))$$
    conditions(**compatible relations**) to make sense:  
    1. input relations have the same number of attributes(arity)  
    2. when the attributes have associated types, the types the attributes in the position must be the same  
4. intersection(交) : $\cap$  
    $$\Pi_{course_id}(\sigma_{semester=\text{"Fall"}∧year=2017}(section))\cap\\\Pi_{course_id}(\sigma_{semester=\text{"Spring"}∧year=2018}(section))$$  
    also the relations being intersected should be **compatible relations**  
5. Set-Difference(差) : $-$  
    $$\Pi_{course_id}(\sigma_{semester=\text{"Fall"}∧year=2017}(section)) -\\\Pi_{course_id}(\sigma_{semester=\text{"Spring"}∧year=2018}(section))$$
    find tuples only in one but not in another  
6. Cartesian Product(笛卡尔积) : $\times$  
    $$r_1 \times r_2$$  
    join every tuple in one relation with every tuple in another relation as one tuple in the result  
    usually combined with selection, projection and renaming operations  
7. Rename(重命名) : $\rho$  
    $$\rho_x(E)$$  
    1. `E` is an expression  
    2. `x` is a new name for `E`
    rename the relation and its attributes

**Additional Operators:**  

1. Intersection(交) : $\cap$  
2. natural-join(自然联结): $\bowtie$
3. Theta Join(θ联结) : $\bowtie_{\theta}$  
4. Division(除) : $\div$  
5. Assignment(赋值) : $\leftarrow$  
    *相当于将关系运算结果存储在一个临时变量之中*  
    very useful to express complex queries  
    $$courses\_fall\_2017 \leftarrow \Pi_{course_id}(\sigma_{semester=\text{"Fall"}∧year=2017}(section))\\courses\_spring\_2018 \leftarrow \Pi_{course_id}(\sigma_{semester=\text{"Spring"}∧year=2018}(section))\\courses\_fall\_2017 \cup courses\_spring\_2018$$

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
