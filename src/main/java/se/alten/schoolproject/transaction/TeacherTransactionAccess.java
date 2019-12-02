package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.TeacherEntity;
import se.alten.schoolproject.exceptions.DuplicateEntityException;
import se.alten.schoolproject.exceptions.NotFoundException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface TeacherTransactionAccess {
    List<TeacherEntity> listAllTeachers();
    TeacherEntity findTeacherByEmail(String teacherEmail) throws NotFoundException;
    TeacherEntity addTeacher(TeacherEntity teacherToAdd) throws DuplicateEntityException;
    void removeTeacher(String teacher) throws NotFoundException;
    void updateTeacher(String foreName, String lastName, String email) throws NotFoundException;
    List<TeacherEntity> findTeacher(String foreName, String lastName);
}
