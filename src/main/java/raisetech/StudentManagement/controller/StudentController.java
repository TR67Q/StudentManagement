package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.TestException;
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
    StudentDetail responseStudentDetail1 = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail1);
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

  @Operation(summary = "例外処理", description = "例外処理を行った後、エラーメッセージを返します。")
  @GetMapping("/studentListException")
  public List<StudentDetail> getStudentListException() throws TestException {
    throw new TestException("エラーが発生しました。");
  }

}
