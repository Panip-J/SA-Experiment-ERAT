# 实验课程任务3：实验报告统计分析工具ERAT

**课程名称：** 软件体系结构设计  
**作业类型：** 小组作业  
**小组成员：** 黄智昊、刘增垚  
**分工：**  

- 黄智昊：系统实现、测试 
- 刘增垚：需求分析、设计文档

---

## 一、作业背景  
​        在软件工程专业相关课程教学中，学生需多次完成并提交实验报告（如10次实验），教师需手动统计缺交情况，效率低下且易出错。现需设计一个应用工具 ERAT，实现按照班级和课程的方式，统计实验报告缺交情况统计功能。本实验任务建议使用Attribute-Driven Development ADD软件设计方法，实现该工具的设计，核心功能如下：

1. 学生名单管理
     支持从Excel文件导入学生名单（学号、姓名，年级、专业）。
     支持手动添加/删除学生信息。

2. 实验报告目录
     假定实验报告按照每个课程的每个班的每个实验按照一定格式存放学生的实验报告。

3. 统计与分析功能
     学生维度统计：生成每位学生缺交的实验编号列表。
     实验维度统计：生成每个实验的缺交学生名单（如“实验3：缺交学生李四、王五”）。
     可视化分析：绘制折线图，横轴为实验编号，纵轴为提交率（提交人数/总人数）。

4. 数据导出
     支持将统计结果导出为Excel或CSV文件。

---

## 二、作业任务提交内容  

### 1. 需求分析

<img src="media\1.png" alt="1" style="zoom:80%;" />

**非功能需求与约束：**

- 性能：统计查询响应时间<3秒

- 可靠性：支持5000+学生数据量

- 可维护性：分层架构解耦

- 兼容性：支持Excel/CSV格式

### 2. 设计文档
#### （1）体系架构设计

<img src="media\2.png" alt="2" style="zoom:80%;" />

#### （2）模块图  

<img src="media\3.png" alt="3" style="zoom:80%;" />

#### （3）类图

<img src="media\4.png" alt="4" style="zoom:80%;" />

#### （4）时序图

<img src="media\5.png" alt="5" style="zoom:80%;" />

#### （5）工厂模式

<img src="media\6.png" alt="6" style="zoom:80%;" />

#### （6）组合模式（统计聚合）

<img src="media\7.png" alt="7" style="zoom:80%;" />

#### （7）单例模式（数据库工具）

<img src="media\8.png" alt="8" style="zoom:80%;" />

#### （8）数据库设计

##### 表结构设计

为满足ERAT工具的核心功能需求，数据库设计采用以下表结构，确保数据完整性和查询效率：

1. **学生表（Students）**

   | 字段名    | 类型        | 约束                          | 说明             |
   | :-------- | :---------- | :---------------------------- | :--------------- |
   | StudentID | VARCHAR(20) | PRIMARY KEY                   | 学号（唯一标识） |
   | Name      | VARCHAR(50) | NOT NULL                      | 学生姓名         |
   | Grade     | VARCHAR(10) | NOT NULL                      | 年级             |
   | Major     | VARCHAR(50) | NOT NULL                      | 专业             |
   | ClassID   | VARCHAR(20) | FOREIGN KEY (Classes.ClassID) | 所属班级ID       |

2. **课程表（Courses）**

   | 字段名     | 类型         | 约束        | 说明               |
   | :--------- | :----------- | :---------- | :----------------- |
   | CourseID   | VARCHAR(20)  | PRIMARY KEY | 课程ID（唯一标识） |
   | CourseName | VARCHAR(100) | NOT NULL    | 课程名称           |

3. **班级表（Classes）**

   | 字段名    | 类型        | 约束                           | 说明               |
   | :-------- | :---------- | :----------------------------- | :----------------- |
   | ClassID   | VARCHAR(20) | PRIMARY KEY                    | 班级ID（唯一标识） |
   | CourseID  | VARCHAR(20) | FOREIGN KEY (Courses.CourseID) | 所属课程ID         |
   | ClassName | VARCHAR(50) | NOT NULL                       | 班级名称           |

