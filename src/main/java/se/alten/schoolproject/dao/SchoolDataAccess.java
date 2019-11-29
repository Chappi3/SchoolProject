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

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Inject
    SubjectTransactionAccess subjectTransactionAccess;

    /**
     * Students
     */

    @Override
    public List<StudentModel> listAllStudents(){
        return studentTransactionAccess.listAllStudents()
                .stream()
                .map(StudentEntity::studentEntityToStudentModel)
                .collect(Collectors.toList());
    }

    @Override
    public StudentModel addStudent(String newStudent) throws BadRequestException, DuplicateEntityException {
        StudentEntity studentToAdd = convertFromJson.studentJsonToStudentEntity(newStudent);
        boolean checkForEmptyVariables = Stream.of(studentToAdd.getForeName(),
                studentToAdd.getLastName(),
                studentToAdd.getEmail())
                .anyMatch(String::isBlank);

        if (checkForEmptyVariables) {
            throw new BadRequestException("Empty parameters");
        } else {
//            List<SubjectEntity> subjects = subjectTransactionAccess.getSubjectByName(studentToAdd.getSubjects());
//            subjects.forEach(sub -> studentToAdd.getSubjects().add(sub));
            return studentTransactionAccess.addStudent(studentToAdd).studentEntityToStudentModel();
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
    public List<StudentModel> findStudent(String foreName, String lastName) {
        return studentTransactionAccess.findStudent(foreName, lastName)
                .stream()
                .map(StudentEntity::studentEntityToStudentModel)
                .collect(Collectors.toList());
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
        SubjectEntity subjectToAdd = convertFromJson.subjectJsonToSubjectEntity(newSubject);
        if (subjectToAdd.getTitle().isBlank()) {
            throw new BadRequestException("Empty title");
        }
        else {
            return subjectTransactionAccess.addSubject(subjectToAdd).subjectEntityToModel();
        }
    }
}
