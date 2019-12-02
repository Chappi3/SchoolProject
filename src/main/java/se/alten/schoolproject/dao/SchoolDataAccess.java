package se.alten.schoolproject.dao;

import se.alten.schoolproject.entity.StudentEntity;
import se.alten.schoolproject.entity.TeacherEntity;
import se.alten.schoolproject.exceptions.BadRequestException;
import se.alten.schoolproject.exceptions.DuplicateEntityException;
import se.alten.schoolproject.exceptions.NotFoundException;
import se.alten.schoolproject.entity.SubjectEntity;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;
import se.alten.schoolproject.model.TeacherModel;
import se.alten.schoolproject.transaction.StudentTransactionAccess;
import se.alten.schoolproject.transaction.SubjectTransactionAccess;
import se.alten.schoolproject.transaction.TeacherTransactionAccess;
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
    TeacherTransactionAccess teacherTransactionAccess;

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
     * Teachers
     */

    @Override
    public List<TeacherModel> listAllTeachers() {
        return teacherTransactionAccess.listAllTeachers()
                .stream()
                .map(TeacherEntity::teacherEntityToTeacherModel)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherModel addTeacher(String teacherModelJson) throws BadRequestException, DuplicateEntityException {
        TeacherEntity teacherEntity = convertFromJson.teacherJsonToTeacherEntity(teacherModelJson);
        boolean checkForEmptyVariables = Stream.of(teacherEntity.getForeName(),
                teacherEntity.getLastName(),
                teacherEntity.getEmail())
                .anyMatch(String::isBlank);

        if (checkForEmptyVariables) {
            throw new BadRequestException("Empty parameters");
        } else {
            return teacherTransactionAccess.addTeacher(teacherEntity).teacherEntityToTeacherModel();
        }
    }

    @Override
    public void removeTeacher(String teacherEmailJson) throws NotFoundException, BadRequestException {
        String teacherEntityEmail = convertFromJson.teacherJsonToTeacherEntity(teacherEmailJson).getEmail();
        if (teacherEntityEmail.isBlank()) {
            throw new BadRequestException("No email received");
        }
        teacherTransactionAccess.removeTeacher(teacherEntityEmail);
    }

    @Override
    public void updateTeacher(String teacherUpdateJson) throws NotFoundException, BadRequestException {
        TeacherEntity updatedTeacherEntity = convertFromJson.teacherJsonToTeacherEntity(teacherUpdateJson);
        if (updatedTeacherEntity.getEmail().isBlank()) {
            throw new BadRequestException("No email received");
        }
        else if (updatedTeacherEntity.getForeName().isBlank() && updatedTeacherEntity.getLastName().isBlank()) {
            throw new BadRequestException("No data");
        }
        teacherTransactionAccess.updateTeacher(updatedTeacherEntity.getForeName(),
                updatedTeacherEntity.getLastName(),
                updatedTeacherEntity.getEmail());
    }

    @Override
    public List<TeacherModel> findTeacher(String findTeacherJson) throws BadRequestException {
        TeacherEntity findTeacherEntity = convertFromJson.teacherJsonToTeacherEntity(findTeacherJson);
        if (findTeacherEntity.getForeName().isBlank() && findTeacherEntity.getLastName().isBlank()) {
            throw new BadRequestException("No data");
        }
        return teacherTransactionAccess.findTeacher(findTeacherEntity.getForeName(), findTeacherEntity.getLastName())
                .stream()
                .map(TeacherEntity::teacherEntityToTeacherModel)
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

        validateAddToSubjectData(studentEmail, subjectTitle);

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
    public SubjectModel addTeacherToSubject(String jsonData) throws BadRequestException, DuplicateEntityException, NotFoundException {
        String subjectTitle = convertFromJson.subjectJsonToSubjectEntity(jsonData).getTitle();
        String teacherEmail = convertFromJson.teacherJsonToTeacherEntity(jsonData).getEmail();

        validateAddToSubjectData(teacherEmail, subjectTitle);

        SubjectEntity subjectEntity = subjectTransactionAccess.getSubjectByName(subjectTitle);
        TeacherEntity teacherEntity = teacherTransactionAccess.findTeacherByEmail(teacherEmail);

        if (isTeacherInSubject(subjectEntity, teacherEntity)) {
            throw new DuplicateEntityException("Teacher already in subject");
        }

        teacherEntity.getSubjects().add(subjectEntity);
        subjectEntity.getTeachers().add(teacherEntity);

        return subjectTransactionAccess.updateSubject(subjectEntity).subjectEntityToModel();
    }

    @Override
    public void removeStudentFromSubject(String jsonData) throws BadRequestException, NotFoundException {
        String subjectTitle = convertFromJson.subjectJsonToSubjectEntity(jsonData).getTitle();
        String studentEmail = convertFromJson.studentJsonToStudentEntity(jsonData).getEmail();

        validateAddToSubjectData(studentEmail, subjectTitle);

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
    public void removeTeacherFromSubject(String jsonData) throws BadRequestException, NotFoundException {
        String subjectTitle = convertFromJson.subjectJsonToSubjectEntity(jsonData).getTitle();
        String teacherEmail = convertFromJson.teacherJsonToTeacherEntity(jsonData).getEmail();

        validateAddToSubjectData(teacherEmail, subjectTitle);

        SubjectEntity subjectEntity = subjectTransactionAccess.getSubjectByName(subjectTitle);
        TeacherEntity teacherEntity = teacherTransactionAccess.findTeacherByEmail(teacherEmail);

        if (!isTeacherInSubject(subjectEntity, teacherEntity)) {
            throw new NotFoundException("Teacher not found in subject");
        }

        int i = subjectTransactionAccess.removeTeacherFromSubject(subjectEntity.getId(), teacherEntity.getId());
        if (i == 0) {
            throw new NotFoundException("Could not remove teacher from subject");
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

    private void validateAddToSubjectData(String email, String subjectTitle) throws BadRequestException {

        if (email.isBlank() && subjectTitle.isBlank()) {
            throw new BadRequestException("No Data");
        }
        else if (email.isBlank()) {
            throw new BadRequestException("No email received");
        }
        else if (subjectTitle.isBlank()) {
            throw new BadRequestException("No subject received");
        }
    }

    private boolean isStudentInSubject(SubjectEntity subjectEntity, StudentEntity studentEntity) {
        return subjectEntity.getStudents()
                .stream()
                .anyMatch(studentInSubject -> studentInSubject.getId().longValue() == studentEntity.getId().longValue());
    }

    private boolean isTeacherInSubject(SubjectEntity subjectEntity, TeacherEntity teacherEntity) {
        return subjectEntity.getTeachers()
                .stream()
                .anyMatch(teacherInSubject -> teacherInSubject.getId().longValue() == teacherEntity.getId().longValue());
    }
}
