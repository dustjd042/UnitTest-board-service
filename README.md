# Board Service API
> 게시판 플랫폼 RESTful API 서버입니다.
> * 주요 기능
>   * 회원
>   * 게시판
>   * 댓글
>   * 알림
---

## 💡 1. 핵심 설계 및 문제 해결
### 외부 의존성(데이터베이스, 외부 API)에 독립적인 구조 설계
개발 과정에서 데이터베이스와 외부 API에 강하게 결합된 구조로 인해 테스트 작성이 어렵고 실행 속도가 느려지는 문제를 경험했습니다.  
이를 해결하기 위해 외부 의존성을 제거하고, 테스트 가능한 구조로 개선했습니다.
* Fake Repository 활용해 데이터베이스 없이도 도메인 로직을 검증
  * [fake repository](https://github.com/dustjd042/UnitTest-board-service/tree/main/src/test/java/com/yeonseong/board/repository/fake)
* Mock 객체를 활용해 외부 API 응답을 제어하고 다양한 케이스 테스트
  * [Mock 테스트](https://github.com/dustjd042/UnitTest-board-service/blob/main/src/test/java/com/yeonseong/board/service/NoticeServiceTest.java)
* 외부 API 호출을 인터페이스로 추상화하여 구현체 교체가 가능한 구조
  * [인터페이스 구조](https://github.com/dustjd042/UnitTest-board-service/tree/main/src/main/java/com/yeonseong/board/service/send)
    
---

## 🚀 2. 기술 스택
| 분류 | 기술                         |
| :--- |:---------------------------|
| **Framework** | Spring Boot 3.5.11         |
| **Language** | Java 17                    |
| **Test** | JUnit 5, AssertJ, Mockito, |

---

## 📂 3. 프로젝트 구조

### **Project Structure**
```plaintext
project-root
 │ 
 ├── src
 │    ├── main
 │    │    ├── java/com/yeonseong/board/
 │    │    │    ├── aop/         # 공통 관심사 분리 코드
 │    │    │    ├── config/      # 프로젝트 환경 설정
 │    │    │    ├── construct/   # 상수 및 고정 코드값 정의
 │    │    │    ├── controller/  # REST API 엔드포인트
 │    │    │    ├── dto/         # 계층 간 데이터 전송 객체 (Request/Response)
 │    │    │    ├── entity/      # 도메인 모델 및 데이터 객체
 │    │    │    ├── exception/   # 전역 예외 처리 및 커스텀 에러 관리
 │    │    │    ├── repository/  # 데이터 접근 계층
 │    │    │    ├── service/     # 핵심 비즈니스 로직
 │    │    │    └── util/        # 공통 유틸리티 클래스
 │    │    └── resources/        # 설정 파일
 │    │
 │    └── test/java/com/yeonseong/board/
 │         ├── helper/           # 테스트 서포트 클래스
 │         ├── repository/
 │         │    └── fake/        # DB 의존성 없이 로직 검증을 가능케 하는 Fake Repository
 │         └── service/          # 핵심 비즈니스 로직 단위 테스트
 │
 ├── build.gradle                # 프로젝트 의존성 및 빌드 환경 설정
 └── README.md                   # 프로젝트 가이드 및 설계 상세
```

