# Annotation #

  * `@Join`, `@Fields` 추가
  * `@Column` 에 references 속성 추가

# 사용법 #

  1. `@Table` annotation을 갖는 클래스들로 복합 클래스를 생성하고 `@Join` annotation 부여
  1. `@Table` annotation 클래스에 `@Fields` annotation 이용하여 SELECT 대상 필드 지정
  1. foreign key에 해당되는 필드의 `@Column` annotation에 references 속성 추가

# @Join Annotation #

`@Join(<join hint>)`와 같이 쓴다. <join hint> 가 없으면, `@Join` 클래스에 선언된 순서대로 INNER JOIN 구문 생성한다.

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
SELECT group_.group_key, group_.group_name, user_.user_key, user_.user_name
FROM GROUP group_ INNER JOIN GROUP_USER groupUser_ ON group_.group_key = groupUser_.group_key
    INNER JOIN USER user_ ON groupUser_.user_key = user_.user_key
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

# Join Hint #

join hint는 join 이 일어나는 테이블과 컬럼에 매핑되는 클래스와 필드로 구성되는 join 구문이다. 예를 들어서, GROUP 테이블과 GROUP\_USER 테이블의 inner join 구문을 작성하면,

```
GROUP G INNER jOIN GROUP_USER GU ON G.group_key = GU.group_key
```

과 같다. GROUP 테이블, GROUP\_USER 테이블에 매핑되는 클래스가 Group, GroupUser이고, join 클래스가 GroupAndUser 라고 할 때, join hint 는 GROUP 과 GROUP\_USER 대신 GroupAndUser의 group, groupUser 필드를 삽입하고, GROUP.group\_key와 GROUP\_USER.group\_key 대신 group.groupKey, groupUser.groupKey 를 삽입한 것이다.

```
group INNER JOIN groupUser ON group.groupKey = groupUser.groupKey
```

다음은 복잡한 LEFT OUTER JOIN 의 예이다.

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

다음과 같은 join 구문을 생성한다.

```
BLOG blog_
    LEFT JOIN AUTHOR author_ ON blog_.author_id = author_.id
    LEFT JOIN POST post_ ON blog_.id = post_.blog_id
    LEFT JOIN COMMENT comment_ ON post_.id = comment_.post_id
    LEFT JOIN POST_TAG postTag_ ON postTag_.post_id = post_.id
    LEFT JOIN TAG tag_ ON postTag_.tag_id = tag_.id
```

JOIN 테이블들에 @Column의 references 속성이 정의되어 있는 경우엔 ON 구문을 생략할 수 있다. ON 구문 생략 시 references 에 따라서 자동으로 ON 구문을 생성한다. ON 구문이 없고, references도 지정되어 있지 않으면, InvalidJoinException이 발생한다.

```
@Join("blog LEFT JOIN author LEFT JOIN post LEFT JOIN comment LEFT JOIN postTag LEFT JOIN tag")
```

다만, 이럴 땐 실제로 JOIN 구문이 정확하게 생성이 되는지 확인이 필요하다.

```
JoinHandler handler = new JoinHandler(BlogResult.class);
System.out.println(handler.getName());
// 출력 결과
BLOG blog_
    LEFT JOIN AUTHOR author_ ON blog_.author_id = author_.id
    LEFT JOIN POST post_ ON blog_.id = post_.blog_id
    LEFT JOIN COMMENT comment_ ON post_.id = comment_.post_id
    LEFT JOIN POST_TAG postTag_ ON postTag_.post_id = post_.id
    LEFT JOIN TAG tag_ ON postTag_.tag_id = tag_.id
```

다음은 SELF JOIN의 예이다.

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

EmployManager empMananager = new EmployerManager();
entityManager.list(empManager);
```

다음과 같은 MyBatis statement가 실행된다.

```
SELECT employee_.employeeId, employee_.name, employee_.managerId,
    manager_.employeeId, manager_.name
FROM EMPLOYEE employee_ LEFT JOIN EMPLOYEE manager_
    ON employee_.managerId = manager_.employeeId
```