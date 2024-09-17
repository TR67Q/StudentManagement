package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();

  @Insert("INSERT INTO students (name, kana_name, nickname, mail_address, area, age, gender, remark, isDeleted) "
      + "VALUES (#{name}, #{kanaName}, #{nickname}, #{mailAddress}, #{area}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true,keyProperty = "id")
  void registerStudent(Student student);
}