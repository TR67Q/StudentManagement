package raisetech.StudentManagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.StudentCourseStatus;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentsCourseList()).thenReturn(studentCourseList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentsCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  void 受講生詳細の単一検索_リポジトリの処理が適切に呼び出せていること() {
    String studentId = "99";
    Student student = new Student();
    student.setId(studentId);
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.searchStudent(studentId)).thenReturn(student);
    when(repository.searchStudentCourseList(studentId)).thenReturn(studentCourseList);

    StudentDetail expected = new StudentDetail(student, studentCourseList);

    StudentDetail actual = sut.searchStudent(studentId);

    verify(repository, times(1)).searchStudent(studentId);
    verify(repository, times(1)).searchStudentCourseList(studentId);
    assertThat(expected.getStudent().getId())
        .isEqualTo(actual.getStudent().getId());
  }

  @Test
  void 受講生詳細の条件検索() {
    String name = "山田太郎";
    String kanaName = "ヤマダタロウ";
    String nickname = "タロ";
    String mailAddress = "taro@example.com";
    String area = "東京";
    int age = 27;
    String gender = "男性";

    Student student = new Student();
    List<Student> studentList = List.of(student);
    student.setName(name);
    student.setKanaName(kanaName);
    student.setNickname(nickname);
    student.setMailAddress(mailAddress);
    student.setArea(area);
    student.setAge(age);
    student.setGender(gender);

    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();

    when(repository.searchFilteredStudent(name, kanaName,mailAddress, area, age, gender))
        .thenReturn(studentList);
    when(repository.searchStudentsCourseList())
        .thenReturn(studentCourseList);
    when(repository.searchAllCourseStatuses())
        .thenReturn(studentCourseStatusList);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);
    List<StudentDetail> expected = List.of(studentDetail);

    List<StudentDetail> actual = sut.searchFilteredStudentList(name, kanaName,mailAddress, area, age, gender);

    verify(repository, times(1)).searchFilteredStudent(name, kanaName, mailAddress, area, age, gender);
    verify(repository, times(1)).searchStudentsCourseList();
    verify(repository, times(1)).searchAllCourseStatuses();

    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void 受講生詳細の新規登録_リポジトリの処理が適切に呼び出せていること(){
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(any());
    verify(repository, times(1)).registerStudentCourseStatus(any());
  }

  @Test
  void 受講生詳細の更新_リポジトリの処理が適切に呼び出せていること(){
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
  }

  @Test
  void 受講生コースの申込状況の更新() {
    StudentCourseStatus status = new StudentCourseStatus();
    status.setId("1");
    status.setCourseStatusId("1");
    status.setStatus("本申込");

    sut.updateStudentCourseStatus(status);

    verify(repository, times(1)).updateStudentCourseStatus(status);
  }

  @Test
  void 受講生コースの申込状況の全件検索() {
    List<StudentCourseStatus> statuses = List.of(new StudentCourseStatus());

    when(repository.searchAllCourseStatuses()).thenReturn(statuses);

    List<StudentCourseStatus> result = sut.getAllCourseStatuses();

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(repository, times(1)).searchAllCourseStatuses();
  }

  @Test
  void 受講生コースの申込状況の条件検索() {
    String courseName = "Java";
    List<StudentCourseStatus> statuses = List.of(new StudentCourseStatus());

    when(repository.searchCourseStatusesByCourseName(courseName)).thenReturn(statuses);

    List<StudentCourseStatus> result = sut.getCourseStatusesByCourseName(courseName);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(repository, times(1)).searchCourseStatusesByCourseName(courseName);
  }

  @Test
  void 受講生情報の論理削除() {
    String id = "99";

    sut.deleteStudent(id);

    verify(repository, times(1)).deleteStudentById(id);
  }

  @Test
  void 受講生コース情報の論理削除() {
    String id = "99";

    sut.deleteStudentCourse(id);

    verify(repository, times(1)).deleteStudentCourseById(id);
  }

}