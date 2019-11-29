package se.alten.schoolproject.entity;

import lombok.*;
import se.alten.schoolproject.model.StudentModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="student")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StudentEntity implements Serializable {

    private static final long serialVersionUID = 7884822492814832552L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "foreName")
    private String foreName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @ManyToMany(mappedBy = "students", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<SubjectEntity> subjects = new HashSet<>();

    /*@Transient
    private List<String> subjects = new ArrayList<>();*/

    public StudentModel studentEntityToStudentModel() {
        StudentModel studentModel = new StudentModel();
        studentModel.setId(getId());
        studentModel.setForeName(getForeName());
        studentModel.setLastName(getLastName());
        studentModel.setEmail(getEmail());
        studentModel.setSubjects(getSubjects().stream().map(SubjectEntity::getTitle).collect(Collectors.toSet()));

        return studentModel;
    }
}
