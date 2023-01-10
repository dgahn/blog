# 이펙티브 자바 3판

## 1장 들어가기

이펙티브 자바를 직역하면 효과적인 자바인데 이 `효과적인` 이라는 단어가 `성능적으로 뛰어난` 으로 생각할 소지가 있다. 여기서 말하는 `효과적인` 은 코드를 군더더기 없이 깔끔하게 기능에 대해 최대한 작은 라인으로 구현한다는 의미다.

자바를 이해하는데에는 3가지 단계가 있다.

- 1단계 : 자바 문법을 이해하는 단계
- 2단계 : 자바 관련 라이브러리와 API 이해하기
- 3단계 : 자바로 프로그래밍하는 그 동안의 관례와 효과적인 방법

이 책은 그중에서도 3단계를 익힐 수 있는 책이다. 책에서 말하는 3단계는 자바의 기본 문법을 효율적으로 프로그래밍하는 것뿐만 아니라 기본 라이브러리들(`java.io` , `java.util.concurrent` , `java.util.function` )을 효율적으로 사용하는 방법에 대해서도 많은 비중으로 소개한다.

책은 총 90개의 규칙을 담고 있고 이를 비슷한 주제로 11개의 장으로 묶어 있다. 이는 단순히 자바 프로그래밍이라는 관점뿐만 아니라 더 넓게 소프트웨어 설계라는 측면에서도 다룬다.

책에서는 **Best Practice** 만 소개하는 것이 아니라 **Anti Pattern** 도 같이 소개하면서 좀 더 이해를 돕도록 한다.

책에서 소개하는 90개의 규칙은 기본적으로 2가지 기본 원칙에서 시작한다.

```
컴포넌트란?

- 개별 메서드
- 패키지
- 여러 패키지로 이뤄진 복잡한 프레임워크
```

- 명료성
    - 컴포넌트는 정해진 동작이나 예측할 수 있는 동작만 수행해야 한다.
- 단순성
    - 컴포넌트는 가능한 작아야 하지만 너무 작으면 안된다.
    - 코드는 복사되는게 아니라 재사용되어야 한다.
    - 컴포넌트 사이의 의존성은 최소로 유지해야 한다.
    - 오류는 되도록 컴파일타임에 잡아야 한다.

이 두가지가 무엇보다 중요하다. 이 규칙이 100% 맞지는 않지만 대부분 옳은 판단이고 따르지 않을 때는 그에 맞는 합당한 이유가 있는 것이 좋다.

## 2장 객체 생성과 파괴

이 장에서는 올바르게 객체를 생성하고 파괴하는 방법에 대해서 배운다.

### 아이템 1 : 생성자 대신 정적 팩터리 메서드를 고려하라.

정적 팩터리 메서드는 그 클래스의 인스턴스를 반환하는 단순한 정적 메서드를 말한다. 예를 들어 박싱 클래스인 `Boolean`을 생성하는 정적 팩터리 메서드는 아래와 같이 표현할 수 있다.

```java
public static Boolean valueOf(boolean b) {
	return b ? Boolean.TRUE : Boolean.FALSE;
}
```

정적 팩터리 메서드는 장점과 단점을 모두 가지고 있다. 먼저 장점 다섯가지에 대해 알아보자.

**정적 팩터리 메서드의 장점**

- 이름을 가질 수 있다.
    - 이름을 통해 생성하는 객체의 특성을 쉽게 묘사할 수 있다.

        ```java
        // 정적 팩터리 메서드가 값이 소수인 BigInteger를 반환한다는 의미를 더 잘 이해할 수 있다
        new BigInteger()
        BigIntger.probablePrime()
        ```

- 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.
    - 자주 호출되는 객체는 미리 만들어놓고 전달해주면 된다.

        ```java
        // 자주 호출되는 경우 미리 인스턴스를 생성해서 상수처럼 사용하면 된다.
        public static Boolean valueOf(boolean b) {
        	return b ? Boolean.TRUE : Boolean.FALSE;
        }
        ```

    - 미리 만들어놓은 인스턴스를 활용하여 싱글톤 패턴으로 활용할 수 있다.
