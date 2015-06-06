Condition 클래스는 SQL 생성 시 where 절의 조건문을 다양한 operator와 확장된 값으로 생성하기 위한 클래스이다.

# Constructors #

## Condition() ##
기본 객체를 생성한다. 기본 구분자는 AND로 지정된다.

## Condition(Seperator seperator) ##
seperator 구분자로 기본 객체를 생성한다. seperator는 Seperator.AND 혹은 Seperator.OR 값을 갖는다.

# Methods #

## void add(String field, String operator, Object value) ##
조건문을 추가한다. field는 파라미터 객체의 필드명, operator는 연산자, value는 컬럼값이다. 쿼리 실행 시에 파라미터 객체의 필드명은 매핑 컬럼명으로 치환된다.

operator는 =, <, >, <>, IN, BETWEEN, LIKE를 쓸 수 있다.

value로 사용 가능한는 타입의 목록은 아래와 같다.
| primitive type | int, long, float, double, char |
|:---------------|:-------------------------------|
| wrapper type   | Integer, Long, Float, Double, String, Character |
| Collection     | `LinkedList`, `ArrayList`, Vector, `TreeSet`, `HashSet` 등 |
| primitive type array | int`[]`, long`[]`, float`[]`, double`[]`, char`[]` |
| wrapper type array | Integer`[]`, Long`[]`, Float`[]`, Double`[]`, String`[]`, Character`[]` |

operator 가 "IN"이면, value는 ( ) 안에 포함된다. 이 때, value가 array 혹은 Collection 이면, value는 **(?, ?, ?)** 처럼 값들이 나열된다.

operator가 "BETWEEN"이면, value는 **? AND ?** 와 같은 형태로 조건절에 삽입된다.

## void add(Condition condition) ##

서브 조건문을 추가한다.


# Examples #

## 예1 ##
```
Condition condition = new Condition();
condition.add("nightCheck", ">", "#{nightCheck}");
condition.add("name","=","비타민");
```
생성되는 조건절
```sql

night_check > #{nightCheck} AND name = '비타민'
```

## 예2. IN operator 사용 ##
```
Condition condition1 = new Condition();
condition1.add("nightCheck", "IN", new int[] {1,2,3});

Condition condition2 = new Condition();
condition2.add("nightCheck", "IN", new Integer[] {1,2,3});

Condition condition3 = new Condition();
condition3.add("nightCheck", "IN", 1, 2, 3);

Condition condition4 = new Condition();
ArrayList list = new ArrayList();
list.add(1);
list.add(2);
list.add(3);
condition4.add("nightCheck", "IN", list);
```
condition1, condition2, condition3, condition4 은 모두 같은 조건절을 생성
```sql

night_check IN (1, 2, 3)
```

### IN SUBQUERY ###
GROUP(Group 클래스)과 USER(User 클래스) 가 M:N 이고, GROUP\_USER 통해서 특정 그룹에 속한 사용자 목록 조회

```
User user = new User(); 
user.setGroupKey(groupKey); 
Condition condition = new Condition(); 
condition.add("userKey", "IN", "SELECT USER_KEY FROM GROUP_USER WHERE GROUP_KEY = #{groupKey}");

entityManager.list(user, condition, "USER_NAME ASC"); 
```

## 예3. BETWEEN operator 사용 ##
```
Condition condition1 = new Condition();
condition.add("nightCheck", "BETWEEN", new Integer[] {1,10});

Condition condition2 = new Condition();
condition.add("nightCheck", "BETWEEN", 1, 10);
```
생성되는 조건절
```sql

night_check BETWEEN 1 AND 10
```

## 예4. LIKE operator 사용 ##
```
/* MySql */
Condition condition = new Condition();
condition.add("name", "LIKE", "CONCAT('%',#{name},'%')");

/* Oracle */
Condition condition = new Condition();
condition.add("name", "LIKE", "'%' || #{name} || '%'");
```

## 예5. 구분자를 Seperator.OR 로 지정 ##
```
Condition condition = new Condition(Seperator.OR);
condition.add("nightCheck", ">", "#{nightCheck}");
condition.add("name","=","비타민");
condition.add("hospitalid","=",1234);
```
생성되는 조건절
```sql

night_check = #{nightCheck} OR name = '비타민' OR hosptalid = 1234
```

## 예6. 서브 조건문 ##
```
Condition condition = new Condition(Seperator.AND);
condition.add("nightCheck", ">", "#{nightCheck}");

Condition subcon = new Condition(Seperator.OR);
subcon.add("name","=","비타민");
subcon.add("hospitalid","=",1234);

condition.add(subcon); 
```

생성되는 조건절
```sql

night_check = #{nightCheck} AND (name = '비타민' OR hosptalid = 1234)
```