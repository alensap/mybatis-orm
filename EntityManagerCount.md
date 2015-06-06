## int count(Object parameter) ##

parameter의 null이 아닌 field에 대해 where 절을 생성하여 개수를 count 한다.

예)
```
Hospital hospital = new Hospital();
hospital.setHospitalid(1234);
hospital.setNightCheck(1);
int count = entityManager.count(hospital);
```

생성되는 MyBatis statement
```sql

SELECT COUNT(*)
FROM hospital
WHERE hosptailid = #{hospitalid} AND night_check = #{nightCheck}
```

## int count(Object parameter, String condition) ##

condition으로 where 절을 생성하여 개수를 count 한다.

예)
```
Hospital hospital = new Hospital();
hospital.setNightCheck(1);
int count = entityManager.count(hospital,
    "night_check > #{nightCheck} AND name LIKE '비타민%'");
```

생성되는 MyBatis statement
```sql

SELECT COUNT(*)
FROM hospital
WHERE night_check > #{nightCheck} AND name LIKE '비타민%'
```

## int count(Object parameter, Condition condition) ##

condition 객체로 where 절을 생성하여 개수를 count 한다.

예1)
```
Hospital hospital = new Hospital();
hospital.setNightCheck(1);
Condition condition = new Condition();
condition.add("night_check", ">", "#{nightCheck}");
condition.add("name","=","비타민");
int count = entityManager.count(hospital,condition);
```

생성되는 MyBatis statement
```sql

SELECT COUNT(*)
FROM hospital
WHERE night_check > #{nightCheck} AND name = '비타민'
```

예2)
```
Hospital hospital = new Hospital();
Condition condition = new Condition();
condition.add("night_check", "IN", new int[] {1,2,3});
int count = entityManager.count(hospital,condition);
```

배열 대신 List와 같은 Collection 객체를 파라미터 값으로 입력할 수 있다.

```
Hospital hospital = new Hospital();
Condition condition = new Condition();
ArrayList list = new ArrayList();
list.add(1);
list.add(2);
list.add(3);
condition.add("night_check", "IN", list);
int count = entityManager.count(hospital,condition);
```

생성되는 PreparedStatement
```sql

SELECT COUNT(*)
FROM hospital
WHERE night_check IN (?, ?, ?)
```

실행되는 SQL
```sql

SELECT COUNT(*)
FROM hospital
WHERE night_check IN (1, 2, 3)
```

예3)
```
Hospital hospital = new Hospital();
Condition condition = new Condition();
condition.add("night_check", "BETWEEN", new Integer[] {1,10});
int count = entityManager.count(hospital,condition);
```

생성되는 PreparedStatement
```sql

SELECT COUNT(*)
FROM hospital
WHERE night_check BETWEEN ? AND ?
```

실행되는 SQL
```sql

SELECT COUNT(*)
FROM hospital
WHERE night_check BETWEEN 1 AND 10
```