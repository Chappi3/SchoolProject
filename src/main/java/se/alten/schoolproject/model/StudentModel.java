package se.alten.schoolproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import se.alten.schoolproject.entity.StudentEntity;

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
public class StudentModel implements Serializable {

    private static final long serialVersionUID = -3593725019308065508L;

    private Long id;
    private String foreName;
    private String lastName;
    private String email;
    private Set<String> subjects = new HashSet<>();

    public StudentEntity studentModelToStudentEntity() {
        StudentEntity studentEntity = new StudentEntity();

        studentEntity.setId(getId());
        studentEntity.setForeName(getForeName());
        studentEntity.setLastName(getLastName());
        studentEntity.setEmail(getEmail());
//        TODO: FIX
//        studentEntity.setSubjects(getSubjects().stream().map().collect(Collectors.toList()));

        return studentEntity;
    }
}
