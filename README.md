# 학습 관리 시스템(Learning Management System)
## 진행 방법
* 학습 관리 시스템의 수강신청 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 수강신청 2단계 요구사항
- [x] `Course`는 기수 단위로 운영되며, 여러 개의 `Session`을 가질 수 있다.
  - [x] 해당 비즈니스 객체가 응집된 `Sessions` 일급 컬렉션을 만든다.
- [x] `Session`은 `startDate` 와 `endDate` 를 가진다.
- [x] `Session`은 `CoverImage` 라는 강의 커버 이미지 정보를 가진다.
  - [x] `CoverImage`의 용량은 1MB 이하여야 한다.
  - [x] `CoverImage`의 확장자는 `gif, jpg (jpeg), png, svg` 이어야 한다.
  - [x] `CoverImage`는 `width`가 300px 이상, `height`는 200px 이상이어야 하며, `width와 height의 비율`은 3:2 여야 한다.
- [ ] `Session`은 `FreeSession(무료강의)`과 `PaidSession(유료강의)`으로 나뉜다.
  - [ ] `FreeSession`은 최대 수강 인원 제한이 없다. 하지만 `PaidSession`은 제한이 있다.
  - [ ] `PaidSession`은 수강생이 결체한 금액과 수강료가 일치할 때만, 수강신청이 가능하다.
  - [ ] 이외의 비즈니스 로직은 차이나는 부분보다는 동일한 부분이 더 많으므로 `추상클래스`의 적용을 고려한다.
- [ ] `Session`은 `SessionStatus`를 가진다.
   - [ ] `SessionStatus`는 `PREPAREING, RECRUITING, CLOSED` 세 가지 상태를 가진다.
   - [ ] 수강신청은 상태가 `RECRUITING` 일 때만 가능하다.