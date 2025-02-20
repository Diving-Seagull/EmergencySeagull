# 🚒 Emergency_911
![메인 이미지](/asset/main.png)
## 소방 업무 효율화를 위한 인공지능 통합 신고 처리 시스템

**Emergency_911**은 소방 업무 효율화를 위한 인공지능 통합 신고 처리 시스템입니다. 사용자의 음성 및 텍스트 신고를 AI(Whisper, GPT-3.5 Turbo)을 통해 분석하여 사건의 카테고리를 분류하며, 엘라스틱서치를 활용해 중복 신고를 판별합니다. 이를 통해 적은 인력으로도 신속하고 정확한 신고 처리를 바탕으로 부산 시민 안전을 효과적으로 확보할 수 있도록 설계한 서비스입니다.

## 서비스 시연 영상
<p align="center">
  <a href="https://www.youtube.com/shorts/NKZJTZo1xJk">
    <img src="/asset/thumbnail.png" width="20%" alt="서비스 시연 영상">
  </a>
</p>

## 기획 배경
부산은 자연재해로 인한 인명 피해가 가장 큰 지역 중 하나로, 지난 10년간 약 1,800억 원의 피해액을 기록하며 안전 문제가 심각하게 대두되고 있습니다. 특히, 부산은 소방 설비와 인력 부족으로 인해 지역 안전지수가 최하등급을 기록하고 있으며, 이러한 문제는 재난 상황에서 더욱 두드러집니다.

예를 들어, 2020년 제9호 태풍 마이삭 당시 부산 소방본부는 1시간 만에 약 3,400건의 신고를 접수했지만, 인력 부족으로 인해 다수의 신고가 ARS로 전환되는 문제가 발생했습니다. 또한, 일평균 신고 중 약 37%가 중복 사건으로 확인되며, 이는 소방 자원의 비효율적 운영을 초래하고 있습니다.

현재 시범 운영 중인 여러 소방 관련 서비스(119 신고 앱, 음성인식 지능형 콜백 시스템 등) 역시 접수 단계에서의 한계를 극복하지 못하고 있으며, 상용화되지 못한 사례가 많습니다. 이에 따라, 재난 상황에서도 효율적인 신고 처리와 소방 인력의 부담을 줄일 수 있는 새로운 시스템의 필요성이 절실히 요구되고 있습니다.

이러한 문제를 해결하기 위해 **Emergency_911**은 AI를 활용한 통합 신고 처리 시스템을 기획했습니다. 이 시스템은 적은 인력으로도 효율적인 신고 처리가 가능하도록 설계되었으며, 부산시민의 안전을 보다 효과적으로 확보하기 위한 접근법을 제안합니다.

## 서비스 소개
![소개 이미지1](/asset/intro1.png)
![소개 이미지2](/asset/intro2.png)
![소개 이미지3](/asset/intro3.png)
![소개 이미지4](/asset/intro4.png)
![소개 이미지5](/asset/intro7.png)
![소개 이미지6](/asset/intro8.png)
![소개 이미지7](/asset/intro9.png)
![소개 이미지8](/asset/intro10.png)
![소개 이미지9](/asset/intro11.png)

## 추후 개발 계획
![병상 연계 시스템1](/asset/intro13.png)
![병상 연계 시스템2](/asset/intro14.png)
![교통 연계 시스템](/asset/intro15.png)

## 기술 및 아키텍처
**기술 스택**
- **Design**: Figma, Adobe Illustrator, Adobe Photoshop, Rotato
- **Frontend**: Kotlin
- **Backend**: Java, Spring Boot, H2, Elasticsearch 
- **Deploy**: AWS, Docker

**사용된 API 및 서비스**
- Kakao Map API
- Google Geocoding API
- Open AI API (Whisper, GPT-3.5 Turbo))

**서비스 아키텍처**
![시스템아키텍쳐](/asset/시스템아키텍쳐.png)

**플로우 차트**
<p align="center">
  <img src="/asset/플로우차트.png" width="50%" alt="플로우차트">
</p>

[API 명세서](https://documenter.getpostman.com/view/34763300/2sAYXFhx6w)


## 다이빙갈매기 🦅

### 팀원 소개
<div align="center">
  <table>
    <tr>
      <td align="center" width="33%">
        <img src="https://avatars.githubusercontent.com/u/55781137?v=4" width="100" height="100"><br>
        <a href="https://github.com/J-1ac">이상준</a><br>
        BE
      </td>
      <td align="center" width="33%">
        <img src="https://avatars.githubusercontent.com/u/182388479?v=4" width="100" height="100"><br>
        <a href="https://github.com/LeeJuAe124">이주애</a><br>
        Design
      </td>
      <td align="center" width="33%">
        <img src="https://avatars.githubusercontent.com/u/77396909?v=4" width="100" height="100"><br>
        <a href="https://github.com/gykim22">김기윤</a><br>
        FE
      </td>
    </tr>
  </table>
</div>

--- 
MIT License 

Copyright (c) 2025 Diving-Seagull