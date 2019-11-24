package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.SubjectEntity;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SubjectTransactionAccess {
    List listAllSubjects();
    SubjectEntity addSubject(SubjectEntity subject);
    List<SubjectEntity> getSubjectByName(List<String> subject);
}
