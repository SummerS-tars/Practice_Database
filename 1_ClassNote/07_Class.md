# Week 9 : Relational Database Design - 1

---

- [1. Features of Good Relational Design](#1-features-of-good-relational-design)
    - [1.1. Larger Schemas](#11-larger-schemas)
    - [1.2. Smaller Schemas](#12-smaller-schemas)
    - [1.3. Lossless Decomposition(无损分解)](#13-lossless-decomposition无损分解)
        - [1.3.1. Definition](#131-definition)
    - [1.4. Normalized Theory](#14-normalized-theory)
- [2. Functional Dependency(函数依赖)](#2-functional-dependency函数依赖)
    - [2.1. Some Basic Character Convention](#21-some-basic-character-convention)
    - [2.2. Definition](#22-definition)
        - [2.2.1. Basic Usage](#221-basic-usage)
    - [2.3. Lossless Decomposition and Functional Dependency](#23-lossless-decomposition-and-functional-dependency)
- [3. Normal Forms(范式)](#3-normal-forms范式)
    - [3.1. BCNF](#31-bcnf)
        - [Definition](#definition)
    - [3.2. 3NF](#32-3nf)
    - [3.3. 1NF](#33-1nf)
- [4. Functional Dependency Theory](#4-functional-dependency-theory)
- [5. Algorithms for Decomposition](#5-algorithms-for-decomposition)

---

## 1. Features of Good Relational Design

Goal:  

1. a set of relation schemas  
2. without unnecessary redundancy  
3. retrieve information easily  

two main design alternatives:  

- Larger Schemas  
- Smaller Schemas  

### 1.1. Larger Schemas

Usually comes from the situation we want to combination of two or more relations  
This may cause data redundancy  

more specifically,  
this happens when the join attributes are not **PK** for **both** schemas  
(if it is, there will be no redundancy)  

### 1.2. Smaller Schemas

Come from above situation, if we started with the improper large schemas  
how do we split(**decompose**) it into proper smaller schemas?  

in other words, what should be the reference standard of the decomposition?
we have to take a deeper view into the relationship among these attributes  

**Attention:**  

- Not all decomposition is good  
    it may cause **loss of information**  
    *actually we get info that can not be recognized*  
    *it is also lost, because the data is useless*  

### 1.3. Lossless Decomposition(无损分解)

we have mentioned info loss above  
the decomposition with loss of info is called **lossy decomposition**  
the reverse is **lossless decomposition**  
we should keep that all the decomposition should be lossless!  

#### 1.3.1. Definition

simply put,  
if we break $R$ into $R_1$ and $R_2$  
(that is to say, $R = R_1 \cup R_2$)  
and it satisfies that  
$\Pi_{R_1}(r) \Join \Pi_{R_2}(r) = r$  
the decomposition is lossless  

### 1.4. Normalized Theory

we may wonder :  
how we know a schema is proper designed?  

we should take a look at the goal above again  
they are the basic evaluation criteria  
to achieve these, we use the normalization procedure to do the design  

so we'll talk about there things below :  

1. whether a schema is "good"  
2. how to decompose a "bad" schema into "good" schemas  

and we need more info in real world  
among these, the mostly used one is **functional dependency**  

## 2. Functional Dependency(函数依赖)

functional dependency comes from the concept of function  
which means that some things will determine some other things  

### 2.1. Some Basic Character Convention

1. attributes set  
    represented by lower case Greek letters  
    e.g. $\alpha$  

2. relation schema  
    represented by capital letters  
    e.g. $R$  

3. relation(table)  
    represented by lower case letters  
    e.g. $r$, $instructor$  

    $r(R)$ represents the relation $r$ with schema $R$  

4. key  
    usually $K$  
    we can say $K$ is a super key of $M$  

5. instance  
    a relation has its own instance at anytime  
    we can say it is "the instance of $r$"  
    or usually just $r$  

6. tuple  
    usually $t$  

### 2.2. Definition

before the introduction of functional dependency,  
we should know **key**  
it is actually the form of constraints in real world  

a **superkey** can distinguish a unique tuple in a relation  
we have know this in previous class  

and the constraints can also be represented by **functional dependency**  

suppose there are :  

1. relation schema $r(R)$  
2. $\alpha \subseteq R$ and $\beta \subseteq R$  
3. $r(R)$ has an instance $r$  
4. $t_1, t_2 \in r$  

if $\forall t_1, t_2 \in r$  
there is $t_1[\beta] = t_2[\beta]$ when $t_1[\alpha] = t_2[\alpha]$  
then we can say that $\alpha$ functionally determines $\beta$  
or the instance satisfies the functional dependency $\alpha \rightarrow \beta$  

if all the legal instances of $r(R)$ satisfy the condition above,  
we say the functional dependency $\alpha \rightarrow \beta$ **holds(成立)** on schema $r(R)$  

we can find some attributes sets will always satisfy the condition  
these functional dependencies are called **trivial(平凡的)**  

e.g.  
$\beta \subseteq \alpha$ then $\alpha \rightarrow \beta$ is trivial  

actually, we can find we are usually able to derive some new functional dependencies based on the existing ones  
we can use the conception of **closure(闭包)** to denotes this  
$F^+$ is the closure of $F$  
it contains all the functional dependencies that can be derived from $F$  
we can easy to know that $F \subseteq F^+$  

#### 2.2.1. Basic Usage

we use functional dependency in two ways:  

1. test relation instance  
    to validate whether it satisfies a specific functional dependency set  

2. declare the constraints over legal relation instances  
    so we only care about the instances satisfying the functional dependency set  
    or $F$ holds on $r$  

### 2.3. Lossless Decomposition and Functional Dependency

functional dependency can be used to find when a decomposition is lossless  

use the notation in [definition](#22-definition)  
and there is a functional dependency set $F$  

if at least one of the following conditions is in $F^+$  

- $R_1 \cap R_2 \rightarrow R_1$  
- $R_1 \cap R_2 \rightarrow R_2$

we can say the decomposition is lossless  

in other words,  
if the intersection of $R_1$ and $R_2$ is a superkey of $R_1$ or $R_2$  
then the decomposition is lossless  

this is a sufficient condition of lossless decomposition  
but not necessary!  
*only when all the constraints are functional dependencies, it is*  

## 3. Normal Forms(范式)

just mentioned in [Normalized Theory](#14-normalized-theory)  
we talk about some important and general normal forms  

### 3.1. BCNF

Boyce-Codd Normal Form(BCNF)  
it eliminates all the redundancy that can be found basic on functional dependencies  
*that is to say, there may still be other redundancy*  

#### Definition



### 3.2. 3NF

### 3.3. 1NF

## 4. Functional Dependency Theory

## 5. Algorithms for Decomposition
