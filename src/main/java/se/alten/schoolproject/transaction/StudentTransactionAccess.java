package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.StudentEntity;
import se.alten.schoolproject.exceptions.DuplicateEntityException;
import se.alten.schoolproject.exceptions.NotFoundException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StudentTransactionAccess {
    List<StudentEntity> listAllStudents();
    StudentEntity findStudentByEmail(String studentEmail);
    StudentEntity addStudent(StudentEntity studentToAdd) throws DuplicateEntityException;
    void removeStudent(String student) throws NotFoundException;
    void updateStudent(String foreName, String lastName, String email) throws NotFoundException;
    List<StudentEntity> findStudent(String foreName, String lastName);
}
