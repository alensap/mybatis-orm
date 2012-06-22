/*
 *    Copyright 2012 The MyBatisORM Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatisorm;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.annotation.handler.SqlCommandAnnotation;
import org.mybatisorm.exception.InvalidSqlSourceException;
import org.mybatisorm.sql.builder.SqlBuilder;
import org.mybatisorm.sql.source.CountSqlSource;
import org.mybatisorm.sql.source.DeleteSqlSource;
import org.mybatisorm.sql.source.IncrementSqlSource;
import org.mybatisorm.sql.source.ListSqlSource;
import org.mybatisorm.sql.source.UpdateSqlSource;
import org.mybatisorm.sql.source.ValueGenerator;
import org.mybatisorm.sql.source.ValueGeneratorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Donghyeok Kang(wolfkang@gmail.com)
 *
 */
public class EntityManager extends SqlSessionDaoSupport implements InitializingBean {

	private static Logger logger = Logger.getLogger(EntityManager.class);
	
	private Configuration configuration;
	private SqlSession sqlSession;
	private SqlSourceBuilder sqlSourceParser;
	private String sourceType = "mysql";  // 기본 소스타입은 "mysql"
	private Class<?> loadSqlSourceClass;
	private Class<?> pageSqlSourceClass;
	private Class<?> insertSqlSourceClass;
	private ValueGenerator valueGenerator;
	private String sourceTypePackage;
	
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
	public <T> T getDao(Class<T> clazz) {
		return getSqlSession().getMapper(clazz);
	}
	
	protected void initDao() throws Exception {
		sqlSession = getSqlSession();
		configuration = sqlSession.getConfiguration();
		sqlSourceParser = new SqlSourceBuilder(configuration);
		sourceTypePackage = this.getClass().getPackage().getName()+".sql.source."+sourceType+".";
		
		loadSqlSourceClass = getSourceTypeClass("LoadSqlSource");
		pageSqlSourceClass = getSourceTypeClass("PageSqlSource");
		insertSqlSourceClass = getSourceTypeClass("InsertSqlSource");
		valueGenerator = (ValueGeneratorImpl)getSourceTypeClass("ValueGenerator").newInstance();
		valueGenerator.setConfiguration(configuration);
	}
	
	private Class<?> getSourceTypeClass(String className) throws ClassNotFoundException {
		return Class.forName(sourceTypePackage+className);
	}

