## void delete(Object parameter) ##

parameter 객체에서 null 이 아닌 필드들로 where 절을 생성하여 delete 한다.

예)
```
Hospital hospital = new Hospital();
hospital.setName("비타민 병원");
hospital.setNightCheck(1);
entityManager.delete(hospital);
```

생성되는 MyBatis statement
```sql

DELETE FROM hospital
WHERE name = #{name} AND night_check = #{nightCheck}
```

## void delete(Object parameter, String condition) ##

condition으로 where 절을 생성하여 delete 한다.

예)
```
Hospital hospital = new Hospital();
hospital.setName("비타민 병원");
hospital.setNightCheck(1);
entityManager.delete(hospital, "night_check = #{nightCheck}");
```

생성되는 MyBatis statement
```sql

DELETE FROM hospital
WHERE night_check = #{nightCheck}
```

## void delete(Object parameter, Condition condition) ##

Condition 객체로 where 절을 생성하여 delete 한다.

예)
```
Hospital hospital = new Hospital();
hospital.setName("비타민 병원");
hospital.setNightCheck(1);
Condition condition = new Condition();
condition.add("night_check", "<>", "#{nightCheck}");
entityManager.delete(hospital, condition);
```

생성되는 MyBatis statement
```sql

DELETE FROM hospital
WHERE night_check <> #{nightCheck}
```