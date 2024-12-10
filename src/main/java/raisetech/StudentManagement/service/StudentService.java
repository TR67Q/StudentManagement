package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.data.StudentCourseStatus;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービス
 * 受講生の検索や登録・更新処理を行います
 */
@Service
public class StudentService {

  public StudentRepository repository;
  public StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生詳細の一覧検索機能
   * 全件検索を行うので、条件指定は行いません
   *
   * @return 受講生詳細一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList = repository.searchStudentsCourseList();
    List<StudentCourseStatus> studentCourseStatusList = repository.searchAllCourseStatuses();

    Map<String, List<StudentCourseStatus>> statusMap = studentCourseStatusList.stream()
        .collect(Collectors.groupingBy(StudentCourseStatus::getId));

    for (StudentCourse course : studentCourseList) {
      course.setStudentCourseStatusList(statusMap.getOrDefault(course.getStudentId(), new ArrayList<>()));
    }

    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * 受講生詳細検索機能
   * IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchStudent(String id){
    Student student = repository.searchStudent(id);
    List<StudentCourse> studentCourses = repository.searchStudentCourseList(student.getId());
    List<StudentCourseStatus> studentCourseStatusList = repository.searchAllCourseStatuses();
    for (StudentCourse course : studentCourses) {
      List<StudentCourseStatus> statuses = studentCourseStatusList.stream()
          .filter(status -> status.getId().equals(course.getStudentId()))
          .collect(Collectors.toList());
      course.setStudentCourseStatusList(statuses);
    }
    return new StudentDetail(student, studentCourses);
  }

  /**
   * 受講生情報の条件検索機能
   *
   * @param name 名前
   * @param kanaName カナ名
   * @param mailAddress メールアドレス
   * @param area 住所
   * @param age 年齢
   * @param gender 性別
   * @return 条件に該当する受講生詳細
   */
  public List<StudentDetail> searchFilteredStudentList(String name, String kanaName, String mailAddress, String area, Integer age, String gender) {
    List<Student> studentList = repository.searchFilteredStudent(name, kanaName, mailAddress, area, age, gender);
    List<StudentCourse> studentCourseList = repository.searchStudentsCourseList();
    List<StudentCourseStatus> studentCourseStatusList = repository.searchAllCourseStatuses();

    for (StudentCourse studentCourse : studentCourseList) {
      List<StudentCourseStatus> statusList = studentCourseStatusList.stream()
          .filter(studentCourseStatus -> studentCourseStatus.getId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());
      studentCourse.setStudentCourseStatusList(statusList);
    }

    return studentList.stream()
        .map(student -> {
          List<StudentCourse> courseList = studentCourseList.stream()
              .filter(studentCourse -> studentCourse.getStudentId().equals(student.getId()))
              .collect(Collectors.toList());

          return new StudentDetail(student, courseList);
        })
        .collect(Collectors.toList());
  }

  /**
   * 受講生詳細の新規登録機能
   * 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日、コース終了日を設定します
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.registerStudent(student);

    for (StudentCourse studentCourse : studentDetail.getStudentCourseList()) {
      studentCourse.setStudentId(student.getId());

      LocalDateTime now = LocalDateTime.now();
      studentCourse.setStartingDate(now);
      studentCourse.setEndDate(now.plusYears(1));

      repository.registerStudentCourse(studentCourse);

      StudentCourseStatus courseStatus = new StudentCourseStatus();
      courseStatus.setCourseName(studentCourse.getCourseName());
      courseStatus.setCourseStatusId(studentCourse.getId());
      courseStatus.setStatus("仮申込");
      repository.registerStudentCourseStatus(courseStatus);
    }
    return studentDetail;

  }

  /**
   * 受講生詳細の更新を行います
   * 受講生の情報と受講生コース情報をそれぞれ更新します
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    studentDetail.getStudentCourseList().forEach(studentCourse -> {
      studentCourse.setStudentId(studentDetail.getStudent().getId());
      repository.updateStudentCourse(studentCourse);
    });
  }

  /**
   * 受講生コースの申込状況のステータスを更新します
   *
   * @param status　コースの申込状況のステータス
   */
  @Transactional
  public void updateStudentCourseStatus(StudentCourseStatus status) {
    repository.updateStudentCourseStatus(status);
  }

  /**
   * 受講生コースの申込状況の全件検索を行います
   *
   * @return コースの申込状況（全件）
   */
  public List<StudentCourseStatus> getAllCourseStatuses() {
    return repository.searchAllCourseStatuses();
  }

  /**
   * 受講生コースの申込状況の条件検索を行います
   * 特定のコースを受講している申込状況を検索します
   *
   * @param courseName コース名
   * @return コースの申込状況（特定のコース名）
   */
  public List<StudentCourseStatus> getCourseStatusesByCourseName(String courseName) {
    return repository.searchCourseStatusesByCourseName(courseName);
  }

  /**
   * 受講生情報の論理削除を行います
   *
   * @param id 受講生ID
   */
  @Transactional
  public void deleteStudent(String id) {
    repository.deleteStudentById(id);
  }

  /**
   * 受講生コース情報の論理削除を行います
   *
   * @param id 受講生コースID
   */
  @Transactional
  public void deleteStudentCourse(String id) {
    repository.deleteStudentCourseById(id);
  }
}
