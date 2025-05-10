# Week 7-8 : Entity-Relation Model - 2

---

- [1. Weak Entity Sets(弱实体集)](#1-weak-entity-sets弱实体集)
    - [1.1. Conceptions](#11-conceptions)
    - [1.2. E-R Diagram Representation](#12-e-r-diagram-representation)
    - [1.3. Properties](#13-properties)
- [2. Extended E-R Features](#2-extended-e-r-features)
    - [2.1. Specialization and Generalization](#21-specialization-and-generalization)
        - [2.1.1. Specialization(特化)](#211-specialization特化)
        - [2.1.2. Generalization(泛化，概化)](#212-generalization泛化概化)
        - [2.1.3. Constraints](#213-constraints)
    - [2.2. Aggregation](#22-aggregation)
- [3. Reduction to Relation Schemas](#3-reduction-to-relation-schemas)
    - [Strong Entity Sets](#strong-entity-sets)
    - [Composite Attributes](#composite-attributes)
    - [Weak Entity Sets](#weak-entity-sets)
    - [Relationship Sets](#relationship-sets)
    - [Redundancy of Schemas](#redundancy-of-schemas)
        - [Relationship Sets Linking Weak and Strong Entity Sets](#relationship-sets-linking-weak-and-strong-entity-sets)
        - [Merge Schemas](#merge-schemas)
    - [Generalization](#generalization)
    - [Aggregation](#aggregation)
    - [Issues](#issues)
- [4. UML](#4-uml)

---

## 1. Weak Entity Sets(弱实体集)

### 1.1. Conceptions

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

### 1.2. E-R Diagram Representation

weak entity set is represented by a double rectangle  

the relationship set between the weak entity set and the identifying entity set is represented by a double diamond  

the discriminator attributes of the weak entity are underlined with a dashed line  

### 1.3. Properties

generally  
weak entity set is in total participation with the identifying entity set  

and normally the relationship set between them is many-to-one relationship set  
the weak entity is the many side  

## 2. Extended E-R Features

### 2.1. Specialization and Generalization

specific performance:  

- Attribute Inheritance  
- Constraints on Generalizations  

lower-level entity sets are the sets below the higher-level entity sets  

#### 2.1.1. Specialization(特化)

a top-down design process  
from basic entity sets to more specialized entity sets  

the lower-level entity set inherits all the attributes of the higher-level entity set  

#### 2.1.2. Generalization(泛化，概化)

a bottom-up design process  
combine some entity sets sharing the same features into a higher-level entity set  

it's some kind the inversion of specialization  

the concepts of superclass and subclass in OOP are also used here  

#### 2.1.3. Constraints

1. **Disjoint** or **Overlapping** specialization  
    - disjoint specialization(不相交特化，互斥特化)  
        means that the higher-level entity set can be only specialized into most one lower-level entity set  
    - overlapping specialization(重叠特化)  
        can specialized to more than one  

2. completeness constraint(完全性约束)  
    - **total**  
        similar to total participation in relationship set  
        here it means the higher-level entity set must be specialized into at least one lower-level entity set  
    - **partial**  
        means that the higher-level entity set can be not specialized  

### 2.2. Aggregation

aggregation means treating a relationship as an abstract entity  
so we can define relationships between relationships  

## 3. Reduction to Relation Schemas

### Strong Entity Sets

if it only contains simple attributes  
it's easy to do the reduction:  
just create a relation schema contains all the attributes corresponding to the entity set  

### Composite Attributes

it's of course more complicated  

1. composite attributes  
    we create attributes corresponding to the basic attributes(atomic, can't be divided to smaller parts)  
    but not for the composite attributes itself  

2. multivalued attributes  
    we create a new schema for the multivalued attribute itself  

3. derived attributes  
    not represented in the relation schema  
    it can be represented by procedure, function, method or etc.  

### Weak Entity Sets

similar to strong entity sets  
but with additional attributes: the identifying entity set's PK  

### Relationship Sets

the union of the PKs of the participating entity sets  
with the descriptive attributes of the relationship set itself  

### Redundancy of Schemas

some (relationship) sets in the E-R diagram can not exist in the relation schema  
as they can be eliminated or merged into other schemas  
we can say they are redundant  
here we'll talk about the details about the redundancy of schemas  

#### Relationship Sets Linking Weak and Strong Entity Sets

generally, the relationship set linking weak and strong entity sets is redundant  
and can be omitted in the relation schema  

as the weak entity set has the PK of the strong entity set  
and this kind of relationship set is many-to-one and without descriptive attributes  

#### Merge Schemas

prerequire:  

1. many-to-one(A to B)  relationship set(AB)  
2. A is totally participating in AB  

then we can merge A and AB into a single schema  
rather than creating a new schema for AB  
the PK of the new schema is the PK of A  

and to be more general  
we talk about some other cases  

1. one-to-one  
    arbitrary side of two can be merged  
2. partial participation  
    we can use null values to take the position and then merge  

### Generalization

suppose there are three entity sets:  

Higher-level entity set A  
Lower-level entity sets B and C  

1. create a schema for higher-level set A(r1)  
    and create a schema for each lower-level set B(r21) and C(r22)  
    all schemas share the same PK of A  
    and the lower-level schemas(r22 and r21) should have foreign keys referring the higher-level schema(r1)
2. for disjoint and total specialization cases  
    in our example, we do not create r1 but only r21 and r22  
    and r21 and r22 contain all the attributes of r1  

### Aggregation

the PK of aggregation is the PK of the relationship set defining the aggregation  
it needs no more schema but uses the schema of the relationship set above  

### Issues

we may encounter some issues  
mentioned last part  
[E-R design issues](./05_Class.md#5-design-issues)  

## 4. UML

UML(Unified Modeling Language, 统一建模语言)  

components:  

1. class diagram(类图)  
    similar to E-R diagram  
2. use case diagram(用例图)  
    represent how user and system interact  
    especially about the sequence acted by user  
3. activity diagram(活动图)  
    the task flow of the parts of the system  
4. implementation diagram(实现图)  
