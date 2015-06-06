# Annotation #

  * `@Join`, `@Fields` 추가
  * `@Column` 에 references 속성 추가

# 사용법 #

  1. `@Table` annotation을 갖는 클래스들로 복합 클래스를 생성하고 `@Join` annotation 부여
  1. `@Table` annotation 클래스에 `@Fields` annotation 이용하여 SELECT 대상 필드 지정
  1. foreign key에 해당되는 필드의 `@Column` annotation에 references 속성 추가

# @Join Annotation #

`@Join(<join hint>)`와 같이 쓴다. join hint 가 없으면, `@Join` 클래스에 선언된 순서대로 INNER JOIN 구문 생성한다.

```
// Group 과 User 가 M:N 관계일 때
@Table("GROUP")
class Group {
    @Column(name="group_key",primaryKey=true)
    Long groupKey;
    @Column("group_name")
    String groupName;
}

@Table("USER")
class User {
    @Column(name="user_key", primaryKey=true)
    Long userKey;
    @Column("user_name")
    String useName;
}

@Table("GROUP_USER")
class GroupUser {
    @Column(name="group_key", references="Group.groupKey")
    Long groupKey;
    @Column(name="user_key", references="User.userKey")
    Long userKey;
}

@Join
class GroupAndUser {
    @Fields("groupKey,groupName")
    Group group;
    GroupUser groupUser;
    @Fields("*")
    User user;
}

Group group = new Group();
group.setGroupKey(1);
GroupAndUser gu = new GroupAndUser();
gu.setGroup(group);

List<GroupAndUser> list = entityManager.list(gu);
for (GroupAndUser gau : list) {
    System.out.println(gau.getUser().getUserName();
}
```

다음과 같은 유형의 SELECT문 생성

```
SELECT A.group_key group_groupKey, A.group_name group_groupName,
    C.user_key user_userKey, C.user_name user_userName
FROM GROUP A INNER JOIN GROUP_USER B ON A.group_key = B.group_key
    INNER JOIN USER C ON B.user_key = C.user_key
WHERE A.group_key = 1
```

만약, 이전에 선언된 클래스를 참조하는 필드가 없으면 InvalidJoinException이 발생한다.

```
@Join
class GroupAndUser {
    @Fields("groupKey,groupName")
    Group group;
    @Fields("*")
    User user;
    GroupUser groupUser;
}   // User에서 Group을, 혹은 Group에서 User를 references하는 필드가 없기 때문에 InvalidJoinException 발생
```

http://www.mybatis.org/core/sqlmap-xml.html#Result_Maps

```
@Join("blog LEFT JOIN author ON blog.authorId = author.id\
LEFT JOIN post ON blog.id = post.blogId\
LEFT JOIN comment ON post.id = comment.PostId
LEFT JOIN postTag ON postTag.postId = post.id
LEFT JOIN tag ON postTag.tagId = tag.id")

class BlogResult {
    @Fields("id,title,authorId")
    Blog blog;
    @Fields("id,username,passwod,email,bio,favouriteSection")
    Author author;
    @Fields("id,blogId,authorId,createdOn,section,subject,draft,body")
    Post post;
    @Fields("id,postId,name,comment")
    Comment comment;
    PostTag postTag;
    @Fields("id,name")
    Tag tag
}
```

다음과 같은 유형의 FROM절 생성

```
FROM Blog B
    LEFT JOIN Author A ON B.author_id = A.id
    LEFT JOIN Post P ON B.id = P.blog_id
    LEFT JOIN Comment C ON P.id = C.post_id
    LEFT JOIN Post_Tag PT ON PT.post_id = P.id
    LEFT JOIN Tag T ON PT.tag_id = T.id
```

JOIN 테이블들에 @Column의 references 속성이 정의되어 있는 경우엔 ON 구문을 생략할 수 있다. ON 구문 생략 시 references 에 따라서 자동으로 ON 구문을 생성한다.

```
@Join("blog LEFT JOIN author LEFT JOIN post LEFT JOIN comment LEFT JOIN postTag LEFT JOIN tag")
```

다만, 이럴 땐 실제로 JOIN 구문이 정확하게 생성이 되는지 확인이 필요하다.
```
JoinHandler handler = new JoinHandler(BlogResult.class);
System.out.println(handler.getName());
// 출력 결과
Blog blog_
    LEFT JOIN Author author_ ON blog_.author_id = author_.id
    LEFT JOIN Post post_ ON blog_.id = post_.blog_id
    LEFT JOIN Comment comment_ ON post_.id = comment_.post_id
    LEFT JOIN Post_Tag postTag_ ON postTag_.post_id = post_.id
    LEFT JOIN Tag tag_ ON postTag_.tag_id = tag_.id
```

SELF JOIN

```
@Table("EMPLOYEE")
class Employee {
    @Column(primaryKey=true)
    Integer employeeId;
    @Column
    String name;
    @Column
    Integer managerId;
}

@Join("employee LEFT JOIN manager ON employee.managerId = manager.employeeId")
class EmployeeManager {
    @Fields("*")
    Employee employee;
    @Fields("employeeId,name")
    Employee manager;
}
```

```
SELECT A.employeeId, A.name, A.managerId, B.employeeId, B.name
FROM EMPLOYEE A LEFT JOIN EMPLOYEE B ON A.managerId = B.employeeId
```

[SQL92](http://www.contrib.andrew.cmu.edu/~shadow/sql/sql1992.txt) JOIN FORMAT


```
         <joined table> ::=
                <cross join>
              | <qualified join>
              | <left paren> <joined table> <right paren>

         <cross join> ::=
              <table reference> CROSS JOIN <table reference>

         <qualified join> ::=
              <table reference> [ NATURAL ] [ <join type> ] JOIN
                <table reference> [ <join specification> ]

         <join specification> ::=
                <join condition>
              | <named columns join>

         <join condition> ::= ON <search condition>

         <named columns join> ::=
              USING <left paren> <join column list> <right paren>

         <join type> ::=
                INNER
              | <outer join type> [ OUTER ]
              | UNION

         <outer join type> ::=
                LEFT
              | RIGHT
              | FULL

         <join column list> ::= <column name list>
```