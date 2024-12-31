# 프로젝트 목적

레스토랑 도메인을 MSA + 멀티모듈 구조로 개발합니다.
Spring boot와 Kotlin을 사용합니다.
gradle을 사용합니다.

## 기본 컨벤션

- SOLID 원칙을 준수합니다.

## 필수 달성 목록

- EDA (Event-Driven Architecture)
  - MSA간 통신은 Kafka를 사용합니다.
- Saga Transaction (분산 트랜잭션 관리를 위해)
  - MSA간 트랜잭션이 필요한 경우 Saga 패턴을 사용합니다.
- CQRS (Command Query Responsibility Segregation)
  - 명령과 조회를 분리하여 명령 처리와 조회 처리를 분리합니다.
  - ReadModel은 읽기 전용 모델이며, 조회 처리를 담당합니다.
  - Redis를 이용한 캐싱을 합니다.
- DDD (Domain-Driven Design)
  - 도메인 중심의 설계를 합니다.
- Port and Adapter (헥사고날 아키텍처)
- OpenAPI Swagger (API 문서화)
  - 모든 API는 OpenAPI 스펙을 따릅니다.

## 주요 기능

- **주문 서비스**

  - 주문 접수
  - 주문 상태 관리
  - 주문 내역 조회

- **결제 서비스**

  - 결제 처리
  - 결제 내역 조회
  - 환불 처리

- **메뉴 서비스**

  - 메뉴 등록, 수정, 삭제
  - 메뉴 조회

- **고객 서비스**
  - 고객 정보 관리
  - 고객 등급 관리
  - 고객 문의 처리

## 아키텍처 및 기술 스택

- **마이크로서비스 아키텍처 (MSA)**

  - 주문 서비스 (order-app)
  - 결제 서비스 (pay-app)
  - 메뉴 서비스 (menu-app)
  - 고객 서비스 (customer-app)

- **멀티모듈 구조**

  - **독립(Independent) 모듈 계층:**

    - 시스템과 전혀 무관하게, 어디에서나 사용 가능한 라이브러리 성격의 모듈
    - 프로젝트 밖으로도 쉽게 빼서 재사용 가능한 형태
    - 프로젝트의 도메인/비즈니스 로직과 결합 없음
    - 다른 모듈에 의존하지 않음
    - 예: yaml-importer(Yaml 로더), data-dynamo-reactive(DynamoDB 매핑)

  - **공통(Common) 모듈 계층:**

    - 프로젝트 내 모든 모듈에서 두루 사용될 수 있는 최소한의 요소
    - Type(단순 DTO), Utility 같은 순수 Java 코드만 위치
    - 외부 라이브러리에 의존하지 않음 (= 의존성이 거의 0인 모듈)
    - 가능하면 사용하지 않는 방향으로 설계 (최후의 수단)
    - 예: common-types(공통 DTO), common-utils(공통 유틸리티)

  - **도메인(Domain) 모듈 계층:**

    - 저장소와 밀접한 '중심 도메인'을 다루는 모듈
    - 서비스 비즈니스 로직을 모름 (순수 도메인 비즈니스에만 집중)
    - 하나의 모듈은 최대 하나의 인프라(DB, Redis, etc.)만 책임
    - 도메인끼리 조합한 더 큰 모듈을 만들 수 있지만, 기본 원칙은 도메인당 단일 인프라
    - 다른 계층에 의존하지 않음
    - 예: order-domain, member-domain, menu-domain, payment-domain (RDB 사용 시 xxx-domain 이름)

  - **내부(Internal) 모듈 계층:**

    - 도메인 외에도 시스템 전체 흐름을 지원하는 기능성 모듈
    - 어플리케이션이나 도메인 구체 비즈니스를 모름
    - 프로젝트 내 어느 실행 모듈에서든 독립적으로 재사용 가능
    - 도메인 모듈에 의존하지 않음
    - 예: order-client, payment-client, core-web(공통 Web 필터/보안/로깅 등 설정), event-publisher(스프링 이벤트)

  - **어플리케이션(Application) 모듈 계층:**
    - 실행 가능한 독립 어플리케이션(프로그램)이 위치하는 계층
    - 실제 서비스 비즈니스 로직을 여기서 완성
    - 필요한 다른 계층 모듈에 의존 추가하여 사용
    - 'app' 네이밍 (예: order-app, pay-app, menu-app, customer-app)

- **기술 스택**
  - 프로그래밍 언어: Kotlin
  - 프레임워크: Spring Boot
  - 빌드 도구: Gradle
  - 데이터베이스: PostgreSQL (order-domain, member-domain, menu-domain, payment-domain 등에서 사용)
  - 메시지 브로커: Kafka (이벤트 기반 아키텍처, Saga 패턴 구현에 사용)
  - API 게이트웨이: Spring Cloud Gateway (추가 예정)
  - 분산 추적: Spring Cloud Sleuth, Zipkin (추가 예정)
  - 모니터링: Prometheus, Grafana (추가 예정)
  - 테스트: JUnit 5, Mockito, Spring Test

