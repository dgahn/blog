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

### 아이템 5: 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

클래스에 필드를 선언할 때 객체를 직접 생성하지 말라는 의미다. 가장 쉬운 예로는 생성자로 객체를 넘겨주는 방식이다.

또 다른 방식은 팩터리 메서드 패턴을 구현한 객체를 넘겨주는 방식이다. 자바8 Supplier<T>(item31를 보면 좀 더 이해할 수 있을 듯)를 활용할 수 있다.

스프링과 같은 프레임워크를 사용하면 의존 객체를 주입하는 것을 좀 더 쉽게 해결할 수도 있다.

### 아이템 6: 불필요한 객체 생성을 피하라

말그대로 불필요한 객체를 생성하라는 의미다. 예를 들어 자바에서 같은 리터럴은 객체를 재사용함을 보장한다. 그렇기 때문에 아래와 같은 생성하는 것은 정말 불필요하다.

```java
String s = new String("bogle"); // X
String s = "bogle"; // O
```

### 아이템 7: 다 쓴 객체 참조를 해제하라

자바는 C나 C++과 다르게 가바지 컬렉션이 메모리 회수를 해주기 때문에 편하지만 가끔 클래스에서 자기 메모리를 직접 관리하는 경우에는 메모리 누수가 발생할 수도 있다. 아직 정확하게 무슨 뜻인지는 잘 모르겠다.

### 아이템 8: finalizer와 cleaner 사용을 피하라.

자바는 두가지 객체 소멸자를 제공한다.

- `finalizer`
- `cleaner`

자바 9에서는 `finalizer`를 **deprecated**했고 `cleaner` 를 추천한다.

둘 다 메모리 회수를 위해 사용하지만 실행되기까지 얼마나 걸릴지는 알 수 없고 성능적으로도 좋지 않다.

`finalizer`나 `cleaner`를 사용하기보다 `AutoCloseable` 를 구현해주면 된다.

### 아이템 9: try-finally보다는 try-with-resources를 사용하라

자바에는 객체를 사용하고 나서 자원을 회수해야하는 라이브러리들이 있다.

- InputStream
- OutputStream
- java.sql.Connection

이걸 하나만 사용한다면 문제없이 finally에서 처리할 수 있겠지만 두개 이상 사용할 때는 finally에서 에러가 발생하는 경우에 자원을 회수할 수 없을 수도 있다. 그렇기 때문에 이를 보안하기 위해 `try-with-resources`를 사용하는 것이 더 좋다.

## 3장 모든 객체의 공통 메서드

이 장에서는 final이 아닌 Object 메서드들을 언제 어떻게 재정의해야 하는지를 다룬다.

### 아이템10 : equals는 일반 규약을 지켜 재정의하라

`equals`는 잘못 재정의하면 프로그래머가 원치 않은 결과를 나을 수 있다. 그래서 문제가 생길거 같으면 `equals`에 대해 재정의하지 않는 것이 좋다. `equals`를 재정의하지 않으면 **인스턴스는 자기자신만을 같은 객체**라고 판단한다. 다음의 경우에는 `equals`를 재정의하지 않아도 된다.

- **각 인스턴스가 본질적으로 고유하다.** 말이 어렵지만 객체가 필드 값을 가지고 있지 않고 동작을 의미할 때는 이야기한다. `Thread` 같은 클래스가 그 예다.
- **인스턴스의 `논리적 동치성`을 검사할 일이 없다.** 1번과 크게 다르지 않지만 객체가 필드를 가지고 있음에도 논리적으로 동치성을 비교할 필요가 없는 경우를 의미한다. 예를 들어 `java.util.regex.Pattern`의 경우 필드를 가지고 있지만 실질적으로 equals를 쓸 일이 없기 때문에 `equals`를 재정의할 필요가 없다.
- **상위 클래스에서 재정의한 equals가 하위 클래스에도 딱 들어맞는다.** 말 그대로 상위 클래스의 `equals`와 하위 클래스에서 그대로 사용해도 무방하다면 **하위 클래스**에서 `equals`를 재정의 할 필요 없다는 의미다.
- **클래스가 `private`이거나 `package-private`이고 `equals` 메서드를 호출할 일이 없다.** 클래스의 가시성을 매우 제한한 상태여서 패키지 외부에서 호출 될 일이 없고 해당 패키지에서도 **`equals`**를 사용하지 않는다면 `equals`를 재정의 할 필요가 없다.

반대로 `equals`를 재정의해야하는 경우는 언제일까? 조건을 상세히 보면 다음과 같다.

