# Week 7-8 : Entity-Relation Model - 2

---

- [1. Weak Entity Sets(弱实体集)](#1-weak-entity-sets弱实体集)
    - [Conceptions](#conceptions)
    - [E-R Diagram Representation](#e-r-diagram-representation)
    - [Properties](#properties)
- [2. Extended E-R Features](#2-extended-e-r-features)
    - [Specialization and Generalization](#specialization-and-generalization)
    - [Aggregation](#aggregation)
- [3. Reduction to Relation Schemas](#3-reduction-to-relation-schemas)
- [4. UML](#4-uml)
- [5. ER Modeling Precess](#5-er-modeling-precess)

---

## 1. Weak Entity Sets(弱实体集)

### Conceptions

in a word  
**weak entity set** dosen't contain a PK  
its existence depends on another entity set, called **identifying entity set**  

the PK of the identifying entity set and  
some attributes called **discriminator attribute**  
are used to distinguish the entities in the weak entity set  

actually we use it as extra attributes to distinguish the entities in the weak entity set  

as comparison, any entity sets that are not weak entity set  
is called **strong entity set**  

we can say the identifying entity **owns** the weak entity set  
what the former to the latter is **existence dependent**  

### E-R Diagram Representation

weak entity set is represented by a double rectangle  

the relationship set between the weak entity set and the identifying entity set is represented by a double diamond  

the attributes of the weak entity are underlined with a dashed line  

### Properties

generally  
weak entity set is in total participation with the identifying entity set  

and normally the relationship set between them is many-to-one relationship set  
the weak entity is the many side  

## 2. Extended E-R Features

### Specialization and Generalization

specific performance:  

- Attribute Inheritance  
- Constraints on Generalizations  

### Aggregation

## 3. Reduction to Relation Schemas

## 4. UML

## 5. ER Modeling Precess
