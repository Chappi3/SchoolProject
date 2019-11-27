package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.SubjectEntity;
import se.alten.schoolproject.exceptions.BadRequestException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SubjectTransactionAccess {
    List listAllSubjects();
    SubjectEntity addSubject(SubjectEntity subject) throws BadRequestException;
    List<SubjectEntity> getSubjectByName(List<String> subject);
}
