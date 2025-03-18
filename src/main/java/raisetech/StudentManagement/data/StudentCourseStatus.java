package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コースの申込状況")
@Getter
@Setter
public class StudentCourseStatus {

  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください。")
  private String id;

  private String courseName;

  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください。")
  private String courseStatusId;

  private String status;
}
