# &nbsp; 읽을거리 <a href="https://apps.apple.com/kr/app/%EC%9D%BD%EC%9D%84%EA%B1%B0%EB%A6%AC/id6664069391"><img src="https://github.com/BOOK-TALK/Readables-Server/blob/main/src/main/resources/static/logo.png" align="left" width="100"></a>
[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2FBOOK-TALK%2FReadables-Server&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

<br>

## Download

- #### [App Store Download](https://apps.apple.com/kr/app/%EC%9D%BD%EC%9D%84%EA%B1%B0%EB%A6%AC/id6664069391)

<br>

## Introduction

**읽을거리**는 [도서관 정보나루](https://www.data4library.kr/)에서 제공하는 Open API를 활용하여 **사용자의 독서 취향에 맞는 도서를 추천**하고, **전국 도서관의 도서를 조회**하는 기능을 제공하는 iOS 독서 플랫폼 앱입니다.
이와 함께, **오픈톡 기능**을 통해 사용자 간의 자유로운 의견 교류를 지원하며, **독서 목표 설정 및 공유 기능**을 제공합니다.
<br>

#### 📍 개발 기간
- 기능 구현
  - 2024.07.18. ~ 2024.08.30.
- 유지 보수 및 업데이트
  - 2024.09.01. ~ 진행 중

#### 📍 비고
- 「**2024 도서관 데이터 활용 공모전**」 (국립중앙도서관 주관) - 서비스 아이디어 부문 출품작

<br>

## Team

### 팀 이음

📪Team contact: contact.ieumteam@gmail.com

| iOS Developer | iOS Developer | Server Developer | Server Developer |
| --- | --- | --- | --- |
| [@minnnidev](https://github.com/minnnidev) | [@rafa-e1](https://github.com/rafa-e1) | [@chanwoo7](https://github.com/chanwoo7) | [@hyeesw](https://github.com/hyeesw) |

<br>

## Server Developers

<table>
    <tr align="center">
        <td style="min-width: 130px;">
            <img src="https://github.com/chanwoo7.png" width="100">
        </td>
        <td style="min-width: 130px;">
          <img src="https://github.com/hyeesw.png" width="100">
        </td>
    </tr>
    <tr align="center">
        <td>
            <b>이찬우</b>
        </td>
        <td>
            <b>김혜은</b>
        </td>
    </tr>
    <tr align="center">
        <td>
            <a href="https://github.com/chanwoo7">
                <img src="https://img.shields.io/badge/chanwoo7-181717?style=for-the-social&logo=github&logoColor=white"/>
            </a>
        </td>
        <td>
            <a href="https://github.com/hyeesw">
                <img src="https://img.shields.io/badge/hyeesw-181717?style=for-the-social&logo=github&logoColor=white"/>
            </a>
        </td>
    </tr>
</table>

#### 📍 맡은 부분들
- 이찬우
  - JWT Access Token, Refresh Token 기반 **회원 인증** 및 Redis를 활용한 **토큰 재발급** 서비스 구현
  - Spring Data JPA, Spring Security를 활용한 **회원 정보 관리 서비스** 구현
  - OIDC(OpenID Connect)를 활용한 ID Token 기반 **카카오, Apple 로그인** 구현 
  - **커스텀 페이지네이션** 로직 구현 및 Open API 기반 **장르 도서 조회** API, **도서관 검색 및 저장** API 구현
  - **목표 CRUD** API 및 **목표 기록 추가** API 구현
  - **도메인 관리** 및 **SSL 인증서** 적용

- 김혜은
  - **AWS EC2 서버 배포** 및 Docker, Github-Actions, Portainer를 활용해 **서버 배포 자동화**
  - 정보나루 **Open API 통신 로직** 및 **JSON 파싱 인터페이스** 구축
  - Open API 기반 **커스텀 인기대출도서, 책 검색, 책 상세, 도서관 책 대출 여부** API 구현
  - STOMP 프로토콜 기반 **실시간 채팅 구현** (오픈톡)
  <!-- flyway 를 활용한 DB migration-->
<br>

## System Architecture

<table align="center">
    <tr align="center">
        <td>
            시스템 아키텍처
        </td>
    </tr>
    <tr align="center">
      <td>
        <img src="https://github.com/user-attachments/assets/1adfae6a-1816-45a1-af21-35f041b897cd" width="100%">
      </td>
    </tr>
  </table>
  <br>
  <table align="center">
    <tr align="center">
        <td>
            CI/CD 아키텍처
        </td>
    </tr>
    <tr align="center">
      <td>
        <img src="https://github.com/kookmin-sw/capstone-2024-17/assets/119438312/d632a295-e00e-4828-8bc3-711a066fb987" width="100%">
      </td>
    </tr>
</table>
<br>

## ERD

<img src="https://github.com/user-attachments/assets/fd91fd65-ef29-4ed6-a7e2-0ebd02f789dd" width="100%">

<br>

## Features

> 이 문단의 내용은 `Readables-iOS`(프론트엔드) 레포지토리의 [README](https://github.com/BOOK-TALK/Readables-iOS)에서 발췌한 내용입니다.

#### 1. 회원가입 및 로그인
   - 유저는 애플 로그인, 카카오 로그인 총 2개의 소셜 로그인을 사용할 수 있으며, 입력 폼에서 정보를 입력하면 회원 가입이 완료됩니다. (닉네임만 필수 입력)
   - 한번 로그인을 하면 자동 로그인이 활성화됩니다.
     
     | 애플 로그인 | 카카오 로그인 | 정보 등록 |
     | :---: | :---: |  :---:  |
     | <img src = "https://github.com/user-attachments/assets/d2188f15-4964-42f1-a76e-56a86c4df272" width = "200"> |  <img src = "https://github.com/user-attachments/assets/d14fd416-3ceb-4e5b-96fc-ff43b6dccd03" width = "200"> | <img src = "https://github.com/user-attachments/assets/7e758873-81d2-4792-b8ed-9847f41c890a" width = "200"> |

</br>

#### 2. 맞춤형 도서 추천
   - 홈화면에서 유저는 지난달 인기 키워드를 확인할 수 있습니다.
   - 이번주 인기 도서, 유저 나이대에서 인기 있는 도서, 대출 급상승 도서를 확인할 수 있습니다.
   - 정보 등록 시 나이를 입력하지 않았다면, 전체 나이대에 인기 있는 도서를 보여줍니다.
     
     | 기본 홈화면1 | 기본 홈화면2| 나이를 입력하지 않았을 때 |
      | :---: | :---: | :---: |
      | <img src = "https://github.com/user-attachments/assets/b90eebd9-0093-49d3-aa73-9ecf63abcf4d" width = "200"> | <img src = "https://github.com/user-attachments/assets/207907b6-1f0a-4c3b-bed8-e1a353677a60" width = "200"> | <img src = "https://github.com/user-attachments/assets/450c89ad-f79f-42f7-96f5-9f1c7cf48bc7" width = "200"> |

</br>

#### 3. 키워드 검색
   - 책 이름 또는 작가 이름으로 검색을 할 수 있습니다.
   - 키워드 토글을 켜서 정보나루에서 제공하는 키워드를 사용하여 검색할 수 있습니다.

     | 일반 검색 | 키워드 검색 |
      | :---: | :---: |
      | <img src = "https://github.com/user-attachments/assets/219e97b9-195d-4704-9715-ee60bf40179d" width = "200"> | <img src = "https://github.com/user-attachments/assets/8680b6ad-64ec-4015-844b-b1a4080ed42c" width = "200">|
     
</br>

#### 4. 내 도서관 등록
   - 도서관 목록을 검색하여 나만의 도서관을 최대 3개까지 등록할 수 있습니다.
   -  지역 - 세부 지역을 나누어 검색할 수 있고, 추가, 삭제가 가능합니다.
   - 내 도서관 등록은 추후 나올 도서 상세 정보에서 도서 대출 여부를 나타낼 때 사용됩니다.

      | 내 도서관 관리 | 내 도서관 삭제 | 도서관 등록 |
      | :---: | :---: |  :---: |
      | <img src = "https://github.com/user-attachments/assets/63834604-b37b-4787-873e-0a8890bc41de" width = "200"> |  <img src = "https://github.com/user-attachments/assets/30510ba8-831c-412d-a48a-af2db2d59fec" width = "200"> |  <img src = "https://github.com/user-attachments/assets/1c701c21-da37-4d20-99a4-d65ca782d121" width = "200"> |

</br>

#### 5. 목표 
   - 도서를 검색하고 해당 도서에 대한 목표를 세울 수 있습니다. </br>
     도서의 최종 페이지 수를 입력한 뒤, 하루에 읽은 양을 기록하면 읽을거리에서 차트 정보를 제공합니다.
   - 목표를 삭제하고 완료 처리할 수 있습니다.

     | 목표 추가 | 목표 탭 | 목표 기록 |
      | :---: | :---: |  :---: |
      | <img src = "https://github.com/user-attachments/assets/ccc7b39f-f70d-40d0-89f2-598003299bde" width = "200"> |  <img src = "https://github.com/user-attachments/assets/e6cfbcb5-2af4-4d65-8deb-7b12795baa7c" width = "200"> |  <img src = "https://github.com/user-attachments/assets/a1b97886-bf09-47b3-9a56-112fb432678e" width = "200"> 
     

</br>

#### 6. 오픈톡 커뮤니티
   - 관심있는 도서에 대해 자유롭게 오픈톡에서 대화할 수 있습니다.
   - 채팅 사이드 메뉴에서 해당 도서의 목표를 진행 중인 사람, 완료한 사람을 확인하여 같이 책 읽는 분위기를 만들고자 하였습니다.
   - 해당 도서에 대한 나의 목표 진행 여부에 따라 목표 진행도와 목표 추가가 나타나도록 처리되어 있습니다.

     | 오픈톡 진입 | 채팅 | 채팅 사이드 메뉴1 | 채팅 사이드 메뉴2 |
     | :---: | :---: |  :---:  |  :---:  |
     | <img src = "https://github.com/user-attachments/assets/77dce7b3-fb67-4302-ac28-0cc34d33087a" width = "200"> |  <img src = "https://github.com/user-attachments/assets/879c1975-c69b-4383-8928-61e55b0d6832" width = "200"> |  <img src = "https://github.com/user-attachments/assets/359cc2af-7b5b-4dbd-bb1c-6f5cb5734a38" width = "200"> | <img src = "https://github.com/user-attachments/assets/8bd74d2f-c43a-4c0e-a988-d80e47ac5fcc" width = "200"> 

</br>

#### 7. 장르별 검색
   - 카테고리 탭에서는 대주제를 선택하여, 기본적으로 이번주 인기 도서와 신작 도서를 확인할 수 있습니다.
   - 각 대주제에 대한 소주제를 선택할 수 있으며, 마찬가지로 인기 도서와 신작 도서를 확인할 수 있습니다.
   - 전체보기를 통해 각 소주제에 대한 일주일 인기순, 한달 인기순, 신작순, 랜덤순 필터로 도서를 확인할 수 있습니다.
    
      | 카테고리 진입 | 인기 도서, 신작 도서 | 소주제 선택 | 필터 이용한 전체 보기 |
      | :---: | :---: |  :---:  |  :---: |
      | <img src = "https://github.com/user-attachments/assets/c49ffb68-7481-499d-91b0-aed4af967901" width = "200"> |  <img src = "https://github.com/user-attachments/assets/83d1e14a-b208-4b6e-9e9e-d63764aff833" width = "200"> |  <img src = "https://github.com/user-attachments/assets/823bcf0c-3e10-4dba-ad7c-287a57eb8722" width = "200"> | <img src = "https://github.com/user-attachments/assets/1d420f39-de01-44f2-ac7e-72d354fab937" width = "200"> |
  

</br>

#### 8. 도서 상세 정보 조회
   - 도서 상세 정보에서는 기본적인 도서 정보를 확인할수 있습니다.
   - 상세 조회 페이지에서 해당 책에 대한 오픈톡에 참여할 수 있습니다.
   - 등록한 내 도서관에서 대출이 가능한지 여부에 따라 대출 가능, 대출 불가능을 표시합니다.
         
      | 도서 상세 정보(내 도서관 등록 안 한 경우) | 대출 불가일 때 | 대출 가능할 때 |
      | :---: | :---: |  :---: |
      | <img src = "https://github.com/user-attachments/assets/a999baf9-acc1-48f1-93c1-9cbe6ce3786f" width = "200"> |  <img src = "https://github.com/user-attachments/assets/8bef9d9c-91e3-4f2a-b7e6-c5c1af5829cd" width = "200"> |  <img src = "https://github.com/user-attachments/assets/e78c900c-83b3-428c-bf30-7bf21e844fbe" width = "200"> |

</br>

#### 9. 회원 설정
   - 로그인, 로그아웃이 가능합니다. 유저에게 한번 더 의사를 물어보도록 구현하였습니다.

      | 설정 | 로그아웃 | 탈퇴하기 |
      | :---: | :---: |  :---: |
      | <img src = "https://github.com/user-attachments/assets/0031223d-6361-41a1-9ebe-9573b68c5122" width = "200"> |  <img src = "https://github.com/user-attachments/assets/9d0addf5-b3a6-4c4c-84d3-c1d86aac69ce" width = "200"> |  <img src = "https://github.com/user-attachments/assets/0318acb8-d4d9-43ef-9e44-390ffa9e3011" width = "200"> | 

</br>

## Directory Structure

```
src/main/
├── java
│   └── com
│       └── book
│           └── backend
│               ├── domain
│               │   ├── auth
│               │   ├── book
│               │   ├── detail
│               │   ├── genre
│               │   ├── goal
│               │   ├── library
│               │   ├── message
│               │   ├── oidc
│               │   ├── openapi
│               │   ├── opentalk
│               │   ├── record
│               │   ├── search
│               │   ├── user
│               │   ├── userBook
│               │   └── userOpentalk
│               ├── exception
│               ├── global
│               └── util
└── resources
    ├── sql
    └── static
```

