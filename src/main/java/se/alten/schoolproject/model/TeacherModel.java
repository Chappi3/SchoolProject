package se.alten.schoolproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherModel implements Serializable {

    private static final long serialVersionUID = 3397673393369641465L;

    private final Long id;
    private final String foreName;
    private final String lastName;
    private final String email;
    private final Set<String> subjects;

}
