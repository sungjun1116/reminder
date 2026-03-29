# PRD: Apple Reminder Web App

## 1. 개요

Apple Reminder App의 핵심 기능과 UI/UX를 웹에서 최대한 동일하게 재현하는 서비스.

- **Backend**: Spring Boot 4.0.5 (Java 25, JPA/H2)
- **Frontend**: Next.js (App Router)
- **디자인 레퍼런스**: macOS/iOS Apple Reminders (최신)

---

## 2. UI/UX 디자인 스펙

### 2.1 전체 레이아웃

```
┌─────────────────────┬─────────────────────────────────┬──────────────────────┐
│      사이드바         │          메인 콘텐츠               │     상세 패널         │
│   (260px, 고정)      │        (flex-grow)               │  (320px, 슬라이드인)  │
└─────────────────────┴─────────────────────────────────┴──────────────────────┘
```

- 전체 배경: macOS frosted glass 느낌, `bg-[#F2F2F7]` (라이트) / `bg-[#1C1C1E]` (다크)
- 사이드바 배경: `bg-white/80` + `backdrop-blur` (라이트) / `bg-[#2C2C2E]/80` (다크)
- 다크 모드: 지원 (시스템 설정 연동)

### 2.2 사이드바

**상단 — 스마트 목록 카드 그리드 (2×2)**

Apple Reminder와 동일하게 4개 카드를 2열 그리드로 배치:

```
┌──────────────┬──────────────┐
│  🔵 오늘      │  📅 예정      │
│           3  │           7  │
├──────────────┬──────────────┤
│  📋 전체      │  🚩 깃발 표시 │
│          12  │           2  │
└──────────────┴──────────────┘
```

- 카드 크기: `~120px × 80px`, 둥근 모서리 `rounded-xl`
- 각 카드 배경색 (Apple 색상 사용):
  - 오늘: 파랑 `#007AFF`
  - 예정: 빨강 `#FF3B30`
  - 전체: 회색 `#8E8E93`
  - 깃발 표시: 주황 `#FF9500`
- 카드 텍스트: 흰색, 제목 `text-sm font-medium`, 숫자 `text-2xl font-bold` (우하단 정렬)
- 선택 시: 약간 어두워지는 pressed 효과

**하단 — 내 목록**

```
내 목록                    편집
────────────────────────────
● 장보기                   2 >
● 업무                     5 >
● 개인                       >
────────────────────────────
+ 목록 추가
```

- 목록 아이콘: 채워진 원 (list color), 그 안에 이모지/아이콘
- 항목 높이: `44px` (iOS HIG 기준)
- 미완료 수: 우측 회색 숫자 + `>`
- `+ 목록 추가`: 파랑 텍스트 버튼, 하단 고정
- 목록 그룹: 그룹명은 회색 섹션 헤더 (대문자, `text-xs tracking-wider`)

### 2.3 메인 콘텐츠 영역

**헤더**
```
[목록 색상 대형 제목]                    ...  ⊕
업무
────────────────
```
- 목록 제목: `text-3xl font-bold`, 목록 컬러 적용
- 우측: `...` (더보기 메뉴), `⊕` (미리 알림 추가)

**미리 알림 목록**

각 항목:
```
○  미리 알림 제목                           ⚑  >
   2026. 3. 29.  ·  메모 첫줄...
```

- 체크박스: 원형, 목록 컬러로 테두리. 완료 시 컬러로 채워지고 체크마크 표시
- 완료 애니메이션: 체크 → 0.3s 후 취소선 → 0.5s 후 목록에서 사라짐 (slide-up)
- 우선순위 표시: 제목 앞에 `!` (낮음), `!!` (보통), `!!!` (높음) — 빨간색
- 깃발: 제목 우측에 🚩 오렌지 아이콘
- 날짜/메모: 제목 하단에 회색 `text-sm` (오늘 이전 날짜는 빨간색)
- 하위 항목: 좌측 16px indent, 동일한 구조
- 완료된 항목: "완료됨" 섹션으로 하단 분리 (기본 접힘, 클릭으로 펼치기)
- 항목 선택: 배경 살짝 하이라이트

**항목 추가**
```
○  새로운 미리 알림
```
- 목록 하단의 빈 인라인 입력 영역 (Apple과 동일)
- 또는 `⊕` 버튼 클릭 시 인라인 입력 활성화
- Enter: 다음 항목 추가
- Tab: 하위 항목으로 변환
- Escape: 취소

### 2.4 상세 패널 (Detail Panel)

항목 클릭/선택 시 우측에서 슬라이드인. 메인 콘텐츠를 밀지 않고 오버레이하지 않음 — 별도 패널로 분리.

