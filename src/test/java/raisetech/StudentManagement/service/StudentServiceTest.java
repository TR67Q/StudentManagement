package raisetech.StudentManagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
  void 受講生詳細の新規登録_リポジトリの処理が適切に呼び出せていること(){
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);
  }

  @Test
  void 受講生詳細の登録_初期化処理が行われること(){
    String studentId = "99";
    Student student = new Student();
    student.setId(studentId);
    StudentCourse studentCourse = new StudentCourse();

    sut.initStudentsCourse(studentCourse, student.getId());

    assertThat(studentId)
        .isEqualTo(studentCourse.getStudentId());
    assertThat(LocalDateTime.now().getHour())
        .isEqualTo(studentCourse.getStartingDate().getHour());
    assertThat(LocalDateTime.now().plusYears(1).getYear())
        .isEqualTo(studentCourse.getEndDate().getYear());
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

}