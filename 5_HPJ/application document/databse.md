# 数据库最终设计

## E-R模型描述

下文中：ES表示Entity Set，RS表示Relation Set。  
通过`**加粗**`标识主键。  
弱实体集通过weak ES并注明相关的主实体集。  
AP表示all participant，PP表示partial participant。  
ES1 many与ES2 one表示ES1到ES2为多对一关系，以此类推。

原始问答：  

ES

1. raw_questions  
    1. **raw_question_id**  
    2. title
    3. content
    4. tags -- 原始标签
    5. post_id -- 平台发布id
    6. source_platform -- 来源平台名称
    7. score
2. raw_answers
    1. **raw_answer_id**
    2. content
    3. parent_id -- 所属原始问题id
    4. post_id -- 平台发布id
    5. source_platform -- 来源平台名称
    6. score

RS

1. raw_q_and_a between raw_questions and raw_answers  
    1. raw_questions MP many  
    2. raw_answers AP one  
    3. raw_answers(parent_id) referencing raw_questions(raw_question_id)  

标准问题相关：  

ES

1. std_questions
    1. **std_question_id**
    2. type  
        OBJECTIVE or SUBJECTIVE  
    3. content  
    4. status  
        WAITING_ANSWERS or ANSWERED  
    5. created_at
2. std_answers
    1. **std_answers_id**  
    2. type  
        OBJECTIVE or SUBJECTIVE  
    3. score  
    4. status  
        ACCEPTED or OMITTED  
    5. created_at  
3. std_answers_obj  
    weak ES of std_answers  
    1. obj_answer  
        选项或者正误  
        选项为A、B、C、D、E，正误为TRUE或FALSE  
4. std_answers_sub  
    weak ES of std_answers  
    1. sub_answer  
        主观题答案内容（文本形式）  
5. data_set_version  
    1. **version** -- 数据集版本号
6. data_set_tags
    1. **tag** -- 标准问题标签  

RS

1. version_is between std_questions and version  
    一个标准问题可以存在于多个版本  
    1. std_questions AP many  
    2. version PP many  

2. tag_is between std_questions and tags
    一个标准问题可以有多个标签  
    1. std_questions PP many  
    2. tags PP many  

3. std_q_and_a between std_questions and std_answers  
    1. std_questions PP one  
    2. std_answers AP many  

众包候选回答相关：

ES

1. candidate_answers  
    1. **candidate_answer_id**
    2. type  
        OBJECTIVE or SUBJECTIVE  
    3. status  
        PENDING or ACCEPTED or REJECTED  
2. candidate_answers_obj  
    weak ES of candidate_answers  
    1. obj_answer  
        选项或者正误  
        选项为A、B、C、D、E，正误为TRUE或FALSE  
3. candidate_answers_sub  
    weak ES of candidate_answers  
    1. sub_answer  
        主观题答案内容（文本形式）

RS

1. candidate_a_of_std_q between std_questions and candidate_answers  
    1. std_questions PP one  
    2. candidate_answers AP many  

2. selected between candidate_answers and std_answers  
    1. candidate_answers PP one
    2. std_answers AP one  
    3. std_answers(candidate_answer_id) referencing candidate_answers(candidate_answer_id)  

评估部分：

ES

1. evaluation_tags
    1. **evaluation_tag_id**
    2. data_set_version
    3. evaluation_time -- 对该数据集版本的测试的次数（指第几次）
    4. model -- 用于测试回答问题的模型  
2. evaluation_results  
    weak ES of evaluation_tags and std_questions  
    1. content  
    2. status  
        PENDING or ANALYZED or OMITTED  
    3. result_id  
        将会被关联测试结果分析  
        是候选键  
3. evaluation_analysis  
    weak ES of analysis_tags  
    1. **result_id**  
    2. score -- 回答测试结果得分（应该取0-10分中的一个整数）
    3. created_at
4. analysis_tags  
    weak ES of evaluation_tags  
    1. **analysis_tag_id**  
    2. analysis_time -- 对该数据集版本的测试结果的分析次数（指第几次）
    3. model -- 用于分析测试结果的模型  

RS

1. test_results between evaluation_results and std_questions  
    1. evaluation_results AP many  
    2. std_questions PP one  

2. results_has_tags between evaluation_results and evaluation_tags  
    1. evaluation_results AP many
    2. evaluation_tags PP one  

