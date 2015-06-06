@Column 을 갖는 필드명과 매핑되는 컬럼명이 다를 때, @ColumnNaming을 이용하여 필드명으로 컬럼명을 자동 생성할 수 있다.

# Annotation #

```
@ColumnNaming(value)
```

클래스의 필드에 선언된 @Column annontation 의 value 또는 name 을 지정하지 않고, 동일 규칙에 의해 필드명을 컬럼명으로 변환하기 위한 annotation 이다.

| Elements | Description | Type | Default | Database |
|:---------|:------------|:-----|:--------|:---------|
| value    | column naming rule | NamingRule | NamingRule.NONE | All      |
| prefix   | 컬럼명 접두사 | String | ""      | All      |

# NamingRule #

| 상수 | 설명 | 필드명 예 | 생성 컬럼명 |
|:-------|:-------|:--------------|:-----------------|
| NONE   | naming 규칙을 지정하지 않음 |               |                  |
| UPPERCASE | 대문자 사용 | parentGroupKey | PARENTGROUPKEY   |
| LOWERCASE | 소문자 사용 | parentGroupKey | parentgroupkey   |
| UNDERSCORE | camelCase 필드명을 underscore `"_"` 사용하여 단어 구분 | parentGroupKey<br>groupID <table><thead><th> <code>parent_Group_Key</code><br><code>group_ID</code> </th></thead><tbody>
<tr><td> UPPERCASE_UNDERSCORE </td><td> 대문자와 underscore 사용 </td><td> parentGroupKey </td><td> PARENT_GROUP_KEY </td></tr>
<tr><td> LOWERCASE_UNDERSCORE </td><td> 소문자와 underscore 사용 </td><td> parentGroupKey </td><td> parent_group_key </td></tr></tbody></table>

<code>@ColumnNaming</code> 에 의해 생성되는 컬럼명보다 <code>@Column</code>의 column name 이 우선한다. 즉, <code>@ColumnNaming</code> 이 선언되어 있더라도, <code>@Column</code>에 value 또는 name 이 지정되어 있으면, <code>@Column</code>의 컬럼명을 사용한다.<br>
<br>
<h1>Example</h1>

아래의 두 매핑 클래스는 동일한 컬럼명을 갖는다.<br>
<br>
<pre><code>@Table("GROUP")<br>
@ColumnNaming(NamingRule.UPPERCASE_UNDERSCORE)<br>
public class Group {<br>
	@Column(primaryKey = true)<br>
	protected Long groupID;<br>
<br>
	@Column<br>
	protected Long parentGroupKey;<br>
<br>
	@Column<br>
	protected Integer groupType;<br>
<br>
	@Column<br>
	protected String groupID;<br>
<br>
	@Column<br>
	protected String groupName;<br>
}<br>
<br>
@Table("GROUP")<br>
public class Group {<br>
	@Column(name = "GROUP_KEY",  primaryKey = true)<br>
	protected Long groupKey;<br>
<br>
	@Column(name="PARENT_GROUP_KEY")<br>
	protected Long parentGroupKey;<br>
	<br>
	@Column(name="GROUP_TYPE")<br>
	protected Integer groupType;<br>
		<br>
	@Column(name="GROUP_ID")<br>
	protected String groupID;<br>
	<br>
	@Column(name="GROUP_NAME")<br>
	protected String groupName;<br>
}<br>
</code></pre>

아래의 두 매핑 클래스는 동일한 컬럼명을 갖는다.<br>
<br>
<pre><code>@Table("GROUP")<br>
@ColumnNaming(value=NamingRule.UPPERCASE_UNDERSCORE, prefix="GR_")<br>
public class Group {<br>
	@Column(primaryKey = true)<br>
	protected Long groupID;<br>
<br>
	@Column<br>
	protected Long parentGroupKey;<br>
<br>
	@Column<br>
	protected Integer groupType;<br>
<br>
	@Column<br>
	protected String groupID;<br>
<br>
	@Column<br>
	protected String groupName;<br>
}<br>
<br>
@Table("GROUP")<br>
public class Group {<br>
	@Column(name = "GR_GROUP_KEY",  primaryKey = true)<br>
	protected Long groupKey;<br>
<br>
	@Column(name="GR_PARENT_GROUP_KEY")<br>
	protected Long parentGroupKey;<br>
	<br>
	@Column(name="GR_GROUP_TYPE")<br>
	protected Integer groupType;<br>
		<br>
	@Column(name="GR_GROUP_ID")<br>
	protected String groupID;<br>
	<br>
	@Column(name="GR_GROUP_NAME")<br>
	protected String groupName;<br>
}<br>
</code></pre>

<b>Since:</b> 0.3