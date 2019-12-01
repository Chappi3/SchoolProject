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
import java.util.*;
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
            return studentTransactionAccess.addStudent(studentToAdd).studentEntityToStudentModel();
        }
    }

    @Override
    public void removeStudent(String studentEmailJson) throws NotFoundException, BadRequestException {
        StudentEntity studentEntityEmail = convertFromJson.studentJsonToStudentEntity(studentEmailJson);
        if (studentEntityEmail.getEmail().isBlank()) {
            throw new BadRequestException("No email received");
        }
        studentTransactionAccess.removeStudent(studentEntityEmail.getEmail());
    }

    @Override
    public void updateStudent(String studentUpdateJson) throws NotFoundException, BadRequestException {
        StudentEntity updatedStudentEntity = convertFromJson.studentJsonToStudentEntity(studentUpdateJson);
        if (updatedStudentEntity.getEmail().isBlank()) {
            throw new BadRequestException("No email received");
        }
        else if (updatedStudentEntity.getForeName().isBlank() && updatedStudentEntity.getLastName().isBlank()) {
            throw new BadRequestException("No data");
        }
        studentTransactionAccess.updateStudent(updatedStudentEntity.getForeName(),
                updatedStudentEntity.getLastName(),
                updatedStudentEntity.getEmail());
    }

    @Override
    public List<StudentModel> findStudent(String findStudentJson) throws BadRequestException {
        StudentEntity findStudentEntity = convertFromJson.studentJsonToStudentEntity(findStudentJson);
        if (findStudentEntity.getForeName().isBlank() && findStudentEntity.getLastName().isBlank()) {
            throw new BadRequestException("No data");
        }
        return studentTransactionAccess.findStudent(findStudentEntity.getForeName(), findStudentEntity.getLastName())
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
    public SubjectModel addSubject(String subjectModelJson) throws DuplicateEntityException, BadRequestException {
        SubjectEntity newSubject = convertFromJson.subjectJsonToSubjectEntity(subjectModelJson);
        if (newSubject.getTitle().isBlank()) {
            throw new BadRequestException("Empty title");
        }
        else {
            return subjectTransactionAccess.addSubject(newSubject).subjectEntityToModel();
        }
    }

    @Override
    public SubjectModel addStudentToSubject(String jsonData) throws BadRequestException, DuplicateEntityException, NotFoundException {
        String subjectTitle = convertFromJson.subjectJsonToSubjectEntity(jsonData).getTitle();
        String studentEmail = convertFromJson.studentJsonToStudentEntity(jsonData).getEmail();

        validateStudentToSubjectData(studentEmail, subjectTitle);

        SubjectEntity subjectEntity = subjectTransactionAccess.getSubjectByName(subjectTitle);
        StudentEntity studentEntity = studentTransactionAccess.findStudentByEmail(studentEmail);

        if (isStudentInSubject(subjectEntity, studentEntity)) {
            throw new DuplicateEntityException("Student already in subject");
        }

        studentEntity.getSubjects().add(subjectEntity);
        subjectEntity.getStudents().add(studentEntity);

        return subjectTransactionAccess.updateSubject(subjectEntity).subjectEntityToModel();
    }

    @Override
    public void removeStudentFromSubject(String jsonData) throws BadRequestException, NotFoundException {
        String subjectTitle = convertFromJson.subjectJsonToSubjectEntity(jsonData).getTitle();
        String studentEmail = convertFromJson.studentJsonToStudentEntity(jsonData).getEmail();

        validateStudentToSubjectData(studentEmail, subjectTitle);

        SubjectEntity subjectEntity = subjectTransactionAccess.getSubjectByName(subjectTitle);
        StudentEntity studentEntity = studentTransactionAccess.findStudentByEmail(studentEmail);

        if (!isStudentInSubject(subjectEntity, studentEntity)) {
            throw new NotFoundException("Student not found in subject");
        }

        int i = subjectTransactionAccess.removeStudentFromSubject(subjectEntity.getId(), studentEntity.getId());
        if (i == 0) {
            throw new NotFoundException("Could not remove student from subject");
        }
    }

    @Override
    public void removeSubject(String jsonData) throws BadRequestException, NotFoundException {
        String subjectTitle = convertFromJson.subjectJsonToSubjectEntity(jsonData).getTitle();
        if (subjectTitle.isBlank()) {
            throw new BadRequestException("No subject received");
        }
        int i = subjectTransactionAccess.removeSubject(subjectTitle);
        if (i == 0) {
            throw new NotFoundException("Subject not found");
        }
    }

    /**
     * Helper Methods
     */

    private void validateStudentToSubjectData(String studentEmail, String subjectTitle) throws BadRequestException {

        if (studentEmail.isBlank() && subjectTitle.isBlank()) {
            throw new BadRequestException("No Data");
        }
        else if (studentEmail.isBlank()) {
            throw new BadRequestException("No email received");
        }
        else if (subjectTitle.isBlank()) {
            throw new BadRequestException("No subject received");
        }
    }

    private boolean isStudentInSubject(SubjectEntity subjectEntity, StudentEntity studentEntity) {
        return subjectEntity.getStudents()
                .stream()
                .anyMatch(studentInSubject -> studentInSubject.getEmail().equals(studentEntity.getEmail()));
    }
}
