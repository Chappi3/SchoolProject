package se.alten.schoolproject.dao;

import se.alten.schoolproject.exceptions.BadRequestException;
import se.alten.schoolproject.exceptions.NotFoundException;
import se.alten.schoolproject.model.StudentModel;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SchoolAccessLocal {
    List listAllStudents() throws Exception;
    StudentModel addStudent(String studentModel) throws BadRequestException;
    void removeStudent(String student) throws NotFoundException;
    void updateStudent(String foreName, String lastName, String email) throws NotFoundException;
    List findStudent(String foreName, String lastName);
}
