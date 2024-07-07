# **미니 프로젝트 숙박 예약 서비스**

<br />

**프로젝트 기간**

- 2024.06.17 - 2024.07.05




---

**팀원 구성**

|     **신원준**      | **손민주**  |        **권종원**        |  **구준한**   | **한성민** |
|:----------------:|:---------------------:|:---------------------:|:-------:|:-------:|
| 숙소 카테고리 조회, 장바구니 |  숙소 조회 | CI/CD, ELK, 회원가입, 로그인 | 예약하기, 예약조회  | 테스트 코드  |
|    조회 성능 최적화     | 조회 성능 최적화 |       예약 동시성 처리       |  예약 동시성 처리  |         |
---

**아키텍쳐**


---

**기술 스택 및 도구**

- **Develop**
    - Java 17
    - Spring boot 3.3.0
    - MySQL
    - Spring security
    - Redis ( Lettuce, Redisson )
    - ELK
- **Test**
    - Junit
    - mockito
    - Jmeter

---

## ERD

---

**구현 내용**

- 회원
    - 회원 가입
    - 로그인
- 숙소
    - 카테고리 별 숙소 조회
    - 숙소 상세 조회
- 예약
    - 예약하기 ( 동시성 처리 )
    - 예약 내역 조회
- 장바구니
    - 장바구니 추가
    - 장바구니 조회
    - 장바구니 갯수 조회
    - 장바구니 삭제

## API 명세서
https://sincere-nova-ec6.notion.site/API-93615a95e2eb4c559fa91d54fe32e5c8?pvs=4

## 발표자료

https://drive.google.com/drive/folders/1BHb-9GAqZAKyT5bBluAtRcC0Q1u1KW1Y?usp=drive_link