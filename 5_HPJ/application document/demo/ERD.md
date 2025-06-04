# ERD of HPJ 1

```mermaid
erDiagram
    %% 核心实体（Stack Exchange数据模型）
    Post {
        int PostId PK "帖子ID"
        int PostTypeId "1=问题, 2=回答"
        int AcceptedAnswerId FK "已采纳答案ID"
        int ParentId FK "父帖子ID"
        datetime CreationDate "创建时间"
        int Score "得分"
        string Body "HTML内容"
        int OwnerUserId FK "用户ID"
        string Tags "标签列表"
    }

    Tag {
        int TagId PK "标签ID"
        string TagName "标签名称"
        int Count "使用次数"
    }

    Comment {
        int CommentId PK "评论ID"
        int PostId FK "关联帖子"
        string Text "评论内容"
        datetime CreationDate "创建时间"
    }

    %% 评测相关实体
    StandardQuestion {
        int QuestionId PK "问题ID"
        string QuestionText "问题内容"
        string Category "问题类别"
    }

    StandardAnswer {
        int AnswerId PK "答案ID"
        int QuestionId FK "关联问题"
        string AnswerText "标准答案"
        json KeyPoints "关键评分点"
    }

    LLMModel {
        int ModelId PK "模型ID"
        string ModelName "模型名称"
        string Version "版本号"
    }

    Evaluation {
        int EvaluationId PK "评测ID"
        int ModelId FK "模型ID"
        int QuestionId FK "问题ID" 
        float CoverageRate "覆盖率"
        json ErrorTypes "错误类型"
    }

    %% 中间表
    PostTags {
        int PostId FK "帖子ID"
        int TagId FK "标签ID"
    }

    %% 关系定义
    Post ||--o{ Comment : "包含"
    Post ||--|| PostTags : "拥有"
    Tag ||--|| PostTags : "属于"
    StandardQuestion ||--o{ StandardAnswer : "对应"
    StandardQuestion ||--o{ Evaluation : "被评测"
    LLMModel ||--o{ Evaluation : "产生"
    Post }o--o{ Post : "问题-回答"
```

架构说明：
核心实体（来自Stack Exchange）：

Post：区分问题（PostTypeId=1）和回答（PostTypeId=2），通过ParentId关联答案到问题。

Tag：通过中间表PostTags实现多对多关联。

Comment：一对多关联到Post。

评测扩展实体：

StandardQuestion：存储构建的标准问题（如Linux内核相关问题）。

StandardAnswer：每个问题对应一个或多个标准答案（支持众包候选答案）。

LLMModel：记录不同LLM的版本信息。

Evaluation：存储评测结果，包含覆盖率、错误类型等结构化数据。

关键设计点：

使用JSON字段存储动态内容（如评分要点、错误类型）。

通过中间表PostTags实现灵活的标签关联。

标准化评测流程：LLM回答通过Evaluation表与标准答案对比。

可扩展性建议：
添加User表以记录用户贡献（需处理OwnerUserId=-1的特殊情况）。

增加DatasetVersion表管理不同版本的标准问答集。

在Evaluation中添加时间戳字段以支持历史结果追踪。

此设计平衡了原始数据结构和LLM评测需求，可直接用于构建技术问答评测数据库。