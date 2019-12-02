package se.alten.schoolproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectModel implements Serializable {

    private static final long serialVersionUID = 8201375259501590466L;

    private final Long id;
    private final String title;
    private final Set<String> students;
    private final Set<String> teachers;

}
