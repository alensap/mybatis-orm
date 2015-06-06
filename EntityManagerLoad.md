## boolean load(Object parameter) ##

parameter의 null이 아닌 field에 대해 where 절을 생성하여 매핑 테이블에서 하나의 row만 select 한다. 결과가 존재할 경우 parameter 객체에 값을 입력하고, true를 리턴한다. 결과가 없을 때는 false를 리턴한다.

예)
```
Hospital hospital = new Hospital();
hospital.setHospitalid(1234);
hospital.setNightCheck(1);
if (entityManager.load(hospital)) {
    System.out.println("병원명:"+hospital.getName());
} else {
    System.out.println("병원이 존재하지 않습니다.");
}
```

생성되는 MyBatis statement
```sql

/* MySql */
SELECT *
FROM hospital
WHERE hosptailid = #{hospitalid} AND night_check = #{nightCheck}
LIMIT 1

/* Oracle */
SELECT *
FROM hospital
WHERE hosptailid = #{hospitalid} AND night_check = #{nightCheck}
AND ROWNUM = 1
```