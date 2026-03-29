# Tasks

## Phase 1 — 뼈대 (Skeleton)

### Backend
- [ ] `application.properties` 설정
  - [ ] H2 file-based datasource (`jdbc:h2:file:./data/reminder`)
  - [ ] H2 console 활성화 (`spring.h2.console.enabled=true`)
  - [ ] JPA ddl-auto `update` 설정
  - [ ] CORS 설정 (`localhost:3000` 허용)
- [ ] `ReminderList` 도메인
  - [ ] `ReminderList` 엔티티 (`id`, `name`, `sortOrder`, `createdAt`)
  - [ ] `ReminderListRepository` (JPA)
  - [ ] `ReminderListService` (CRUD 로직)
  - [ ] `ReminderListController`
    - [ ] `GET /api/lists`
    - [ ] `POST /api/lists`
    - [ ] `PUT /api/lists/{id}`
    - [ ] `DELETE /api/lists/{id}`
- [ ] `Reminder` 도메인
  - [ ] `Reminder` 엔티티 (`id`, `listId`, `title`, `completed`, `sortOrder`, `createdAt`, `updatedAt`)
  - [ ] `ReminderRepository` (JPA)
  - [ ] `ReminderService` (CRUD 로직)
  - [ ] `ReminderController`
    - [ ] `GET /api/lists/{listId}/reminders`
    - [ ] `POST /api/lists/{listId}/reminders`
    - [ ] `PUT /api/reminders/{id}`
    - [ ] `DELETE /api/reminders/{id}`
- [ ] `GlobalExceptionHandler` (`@RestControllerAdvice`)
  - [ ] 404 응답 (`EntityNotFoundException`)
  - [ ] 400 응답 (`MethodArgumentNotValidException`)
- [ ] 샘플 데이터 초기화 (`CommandLineRunner` 또는 `data.sql`)

### Frontend
- [ ] Next.js 프로젝트 생성
  - [ ] `create-next-app` (TypeScript, App Router, Tailwind CSS v4)
  - [ ] 패키지 설치: `lucide-react`, `@tanstack/react-query`, `zustand`, `date-fns`
  - [ ] `.env.local` 설정 (`NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api`)
- [ ] `lib/types.ts` — `ReminderList`, `Reminder` 타입 정의
- [ ] `lib/api.ts` — fetch wrapper (baseURL, 공통 error handling)
- [ ] 전체 레이아웃 (`app/layout.tsx`)
  - [ ] 사이드바(260px) + 메인(flex-grow) 구조
  - [ ] macOS 시스템 폰트 적용
  - [ ] 배경색 `bg-[#F2F2F7]`
- [ ] 사이드바
  - [ ] 스마트 목록 카드 2×2 그리드 (`SmartListGrid.tsx`) — 정적 UI
  - [ ] 내 목록 리스트 (`ListItem.tsx`) — `GET /api/lists` 연동
  - [ ] `+ 목록 추가` 버튼 (`AddListButton.tsx`)
- [ ] 메인 영역
  - [ ] `app/list/[id]/page.tsx` 라우트 생성
  - [ ] 선택된 목록의 미리 알림 목록 표시 (`GET /api/lists/{listId}/reminders` 연동)
  - [ ] `app/page.tsx` — `/list/today` 리다이렉트

---

## Phase 2 — 핵심 CRUD + 완료 기능

### Backend
- [ ] `Reminder` 엔티티에 `flagged` 필드 추가
- [ ] `PATCH /api/reminders/{id}/complete` — 완료 토글
- [ ] `PATCH /api/reminders/{id}/flag` — 깃발 토글
- [ ] 스마트 목록 쿼리 (`GET /api/reminders?filter=`)
  - [ ] `today` — `dueDate = today AND completed = false`
  - [ ] `scheduled` — `dueDate IS NOT NULL AND completed = false`
  - [ ] `all` — `completed = false`
  - [ ] `flagged` — `flagged = true AND completed = false`
  - [ ] `completed` — `completed = true`
- [ ] `sortOrder` 자동 부여 (생성 시 마지막 순서)

### Frontend
- [ ] `ReminderCheckbox.tsx`
  - [ ] 원형 체크박스 렌더링
  - [ ] 목록 컬러로 테두리/채움 적용
  - [ ] 완료 시 체크마크 아이콘 표시
