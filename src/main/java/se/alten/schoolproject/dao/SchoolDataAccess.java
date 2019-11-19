package se.alten.schoolproject.dao;

import se.alten.schoolproject.entity.StudentEntity;
import se.alten.schoolproject.exceptions.BadRequestException;
import se.alten.schoolproject.exceptions.NotFoundException;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.transaction.StudentTransactionAccess;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {

    private StudentEntity studentEntity = new StudentEntity();
    private StudentModel studentModel = new StudentModel();

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Override
    public List listAllStudents(){
        return studentTransactionAccess.listAllStudents();
    }

    @Override
    public StudentModel addStudent(String newStudent) throws BadRequestException {
        StudentEntity studentToAdd = studentEntity.toEntity(newStudent);
        boolean checkForEmptyVariables = Stream.of(studentToAdd.getForeName(), studentToAdd.getLastName(), studentToAdd.getEmail()).anyMatch(String::isBlank);

        if (checkForEmptyVariables) {
            throw new BadRequestException("Empty parameters");
        } else {
            studentTransactionAccess.addStudent(studentToAdd);
            return studentModel.toModel(studentToAdd);
        }
    }

    @Override
    public void removeStudent(String studentEmail) throws NotFoundException {
        studentTransactionAccess.removeStudent(studentEmail);
    }

    @Override
    public void updateStudent(String foreName, String lastName, String email) throws NotFoundException {
        studentTransactionAccess.updateStudent(foreName, lastName, email);
    }

    @Override
    public void updateStudentPartial(String studentModel) {
        Student studentToUpdate = student.toEntity(studentModel);
        studentTransactionAccess.updateStudentPartial(studentToUpdate);
    }
}
