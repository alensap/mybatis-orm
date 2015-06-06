# Introduction #

`MyBatis` ORM is a Simple ORM framework to insert, update, delete, and select an JAVA object. It's using MyBatis to generate SQLs in runtime.

`MyBatis` ORM은 MyBatis를 이용하여 자바 객체를 insert, update, delete 그리고, select 하기 위한 간단한 ORM 프레임웍으로, DB 테이블과 자바 객체를 1:1 매핑한다.

# Project Goal #
  * Minimizing writing SQL statements
  * Simple configuration
  * Low learning curve

# `MyBatis` ORM does #
  * object 와 relation 의 1:1 매핑
    * MyBatis 를 이용하여 기본적인 insert, update, delete, select 문을 runtime 자동 생성
    * 생성된 SQL문을 MyBatis SQL repository 에 저장
  * [Join](Join.md) 지원
  * 설정은 Java Annotation 사용
  * MySql의 auto increment, Oracle의 sequence 자동 처리

# `MyBatis` ORM doesn't #
  * 객체간의 Association 은 다루지 않음

# Effect #
모 인터넷 사이트에 적용 결과

| SQL | 적용 전 | 적용 후 |
|:----|:-----------|:-----------|
| insert | 112        | 7          |
| update | 80         | 2          |
| delete | 79         | 0          |
| select | 327        | 176        |
| Total | 598        | 185        |

69% SQL문 제거

# Supported Databases #
  * `MySql`
  * Oracle

# Required #
  * MyBatis 3.1.0+
  * MyBatisSpring  1.1.0+

# Release Notes #
2012년 6월 22일 v0.3

  * [JOIN](Join.md) 지원
  * ColumnNaming 에 의해 컬럼명 자동 매핑 지원

2012년 5월 30일 v0.2

  * column name 설정되지 않는 버그 수정
  * Oracle 지원
  * MySql의 auto increment, Oracle의 sequence 지원
  * EntityManager API 추가
  * 조건절 추가 생성 지원([Condition](Condition.md) 객체)
  * 페이지 처리 지원([Page](Page.md) 객체)