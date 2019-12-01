package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.SubjectEntity;
import se.alten.schoolproject.exceptions.DuplicateEntityException;
import se.alten.schoolproject.exceptions.NotFoundException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SubjectTransactionAccess {
    List<SubjectEntity> listAllSubjects();
    SubjectEntity addSubject(SubjectEntity subject) throws DuplicateEntityException;
    SubjectEntity updateSubject(SubjectEntity subject);
    int removeStudentFromSubject(Long subjectId, Long studentId);
    SubjectEntity getSubjectByName(String subjectTitle) throws NotFoundException;
    int removeSubject(String subjectTitle);
}
