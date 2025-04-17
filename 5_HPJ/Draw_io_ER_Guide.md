# 使用Draw.io创建HPJ系统ER模型的详细指南

## 准备工作

1. 访问 [draw.io](https://app.diagrams.net/)
2. 点击"创建新图表"或"Create New Diagram"
3. 选择"空白图表"(Blank Diagram)
4. 保存为适当的文件名，如"HPJ_Traditional_ER_Model"

## 第一步：配置绘图环境

1. 在左侧面板找到搜索框，输入"ER"
2. 这会过滤出实体关系图相关的形状
3. 您将看到矩形(实体)、菱形(关系)、椭圆(属性)等形状

## 第二步：创建基本实体

首先绘制主要实体，建议的布局方式：

- 上方：原始数据实体(RAW_QUESTIONS, RAW_ANSWERS)
- 中间：标准问答实体(STD_QUESTIONS及其特化、CANDIDATE_ANSWERS、STD_ANSWERS及其特化)
- 下方：评测实体(DATASET_VERSION, LLM_MODEL, EVALUATION_VERSION, EVALUATION_RESULTS)

### 绘制实体步骤

1. 从左侧面板拖拽矩形到画布
2. 双击矩形，输入实体名称(如"RAW_QUESTIONS")
3. 右键矩形，选择"Style"，可更改填充颜色区分不同类别实体
   - 原始数据实体：浅蓝色
   - 标准问答实体：浅绿色
   - 评测实体：浅黄色

## 第三步：添加实体属性

为每个实体添加属性：

1. 从左侧面板拖拽椭圆到画布，放置在实体矩形周围
2. 双击椭圆，输入属性名(如"question_id")
3. 主键属性需要下划线：在属性文本前后添加`<u>`和`</u>`，如`<u>question_id</u>`
4. 使用连接线工具(左侧工具栏中)连接实体和其属性

## 第四步：创建关系集

1. 从左侧面板拖拽菱形到画布，放在相关实体之间
2. 双击菱形，输入关系名称(如"HAS_ANSWER")
3. 使用连接线工具连接菱形和相关实体
4. 在连接线上添加基数约束(1, N, M)：
   - 双击连接线
   - 输入"1"表示一，"N"或"M"表示多
5. 全部参与关系使用双线表示：
   - 选中连接线
   - 右键选择"Style"
   - 将"Line"改为"double"

## 第五步：表示特化关系

特化关系(STD_QUESTIONS到其子类)的表示方法：

1. 绘制两个实体矩形(父实体和子实体)
2. 从工具栏选择空心三角形(表示"是一个"关系)
3. 将三角形顶点连接到父实体，底边连接到子实体
4. 标记为不相交特化：在三角形旁添加文本框，写上"d"(disjoint)

## 第六步：完善与优化

1. **调整布局**：拖动实体和关系使图表更整洁
2. **添加注释**：使用文本框添加必要的说明
3. **使用颜色编码**：统一相似实体的颜色
4. **对齐元素**：选中多个元素后，使用右键菜单中的对齐选项

## 绘制具体内容指南

### 原始数据部分

1. 绘制RAW_QUESTIONS和RAW_ANSWERS两个实体矩形
2. 在它们之间添加HAS_ANSWER菱形关系
3. 在RAW_QUESTIONS一侧标记"1"，RAW_ANSWERS一侧标记"N"
4. RAW_ANSWERS到关系的线使用双线(表示全部参与)
5. 为每个实体添加全部属性椭圆

### 标准问答部分

1. 绘制STD_QUESTIONS实体矩形
2. 使用特化符号(空心三角形)连接到STD_QUESTION_SUBJECTIVE和STD_QUESTION_OBJECTIVE
3. 添加CANDIDATE_ANSWERS和STD_ANSWERS实体
4. 绘制STD_ANSWERS的特化到STD_ANSWER_SUBJECTIVE和STD_ANSWER_OBJECTIVE
5. 添加以下关系：
   - DERIVES_FROM (STD_QUESTIONS到RAW_QUESTIONS)
   - HAS_TAG (STD_QUESTIONS到TAG)
   - PROPOSES_FOR (CANDIDATE_ANSWERS到STD_QUESTIONS)
   - SELECTED_AS (CANDIDATE_ANSWERS到STD_ANSWERS)
   - ANSWERS (STD_ANSWERS到STD_QUESTIONS)

### 评测部分

1. 绘制评测相关的四个实体
2. 添加以下关系：
   - IS_REFERENCED_BY (DATASET_VERSION到EVALUATION_VERSION)
   - USES_MODEL (EVALUATION_VERSION到LLM_MODEL)
   - IS_EVALUATED_IN (STD_QUESTIONS到EVALUATION_RESULTS)
   - IS_COMPARED_WITH (STD_ANSWERS到EVALUATION_RESULTS)
   - CONTAINS_RESULT (EVALUATION_VERSION到EVALUATION_RESULTS)

## 保存和导出

1. 点击菜单"文件" > "保存"保存您的工作
2. 若要导出：点击菜单"文件" > "导出为" > 选择所需格式
   - PNG: 适合一般查看
   - SVG: 可缩放矢量图，保持高质量
   - PDF: 适合打印和文档包含

## 示例布局参考

```txt
┌───────────────────────────┐
│     原始数据实体区域       │
│ RAW_QUESTIONS RAW_ANSWERS │
└───────────┬───────────────┘
            ↓
┌───────────────────────────────────────────────┐
│              标准问答实体区域                  │
│ STD_QUESTIONS──→STD_QUESTION_SUBJECTIVE       │
│       │        →STD_QUESTION_OBJECTIVE        │
│       ↓                                       │
│ CANDIDATE_ANSWERS→STD_ANSWERS──→STD_ANSWER_XXX│
└───────────────────────┬───────────────────────┘
                        ↓
┌───────────────────────────────────────────────┐
│               评测实体区域                     │
│ DATASET_VERSION  LLM_MODEL                    │
│        ↓              ↓                       │
│    EVALUATION_VERSION→EVALUATION_RESULTS      │
└───────────────────────────────────────────────┘
```

遵循此指南，您应该能够在draw.io中创建一个清晰、专业的传统ER模型图。记住，好的ER图不仅包含所有必要的信息，还应该视觉上清晰且易于理解。
