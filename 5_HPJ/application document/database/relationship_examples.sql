-- Example queries demonstrating the std_questions to raw_questions relationship
-- This shows the many-to-one relationship where std_questions is total participant

-- 1. Insert sample raw questions first
INSERT INTO raw_questions (title, content, source_platform, tags, post_id, score) VALUES
('How to optimize MySQL queries?', 'I have slow queries in my application...', 'stackoverflow', 'mysql,performance', 12345, 15),
('Python list comprehension best practices', 'What are the best practices for list comprehensions?', 'stackoverflow', 'python,lists', 12346, 8),
('JavaScript async/await patterns', 'How to properly handle async operations?', 'stackoverflow', 'javascript,async', 12347, 22);

-- 2. Insert standard questions derived from raw questions
-- Multiple std_questions can reference the same raw_question (many-to-one)
INSERT INTO std_questions (original_raw_question_id, type, content) VALUES
(1, 'OBJECTIVE', 'Which of the following is the most effective way to optimize MySQL query performance? A) Use indexes B) Avoid SELECT * C) Use LIMIT D) All of the above'),
(1, 'SUBJECTIVE', 'Explain three key strategies for optimizing MySQL query performance and provide examples for each.'),
(2, 'OBJECTIVE', 'What is the correct syntax for a Python list comprehension that filters even numbers? A) [x for x in range(10) if x % 2 == 0] B) [x if x % 2 == 0 for x in range(10)] C) [for x in range(10): if x % 2 == 0] D) None of the above'),
(3, 'SUBJECTIVE', 'Compare and contrast Promise-based approach vs async/await in JavaScript for handling asynchronous operations.');

-- 3. Example queries to demonstrate the relationship

-- Query 1: Find all standard questions derived from a specific raw question
SELECT 
    rq.title AS raw_title,
    sq.id AS std_question_id,
    sq.type,
    sq.content AS std_content
FROM raw_questions rq
JOIN std_questions sq ON rq.id = sq.original_raw_question_id
WHERE rq.id = 1;

-- Query 2: Count how many standard questions were created from each raw question
SELECT 
    rq.id,
    rq.title,
    COUNT(sq.id) AS std_questions_count
FROM raw_questions rq
LEFT JOIN std_questions sq ON rq.id = sq.original_raw_question_id
GROUP BY rq.id, rq.title
ORDER BY std_questions_count DESC;

-- Query 3: Find raw questions that haven't been converted to standard questions yet
-- (raw_questions as partial participant - not all need std_questions)
SELECT 
    rq.id,
    rq.title,
    rq.content
FROM raw_questions rq
LEFT JOIN std_questions sq ON rq.id = sq.original_raw_question_id
WHERE sq.id IS NULL;

-- Query 4: Get complete information about a standard question and its source
SELECT 
    sq.id AS std_question_id,
    sq.type AS question_type,
    sq.content AS std_content,
    sq.status,
    rq.title AS original_title,
    rq.source_platform,
    rq.tags AS original_tags,
    rq.score AS original_score
FROM std_questions sq
JOIN raw_questions rq ON sq.original_raw_question_id = rq.id
WHERE sq.id = 1;

-- Query 5: Statistics on conversion from raw to standard questions
SELECT 
    COUNT(DISTINCT rq.id) as total_raw_questions,
    COUNT(DISTINCT sq.original_raw_question_id) as converted_raw_questions,
    COUNT(sq.id) as total_std_questions,
    ROUND(COUNT(sq.id) / COUNT(DISTINCT rq.id), 2) as avg_std_per_raw
FROM raw_questions rq
LEFT JOIN std_questions sq ON rq.id = sq.original_raw_question_id; 