## 각 모듈의 역할과 책임, 의존성 예시

- **독립 모듈:**

  - 역할과 책임: 시스템과 무관하게 재사용 가능한 라이브러리 기능을 제공합니다.
  - 의존성: 다른 모듈에 의존하지 않습니다.

- **공통 모듈:**

  - 역할과 책임: 프로젝트 내에서 공통으로 사용되는 타입, 유틸리티 등을 제공합니다.
  - 의존성: 외부 라이브러리 의존성을 최소화하며, 독립 모듈을 제외한 다른 모듈에는 의존하지 않습니다.

- **도메인 모듈 (order-domain):**

  - 역할과 책임: 주문 도메인과 관련된 엔티티, 리포지토리, 도메인 서비스 등을 포함합니다.
  - 의존성: common 모듈의 일부 유틸리티를 사용할 수 있습니다. JPA, PostgreSQL 의존성을 가집니다. 내부, 어플리케이션 모듈에는 의존하지 않습니다.

- **내부 모듈 (payment-client):**

  - 역할과 책임: 외부 결제 시스템과의 연동을 담당합니다.
  - 의존성: common 모듈의 일부 유틸리티를 사용할 수 있습니다. (원칙상) 도메인 모듈에 의존하지 않습니다. Kafka 의존성을 가질 수 있습니다.

- **어플리케이션 모듈 (xx-order-app):**
  - 역할과 책임: 주문 서비스의 진입점 역할을 하며, 주문 관련 비즈니스 로직을 완성합니다.
  - 의존성: order-domain(도메인 계층), payment-client(내부 계층), common 모듈 등을 의존하여 사용합니다. Spring Boot, Kafka 의존성을 가집니다.

## 모듈 간 의존성 및 확장/사용성

- **모듈 간 의존 방향:**

  - 독립 모듈 ↔ "(의존 없음)" ↔ 공통 모듈 → 도메인 모듈 → 내부 모듈 → 어플리케이션 모듈
  - 어플리케이션 모듈이 최상위에서 모든 하위 모듈을 선택적으로 의존
  - "아래 계층"일수록 다른 모듈에 의존하지 않거나, 최소한으로만 의존
  - 상위 계층의 모듈은 하위 계층의 모듈을 의존할 수 있지만, 그 반대는 불가능합니다.

- **최소 의존성 활용:**

  - Gradle의 api / implementation 키워드 활용
  - 상위 모듈에서 하위 모듈을 implementation 으로 의존하면, 그 하위 모듈이 의존하는 다른 라이브러리를 상위에서 바로 쓸 수 없게 막음(접근 차단)
  - 불필요한 접근과 스파게티 의존을 줄이고, 보호가 필요한 모듈을 감춤

- **Configuration Property / Component Scan:**

  - Spring Boot property 로드:

    - 각 모듈마다 application-xxx.yml을 만들어, 상위 모듈에서 spring.profiles.include=xxx로 로드
    - 또는, EnvironmentPostProcessor나 별도 importer(예: yaml-importer)를 만들어 "모듈만 의존 추가하면 자동으로 설정"되도록 구성

  - Bean(Component) Scan:
    - 일반적으로 "@SpringBootApplication이 위치한 상위 패키지"에 모든 모듈들을 하위 패키지로 두어 스캔
    - 또는, scanBasePackages 확장, spring.factories 설정 등을 활용

- **개방/폐쇄(확장 가능성):**

  - 접근 제한: Java의 public/protected 등 접근제한자와, Gradle의 api/implementation을 함께 활용해 의도치 않은 접근을 최소화
  - 유연한 확장: @ConditionalOnMissingBean, Customizer 패턴 등을 통해 "상위 모듈"이 필요 시 Bean을 오버라이드 가능하도록 여지를 둠

- **README 의무화:**
  - 각 모듈에 README.md를 작성해 "이 모듈의 역할과 책임, 실행/사용 방법, 관례" 등을 팀원과 공유
  - 모듈이 늘어날수록 README 없으면 의도가 불분명해지고 유지보수 난이도가 커짐

---
폴더구조 예시

