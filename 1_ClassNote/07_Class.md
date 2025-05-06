# Week 9 : Relational Database Design - 1

---

- [1. Features of Good Relational Design](#1-features-of-good-relational-design)
    - [Larger Schemas](#larger-schemas)
    - [Smaller Schemas](#smaller-schemas)
    - [Summary](#summary)
- [2. Functional Dependency and Normal Forms](#2-functional-dependency-and-normal-forms)
    - [2.1. BCNF](#21-bcnf)
    - [2.2. 3NF](#22-3nf)
    - [2.3. 1NF](#23-1nf)
- [3. Functional Dependency Theory](#3-functional-dependency-theory)
- [4. Algorithms for Decomposition](#4-algorithms-for-decomposition)

---

## 1. Features of Good Relational Design

Goal:  

1. a set of relation schemas  
2. without unnecessary redundancy  
3. retrieve information easily  

two main design alternatives:  

- Larger Schemas  
- Smaller Schemas  

### Larger Schemas

Usually comes from the situation we want to combination of two or more relations  
This may cause data redundancy  


### Smaller Schemas

Come from above situation, if we started with the improper large schemas  
how do we split(or **decompose**) it into proper smaller schemas?  

**Attention:**  

- Not all decomposition is good  
    it may cause **loss of information**  

### Summary

What is good to do?  
to answer this question  
our theory is based on:  

- Functional dependencies  
- Multivalued dependencies  

## 2. Functional Dependency and Normal Forms

### 2.1. BCNF

### 2.2. 3NF

### 2.3. 1NF

## 3. Functional Dependency Theory

## 4. Algorithms for Decomposition