3. analysis_of_results between evaluation_analysis and evaluation_results  
    1. evaluation_results PP one  
    2. evaluation_analysis AP many  

4. analysis_has_tags between evaluation_analysis and analysis_tags  
    1. evaluation_analysis AP many  
    2. analysis_tags PP one

## 数据表设计

```sql
-- -----------------------------------------------------
-- 原始问答 (Raw Q&A)
-- -----------------------------------------------------
CREATE TABLE raw_questions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    source_platform VARCHAR(100),
    tags VARCHAR(255), -- 原始标签, 可能是逗号分隔的字符串或JSON
    post_id INT, -- 平台上的帖子ID
    score INT
);

CREATE TABLE raw_answers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    raw_question_id INT NOT NULL, -- 外键，实现 raw_q_and_a 关系 (一个问题多个答案，一个答案属一个问题)
    content TEXT,
    source_platform VARCHAR(100),
    post_id INT, -- 平台上的帖子ID
    score INT,
    FOREIGN KEY (raw_question_id) REFERENCES raw_questions(id) ON DELETE CASCADE
);

-- 如何为raw_answer找到对应的raw_question_id? 答案是通过外部信息(具体来说对StackOverflow的是parent_id)
  
-- -----------------------------------------------------
-- 标准问题相关 (Standard Question Related)
-- -----------------------------------------------------
CREATE TABLE version (
    version VARCHAR(20) PRIMARY KEY -- 数据集版本号
);

CREATE TABLE tags (
    tag VARCHAR(100) PRIMARY KEY -- 标准问题标签
);

CREATE TABLE std_questions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type ENUM('OBJECTIVE', 'SUBJECTIVE') NOT NULL,
    content TEXT NOT NULL,
    status ENUM('WAITING_ANSWERS', 'ANSWERED') DEFAULT 'WAITING_ANSWERS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- M:N Relationship between std_questions and version
CREATE TABLE std_question_versions (
    std_question_id INT NOT NULL,
    version_id VARCHAR(20) NOT NULL,
    PRIMARY KEY (std_question_id, version_id),
    FOREIGN KEY (std_question_id) REFERENCES std_questions(id) ON DELETE CASCADE,
    FOREIGN KEY (version_id) REFERENCES version(version) ON DELETE CASCADE
);

-- M:N Relationship between std_questions and tags
CREATE TABLE std_question_tags (
    std_question_id INT NOT NULL,
    tag_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (std_question_id, tag_name),
    FOREIGN KEY (std_question_id) REFERENCES std_questions(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_name) REFERENCES tags(tag) ON DELETE CASCADE
);

CREATE TABLE std_answers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    std_question_id INT NOT NULL, -- 外键，实现 std_q_and_a 关系 (一个标准问题可以有多个标准答案)
    type ENUM('OBJECTIVE', 'SUBJECTIVE') NOT NULL,
    score INT, -- 评分，整数，范围 0-10
    status ENUM('ACCEPTED', 'OMITTED') DEFAULT 'ACCEPTED', -- 答案状态
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    selected_from_candidate_id INT NULL UNIQUE, -- 允许为空，因为标准答案不一定来自候选，UNIQUE确保一个候选只被选一次
    FOREIGN KEY (std_question_id) REFERENCES std_questions(id) ON DELETE CASCADE,
    -- Foreign key to candidate_answers will be added after candidate_answers table is defined
    CONSTRAINT chk_score CHECK (score >= 0 AND score <= 10) -- 约束score在0到10之间
);

CREATE TABLE std_answers_obj ( -- std_answers 的弱实体
    std_answer_id INT PRIMARY KEY,
    obj_answer ENUM('A', 'B', 'C', 'D', 'E', 'TRUE', 'FALSE') NOT NULL, -- 客观题答案内容，限制为选项或正误判断
    FOREIGN KEY (std_answer_id) REFERENCES std_answers(id) ON DELETE CASCADE
);

CREATE TABLE std_answers_sub ( -- std_answers 的弱实体
    std_answer_id INT PRIMARY KEY,
    sub_answer TEXT NOT NULL, -- 主观题答案内容（文本形式）
    FOREIGN KEY (std_answer_id) REFERENCES std_answers(id) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- 众包候选回答相关 (Crowdsourced Candidate Answers)
-- -----------------------------------------------------
CREATE TABLE candidate_answers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    std_question_id INT NOT NULL, -- 外键，实现 candidate_a_of_std_q 关系
    type ENUM('OBJECTIVE', 'SUBJECTIVE') NOT NULL,
    status ENUM('PENDING', 'ACCEPTED', 'REJECTED') DEFAULT 'PENDING',
    -- submitted_by_user_id INT, -- 考虑未来扩展
    -- submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 考虑未来扩展
    FOREIGN KEY (std_question_id) REFERENCES std_questions(id) ON DELETE CASCADE
);

-- Add FK for std_answers.selected_from_candidate_id now that candidate_answers is defined
ALTER TABLE std_answers
ADD CONSTRAINT fk_std_answers_candidate
FOREIGN KEY (selected_from_candidate_id) REFERENCES candidate_answers(id) ON DELETE SET NULL;

CREATE TABLE candidate_answers_obj ( -- candidate_answers 的弱实体
    candidate_answer_id INT PRIMARY KEY,
    obj_answer ENUM('A', 'B', 'C', 'D', 'E', 'TRUE', 'FALSE') NOT NULL, -- 客观题答案内容，限制为选项或正误判断
    FOREIGN KEY (candidate_answer_id) REFERENCES candidate_answers(id) ON DELETE CASCADE
);

CREATE TABLE candidate_answers_sub ( -- candidate_answers 的弱实体
    candidate_answer_id INT PRIMARY KEY,
    sub_answer TEXT NOT NULL, -- 主观题答案内容（文本形式）
    FOREIGN KEY (candidate_answer_id) REFERENCES candidate_answers(id) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- 评估部分 (Evaluation Part)
-- -----------------------------------------------------
CREATE TABLE evaluation_tags ( -- 代表一次LLM评估的配置或批次
    tag_id INT PRIMARY KEY AUTO_INCREMENT,
    data_set_version VARCHAR(50),
    evaluation_time INT, -- 指第几次测试，根据E-R描述变化
    model VARCHAR(100) NOT NULL, -- 用于测试回答问题的模型
    FOREIGN KEY (data_set_version) REFERENCES version(version) ON DELETE SET NULL
);

CREATE TABLE evaluation_results ( -- 存储LLM对特定问题的具体回答
    id INT PRIMARY KEY AUTO_INCREMENT, -- Renamed from result_id in E-R to be consistent
    evaluation_tag_id INT NOT NULL, -- 外键，关联到评估批次
    std_question_id INT NOT NULL,   -- 外键，关联到标准问题
    content TEXT,                   -- LLM生成的答案内容
    status ENUM('PENDING', 'ANALYZED', 'OMITTED') DEFAULT 'PENDING',
    has_analysis BOOLEAN DEFAULT FALSE, -- 标识是否有对应的分析结果
    FOREIGN KEY (evaluation_tag_id) REFERENCES evaluation_tags(tag_id) ON DELETE CASCADE,
    FOREIGN KEY (std_question_id) REFERENCES std_questions(id) ON DELETE CASCADE
);

CREATE TABLE analysis_tags ( -- 代表一次评估结果分析的配置或批次
    analysis_tag_id INT PRIMARY KEY AUTO_INCREMENT,
    evaluation_tag_id INT NOT NULL, -- 关联到原始的评估批次
    analysis_time INT, -- 对该数据集版本的测试结果的分析次数（指第几次）
    model VARCHAR(100) NOT NULL, -- 用于分析测试结果的模型
    FOREIGN KEY (evaluation_tag_id) REFERENCES evaluation_tags(tag_id) ON DELETE CASCADE
);

CREATE TABLE evaluation_analysis ( -- 存储对 evaluation_results 的分析
    id INT PRIMARY KEY AUTO_INCREMENT, -- Using own PK for clarity
    evaluation_result_id INT NOT NULL, -- 外键，关联到具体的LLM回答
    analysis_tag_id INT NOT NULL,      -- 外键，关联到分析批次
    score INT, -- 回答测试结果得分（0-10分中的一个整数）
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (evaluation_result_id) REFERENCES evaluation_results(id) ON DELETE CASCADE,
    FOREIGN KEY (analysis_tag_id) REFERENCES analysis_tags(analysis_tag_id) ON DELETE CASCADE,
    CONSTRAINT chk_analysis_score CHECK (score >= 0 AND score <= 10) -- 约束score在0到10之间
);
```
