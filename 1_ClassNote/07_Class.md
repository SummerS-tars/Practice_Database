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
        - [Decomposing a Schema into BCNF](#decomposing-a-schema-into-bcnf)
    - [3.2. 3NF](#32-3nf)
    - [Comparison between BCNF and 3NF](#comparison-between-bcnf-and-3nf)
    - [3.3. 1NF](#33-1nf)
- [4. Functional Dependency Theory](#4-functional-dependency-theory)
    - [Closure of Functional Dependency Set](#closure-of-functional-dependency-set)
    - [Closure of Attribute Set](#closure-of-attribute-set)
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

but it is easier to retrieve information  

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

if we say a relation schema $R$ related to $F$(a set of functional dependencies)  
is in BCNF  
we means:  

at least one of the following conditions is satisfied:  
for every functional dependency $\alpha \rightarrow \beta$ in $F^+$,

1. $\alpha \rightarrow \beta$ is trivial  
2. $\alpha$ is a superkey of $R$  

and a relational database is in BCNF  
when all the relation schemas are in BCNF  

#### Decomposing a Schema into BCNF

if we want to do the thing mentioned above,  
clearly we are in the situation where the schema is not in BCNF  

in other words, we give as easy example:  
suppose it has a functional dependency $\alpha \rightarrow \beta$  
which isn't trivial  
and $\alpha$ is not a superkey of $R$  

we have said above that we should just do lossless decomposition  
to do this,  
we decompose $R$ into:  
$$
(\alpha \cup \beta) \\
(R - (\beta - \alpha))
$$

we can easily prove that this decomposition satisfies the lossless condition  
and the new relation schemas are in BCNF

### 3.2. 3NF

compared to BCNF, whose all non-trivial functional dependencies' left attributes must be superkeys  
3NF is more relaxed  

it allows one more condition:  

- every attribute A in $\beta - \alpha$ is included in a candidate key of $R$  

*attention the candidate key may not be the same one*  

### Comparison between BCNF and 3NF

we can easily find 3NF is the super set of BCNF  
*that is to say, every BCNF schema is also 3NF*  
so 3NF is a more(or least) relaxed edition of BCNF  

3NF advantages:  
when we decompose a schema into 3NF  
we can definitely keep lossless and **dependency preserving**(保持依赖)

disadvantages:  
we may have to use nulls to represent some relation between attributes  
and may cause some redundancy  

back to our goals of the design:  

1. BCNF  
2. lossless  
3. dependency preserving  

they can't be always satisfied  
so we have to choose between BCNF and 3NF  

### 3.3. 1NF

## 4. Functional Dependency Theory

### Closure of Functional Dependency Set

we have mentioned what is the closure $F^+$ of functional dependency set $F$  
basically, we can use the definition to find the closure  
but it of course not efficient  

so we have **Armstrong's Axioms**(阿姆斯特朗公理):  

- **reflexivity rule**(自反律)  
    if $\beta \subseteq \alpha$  
    then $\alpha \rightarrow \beta$  
- **augmentation rule**(增补律)  
    if $\alpha \rightarrow \beta$  
    then $\alpha \gamma \rightarrow \beta \gamma$  
- **transitivity rule**(传递律)  
    if $\alpha \rightarrow \beta$ and $\beta \rightarrow \gamma$  
    then $\alpha \rightarrow \gamma$  

we can iteratively apply these rules to find the closure
but it's still not efficient enough  

so we have some more rules(can be derived from Armstrong's Axioms):  

- **union rule**(合并律)  
    if $\alpha \rightarrow \beta$ and $\alpha \rightarrow \gamma$  
    then $\alpha \rightarrow \beta \cup \gamma$
- **decomposition rule**(分解律)
    if $\alpha \rightarrow \beta \cup \gamma$  
    then $\alpha \rightarrow \beta$ and $\alpha \rightarrow \gamma$
- **pseudotransitivity rule**(伪传递律)
    if $\alpha \rightarrow \beta$ and $\beta \cup \gamma \rightarrow \delta$  
    then $\alpha \cup \gamma \rightarrow \delta$

### Closure of Attribute Set

at first, we should know a concept: **functionally determine**(函数决定)  
if $\alpha \rightarrow B$ we say $B$ is functionally determined by $\alpha$  

we should know this,  
because if we want to prove that $\alpha$ is a superkey of $R$  
we have to find the $B$ that is functionally determined by $\alpha$  

we can of course use $F^+$ to find the closure of attribute set  
but it is not efficient as it may be very large  
another way is to find the closure $\alpha^+$ of attribute set $\alpha$  
witch is the set of all attributes that are functionally determined by $\alpha$  

## 5. Algorithms for Decomposition
