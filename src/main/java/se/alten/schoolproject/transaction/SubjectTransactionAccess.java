package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.SubjectEntity;
import se.alten.schoolproject.exceptions.DuplicateEntityException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SubjectTransactionAccess {
    List<SubjectEntity> listAllSubjects();
    SubjectEntity addSubject(SubjectEntity subject) throws DuplicateEntityException;
    List<SubjectEntity> getSubjectByName(List<String> subject);
}