- [ ] 완료 애니메이션
  - [ ] 체크 → 취소선 등장 (0.3s)
  - [ ] 0.5s 후 항목 slide-up으로 사라짐
- [ ] `CompletedSection.tsx` — 완료 항목 섹션
  - [ ] 목록 하단 분리 ("완료됨" 헤더)
  - [ ] 아코디언 접기/펼치기 (기본 접힘)
- [ ] `ReminderInlineInput.tsx` — 인라인 항목 추가
  - [ ] `⊕` 버튼 클릭 → 인라인 input 활성화
  - [ ] Enter: 항목 저장 후 다음 input 포커스
  - [ ] Escape: 취소
- [ ] `ReminderItem.tsx` 인라인 편집
  - [ ] 제목 클릭 → `contenteditable` 또는 input으로 전환
  - [ ] 포커스 아웃 → 자동 저장
- [ ] 스마트 목록 카드 클릭 → 필터 파라미터로 미리 알림 조회
- [ ] TanStack Query 설정
  - [ ] `QueryClientProvider` 래핑
  - [ ] `useQuery` — 목록/미리 알림 조회
  - [ ] `useMutation` — 생성/수정/삭제/토글
  - [ ] 낙관적 업데이트 (완료 토글 즉시 반영)
- [ ] Zustand `uiStore.ts`
  - [ ] 선택된 목록 ID 상태
  - [ ] 상세 패널 열림/닫힘 상태
  - [ ] 선택된 미리 알림 ID 상태

---

## Phase 3 — 상세 패널 + 필드 확장

### Backend
- [ ] `Reminder` 엔티티 필드 추가
  - [ ] `notes: String`
  - [ ] `dueDate: LocalDate`
  - [ ] `dueTime: LocalTime`
  - [ ] `priority: Enum (NONE, LOW, MEDIUM, HIGH)`
  - [ ] `url: String`
- [ ] `PUT /api/reminders/{id}` 전체 필드 업데이트 지원
- [ ] `PATCH /api/reminders/{id}/move` — `listId` 변경
- [ ] `ListGroup` 도메인
  - [ ] `ListGroup` 엔티티 (`id`, `name`, `sortOrder`)
  - [ ] `GET /api/list-groups`
  - [ ] `POST /api/list-groups`
  - [ ] `PUT /api/list-groups/{id}`
  - [ ] `DELETE /api/list-groups/{id}`
- [ ] `ReminderList` 엔티티 필드 추가
  - [ ] `color: String` (hex)
  - [ ] `icon: String` (lucide icon name or emoji)
  - [ ] `groupId: Long` (nullable)

### Frontend
- [ ] `DetailPanel.tsx` — 우측 상세 패널
  - [ ] 슬라이드인 애니메이션 (0.25s ease-out)
  - [ ] 제목 편집 textarea
  - [ ] 메모 편집 textarea
  - [ ] 날짜 커스텀 팝오버 picker
  - [ ] 시간 커스텀 팝오버 picker
  - [ ] 우선순위 선택 팝오버 (`없음 / ! / !! / !!!`)
  - [ ] 깃발 토글 행
  - [ ] URL 입력 행
  - [ ] 목록 이동 선택 팝오버
  - [ ] 패널 닫기 버튼 (`✕`)
- [ ] 목록 생성/편집 모달
  - [ ] 목록 이름 입력
  - [ ] 색상 팔레트 17종 선택
  - [ ] 아이콘 선택 (Lucide 아이콘 목록 또는 이모지 입력)
- [ ] `ReminderItem.tsx` 메타 정보 표시
  - [ ] 우선순위 `!` / `!!` / `!!!` (빨간색, 제목 앞)
  - [ ] 깃발 아이콘 (주황, 제목 우측)
  - [ ] 날짜 표시 (제목 하단, 오늘 이전 빨간색)
- [ ] `SmartListGrid.tsx` 카드에 미완료 수 배지 연동
- [ ] `ListItem.tsx` 미완료 수 배지 표시
- [ ] 목록 그룹 섹션 헤더 (`text-xs tracking-wider 대문자`)
- [ ] `ColorPalette.tsx` 컴포넌트 (17종 색상 선택기)

---

## Phase 4 — 하위 항목 + 태그

### Backend
- [ ] `Reminder` 엔티티 `parentId: Long` 필드 추가
- [ ] 목록 조회 응답에 `children: List<Reminder>` 포함
  - [ ] 최상위 항목만 반환, 하위 항목은 `children`에 중첩
