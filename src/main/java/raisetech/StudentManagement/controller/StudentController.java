package raisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うRestAPIとして受け付けるController
 */
@RestController
public class StudentController {

  private final StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細一覧検索機能
   * 全件検索を行うので、条件指定は行いません
   *
   * @return 受講生詳細一覧（全件）
   */
  @GetMapping("/studentList")
    public List<StudentDetail> getStudentList(){
    return service.searchStudentList();
  }

  /**
   * 受講生詳細検索機能
   * IDに紐づく任意の受講生情報を取得します
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable String id){
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の新規登録機能
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail){
    StudentDetail responseStudentDetail1 = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail1);
  }

  /**
   * 受講生詳細の更新機能
   * キャンセルフラグの更新もここで行います（論理削除）
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail){
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }
}
