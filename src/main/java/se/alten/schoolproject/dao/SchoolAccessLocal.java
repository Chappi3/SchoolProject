package se.alten.schoolproject.dao;

import se.alten.schoolproject.exceptions.BadRequestException;
import se.alten.schoolproject.exceptions.DuplicateEntityException;
import se.alten.schoolproject.exceptions.NotFoundException;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SchoolAccessLocal {
    List<StudentModel> listAllStudents();
    StudentModel addStudent(String studentModel) throws BadRequestException, DuplicateEntityException;
    void removeStudent(String student) throws NotFoundException;
    void updateStudent(String foreName, String lastName, String email) throws NotFoundException;
    List<StudentModel> findStudent(String foreName, String lastName);

    List<SubjectModel> listAllSubjects();
    SubjectModel addSubject(String subjectModel) throws DuplicateEntityException, BadRequestException;
}