```
┌─────────────────────────────┐
│  ✕  세부 정보                │
│─────────────────────────────│
│  제목                        │
│  ┌─────────────────────────┐│
│  │ 업무 보고서 작성          ││
│  └─────────────────────────┘│
│                              │
│  메모                        │
│  ┌─────────────────────────┐│
│  │                         ││
│  └─────────────────────────┘│
│                              │
│  ─────── 세부 정보 ─────────  │
│  📅 날짜     2026. 3. 29. ›  │
│  🕐 시간     오전 9:00     ›  │
│  🚩 깃발 표시         ○      │
│  ⚡ 우선순위       없음    ›  │
│  🔗 URL                   ›  │
│  # 태그                   ›  │
│  📋 목록          업무    ›  │
│                              │
│  ─────── 하위 항목 ─────────  │
│  ○  하위 항목 1              │
│  + 항목 추가                 │
└─────────────────────────────┘
```

- 각 행: 좌측 아이콘 + 레이블, 우측 값 + `>` (탭 시 편집)
- 날짜/시간: 네이티브 date picker 대신 커스텀 캘린더 팝오버 (Apple 스타일)
- 우선순위: 팝오버로 `없음 / ! 낮음 / !! 보통 / !!! 높음` 선택
- 목록 이동: 팝오버로 목록 선택

### 2.5 색상 팔레트 (목록 컬러 선택)

Apple Reminders와 동일한 17가지 색상:

| 이름 | Hex |
|------|-----|
| 빨강 | `#FF3B30` |
| 주황 | `#FF9500` |
| 노랑 | `#FFCC00` |
| 초록 | `#34C759` |
| 민트 | `#00C7BE` |
| 청록 | `#32ADE6` |
| 파랑 | `#007AFF` |
| 남색 | `#5856D6` |
| 보라 | `#AF52DE` |
| 분홍 | `#FF2D55` |
| 갈색 | `#A2845E` |
| 회색 | `#8E8E93` |
| 라이트그레이 | `#C7C7CC` |
| 진빨강 | `#D70015` |
| 진주황 | `#C93400` |
| 진초록 | `#248A3D` |
| 진파랑 | `#0040DD` |

### 2.6 목록 아이콘

웹에서는 SF Symbol 대신 **Lucide React** 아이콘 사용 (SF Symbol과 유사한 스타일):

- 기본 목록: `list-todo`
- 장보기: `shopping-cart`
- 업무: `briefcase`
- 개인: `user`
- 그 외: 사용자가 이모지로도 설정 가능

### 2.7 타이포그래피

- 폰트: `font-family: -apple-system, BlinkMacSystemFont, 'SF Pro Text', sans-serif`
- 목록 제목 (헤더): `text-3xl font-bold`
- 스마트 목록 카드 숫자: `text-2xl font-bold`
- 항목 제목: `text-[15px]`
- 서브텍스트 (날짜/메모): `text-[13px] text-gray-500`
- 완료된 항목: `line-through text-gray-400`

### 2.8 애니메이션 / 트랜지션

| 인터랙션 | 애니메이션 |
|---------|----------|
| 체크박스 클릭 | 원이 컬러로 채워짐 (0.2s ease) |
| 항목 완료 | 취소선 등장 → 0.5s 후 위로 슬라이드 사라짐 |
| 상세 패널 열기 | 우측에서 슬라이드인 (0.25s ease-out) |
| 목록 선택 | 메인 영역 페이드 트랜지션 (0.2s) |
| 완료된 항목 섹션 | 아코디언 펼치기/접기 (0.3s) |
| 항목 생성 | 아래서 슬라이드 업 (0.2s) |

---

## 3. 핵심 기능 범위

### 3.1 스마트 목록
| 목록 | 조건 |
|------|------|
| 오늘 | `dueDate = today` AND `completed = false` |
| 예정 | `dueDate IS NOT NULL` AND `completed = false` (날짜순 정렬) |
| 전체 | `completed = false` (전체 목록) |
| 깃발 표시 | `flagged = true` AND `completed = false` |
| 완료 | `completed = true` |

### 3.2 목록 (Lists)
- 사용자 정의 목록 생성 / 수정 / 삭제
- 색상 17종 + 아이콘 선택
- 목록 그룹 생성 및 목록 묶기
- 미완료 항목 수 배지

### 3.3 미리 알림
| 필드 | 타입 | 비고 |
|------|------|------|
| 제목 | String | 필수 |
| 메모 | String | 멀티라인 |
| 마감 날짜 | LocalDate | nullable |
| 마감 시간 | LocalTime | nullable, 날짜 설정 시 활성화 |
| 우선순위 | Enum | NONE / LOW / MEDIUM / HIGH |
| 깃발 표시 | Boolean | |
| 하위 항목 | List<Reminder> | parentId 참조, 1단계만 |
| URL | String | nullable |
| 태그 | List<Tag> | |

