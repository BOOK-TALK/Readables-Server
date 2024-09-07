# &nbsp; 읽을거리 <a href="https://apps.apple.com/kr/app/%EC%9D%BD%EC%9D%84%EA%B1%B0%EB%A6%AC/id6664069391"><img src="https://github.com/BOOK-TALK/Readables-Server/blob/main/src/main/resources/static/logo.png" align="left" width="100"></a>
[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2FBOOK-TALK%2FReadables-Server&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

<br>

## Download

- #### [App Store Download](https://apps.apple.com/kr/app/%EC%9D%BD%EC%9D%84%EA%B1%B0%EB%A6%AC/id6664069391)

<br>

## Introduction

**읽을거리**는 [정보나루](https://www.data4library.kr/)에서 제공하는 Open API를 활용하여 **사용자의 독서 취향에 맞는 도서를 추천**하고, **전국 도서관의 도서를 조회**하는 기능을 제공하는 iOS 독서 플랫폼 앱입니다.
이와 함께, **오픈톡 기능**을 통해 사용자 간의 자유로운 의견 교류를 지원하며, **독서 목표 설정 및 공유 기능**을 제공합니다.
<br>

#### 📍 개발 기간
- 2024.07.18. ~ 진행 중

#### 📍 비고
- 「**2024 도서관 데이터 활용 공모전**」 (국립중앙도서관 주관) - 서비스 아이디어 부문 출품작 (현재 심사 중)

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
  - **커스텀 페이지네이션** 구현 및 Open API 기반 **'장르'** 관련 API, **'도서관 검색 및 저장'** API 구현
  - **목표 CRUD** API 및 **기록 추가** API 구현
  - **도메인 관리** 및 **SSL 인증서** 적용

- 김혜은
  - **AWS EC2 서버 배포** 및 Docker, Github-Actions, Portainer를 활용해 **서버 배포 자동화**
  - 정보나루 **Open API 통신 로직**과 **JSON 파싱 인터페이스** 구축
  - Open API 기반 **'커스텀 인기대출도서', '책 검색', '책 상세', '도서관 책 대출 여부' API** 구현
  - STOMP 프로토콜 기반 **실시간 채팅 구현** (오픈톡)

<br>

## System Architecture

TBU

<br>

## ERD

<img src="https://github.com/user-attachments/assets/fd91fd65-ef29-4ed6-a7e2-0ebd02f789dd" width="70%">
<br>

## Features

TBU

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

