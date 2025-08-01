# 스터디 서비스 도메인 모델 고도화 전략서

## 1. 현황 분석

### 1.1 프론트엔드 데이터 요구사항
현재 프론트엔드에서 스터디 페이지 구현을 위해 사용하는 데이터는 다음과 같습니다:

#### 스터디 목록 페이지 (StudyPage)
- ✅ id, name, status
- ✅ title (백엔드), tagline (프론트엔드)
- ✅ description
- ❌ generation (세대 정보)
- ❌ slug (URL 식별자)
- ❌ type, typeLabel (스터디 유형)
- ❌ schedule, duration (일정 정보)
- ❌ capacity, enrolled (정원/현재원)
- ❌ deadline (모집 마감일)
- ❌ leader (리더 정보)
- ❌ color (테마 색상)

#### 스터디 상세 페이지 (TecoTecoPage 예시)
- **Hero Section**
  - ❌ 메인 타이틀, 서브타이틀
  - ❌ 핵심 가치 포인트 (3개)
  - ❌ 프로필 이미지
  
- **Members Section**
  - ❌ 멤버 정보 (name, githubId, imageUrl, contribution, joinDate)
  - ❌ 멤버 상세 정보 (role, streak, solvedProblems, memorableProblem 등)
  - ❌ 통계 데이터 (totalProblems, totalHours, participationRate 등)
  - ❌ 주간 MVP
  
- **How We Roll Section**
  - ❌ 모임 상세 정보 (장소, 시간, 교재, 비용)
  - ❌ 시간표 및 활동 내용
  
- **Journey Section**
  - ❌ 시즌별 진행 내역
  - ❌ 성과 및 통계
  - ❌ 로드맵 이미지
  
- **Reviews Section**
  - ❌ 후기 데이터 (작성자, 참석 횟수, 제목, 내용, 리액션)
  - ❌ 키워드 태그
  
- **FAQ Section**
  - ❌ 자주 묻는 질문과 답변

### 1.2 백엔드 현재 제공 데이터
```java
// 현재 Study 도메인 모델
- id (UUID)
- title (String)
- description (String)
- proposerId (String)
- status (StudyStatus: PENDING, APPROVED, REJECTED, TERMINATED)
- createdAt (LocalDateTime)
- updatedAt (LocalDateTime)
```

### 1.3 Gap 분석
프론트엔드 요구사항 대비 백엔드에서 제공하지 못하는 데이터가 **90% 이상**입니다.

## 2. 도메인 모델 확장 전략

### 2.1 핵심 원칙
1. **단계적 확장**: 핵심 기능부터 순차적으로 구현
2. **유연한 구조**: 다양한 스터디 유형을 수용할 수 있는 설계
3. **성능 고려**: 적절한 정규화와 캐싱 전략
4. **확장성**: 미래 요구사항을 고려한 설계

### 2.2 도메인 모델 설계

#### Phase 1: 핵심 도메인 확장 (우선순위 높음)

**1. Study 엔티티 확장**
```java
public class Study {
    // 기존 필드
    private UUID id;
    private String title;
    private String description;
    private String proposerId;
    private StudyStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 신규 필드
    private Integer generation;              // 세대 (1기, 2기...)
    private String slug;                     // URL 식별자 (예: tecoteco)
    private StudyType type;                  // 스터디 유형 (PARTICIPATORY, EDUCATIONAL)
    private String tagline;                  // 한 줄 소개
    private String schedule;                 // 일정 (예: "매주 금요일")
    private String duration;                 // 시간 (예: "19:30-21:30")
    private Integer capacity;                // 정원
    private Integer enrolled;                // 현재 참여 인원
    private LocalDate recruitDeadline;       // 모집 마감일
    private LocalDate startDate;             // 시작일
    private LocalDate endDate;               // 종료일 (nullable)
}
```

**2. StudyLeader 엔티티 (신규)**
```java
public class StudyLeader {
    private UUID id;
    private UUID studyId;
    private String name;
    private String profileImage;
    private String welcomeMessage;
    private String email;
    private String githubId;
}
```

**3. StudyTheme 엔티티 (신규)**
```java
public class StudyTheme {
    private UUID id;
    private UUID studyId;
    private String primaryColor;
    private String secondaryColor;
    private String glowColor;
    private String heroImage;
}
```

#### Phase 2: 운영 데이터 확장

