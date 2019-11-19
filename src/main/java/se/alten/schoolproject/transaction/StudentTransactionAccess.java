package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.StudentEntity;
import se.alten.schoolproject.exceptions.BadRequestException;
import se.alten.schoolproject.exceptions.NotFoundException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StudentTransactionAccess {
    List listAllStudents();
    void addStudent(StudentEntity studentToAdd) throws BadRequestException;
    void removeStudent(String student) throws NotFoundException;
    void updateStudent(String foreName, String lastName, String email) throws NotFoundException;
    List findStudent(String foreName, String lastName);
}
