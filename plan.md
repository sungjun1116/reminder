# Development Plan: Apple Reminder Web App

## 기술 스택

### Backend
| 기술 | 버전 | 역할 |
|------|------|------|
| Java | 25 | 언어 |
| Spring Boot | 4.0.5 | 애플리케이션 프레임워크 |
| Spring Data JPA | (Boot 관리) | ORM / Repository 패턴 |
| Hibernate | (Boot 관리) | JPA 구현체 |
| H2 Database | (Boot 관리) | 내장 DB (file-based) |
| Lombok | latest | 보일러플레이트 제거 |
| Spring Validation | (Boot 관리) | 입력값 검증 (`@Valid`) |
| Spring Web MVC | (Boot 관리) | REST API (`@RestController`) |

**프로젝트 구조 (패키지: `sungjun.ai.reminder`)**
```
reminder/
└── src/main/java/sungjun/ai/reminder/
    ├── ReminderApplication.java
    ├── domain/
    │   ├── list/           # ReminderList, ListGroup 도메인
    │   └── reminder/       # Reminder, Tag 도메인
    ├── api/
    │   ├── list/           # ListController, ListService, ListRepository
    │   └── reminder/       # ReminderController, ReminderService, ReminderRepository
    └── common/
        └── exception/      # GlobalExceptionHandler
```

**H2 설정 (`application.properties`)**
```properties
spring.datasource.url=jdbc:h2:file:./data/reminder
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
```

---

### Frontend
| 기술 | 버전 | 역할 |
|------|------|------|
| Next.js | latest (App Router) | 프레임워크 |
| TypeScript | 5.x | 언어 |
| Tailwind CSS | v4 | 스타일링 |
| Lucide React | latest | 아이콘 (SF Symbol 대체) |
| TanStack Query | v5 | 서버 상태 관리 + 낙관적 업데이트 |
| Zustand | latest | 클라이언트 UI 상태 |
| dnd-kit | latest | 드래그 앤 드롭 |
| date-fns | latest | 날짜 포맷/계산 |

**프로젝트 구조 (Next.js App Router)**
```
frontend/
├── app/
│   ├── layout.tsx          # 전체 레이아웃 (사이드바 + 메인)
│   ├── page.tsx            # 기본 리다이렉트 → /list/today
│   └── list/
│       └── [id]/
│           └── page.tsx    # 목록/스마트목록 뷰
├── components/
│   ├── sidebar/
│   │   ├── SmartListGrid.tsx
│   │   ├── ListItem.tsx
│   │   └── AddListButton.tsx
│   ├── reminder/
│   │   ├── ReminderList.tsx
│   │   ├── ReminderItem.tsx
│   │   ├── ReminderCheckbox.tsx
│   │   ├── ReminderInlineInput.tsx
│   │   └── CompletedSection.tsx
│   └── detail/
│       └── DetailPanel.tsx
├── lib/
│   ├── api.ts              # fetch wrapper
│   └── types.ts            # TypeScript 타입 정의
└── store/
    └── uiStore.ts          # Zustand: 선택된 항목, 패널 상태
```

---

## Phase 1 — 뼈대 (Skeleton)

> 목표: 프로젝트 실행 가능한 기본 구조 구축

### Backend
- [ ] `application.properties` 설정 (H2 file-based, CORS, h2-console)
- [ ] `ReminderList` 엔티티 + `ListRepository` + `ListService` + `ListController`
  - `GET /api/lists`, `POST /api/lists`, `PUT /api/lists/{id}`, `DELETE /api/lists/{id}`
- [ ] `Reminder` 엔티티 + `ReminderRepository` + `ReminderService` + `ReminderController`
  - `GET /api/lists/{listId}/reminders`, `POST /api/lists/{listId}/reminders`
  - `PUT /api/reminders/{id}`, `DELETE /api/reminders/{id}`
- [ ] `GlobalExceptionHandler` (404, 400 공통 응답)
- [ ] 샘플 데이터 초기화 (`data.sql` 또는 `CommandLineRunner`)

### Frontend
- [ ] Next.js 프로젝트 생성 (`create-next-app`, TypeScript, Tailwind v4)
- [ ] 전체 레이아웃: 사이드바(260px) + 메인(flex-grow) 3열 구조
- [ ] 사이드바: 스마트 목록 카드 2×2 그리드 (정적 UI)
- [ ] 사이드바: 내 목록 리스트 (API 연동)
- [ ] 메인: 선택된 목록의 미리 알림 목록 표시 (API 연동)
- [ ] `lib/api.ts`: fetch wrapper (baseURL, error handling)
- [ ] `lib/types.ts`: `ReminderList`, `Reminder` 타입 정의

**완료 기준**: 브라우저에서 목록 조회/생성, 미리 알림 조회/생성 동작 확인

---

## Phase 2 — 핵심 CRUD + 완료 기능

> 목표: 미리 알림의 기본 라이프사이클 완성

### Backend
- [ ] `PATCH /api/reminders/{id}/complete` — 완료 토글
- [ ] `PATCH /api/reminders/{id}/flag` — 깃발 토글
- [ ] 스마트 목록 쿼리: `GET /api/reminders?filter=today|scheduled|all|flagged|completed`
  - JPQL 또는 QueryDSL로 조건별 쿼리 구현
- [ ] `sortOrder` 필드 — 생성 순서 유지

