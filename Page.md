EntityManager에서 page() 메쏘드를 호출했을 때 리턴되는 객체이다. 전체 row의 수와 해당 페이지에 포함되는 row의 객체 리스트(java.util.List)를 가지고 있다.

# Constructors #

## Page(int pageNumber, int rows) ##
Page 객체를 생성한다.
| pageNumber | 현재의 페이지 번호 |
|:-----------|:---------------------------|
| rows       | 한 페이지에 포함되는 아이템의 수 |

## Page(int pageNumber, int rows, int totalCount) ##
Page 객체를 생성한다.
| pageNumber | 현재의 페이지 번호 |
|:-----------|:---------------------------|
| rows       | 한 페이지에 포함되는 아이템의 수 |
| totalCount | 전체 아이템 수       |


# Methods #

## int getPageNumber() ##
현재의 페이지 번호를 리턴한다.

## int getRows() ##
한 페이지에 포함되는 아이템의 수를 리턴한다.

## int getCount() ##
전체 아이템 수를 리턴한다.

## List`<T>` getList() ##
페이지에 포함되어 있는 아이템들을 List에 넣어 리턴한다.

## int getLast() ##
마지막 페이지 번호를 리턴한다. 만약, 전체 아이템 수가 0이면, 마지막 페이지 번호는 0이다.