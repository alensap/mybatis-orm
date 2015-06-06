## void update(Object parameter) ##

parameter의 클래스에서 primaryKey가 true인 필드값으로 where 절을 생성하여 null 이 아닌 필드를 매핑 테이블에 update 한다.


예)
```
Hospital hospital = new Hospital();
hospital.setHospitalid(1234);
hospital.setName("비타민 병원");
hospital.setRegdttm(new Date());
entityManager.insert(hospital);
```

생성되는 MyBatis statement
```sql

UPDATE hospital
SET name=#{name}, regdttm=#{regdttm}
WHERE hosptailid=#{hospitalid}
```