### Frontend
- [ ] `ReminderCheckbox`: 원형 체크박스, 목록 컬러 적용
- [ ] 완료 애니메이션: 체크 → 취소선(0.3s) → slide-up 사라짐(0.5s)
- [ ] 완료된 항목 섹션: 하단 분리, 아코디언 접기/펼치기
- [ ] 인라인 항목 추가 (`⊕` 버튼 → 인라인 input)
- [ ] 인라인 편집 (항목 제목 클릭 → 즉시 편집 가능)
- [ ] 스마트 목록 카드 클릭 → 필터된 목록 표시
- [ ] TanStack Query 적용: `useQuery`, `useMutation` + 낙관적 업데이트
- [ ] Zustand 스토어: 선택된 목록 ID, 상세 패널 열림 상태

**완료 기준**: 미리 알림 추가/편집/삭제/완료 체크가 자연스럽게 동작. 스마트 목록 필터 동작.

---

## Phase 3 — 상세 패널 + 필드 확장

> 목표: Apple Reminders의 상세 정보 패널 구현

### Backend
- [ ] `Reminder` 엔티티 필드 추가: `dueDate`, `dueTime`, `priority`, `notes`, `url`
- [ ] `PUT /api/reminders/{id}` — 전체 필드 업데이트 지원
- [ ] `PATCH /api/reminders/{id}/move` — 다른 목록으로 이동
- [ ] `ListGroup` 엔티티 + `GET/POST/PUT/DELETE /api/list-groups`
- [ ] `ReminderList`에 `color`, `icon` 필드 추가

### Frontend
- [ ] `DetailPanel` 컴포넌트: 우측 슬라이드인 패널
  - 제목, 메모 편집
  - 날짜/시간 커스텀 picker (팝오버)
  - 우선순위 선택 팝오버
  - 깃발 토글, URL 입력
  - 목록 이동 선택
- [ ] 목록 생성 모달: 이름 + 색상(17종) + 아이콘 선택
- [ ] 미리 알림 항목에 날짜, 우선순위(`!`, `!!`, `!!!`), 깃발 아이콘 표시
- [ ] 날짜 지난 항목 빨간색 표시
- [ ] 목록 그룹 섹션 헤더 표시
- [ ] Apple 색상 팔레트 컴포넌트

**완료 기준**: 상세 패널에서 모든 필드 편집 가능. 목록 색상/아이콘 설정 동작.

---

## Phase 4 — 하위 항목 + 태그

> 목표: 계층 구조와 태깅 시스템 구현

### Backend
- [ ] `Reminder.parentId` 필드로 하위 항목 지원
- [ ] 목록 조회 시 하위 항목 포함 응답 (`children: List<Reminder>`)
- [ ] `Tag` 엔티티, `ReminderTag` 연관, `TagRepository`
- [ ] `GET /api/tags`, `DELETE /api/tags/{id}`
- [ ] 미리 알림 생성/수정 시 태그 처리

### Frontend
- [ ] `ReminderItem` 하위 항목 렌더링 (16px indent)
- [ ] Tab 키: 하위 항목 변환 / Shift+Tab: 상위 복귀
- [ ] 태그 입력 컴포넌트 (multi-select, 자동완성)
- [ ] 태그 칩 표시 (항목 상세 패널)
- [ ] 태그 필터링 (사이드바 태그 섹션)

**완료 기준**: 하위 항목 생성/관리 동작. 태그 추가/제거/필터 동작.

---

## Phase 5 — 드래그 앤 드롭 + 검색

> 목표: 고급 인터랙션 완성

### Backend
- [ ] `PATCH /api/reminders/reorder` — 순서 일괄 업데이트 (sortOrder 배열)
- [ ] `PATCH /api/lists/reorder` — 목록 순서 일괄 업데이트
- [ ] `GET /api/reminders/search?q=` — 제목 + 메모 전문 검색 (JPA `LIKE` 또는 H2 전문검색)

### Frontend
- [ ] dnd-kit 적용: 미리 알림 항목 드래그 재정렬
- [ ] dnd-kit: 목록 간 항목 이동 (사이드바 목록으로 드래그)
- [ ] 검색 UI: 상단 검색바, 실시간 검색 (debounce 300ms)
- [ ] 검색 결과: 목록명 그룹핑하여 표시
- [ ] 목록 드래그 재정렬 (사이드바)

**완료 기준**: 드래그로 항목 순서 변경 및 목록 간 이동. 검색 동작.

---

## Phase 6 — 다크 모드 + 키보드 + 마감

> 목표: 완성도 및 Apple 감성 극대화

### Frontend
- [ ] 다크 모드: Tailwind `dark:` 클래스, 시스템 설정 연동 (`prefers-color-scheme`)
- [ ] 키보드 단축키 전체 구현
  - `Enter`: 다음 항목 추가
  - `Tab` / `Shift+Tab`: 하위/상위 변환
  - `Escape`: 편집 취소
  - `Space`: 완료 토글 (항목 선택 시)
  - `Cmd+Z`: 실행 취소 (완료 취소)
- [ ] macOS 시스템 폰트: `-apple-system, BlinkMacSystemFont, SF Pro Text`
- [ ] 전체 애니메이션 polish:
  - 목록 전환 fade
  - 패널 슬라이드인
  - 체크 완료 시퀀스
  - hover/active 상태 미세 피드백
- [ ] 빈 상태(empty state) UI: 목록이 비었을 때 안내 문구

**완료 기준**: 다크 모드 전환, 키보드만으로 미리 알림 생성/완료 가능. 전체 앱 Apple 감성 완성.

---

## 개발 환경 설정

```
# Backend 실행 (포트 8080)
cd reminder
./gradlew bootRun

# Frontend 실행 (포트 3000)
cd frontend
npm run dev

# H2 콘솔
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:file:./data/reminder
```

## API Base URL
- 개발: `http://localhost:8080/api`
- Next.js에서 `NEXT_PUBLIC_API_BASE_URL` 환경변수로 관리
