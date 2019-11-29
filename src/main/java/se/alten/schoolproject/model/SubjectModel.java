package se.alten.schoolproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import se.alten.schoolproject.entity.StudentEntity;
import se.alten.schoolproject.entity.SubjectEntity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectModel implements Serializable {

    private static final long serialVersionUID = 2370348983290737366L;

    private Long id;
    private String title;
    private Set<StudentModel> students = new HashSet<>();

    public SubjectEntity subjectModelToSubjectEntity() {
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setId(getId());
        subjectEntity.setTitle(getTitle());
//        TODO: FIX
//        subjectEntity.setStudents(getStudents().stream().map(StudentEntity::toEntity).collect(Collectors.toList()));
        return subjectEntity;
    }
}
