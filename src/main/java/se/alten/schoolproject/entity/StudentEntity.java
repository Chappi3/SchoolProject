package se.alten.schoolproject.entity;

import lombok.Data;
import se.alten.schoolproject.model.StudentModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="student")
@Data
public class StudentEntity implements Serializable {

    private static final long serialVersionUID = 7884822492814832552L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name = "fore_name")
    private String foreName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email", unique = true)
    private String email;
    @ManyToMany(mappedBy = "students", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<SubjectEntity> subjects = new HashSet<>();

    public StudentModel studentEntityToStudentModel() {
        return new StudentModel(getId(),
                getForeName(),
                getLastName(),
                getEmail(),
                getSubjects().stream().map(SubjectEntity::getTitle).collect(Collectors.toSet()));
    }

    @Override
    public String toString() {
        return getForeName() + ", " + getLastName();
    }
}