**4. StudyMember 엔티티 (신규)**
```java
public class StudyMember {
    private UUID id;
    private UUID studyId;
    private String memberId;         // User ID reference
    private String name;
    private String githubId;
    private String profileImage;
    private String contribution;     // 기여 내용
    private LocalDate joinDate;
    private MemberRole role;         // LEADER, CORE_MEMBER, MEMBER
    private MemberStatus status;     // ACTIVE, INACTIVE, ALUMNI
}
```

**5. StudySchedule 엔티티 (신규)**
```java
public class StudySchedule {
    private UUID id;
    private UUID studyId;
    private String time;            // 예: "19:30 ~ 20:20"
    private String activity;        // 예: "이론/코드 리뷰"
    private String detail;
    private Integer orderIndex;
    private ScheduleType type;      // PRIMARY, SECONDARY, BREAK
}
```

**6. StudyLocation 엔티티 (신규)**
```java
public class StudyLocation {
    private UUID id;
    private UUID studyId;
    private String name;            // 예: "강남역 스터디룸"
    private String address;
    private LocationType type;      // OFFLINE, ONLINE, HYBRID
    private String onlinePlatform;  // 예: "Discord"
    private String accessInfo;      // 오시는 길 등
}
```

#### Phase 3: 콘텐츠 관리

**7. StudySection 엔티티 (신규)**
```java
public class StudySection {
    private UUID id;
    private UUID studyId;
    private SectionType type;       // HERO, INTRO, MEMBERS, HOW_WE_ROLL...
    private String title;
    private String content;         // JSON 형태로 저장
    private Integer orderIndex;
    private Boolean isActive;
}
```

**8. StudyReview 엔티티 (신규)**
```java
public class StudyReview {
    private UUID id;
    private UUID studyId;
    private String authorName;
    private Integer attendCount;
    private String title;
    private String content;
    private List<String> emojis;
    private Integer likes;
    private LocalDateTime createdAt;
}
```

**9. StudyFaq 엔티티 (신규)**
```java
public class StudyFaq {
    private UUID id;
    private UUID studyId;
    private String question;
    private String answer;
    private Integer orderIndex;
}
```

**10. StudyStats 엔티티 (신규)**
```java
public class StudyStats {
    private UUID id;
    private UUID studyId;
    private Integer totalProblems;
    private Integer totalHours;
    private Double participationRate;
    private List<String> popularTopics;
    private String weeklyMvpMemberId;
    private LocalDate statsDate;
}
```

### 2.3 데이터베이스 스키마 설계

