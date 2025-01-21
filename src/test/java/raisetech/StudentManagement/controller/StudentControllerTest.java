package raisetech.StudentManagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.StudentCourseStatus;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private StudentService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の単一検索が実行できて対象となる受講生のリストが返ってくること() throws Exception {
    String id = "999";
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細の条件検索が実行できて空のリストが返ってくること() throws Exception {
    when(service.searchFilteredStudentList(any(), any(), any(), any(), any(), any()))
        .thenReturn(List.of());

    mockMvc.perform(get("/filteredStudentList")
            .param("name", "山田太郎")
            .param("gender", "男性"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchFilteredStudentList(any(), any(), any(), any(), any(), any());
  }

  @Test
  void 受講生詳細の新規登録が実行できて登録された受講生のリストが返ってくること() throws Exception {
    mockMvc.perform(post("/registerStudent").contentType(MediaType.APPLICATION_JSON).content(
        """
            {
                "student": {
                    "name": "江波公史",
                    "kanaName": "エナミコウジ",
                    "nickname": "コウジ",
                    "mailAddress": "test@example.com",
                    "area": "奈良",
                    "age": "37",
                    "gender": "男性",
                    "remark": ""
                },
                "studentCourseList": [
                    {
                        "courseName": "Java"
                    }
                ]
            }
        """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }

  @Test
  void 受講生詳細の更新が実行できて特定のメッセージが表示されること() throws Exception {
    mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON).content(
            """
                {
                    "student": {
                        "name": "江波公史",
                        "kanaName": "エナミコウジ",
                        "nickname": "コウジ",
                        "mailAddress": "test@example.com",
                        "area": "奈良",
                        "age": "37",
                        "gender": "男性",
                        "remark": ""
                    },
                    "studentCourseList": [
                        {
                            "courseName": ""
                        }
                    ]
                }
           """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any());
  }

  @Test
  void 受講生コースの申込状況の更新が行えること() throws Exception {
    StudentCourseStatus status = new StudentCourseStatus();

    mockMvc.perform(put("/updateCourseStatus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(status)))
        .andExpect(status().isOk())
        .andExpect(content().string("受講生コース申込状況の更新に成功しました。"));

    verify(service, times(1)).updateStudentCourseStatus(any());
  }

  @Test
  void 受講生情報の論理削除が行えること() throws Exception {
    String id = "99";

    mockMvc.perform(delete("/students/{id}", id))
        .andExpect(status().isOk())
        .andExpect(content().string("受講生情報を削除しました。"));

    verify(service, times(1)).deleteStudent(id);
  }

  @Test
  void 受講生コース情報の論理削除が行えること() throws Exception {
    String id = "99";

    mockMvc.perform(delete("/students/courses/{id}", id))
        .andExpect(status().isOk())
        .andExpect(content().string("受講生コース情報を削除しました。"));

    verify(service, times(1)).deleteStudentCourse(id);
  }

  @Test
  void 受講生コースの申込状況の全件検索が行えること() throws Exception {
    when(service.getAllCourseStatuses()).thenReturn(List.of());

    mockMvc.perform(get("/courseStatuses"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).getAllCourseStatuses();
  }

  @Test
  void 受講生コースの条件検索が行えること() throws Exception {
    String courseName = "Java";
    when(service.getCourseStatusesByCourseName(courseName)).thenReturn(List.of());

    mockMvc.perform(get("/searchCourseStatuses")
            .param("courseName", courseName))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).getCourseStatusesByCourseName(courseName);
  }


  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setId("1");
    student.setName("江波公史");
    student.setKanaName("エナミコウジ");
    student.setNickname("エナミ");
    student.setMailAddress("test@example.com");
    student.setArea("奈良");
    student.setGender("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに掛かること() {
    Student student = new Student();
    student.setId("テストです。");
    student.setName("江波公史");
    student.setKanaName("エナミコウジ");
    student.setNickname("エナミ");
    student.setMailAddress("test@example.com");
    student.setArea("奈良");
    student.setGender("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力するようにしてください。");
  }

  @Test
  void 受講生詳細の受講生コース情報で適切な値を入力した時に入力チェックに異常が発生しないこと(){
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("1");
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("Java");
    studentCourse.setStartingDate(LocalDateTime.now());
    studentCourse.setEndDate(LocalDateTime.now().plusYears(1));

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生コース情報でIDに数字以外を用いた時に入力チェックに掛かること(){
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("テストです。");
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("Java");
    studentCourse.setStartingDate(LocalDateTime.now());
    studentCourse.setEndDate(LocalDateTime.now().plusYears(1));

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力するようにしてください。");
  }
}