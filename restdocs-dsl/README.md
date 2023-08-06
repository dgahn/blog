# Restdocs DSL

## 개요

평소에 회사에서 API 문서를 작성하기 위해서는 [Swagger](https://github.com/swagger-api)를 사용하여 작성하였다. 
그런데 최근 시작한 프로젝트에 [RestDocs](https://github.com/spring-projects/spring-restdocs)를 사용해야되는 일이 생겼다.
오랜만에 작성하는 RestDocs를 작성하기 위한 테스트 코드는 너무 복잡하고 직관적이지 않다는 것을 느껴 더 좋은 방법이 없을까 고민을 하였고
우연히 toss에서 소개한 [Kotlin으로 DSL 만들기](https://toss.tech/article/kotlin-dsl-restdocs)라는 글을 보게 되었고
코드가 공개되어 있지 않아 직접 사용하기 위해 시간을 내어 만들어 보기로 했다.

## DSL이란?

DSL은 Domain Specific Language의 약자로 특정한 도메인을 적용하는데 특화된 컴퓨터 언어를 의미한다.
굉장히 어려워 보이는 개념 같이 보이지만 사실 개발자라면 모두 DSL를 사용하고 있다.

### DSL 종류

- SQL : 데이터베이스 관리 시스템과 상호작용하여 데이터 조회, 삽입, 수정, 삭제 등을 수행하는데 특화
- HTML : 웹 페이지 구조를 정의하고 표현하는데 사용
- CSS : 웹 페이지의 스타일과 레이아웃을 정의하는데 사용
- XML : 데이터 구조화와 표현에 특화
- JSON : 데이터 교환을 위한 경량화된 데이터 형식으로 사용

종류에서 알 수 있듯이 그냥 어떤 `목적에 특화`되어 있고, 보다 `쉬운 사용법`을 제공하는 것인 것을 알 수 있다.

### Kotlin에서 DSL

DSL은 그 자체가 DSL인 것도 있지만 특정 언어로 DSL을 만들 수가 있다. 대표적으로 Kotlin, Scala등이 있고 이 글에서 소개하는
것도 Kotlin으로 만든 DSL이다.

이미 Kotlin에서는 많은 DSL를 만들었다.

- [Ktor](https://ktor.io/)
- [Kotest](https://kotest.io/)
- [Exposed](https://github.com/JetBrains/Exposed)
- [Arrow](https://arrow-kt.io/)
- [Spring ko-fu](https://github.com/spring-projects-experimental/spring-fu/tree/main/kofu)
- [Koin](https://insert-koin.io/)

### Kotlin 기능들
Kotlin에서 DSL를 가능하게 하는 여러가지 기능들이 있다. 대표적인 기능들을 몇가지 살펴보도록 하겠다.

#### 람다

코틀린에서는 람다를 아래와 같이 표현할 수 있다.
```kotlin
.flatMap ({ a -> Mono.just(a)})
.flatMap { a -> Mono.just(a) }
```

이를 통해서 함수명으로 어떤 영역을 그룹핑했다라는 느낌을 줄 수 있다.

```html
<body>
    <h1>Ktor</h1>
    <articled>
        <h2>Hello from Ktor!</h2>
        <p>Kotlin Framework for creating connected systems.</p>
    </articled>
</body>
```

```kotlin
body {
    h1 {
        + "ktor"
    }
    article {
        h2 {
            + "Hello from Ktor!"
        }
        p {
            + "Kotlin Framework for creating connected systems."
        }
    }
}
```

### 확장 함수

확장함수는 아래와 같이 클래스 외부에서 객체에 메소드를 추가하는 기능을 말한다.

```kotlin
data class Client(
    val name: String
)

fun Client.call(action: () -> Unit) {
    action()
}
```

이 기능을 통해서 내부에 올 수 있는 함수에 대한 타입 체크를 강하게 알 수 있다.

```kotlin
fun main() {
    createHtml().body { // this: BODY
        p {             // BODY.p() 확장함수가 있기 때문에 사용 가능
            
        }
    }
    
    createHtml().p { // HTML.p() 확장함수가 없기 때문에 사용 불가능
        body {       // P.body() 확장함수가 없기 때문에 사용 불가능
            
        }
    }
}
```

### infix 함수
infix 함수는 메소드를 호출할 때 메소드명을 중간에 가도록 할 수 있는 기능이다.

예를 들어, junit에 존재하는 assertEquals()의 시그니처를 보면 아래와 같다.

```Java
public static void assertEquals(Float expected, float actual)
```

실제로 사용할 때 아래와 같이 사용하는데, 문제는 expected와 actual의 순서를 헷갈릴 수 있다는 점이다.
```Kotlin
assertEqual(expected, actual)
```

kotest에서는 아래와 같이 infix 메소드를 제공하는데 주어 - 동사 - 목적어 순으로 작성하면 되기 때문에 헷갈릴 일이 없다.

```Kotlin
infix fun <T, U : T> T.shouldBe(expected: U?)

actual shouldBe expected
```

### Top-level 함수
클래스에 종속되지 않게 메소드를 정의할 수 있다.

### Operator 오버라이드
+,- 등의 연산자를 오버라이드할 수 있다.

```kotlin
@HtmlTagMarker
interface Tag {
    operator fun String.unaryPlus(): Unit {
        text(this)
    }
}

createHtml().body {
    + "hello, DSL" // + 연산을 다른 용도로 사용할 수 있다.
}
```

### 기타등등
이외에도 inline, reified, scope function (run, apply, with 등)등을 통해
DSL을 유용하게 만들 수 있다.
