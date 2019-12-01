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
    StudentModel addStudent(String studentModelJson) throws BadRequestException, DuplicateEntityException;
    void removeStudent(String studentEmailJson) throws NotFoundException, BadRequestException;
    void updateStudent(String studentUpdateJson) throws NotFoundException, BadRequestException;
    List<StudentModel> findStudent(String findStudentJson) throws BadRequestException;

    List<SubjectModel> listAllSubjects();
    SubjectModel addSubject(String subjectModelJson) throws DuplicateEntityException, BadRequestException;
    SubjectModel addStudentToSubject(String jsonData) throws DuplicateEntityException, NotFoundException, BadRequestException;
    void removeStudentFromSubject(String jsonData) throws NotFoundException, BadRequestException;
    void removeSubject(String jsonData) throws NotFoundException, BadRequestException;
}