- 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.
    - 말 그대로 생성자는 단 하나의 본인 타입으로만 인스턴스를 반환하지만 정적 팩터리 메서드는 하위 타입으로 인스턴스를 반환할 수 있다.
    - 이는 상황에 따라 더 적합한 인스턴스를 생성하는데 함수명을 보고 만들 수 있다는 것이다.
    - 정적 팩터리 메서드를 인터페이스로 다루게되면 API를 사용하는 사용자 입장에서는 구체적인 구현을 신경쓰지 않고 사용하기 때문에 API로 좀 더 쉽게 사용할 수도 있다.

    ```java
    interface Ball {
    	public static Ball newSoccerBall(String sportsName) {
    		return new SoccerBall(sportsName);
    	}

    	public static Ball newBaseBall(String sportsName) {
    		return new BaseBall(sportsName);
    	}
    }
    ```

- 입력 매개변수에 따라 객체를 다르게 변환할 수 있다.
    - 예를 들어, Java EnumSet은 원소가 64개 이하면 RegularEnumSet의 인스턴스를
    - 65개 이상이면 JumboEnumSet의 인스턴스를 반환한다.

    ```java
    interface Ball {
    	public static Ball newBall(String sportsName) {
    		switch(sportsName) {
    			case "축구":
    				return new SoccerBall(sportsName);
    			case "야구":
    				return new BaseBall(sportsName);
    			case "농구":
    				return new BasketBall(sportsName);
    			default:
    				throw new IlleagalArgumentException("만들 수 없는 공입니다.");
    		}
    	}
    }
    ```

- 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.
    - 반환 타입을 `Class<?>` 같이 선언을 해놓으면 나중에 현재는 반환할 객체 클래스를 몰라도 해당 정적 팩터리 메서드를 선언할 수 있다. 그래서 추후에 결정됬을 때 정해서 클래스를 반환하면 된다.

**정적 팩터리 메서드의 단점**

- 상속을 하려면 public이나 protected 생성자가 필요하니 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없다.
    - 일반적으로 정적 팩터리 메서드 제공하면 생성자를 `private` 으로 선언하는데 이러면 상속을 할 수 없다.
    - 하지만 이 제약은 `item17` 과 `item18` 을 지키게 한다는 점에서 오히려 좋을 수도 있다.
- 정적 팩터리 메서드는 프로그래머가 찾기 어렵다.
    - 생성자처럼 노골적으로 들어나지 않아 찾기가 어렵다

**정적 팩터리 메서드 이름 관례**

정적 팩터리 메서드임을 들어내기 위해 관례처럼 사용하는 이름들이 있다. 이름과 그 이름의 의미에 대해서 알아보자.

- from: 매개변수 한개 사용하는 경우
    - ex) `Date.from(instant)`
- of: 매개변수 여러개 사용하는 경우
    - ex) `EnumSet.of(JACK, QUEEN, KING)`
- valueOf: from과 of의 더 자세한 버전
    - ex) `BigInteger.valueOf(Integer.MAX_VALUE)`
- instance/getInstance: 인스턴스를 반환하지만 같은 인스턴스임을 보장하지 않음
    - ex) `StackWalker.getInstance(options)`
- create/newInstance: 매번 새로운 인스턴스를 생성해 반환함을 보장
    - ex) `Array.newInstance(classObject, arrayLen)`
- getXXX: getInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 사용
    - ex) `Files.getFileStore(path)`
- newXXX: newInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 사용
    - ex) `Files.newBufferedReader(path)`
- type: getXXX과 newXXX의 간결한 버전
    - ex) `Collections.list(legacyLitany)`

### 아이템 2: 생성자에 매개변수가 많다면 빌더를 고려하라

객체를 생성할 때, 필드의 값이 대부분 고정인 경우가 있다. 책에서는 식품 포장의 영양정보를 예로 들었는다. 아래와 같이 빌더 패턴으로 기본 값을 정해놓고 세터를 통해 필요한 값만 지정하는 패턴을 활용하자는 의미다.

```java
public class NutritionFacts {
	private final int servingSize;
	private final int servings;
	private final int calories;
	private final int fat;
	private final int sodium;
	private final int carbohydrate;

	public static class Builder {
		private final int servingSize;
		private final int servings;
		private final int calories = 0;
		private final int fat      = 0;
		private final int sodium   = 0;
		private final int carbohydrate  = 0;

		public Builder(int servingSize, int servings) {
			this.servingSize = servingSize;
			this.servings = servings;
		}

		public Builder calories(int val) {
			calories = val;
			return this;
		}
		... 생략
	}
}
```

