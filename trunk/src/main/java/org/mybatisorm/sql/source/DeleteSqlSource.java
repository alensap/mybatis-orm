package org.mybatisorm.sql.source;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatisorm.Query;
import org.mybatisorm.annotation.SqlCommand;
import org.mybatisorm.annotation.handler.ColumnHandler;
import org.mybatisorm.annotation.handler.TableHandler;
import org.mybatisorm.sql.builder.DynamicSqlBuilder;

@SqlCommand(SqlCommandType.DELETE)
public class DeleteSqlSource extends DynamicSqlBuilder {

	public DeleteSqlSource(SqlSourceBuilder sqlSourceParser, Class<?> clazz) {
		super(sqlSourceParser);
		staticSql = "DELETE FROM "+TableHandler.getName(clazz);
	}

	public BoundSql getBoundSql(Object parameter) {
		String where = (parameter instanceof Query) ? ((Query)parameter).getCondition() :
			ColumnHandler.getNotNullColumnEqualFieldAnd(parameter);
		return makeWhere(where,parameter);
	}

}