package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.SubjectEntity;
import se.alten.schoolproject.exceptions.BadRequestException;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.*;
import java.util.List;

@Stateless
@Default
public class SubjectTransaction implements SubjectTransactionAccess{

    @PersistenceContext(unitName="school")
    private EntityManager entityManager;

    @Override
    public List<SubjectEntity> listAllSubjects() {
        return entityManager.createQuery("SELECT s FROM SubjectEntity s", SubjectEntity.class).getResultList();
    }

    @Override
    public SubjectEntity addSubject(SubjectEntity subject) throws BadRequestException {
        try {
            entityManager.persist(subject);
            entityManager.flush();
            return subject;
        } catch ( PersistenceException pe ) {
            throw new BadRequestException("Subject already exists");
        }
    }

    @Override
    public List<SubjectEntity> getSubjectByName(List<String> subject) {

        String queryStr = "SELECT sub FROM SubjectEntity sub WHERE sub.title = :subject";
        TypedQuery<SubjectEntity> query = entityManager.createQuery(queryStr, SubjectEntity.class).setParameter("subject", subject);

        return query.getResultList();
     }
}