### 3.4 키보드 단축키
| 단축키 | 동작 |
|--------|------|
| Enter | 같은 레벨 다음 항목 추가 |
| Tab | 하위 항목으로 변환 |
| Shift+Tab | 상위 항목으로 복귀 |
| Escape | 편집 취소 |
| Space | 선택된 항목 완료 토글 |
| Cmd+Z | 실행 취소 |

---

## 4. API 설계 (Backend)

### Lists
| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/lists` | 전체 목록 + 그룹 조회 |
| POST | `/api/lists` | 목록 생성 |
| PUT | `/api/lists/{id}` | 목록 수정 |
| DELETE | `/api/lists/{id}` | 목록 삭제 |
| PATCH | `/api/lists/reorder` | 순서 변경 |

### List Groups
| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/list-groups` | 그룹 조회 |
| POST | `/api/list-groups` | 그룹 생성 |
| PUT | `/api/list-groups/{id}` | 그룹 수정 |
| DELETE | `/api/list-groups/{id}` | 그룹 삭제 |

### Reminders
| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/reminders?filter=today\|scheduled\|all\|flagged\|completed` | 스마트 목록 |
| GET | `/api/lists/{listId}/reminders` | 목록별 미리 알림 |
| POST | `/api/lists/{listId}/reminders` | 미리 알림 생성 |
| PUT | `/api/reminders/{id}` | 미리 알림 전체 수정 |
| PATCH | `/api/reminders/{id}/complete` | 완료 토글 |
| PATCH | `/api/reminders/{id}/flag` | 깃발 토글 |
| DELETE | `/api/reminders/{id}` | 삭제 |
| PATCH | `/api/reminders/{id}/move` | 목록 이동 |
| PATCH | `/api/reminders/reorder` | 순서 변경 |

### Search & Tags
| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/reminders/search?q=` | 검색 |
| GET | `/api/tags` | 전체 태그 |
| DELETE | `/api/tags/{id}` | 태그 삭제 |

---

## 5. 데이터 모델

```
ListGroup
  - id: Long
  - name: String
  - sortOrder: Integer

ReminderList
  - id: Long
  - name: String
  - color: String (hex, e.g. "#007AFF")
  - icon: String (lucide icon name or emoji)
  - groupId: Long (nullable)
  - sortOrder: Integer
  - createdAt: LocalDateTime

Reminder
  - id: Long
  - listId: Long
  - parentId: Long (nullable)
  - title: String
  - notes: String (nullable)
  - dueDate: LocalDate (nullable)
  - dueTime: LocalTime (nullable)
  - priority: Enum (NONE, LOW, MEDIUM, HIGH)
  - flagged: Boolean (default: false)
  - completed: Boolean (default: false)
  - completedAt: LocalDateTime (nullable)
  - url: String (nullable)
  - sortOrder: Integer
  - createdAt: LocalDateTime
  - updatedAt: LocalDateTime

Tag
  - id: Long
  - name: String

ReminderTag
  - reminderId: Long
  - tagId: Long
```

---

## 6. 기술 스택

### Backend
- Spring Boot 4.0.5 / Java 25
- Spring Data JPA
- H2 Database (file-based)
- Lombok
- Spring Validation

### Frontend
- Next.js (App Router) + TypeScript
- Tailwind CSS v4
- Lucide React (아이콘)
- TanStack Query v5 (서버 상태 + 낙관적 업데이트)
- Zustand (UI 상태 — 선택된 목록/항목, 패널 열림 등)
- dnd-kit (드래그 앤 드롭)
- date-fns (날짜 포맷)

---

## 7. 개발 우선순위

### Phase 1 — 핵심 기능
- [ ] 레이아웃 (사이드바 + 메인 + 상세패널 구조)
- [ ] 스마트 목록 카드 그리드 (오늘/예정/전체/깃발)
- [ ] 목록 CRUD + 색상/아이콘 선택
- [ ] 미리 알림 CRUD + 인라인 추가
- [ ] 완료 체크/해제 (애니메이션 포함)

### Phase 2 — 상세 기능
- [ ] 상세 패널 (날짜, 시간, 우선순위, URL, 메모)
- [ ] 깃발 표시, 우선순위 표시
- [ ] 하위 항목
- [ ] 태그

### Phase 3 — 완성도
- [ ] 드래그 앤 드롭 (항목 재정렬, 목록 간 이동)
- [ ] 목록 그룹
- [ ] 검색
- [ ] 키보드 단축키
- [ ] 다크 모드
