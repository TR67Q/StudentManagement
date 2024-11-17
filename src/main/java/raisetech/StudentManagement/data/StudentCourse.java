package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {

  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください。")
  private String id;

  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにしてください。")
  private String studentId;

  private String courseName;

  private LocalDateTime startingDate;
  private LocalDateTime endDate;
}
