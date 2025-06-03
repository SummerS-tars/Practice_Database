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