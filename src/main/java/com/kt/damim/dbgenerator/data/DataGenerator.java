package com.kt.damim.dbgenerator.data;

import com.kt.damim.dbgenerator.config.DataGenerationConfig;
import com.kt.damim.dbgenerator.entity.Attendance;
import com.kt.damim.dbgenerator.entity.Class;
import com.kt.damim.dbgenerator.entity.Enrollment;
import com.kt.damim.dbgenerator.entity.Session;
import com.kt.damim.dbgenerator.entity.User;
import com.kt.damim.dbgenerator.entity.UserProfile;
import com.kt.damim.dbgenerator.entity.Exam;
import com.kt.damim.dbgenerator.entity.Question;
import com.kt.damim.dbgenerator.entity.Submission;
import com.kt.damim.dbgenerator.entity.SubmissionAnswer;
import com.kt.damim.dbgenerator.entity.QuestionType;
import com.kt.damim.dbgenerator.repository.AttendanceRepository;
import com.kt.damim.dbgenerator.repository.ClassRepository;
import com.kt.damim.dbgenerator.repository.EnrollmentRepository;
import com.kt.damim.dbgenerator.repository.SessionRepository;
import com.kt.damim.dbgenerator.repository.UserRepository;
import com.kt.damim.dbgenerator.repository.UserProfileRepository;
import com.kt.damim.dbgenerator.repository.ExamRepository;
import com.kt.damim.dbgenerator.repository.QuestionRepository;
import com.kt.damim.dbgenerator.repository.SubmissionRepository;
import com.kt.damim.dbgenerator.repository.SubmissionAnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataGenerator implements CommandLineRunner {
    
    private final DataGenerationConfig config;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final ClassRepository classRepository;
    private final SessionRepository sessionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final SubmissionRepository submissionRepository;
    private final SubmissionAnswerRepository submissionAnswerRepository;
    
    private final Random random = new Random();
    
    // 교사 이름 목록
    private static final String[] TEACHER_NAMES = {
        "김교수", "이교수", "박교수", "최교수", "정교수",
        "한교수", "윤교수", "임교수", "강교수", "조교수"
    };
    
    // 학생 이름 목록
    private static final String[] STUDENT_NAMES = {
        "김학생", "이학생", "박학생", "최학생", "정학생",
        "한학생", "윤학생", "임학생", "강학생", "조학생",
        "서학생", "신학생", "오학생", "유학생", "장학생",
        "전학생", "제학생", "하학생", "고학생", "문학생"
    };
    
    // 학교 목록
    private static final String[] SCHOOLS = {
        "서울대학교", "연세대학교", "고려대학교", "성균관대학교", "한양대학교",
        "중앙대학교", "경희대학교", "동국대학교", "홍익대학교", "건국대학교"
    };
    
    // 희망 과목 목록
    private static final String[] DESIRED_COURSES = {
        "자바프로그래밍", "웹개발", "모바일앱개발", "데이터분석", "인공지능",
        "게임개발", "보안", "클라우드컴퓨팅", "DevOps", "블록체인"
    };
    
    // 희망 직업 목록
    private static final String[] DESIRED_JOBS = {
        "소프트웨어 개발자", "웹 개발자", "모바일 앱 개발자", "데이터 분석가", "AI 엔지니어",
        "게임 개발자", "보안 전문가", "DevOps 엔지니어", "프로젝트 매니저", "IT 컨설턴트"
    };
    
    // 과목명 목록
    private static final String[] SUBJECTS = {
        "자바프로그래밍", "스프링부트", "데이터베이스", "웹개발", "알고리즘",
        "운영체제", "네트워크", "소프트웨어공학", "컴퓨터구조", "인공지능",
        "머신러닝", "딥러닝", "클라우드컴퓨팅", "모바일앱개발", "게임개발",
        "보안프로그래밍", "블록체인", "IoT프로그래밍", "빅데이터분석", "DevOps"
    };
    
    // 학기 목록
    private static final String[] SEMESTERS = {"2024-1", "2024-2", "2025-1"};
    
    // 요일 비트셋 (월화수목금토일)
    // 월(1), 화(2), 수(4), 목(8), 금(16), 토(32), 일(64)
    // 1~3개 요일 조합만 사용
    private static final int[] DAY_PATTERNS = {
        1, 2, 4, 8, 16, 32, 64,  // 단일 요일
        3, 5, 6, 9, 10, 12, 17, 18, 20, 24, 33, 34, 36, 40, 48, 65, 66, 68, 72, 80, 96,  // 2개 요일
        7, 11, 13, 14, 19, 21, 22, 25, 26, 28, 35, 37, 38, 41, 42, 44, 49, 50, 52, 56, 67, 69, 70, 73, 74, 76, 81, 82, 84, 88, 97, 98, 100, 104, 112  // 3개 요일
    };
    
    // 시간대 패턴
    private static final LocalTime[][] TIME_PATTERNS = {
        {LocalTime.of(9, 0), LocalTime.of(10, 30)},
        {LocalTime.of(10, 0), LocalTime.of(11, 30)},
        {LocalTime.of(13, 0), LocalTime.of(14, 30)},
        {LocalTime.of(14, 0), LocalTime.of(15, 30)},
        {LocalTime.of(15, 0), LocalTime.of(16, 30)}
    };
    
    // 시험 관련 상수
    private static final String[] EXAM_DIFFICULTIES = {"EASY", "MEDIUM", "HARD"};
    
    // 시험 문제 세트 (과목별)
    private static final String[][] EXAM_QUESTIONS = {
        // 자바프로그래밍
        {
            "Java에서 상속을 나타내는 키워드는?",
            "Java의 기본 데이터 타입 중 정수형이 아닌 것은?",
            "Java에서 메모리 관리는 어떻게 이루어지는가?",
            "Java의 접근 제어자 중 가장 제한적인 것은?",
            "Java에서 다형성을 구현하는 방법은?",
            "Java에서 인터페이스와 추상클래스의 차이점은?",
            "Java의 컬렉션 프레임워크에서 List와 Set의 차이점은?",
            "Java에서 예외처리를 위한 키워드는?",
            "Java의 스레드와 프로세스의 차이점은?",
            "Java에서 제네릭(Generic)의 목적은?"
        },
        // 데이터베이스
        {
            "SQL에서 데이터를 조회하는 명령어는?",
            "데이터베이스 정규화의 목적은?",
            "트랜잭션의 ACID 속성 중 A는?",
            "인덱스의 주요 목적은?",
            "외래키(Foreign Key)의 역할은?",
            "SQL에서 JOIN의 종류는?",
            "데이터베이스 백업의 중요성은?",
            "SQL Injection 공격이란?",
            "데이터베이스 성능 최적화 방법은?",
            "NoSQL 데이터베이스의 특징은?"
        },
        // 웹개발
        {
            "HTTP 메서드 중 데이터를 생성하는 것은?",
            "REST API의 특징이 아닌 것은?",
            "세션과 쿠키의 차이점은?",
            "CORS 정책의 목적은?",
            "웹 보안에서 XSS 공격이란?",
            "웹 서버와 웹 애플리케이션 서버의 차이점은?",
            "HTTPS의 동작 원리는?",
            "웹 캐싱의 장점은?",
            "반응형 웹 디자인의 특징은?",
            "웹 접근성의 중요성은?"
        }
    };
    
    // 객관식 선택지 세트
    private static final String[][] MULTIPLE_CHOICE_OPTIONS = {
        {"extends", "implements", "super", "this"},
        {"int", "double", "long", "float"},
        {"가비지 컬렉션", "수동 해제", "참조 카운팅", "메모리 풀"},
        {"private", "public", "protected", "default"},
        {"오버라이딩", "오버로딩", "캡슐화", "상속"},
        {"인터페이스", "추상클래스", "클래스", "열거형"},
        {"순서 있음 vs 순서 없음", "중복 허용 vs 중복 불허", "인덱스 접근 vs 반복자 접근", "크기 고정 vs 크기 가변"},
        {"try-catch", "if-else", "switch-case", "for-while"},
        {"공유 메모리 vs 독립 메모리", "가벼움 vs 무거움", "동시 실행 vs 순차 실행", "생성 비용 vs 생성 비용"},
        {"타입 안전성", "코드 재사용", "성능 향상", "메모리 절약"},
        {"SELECT", "INSERT", "UPDATE", "DELETE"},
        {"데이터 중복 제거", "성능 향상", "보안 강화", "용량 절약"},
        {"Atomicity", "Availability", "Accuracy", "Accessibility"},
        {"검색 속도 향상", "저장 공간 절약", "데이터 무결성", "백업 용이성"},
        {"참조 무결성", "데이터 중복", "성능 향상", "보안 강화"},
        {"INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL JOIN"},
        {"데이터 보호", "성능 향상", "용량 절약", "접근 제어"},
        {"SQL 명령어 삽입", "스크립트 삽입", "파일 업로드", "세션 하이재킹"},
        {"인덱스 최적화", "쿼리 최적화", "정규화", "모든 것"},
        {"스키마 없음", "관계형 모델", "ACID 보장", "SQL 사용"},
        {"GET", "POST", "PUT", "DELETE"},
        {"무상태성", "캐시 가능", "계층화 시스템", "세션 기반"},
        {"서버 저장 vs 클라이언트 저장", "용량 차이", "보안 수준", "생명주기"},
        {"보안 강화", "성능 향상", "호환성 확보", "접근 제어"},
        {"스크립트 삽입", "SQL 삽입", "CSRF 공격", "DDoS 공격"},
        {"정적 파일 vs 동적 처리", "단일 서버 vs 분산 서버", "단순 vs 복잡", "빠름 vs 느림"},
        {"SSL/TLS 암호화", "HTTP 암호화", "세션 암호화", "쿠키 암호화"},
        {"성능 향상", "대역폭 절약", "서버 부하 감소", "모든 것"},
        {"반응형 레이아웃", "모바일 최적화", "크로스 브라우징", "모든 것"},
        {"사용자 편의성", "법적 요구사항", "SEO 최적화", "모든 것"}
    };
    
    // 단답형 답안 세트
    private static final String[] SHORT_ANSWER_ANSWERS = {
        "extends", "double", "가비지 컬렉션", "private", "오버라이딩",
        "인터페이스", "순서 있음 vs 순서 없음", "try-catch", "공유 메모리 vs 독립 메모리", "타입 안전성",
        "SELECT", "데이터 중복 제거", "Atomicity", "검색 속도 향상", "참조 무결성",
        "INNER JOIN", "데이터 보호", "SQL 명령어 삽입", "인덱스 최적화", "스키마 없음",
        "POST", "무상태성", "서버 저장 vs 클라이언트 저장", "보안 강화", "스크립트 삽입",
        "정적 파일 vs 동적 처리", "SSL/TLS 암호화", "성능 향상", "반응형 레이아웃", "사용자 편의성"
    };
    
    @Override
    public void run(String... args) throws Exception {
        if (!config.isEnabled()) {
            log.info("데이터 생성이 비활성화되어 있습니다. data.generation.enabled=true로 설정하세요.");
            return;
        }
        
        log.info("대량 테스트 데이터 생성을 시작합니다...");
        
        // 기존 데이터 확인
        if (userRepository.count() > 0) {
            log.info("기존 데이터가 존재합니다. 데이터 생성을 건너뜁니다.");
            return;
        }
        
        long startTime = System.currentTimeMillis();
        
        // 1. 교사 생성 및 프로필 생성
        List<User> teachers = generateTeachers();
        generateTeacherProfiles(teachers);
        log.info("{}명의 교사와 프로필을 생성했습니다.", teachers.size());
        
        // 2. 학생 생성 및 프로필 생성
        List<User> students = generateStudents();
        generateStudentProfiles(students);
        log.info("{}명의 학생과 프로필을 생성했습니다.", students.size());
        
        // 3. 클래스 생성
        List<Class> classes = generateClasses(teachers);
        log.info("{}개의 클래스를 생성했습니다.", classes.size());
        
        // 4. 세션 생성
        List<Session> sessions = generateSessions(classes);
        log.info("{}개의 세션을 생성했습니다.", sessions.size());
        
        // 5. 수강 신청 생성
        List<Enrollment> enrollments = generateEnrollments(students, classes);
        log.info("{}개의 수강 신청을 생성했습니다.", enrollments.size());
        
        // 6. 출석 데이터 생성
        int attendanceCount = generateAttendances(students, sessions);
        log.info("{}개의 출석 데이터를 생성했습니다.", attendanceCount);
        
        // 7. 시험 데이터 생성
        int examCount = generateExams(sessions, classes);
        log.info("{}개의 시험을 생성했습니다.", examCount);
        
        long endTime = System.currentTimeMillis();
        log.info("데이터 생성이 완료되었습니다. 소요시간: {}ms", endTime - startTime);
        
        printStatistics(teachers.size(), students.size(), classes.size(), sessions.size(), enrollments.size(), attendanceCount, examCount);
    }
    
    private List<User> generateTeachers() {
        List<User> teachers = new ArrayList<>();
        
        for (int i = 0; i < config.getTeacherCount(); i++) {
            User teacher = User.builder()
                    .email("teacher" + (i + 1) + "@university.edu")
                    .passwordHash("hashed_password_teacher_" + (i + 1))
                    .role(User.UserRole.TEACHER)
                    .isActive(true)
                    .createdAt(OffsetDateTime.now())
                    .build();
            
            teachers.add(userRepository.save(teacher));
        }
        
        return teachers;
    }
    
    private List<User> generateStudents() {
        List<User> students = new ArrayList<>();
        
        for (int i = 0; i < config.getStudentCount(); i++) {
            User student = User.builder()
                    .email("student" + (i + 1) + "@university.edu")
                    .passwordHash("hashed_password_student_" + (i + 1))
                    .role(User.UserRole.STUDENT)
                    .isActive(true)
                    .createdAt(OffsetDateTime.now())
                    .build();
            
            students.add(userRepository.save(student));
        }
        
        return students;
    }
    
    private void generateTeacherProfiles(List<User> teachers) {
        for (int i = 0; i < teachers.size(); i++) {
            User teacher = teachers.get(i);
            String teacherName = TEACHER_NAMES[i % TEACHER_NAMES.length];
            
            UserProfile profile = UserProfile.builder()
                    .userId(teacher.getUserId())
                    .name(teacherName)
                    .desiredCourse(DESIRED_COURSES[random.nextInt(DESIRED_COURSES.length)])
                    .desiredJob(DESIRED_JOBS[random.nextInt(DESIRED_JOBS.length)])
                    .birthDate(generateRandomBirthDate(30, 60)) // 교사는 30-60세
                    .school(SCHOOLS[random.nextInt(SCHOOLS.length)])
                    .phone("010-" + String.format("%04d", random.nextInt(10000)) + "-" + String.format("%04d", random.nextInt(10000)))
                    .createdAt(OffsetDateTime.now())
                    .build();
            
            userProfileRepository.save(profile);
        }
    }
    
    private void generateStudentProfiles(List<User> students) {
        for (int i = 0; i < students.size(); i++) {
            User student = students.get(i);
            String studentName = STUDENT_NAMES[i % STUDENT_NAMES.length];
            
            UserProfile profile = UserProfile.builder()
                    .userId(student.getUserId())
                    .name(studentName)
                    .desiredCourse(DESIRED_COURSES[random.nextInt(DESIRED_COURSES.length)])
                    .desiredJob(DESIRED_JOBS[random.nextInt(DESIRED_JOBS.length)])
                    .birthDate(generateRandomBirthDate(18, 25)) // 학생은 18-25세
                    .school(SCHOOLS[random.nextInt(SCHOOLS.length)])
                    .phone("010-" + String.format("%04d", random.nextInt(10000)) + "-" + String.format("%04d", random.nextInt(10000)))
                    .createdAt(OffsetDateTime.now())
                    .build();
            
            userProfileRepository.save(profile);
        }
    }
    
    private LocalDate generateRandomBirthDate(int minAge, int maxAge) {
        int currentYear = OffsetDateTime.now().getYear();
        int birthYear = currentYear - minAge - random.nextInt(maxAge - minAge + 1);
        int birthMonth = 1 + random.nextInt(12);
        int birthDay = 1 + random.nextInt(28); // 간단히 28일로 제한
        
        return LocalDate.of(birthYear, birthMonth, birthDay);
    }
    
    private List<Class> generateClasses(List<User> teachers) {
        List<Class> classes = new ArrayList<>();
        int classIndex = 0;
        
        for (User teacher : teachers) {
            List<Class> teacherClasses = new ArrayList<>(); // 교사별로 이미 생성된 강좌들
            
            for (int i = 0; i < config.getClassPerTeacher(); i++) {
                String subject = SUBJECTS[classIndex % SUBJECTS.length];
                String semester = SEMESTERS[random.nextInt(SEMESTERS.length)];
                
                // 시간 충돌이 없는 요일/시간 조합 찾기
                ClassSchedule schedule = findNonConflictingSchedule(teacherClasses);
                
                if (schedule == null) {
                    log.warn("교사 {}에게 시간 충돌이 없는 스케줄을 찾을 수 없습니다. 기본 스케줄을 사용합니다.", teacher.getUserId());
                    // 기본 스케줄 사용
                    int dayPattern = DAY_PATTERNS[random.nextInt(DAY_PATTERNS.length)];
                    LocalTime[] timePattern = TIME_PATTERNS[random.nextInt(TIME_PATTERNS.length)];
                    schedule = new ClassSchedule(dayPattern, timePattern[0], timePattern[1]);
                }
                
                Class classEntity = Class.builder()
                        .teacherId(teacher.getUserId())
                        .teacherName(TEACHER_NAMES[teachers.indexOf(teacher) % TEACHER_NAMES.length])
                        .className(subject)
                        .semester(semester)
                        .zoomUrl("https://zoom.us/j/" + (100000000 + random.nextInt(900000000)))
                        .heldDay(schedule.dayPattern)
                        .startsAt(schedule.startTime)
                        .endsAt(schedule.endTime)
                        .capacity(20 + random.nextInt(31)) // 20-50명
                        .createdAt(OffsetDateTime.now())
                        .build();
                
                Class savedClass = classRepository.save(classEntity);
                classes.add(savedClass);
                teacherClasses.add(savedClass);
                classIndex++;
            }
        }
        
        return classes;
    }
    
    /**
     * 시간 충돌이 없는 스케줄 찾기
     */
    private ClassSchedule findNonConflictingSchedule(List<Class> existingClasses) {
        List<ClassSchedule> availableSchedules = new ArrayList<>();
        
        // 모든 가능한 요일/시간 조합을 확인하여 충돌이 없는 스케줄들 수집
        for (int dayPattern : DAY_PATTERNS) {
            for (LocalTime[] timePattern : TIME_PATTERNS) {
                ClassSchedule candidate = new ClassSchedule(dayPattern, timePattern[0], timePattern[1]);
                
                boolean hasConflict = false;
                for (Class existingClass : existingClasses) {
                    if (hasTimeOverlap(candidate.dayPattern, candidate.startTime, candidate.endTime,
                                     existingClass.getHeldDay(), existingClass.getStartsAt(), existingClass.getEndsAt())) {
                        hasConflict = true;
                        break;
                    }
                }
                
                if (!hasConflict) {
                    availableSchedules.add(candidate);
                }
            }
        }
        
        // 충돌이 없는 스케줄이 있으면 랜덤하게 선택
        if (!availableSchedules.isEmpty()) {
            int randomIndex = random.nextInt(availableSchedules.size());
            return availableSchedules.get(randomIndex);
        }
        
        return null; // 충돌이 없는 스케줄을 찾을 수 없음
    }
    
    /**
     * 두 스케줄 간의 시간 충돌 여부 확인 (오버로드된 메서드)
     */
    private boolean hasTimeOverlap(int day1, LocalTime start1, LocalTime end1, 
                                 int day2, LocalTime start2, LocalTime end2) {
        // 요일이 겹치는지 확인
        if ((day1 & day2) == 0) {
            return false; // 요일이 겹치지 않으면 충돌 없음
        }
        
        // 시간이 겹치는지 확인
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
    
    /**
     * 강좌 스케줄을 담는 내부 클래스
     */
    private static class ClassSchedule {
        final int dayPattern;
        final LocalTime startTime;
        final LocalTime endTime;
        
        ClassSchedule(int dayPattern, LocalTime startTime, LocalTime endTime) {
            this.dayPattern = dayPattern;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
    
    private List<Session> generateSessions(List<Class> classes) {
        List<Session> sessions = new ArrayList<>();
        OffsetDateTime now = OffsetDateTime.now();
        
        for (Class classEntity : classes) {
            // 강의 요일과 시간 정보 가져오기
            int heldDay = classEntity.getHeldDay();
            LocalTime startTime = classEntity.getStartsAt();
            LocalTime endTime = classEntity.getEndsAt();
            
            // 과거 세션 생성 (오늘 이전)
            int pastSessionCount = config.getSessionPerClass() / 2;
            List<OffsetDateTime> pastSessionDates = generatePastSessionDates(heldDay, startTime, now, pastSessionCount);
            
            for (int i = 0; i < pastSessionDates.size(); i++) {
                Session session = Session.builder()
                        .classId(classEntity.getClassId())
                        .sessionName("session" + (i + 1))
                        .onDate(pastSessionDates.get(i))
                        .build();
                
                sessions.add(sessionRepository.save(session));
            }
            
            // 미래 세션 생성 (오늘 포함 이후)
            int futureSessionCount = config.getSessionPerClass() - pastSessionCount;
            List<OffsetDateTime> futureSessionDates = generateFutureSessionDates(heldDay, startTime, now, futureSessionCount);
            
            for (int i = 0; i < futureSessionDates.size(); i++) {
                Session session = Session.builder()
                        .classId(classEntity.getClassId())
                        .sessionName("session" + (pastSessionDates.size() + i + 1))
                        .onDate(futureSessionDates.get(i))
                        .build();
                
                sessions.add(sessionRepository.save(session));
            }
        }
        
        return sessions;
    }
    
    /**
     * 과거 세션 날짜들 생성 (오늘 이전)
     */
    private List<OffsetDateTime> generatePastSessionDates(int heldDay, LocalTime startTime, OffsetDateTime now, int targetCount) {
        List<OffsetDateTime> sessionDates = new ArrayList<>();
        OffsetDateTime currentDate = now.minusDays(1); // 오늘 이전부터 시작
        
        // 과거로 가면서 강의 요일에 맞는 날짜들을 찾아서 세션 생성
        while (sessionDates.size() < targetCount) {
            int dayOfWeek = currentDate.getDayOfWeek().getValue(); // 1(월요일) ~ 7(일요일)
            int dayBit = 1 << (dayOfWeek - 1); // 월(1), 화(2), 수(4), 목(8), 금(16), 토(32), 일(64)
            
            // 해당 요일이 강의 요일에 포함되는지 확인
            if ((heldDay & dayBit) != 0) {
                OffsetDateTime sessionDate = currentDate
                        .withHour(startTime.getHour())
                        .withMinute(startTime.getMinute())
                        .withSecond(0)
                        .withNano(0);
                
                sessionDates.add(sessionDate);
            }
            
            // 이전 날짜로 이동
            currentDate = currentDate.minusDays(1);
        }
        
        return sessionDates;
    }
    
    /**
     * 미래 세션 날짜들 생성 (오늘 포함 이후)
     */
    private List<OffsetDateTime> generateFutureSessionDates(int heldDay, LocalTime startTime, OffsetDateTime now, int targetCount) {
        List<OffsetDateTime> sessionDates = new ArrayList<>();
        OffsetDateTime currentDate = now; // 오늘부터 시작
        
        // 미래로 가면서 강의 요일에 맞는 날짜들을 찾아서 세션 생성
        while (sessionDates.size() < targetCount) {
            int dayOfWeek = currentDate.getDayOfWeek().getValue(); // 1(월요일) ~ 7(일요일)
            int dayBit = 1 << (dayOfWeek - 1); // 월(1), 화(2), 수(4), 목(8), 금(16), 토(32), 일(64)
            
            // 해당 요일이 강의 요일에 포함되는지 확인
            if ((heldDay & dayBit) != 0) {
                OffsetDateTime sessionDate = currentDate
                        .withHour(startTime.getHour())
                        .withMinute(startTime.getMinute())
                        .withSecond(0)
                        .withNano(0);
                
                sessionDates.add(sessionDate);
            }
            
            // 다음 날짜로 이동
            currentDate = currentDate.plusDays(1);
        }
        
        return sessionDates;
    }
    

    

    
    private List<Enrollment> generateEnrollments(List<User> students, List<Class> classes) {
        List<Enrollment> enrollments = new ArrayList<>();
        
        for (User student : students) {
            // 학생별로 랜덤하게 5개 클래스 선택
            List<Class> availableClasses = new ArrayList<>(classes);
            List<Class> enrolledClasses = new ArrayList<>(); // 이미 신청한 강좌들
            int enrollmentCount = Math.min(config.getEnrollmentPerStudent(), availableClasses.size());
            
            for (int i = 0; i < enrollmentCount; i++) {
                if (availableClasses.isEmpty()) break;
                
                // 시간 충돌이 없는 강좌들만 필터링
                List<Class> nonConflictingClasses = filterNonConflictingClasses(availableClasses, enrolledClasses);
                
                if (nonConflictingClasses.isEmpty()) {
                    // 충돌이 없는 강좌가 없으면 기존 방식으로 진행
                    log.warn("학생 {}에게 시간 충돌이 없는 강좌가 없습니다. 기존 방식으로 진행합니다.", student.getUserId());
                    break;
                }
                
                int randomIndex = random.nextInt(nonConflictingClasses.size());
                Class selectedClass = nonConflictingClasses.get(randomIndex);
                
                // 선택된 강좌를 availableClasses에서 제거
                availableClasses.remove(selectedClass);
                enrolledClasses.add(selectedClass);
                
                Enrollment enrollment = Enrollment.builder()
                        .studentId(student.getUserId())
                        .classId(selectedClass.getClassId())
                        .status("ENROLLED")
                        .createdAt(OffsetDateTime.now())
                        .build();
                
                enrollments.add(enrollmentRepository.save(enrollment));
            }
        }
        
        return enrollments;
    }
    
    /**
     * 시간 충돌이 없는 강좌들만 필터링
     */
    private List<Class> filterNonConflictingClasses(List<Class> availableClasses, List<Class> enrolledClasses) {
        return availableClasses.stream()
                .filter(availableClass -> !hasTimeConflict(availableClass, enrolledClasses))
                .collect(Collectors.toList());
    }
    
    /**
     * 특정 강좌가 이미 신청한 강좌들과 시간 충돌이 있는지 확인
     */
    private boolean hasTimeConflict(Class newClass, List<Class> enrolledClasses) {
        for (Class enrolledClass : enrolledClasses) {
            if (hasTimeOverlap(newClass, enrolledClass)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 두 강좌 간의 시간 충돌 여부 확인
     */
    private boolean hasTimeOverlap(Class class1, Class class2) {
        // 요일이 겹치는지 확인
        if ((class1.getHeldDay() & class2.getHeldDay()) == 0) {
            return false; // 요일이 겹치지 않으면 충돌 없음
        }
        
        // 시간이 겹치는지 확인
        LocalTime start1 = class1.getStartsAt();
        LocalTime end1 = class1.getEndsAt();
        LocalTime start2 = class2.getStartsAt();
        LocalTime end2 = class2.getEndsAt();
        
        // 시간 겹침 확인: (start1 < end2) && (start2 < end1)
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
    
    private int generateAttendances(List<User> students, List<Session> sessions) {
        int attendanceCount = 0;
        OffsetDateTime now = OffsetDateTime.now();
        
        for (User student : students) {
            // 학생이 수강하는 클래스의 세션들만 필터링
            List<Session> studentSessions = getStudentSessions(student, sessions);
            
            for (Session session : studentSessions) {
                // 현재 시간 이전의 세션에만 출석 데이터 생성
                if (session.getOnDate().isBefore(now)) {
                    // 출석률에 따라 출석 여부 결정
                    if (random.nextDouble() < config.getAttendanceRate()) {
                        Attendance.AttendanceStatus status = getRandomAttendanceStatus();
                        
                        Attendance attendance = Attendance.builder()
                                .sessionId(session.getSessionId())
                                .studentId(student.getUserId())
                                .status(status)
                                .note(getRandomNote(status))
                                .createdAt(session.getOnDate().plusMinutes(random.nextInt(30)))
                                .build();
                        
                        attendanceRepository.save(attendance);
                        attendanceCount++;
                    }
                }
            }
        }
        
        return attendanceCount;
    }
    
    private int generateExams(List<Session> sessions, List<Class> classes) {
        int examCount = 0;
        OffsetDateTime now = OffsetDateTime.now();
        
        for (Session session : sessions) {
            // 과거 세션에만 시험 생성 (현재 시간보다 이전)
            if (session.getOnDate().isBefore(now)) {
                // 확률에 따라 시험 생성
                if (random.nextDouble() < config.getExamPerSessionRate()) {
                    // 해당 세션의 클래스 찾기
                    Class sessionClass = classes.stream()
                            .filter(c -> c.getClassId().equals(session.getClassId()))
                            .findFirst()
                            .orElse(null);
                    
                    if (sessionClass != null) {
                        // 시험 생성
                        Exam exam = createExam(session, sessionClass);
                        examRepository.save(exam);
                        
                        // 문제 생성 (5~8개 랜덤)
                        int questionCount = 5 + random.nextInt(4); // 5~8개
                        List<Question> questions = createQuestions(exam, questionCount);
                        questionRepository.saveAll(questions);
                        
                        // 수강생들의 제출물 생성
                        List<User> enrolledStudents = getEnrolledStudents(sessionClass.getClassId());
                        for (User student : enrolledStudents) {
                            Submission submission = createSubmission(exam, student, questions);
                            submissionRepository.save(submission);
                            
                            // 각 문제별 답안 생성
                            for (Question question : questions) {
                                SubmissionAnswer answer = createSubmissionAnswer(submission, question, student);
                                submissionAnswerRepository.save(answer);
                            }
                        }
                        
                        examCount++;
                    }
                }
            }
        }
        
        return examCount;
    }
    
    private Exam createExam(Session session, Class sessionClass) {
        String examName = sessionClass.getClassName() + " 중간고사";
        String difficulty = EXAM_DIFFICULTIES[random.nextInt(EXAM_DIFFICULTIES.length)];
        
        Exam exam = new Exam();
        exam.setSessionId(session.getSessionId());
        exam.setName(examName);
        exam.setDifficulty(difficulty);
        exam.setIsReady(true);
        exam.setCreatedBy(sessionClass.getTeacherId());
        exam.setCreatedAt(Instant.now());
        
        return exam;
    }
    
    private List<Question> createQuestions(Exam exam, int questionCount) {
        List<Question> questions = new ArrayList<>();
        
        // 과목에 따른 문제 세트 선택
        int subjectIndex = Math.abs(exam.getName().hashCode()) % EXAM_QUESTIONS.length;
        String[] subjectQuestions = EXAM_QUESTIONS[subjectIndex];
        String[] subjectChoices = MULTIPLE_CHOICE_OPTIONS[subjectIndex];
        String[] subjectAnswers = SHORT_ANSWER_ANSWERS;
        
        for (int i = 0; i < questionCount; i++) {
            QuestionType qtype = random.nextBoolean() ? QuestionType.MCQ : QuestionType.SHORT;
            String body = subjectQuestions[i % subjectQuestions.length];
            String choices = null;
            String answerKey = null;
            
            if (qtype == QuestionType.MCQ) {
                // 객관식인 경우
                choices = "[\"" + String.join("\",\"", subjectChoices[i % subjectChoices.length]) + "\"]";
                answerKey = String.valueOf(1 + random.nextInt(4)); // 1~4번 중 랜덤
            } else {
                // 단답형인 경우
                answerKey = subjectAnswers[i % subjectAnswers.length];
            }
            
            Question question = new Question();
            question.setExamId(exam.getId().intValue());
            question.setQtype(qtype);
            question.setBody(body);
            question.setChoices(choices);
            question.setAnswerKey(answerKey);
            question.setPoints(BigDecimal.valueOf(10)); // 각 문제 10점
            question.setPosition(i + 1);
            
            questions.add(question);
        }
        
        return questions;
    }
    
    private List<User> getEnrolledStudents(Integer classId) {
        List<Enrollment> enrollments = enrollmentRepository.findByClassId(classId);
        List<Integer> studentIds = enrollments.stream()
                .map(Enrollment::getStudentId)
                .collect(Collectors.toList());
        
        return userRepository.findAllById(studentIds);
    }
    
    private Submission createSubmission(Exam exam, User student, List<Question> questions) {
        // 답안들을 먼저 생성하여 총점 계산
        BigDecimal totalScore = BigDecimal.ZERO;
        List<SubmissionAnswer> answers = new ArrayList<>();
        
        for (Question question : questions) {
            SubmissionAnswer answer = createSubmissionAnswer(null, question, student);
            answers.add(answer);
            totalScore = totalScore.add(answer.getScore());
        }
        
        Submission submission = new Submission();
        submission.setExamId(exam.getId().intValue());
        submission.setUserId(student.getUserId());
        submission.setSubmittedAt(Instant.now());
        submission.setTotalScore(totalScore);
        submission.setFeedback("잘 했습니다! 더 노력하세요.");
        
        return submission;
    }
    
    private SubmissionAnswer createSubmissionAnswer(Submission submission, Question question, User student) {
        String answerText;
        boolean isCorrect;
        BigDecimal score;
        
        if (question.getQtype() == QuestionType.MCQ) {
            // 객관식 답안
            int studentAnswer = 1 + random.nextInt(4); // 1~4번 중 랜덤
            answerText = String.valueOf(studentAnswer);
            isCorrect = answerText.equals(question.getAnswerKey());
        } else {
            // 단답형 답안
            answerText = question.getAnswerKey(); // 정답으로 설정 (실제로는 다양한 답안이 있을 수 있음)
            isCorrect = random.nextDouble() < 0.8; // 80% 확률로 정답
        }
        
        score = isCorrect ? question.getPoints() : BigDecimal.ZERO;
        
        SubmissionAnswer answer = new SubmissionAnswer();
        answer.setExamId(question.getExamId());
        answer.setUserId(student.getUserId());
        answer.setQuestionId(question.getId().intValue());
        answer.setAnswerText(answerText);
        answer.setIsCorrect(isCorrect);
        answer.setScore(score);
        answer.setElapsedTimeSeconds(30 + random.nextInt(120)); // 30~150초
        
        return answer;
    }
    
    private List<Session> getStudentSessions(User student, List<Session> allSessions) {
        // 학생이 수강하는 클래스의 세션들만 반환
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student.getUserId());
        List<Integer> enrolledClassIds = enrollments.stream()
                .map(Enrollment::getClassId)
                .toList();
        
        return allSessions.stream()
                .filter(session -> enrolledClassIds.contains(session.getClassId()))
                .toList();
    }
    
    private Attendance.AttendanceStatus getRandomAttendanceStatus() {
        double rand = random.nextDouble();
        if (rand < 0.7) return Attendance.AttendanceStatus.PRESENT;
        if (rand < 0.85) return Attendance.AttendanceStatus.LATE;
        if (rand < 0.95) return Attendance.AttendanceStatus.EXCUSED;
        return Attendance.AttendanceStatus.ABSENT;
    }
    
    private String getRandomNote(Attendance.AttendanceStatus status) {
        switch (status) {
            case PRESENT:
                return "좋은 수업이었습니다";
            case LATE:
                return "지각했습니다";
            case EXCUSED:
                return "사유로 인한 결석";
            case ABSENT:
                return "무단 결석";
            default:
                return "";
        }
    }
    
    private void printStatistics(int teacherCount, int studentCount, int classCount, 
                                int sessionCount, int enrollmentCount, int attendanceCount, int examCount) {
        log.info("=== 데이터 생성 통계 ===");
        log.info("교사: {}명", teacherCount);
        log.info("학생: {}명", studentCount);
        log.info("클래스: {}개", classCount);
        log.info("세션: {}개", sessionCount);
        log.info("수강신청: {}개", enrollmentCount);
        log.info("출석기록: {}개", attendanceCount);
        log.info("시험: {}개", examCount);
        log.info("평균 출석률: {:.1f}%", (double) attendanceCount / (studentCount * sessionCount) * 100);
        log.info("=====================");
    }
}
