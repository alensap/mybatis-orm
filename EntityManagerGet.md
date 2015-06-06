## `<T>` T get(T parameter) ##

parameter의 null이 아닌 field에 대해 where 절을 생성하여 매핑 테이블에서 하나의 row만 select 하여 parameter와 동일한 class의 객체를 생성하여 리턴한다. 만약, select 결과가 없으면 null을 리턴한다.

예)
```
Hospital hospital = new Hospital();
hospital.setHospitalid(1234);
hospital.setNightCheck(1);
hospital = entityManager.get(hospital);
```

생성되는 MyBatis statement
```sql

SELECT *
FROM hospital
WHERE hosptailid = #{hospitalid} AND night_check = #{nightCheck}
```