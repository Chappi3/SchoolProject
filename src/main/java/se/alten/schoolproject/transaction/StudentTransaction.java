package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.StudentEntity;
import se.alten.schoolproject.exceptions.BadRequestException;
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
    public List listAllStudents() {
        Query query = entityManager.createQuery("SELECT s FROM StudentEntity s");
        return query.getResultList();
    }

    @Override
    public void addStudent(StudentEntity studentToAdd) throws BadRequestException {
        try {
            entityManager.persist(studentToAdd);
            entityManager.flush();
        } catch (PersistenceException e) {
            throw new BadRequestException("Email already exists");
        }
    }

    @Override
    public void removeStudent(String email) throws NotFoundException {
        if (isEmailExist(email)) {
            Query query = entityManager.createQuery("DELETE FROM StudentEntity s WHERE s.email = :email");
            query.setParameter("email", email).executeUpdate();
        }
        else {
            throw new NotFoundException("Email not found");
        }
    }

    @Override
    public void updateStudent(String foreName, String lastName, String email) throws NotFoundException {
        if (isEmailExist(email)) {
            if (foreName.isBlank()) {
                Query updateQuery = entityManager.createQuery("UPDATE StudentEntity s SET s.lastName = :lastName WHERE s.email = :email");

                updateQuery.setParameter("lastName", lastName)
                        .setParameter("email", email)
                        .executeUpdate();
            }
            else if (lastName.isBlank()) {
                Query updateQuery = entityManager.createQuery("UPDATE StudentEntity s SET s.foreName = :foreName WHERE s.email = :email");

                updateQuery.setParameter("foreName", foreName)
                        .setParameter("email", email)
                        .executeUpdate();
            }
            else {
                Query updateQuery = entityManager.createQuery("UPDATE StudentEntity s SET s.foreName = :foreName, s.lastName = :lastName WHERE s.email = :email");

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
    public List findStudent(String foreName, String lastName) {
        if (lastName.isBlank()) {
            Query query = entityManager.createQuery("SELECT s FROM StudentEntity s WHERE s.foreName = :foreName")
                    .setParameter("foreName", foreName);
            return query.getResultList();
        }
        else if (foreName.isBlank()) {
            Query query = entityManager.createQuery("SELECT s FROM StudentEntity s WHERE s.lastName = :lastName")
                    .setParameter("lastName", lastName);
            return query.getResultList();
        }
        else {
            Query query = entityManager.createQuery("SELECT s FROM StudentEntity s WHERE s.foreName = :foreName AND s.lastName = :lastName")
                    .setParameter("foreName", foreName)
                    .setParameter("lastName", lastName);
            return query.getResultList();
        }
    }

    private boolean isEmailExist(String email) {
        Long count = (Long) entityManager.createQuery("SELECT COUNT(s) FROM StudentEntity s WHERE s.email = :email")
                .setParameter("email", email)
                .getSingleResult();
        return (!count.equals(0L));
    }
}
