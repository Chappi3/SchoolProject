package se.alten.schoolproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherModel implements Serializable {

    private static final long serialVersionUID = 3397673393369641465L;

    private Long id;
    private String foreName;
    private String lastName;
    private String email;
    private Set<String> subjects;

}
