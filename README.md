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

## upgradeLevels 메서드 리팩토링
### UserService 클래스의 기존 upgradeLevels 메서드
```java
public void upgradeLevels() {
    List<User> users = userDao.getAll();
    for (User user : users) {
        Boolean changed = null;
        
        if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
            user.setLevel(Level.SILVER);
            changed = true;
        } else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
            user.setLevel(Level.GOLD);
            changed = true;
        } else if (user.getLevel() == Level.GOLD) {
            changed = false;
        } else {
            changed = false;
        }

        if (changed) {
            userDao.update(user);
        }
    }
}
```
* 문제점 1. 현재 레벨 파악 로직(user.getLevel() == Level.BASIC)과 업그레이드 조건을 담은 로직(user.getLogin() >= 50) 혼재 

* 문제점 2. Level 이 추가될 시에 else 문 추가 필요

* 문제점 3. 너무 많은 if/else 문 산재

### upgradeLevel() 리팩토링
```java
public void upgradeLevels() {
    List<User> users = userDao.getAll();
    for (User user : users) {
        if (canUpdateLevel(user)) {
            upgradeLevel(user);
        }
    }
}

private boolean canUpdateLevel(User user) {
    Level currentLevel = user.getLevel();
    switch (currentLevel) {
        case BASIC: return (user.getLogin() >= 50);
        case SILVER: return (user.getRecommend() >= 30);
        case GOLD: return false;
        default: throw new IllegalArgumentException("Unknown Level : " + currentLevel);
    }
}

private void upgradeLevel(User user) {
    if (user.getLevel() == Level.BASIC) user.setLevel(Level.SILVER);
    else if (user.getLevel() == Level.SILVER) user.setLevel(Level.GOLD);
    userDao.update(user);
}
```
* 업그레이드 작업용 메서드를 따로 분리해두면 나중에 작업 내용이 추가되더라도 어느 곳을 수정해야 할지가 명확해진다는 장점이있다.  
ex) 업그레이드시 안내 메일을 보낸다거나 로그를 남기거나 관리자에게 통보를 하는 등의 작업 내용 추가 가능.

* upgradeLevel 메서드에서 다음 단계가 무엇인가 하는 로직과 그때 사용자 오브젝트의 level 필드를 변경해준다는 로직이 함께 있는데다, 너무 노골적으로 드러나 있다.  
게다가 예외상황에 대한 처리가 없다. 만약 GOLD 레벨인 사용자를 업그레이드하려고 이 메서드를 호출하면 그에 따른 예외 상황 처리가 안된다.

### enum Level 리팩토링
* 레벨의 순서와 다음 단계 레벨이 무엇인지를 결정하는 일은 Level 에 맡긴다. 레벨의 순서를 굳이 UserService에 담아둘 이유가 없다.
```java
public enum Level {
    GOLD(3, null),
    SILVER(2, GOLD),
    BASIC(1, SILVER);

    private final int   value;
    private final Level next;

    Level(int value, Level next) {
        this.value = value;
        this.next = next;
    }
    ...
    
    public Level nextLevel() {
        return this.next;
    }
    ...    
```

### User 리팩토링
* 이제 사용자 정보가 바뀌는 부분을 UserService 메서드에서 User로 올긴다.

* User의 내부 정보가 변경되는 것은 UserService보다는 User가 스스로 다루는 게 적절하다.

* UserService가 일일이 레벨 업그레이드 시에 User의 어떤 필드를 수정한다는 로직을 갖고 있기보다는, User에게 레벨 업그레이드를 해야 하니 정보를 변경하라고 요청하는 편이 낫다.

```java
public void upgradeLevel() {
    Level nextLevel = this.level.nextLevel();
    if (nextLevel == null) {
        throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다.");
    } else {
        this.level = nextLevel;
    }
}
```

* 객체지향적인 코드는 다른 오브젝트의 데이터를 가져와서 작업하는 대신 <b>데이터를 갖고 있는 다른 오브젝트에게 작업을 해달라고 요청한다</b>.  
오브젝트에게 데이터를 요구하지 말고 <b>작업을 요청하라는 것</b>이 객체지향 프로그래밍의 가장 기본이 되는 원리이기도 하다.