- [ ] `Tag` 엔티티 (`id`, `name`)
- [ ] `ReminderTag` 연관 엔티티 (`reminderId`, `tagId`)
- [ ] `TagRepository`
- [ ] `GET /api/tags` — 전체 태그 조회
- [ ] `DELETE /api/tags/{id}` — 태그 삭제
- [ ] 미리 알림 생성/수정 시 태그 목록 처리 (추가/제거)

### Frontend
- [ ] `ReminderItem.tsx` 하위 항목 렌더링
  - [ ] `children` 배열 재귀 렌더링
  - [ ] 16px 좌측 indent
  - [ ] 하위 항목 접기/펼치기 토글
- [ ] 인라인 입력에서 Tab 키 → 하위 항목 변환
- [ ] 인라인 입력에서 Shift+Tab → 상위 항목 복귀
- [ ] `TagInput.tsx` — 태그 입력 컴포넌트
  - [ ] 기존 태그 자동완성
  - [ ] Enter/쉼표로 태그 추가
  - [ ] 태그 칩 삭제 (`×`)
- [ ] `DetailPanel.tsx`에 태그 입력 행 추가
- [ ] 사이드바 태그 섹션
  - [ ] 전체 태그 목록 표시 (`#태그명`)
  - [ ] 태그 클릭 → 해당 태그 미리 알림 필터
- [ ] `lib/types.ts` `Tag` 타입, `Reminder.children` 타입 추가

---

## Phase 5 — 드래그 앤 드롭 + 검색

### Backend
- [ ] `PATCH /api/reminders/reorder` — `[{id, sortOrder}]` 배열로 일괄 업데이트
- [ ] `PATCH /api/lists/reorder` — 목록 순서 일괄 업데이트
- [ ] `GET /api/reminders/search?q={keyword}` — 제목 + 메모 LIKE 검색

### Frontend
- [ ] dnd-kit 설정
  - [ ] `DndContext`, `SortableContext` 래핑
  - [ ] `useSortable` 훅으로 `ReminderItem` 드래그 가능하게
  - [ ] 드래그 중 ghost 아이템 스타일
  - [ ] 드롭 완료 시 `reorder` API 호출
- [ ] 목록 간 항목 이동
  - [ ] 사이드바 `ListItem`을 드롭 타겟으로 설정
  - [ ] 드롭 시 `move` API 호출
- [ ] 사이드바 목록 드래그 재정렬
- [ ] 검색 UI
  - [ ] 사이드바 상단 또는 메인 상단 검색바
  - [ ] 입력 debounce 300ms
  - [ ] `GET /api/reminders/search?q=` 연동
- [ ] 검색 결과 뷰
  - [ ] 목록명으로 그룹핑하여 표시
  - [ ] 검색어 하이라이트

---

## Phase 6 — 다크 모드 + 키보드 + Polish

### Frontend
- [ ] 다크 모드
  - [ ] Tailwind `dark:` 클래스 전체 컴포넌트에 적용
  - [ ] `prefers-color-scheme` 시스템 연동
  - [ ] 사이드바 `bg-[#2C2C2E]/80` dark 배경
  - [ ] 메인 영역 `bg-[#1C1C1E]` dark 배경
- [ ] 키보드 단축키 (`useEffect` + `keydown` 이벤트)
  - [ ] `Enter` — 같은 레벨 다음 항목 추가
  - [ ] `Tab` — 하위 항목 변환
  - [ ] `Shift+Tab` — 상위 항목 복귀
  - [ ] `Escape` — 편집 취소 / 패널 닫기
  - [ ] `Space` — 선택된 항목 완료 토글
  - [ ] `Cmd+Z` — 완료 실행 취소
- [ ] 애니메이션 polish
  - [ ] 목록 전환 fade (0.2s)
  - [ ] 패널 슬라이드인 (0.25s ease-out)
  - [ ] 완료 체크 시퀀스 (채움 → 취소선 → slide-up)
  - [ ] hover `scale(0.97)` / active 피드백
  - [ ] 목록 카드 pressed 효과
- [ ] 빈 상태 UI
  - [ ] 목록이 비었을 때 안내 문구 + 아이콘
  - [ ] 스마트 목록 결과 없을 때 안내
- [ ] macOS 폰트 스택 전역 적용
  - [ ] `-apple-system, BlinkMacSystemFont, 'SF Pro Text', sans-serif`
