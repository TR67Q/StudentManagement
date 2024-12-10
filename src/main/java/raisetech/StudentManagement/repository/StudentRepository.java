package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.StudentCourseStatus;

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
   * 受講生の条件検索を行います
   * 名前、カナ名、メールアドレス、住所、年齢、性別
   *
   * @param name　名前
   * @param kanaName　カナ名
   * @param mailAddress　メールアドレス
   * @param area　住所
   * @param age　年齢
   * @param gender　性別
   * @return 該当する受講生詳細
   */
  List<Student> searchFilteredStudent(String name, String kanaName,
      String mailAddress, String area, Integer age, String gender);

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
   * 受講生コースの申込状況の新規登録を行います
   *
   * @param studentCourseStatus コースの申込状況
   */
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentCourseStatus(StudentCourseStatus studentCourseStatus);

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


  /**
   * 受講生コースの申込状況の全件検索を行います
   *
   * @return 受講生コースの申込状況（全件）
   */
  List<StudentCourseStatus> searchAllCourseStatuses();

  /**
   * 受講生コースの申込状況の条件検索を行います。任意のコース名の申込状況を表示します
   *
   * @param courseName コース名
   * @return 受講生コースの申込状況（任意のコース名）
   */
  List<StudentCourseStatus> searchCourseStatusesByCourseName(String courseName);

  /**
   * 受講生コースの申込状況の更新を行います。
   *
   * @param status 受講生コースの申込状況のステータス
   */
  void updateStudentCourseStatus(StudentCourseStatus status);

  /**
   * 受講生情報の削除を行います。特定のIDの受講生情報を削除します
   *
   * @param id 受講生ID
   * @return
   */
  void deleteStudentById(String id);

  /**
   * 受講生コース情報の削除を行います。特定のコース情報の受講生コース情報を削除します
   *
   * @param id 受講生コースID
   */
  void deleteStudentCourseById(String id);
}