package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.data.StudentCourseStatus;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うRestAPIとして受け付けるController
 */
@Validated
@RestController
public class StudentController {

  private final StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細一覧検索機能 全件検索を行うので、条件指定は行いません
   *
   * @return 受講生詳細一覧（全件）
   */
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。",
  responses = {@ApiResponse(responseCode = "200", description = "受講生詳細一覧を全件表示します。")})
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList(){
    return service.searchStudentList();
  }

  /**
   * 受講生詳細検索機能 IDに紐づく任意の受講生情報を取得します
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  @Operation(summary = "受講生詳細検索", description = "任意の受講生の受講生詳細を検索します。",
  responses = {@ApiResponse(responseCode = "200", description = "指定した受講生IDに紐づく受講生詳細を表示します。")})
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable @NotBlank
  @Size(min = 1, max = 3) @Pattern(regexp = "^\\d+$") String id) {
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の条件検索機能 名前、カナ名、メールアドレス、住所、年齢、性別を選択して検索します
   *
   * @param name        名前
   * @param kanaName    カナ名
   * @param mailAddress メールアドレス
   * @param area        住所
   * @param age         年齢
   * @param gender      性別
   * @return 受講生詳細
   */
  @GetMapping("/filteredStudentList")
  public ResponseEntity<Object> filteredStudentList(@RequestParam(required = false) String name,
      @RequestParam(required = false) String kanaName,
      @RequestParam(required = false) String mailAddress,
      @RequestParam(required = false) String area,
      @RequestParam(required = false) Integer age,
      @RequestParam(required = false) String gender) {
    List<StudentDetail> filteredStudentList = service.searchFilteredStudentList(name, kanaName, mailAddress, area, age, gender);
    if (filteredStudentList.isEmpty()) {
      return ResponseEntity.ok(Collections.emptyList());  // ここで 200 OK と空リストを返す
    }
    return ResponseEntity.ok(filteredStudentList);
  }

  /**
   * 受講生詳細の新規登録機能
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生新規登録", description = "受講生を新規登録します。",
      responses = {@ApiResponse(responseCode = "200", description = "登録された受講生詳細を表示します。")})
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    return ResponseEntity.ok(service.registerStudent(studentDetail));
  }

  /**
   * 受講生詳細の更新機能 キャンセルフラグの更新もここで行います（論理削除）
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生詳細更新処理", description = "受講生詳細を更新します。",
  responses = {@ApiResponse(responseCode = "200", description = "更新処理が成功しました。")})
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  /**
   * 受講生コースの申込状況の更新機能
   * 受講生コースの申込状況
   *
   * @param status 受講コースのステータス
   * @return message
   */
  @PutMapping("/updateCourseStatus")
  public ResponseEntity<String> updateCourseStatus(@RequestBody @Valid StudentCourseStatus status) {
    service.updateStudentCourseStatus(status);
    return ResponseEntity.ok("受講生コース申込状況の更新に成功しました。");
  }

  /**
   * 受講生コースの申込状況の全件検索を行います
   *
   * @return コースの申込状況（全件）
   */
  @GetMapping("/courseStatuses")
  public List<StudentCourseStatus> getAllCourseStatuses() {
    return service.getAllCourseStatuses();
  }

  /**
   * 受講生コースの申込状況について
   *
   * @param courseName 受講コース名
   * @return コースの申込状況（任意のコース名）
   */
  @GetMapping("/searchCourseStatuses")
  public List<StudentCourseStatus> searchCourseStatuses(@RequestParam String courseName) {
    return service.getCourseStatusesByCourseName(courseName);
  }

  /**
   * 受講生情報の論理削除を行います
   *
   * @param id 受講生ID
   * @return message
   */
  @DeleteMapping("/students/{id}")
  public ResponseEntity<String> deleteStudent(@PathVariable String id) {
    service.deleteStudent(id);
    return ResponseEntity.ok("受講生情報を削除しました。");
  }

  /**
   * 受講生コース情報の論理削除を行います
   *
   * @param id 受講生コースID
   * @return message
   */
  @DeleteMapping("/students/courses/{id}")
  public ResponseEntity<String> deleteStudentCourse(@PathVariable String id) {
    service.deleteStudentCourse(id);
    return ResponseEntity.ok("受講生コース情報を削除しました。");
  }

}
