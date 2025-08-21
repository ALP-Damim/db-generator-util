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
    
    // 학교 목록 (초등학교)
    private static final String[] SCHOOLS = {
        "서울초등학교", "연세초등학교", "고려초등학교", "성균관초등학교", "한양초등학교",
        "중앙초등학교", "경희초등학교", "동국초등학교", "홍익초등학교", "건국초등학교"
    };
    
    // 희망 과목 목록 (초등학교)
    private static final String[] DESIRED_COURSES = {
        "과학", "수학", "영어", "국어", "사회", "음악", "미술", "체육", "실과", "도덕"
    };
    
    // 희망 직업 목록 (초등학교)
    private static final String[] DESIRED_JOBS = {
        "교사", "의사", "과학자", "엔지니어", "예술가",
        "운동선수", "요리사", "경찰관", "소방관", "간호사"
    };
    
    // 과목명 목록 (초등학교)
    private static final String[] SUBJECTS = {
        "과학", "수학", "영어", "국어"
    };
    
    // 과목 코드 (0: 과학, 1: 수학, 2: 영어, 3: 국어)
    private static final int[] SUBJECT_CODES = {0, 1, 2, 3};
    
    // 학년 목록 (정수)
    private static final int[] SCHOOL_YEARS = {3, 4};
    
    // 소단원 목록 (과목별)
    private static final String[][] SUBJECT_UNITS = {
        // 과학 (0)
        {"식물의 생활", "동물의 생활", "물의 상태 변화", "지구와 달", "전기와 자기", "소리와 빛", "산과 염기", "대기와 날씨", "생태계와 환경", "에너지와 생활"},
        // 수학 (1)
        {"덧셈과 뺄셈", "곱셈과 나눗셈", "분수", "소수", "도형", "측정", "자료와 가능성", "규칙성", "비례식", "정비례와 반비례"},
        // 영어 (2)
        {"인사하기", "자기소개", "가족 소개", "취미와 관심사", "학교생활", "시간과 날짜", "날씨와 계절", "음식과 음료", "색깔과 모양", "숫자와 셈하기"},
        // 국어 (3)
        {"듣기와 말하기", "읽기", "쓰기", "문법", "문학", "어휘", "한자", "독서", "토론", "글짓기"}
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
    
    // 시험 문제 세트 (초등학교 과목별)
    private static final String[][] EXAM_QUESTIONS = {
        // 과학 (0)
        {
            "식물이 자라는데 필요한 것 중 가장 중요한 것은?",
            "동물의 생태계에서 포식자와 피식자의 관계는?",
            "물의 상태 변화 중 액체에서 기체로 변하는 것은?",
            "지구의 자전으로 인해 생기는 현상은?",
            "전기 회로에서 전류가 흐르는 방향은?",
            "소리의 높낮이를 결정하는 요소는?",
            "빛의 반사 현상이 일어나는 이유는?",
            "산과 염기의 중화 반응에서 생성되는 것은?",
            "지구의 대기권에서 가장 많은 기체는?",
            "생물의 적응 현상 중 계절에 따른 변화는?"
        },
        // 수학 (1)
        {
            "3학년 수학: 25 + 37 = ?",
            "4학년 수학: 156 ÷ 12 = ?",
            "3학년 수학: 1/2 + 1/4 = ?",
            "4학년 수학: 0.5 × 0.3 = ?",
            "3학년 수학: 1000 - 234 = ?",
            "4학년 수학: 3/4 × 2/3 = ?",
            "3학년 수학: 15 × 6 = ?",
            "4학년 수학: 2.5 + 1.75 = ?",
            "3학년 수학: 1/3 + 1/6 = ?",
            "4학년 수학: 0.8 ÷ 0.2 = ?"
        },
        // 영어 (2)
        {
            "What is your name?의 뜻은?",
            "How old are you?에 대한 답으로 적절한 것은?",
            "I like apples.에서 like의 뜻은?",
            "This is a book.에서 this의 뜻은?",
            "Where do you live?의 뜻은?",
            "What time is it?의 뜻은?",
            "I can swim.에서 can의 뜻은?",
            "She is beautiful.에서 beautiful의 뜻은?",
            "What color is it?의 뜻은?",
            "How many students are there?의 뜻은?"
        },
        // 국어 (3)
        {
            "다음 중 명사가 아닌 것은?",
            "'아름다운'의 품사는?",
            "다음 중 맞춤법이 올바른 것은?",
            "'달리다'의 활용형이 아닌 것은?",
            "다음 중 문장이 올바른 것은?",
            "'사랑하다'의 어간은?",
            "다음 중 부사가 아닌 것은?",
            "'먹다'의 과거형은?",
            "다음 중 조사가 아닌 것은?",
            "'가다'의 명령형은?"
        }
    };
    
    // 객관식 선택지 세트 (초등학교 과목별)
    private static final String[][] MULTIPLE_CHOICE_OPTIONS = {
        // 과학 (0)
        {"물", "햇빛", "공기", "흙"},
        {"먹고 먹히는 관계", "친구 관계", "경쟁 관계", "협력 관계"},
        {"증발", "응결", "응고", "융해"},
        {"낮과 밤", "계절의 변화", "조수간만", "지구의 공전"},
        {"양극에서 음극으로", "음극에서 양극으로", "무작위로", "정지"},
        {"진동수", "진폭", "파장", "속도"},
        {"빛이 매질의 경계에서 튀어나오기 때문", "빛이 흡수되기 때문", "빛이 굴절되기 때문", "빛이 산란되기 때문"},
        {"소금", "산", "염기", "물"},
        {"질소", "산소", "이산화탄소", "수소"},
        {"동면", "이동", "탈피", "번식"},
        
        // 수학 (1)
        {"62", "63", "64", "65"},
        {"13", "14", "15", "16"},
        {"3/4", "2/6", "1/6", "2/4"},
        {"0.15", "0.8", "1.5", "0.08"},
        {"766", "776", "786", "796"},
        {"1/2", "6/12", "1/12", "5/12"},
        {"90", "85", "95", "80"},
        {"4.25", "4.15", "4.35", "4.05"},
        {"1/2", "1/6", "2/6", "1/9"},
        {"4", "0.4", "40", "0.04"},
        
        // 영어 (2)
        {"당신의 이름은 무엇입니까?", "당신은 몇 살입니까?", "당신은 어디에 삽니까?", "당신은 무엇을 좋아합니까?"},
        {"I am 10 years old", "My name is Kim", "I live in Seoul", "I like pizza"},
        {"좋아하다", "싫어하다", "먹다", "마시다"},
        {"이것", "저것", "그것", "무엇"},
        {"당신은 어디에 삽니까?", "당신은 몇 살입니까?", "당신의 이름은 무엇입니까?", "당신은 무엇을 좋아합니까?"},
        {"지금 몇 시입니까?", "오늘 날씨는 어떻습니까?", "오늘은 무슨 요일입니까?", "오늘은 몇 월 며칠입니까?"},
        {"~할 수 있다", "~해야 한다", "~하고 싶다", "~해야 한다"},
        {"아름다운", "큰", "작은", "빠른"},
        {"무슨 색입니까?", "무엇입니까?", "어디입니까?", "언제입니까?"},
        {"학생이 몇 명입니까?", "교실이 몇 개입니까?", "책이 몇 권입니까?", "펜이 몇 개입니까?"},
        
        // 국어 (3)
        {"달리다", "학교", "친구", "책상"},
        {"형용사", "명사", "동사", "부사"},
        {"되다", "돼다", "안되다", "안돼다"},
        {"달린다", "달렸다", "달리자", "달리면"},
        {"나는 학교에 간다", "나는 학교에 가다", "나는 학교에 가고", "나는 학교에 가서"},
        {"사랑하", "사랑하다", "사랑한", "사랑할"},
        {"빨리", "아름답게", "크게", "학교"},
        {"먹었다", "먹는다", "먹자", "먹으면"},
        {"은", "있다", "없다", "많다"},
        {"가라", "가다", "간다", "갔다"}
    };
    
    // 단답형 답안 세트 (초등학교 과목별)
    private static final String[][] SHORT_ANSWER_ANSWERS = {
        // 과학 (0)
        {"물", "먹고 먹히는 관계", "증발", "낮과 밤", "음극에서 양극으로", "진동수", "빛이 매질의 경계에서 튀어나오기 때문", "소금", "질소", "동면"},
        // 수학 (1)
        {"62", "13", "3/4", "0.15", "766", "1/2", "90", "4.25", "1/2", "4"},
        // 영어 (2)
        {"당신의 이름은 무엇입니까?", "I am 10 years old", "좋아하다", "이것", "당신은 어디에 삽니까?", "지금 몇 시입니까?", "~할 수 있다", "아름다운", "무슨 색입니까?", "학생이 몇 명입니까?"},
        // 국어 (3)
        {"달리다", "형용사", "되다", "달린다", "나는 학교에 간다", "사랑하", "빨리", "먹었다", "은", "가라"}
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
                    .email("teacher" + (i + 1) + "@elementary.edu")
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
                    .email("student" + (i + 1) + "@elementary.edu")
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
                    .birthDate(generateRandomBirthDate(25, 50)) // 초등학교 교사는 25-50세
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
                    .birthDate(generateRandomBirthDate(8, 11)) // 초등학교 3-4학년 학생은 8-11세
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
                int subjectCode = SUBJECT_CODES[classIndex % SUBJECT_CODES.length];
                String subject = SUBJECTS[subjectCode];
                int schoolYear = SCHOOL_YEARS[random.nextInt(SCHOOL_YEARS.length)];
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
                
                // 소단원 선택
                String[] units = SUBJECT_UNITS[subjectCode];
                String unit = units[random.nextInt(units.length)];
                
                Class classEntity = Class.builder()
                        .teacherId(teacher.getUserId())
                        .teacherName(TEACHER_NAMES[teachers.indexOf(teacher) % TEACHER_NAMES.length])
                        .className(schoolYear + "학년 " + subject + " - " + unit)
                        .semester(semester)
                        .schoolYear(schoolYear)
                        .subject(subjectCode)
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
                        List<Question> questions = createQuestions(exam, questionCount, sessionClass);
                        questionRepository.saveAll(questions);
                        
                        // 수강생들의 제출물 생성
                        List<User> enrolledStudents = getEnrolledStudents(sessionClass.getClassId());
                        for (User student : enrolledStudents) {
                            Submission submission = createSubmission(exam, student, questions);
                            submissionRepository.save(submission);
                            
                            // 각 문제별 답안 생성
                            for (Question question : questions) {
                                SubmissionAnswer answer = createSubmissionAnswer(submission, question, student, sessionClass);
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
        String examName = sessionClass.getClassName() + " 단원평가";
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
    
    private List<Question> createQuestions(Exam exam, int questionCount, Class sessionClass) {
        List<Question> questions = new ArrayList<>();
        
        // 클래스의 과목 정보를 사용하여 문제 세트 선택
        int subjectCode = sessionClass.getSubject();
        String[] subjectQuestions = EXAM_QUESTIONS[subjectCode];
        String[] subjectChoices = MULTIPLE_CHOICE_OPTIONS[subjectCode];
        String[] subjectAnswers = SHORT_ANSWER_ANSWERS[subjectCode];
        
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
            SubmissionAnswer answer = createSubmissionAnswer(null, question, student, null);
            answers.add(answer);
            totalScore = totalScore.add(answer.getScore());
        }
        
        Submission submission = new Submission();
        submission.setExamId(exam.getId().intValue());
        submission.setUserId(student.getUserId());
        submission.setSubmittedAt(Instant.now());
        submission.setTotalScore(totalScore);
        submission.setFeedback("잘 했습니다! 더 노력하세요.");
        // 피드백 상태/요청 시간/재시도 횟수 설정
        // 40% 확률로 피드백 요청, 그 외 NONE
        double fr = random.nextDouble();
        if (fr < 0.4) {
            submission.setFeedbackStatus(Submission.FeedbackStatus.REQUESTED);
            // 제출 시점 이후 0~10분 내 요청
            submission.setFeedbackRequestedAt(Instant.now().plusSeconds(random.nextInt(600)));
            // 재시도는 0~2회 랜덤
            submission.setFeedbackRetryCount(random.nextInt(3));
        } else {
            submission.setFeedbackStatus(Submission.FeedbackStatus.NONE);
            submission.setFeedbackRequestedAt(null);
            submission.setFeedbackRetryCount(0);
        }
        
        return submission;
    }
    
    private SubmissionAnswer createSubmissionAnswer(Submission submission, Question question, User student, Class sessionClass) {
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
        answer.setSolvingTime(30 + random.nextInt(120)); // 30~150초
        
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
