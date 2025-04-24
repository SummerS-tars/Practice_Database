# Week 6-7 : Entity-Relationship Model - 1

---

- [1. Design Process](#1-design-process)
    - [1.1. Design Phase](#11-design-phase)
    - [1.2. Design Selection](#12-design-selection)
- [2. Modeling](#2-modeling)
    - [2.1. Entity Sets](#21-entity-sets)
    - [2.2. Basic Attributes](#22-basic-attributes)
        - [2.2.1. Types](#221-types)
    - [2.3. Relationship Sets](#23-relationship-sets)
        - [2.3.1. With Attributes](#231-with-attributes)
        - [2.3.2. Specifications](#232-specifications)
        - [2.3.3. Degree of a Relationship Set](#233-degree-of-a-relationship-set)
    - [2.4. Complicated Attributes](#24-complicated-attributes)
        - [2.4.1. Concepts](#241-concepts)
        - [2.4.2. Types](#242-types)
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
    - [4.4. Participation Notations](#44-participation-notations)
    - [4.3.1. Complicate Notation for Cardinality and Participation Constraints](#431-complicate-notation-for-cardinality-and-participation-constraints)
- [5. Design Issues](#5-design-issues)

---

This Chapter, we pay attention to Entity-Relationship Model  

## 1. Design Process

### 1.1. Design Phase

1. get requirements  
    specify the requirements from the user  
2. Conceptual-design  
    Result the construction of E-R diagram  
    not deep into the details of physical storage details  
3. specification of functional requirement  
    review the model to confirm it is able to implement the functional requirements  
4. Abstract data model to database implementation  
    1. Logical-design phase  
    2. Physical-design phase  

### 1.2. Design Selection

Get rid of two main drawbacks of the relational model:  

1. **Redundancy**  
2. **Incompleteness**  

## 2. Modeling

Entity-Relationship Model (ER Model)  

three basic concepts:  

1. **Entity Set**(实体集)  
2. **Relationship Set**(联系集)  
3. **Attribute**(属性)  

### 2.1. Entity Sets

**Entity**  

**Entity Set**  

**Extension**  
extension of entity set means its the real set  
containing the entities of the entity set  
*similar to the relation between the `relation instance` and the `relation`*  

### 2.2. Basic Attributes

**Attribute**  
entity is denoted by a set of attributes  
attributes is the descriptive properties shared by all entities in the entity set  

#### 2.2.1. Types

### 2.3. Relationship Sets

**Relationship**  
an association among several entities  

**Relationship Set**  
the set of relationships of the same type  

**Relationship instance**  
one relationship denotes the association among several entities  

**Participate**  
a relationship set can relate to on or more entity sets  
the association among these entity sets is called participation  
we can say:  
$E_1, E_2, ..., E_n$ participate in the relationship set $R$  

**role**  
the function of the entity in the relationship  
*usually omitted in normal situations*  
*when the meaning is needed to be explained, it is useful*  
*for example, the entity sets participating in a relationship aren't distinct*  
*we need to use role name to present how the entity participates in the relation instance*  

#### 2.3.1. With Attributes

Relationship can own attributes  
witch are called **Descriptive Attribute**  

#### 2.3.2. Specifications

E-R diagram can be very complicated  
that we may use several pages to present it  

we need to:  

1. show one relationship set only at one place  
2. show the attributes of one entity set only at the first place it appears  

#### 2.3.3. Degree of a Relationship Set

Denotes the number of entities sets that participate in a relationship set  

- **Binary**  
    most of the relationship sets are binary  
- **Ternary**  

**Degree**  
the number of entity sets that participate in a relationship set  

### 2.4. Complicated Attributes

#### 2.4.1. Concepts

**Domain**(or value set)  
the available values of an attribute  

#### 2.4.2. Types

- **simple** and **composite**  
    the latter can be divided into several sub-attributes  
    *usually used in the situation:*  
    *sometime the hole attribute is needed*  
    *sometime only part ot the attribute is needed*  

    *besides , it can have several levels of composition*  
- **single-values** and **multivalued**  
    multivalued attribute means it can have 0, 1 or more values  
- **derived**  
    means the attribute can be derived(派生) from the value of other attributes or entity  
    derived attributes should not be stored, but be calculated when needed  
    *at normal situation*  

    the attributes storing the values used to derive the derived attribute  
    are called base or stored attributes  

how to denote these in E-R diagram?  
presented [below](#42-attributes)  

## 3. Constraints

- Mapping Cardinality
- Keys
- Participation Constraints

*we should separate participation constraints from mapping cardinality constraints.*  
*They are different!*  

### 3.1. Mapping Cardinalities

Express the number of entities to witch another entity can be associated via a relationship set  
mostly used in binary relationship sets  

- Mapping Cardinality for Binary Relationship Sets
    - One-to-One  
        并不意味着一一对应，而是最多对应一个  
        体现在联系集中，实体集中的一个实体最多参与一个联系  
    - One-to-Many
    - Many-to-One
    - Many-to-Many

the cardinality of a specific relationship set is depend on the situation of the real world  

### 3.2. Keys

#### 3.2.1. Keys of Entity Sets

just the keys we mentioned before in chapter about relation schema  

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

1. primary key: underlined  
2. composite: multi tabs  
    e.g.  

    ```txt
    name
        first_name
        middle_name
        last_name
    ```

3. multivalued: in `{}`  
    e.g.  

    ```txt
    { phone_number }
    ```

4. derived: plus `()`  
    e.g.  

    ```txt
    age()
    ```

#### 4.2.1. An Alternative Representation

use ellipses to denote attributes  

### 4.3. Cardinality Constraints

use arrow to denote the side pointing to can participate in the relationship at most 1 time  
no arrow means no constraint  

### 4.4. Participation Notations

single line to denote partial participation  
double line to denote total participation  

### 4.3.1. Complicate Notation for Cardinality and Participation Constraints

use `l..h` to denote the specific limitation of the times each entity can participate in the relationship  
`l` means lower, the minimum number of entities in the relationship  
`h` means higher, the maximum number of entities in the relationship  

`*` is used to denote no limitation for maximum  
*`0` of `l` can denote for no limitation of minimum(any value between 0 and maximum)*  

*easy to understand, this can not only represent the cardinality constraints*  
*but also the participation constraints*  

## 5. Design Issues

**Basic Issues:**  

- entity sets vs. attributes  
- entity sets vs. relationship sets  
- binary relationship vs. non-binary relationship sets  
- placement of relationship attributes  

**Common Mistakes:**  

- use primary key of an entity set as an attribute of another entity set  
- designate primary-key attributes of related entity sets as attributes of the relationship set  
- use a relationship with a single-valued attribute in a situation that requires a multivalued attribute  
