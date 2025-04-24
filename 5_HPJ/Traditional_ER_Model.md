# HPJ技术问答数据集评测系统 - 传统ER模型设计

## 传统实体关系模型说明

传统的实体关系(ER)模型使用以下符号:  

- **矩形**: 表示实体集(Entity Sets)
- **菱形**: 表示关系集(Relationship Sets)
- **椭圆**: 表示属性(Attributes)，下划线表示主键属性
- **线条**: 连接实体与关系，双线表示全部参与(Total Participation)

## 实体集(Entity Sets)

### 原始数据实体

1. **RAW_QUESTIONS**

   - <u>question_id</u> (主键)
   - question_text
   - creation_date
   - score
   - view_count

2. **RAW_ANSWERS**
   - <u>answer_id</u> (主键)
   - answer_text
   - creation_date
   - score
   - is_accepted

3. **TAG**
   - <u>tag_id</u> (主键)
   - tag_name
   - description

### 标准问答实体

1. **STD_QUESTIONS**
   - <u>std_question_id</u> (主键)
   - question_text
   - question_type
   - creation_date

2. **STD_QUESTION_SUBJECTIVE** (STD_QUESTIONS的特化)
   - <u>std_question_id</u> (主键)
   - explanation_points

3. **STD_QUESTION_OBJECTIVE** (STD_QUESTIONS的特化)
   - <u>std_question_id</u> (主键)
   - question_subtype
   - options

4. **CANDIDATE_ANSWERS**
   - <u>candidate_id</u> (主键)
   - answer_text
   - submission_date
   - contributor_id

5. **STD_ANSWERS**
   - <u>std_answer_id</u> (主键)
   - answer_text
   - is_official

6. **STD_ANSWER_SUBJECTIVE** (STD_ANSWERS的特化)
   - <u>std_answer_id</u> (主键)
   - key_points
   - explanation

7. **STD_ANSWER_OBJECTIVE** (STD_ANSWERS的特化)
    - <u>std_answer_id</u> (主键)
    - correct_option
    - distractors

### 评测实体

1. **DATASET_VERSION**
    - <u>version_id</u> (主键)
    - version_name
    - release_date
    - description
    - is_active

2. **LLM_MODEL**
    - <u>model_id</u> (主键)
    - model_name
    - version
    - release_date
    - description

3. **EVALUATION_VERSION**
    - <u>eval_version_id</u> (主键)
    - eval_date
    - eval_config

4. **EVALUATION_RESULTS**
    - <u>result_id</u> (主键)
    - coverage_rate
    - error_types
    - accuracy_score
    - eval_timestamp

## 关系集(Relationship Sets)

1. **HAS_ANSWER** (RAW_QUESTIONS 与 RAW_ANSWERS)
   - 一对多关系(1:N)
   - RAW_QUESTIONS 一端: 部分参与
   - RAW_ANSWERS 一端: 全部参与(双线表示)

2. **DERIVES_FROM** (STD_QUESTIONS 与 RAW_QUESTIONS)
   - 多对一关系(N:1)
   - STD_QUESTIONS 一端: 全部参与
   - RAW_QUESTIONS 一端: 部分参与

3. **HAS_TAG** (STD_QUESTIONS 与 TAG)
   - 多对多关系(M:N)
   - 通过关联实体 STD_QUESTION_TAGS 实现

4. **PROPOSES_FOR** (CANDIDATE_ANSWERS 与 STD_QUESTIONS)
   - 多对一关系(N:1)
   - CANDIDATE_ANSWERS 一端: 全部参与
   - STD_QUESTIONS 一端: 部分参与

5. **SELECTED_AS** (CANDIDATE_ANSWERS 与 STD_ANSWERS)
   - 一对一关系(1:1)
   - 非强制性(部分参与)

6. **ANSWERS** (STD_ANSWERS 与 STD_QUESTIONS)
   - 多对一关系(N:1)
   - STD_ANSWERS 一端: 全部参与
   - STD_QUESTIONS 一端: 部分参与

7. **IS_REFERENCED_BY** (DATASET_VERSION 与 EVALUATION_VERSION)
   - 一对多关系(1:N)
   - DATASET_VERSION 一端: 部分参与
   - EVALUATION_VERSION 一端: 全部参与

8. **USES_MODEL** (EVALUATION_VERSION 与 LLM_MODEL)
   - 多对一关系(N:1)
   - EVALUATION_VERSION 一端: 全部参与
   - LLM_MODEL 一端: 部分参与

9. **IS_EVALUATED_IN** (STD_QUESTIONS 与 EVALUATION_RESULTS)
   - 一对多关系(1:N)
   - STD_QUESTIONS 一端: 部分参与
   - EVALUATION_RESULTS 一端: 全部参与

10. **IS_COMPARED_WITH** (STD_ANSWERS 与 EVALUATION_RESULTS)
    - 一对多关系(1:N)
    - STD_ANSWERS 一端: 部分参与
    - EVALUATION_RESULTS 一端: 全部参与

11. **CONTAINS_RESULT** (EVALUATION_VERSION 与 EVALUATION_RESULTS)
    - 一对多关系(1:N)
    - EVALUATION_VERSION 一端: 全部参与
    - EVALUATION_RESULTS 一端: 全部参与

## 特化关系(Specialization)

1. **STD_QUESTIONS 特化**:
   - 上级实体: STD_QUESTIONS
   - 下级实体: STD_QUESTION_SUBJECTIVE, STD_QUESTION_OBJECTIVE
   - 类型: 不相交特化(Disjoint)，基于question_type属性

2. **STD_ANSWERS 特化**:
   - 上级实体: STD_ANSWERS
   - 下级实体: STD_ANSWER_SUBJECTIVE, STD_ANSWER_OBJECTIVE
   - 类型: 不相交特化(Disjoint)，基于答案类型(对应问题类型)

## 可视化建议

要创建此ER模型的可视化图表，推荐使用以下工具:

1. **[draw.io](https://app.diagrams.net/)** (免费在线工具)
   - 支持传统ER图符号
   - 可导出为多种格式
   - 支持协作

2. **[Lucidchart](https://www.lucidchart.com/)** (提供免费版)
   - 专业ER建模工具
   - 具有现代化界面
   - 支持团队协作

3. **[ERDPlus](https://erdplus.com/)** (在线ER工具)
   - 专注于ER建模
   - 可自动转换为关系模式

4. **[MySQL Workbench](https://www.mysql.com/products/workbench/)** (免费)
   - 支持正向工程和逆向工程
   - 适合同时设计数据库模式

按照上述实体集、关系集和属性描述，使用任一工具创建图表时，建议使用Chen表示法(矩形-菱形-椭圆)展示各元素间的关联。

## 与数据库模式图的区别

本文档描述的传统ER模型与之前的数据库模式图的主要区别:

1. **关系表示方式**: 传统ER模型使用菱形表示关系集，而数据库模式图直接用线连接表
2. **属性表示**: 传统ER模型将属性以椭圆形式显示在实体周围，而数据库模式图将属性列在表内
3. **抽象级别**: 传统ER模型更关注概念设计，而数据库模式图更接近物理实现
4. **关系表示**: 传统ER模型中多对多关系不需要显式中间表，而是通过菱形关系集表示

上述传统ER模型保留了之前设计的所有实体和关系，只是采用了不同的表示方法，更适合在概念设计阶段使用。
