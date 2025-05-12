# Team1 - Find Member Project


### **프로젝트 팀원 모집 커뮤니티**
- 실전 프로젝트 경험을 함께 쌓을 팀원을 빠르게 찾을 수 있습니다.
- 협업 성과를 관리할 수 있도록 지원합니다.
- 질의응답 게시판을 통해 자유롭게 도움을 주고받을 수 있습니다.
- 관리자가 회원 관리, 신고글 제재 등을 통해 건전한 커뮤니티 환경을 제공합니다.


>#### 📌 주요 기능
>**🙍‍♂️  사용자**  
>>- 회원가입 및 로그인/로그아웃  
>>- 팀원 모집 게시글 작성  
>>- 팀원 모집 지원  
>>- 질의 응답 게시글 및 댓글  
>>- 프로필  
>>- 게시글 및 댓글 신고
>
>**👷‍♂️  관리자**  
>>- 회원 정보 조회  
>>- 회원 권한 관리  
>>- 게시글 관리  
>>- 신고 내역 관리  
---
### 목차
[1. 개발 환경](#1-개발-환경)  
[2. 시스템 설계](#2-시스템-설계)  
[3. 프로젝트 구조](#3-프로젝트-구조)  
[4. API 명세서](#4-api-명세서)   
[5. 요구사항 명세서](#5-요구사항-명세서)



***

### 1. 개발 환경
<div align=center>
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
  <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white">
  <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
  <br>
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white">
  <img src="https://img.shields.io/badge/ERDCloud-blue?style=for-the-badge">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
</div>

- JAVA JDK 17
- HTML, CSS, JavaScript
- Spring Boot 3.4.4
- MySQL
- JPA (Hibernate)
- 배포환경: AWS EC2, RDS
- 협업도구: GitHub, Notion, Figma, Miro, ERD Cloud

### 2. 시스템 설계
> [ Figma ](https://www.figma.com/design/vPZxGEnBXhnaYrUY8AHhe0/example_site?node-id=10-2&t=k57qr4Df525gChfl-0)

> [ ERD ](https://www.erdcloud.com/d/8vBncPTWotTnE4N64)

### 3. 프로젝트 구조
```
📂src
├── 📂main
│   ├── 📂java
│   │   └── 📂com.estsoft.findmember_team01
│   │       ├── admin
│   │       ├── application
│   │       ├── comment
│   │       ├── configuration
│   │       ├── exception
│   │       ├── information
│   │       ├── login
│   │       ├── member
│   │       ├── recruitment
│   │       └── report
│   └── 📂resources
│       ├── 📂static (JS, CSS)
│       └── 📂templates (HTML)
```

### 4. API 명세서

  
<details><summary>📗 회원 기능</summary>

| 기능 | Method | URL |
|------|--------|-----|
| 회원가입 | POST | `/api/user/signup` |
| 로그인 | POST | `/login` |
| 로그아웃 | POST | `/logout` |
| 회원 탈퇴 | DELETE | `/api/user/{id}` |
| 회원 정보 조회 | GET | `/api/user/me` |
| 회원 정보 수정 | PUT | `/api/user/me` |
| 회원 등급 확인 | GET | `/api/user/{id}/level` |
</details>

<details><summary>📝 팀원 모집 게시판</summary>

| 기능 | Method | URL |
|------|--------|-----|
| 게시글 목록 조회 | GET | `/api/posts` |
| 게시글 상세보기 | GET | `/api/posts/{id}` |
| 새 게시글 작성 | POST | `/api/posts` |
| 게시글 수정 | PUT | `/api/posts/{id}` |
| 게시글 삭제 | DELETE | `/api/posts/{id}` |
| 게시글 완료 | POST | `/api/posts/{projectId}/complete` |
| 게시글 신고 | POST | `/api/posts/{id}/report` |
| 완료된 게시글 목록 | GET | `/api/posts/complete` |
| 완료된 게시글 상세보기 | GET | `/api/posts/complete/{id}` |

✔ 팀원 모집 지원

| 기능 | Method | URL |
|------|--------|-----|
| 지원서 작성 | POST | `/api/posts/{recruitmentId}/apply` |
| 지원서 목록 조회 | GET | `/api/posts/{recruitmentId}/apply` |
| 지원서 상세보기 | GET | `/api/posts/{recruitmentId}/apply/{id}` |
| 지원서 삭제 | DELETE | `/api/posts/{recruitmentId}/apply/{id}` |

</details>

<details><summary>📚 정보공유 게시판</summary>

| 기능 | Method | URL |
|------|--------|-----|
| 게시글 목록 조회 | GET | `/api/information` |
| 게시글 상세보기 | GET | `/api/information/{id}` |
| 게시글 작성 | POST | `/api/information` |
| 게시글 수정 | PUT | `/api/information/{id}` |
| 게시글 삭제 | DELETE | `/api/information/{id}` |

💬 정보공유 게시판 댓글

| 기능 | Method | URL |
|------|--------|-----|
| 댓글 작성 | POST | `/api/information/{informationId}/comment` |
| 댓글 조회 | GET | `/api/information/{informationId}/comment` |
| 댓글 수정 | PUT | `/api/information/{informationId}/comment/{commentId}` |
| 댓글 삭제 | DELETE | `/api/information/{informationId}/comment/{commentId}` |
</details>

<details><summary>🛠 관리자 페이지</summary>

| 기능 | Method | URL |
|------|--------|-----|
| 회원 목록 조회 | GET | `/api/admin/users` |
| 회원 등급 변경 | POST | `/api/admin/users/{id}/role` |
| 회원 등급 관리 | PUT | `/api/admin/users/{id}/level` |
| 신고된 목록 조회 | GET | `/api/admin/reports` |
| 신고글 상세보기 | GET | `/api/admin/reports/{id}` |
| 신고내역 삭제 | DELETE | `/api/admin/reports/{id}` |
| 팀원 모집글 목록 | GET | `/api/admin/posts` |
| 팀원 모집글 상세조회 | GET | `/api/admin/posts/{id}` |
| 신고된 게시글 삭제 | DELETE | `/api/admin/posts/{id}` |
| 게시글 숨기기 | PUT | `/api/admin/posts/{id}` |
</details>

### 5. 요구사항 명세서

<details><summary>📑요구사항 명세서 전체보기</summary>

| 구분 | 기능 | 상세 내용 |
|-------------|------|-----------|
| 공통 | 정렬 | 최신순, 정확도순, 댓글 많은 순 정렬 | 
|  | 페이징 | 게시글 10개씩 페이지 단위로 조회 |
|  | 검색 | 제목 또는 내용에 포함된 키워드 검색 | 
|  | 팀원 모집 필터링 | 전체/모집중/모집완료 선택하여 글 보기 | 
|  | 질의응답 필터링 | 전체/해결/미해결 선택하여 글 보기 |
| 비회원 | 게시글 목록 조회 | 게시글 조회 가능 (읽기 전용) |
| 회원 (USER) | 회원가입 | 형식(조건) 만족하는 이메일, 비밀번호를 입력 받음 |
|  | 로그인 | 이메일과 비밀번호를 입력하여 인증에 성공하면 로그인 처리 및 세션이 생성됨 |
|  | 로그아웃 | 로그인 상태 종료 |
|  | 회원 탈퇴 | 사용자 계정 및 관련 정보 영구 삭제 |
|  | 팀원 모집글 작성/수정/삭제 | 수정·삭제는 본인이 작성한 글만 가능 |
|  | 질의응답 작성/수정/삭제 | 수정·삭제는 본인이 작성한 글만 가능 |
|  | 지원서 작성 | 팀원 모집글에 지원서를 작성하여 제출 |
|  | 게시글/댓글 신고 | 부적절한 게시글·댓글 신고 가능 |
| 회원 (MASTER) | 프로필 업데이트 | 프로필에 이미지를 추가할 수 있음 |
|  | 댓글 작성 | 질의응답 게시판 댓글 작성 가능 |
| 관리자 | 회원 목록 조회 | 회원 닉네임 검색<br>회원별 가입일자, 최종 접속시간, 접속 횟수, User Agent 확인 |
|  | 회원 등급 변경 | USER/MASTER/ADMIN 변경 |
|  | 신고글 목록 조회 | 신고된 게시글/댓글 목록 확인 가능 |
|  | 신고글 상세 조회 | 신고된 게시글/댓글 상세 조회 |
|  | 신고글 삭제 | 신고된 게시글/댓글 삭제 가능 |
|  | 게시글 조회 | 모든 게시글 조회 및 관리 기능 |
|  | 게시글 숨기기 | 게시글 숨김 처리 |
</details>

<br>
<br>


[목차로 돌아가기](#목차)
