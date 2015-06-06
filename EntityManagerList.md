## `<T>` List`<T>` list(T parameter) ##

parameter의 null이 아닌 field에 대해 where 절을 생성하여 select 하여, parameter와 동일한 타입의 객체 List를 리턴한다.

예)
```
Hospital hospital = new Hospital();
hospital.setHospitalid(1234);
hospital.setNightCheck(1);
List<Hospital> list = entityManager.list(hospital);
```

생성되는 MyBatis statement
```sql

SELECT *
FROM hospital
WHERE hosptailid = #{hospitalid} AND night_check = #{nightCheck}
```

## `<T>` List`<T>` list(T parameter, String orderBy) ##

parameter의 null이 아닌 field에 대해 where 절을 생성하고 orderBy로 정렬하여, parameter와 동일한 타입의 객체 List를 리턴한다.

예)
```
Hospital hospital = new Hospital();
hospital.setNightCheck(1);
List<Hospital> list = entityManager.list(hospital, "regdttm DESC");
```

생성되는 MyBatis statement
```sql

SELECT *
FROM hospital
WHERE night_check = #{nightCheck}
ORDER BY regdttm DESC
```


## `<T>` List`<T>` list(T parameter, String condition, String orderBy) ##

condition으로 where 절을 생성하고 orderBy로 정렬하여, parameter와 동일한 타입의 객체 List를 리턴한다. 정렬이 필요없을 때는 orderBy를 null로 세팅한다.

예)
```
Hospital hospital = new Hospital();
hospital.setNightCheck(1);
List<Hospital> list = entityManager.list(hospital,
    "night_check > #{nightCheck} AND name LIKE '비타민%'",
    "regdttm DESC");
```

생성되는 MyBatis statement
```sql

SELECT *
FROM hospital
WHERE night_check > #{nightCheck} AND name LIKE '비타민%'
ORDER BY regdttm DESC
```


## `<T>` List`<T>` list(T parameter, Condition condition, String orderBy) ##

condition 객체로 where 절을 생성하고 orderBy로 정렬하여, parameter와 동일한 타입의 객체 List를 리턴한다. 정렬이 필요없을 때는 orderBy를 null로 세팅한다.

예1)
```
Hospital hospital = new Hospital();
hospital.setNightCheck(1);
Condition condition = new Condition();
condition.add("night_check", ">", "#{nightCheck}");
condition.add("name","=","비타민");
List<Hospital> list = entityManager.list(hospital,condition,
    "regdttm DESC");
```

생성되는 MyBatis statement
```sql

SELECT *
FROM hospital
WHERE night_check > #{nightCheck} AND name = '비타민'
ORDER BY regdttm DESC
```