4. **实验表（Experiments）**

   | 字段名           | 类型         | 约束                           | 说明               |
   | :--------------- | :----------- | :----------------------------- | :----------------- |
   | ExperimentID     | VARCHAR(20)  | PRIMARY KEY                    | 实验ID（唯一标识） |
   | CourseID         | VARCHAR(20)  | FOREIGN KEY (Courses.CourseID) | 所属课程ID         |
   | ExperimentNumber | INT          | NOT NULL                       | 实验编号           |
   | ExperimentName   | VARCHAR(100) | NOT NULL                       | 实验名称           |

5. **提交记录表（Submissions）**

   | 字段名         | 类型        | 约束                                   | 说明                    |
   | :------------- | :---------- | :------------------------------------- | :---------------------- |
   | SubmissionID   | INT         | PRIMARY KEY, AUTO_INCREMENT            | 提交记录ID              |
   | StudentID      | VARCHAR(20) | FOREIGN KEY (Students.StudentID)       | 学生学号                |
   | ExperimentID   | VARCHAR(20) | FOREIGN KEY (Experiments.ExperimentID) | 实验ID                  |
   | IsSubmitted    | BOOLEAN     | NOT NULL                               | 是否提交（缺交为False） |
   | SubmissionTime | DATETIME    |                                        | 提交时间（可选）        |

##### 外键关系

- **Students.ClassID** → **Classes.ClassID**
- **Classes.CourseID** → **Courses.CourseID**
- **Experiments.CourseID** → **Courses.CourseID**
- **Submissions.StudentID** → **Students.StudentID**
- **Submissions.ExperimentID** → **Experiments.ExperimentID**

##### 索引设计

- **Students.StudentID**：主键索引（默认）。
- **Experiments.ExperimentNumber**：唯一索引，确保实验编号不重复。
- **Submissions(StudentID, ExperimentID)**：联合索引，加速缺交记录查询。

##### 数据完整性说明

1. 外键约束确保学生、班级、课程、实验的关联关系合法。
2. **IsSubmitted**字段默认为`False`，未提交时自动记录缺交。
3. **ExperimentNumber**在课程内唯一，避免实验编号冲突。

### 3. 系统实现

#### （1）数据模型类（Model）

##### `Student.java`

```java
package com.erat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private String studentId;
    private String name;
    private String grade;
    private String major;
    private String classId;
}
```

##### `Experiment.java`

```java
package com.erat.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Experiment {
    private String experimentId;
    private String courseId;
    private int experimentNumber;
    private String experimentName;
}
```

##### `Submission.java`

```java
package com.erat.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Submission {
    private int submissionId;
    private String studentId;
    private String experimentId;
    private boolean isSubmitted;
    private LocalDateTime submissionTime;
}
```

#### （2） DAO层实现

##### `StudentDAOImpl.java`

```java
package com.erat.dao.impl;

import com.erat.model.Student;
import com.erat.utils.DBUtil;
import java.sql.\*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl {
    public void addStudent(Student student) throws SQLException {
        String sql \= "INSERT INTO Students VALUES (?, ?, ?, ?, ?)";
        try (Connection conn \= DBUtil.getConnection();
             PreparedStatement stmt \= conn.prepareStatement(sql)) {
            stmt.setString(1, student.getStudentId());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getGrade());
            stmt.setString(4, student.getMajor());
            stmt.setString(5, student.getClassId());
            stmt.executeUpdate();
        }
    }

    public List<Student\> getAllStudents() throws SQLException {
        List<Student\> students \= new ArrayList<\>();
        String sql \= "SELECT \* FROM Students";
        try (Connection conn \= DBUtil.getConnection();
             Statement stmt \= conn.createStatement();
             ResultSet rs \= stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student student \= new Student(
                    rs.getString("StudentID"),
                    rs.getString("Name"),
                    rs.getString("Grade"),
                    rs.getString("Major"),
                    rs.getString("ClassID")
                );
                students.add(student);
            }
        }
        return students;
    }

}
```

#### （3）业务逻辑层（Service）

##### `ReportServiceImpl.java`

