package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.StudentCourseStatus;

@MybatisTest
class StudentRepositoryTest {

  @Mock
  private SqlSessionTemplate sqlSession;

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること(){
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生の単一検索が行えること(){
    String id = "1";
    Student actual = sut.searchStudent(id);
    assertThat(actual.getId()).isEqualTo(id);
  }

  @Test
  void 受講生コース情報の全件検索が行えること(){
    List<StudentCourse> actual = sut.searchStudentsCourseList();
    assertThat(actual.size()).isEqualTo(7);
  }

  @Test
  void 受講生の条件検索が行えること() {
    Student student = createStudent();
    List<Student> actual = sut.searchFilteredStudent(
        student.getName(), student.getKanaName(), student.getMailAddress(), student.getArea(), student.getAge(), student.getGender());
    assertThat(actual.size()).isEqualTo(1);
  }

  @Test
  void 受講生コース情報の単一検索が行えること(){
    String id = "1";
    List<StudentCourse> actual = sut.searchStudentCourseList(id);
    assertThat(actual.size()).isEqualTo(2);
  }


  @Test
  void 受講生の新規登録が行えること(){
    Student student = createStudent();

    sut.registerStudent(student);

    List<Student> mockStudent = sut.search();

    assertThat(mockStudent.size()).isEqualTo(6);
  }

  @Test
  void 受講生コース情報の新規登録が行えること(){
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("6");
    studentCourse.setCourseName("Java");
    studentCourse.setStartingDate(LocalDateTime.now());
    studentCourse.setEndDate(LocalDateTime.now().plusYears(1));

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentsCourseList();

    assertThat(actual.size()).isEqualTo(8);
  }

  @Test
  void 受講生コースの申込状況の新規登録() {
    StudentCourseStatus status = new StudentCourseStatus();
    status.setCourseName("Java");
    status.setCourseStatusId("99");
    status.setStatus("仮申込");

    sut.registerStudentCourseStatus(status);

    List<StudentCourseStatus> actual = sut.searchAllCourseStatuses();

    assertThat(actual.size()).isEqualTo(8);
  }

  @Test
  void 受講生情報の更新が行えること(){
    Student student = createStudent();

    sut.updateStudent(student);

    Student updatedStudent = sut.searchStudent("1");
    assertThat(updatedStudent.getName()).isEqualTo("山田太郎");
    assertThat(updatedStudent.getMailAddress()).isEqualTo("taro@example.com");
  }

  @Test
  void 受講生コース情報のコース名の更新が行えること(){
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("Music");

    sut.updateStudentCourse(studentCourse);

    StudentCourse updatedStudentCourse = sut.searchStudentCourseList("1").get(0);
    assertThat(updatedStudentCourse.getCourseName()).isEqualTo("Music");
  }

  @Test
  void 受講生コースの申込状況の全件検索() {
    List<StudentCourseStatus> actual = sut.searchAllCourseStatuses();
    assertThat(actual.size()).isEqualTo(7);
  }

  @Test
  void 受講生コースの申込状況について特定のコースを受講している受講生の状況が検索できること() {
    String courseName = "Java";

    List<StudentCourseStatus> actual = sut.searchCourseStatusesByCourseName(courseName);

    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void 受講生情報の論理削除() {
    String id = "1";

    sut.deleteStudentById(id);

    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(4);
  }

  @Test
  void 受講生コース情報の論理削除() {
    String id = "1";

    sut.deleteStudentCourseById(id);

    List<StudentCourse> actual = sut.searchStudentsCourseList();
    assertThat(actual.size()).isEqualTo(6);
  }



  private Student createStudent() {
    Student student = new Student();
    student.setName("山田太郎");
    student.setKanaName("ヤマダタロウ");
    student.setNickname("タロ");
    student.setMailAddress("taro@example.com");
    student.setArea("東京");
    student.setAge(27);
    student.setGender("男性");
    student.setRemark("");
    student.setDeleted(false);
    return student;
  }
}