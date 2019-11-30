package se.alten.schoolproject.entity;

import lombok.*;
import se.alten.schoolproject.model.SubjectModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "subject")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubjectEntity implements Serializable {

    private static final long serialVersionUID = 2066770006985636516L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "student_subject",
            joinColumns=@JoinColumn(name="student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
    private Set<StudentEntity> students = new HashSet<>();

    public SubjectModel subjectEntityToModel() {
        SubjectModel subjectModel = new SubjectModel();
        subjectModel.setId(getId());
        subjectModel.setTitle(getTitle());
        subjectModel.setStudents(getStudents()
                .stream()
                .map(StudentEntity::studentEntityToStudentModel)
                .collect(Collectors.toSet()));

        return subjectModel;
    }
}