	/**
	 * object의 null이 아닌 필드의 값을 증가시킨다. 값이 음수이면 감소된다.
	 * 
	 * @param parameter
	 */
	public void increment(Object parameter) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(IncrementSqlSource.class, clazz);
		sqlSession.update(statementName, parameter);
	}
	
	/**
	 * object에 null 값을 제외하고 매핑 테이블에 insert 한다.
	 * 
	 * @param parameter
	 */
	public void insert(Object parameter) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(insertSqlSourceClass, clazz); 
		sqlSession.insert(statementName, parameter);
	}
	
	/**
	 * object에 primaryKey가 true인 필드값으로 조건절을 생헝하여, null 값인 필드를 제외하고 매핑 테이블에 update 한다.
	 * 
	 * @param parameter
	 */
	public void update(Object parameter) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(UpdateSqlSource.class, clazz);
		sqlSession.update(statementName, parameter);
	}
	
	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블을 delete 한다.
	 * 
	 * @param parameter
	 */
	public void delete(Object parameter) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(DeleteSqlSource.class, clazz);
		sqlSession.delete(statementName, parameter);
	}
	
	/**
	 * condition으로 where 조건을 생성하여 매핑 테이블을 delete 한다.
	 * 
	 * @param parameter
	 * @param condition
	 * @since 0.2.2
	 */
	public void delete(Object parameter, String condition) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(DeleteSqlSource.class, clazz);
		sqlSession.delete(statementName, new Query(parameter,condition,null));
	}
	
	/**
	 * condition으로 where 조건을 생성하여 매핑 테이블을 delete 한다.
	 * 
	 * @param parameter
	 * @param condition
	 * @since 0.2.2
	 */
	public void delete(Object parameter, Condition condition) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(DeleteSqlSource.class, clazz);
		sqlSession.delete(statementName, new Query(parameter,condition,null));
	}
	
	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블에서 1 row만  select한 다음,
	 * parameter 객체에 값을 setting 한다.
	 * 
	 * @param parameter
	 * @return select 결과가 존재하면 true, 없으면 false
	 */
	public boolean load(Object parameter) {
		Object result = get(parameter);
		if (result != null) {
			BeanUtils.copyProperties(result, parameter);
			return true;
		}
		return false;
	}
	
	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블에서 1 row만  select한다.
	 * 
	 * @param parameter
	 * @return select 결과 object
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(T parameter) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(loadSqlSourceClass, clazz); 
		T result = (T)sqlSession.selectOne(statementName, parameter);
		return result;
	}
	
	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블에서 select count(*) 한다.
	 * 
	 * @param parameter
	 * @return select count(*) 결과값 
	 */
	public int count(Object parameter) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(CountSqlSource.class, clazz); 
		return (Integer)sqlSession.selectOne(statementName,parameter);
	}
	
	/**
	 * condition으로 where 조건을 생성하여 매핑 테이블에서 select count(*) 한다.
	 * 
	 * @param parameter
	 * @param condition
	 * @return select count(*) 결과값 
	 */
	public int count(Object parameter, String condition) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(CountSqlSource.class, clazz);
		return (Integer)sqlSession.selectOne(statementName,new Query(parameter,condition,null));
	}
	
	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블에서 select count(*) 한다.
	 * 
	 * @param parameter
	 * @return select count(*) 결과값 
	 */
	public int count(Object parameter, Condition condition) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(CountSqlSource.class, clazz);
		return (Integer)sqlSession.selectOne(statementName,new Query(parameter,condition,null));
	}
	
	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블을 select하여,
	 * object와 동일한 타입의 객체를 java.util.List 에 담아 리턴한다.
	 * 
	 * 예)
	 * Comment comment = new Comment();
	 * comment.setPlusCount(1);
	 * comment.setCommentKey(10);
	 * entityManager.list(comment,"register_date ASC");
	 * 실행되는 SQL
	 * SELECT * FROM plus_count = 1 AND comment_key = 10
	 * 
	 * @param parameter
	 * @return select 결과 리스트
	 */
	public <T> List<T> list(T parameter) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(ListSqlSource.class, clazz);
		List<T> list = sqlSession.selectList(statementName,parameter);
		return list;
	}
	
	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블을 select하여,
	 * orderBy에 의해 정렬하고,
	 * object와 동일한 타입의 객체를 java.util.List 에 담아 리턴한다.
	 * 
	 * 예)
	 * Comment comment = new Comment();
	 * comment.setPlusCount(1);
	 * comment.setCommentKey(10);
	 * entityManager.list(comment,"register_date ASC");
	 * 실행되는 SQL
	 * SELECT * FROM plus_count = 1 AND comment_key = 10 ORDER BY register_date ASC
	 *
	 * @param parameter
	 * @param orderBy
	 * @return
	 */
	public <T> List<T> list(T parameter, String orderBy) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(ListSqlSource.class, clazz);
		List<T> list = sqlSession.selectList(statementName,new Query(parameter,orderBy));
		return list;
	}

	/**
	 * condition으로 where 조건을 생성하여 매핑 테이블을 select한 다음,
	 * object와 동일한 타입의 객체를 java.util.List 에 담아 리턴한다.
	 * 
	 * 예)
	 * Comment comment = new Comment();
	 * comment.setPlusCount(1);
	 * entityManager.list(comment,"plus_count >= #{plusCount}",null);
	 * 실행되는 SQL
	 * SELECT * FROM plus_count >= 1
	 * 
	 * @param parameter
	 * @param condition
	 * @param orderBy
	 * @return select 결과 List 객체
	 */
	public <T> List<T> list(T parameter, String condition, String orderBy) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(ListSqlSource.class, clazz);
		List<T> list = sqlSession.selectList(statementName, new Query(parameter,condition,orderBy));
		return list;
	}
	
	public <T> List<T> list(T parameter, Condition condition, String orderBy) {
		Class<?> clazz = parameter.getClass();
		String statementName = addStatement(ListSqlSource.class, clazz);
		List<T> list = sqlSession.selectList(statementName, new Query(parameter,condition,orderBy));
		return list;
	}
	
	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블에서 count() 한 다음,
	 * 데이터를 select 하여, java.util.List에 담고, 이를 Page 객체에 저장하여 리턴한다.
	 * 
	 * @param parameter 파라미터 객체
	 * @param pageNumber 페이지 번호
	 * @param rows 페이지 당 아이템 개수
	 * @return select 결과 Page 객체
	 */
	public <T> Page<T> page(T parameter, int pageNumber, int rows) {
		return page(parameter,null,pageNumber,rows);
	}
	
	/**
	 * object의 null이 아닌 field에 대해 AND 조건으로 where 조건을 생성하여 매핑 테이블에서 count() 한 다음,
	 * 데이터를 select 하여, java.util.List에 담고, 이를 Page 객체에 저장하여 리턴한다.
	 * 
	 * @param parameter 파라미터 객체
	 * @param orderBy 정렬 방식
	 * @param pageNumber 페이지 번호
	 * @param rows 페이지 당 아이템 개수
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Page<T> page(T parameter, String orderBy, int pageNumber, int rows) {
		int count = count(parameter);
		Page<T> page = new Page<T>(pageNumber,rows,count);
		if (count > (pageNumber-1)*rows) {
			Class<?> clazz = parameter.getClass();
			String statementName = addStatement(pageSqlSourceClass,clazz); 
			page.setList((List<T>)sqlSession.selectList(statementName,
					new Query(parameter,orderBy,pageNumber,rows)));
		}
		return page;
	}
	
	/**
	 * condition으로 where 조건을 생성하여 매핑 테이블에서 count() 한 다음,
	 * 데이터를 select 하여, java.util.List에 담고, 이를 Page 객체에 저장하여 리턴한다.
	 * object의 field 값들은 where 조건에 사용되지 않는다.
	 * 
	 * @param clazz 매핑 클래스
	 * @param condition 조건
	 * @param orderBy 정렬 방식
	 * @param pageNumber 페이지 번호
	 * @param rows 페이지 당 아이템 개수
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Page<T> page(T parameter, String condition, String orderBy, int pageNumber, int rows) {
		int count = count(parameter,condition);
		Page<T> page = new Page<T>(pageNumber,rows,count);
		if (count > (pageNumber-1)*rows) {
			Class<?> clazz = parameter.getClass();
			String statementName = addStatement(pageSqlSourceClass,clazz); 
			page.setList((List<T>)sqlSession.selectList(statementName,
					new Query(parameter,condition,orderBy,pageNumber,rows)));
		}
		return page;
	}
	
	/**
	 * condition으로 where 조건을 생성하여 매핑 테이블에서 count() 한 다음,
	 * 데이터를 select 하여, java.util.List에 담고, 이를 Page 객체에 저장하여 리턴한다.
	 * object의 field 값들은 where 조건에 사용되지 않는다.
	 * 
	 * @param clazz 매핑 클래스
	 * @param condition 조건
	 * @param orderBy 정렬 방식
	 * @param pageNumber 페이지 번호
	 * @param rows 페이지 당 아이템 개수
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Page<T> page(T parameter, Condition condition, String orderBy, int pageNumber, int rows) {
		int count = count(parameter,condition);
		Page<T> page = new Page<T>(pageNumber,rows,count);
		if (count > (pageNumber-1)*rows) {
			Class<?> clazz = parameter.getClass();
			String statementName = addStatement(pageSqlSourceClass,clazz); 
			page.setList((List<T>)sqlSession.selectList(statementName,
					new Query(parameter,condition,orderBy,pageNumber,rows)));
		}
		return page;
	}
	
	private synchronized String addStatement(Class<?> sqlSourceClass, Class<?> type) {
		String id = "_" + sqlSourceClass.getSimpleName() + type.getSimpleName();
		if (!configuration.hasStatement(id)) {
			logger.debug("add a mapped statement, "+id);
			Constructor<?> constructor = null;
			try {
				constructor = sqlSourceClass.getDeclaredConstructor(SqlSourceBuilder.class, Class.class);
			} catch (Exception e) {
				throw new InvalidSqlSourceException(e);
			}
			SqlBuilder sqlBuilder = (SqlBuilder) BeanUtils.instantiateClass(constructor,sqlSourceParser,type);
			SqlCommand sqlCommand = SqlCommandAnnotation.getSqlCommand(sqlSourceClass);
			SqlCommandType sqlType = sqlCommand.value();
			MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration,id,sqlBuilder,sqlType);
			statementBuilder.timeout(configuration.getDefaultStatementTimeout());
			Class<?> resultType = sqlBuilder.getResultType();
			if (resultType != null) {
				List<ResultMap> resultMaps = new ArrayList<ResultMap>();
				ResultMap.Builder resultMapBuilder = new ResultMap.Builder(
						configuration,
						statementBuilder.id() + "-Inline",
						resultType,
						sqlBuilder.getResultMappingList()
						);
				resultMaps.add(resultMapBuilder.build());
				statementBuilder.resultMaps(resultMaps);
			}
			if (SqlCommandType.INSERT.equals(sqlType)) {
				valueGenerator.generate(statementBuilder,id,type);
			}
			MappedStatement statement = statementBuilder.build();
			configuration.addMappedStatement(statement);
		}
		return id;
	}
}