```java
package com.erat.service.impl;

import com.erat.dao.StudentDAO;
import com.erat.dao.SubmissionDAO;
import com.erat.model.Student;
import com.erat.service.ReportService;
import java.util.\*;

public class ReportServiceImpl implements ReportService {
    private final StudentDAO studentDAO \= new StudentDAOImpl();
    private final SubmissionDAO submissionDAO \= new SubmissionDAOImpl();

    @Override
    public Map<String, Integer\> getStudentMissingStats(String studentId) {
        return submissionDAO.getMissingExperimentsByStudent(studentId);
    }
    
    @Override
    public void importStudentsFromFile(String filePath, String fileType) {
        // 工厂模式调用
        FileParser parser \= FileParserFactory.getParser(fileType);
        List<Student\> students \= parser.parseStudentFile(filePath);
        students.forEach(student \-> {
            try {
                studentDAO.addStudent(student);
            } catch (SQLException e) {
                LoggerUtil.logError("导入学生失败: " + e.getMessage());
            }
        });
    }

}
```

#### （4） 工厂模式实现

##### `FileParserFactory.java`

```java
package com.erat.factory;

import com.erat.service.FileParser;
import com.erat.service.impl.CsvParser;
import com.erat.service.impl.ExcelParser;

public class FileParserFactory {
    public static FileParser getParser(String fileType) {
        if ("excel".equalsIgnoreCase(fileType)) {
            return new ExcelParser();
        } else if ("csv".equalsIgnoreCase(fileType)) {
            return new CsvParser();
        }
        throw new IllegalArgumentException("不支持的文件类型: " + fileType);
    }
}
```

#### （5） 工具类与配置

##### `DBUtil.java`

```java
package com.erat.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection \== null || connection.isClosed()) {
            connection \= DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/erat\_db",
                "root",
                "password"
            );
        }
        return connection;
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
```

### 4. 测试

用Postman创建名为`ERAT_Test`的集合，包含以下请求：

#### （1） 学生维度缺交统计测试

- **请求名称**: `Get Student Missing Report`

- **请求类型**: `GET`

- **URL**: `http://localhost:8080/api/reports/students/2023001/missing`

- **测试脚本**（Tests标签）：

```javascript
  // 验证状态码
  pm.test("Status code is 200", () => {
      pm.response.to.have.status(200);
  });
  
  // 验证响应体格式
  pm.test("Response includes missing count and list", () => {
      const jsonData = pm.response.json();
      pm.expect(jsonData).to.have.property("studentId", "2023001");
      pm.expect(jsonData).to.have.property("missingCount", 1);
      pm.expect(jsonData.missingExperiments).to.include("实验1");
  });
```

#### （2） 实验维度缺交统计测试

- **请求名称**: `Get Experiment Missing Report`

- **请求类型**: `GET`

- **URL**: `http://localhost:8080/api/reports/experiments/EXP001/missing`

- **测试脚本**：

```javascript
  pm.test("Status code is 200", () => {
      pm.response.to.have.status(200);
  });
  
  pm.test("Response includes missing students", () => {
      const jsonData = pm.response.json();
      pm.expect(jsonData).to.have.property("experimentName", "实验1");
      pm.expect(jsonData.missingStudents).to.include("张三", "李四");
  });
```

#### （3）数据导出功能测试

- **请求名称**: `Export Report to Excel`

- **请求类型**: `GET`

- **URL**: `http://localhost:8080/api/reports/export?type=excel`

- **测试脚本**：

```javascript
  pm.test("Status code is 200", () => {
      pm.response.to.have.status(200);
  });
  
  pm.test("Response is Excel file", () => {
      pm.expect(pm.response.headers.get("Content-Type")).to.include("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
  });
```

#### （4） 异常测试：无效学生ID

- **请求名称**: `Invalid Student ID`

- **请求类型**: `GET`

- **URL**: `http://localhost:8080/api/reports/students/999999/missing`

- **测试脚本**：

```javascript
  pm.test("Status code is 404", () => {
      pm.response.to.have.status(404);
  });
  
  pm.test("Error message is correct", () => {
      const jsonData = pm.response.json();
      pm.expect(jsonData).to.have.property("message", "学生不存在");
  });
```
