## `<T>` Page`<T>` page(T parameter, int pageNumber, int rows) ##

parameter의 null이 아닌 field에 대해 where 절을 생성하여 select 하여 Page 객체 리턴한다. Page 객체에는 parameter와 동일한 타입의 객체 List가 있다.

pageNumber는 현재의 페이지 번호이며, rows는 한 페이지에 포함되는 객체의 수이다.

예)
```
Hospital hospital = new Hospital();
hospital.setHospitalid(1234);
hospital.setNightCheck(1);
Page<Hospital> page = entityManager.page(hospital,2,10);
int count = page.getCount();
for (Hospital h : page.getList()) {
    System.out.println(h.getName());
}
int lastPage = page.getLast();
```

생성되는 MyBatis statement
```sql

/* MySql */
SELECT *
FROM hospital
WHERE hosptailid = #{hospitalid} AND night_check = #{nightCheck}
LIMIT 10, 10

/* Oracle */
SELECT *
FROM (
SELECT *, ROWNUM rnum
FROM hospital
WHERE hosptailid = #{hospitalid} AND night_check = #{nightCheck}
AND ROWNUM <= 20
)
WHERE rnum >= 11

```


## `<T>` Page`<T>` page(T parameter, String orderBy, int pageNumber, int rows) ##

parameter의 null이 아닌 field에 대해 where 절을 생성하고 orderBy로 정렬하여, Page 객체를 리턴한다. Page 객체에는 parameter와 동일한 타입의 객체 List가 있다.

pageNumber는 현재의 페이지 번호이며, rows는 한 페이지에 포함되는 객체의 수이다.

예)
```
Hospital hospital = new Hospital();
hospital.setHospitalid(1234);
hospital.setNightCheck(1);
Page<Hospital> page = entityManager.page(hospital,"regdttm DESC",2,10);
int count = page.getCount();
for (Hospital h : page.getList()) {
    System.out.println(h.getName());
}
int lastPage = page.getLast();
```

생성되는 MyBatis statement
```sql

/* MySql */
SELECT *
FROM hospital
WHERE hosptailid = #{hospitalid} AND night_check = #{nightCheck}
ORDER BY regdttm DESC
LIMIT 10, 10

/* Oracle */
SELECT *
FROM (
SELECT *, ROWNUM rnum
FROM hospital
WHERE hosptailid = #{hospitalid} AND night_check = #{nightCheck}
AND ROWNUM <= 20
ORDER BY regdttm DESC
)
WHERE rnum >= 11
```

## `<T>` Page`<T>` page(T parameter, String condition, String orderBy, int pageNumber, int rows) ##

condition으로 where 절을 생성하고 orderBy로 정렬하여, Page 객체를 리턴한다. Page 객체에는 parameter와 동일한 타입의 객체 List가 있다.

pageNumber는 현재의 페이지 번호이며, rows는 한 페이지에 포함되는 객체의 수이다.

예)
```
Hospital hospital = new Hospital();
hospital.setHospitalid(1234);
hospital.setNightCheck(1);
Page<Hospital> page = entityManager.page(hospital,
    "night_check > #{nightCheck} AND name LIKE '비타민%'",
    "regdttm DESC",2,10);
int count = page.getCount();
for (Hospital h : page.getList()) {
    System.out.println(h.getName());
}
int lastPage = page.getLast();
```

생성되는 MyBatis statement
```sql

/* MySql */
SELECT *
FROM hospital
WHERE night_check > #{nightCheck} AND name LIKE '비타민%'
ORDER BY regdttm DESC
LIMIT 10, 10

/* Oracle */
SELECT *
FROM (
SELECT *, ROWNUM rnum
FROM hospital
WHERE night_check > #{nightCheck} AND name LIKE '비타민%'
AND ROWNUM <= 20
ORDER BY regdttm DESC
)
WHERE rnum >= 11
```


## `<T>` Page`<T>` page(T parameter, Condition condition, String orderBy, int pageNumber, int rows) ##

condition 객체로 where 절을 생성하고 orderBy로 정렬하여, Page 객체를 리턴한다. Page 객체에는 parameter와 동일한 타입의 객체 List가 있다.

pageNumber는 현재의 페이지 번호이며, rows는 한 페이지에 포함되는 객체의 수이다.

예)
```
Hospital hospital = new Hospital();
hospital.setNightCheck(1);
Condition condition = new Condition();
condition.add("night_check", ">", "#{nightCheck}");
condition.add("name","=","비타민");
Page<Hospital> page = entityManager.page(hospital,
    condition,"regdttm DESC",2,10);
int count = page.getCount();
for (Hospital h : page.getList()) {
    System.out.println(h.getName());
}
int lastPage = page.getLast();
```

생성되는 MyBatis statement
```sql

/* MySql */
SELECT *
FROM hospital
WHERE night_check > #{nightCheck} AND name = '비타민'
ORDER BY regdttm DESC
LIMIT 10, 10

/* Oracle */
SELECT *
FROM (
SELECT *, ROWNUM rnum
FROM hospital
WHERE night_check > #{nightCheck} AND name = '비타민'
AND ROWNUM <= 20
ORDER BY regdttm DESC
)
WHERE rnum >= 11
```