- 논리적 동치성을 확인해야하고
- 상위 클래스의 equals가 논리적 동치성을 판단하지 못한다면

이런 경우는 대부분 값 클래스에 해당한다. 값 클래스는 대표적으로 `Integer`, `String` 이 있다. 값 객체를 비교할 때는 인스턴스가 같은지 확인하는 것이 아니라 값 객체가 가지고 있는 값 자체를 비교하고 싶을 것이다. 논리적 동치성을 이루게 되면 `Map`의 키와 `Set`의 원소로 사용할 수 있다.

값 클래스라고 해도 해당 객체의 인스턴스가 하나임을 보장한다면 `equals`를 재정의하지 않아도 된다.

- 싱글턴 패턴이 적용된 클래스
- enum

equals 메서드를 재정의할 때는 다음을 만족해야한다.

- 반사성
    
    ```
    null이 아닌 모든 참조 값 x에 대해, x.equals(x)는 true다.
    ```
    
    어려울 것이 없고 객체가 자기자신과 같아야한다는 의미다.
    
- 대칭성
    
    ```
    null이 아닌 모든 참조 값 x, y에 대해 x.equals(y)가 true면 y.equals(x)도 true다.
    ```
    
    두 객체는 서로에 대한 동치 여부에 똑같이 답해야한다는 의미다.
    
- 추이성
    
    ```
    null이 아닌 모든 참조 값 x,y,z에 대해 x.equal(y)가 true이고 y.equals(z)도 true면 x.equal(z)도 true다
    ```
    
    첫번째 객체와 두번째 객체가 같고 두번째 객체와 세번째 객체가 같다면 첫번째 객체와 세번째 객체가 같다는 의미다.
    
- 일관성
    
    ```
    null이 아닌 모든 참조 값 x,y에 대해, x.equals(y)를 반복해서 호출하면 항상 true를 반환하거나 항상 false를 반환한다.
    ```
    
    `equals`를 여러번 호출했을 때, 항상 같은 같은 결과가 나와야한다는 의미다. 항상 **true**이거나 항상 **false**여야 된다.
    
- null-아님
    
    ```
    null이 아닌 모든 참조 값 x에 대해, x.equals(null)은 false다.
    ```

### 아이템 11: equals를 재정의하려거든 hashCode도 재정의하라

**equals**를 재정의하면 **hashCode**도 재정의해야한다. 그 이유는 **hashCode 일반 규약**을 어기게 되기 때문이다. **hasCode 일반 규약**을 지키지 못하면 `HashMap`이나 `HashSet` 같은 컬렉션에서 해당 클래스를 원소로 사용할 때 문제가 생긴다.

**Object 명세**에서 말하는 **HashCode** 일반 규약은 다음과 같다.

- **Object의 equals** 메소드에서 사용하는 필드의 정보가 변경되지 않았다면 런타임 시간 동안은 **Object의 hashCode** 메서드를 몇번 호출해도 항상 같은 값을 반환해야한다.
단, 애플리케이션을 재실행하면 그 값이 달라질 수 있다.
- **equals**가 두 객체를 같다고 판단했다면, 두 객체의 **hashCode**는 같은 값을 반환한다.
- **equals**가 두 객체를 다르게 판단했더라도, 두 객체의 **hashCode**가 서로 다른 값을 반환할 필요는 없다.
단, **equals**가 두 객체를 다르게 판단했을 때, 두 객체의 **hashCode**가 서로 다른 값을 반환해야 **hashTable**의 성능이 좋아진다.

두번째 조항

**hash 코드**를 잘못 정의 했을 때 문제가 되는 조항은 두 번째다. 논리적으로 동치인 객체는 같은 **hashCode**를 반환해야한다. 클래스의 해쉬코드를 재정의하지 않으면 **Object의 hashcode**를 그대로 사용하는데 이 때 **hashCode**는 두 객체가 논리적으로 같아도 서로 다른 **hashCode**를 반환한다.

위 조항을 만족하기 위해서 **hashCode**를 구현할 때 주의해야하는 것은 **equals**에 포함되지 않은 필드는 **hashcode**에도 포함하면 안된다.

세번째 조항

