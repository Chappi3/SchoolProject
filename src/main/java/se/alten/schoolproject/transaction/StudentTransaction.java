package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.StudentEntity;
import se.alten.schoolproject.exceptions.DuplicateEntityException;
import se.alten.schoolproject.exceptions.NotFoundException;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.*;
import java.util.List;

@Stateless
@Default
public class StudentTransaction implements StudentTransactionAccess{

    @PersistenceContext(unitName="school")
    private EntityManager entityManager;

    @Override
    public List<StudentEntity> listAllStudents() {
        return entityManager.createQuery("SELECT s FROM StudentEntity s", StudentEntity.class).getResultList();
    }

    @Override
    public StudentEntity findStudentByEmail(String studentEmail) {
        String queryStr = "SELECT s FROM StudentEntity s WHERE s.email = :studentEmail";
        TypedQuery<StudentEntity> query = entityManager.createQuery(queryStr, StudentEntity.class)
                .setParameter("studentEmail", studentEmail);

        return query.getSingleResult();
    }

    @Override
    public StudentEntity addStudent(StudentEntity student) throws DuplicateEntityException {
        try {
            entityManager.persist(student);
            entityManager.flush();
            return student;
        } catch (PersistenceException e) {
            throw new DuplicateEntityException("Email already exists");
        }
    }

    @Override
    public void removeStudent(String email) throws NotFoundException {
        if (isEmailExist(email)) {
            Query query = entityManager.createQuery("DELETE FROM StudentEntity s" +
                    " WHERE s.email = :email");

            query.setParameter("email", email)
                    .executeUpdate();
        }
        else {
            throw new NotFoundException("Email not found");
        }
    }

    @Override
    public void updateStudent(String foreName, String lastName, String email) throws NotFoundException {
        if (isEmailExist(email)) {
            if (foreName.isBlank()) {
                Query updateQuery = entityManager.createQuery("UPDATE StudentEntity s" +
                        " SET s.lastName = :lastName" +
                        " WHERE s.email = :email");

                updateQuery.setParameter("lastName", lastName)
                        .setParameter("email", email)
                        .executeUpdate();
            }
            else if (lastName.isBlank()) {
                Query updateQuery = entityManager.createQuery("UPDATE StudentEntity s" +
                        " SET s.foreName = :foreName" +
                        " WHERE s.email = :email");

                updateQuery.setParameter("foreName", foreName)
                        .setParameter("email", email)
                        .executeUpdate();
            }
            else {
                Query updateQuery = entityManager.createQuery("UPDATE StudentEntity s" +
                        " SET s.foreName = :foreName, s.lastName = :lastName" +
                        " WHERE s.email = :email");

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
    public List<StudentEntity> findStudent(String foreName, String lastName) {
        if (lastName.isBlank()) {
            return entityManager.createQuery("SELECT s FROM StudentEntity s" +
                    " WHERE s.foreName = :foreName", StudentEntity.class)
                    .setParameter("foreName", foreName)
                    .getResultList();
        }
        else if (foreName.isBlank()) {
            return entityManager.createQuery("SELECT s FROM StudentEntity s" +
                    " WHERE s.lastName = :lastName", StudentEntity.class)
                    .setParameter("lastName", lastName)
                    .getResultList();
        }
        else {
            return entityManager.createQuery("SELECT s FROM StudentEntity s" +
                    " WHERE s.foreName = :foreName AND s.lastName = :lastName", StudentEntity.class)
                    .setParameter("foreName", foreName)
                    .setParameter("lastName", lastName)
                    .getResultList();
        }
    }

    private boolean isEmailExist(String email) {
        Long count = (Long) entityManager.createQuery("SELECT COUNT(s) FROM StudentEntity s" +
                " WHERE s.email = :email")
                .setParameter("email", email)
                .getSingleResult();
        return (!count.equals(0L));
    }
}
