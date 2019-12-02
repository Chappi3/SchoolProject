package se.alten.schoolproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentModel implements Serializable {

    private static final long serialVersionUID = -976794302066669941L;

    private final Long id;
    private final String foreName;
    private final String lastName;
    private final String email;
    private final Set<String> subjects;

}
