# Design For HPJ

## Question Translate

以下是为您的PJ设计的详细需求规范化和技术细节文档，便于您进一步构思和实现数据库设计：

---

## **1. 项目目标**

构建一个面向LLM评测的技术问答数据集数据库，支持：

- **数据管理**：存储Stack Exchange原始数据（问题、答案、标签、评论）。
- **标准问答库**：构建标准问题和参考答案（含关键评分点）。
- **模型评测**：记录不同LLM的回答及其评测结果（覆盖率、错误类型、排名）。

---

## **2. 功能性需求**

### **2.1 核心模块**

| 模块           | 功能描述                                                                 |
|----------------|------------------------------------------------------------------------|
| **数据导入**   | 从Stack Exchange数据转储（Posts.xml, Tags.xml, Comments.xml）导入数据，支持增量更新。 |
| **标准问答库** | 构建主观题/客观题的标准问题集，关联参考答案和评分要点（JSON格式）。                     |
| **评测管理**   | 记录LLM模型生成的回答，对比标准答案生成覆盖率、错误类型等评测结果。                     |
| **统计分析**   | 按模型、时间、问题类别生成评测报告（如关键点覆盖率、错误分布、模型排名）。                |

### **2.2 数据实体**

| 实体               | 核心属性                                                                 |
|--------------------|------------------------------------------------------------------------|
| **Post**           | PostId (PK), PostTypeId (1=问题, 2=答案), ParentId, AcceptedAnswerId, Score, Tags, Body, OwnerUserId, ViewCount |
| **Tag**            | TagId (PK), TagName, Count, ExcerptPostId, WikiPostId, IsModeratorOnly |
| **Comment**        | CommentId (PK), PostId (FK), Text, CreationDate, UserId               |
| **StandardQuestion** | QuestionId (PK), QuestionText, Category (主观/客观), Tags (关联Linux内核相关标签) |
| **StandardAnswer** | AnswerId (PK), QuestionId (FK), AnswerText, KeyPoints (JSON)          |
| **LLMModel**       | ModelId (PK), ModelName, Version, ReleaseDate                         |
| **Evaluation**     | EvaluationId (PK), ModelId (FK), QuestionId (FK), CoverageRate, ErrorTypes (JSON), Timestamp |

### **2.3 关键关系**

| 关系类型           | 描述                                                                 |
|--------------------|--------------------------------------------------------------------|
| **Post ↔ Tag**     | 多对多（通过中间表 `PostTags`），记录每个问题关联的标签（如linux、kernel）。 |
| **Question ↔ Answer** | 一对多（通过 `ParentId`），一个标准问题可关联多个候选答案。                |
| **Question ↔ Evaluation** | 一对多，每个问题可被多个模型评测。                                      |
| **Model ↔ Evaluation** | 一对多，每个模型可生成多个评测结果。                                    |

---

## **3. 非功能性需求**

### **3.1 性能要求**

- **数据导入**：支持批量导入Stack Exchange数据（单次处理百万级记录）。
- **查询效率**：评测结果分析（如按模型、时间段筛选）响应时间 < 2秒。

### **3.2 数据安全**

- **敏感字段**：用户信息（如OwnerUserId）需匿名化处理。
- **权限控制**：区分数据管理员（读写）、评测员（只读）。

### **3.3 可扩展性**

- **动态字段**：使用JSON类型存储评分要点（KeyPoints）、错误类型（ErrorTypes）。
- **版本管理**：支持数据集版本（如`DatasetVersion`表）和模型版本（LLMModel.Version）。

---

### **4. 业务流程**

### **4.1 数据源构建**

```plaintext
1. 下载Stack Exchange数据转储（Posts.xml, Tags.xml）。
2. 筛选与Linux内核相关的帖子：
   - 筛选条件：Tags包含`linux`、`kernel`或`linux-kernel`。
   - 清洗规则：排除已删除（DeletionDate非空）或低质量（Score < 0）的帖子。
3. 导入到数据库表`Post`, `Tag`, `PostTags`。
```

### **4.2 标准问答库构建**

```plaintext
1. 人工构建标准问题：
   - 示例：Q: "Which function implements the mkdir system call in Linux kernel?"
   - 分类为主观题（需解释性回答）或客观题（单选/判断题）。
2. 众包候选答案：
   - 通过平台收集多个候选答案（存储为`StandardAnswer`）。
3. 确定标准答案：
   - 专家审核候选答案，标记关键评分点（如JSON格式的KeyPoints）。
```

