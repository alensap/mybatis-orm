## void insert(Object parameter) ##

parameter 객체에서 null 이 아닌 필드들로 insert 문을 생성하여 실행한다.

예)
```
Hospital hospital = new Hospital();
hospital.setName("비타민 병원");
hospital.setRegdttm(new Date());
entityManager.insert(hospital);
```
생성되는 MyBatis statement
```sql

INSERT INTO hospital(name,regdttm)
VALUES (#{name}, #{regdttm})
```

MySql에서 hosptialid 컬럼이 auto increment 로 되어 있고, hospitalid 필드에 대해 autoIncrement가 true로 설정되어 있으면, insert() 완료 후, hospitalid 필드에는 auto increment에 의해 새로 생성된 값이 입력된다.

마찬가지로 Oracle에서 hosptailid 필드에 시퀀스명 sequence="hospitalid\_seq" 와 같이 지정되어 있으면, 해당 시퀀스에 의해 생성된 값(hospitalid\_seq.NEXTVAL)이 hospitalid 에 입력된다.