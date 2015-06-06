EntityManager 클래스는 데이터베이스의 테이블과 클래스를 1:1 로 매핑하여 기본적인 CRUD operation을 수행하는 클래스로, 다음과 같은 API를 제공한다.

```
void insert(Object parameter)

void update(Object parameter)

void increment(Object parameter)

void delete(Object parameter)
void delete(Object parameter, String condition)
void delete(Object parameter, Condition condition)

<T> T get(T parameter)

boolean load(Object parameter)

int count(Object parameter)
int count(Object parameter, String condition)
int count(Object parameter, Condition condition)

<T> List<T> list(T parameter)
<T> List<T> list(T parameter, String orderBy)
<T> List<T> list(T parameter, String condition, String orderBy)
<T> List<T> list(T parameter, Condition condition, String orderBy)

<T> Page<T> page(T parameter, int pageNumber, int rows)
<T> Page<T> page(T parameter, String orderBy, int pageNumber, int rows)
<T> Page<T> page(T parameter, String condition, String orderBy, int pageNumber, int rows)
<T> Page<T> page(T parameter, Condition condition, String orderBy, int pageNumber, int rows)
```