**빌더 패턴의 장점**

- 어떤 값을 어떤 파라미터로 넣었는지 한눈에 보기 쉽다.
- 점층적 패턴보다 필드가 추가 되었을 때 유지보수하기 더 편하다

**빌더 패턴의 단점**

- 빌더를 생성해야하기 때문에 성능이 중요한 경우 걸림돌이 될 수 있다.
- 매개변수가 4개 이상 되었을 때 의미가 있다.


### 아이템 3: private 생성자나 열거 타입으로 싱글턴임을 보증하라

**싱글턴**이란 인스턴스를 오직 하나만 생성할 수 있는 클래스를 의미한다. 클래스를 싱글턴으로 만들면 mock으로 대체하기가 어렵기 때문에 테스트가 어려울 수도 있다.

싱글턴을 만드는 방식은 보통 두가지다. 일단, 두가지 모두 생성자를 접근 제한자를 `private`으로 제한한다.

첫번째는 필드에 인스턴스를 선언해놓는 방식

```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elivis() { ... }
}
```

`public static final` 이기 때문에 초기화할 때 딱 한번만 호출된다. 그렇기 때문에 인스턴스가 하나임을 보장한다.단, 리플렉션 API를 활용하면 `private` 생성자를 호출할 수 있다.

이 방법의 장점은 다음과 같다.

- 해당 클래스가 싱글턴임이 API에 명백하게 명시된다.
- 간결하다.

두번째는 정적 팩터리 메서드를 통해서 인스턴스를 제공하는 방법이다.

```java
public class Elvis {
    private static final Elvis INSTANCE = new Elvis();
    private Elvis() { ... }
    public static Elvis getInstatnce() { return INSTANCE; }
}
```

이 방법의 장점은 다음과 같다.

- 정적 팩터리 메서드를 통해 쉽게 변경할 수 있다.
- 예를 들어, 싱글턴이 아니라 호출할 때마다 새로운 인스턴스를 반환하도록 변경할 수 있다.
- 제네릭 싱글턴 팩터리로 만들 수 있다.(아이템 30)
- 아이템 43, 44 참고

하지만 주의해야할 점은 직렬화하고 역직렬화할 때 이런 싱글턴 패턴은 단순히 Serializable을 구현한다고 싱글턴 방식으로 동작하지 않는다. 모든 인스턴스 필드를 일시적(transient)라고 선언하고 readResolve 메서드를 제공해야한다.

readResolve 메서드는 예시는 아래와 같다.

```java
private Object readResolve() {
    return INSTANCE;
}
```

세번째 방법은 원소가 하나인 열거 타입으로 선언하는 것이다.

```java
public enum Elvis {
    INSTANCE;
}
```

이 방법이 매우 간결하고 추가 노력 없이 직렬화도 할 수 있는 깔끔한 방법이다. 그래서 어색하지만 싱글턴을 만드는 **가장 좋은 방법**은 **원소가 하나뿐인 열거타입**을 만드는 것이다.

### 아이템 4: 인스턴스화를 막으려거든 private 생성자를 사용하라

가끔 정적 메서드와 정적 필드만을 담은 클래스를 만들고 싶을 때가 있다. 객체지향적인 코드는 아니지만 그 나름의 쓸모가 있다. 그 예시는 다음과 같다.

- 자바 라이브러리 `java.lang.Math` 은 기본 타입 값과 관련 있는 메서드 선언
- `java.util.Arrays` 은 배열 관련 메서드를 선언
- `java.util.Collections` 은 콜렉션 생성과 관련된 정적 팩터리 메서드를 선언
- `final` 클래스와 관련된 메소드를 모음(final은 상속이 안되기 때문)

위에 소개된 클래스에 대해서 인스턴스화를 막기 위한 방법은 생각보다 간단한다. 생성자의 접근제어자를 `private` 으로 선언하면 된다.

단, 단점이 있다. 생성자가 존재하지만 생성자를 호출할 수 없다는 모순이 있고 상속을 할 수없다. 하위클래스에서 상위 클래스의 생성자를 호출할 수 없기 때문이다.