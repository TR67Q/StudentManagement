package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コースの申込状況")
@Getter
@Setter
public class StudentCourseStatus {

  private String id;
  private String courseName;
  private String courseStatusId;
  private String status;
}