```sql
-- 1. studies 테이블 확장
ALTER TABLE studies 
ADD COLUMN generation INT,
ADD COLUMN slug VARCHAR(50) UNIQUE,
ADD COLUMN type VARCHAR(20),
ADD COLUMN tagline VARCHAR(200),
ADD COLUMN schedule VARCHAR(100),
ADD COLUMN duration VARCHAR(50),
ADD COLUMN capacity INT,
ADD COLUMN enrolled INT DEFAULT 0,
ADD COLUMN recruit_deadline DATE,
ADD COLUMN start_date DATE,
ADD COLUMN end_date DATE;

-- 2. study_leaders 테이블
CREATE TABLE study_leaders (
    id BINARY(16) PRIMARY KEY,
    study_id BINARY(16) NOT NULL,
    name VARCHAR(100) NOT NULL,
    profile_image VARCHAR(500),
    welcome_message TEXT,
    email VARCHAR(255),
    github_id VARCHAR(100),
    FOREIGN KEY (study_id) REFERENCES studies(id)
);

-- 3. study_themes 테이블
CREATE TABLE study_themes (
    id BINARY(16) PRIMARY KEY,
    study_id BINARY(16) NOT NULL,
    primary_color VARCHAR(7),
    secondary_color VARCHAR(7),
    glow_color VARCHAR(30),
    hero_image VARCHAR(500),
    FOREIGN KEY (study_id) REFERENCES studies(id)
);

-- 4. study_members 테이블
CREATE TABLE study_members (
    id BINARY(16) PRIMARY KEY,
    study_id BINARY(16) NOT NULL,
    member_id VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    github_id VARCHAR(100),
    profile_image VARCHAR(500),
    contribution VARCHAR(500),
    join_date DATE,
    role VARCHAR(20),
    status VARCHAR(20),
    FOREIGN KEY (study_id) REFERENCES studies(id),
    INDEX idx_member_study (study_id, member_id)
);

-- 5. study_schedules 테이블
CREATE TABLE study_schedules (
    id BINARY(16) PRIMARY KEY,
    study_id BINARY(16) NOT NULL,
    time VARCHAR(50),
    activity VARCHAR(200),
    detail TEXT,
    order_index INT,
    type VARCHAR(20),
    FOREIGN KEY (study_id) REFERENCES studies(id)
);

-- 6. study_locations 테이블
CREATE TABLE study_locations (
    id BINARY(16) PRIMARY KEY,
    study_id BINARY(16) NOT NULL,
    name VARCHAR(200),
    address VARCHAR(500),
    type VARCHAR(20),
    online_platform VARCHAR(100),
    access_info TEXT,
    FOREIGN KEY (study_id) REFERENCES studies(id)
);

-- 7. study_sections 테이블
CREATE TABLE study_sections (
    id BINARY(16) PRIMARY KEY,
    study_id BINARY(16) NOT NULL,
    type VARCHAR(50),
    title VARCHAR(200),
    content JSON,
    order_index INT,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (study_id) REFERENCES studies(id)
);

-- 8. study_reviews 테이블
CREATE TABLE study_reviews (
    id BINARY(16) PRIMARY KEY,
    study_id BINARY(16) NOT NULL,
    author_name VARCHAR(100),
    attend_count INT,
    title VARCHAR(200),
    content TEXT,
    emojis JSON,
    likes INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (study_id) REFERENCES studies(id)
);

-- 9. study_faqs 테이블
CREATE TABLE study_faqs (
    id BINARY(16) PRIMARY KEY,
    study_id BINARY(16) NOT NULL,
    question TEXT,
    answer TEXT,
    order_index INT,
    FOREIGN KEY (study_id) REFERENCES studies(id)
);

-- 10. study_stats 테이블
CREATE TABLE study_stats (
    id BINARY(16) PRIMARY KEY,
    study_id BINARY(16) NOT NULL,
    total_problems INT,
    total_hours INT,
    participation_rate DECIMAL(5,2),
    popular_topics JSON,
    weekly_mvp_member_id VARCHAR(100),
    stats_date DATE,
    FOREIGN KEY (study_id) REFERENCES studies(id),
    INDEX idx_stats_date (study_id, stats_date)
);
```

## 3. API 설계

### 3.1 스터디 상세 정보 조회
```
GET /api/v1/studies/{slug}/detail

Response:
{
  "study": {
    "id": "uuid",
    "slug": "tecoteco",
    "name": "테코테코",
    "generation": 3,
    "type": "PARTICIPATORY",
    "tagline": "함께 풀어가는 알고리즘의 즐거움",
    "description": "...",
    "status": "RECRUITING",
    "schedule": "매주 금요일",
    "duration": "19:30-21:30",
    "capacity": 20,
    "enrolled": 17,
    "recruitDeadline": "2024-12-25",
    "startDate": "2024-09-01"
  },
  "leader": {
    "name": "김준혁",
    "profileImage": "...",
    "welcomeMessage": "..."
  },
  "theme": {
    "primaryColor": "#C3E88D",
    "glowColor": "rgba(195, 232, 141, 0.3)"
  },
  "location": {
    "name": "강남역 스터디룸",
    "type": "OFFLINE",
    "onlinePlatform": "Discord"
  },
  "schedules": [...],
  "members": [...],
  "sections": [...],
  "stats": {...},
  "reviews": [...],
  "faqs": [...]
}
```

### 3.2 섹션별 관리 API
```
# 섹션 수정
PUT /api/v1/studies/{id}/sections/{sectionType}

# 멤버 관리
POST /api/v1/studies/{id}/members
PUT /api/v1/studies/{id}/members/{memberId}
DELETE /api/v1/studies/{id}/members/{memberId}

# 통계 업데이트
POST /api/v1/studies/{id}/stats

# 리뷰 관리
POST /api/v1/studies/{id}/reviews
PUT /api/v1/studies/{id}/reviews/{reviewId}/like
```

## 4. 주의사항

1. **하위 호환성**: 기존 API는 유지하면서 새로운 엔드포인트 추가
2. **데이터 마이그레이션**: 기존 하드코딩된 데이터를 DB로 이전
3. **성능**: 상세 페이지 조회 시 N+1 문제 방지 (적절한 Join/Fetch 전략)
4. **확장성**: JSON 필드 활용으로 유연한 데이터 구조 지원
5. **보안**: 멤버 개인정보 보호, 적절한 권한 체크

이 문서를 기반으로 백엔드 개발팀이 단계적으로 도메인 모델을 확장할 수 있습니다.