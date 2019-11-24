package se.alten.schoolproject.dao;

import se.alten.schoolproject.entity.StudentEntity;
import se.alten.schoolproject.exceptions.BadRequestException;
import se.alten.schoolproject.exceptions.NotFoundException;
import se.alten.schoolproject.entity.SubjectEntity;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;
import se.alten.schoolproject.transaction.StudentTransactionAccess;
import se.alten.schoolproject.transaction.SubjectTransactionAccess;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {

    private StudentEntity studentEntity = new StudentEntity();
    private StudentModel studentModel = new StudentModel();
    private SubjectEntity subjectEntity = new SubjectEntity();
    private SubjectModel subjectModel = new SubjectModel();

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Inject
    SubjectTransactionAccess subjectTransactionAccess;

    @Override
    public List listAllStudents(){
//        List<StudentModel> sm = studentModel.toModelList(studentTransactionAccess.listAllStudents());
        List sm = studentTransactionAccess.listAllStudents();
        return sm;
    }

    @Override
    public StudentModel addStudent(String newStudent) throws BadRequestException {
        StudentEntity studentToAdd = studentEntity.toEntity(newStudent, listAllSubjects());
        boolean checkForEmptyVariables = Stream.of(studentToAdd.getForeName(), studentToAdd.getLastName(), studentToAdd.getEmail()).anyMatch(String::isBlank);
        if (checkForEmptyVariables) {
            throw new BadRequestException("Empty parameters");
        } else {
           studentTransactionAccess.addStudent(studentToAdd);
            List<SubjectEntity> subjects = subjectTransactionAccess.getSubjectByName(studentToAdd.getSubjects());
            subjects.forEach(sub -> studentToAdd.getSubject().add(sub));
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
    public List findStudent(String foreName, String lastName) {
        return studentTransactionAccess.findStudent(foreName, lastName);
    }

    @Override
    public List listAllSubjects() {
        return subjectTransactionAccess.listAllSubjects();
    }

    @Override
    public SubjectModel addSubject(String newSubject) {
        SubjectEntity subjectToAdd = subjectEntity.toEntity(newSubject);
        subjectTransactionAccess.addSubject(subjectToAdd);
        return subjectModel.toModel(subjectToAdd);
    }
}
