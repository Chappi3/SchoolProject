package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.SubjectEntity;
import se.alten.schoolproject.exceptions.DuplicateEntityException;
import se.alten.schoolproject.exceptions.NotFoundException;

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
    public SubjectEntity addSubject(SubjectEntity subject) throws DuplicateEntityException {
        try {
            entityManager.persist(subject);
            entityManager.flush();
            return subject;
        } catch ( PersistenceException e) {
            throw new DuplicateEntityException("Subject already exists");
        }
    }

    @Override
    public SubjectEntity updateSubject(SubjectEntity subject) {
        entityManager.merge(subject);
        entityManager.flush();
        return subject;
    }

    @Override
    public int removeStudentFromSubject(Long subjectId, Long studentId) {
        Query query = entityManager.createNativeQuery("DELETE FROM subject_student s" +
                " WHERE s.student_id = :studentId" +
                " AND s.subject_id = :subjectId").setParameter("studentId", studentId).setParameter("subjectId", subjectId);
        return query.executeUpdate();
    }

    @Override
    public int removeSubject(String subjectTitle) {
        Query query = entityManager.createQuery("DELETE FROM SubjectEntity s" +
                " WHERE s.title = :subjectTitle");

        return query.setParameter("subjectTitle", subjectTitle)
                .executeUpdate();
    }

    @Override
    public SubjectEntity getSubjectByName(String subjectTitle) throws NotFoundException {

        String queryStr = "SELECT sub FROM SubjectEntity sub WHERE sub.title = :subjectTitle";
        TypedQuery<SubjectEntity> query = entityManager.createQuery(queryStr, SubjectEntity.class)
                .setParameter("subjectTitle", subjectTitle);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException("Subject not found");
        }
    }
}
