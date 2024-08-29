package raisetech.StudentManagement.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private final StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList(){
    return repository.search();
  }

  public List<StudentCourse> searchStudentCourseList(){
    return repository.searchCourse();
  }

  public List<Student> searchStudentListByAge(){
    return repository.search().stream()
        .filter(student -> student.getAge() >= 30 && student.getAge() <= 39)
        .collect(Collectors.toList());
  }

  public List<StudentCourse> searchStudentCourseListByCourseName(){
    return repository.searchCourse().stream()
        .filter(studentCourse -> studentCourse.getCourseName().equals("Java"))
        .collect(Collectors.toList());
  }
}