restaurant-msa-project/
├── build.gradle
├── settings.gradle
├── independent/
│   └── external-api-adapters/       
│       ├── build.gradle
│       └── src/
│           └── main/
│               ├── kotlin/
│               │   └── com/restaurant/external/
│               │       ├── order/
│               │       │   ├── ExternalOrderService.kt
│               │       │   └── ExternalOrderServiceImpl.kt
│               │       ├── payment/
│               │       │   ├── ExternalPaymentService.kt
│               │       │   └── ExternalPaymentServiceImpl.kt
│               │       ├── menu/
│               │       │   ├── ExternalMenuService.kt
│               │       │   └── ExternalMenuServiceImpl.kt
│               │       └── customer/
│               │           ├── ExternalCustomerService.kt
│               │           └── ExternalCustomerServiceImpl.kt
│               └── resources/
├── common/
│   ├── common-types/
│   │   ├── build.gradle
│   │   └── src/
│   │       └── main/
│   │           ├── kotlin/
│   │           └── resources/
│   └── common-utils/
│       ├── build.gradle
│       └── src/
│           └── main/
│               ├── kotlin/
│               └── resources/
├── order-app/
│   ├── build.gradle
│   ├── settings.gradle
│   ├── order-domain/
│   │   ├── build.gradle
│   │   └── src/
│   │       └── main/
│   │           ├── kotlin/
│   │           │   └── com/restaurant/order/domain/
│   │           │       ├── entity/
│   │           │       │   └── Order.kt
│   │           │       ├── repository/
│   │           │       │   └── OrderRepository.kt
│   │           │       └── service/
│   │           │           └── OrderService.kt
│   │           └── resources/
│   ├── order-app/
│   │   ├── build.gradle
│   │   └── src/
│   │       └── main/
│   │           ├── kotlin/
│   │           │   └── com/restaurant/order/app/
│   │           │       ├── OrderAppApplication.kt    
│   │           │       ├── controller/
│   │           │       │   └── OrderController.kt
│   │           │       ├── service/
│   │           │       │   └── OrderServiceImpl.kt
│   │           │       └── configuration/
│   │           └── resources/
│   └── order-client/
│       ├── build.gradle
│       └── src/
│           └── main/
│               ├── kotlin/
│               │   └── com/restaurant/order/client/
│               │       ├── repository/
│               │       │   └── OrderRepositoryImpl.kt
│               │       └── configuration/
│               │           └── DatabaseConfig.kt
│               └── resources/
├── pay-app/
│   ├── build.gradle
│   ├── settings.gradle
│   ├── payment-domain/
│   ├── pay-app/
│   │   ├── build.gradle
│   │   └── src/
│   │       └── main/
│   │           ├── kotlin/
│   │           │   └── com/restaurant/pay/app/
│   │           │       ├── PayAppApplication.kt      
│   │           │       ├── controller/
│   │           │       │   └── PaymentController.kt
│   │           │       ├── service/
│   │           │       │   └── PaymentServiceImpl.kt
│   │           │       └── configuration/
│   │           └── resources/
│   └── payment-client/
│       ├── build.gradle
│       └── src/
│           └── main/
│               ├── kotlin/
│               │   └── com/restaurant/pay/client/
│               │       ├── repository/
│               │       │   └── PaymentRepositoryImpl.kt
│               │       └── configuration/
│               │           └── DatabaseConfig.kt
│               └── resources/
├── menu-app/
│   ├── build.gradle
│   ├── settings.gradle
│   ├── menu-domain/
│   ├── menu-app/
│   │   ├── build.gradle
│   │   └── src/
│   │       └── main/
│   │           ├── kotlin/
│   │           │   └── com/restaurant/menu/app/
│   │           │       ├── MenuAppApplication.kt     
│   │           │       ├── controller/
│   │           │       │   └── MenuController.kt
│   │           │       ├── service/
│   │           │       │   └── MenuServiceImpl.kt
│   │           │       └── configuration/
│   │           └── resources/
│   └── menu-client/
│       ├── build.gradle
│       └── src/
│           └── main/
│               ├── kotlin/
│               │   └── com/restaurant/menu/client/
│               │       ├── repository/
│               │       │   └── MenuRepositoryImpl.kt
│               │       └── configuration/
│               │           └── DatabaseConfig.kt
│               └── resources/
├── customer-app/
│   ├── build.gradle
│   ├── settings.gradle
│   ├── customer-domain/
│   ├── customer-app/
│   │   ├── build.gradle
│   │   └── src/
│   │       └── main/
│   │           ├── kotlin/
│   │           │   └── com/restaurant/customer/app/
│   │           │       ├── CustomerAppApplication.kt 
│   │           │       ├── controller/
│   │           │       │   └── CustomerController.kt
│   │           │       ├── service/
│   │           │       │   └── CustomerServiceImpl.kt
│   │           │       └── configuration/
│   │           └── resources/
│   └── customer-client/
│       ├── build.gradle
│       └── src/
│           └── main/
│               ├── kotlin/
│               │   └── com/restaurant/customer/client/
│               │       ├── repository/
│               │       │   └── CustomerRepositoryImpl.kt
│               │       └── configuration/
│               │           └── DatabaseConfig.kt
│               └── resources/
└── shared/
    ├── openapi/
    │   ├── build.gradle
    │   └── src/
    │       └── main/
    │           ├── kotlin/
    │           └── resources/
    ├── kafka-config/
    │   ├── build.gradle
    │   └── src/
    │       └── main/
    │           ├── kotlin/
    │           └── resources/
    └── swagger-docs/
        ├── build.gradle
        └── src/
            └── main/
                ├── kotlin/
                └── resources/
