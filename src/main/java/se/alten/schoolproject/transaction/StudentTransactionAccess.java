package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.StudentEntity;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StudentTransactionAccess {
    List listAllStudents();
    StudentEntity addStudent(StudentEntity studentToAdd);
    void removeStudent(String student);
    void updateStudent(String foreName, String lastName, String email);
    void updateStudentPartial(Student studentToUpdate);
}
