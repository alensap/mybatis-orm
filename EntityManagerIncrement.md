## void increment(Object parameter) ##

parameter의 클래스에서 primaryKey가 true인 필드값으로 where 절을 생성하여 null 이 아닌 필드의 값을 증가(또는 감소)시킨다.

예)
```
Hospital hospital = new Hospital();
hospital.setHospitalid(1234);
hospital.setNightCheck(1);
entityManager.increment(hospital);
```

생성되는 MyBatis statement
```sql

UPDATE hospital
SET night_check = night_check + #{nightCheck}
WHERE hosptailid = #{hospitalid}
```