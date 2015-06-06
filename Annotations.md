# `@Table` #
```
@Table(value)
```

| Elements | Description | Type | Default | Database |
|:---------|:------------|:-----|:--------|:---------|
| value    | table name  | String | ""      | All      |

value가 없을 때, AnnotationNotFoundException이 발생한다.

# `@Column` #
```
@Column(value)
@Column(name=<>, primaryKey=<>, autoIncrement=<>, sequence=<>)
```

| Elements | Description | Type | Default | Database |
|:---------|:------------|:-----|:--------|:---------|
| value    | column name | String | ""      | All      |
| name     | column name | String | ""      | All      |
| primaryKey | primark key or not | boolean | false   | All      |
| autoIncrement | auto increment or not | boolean | false   | MySql    |
| sequence | sequence name | String | ""      | Oracle   |
| references | JOIN 시 참조할 클래스의 필드 <클래스명.필드명> | String | ""      | All      |

value나 name에 의해 컬럼명이 지정되지 않으면, 필드명을 컬럼명으로 사용한다.

MySQL에서 autoIncrement 가 true이면, 데이터를 insert한 다음, 자동으로 autoIncrement 컬럼의 생성값이 해당 field에 저정된다. 마찬가지로, Oracle에서 시퀀스명을 설정한 경우, 시퀀스값을 가져와서 해당 field에 저장하고, insert를 수행한다.

references 는 JOIN 시 사용이 되며, foreign key 처럼 참조하는 클래스의 필드를 지정한다. 만약, GROUP\_USER 테이블의 group\_key 컬럼이 foreign key 로 GROUP 테이블의 group\_key 를 참조한다고 할 때, GroupUser 클래스의 groupKey 필드에는
```
@Column(references="Group.groupKey")
```
와 같이 매핑되는 클래스와 필드를 지정한다. JOIN에 관해서는 [Join](Join.md)을 참고하기 바란다.

# `@ColumnNaming` #

```
@ColumnNaming(value)
```

클래스의 필드에 선언된 @Column annontation 의 value 또는 name 을 지정하지 않고, 동일 규칙에 의해 필드명을 컬럼명으로 변환하기 위한 annotation 이다.

| Elements | Description | Type | Default | Database |
|:---------|:------------|:-----|:--------|:---------|
| value    | column naming rule | NamingRule | NamingRule.NONE | All      |
| prefix   | 컬럼명 접두사      | String     | ""      | All      |

자세한 것은 ColumnNaming 을 참고하기 바란다.

**Since:** 0.3

# `@Join` #

```
@Join
```

테이블 조인을 처리하기 위한 annotation 이다. Join 클래스에 필드로 선언된 클래스들의 매핑 테이블들로 JOIN 절을 생성한다.

```
@Join
class GroupAndUser {
    Group group;
    User user;
}
```

테이블 JOIN에 관해서는 [Join](Join.md)을 참고하기 바란다.

**Since:** 0.3


# `@Fields` #

```
@Fields(value)
```

| Elements | Description | Type | Default | Database |
|:---------|:------------|:-----|:--------|:---------|
| value    | 필드 목록 | String | ""      | All      |

JOIN 테이블에서 SELECT 할 컬럼에 매핑되는 필드의 목록을 지정한다. 만약, @Join 클래스에서 @Fields 가 선언되지 않은 클래스는 SELECT 에 포함되지 않는다. 그리고, value 가 `"*"`이면, 전체 컬럼이 SELECT 된다.

```
@Join
class GroupAndUser {
    @Fields("groupKey,groupName")
    Group group;
    @Fields("*")
    User user;
}
```

테이블 JOIN에 관해서는 [Join](Join.md)을 참고하기 바란다.

**Since:** 0.3

# `@TypeHandler` #

```
@TypeHandler(value, jdbcType)
```

| Elements | Description | Type | Default | Database |
|:---------|:------------|:-----|:--------|:---------|
| value    | TypeHandler 를 구현한 클래스 | Class<?> |         | All      |
| jdbcType  | 데이터베이스 측 타입 | JdbcType | JdbcType.VARCHAR | All      |

필드 맵핑시 TypeHandler를 지정할 경우 사용한다.

## 제공 TypeHandler ##

  * org.mybatisorm.type.YNTypeHandler : Boolean => 'Y' or 'N'
  * org.mybatisorm.type.TFTypeHandler : Boolean => 'T' or 'F'


```
@Table
class User {
    @Column
    String name;
    @Column
    @TypeHandler(YNTypeHandler.class)
    Boolean male; // Y = Male , N = Female
}
```

related [r187](https://code.google.com/p/mybatis-orm/source/detail?r=187), [issue 2](https://code.google.com/p/mybatis-orm/issues/detail?id=2)

TypeHandler 참고 http://mybatis.github.com/mybatis-3/configuration.html#typeHandlers

# Example #
For MySQL,
```
@Table(“hospital”)
Public class Hospital {
	@Column(primaryKey=true, autoIncrement=true) private Integer hospitalid;
	@Column private String name;
	@Column("night_check") private Integer nightCheck;
	@Column(name=“regdttm”) private Date regdttm;
}
```

For Oracle,
```
@Table(“hospital”)
Public class Hospital {
	@Column(primaryKey=true, sequence="hospitalid_seq") private Integer hospitalid;
	@Column private String name;
	@Column("night_check") private Integer nightCheck;
	@Column(name=“regdttm”) private Date regdttm;
}
```

# 주의 사항 #
  * <font color='red'>필드 타입은 primitive type(int, long, boolean, float, double) 대신, wrapper class(Integer, Long, Boolean, Float, Double)을 사용해야 한다.</font>