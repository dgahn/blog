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