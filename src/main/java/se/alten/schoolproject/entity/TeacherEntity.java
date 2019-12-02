package se.alten.schoolproject.entity;

import lombok.*;
import se.alten.schoolproject.model.TeacherModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "teacher")
@Data
public class TeacherEntity implements Serializable {

    private static final long serialVersionUID = 3242990909024120453L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "fore_name")
    private String foreName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email", unique = true)
    private String email;
    @ManyToMany(mappedBy = "teachers", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<SubjectEntity> subjects = new HashSet<>();

    public TeacherModel teacherEntityToTeacherModel() {
        return new TeacherModel(getId(),
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
