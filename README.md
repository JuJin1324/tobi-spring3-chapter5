# 5장 서비스 추상화
토비의 스프링 3.1 Vol.1 스프링 이해와 원리

## 기초 셋팅
* [1장 오브젝트와 의존관계](https://github.com/JuJin1324/tobi-spring3-chapter1)의
기초 셋팅을 모두 가져옴(Maven 포함)

### SQL 추가 쿼리
```mysql
-- testdb 스키마
alter table testdb.users add(Level tinyint not null);
alter table testdb.users add(Login int not null);
alter table testdb.users add(Recommend int not null);

-- spring3 스키마(필요시 추가)
alter table spring3.users add(Level tinyint not null);
alter table spring3.users add(Login int not null);
alter table spring3.users add(Recommend int not null);

```

## 사용자 레벨 관리 기능 추가
### Level Enum
* 'BASIC', 'SILVER', 'GOLD'를 문자열이 아닌 코드화하여 숫자로 저장한다.
```java
class User {
    private static final int BASIC  = 1;
    private static final int SILVER = 2;
    private static final int GOLD   = 3; 
    
    private int level;
    public void setLevel(int level) {
        this.level = level;
    }
}
```
* 해당 코드를 `final int` 로 상수화하여 저장한다면 setLevel(int) 메서드에서 범위 밖의 값을 입력받을 수 있다.

* 다음과 같이 enumerate를 사용하자.
```java
public enum Level {
    BASIC(1),
    SILVER(2),
    GOLD(3);

    private final int value;

    Level(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    public static Level valueOf(int value) {
        switch (value) {
            case 1:
                return BASIC;
            case 2:
                return SILVER;
            case 3:
                return GOLD;
            default:
                throw new AssertionError("Unknown value : " + value);
        }
    }
}
```

