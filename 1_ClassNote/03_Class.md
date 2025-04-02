# Class 3 : Introduction to SQL

---

- [1. Data Definition Language (DDL)](#1-data-definition-language-ddl)
    - [1.1. Create Table Construct](#11-create-table-construct)
    - [1.2. Basic Schema Definition](#12-basic-schema-definition)
    - [1.3. Delete and Alter Table Constructs](#13-delete-and-alter-table-constructs)
- [2. Data Manipulation Language (DML)](#2-data-manipulation-language-dml)
    - [2.1. Basic Query](#21-basic-query)
        - [2.1.1. The select Clause](#211-the-select-clause)
        - [2.1.2. The where Clause](#212-the-where-clause)
        - [2.1.3. The from Clause](#213-the-from-clause)
        - [2.1.4. Query on Multiple Relations](#214-query-on-multiple-relations)
    - [2.2. Additional Basic Operations](#22-additional-basic-operations)
        - [2.2.1. String Operations](#221-string-operations)
        - [2.2.2. The Rename Operation](#222-the-rename-operation)
        - [2.2.3. Ordering the Display of Tuples](#223-ordering-the-display-of-tuples)
        - [2.2.4. Set Operations](#224-set-operations)
    - [2.3. Aggregation Functions(聚合函数)](#23-aggregation-functions聚合函数)
        - [2.3.1. Basic Aggregation](#231-basic-aggregation)
        - [2.3.2. Aggregation with Grouping(分组)](#232-aggregation-with-grouping分组)
        - [2.3.3. The `Having` Clause](#233-the-having-clause)
        - [2.3.4. Summary about Aggregation Functions](#234-summary-about-aggregation-functions)
    - [2.4. Null Values](#24-null-values)
        - [2.4.1. Arithmetic Expression](#241-arithmetic-expression)
        - [2.4.2. Comparison Expression](#242-comparison-expression)
        - [2.4.3. Distinct](#243-distinct)
        - [2.4.4. Aggregation with Null and Boolean Values](#244-aggregation-with-null-and-boolean-values)
    - [2.5. Nested Subqueries(嵌套子查询)](#25-nested-subqueries嵌套子查询)
        - [2.5.1. Subqueries in the `where` Clause](#251-subqueries-in-the-where-clause)
            - [2.5.1.1. Set Membership](#2511-set-membership)
            - [2.5.1.2. Set Comparison](#2512-set-comparison)
            - [2.5.1.3. Test for Empty Relations](#2513-test-for-empty-relations)
            - [2.5.1.4. Test for the Absence of Duplicate Tuples](#2514-test-for-the-absence-of-duplicate-tuples)
        - [2.5.2. Subqueries in the `from` Clause](#252-subqueries-in-the-from-clause)
        - [The `with` Clause](#the-with-clause)
        - [Scalar Subqueries(标量子查询)](#scalar-subqueries标量子查询)
            - [Scalar Without a `from` Clause](#scalar-without-a-from-clause)
    - [Modification of the Database](#modification-of-the-database)

[SQL part2](04_Class.md)  

---

## 1. Data Definition Language (DDL)

### 1.1. Create Table Construct

**create table** command:  

```sql
create table table_name (
    column_name1 data_type1,
    column_name2 data_type2,
    ...
);
```

**type:**  

- char(n): fixed-length character string, n characters long  
    if the string is shorter than n, it is padded with spaces
- varchar(n): variable-length character string, maximum n characters long  
    if the string is shorter than n, it is not padded  
    query performance is slower than char(n)  
- numeric(p, d): fixed-point number, p digits long, d of which are to the right of the decimal point
- int: integer
- smallint: small integer
- real
- float(n): floating-point number, at least n digits long

**Integrity Constraints:**  

- not null
- primary key(column_name)
- foreign key(column_name) references table_name(column_name)

### 1.2. Basic Schema Definition

e.g.  

```sql
create table instructor(
    ID char(5) ,
    name varchar(20) not null,
    dept_name varchar(20),
    salary numeric(8, 2),
    primary key (ID),
    foreign key (dept_name) references department,
);
```

*primary key declaration on an attribute automatically ensures not null*  

**General Format:**  

$$
\begin{aligned}
&create\ table\ r\\
&\ \ \  (A_1\ D_1,\\
&\quad A_2\ D_2,\\
&\quad \ldots,\\
&\quad A_n\ D_n,\\
&\quad <integrity-constraint_1>\\
&\quad \ldots,\\
&\quad <integrity-constraint_n>);
\end{aligned}
$$

### 1.3. Delete and Alter Table Constructs

**drop table** command:  

```sql
drop table table_name;
```

**drastic operation!**  
we can use another sentence to delete all tuples in a table beside itself  

```sql
delete from table_name;
```

**alter table** command:  

- Add attributes  

    ```sql
    alter table table_name
        add column_name data_type;
    ```

- Drop attributes  

    ```sql
    alter table table_name
        drop column column_name;
    ```

- Modify constraints  

    ```sql
    alter table table_name
        add constraint primary key(column_name);
    ```

**Attention:**  

drop and add attributes should be used with caution!  

---

## 2. Data Manipulation Language (DML)

main content of the class  

- query data from a database!(basic)
- statistics(aggregation functions)  
- adding, deleting, and modifying data  

*Query Language is more than 'query'!*  

### 2.1. Basic Query

- general form:

    select $A_1, A_2, \ldots, A_n$
    from $r_1, r_2, \ldots, r_m$
    where $P$

    - $A_i$ represents an attribute  
    - $r_i$ represents a relation(table)
    - $P$ represents a predicate
- equivalent relational algebra expression:

    $\Pi_{A_1, A_2, \ldots, A_n}(\sigma_P(r_1 \times r_2 \times \ldots \times r_m))$

- Result of an SQL query is a **Relation**!

*Note: SQL names are case insensitive*  
*So Name = NAME = name*  

#### 2.1.1. The select Clause

- **select**
    - alternative key word **distinct**  
        *duplicate elimination is time-consuming, so SQL allows duplicates in relations as well as in the results of SQL expressions*  
        **distinct** is provided to eliminate duplicate tuples  
        e.g.  
        select distinct $dept\_name$  
        from $instructor$;  
    - $+, -, *, /$ are allowed in select clause in arithmetic expressions  
        *attention that this only affect the result*  

corresponds to the $\Pi$(project) operator  

#### 2.1.2. The where Clause

- **where** condition  
    - **and, or, not** (logical connectives) are allowed  
    - $<, <=, >, >=, =, <>$ (comparison operators) are allowed  
        strings, arithmetic expressions as well as special types(like date types) are allowed to be compared  

#### 2.1.3. The from Clause

- **from** relation  
    restrict the relations to be used in the query  

**Attention:**  
when an attribute name appears in more than one relation  
we use **$relation\_name.attribute\_name$** to specify the attribute  

#### 2.1.4. Query on Multiple Relations

1. three types of clauses
    1. `select`: used to list the attributes desired in the result of a query  
    2. `from`: a list of the relations to be accessed in the evaluation of the query  
    3. `where`: a predicate involving attributes of the relation in the from clause  
2. general meaning of an SQL query  
    - **from** generate a Cartesian product  
    - **where** apply the predicates  
    - **select** select the attributes to be output  
    *here is the a way to understand how the result is generated but not how the database actually execute the query!*  

### 2.2. Additional Basic Operations

#### 2.2.1. String Operations

- **like**(字符串匹配运算符)  
    - `%` : matches any **substring**
    - `_` : matches any **single character**
- `||` : concatenation(连接)  
- uppercase/lowercase  
    - **upper**(string) : convert to uppercase  
    - **lower**(string) : convert to lowercase
- remove spaces in string  
    - **ltrim**(string) : remove leading spaces  
    - **rtrim**(string) : remove trailing spaces  
    - **trim**(string) : remove both leading and trailing spaces

e.g. for **like**  

- 'Intro%' : 'Intro' + any substring  
- '%Comp%' : any string contains 'Comp'  
- '___' : any string with 3 characters  
- '___%' : any string with at least 3 characters  

we can use **not like** to exclude the strings that match the pattern  

*To include the `%` and `_` and `\` in patterns, we add `\` before them, and then add **escape** '\' after the pattern*  
e.g. `like 'A\%' escape '\'`  
find any string that starts with 'A%'  

#### 2.2.2. The Rename Operation

- **as** clause  
    *old-name* **as** *new-name*  

**as** can appear in both the **select** and **from** clauses  
it can rename both attributes and relations  
usually used to rename the relations with a shortened version  

Another important situation where **as** is needed is
when we want to compare the tuples in the same relation  

e.g. "find the names of all instructors whose salary is greater than at least one instructor in th eBiology department"  

**select distinct** $T.name$  
**from** $instructor\ as\ T, instructor\ as\ S$  
**where** $T.salary > S.salary$ **and** $S.dept\_name = \text{'Biology'}$  

#### 2.2.3. Ordering the Display of Tuples

**order by**:  

- format  
    **order by** $attribute_name$  
    - **asc** : ascending order (default)
    - **desc** : descending order
- multiple attributes as ordering reference  
    implement according to the order how the attributes appear in the **order by** clause  

#### 2.2.4. Set Operations

- **union**
- **intersect**
- **except**

Operations above automatically eliminate duplicates  

### 2.3. Aggregation Functions(聚合函数)

- Average(平均值) : **avg**  
- Minimum(最小值) : **min**  
- Maximum(最大值) : **max**  
- Total(总和) : **sum**  
- Count(计数) : **count**  

#### 2.3.1. Basic Aggregation

always used in **select**  
with the basic **format**:  
aggregate\_function($attribute\_name$)  
as an attribute in the result relation  
usually compared with **as** operation to rename it  s

#### 2.3.2. Aggregation with Grouping(分组)

when we need to apply the aggregate function to a group of sets of tuples  
we use **group by** clause  
**format**:  
**group by** attribute  

the position of above clause is after where(if exists)  
**Attention:**  
we are not allowed to **select** any attribute that is not in the **group by** clause  
In other word, we only allowed to **select** aggregated attributes and the attributes in the **group by** clauses  

*additionally, we can use `/* comment */` or `-- comment` to add a comment*  

#### 2.3.3. The `Having` Clause

Add constraints to the result of the **group by** clause  
**format:**  
**having** **aggregate_function**($attribute\_name$) $comparison\_operator$ value  

after **group by** clause  

#### 2.3.4. Summary about Aggregation Functions

```mermaid
flowchart LR
    A[from]-->B[where]
    B-->C[group by]
    C-->D[having]
    D-->E[select]
```

### 2.4. Null Values

**Null** values are also widely used  
means the value is unknown or inexistent  

to test for null values  
we use **is null** and **is not null**  

but its existence complicates the behavior of queries  
here we talk about several situations when the operations involve null values  

#### 2.4.1. Arithmetic Expression

involving $+,-,*,/$  s
when null is involved in the expression  
the result is **null**  

#### 2.4.2. Comparison Expression

involving $<, <=, >, >=, =, <>$  
this time we can not just say the result is false  
cause if we do so,  
we put not before it and it will work out the result as true  
which is usually not we expect  
Instead, we use **unknown** to denote this situation  

- **and**  
    - true and unknown = unknown
    - false and unknown = false
    - unknown and unknown = unknown
- **or**
    - true or unknown = true
    - false or unknown = unknown
    - unknown or unknown = unknown
- **not**
    - not unknown = unknown

and like **is null** and **is not null**  
we can test **unknown**  
by using **is unknown** and **is not unknown**  

#### 2.4.3. Distinct

unlike comparison expressions  
**select distinct** clause treats null values as identical  

#### 2.4.4. Aggregation with Null and Boolean Values

**null** treatment method:

- count: count the number of non-null values  
    if there are no non-null values, the result is 0  
- other: ignore null values  
    if all values are null, the result is null  

***Boolean** data type can take values **true, false** and **unknown***  

### 2.5. Nested Subqueries(嵌套子查询)

**subquery:**  
A **select-from-where** expression which is nested within another query  

#### 2.5.1. Subqueries in the `where` Clause

**usage:**  

- perform tests for set membership  
- make set comparisons  
- determine set cardinality  

##### 2.5.1.1. Set Membership

**in** and **not in**  
testing tuples for membership in a relation  
*(in other word, test whether the tuples are in a relation)*  

e.g.  

```sql
select distinct course_id
from section
where semester = 'Fall' and year = 2017 and
      course_id in (select course_id
                    from section
                    where semester = 'Spring' and year = 2018)；
```

previously we use **intersect** to work out two relations first  
and then intersect them together to get the result relation  
here we use **in** to test the membership of the tuples  

*Note that we should use `distinct` cause `intersect` removes duplicates by default while `in` doesn't*  

Above situation we test one-attribute relation  
in fact, arbitrary relations can be used in the **in** clause  

##### 2.5.1.2. Set Comparison

provide two key words:  
**some** and **all**  

similar to **in**  
to use them, we need to put them before the subquery  

for their usage  
we can understand by mathematical symbols:  

- **some** : $\exist$  
- **all** : $\forall$  

we can use these to understand all the comparisons using **some** or **all**  
*additionally, `= some` equals to `in` , `<> all` equals to `not in`*  

##### 2.5.1.3. Test for Empty Relations

**exists** and **not exists**  

**exists** test whether the result of a subquery is empty or not  

*Note that a **correlation name** in a query can be used in its subqueries and these subqueries are also called **correlated subquery***  

a useful expression:  
**not exists**($B$ **except** $A$) : relation $A$ contains $B$  

##### 2.5.1.4. Test for the Absence of Duplicate Tuples

**unique** and **not unique**  

**unique** tests whether the result of a subquery contains duplicate tuples or not  
if not it returns true  
*Note that if the result relation is empty, it also returns true*  

#### 2.5.2. Subqueries in the `from` Clause

Subqueries can also be used in the **from** clause  
the result relation of the subqueries can be used just like other relations  

e.g.  

```sql
select dept_name, avg_salary
from (select dept_name, avg(salary)
    from instructor
    group by dept_name)
    as dept_avg(dept_name, avg_salary)
where avg_salary > 42000;
```

*Note that some SQL implementations ask that each subquery relation in the **from** clause must be given a name(notably MySQL and PostgreSQL)*  

By default, the subqueries in a **from** clause  
are not able to use the correlation names of other relations in the same from clause  
More recent SQL:2003 use **lateral** before the subquery  
means it is allowed to use them before it  

#### The `with` Clause

**with** clause provides a way  
to define a temp relation only available to the query including it  

Very similar to assignment operations(or macros definition)  
give a subquery a name(the string after **with**)  
and then use it in the main query  
*And it is only can be used at the part after itself in this query*  

unlike subqueries in the **from** clause  
**lateral** is not needed  
the name of the temp relation can be used after itself(including the **with** clause)  

e.g.  

```sql
with max_budget(value) as
    (select max(budget)
    from department)
select dept_name, budget
from department, max_budget
where department.budget = max_budget.value;
```

#### Scalar Subqueries(标量子查询)

**scalar subquery:**  
the subquery return only one tuple with only one attribute  
these subqueries can occur wherever an expression returning a value is permitted  
that is, it can also be used in **select** clause(most time compared with **as**)  

e.g.  
**count**(*) without **group by** is guaranteed to return only a single value  

##### Scalar Without a `from` Clause

e.g.  

```sql
(select count(*) form teaches) / (select count(*) from instructor);
```

at some implementations, this query is not permitted  
because of lack of **from** clause(notably Oracle)  
some can add **from** $dual$ to the query  

*additionally, note that usually the two numbers above are integer, so the quotient is also integer, witch may consult the loss of precision*  
*to solve this problem, we can perform just like programming language: add `* 1.0` to convert one to float*  

### Modification of the Database

- **delete**  
- **insert**  
- **update**  

saw in [next note](04_Class.md)  
