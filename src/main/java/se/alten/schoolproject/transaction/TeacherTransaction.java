package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.TeacherEntity;
import se.alten.schoolproject.exceptions.DuplicateEntityException;
import se.alten.schoolproject.exceptions.NotFoundException;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.*;
import java.util.List;

@Stateless
@Default
public class TeacherTransaction implements TeacherTransactionAccess {

    @PersistenceContext(unitName = "school")
    private EntityManager entityManager;

    @Override
    public List<TeacherEntity> listAllTeachers() {
        return entityManager.createQuery("SELECT t FROM TeacherEntity t", TeacherEntity.class).getResultList();
    }

    @Override
    public TeacherEntity findTeacherByEmail(String teacherEmail) throws NotFoundException {
        if (isEmailExist(teacherEmail)) {
            String queryStr = "SELECT t FROM TeacherEntity t WHERE t.email = :teacherEmail";
            TypedQuery<TeacherEntity> query = entityManager.createQuery(queryStr, TeacherEntity.class)
                    .setParameter("teacherEmail", teacherEmail);

            return query.getSingleResult();
        } else {
            throw new NotFoundException("Email not found");
        }
    }

    @Override
    public TeacherEntity addTeacher(TeacherEntity teacherToAdd) throws DuplicateEntityException {
        try {
            entityManager.persist(teacherToAdd);
            entityManager.flush();
            return teacherToAdd;
        } catch (PersistenceException e) {
            throw new DuplicateEntityException("Email already exists");
        }
    }

    @Override
    public void removeTeacher(String email) throws NotFoundException {
        if (isEmailExist(email)) {
            Query query = entityManager.createQuery("DELETE FROM TeacherEntity t" +
                    " WHERE t.email = :email");

            query.setParameter("email", email)
                    .executeUpdate();
        }
        else {
            throw new NotFoundException("Email not found");
        }
    }

    @Override
    public void updateTeacher(String foreName, String lastName, String email) throws NotFoundException {
        if (isEmailExist(email)) {
            if (foreName.isBlank()) {
                Query updateQuery = entityManager.createQuery("UPDATE TeacherEntity t" +
                        " SET t.lastName = :lastName" +
                        " WHERE t.email = :email");

                updateQuery.setParameter("lastName", lastName)
                        .setParameter("email", email)
                        .executeUpdate();
            }
            else if (lastName.isBlank()) {
                Query updateQuery = entityManager.createQuery("UPDATE TeacherEntity t" +
                        " SET t.foreName = :foreName" +
                        " WHERE t.email = :email");

                updateQuery.setParameter("foreName", foreName)
                        .setParameter("email", email)
                        .executeUpdate();
            }
            else {
                Query updateQuery = entityManager.createQuery("UPDATE TeacherEntity t" +
                        " SET t.foreName = :foreName, t.lastName = :lastName" +
                        " WHERE t.email = :email");

                updateQuery.setParameter("foreName", foreName)
                        .setParameter("lastName", lastName)
                        .setParameter("email", email)
                        .executeUpdate();
            }
        }
        else {
            throw new NotFoundException("Email not found");
        }
    }

    @Override
    public List<TeacherEntity> findTeacher(String foreName, String lastName) {
        if (lastName.isBlank()) {
            return entityManager.createQuery("SELECT t FROM TeacherEntity t" +
                    " WHERE t.foreName = :foreName", TeacherEntity.class)
                    .setParameter("foreName", foreName)
                    .getResultList();
        }
        else if (foreName.isBlank()) {
            return entityManager.createQuery("SELECT t FROM TeacherEntity t" +
                    " WHERE t.lastName = :lastName", TeacherEntity.class)
                    .setParameter("lastName", lastName)
                    .getResultList();
        }
        else {
            return entityManager.createQuery("SELECT t FROM TeacherEntity t" +
                    " WHERE t.foreName = :foreName AND t.lastName = :lastName", TeacherEntity.class)
                    .setParameter("foreName", foreName)
                    .setParameter("lastName", lastName)
                    .getResultList();
        }
    }

    private boolean isEmailExist(String email) {
        Long count = (Long) entityManager.createQuery("SELECT COUNT(t) FROM TeacherEntity t" +
                " WHERE t.email = :email")
                .setParameter("email", email)
                .getSingleResult();
        return (!count.equals(0L));
    }
}
