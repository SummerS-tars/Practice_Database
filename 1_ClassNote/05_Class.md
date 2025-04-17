# Week 6-7 : Entity-Relationship Model - 1

---

- [1. Design Process](#1-design-process)
- [2. Modeling](#2-modeling)
    - [2.1. Entity Sets](#21-entity-sets)
    - [2.2. Attributes](#22-attributes)
        - [2.2.1. Types](#221-types)
    - [2.3. Relationship Sets](#23-relationship-sets)
        - [2.3.1. With Attributes](#231-with-attributes)
        - [2.3.2. Degree of a Relationship Set](#232-degree-of-a-relationship-set)
- [3. Constraints](#3-constraints)
    - [3.1. Mapping Cardinalities](#31-mapping-cardinalities)
    - [3.2. Keys](#32-keys)
        - [3.2.1. Keys of Entity Sets](#321-keys-of-entity-sets)
        - [3.2.2. Keys of Relationship Sets](#322-keys-of-relationship-sets)
    - [3.3. Participation Constraints](#33-participation-constraints)
- [4. E-R Diagram](#4-e-r-diagram)
    - [4.1. Basic](#41-basic)
    - [4.2. Attributes](#42-attributes)
        - [4.2.1. An Alternative Representation](#421-an-alternative-representation)
    - [4.3. Cardinality Constraints](#43-cardinality-constraints)
        - [4.3.1. Alternative Notation](#431-alternative-notation)
    - [4.4. Participation Notations](#44-participation-notations)
- [5. Design Issues](#5-design-issues)
    - [5.1. Basic](#51-basic)

---

This Chapter, we pay attention to Entity-Relationship Model  

## 1. Design Process

Conceptual-design  
Logical-design  
Physical-design  

Choose Design?  

**Entity**  

Get rid of two main drawbacks of the relational model:  

**Redundancy**  
**Incompleteness**  

## 2. Modeling

Entity-Relationship Model (ER Model)  

three basic concepts:  

1. Entity Set  
2. Relationship Set  
3. Attributes  

### 2.1. Entity Sets

**Entity**  

**Entity Set**  

### 2.2. Attributes

**Attribute**  

#### 2.2.1. Types

### 2.3. Relationship Sets

**Relationship**  
an association among several entities  

#### 2.3.1. With Attributes

Attribute can also be a property of a relationship set  

#### 2.3.2. Degree of a Relationship Set

Denotes the number of entities sets that participate in a relationship set  

- **Binary**  
- **Ternary**

## 3. Constraints

- Mapping Cardinality
- Keys
- Participation Constraints

*we should separate participation constraints from mapping cardinality constraints.*  
*They are different!*  

### 3.1. Mapping Cardinalities

Express the number of entities to witch another entity can be associated via a relationship set  

- Mapping Cardinality for Binary Relationship Sets
    - One-to-One  
        并不意味着一一对应，而是最多对应一个  
        体现在联系集中，实体集中的一个实体最多参与一个联系  
    - One-to-Many
    - Many-to-One
    - Many-to-Many

### 3.2. Keys

#### 3.2.1. Keys of Entity Sets

just the keys we mentioned before  

- **super key**  
- **candidate key**  
- **primary key**  

#### 3.2.2. Keys of Relationship Sets

A similar mechanism to distinguish among relationships  

### 3.3. Participation Constraints

- Total participation  
- Partial participation  

## 4. E-R Diagram

E-R Diagram(Entity-Relationship Diagram)  

### 4.1. Basic

- Entity Set: rectangle
- Relationship Set: diamond

### 4.2. Attributes

How to denote different types of attributes?  

1. primary key: underline  
2. composite: multi tabs  
3. multivalued: in `{}`  
4. derived: plus `()`  

#### 4.2.1. An Alternative Representation

use ellipses to denote attributes  

### 4.3. Cardinality Constraints

#### 4.3.1. Alternative Notation

### 4.4. Participation Notations

## 5. Design Issues

### 5.1. Basic
