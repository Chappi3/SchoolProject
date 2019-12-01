package se.alten.schoolproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectModel {

    private Long id;
    private String title;
    private Set<String> students = new HashSet<>();

}
