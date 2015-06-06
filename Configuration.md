Spring 프레임웍에서 MyBatis ORM 설정을 위해서는 먼저 MyBatis [SqlSessionFactory](http://www.mybatis.org/spring/factorybean.html)를 설정한 다음, 아래의 코드를 Spring의 설정 XML파일에 추가한다.

```
  <!-- MyBatis ORM -->
  <bean class="org.mybatisorm.EntityManager">
    <property name="sqlSessionFactory" ref="sqlSessionFactory" />
    <property name="sourceType" value="oracle" />
  </bean>
```
sourceType은 데이터베이스의 종류로, mysql 혹은 oracle을 입력한다. 기본값은 mysql 이다.

아래는 Spring 설정에서 DataSource, MyBatis, MyBatis ORM 설정의 예이다.

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
  xmlns:tx="http://www.springframework.org/schema/tx"
  xsi:schemaLocation="
  http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans-3.1.xsd
  http://www.springframework.org/schema/context
  http://www.springframework.org/schema/context/spring-context-3.1.xsd
  http://www.springframework.org/schema/tx
  http://www.springframework.org/schema/tx/spring-tx-3.1.xsd
  ">

  <bean id="log4jInitialization"
    class="org.springframework.beans.factory.config.MethodInvokingFactoryBean">
    <property name="targetClass" value="org.springframework.util.Log4jConfigurer" />
    <property name="targetMethod" value="initLogging" />
    <property name="arguments">
      <list>
        <value>classpath:log4j.properties</value>
      </list>
    </property>
  </bean>

  <!-- DataSource 설정 -->
  <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
    destroy-method="close">
    <property name="username" value="${db.username}" />
    <property name="password" value="${db.password}" />
    <property name="minIdle" value="3" />
    <property name="maxActive" value="6" />
    <property name="defaultAutoCommit" value="true" />
    <property name="driverClassName" value="oracle.jdbc.OracleDriver" />
    <property name="validationQuery" value="SELECT 1 FROM dual" />
    <property name="connectionProperties" value="autoReconnect=true;" />
    <property name="url" value="${db.url}" />
  </bean>

  <!-- Configures transaction management around @Transactional components -->
  <tx:annotation-driven transaction-manager="transactionManager" />

  <!-- Local, JDBC-based TransactionManager -->
  <bean id="transactionManager"
    class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource" />
  </bean>

  <!-- Shared JDBC Data Access Template -->
  <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <constructor-arg ref="dataSource" />
  </bean>

  <!-- MyBatis -->
  <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource" />
    <property name="configLocation" value="classpath:sqlmap.xml" />
    <property name="mapperLocations" value="classpath*:mapper/*.xml" />
  </bean>

  <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="netville.component.bbs.mapper" />
  </bean>

  <!-- MyBatis ORM -->
  <bean class="org.mybatisorm.EntityManager">
    <property name="sqlSessionFactory" ref="sqlSessionFactory" />
    <property name="sourceType" value="oracle" />
  </bean>
</beans>
```

Spring Bean 컨테이너에 등록된 EntityManager 객체는 다음과 같이 접근할 수 있다.
```
@Service
public class HospitalService
    @Autowired
    priavte EntityManager entityManager;

    public Hospital getHospital(int hospitalid) {
        Hospital hospital = new Hospital();
        hospital.setHospitalid(hospitalid);
        return entityManager.get(hospital);
    }
}
```