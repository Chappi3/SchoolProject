package se.alten.schoolproject.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
@RequiredArgsConstructor
public class SubjectEntity implements Serializable {

    private static final long serialVersionUID = 2066770006985636516L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "subject_student",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
    private Set<StudentEntity> students = new HashSet<>();
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "subject_teacher",
    joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
    private Set<TeacherEntity> teachers = new HashSet<>();

    public SubjectModel subjectEntityToModel() {
        return new SubjectModel(getId(),
                getTitle(),
                getStudents().stream().map(StudentEntity::toString).collect(Collectors.toSet()),
                getTeachers().stream().map(TeacherEntity::toString).collect(Collectors.toSet()));
    }
}