### **4.3 LLM评测流程**

```plaintext
1. 输入标准问题到LLM模型，获取生成的回答。
2. 自动化评测：
   - 关键点覆盖率：对比LLM回答与KeyPoints的匹配度（如使用NLP相似度算法）。
   - 错误类型标记：识别事实错误（factual_error）、误导性陈述（misleading）等。
3. 结果存储：写入`Evaluation`表，关联模型、问题和评测时间戳。
```

---

## **5. 技术实现细节**

### **5.1 数据库设计（ERD补充）**

```mermaid
erDiagram

Post ||--o{ Comment : "1:N"
Post ||--|{ PostTags : "M:N"
Tag ||--|{ PostTags : "M:N"
StandardQuestion ||--|{ StandardAnswer : "1:N"
StandardQuestion }o--o{ Evaluation : "被评测"
LLMModel ||--o{ Evaluation : "生成结果"

Post {
    int PostId PK
    int PostTypeId
    int? AcceptedAnswerId
    int? ParentId
    datetime CreationDate
    int Score
    string Body
    int? OwnerUserId
    string Tags
}

Tag {
    int TagId PK
    string TagName
    int Count
}

StandardQuestion {
    int QuestionId PK
    string QuestionText
    string Category
}

Evaluation {
    int EvaluationId PK
    int ModelId FK
    int QuestionId FK
    float CoverageRate
    json ErrorTypes
    datetime Timestamp
}
```

## **5.2 工具与框架**

- **数据库**：PostgreSQL（支持JSON字段、高性能查询）。
- **数据清洗**：Python脚本（Pandas处理XML转CSV，去重、过滤）。
- **评测算法**：基于Spacy的NLP关键点匹配，结合规则引擎标记错误类型。

---

### **6. 潜在问题与解决方案**

| 问题                         | 解决方案                                                                 |
|------------------------------|------------------------------------------------------------------------|
| **用户匿名化**               | 将OwnerUserId=-1标记为“社区用户”，其他用户ID哈希化存储。                     |
| **评测标准不一致**           | 引入人工审核模块，允许专家修正自动化评测结果。                                 |
| **数据版本冲突**             | 使用`DatasetVersion`表记录每次数据更新的版本号和变更说明。                     |

---

此文档覆盖了从需求分析到技术实现的核心细节，您可基于此进一步细化表结构、设计API接口或优化评测算法。

## Request

考虑需求：（可以用高级语言配合SQL查询）

1. 原始问题/问答、标准为题/回答的数量
    1. 问题一对多回答
2. 标准问题<—多人给出多个候选回答
3. 标准问题<—标准答案录入
    1. 查询尚未有录入的问题数量和列表
4. 标准问题分类  
    1. 可根据分类查询、统计
5. 标准问题—一个或多个标签
    1. 根据标签查询、统计
6. 数据集的发布发版本可以有多个
7. 数据集多次测评
    1. 测评结果可分别查询

## Basic Design

1. raw_questions  
2. raw_answers  
3. std_questions  
4. std_answers  
5. candidate_answers  
6. evaluation_version_tags
7. evaluation_results  

### Detail Design

1. raw_questions  
    none type  
    have one accepted_answer(in raw_answers)  
2. raw_answers  
    related to raw_questions  
3. std_questions  
    come from raw_questions  
    one raw_questions may break into multiple std_questions  

    consider two types:  
    1. subjective  
    2. objective  
4. std_answers  
    come from candidate_answers  
    consider two types:  
    1. subjective
    2. objective

    related to corresponded std_questions  
5. candidate_answers  
    not come from std_questions  
    may be selected as std_answers  
6. evaluation_version_tags  
    contains tags for evaluation version  
    and other information:  
    model info of LLM  
7. evaluation_results  
    contains evaluation results  
    related to std_questions and std_answers  
    and evaluation_version_tags  

how to treat std_questions and std_answers?  
std_questions have two types:  

1. subjective  
2. objective  

it is preferred to treat this two types as two tables  
and they extends the basic shared table: std_questions  
so we can better handle the answer points  

and as for std_answers, we can also treat them as two tables  
and related to corresponded std_questions type  

for subjective there are multiple std_answers  
for objective, there are only one std_answer  

std_answers selected from candidate_answers and must be related to an entity in std_questions  
std_questions may temporarily don't have related std_answers  
and this should be able to be searched  
(performed as a statement)  
so it's able to know how much and what std_questions are not related to std_answers  
