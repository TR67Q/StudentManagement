package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルに紐づくRepository
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います
   *
   * @return 受講生一覧（全件）
   */
  List<Student> search();

  /**
   * 受講生の単一検索を行います
   *
   * @param id 受講生ID
   * @return 受講生
   */
  Student searchStudent(String id);

  /**
   * 受講生のコース情報の全件検索を行います
   *
   * @return 受講生のコース情報（全件）
   */
  List<StudentCourse> searchStudentsCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourseList(String studentId);

  /**
   * 受講生を新規登録を行います。また、IDに関しては自動採番を行います
   *
   * @param student 受講生
   */
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  /**
   * 受講生コース情報の新規登録を行います。また、IDに関しては自動採番を行います
   *
   * @param studentCourse 受講生コース情報
   */
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生を更新します
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します
   *
   * @param studentCourse 受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);
}