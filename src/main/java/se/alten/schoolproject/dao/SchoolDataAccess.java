package se.alten.schoolproject.dao;

import se.alten.schoolproject.entity.StudentEntity;
import se.alten.schoolproject.exceptions.BadRequestException;
import se.alten.schoolproject.exceptions.DuplicateEntityException;
import se.alten.schoolproject.exceptions.NotFoundException;
import se.alten.schoolproject.entity.SubjectEntity;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;
import se.alten.schoolproject.transaction.StudentTransactionAccess;
import se.alten.schoolproject.transaction.SubjectTransactionAccess;
import se.alten.schoolproject.util.convertFromJson;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {

    private StudentEntity studentEntity = new StudentEntity();
    private StudentModel studentModel = new StudentModel();

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Inject
    SubjectTransactionAccess subjectTransactionAccess;

    /**
     * Students
     */

    @Override
    public List listAllStudents(){
//        List<StudentModel> sm = studentModel.toModelList(studentTransactionAccess.listAllStudents());
        List sm = studentTransactionAccess.listAllStudents();
        return sm;
    }

    @Override
    public StudentModel addStudent(String newStudent) throws BadRequestException {
        StudentEntity studentToAdd = studentEntity.toEntity(newStudent);
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

    /**
     * Subjects
     */

    @Override
    public List<SubjectModel> listAllSubjects() {
        List<SubjectEntity> subjectEntities = subjectTransactionAccess.listAllSubjects();
        return subjectEntities.stream().map(SubjectEntity::subjectEntityToModel).collect(Collectors.toList());
    }

    @Override
    public SubjectModel addSubject(String newSubject) throws DuplicateEntityException, BadRequestException {
        if (newSubject.isBlank()) {
            throw new BadRequestException("Empty title");
        }
        else {
            SubjectEntity subjectToAdd = convertFromJson.subjectJsonToSubjectEntity(newSubject);
            return subjectTransactionAccess.addSubject(subjectToAdd).subjectEntityToModel();
        }
    }
}