간단하게 아래와 같이 구현할 수 있지만 모든 객체에서 같은 해시코드의 값을 반환하게되면 해시테이블에서 하나의 [버킷](https://mangkyu.tistory.com/102)에 모든 객체가 담겨 마치 연결 리스트처럼 동작한다. 그러면 해시테이블 O(1)로 동작해야되는데 O(n)으로 동작하게 되어 속도가 떨어지게 된다.

```java
@Override public int hashCode() { return 42; }
```

좋은 `hashCode()`를 선언하는 방법

```
이상적인 hashCode() 는 인스턴스들을 32비트 정수 범위에 균일하게 분배해야한다.
```

1. equals에서 사용하는 핵심 필드에 대해 해시코드를 계산한다.
1. Primitive 타입인 경우 Type.hashCode(필드)를 수행한다. ex) Integer.hash(a)
2. 참조 타입인 경우 필드가 Primitive 타입이 나올 때까지 재귀적으로 hashCode가 호출된다.
3. 배열인 경우 배열의 요소 중에 핵심 필드에 대해서만 hashCode를 작성하고 모든 요소들이 핵심이라면 Arrays.hashCode를 사용한다.
2. hashcode로 만들어낸 결과물들을 각각 다음과 같이 계산한다.
- **result** = **31 * result + c**`(필드의 해쉬값)`
3. 파생 필드들은 모두 제외해도 된다. 다른 필드로부터 계산해낼 수 있는 필드를 의미한다. 예를 들어, 생년월일이 있다면 나이에 대해서는 제외해도 된다는 의미다.

**2번식**을 사용할 때는 **필드를 어떤 순서로 하느냐에 따라 값**이 달라지기 때문에 **서로 다른 필드의 값**이 같더라도 **해시 효과를 크게 높여**준다. 곱하는 값으로 31을 선택한 이유는 31이 홀수이면서 소수이기 때문이다. 2를 곱하는 것은 시프트 연산과 같기 때문에 사용하지 않고 소수를 곱하는 이유는 전통적으로 그리해왔기 때문이다.

만약 객체의 해쉬코드를 쉽게 구현한다면 아래와 같이 구현할 수 있지만 성능이 살짝 아쉬운 면이 있다.

```java
@Override
public int hashCode() {
    return Objects.hash(lineNum, prefix, areaCode);
}
```

만약 해시코드를 계산하는데 오래걸린다면 캐시를 활용하는 것이 좋다. 해시코드를 필드로 선언하면 된다. 또는 핵심 필드가 해시코드를 계산하는데 오래걸린다고 해서 해시코드를 계산하는데 생략하면 안된다. 해시 품질이 떨어질 수 있기 때문이다.

### 아이템 12: toString을 항상 재정의하라

기본적으로 `Object`의  `toString` 메서드는 **클래스_이름@16진수로_표시한_해시코드**를 반환하게 되어 있다. `toString` 은 사람이 읽기 쉬운 형태로 간결해야하는데 기본적인 `toString`은 간결하기는 하지만 유익한 정보라고 볼 순 없다. 그래서 프로그래머가 디버깅을 하기 위한 정보로 로그를 남기기 위해서는 사람이 읽을 수 있는 형태로 재정의를 해야한다.

toString을 재정의할때 좋은 형태는 **객체가 가진 주요 필드**를 모두 표현해주는 것이 좋다.

### 아이템 13: clone 재정의는 주의해서 진행하라

**Java**에는 인스턴스를 복사하기 위한 도구로 `clone`이라는 메소드가 있다. 이 메소드는 **Object**에 선언이 되어있고 제대로 사용하기 위해서는 **Object**에 선언된 메소드를 오버라이딩 해야된다. 번거로운 점은 오버라이딩를 하는 동시에 복사가 가능한다는 의미로 `Cloneable`이라는 인터페이스를 **implement**를 해야한다. 이 인터페이스에는 실제로 구현해야하는 함수는 존재하지 않는다. 이렇게 단순히 표시로써의 기능만 수행하는 인터페이스를 `maker interface`라고 한다.

이 clone 메소드는 깊은 복사 기반이 아닌 얕은 복사기반으로 동작한다. 그렇기 때문에 **원본 A와 복사본 A`**가 있을 때 한쪽에서 **가변 객체를 참조하는 필드를 변경**한다면 **다른 쪽에서도 변경된 값**을 가져오게 되어있다. 그래서 만약 가변 객체를 참조하는 필드가 있다면 아래와 같이 `clone` 함수를 정의해야한다.

```java
@Override public Stack clone() {
    try {
        Stack result = (Stack) super.clone();
        result.elements = elements.clone();
        return result;
    } catch (CloneNotSupprotedException e) {
        throw new AssertionError();
    }
}
```

여기서 **elements**가 배열이라면 **각 배열의 값 또한 깊은 복사**로 `clone`해야 한다.