# Class 3 : SQL

## Data Definition Language (DDL)

### Create Table Construct

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

### Delete and Alter Table Constructs

**drop table** command:  

```sql
drop table table_name;
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

## Data Manipulation Language (DML)

main content of the class  

- query data from a database!

### Basic Query

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

### The select Clause

- **select**
    - alternative key word **distinct**  
        to eliminate duplicate tuples  

corresponds to the $\Pi$(project) operator

### The where Clause

- **where** condition

### The from Clause

- **from** relation  

### String Operations

- **like**(字符串匹配运算符)  
    - `%` : matches any **substring**
    - `_` : matches any **single character**

### The Rename Operation

- **as** clause  
    *old-name* **as** *new-name*  

then select and where can use the new name  

### Set Operations

- **union**
- **intersect**
- **except**

Operations above automatically eliminate duplicates  

### Aggregation Functions(聚合函数)



### Recursive Queries(递归查询)

### Complex Queries

### Views
