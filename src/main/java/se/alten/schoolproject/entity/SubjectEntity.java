package se.alten.schoolproject.entity;

import lombok.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.*;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubjectEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @ManyToMany(mappedBy = "subject", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<StudentEntity> students = new HashSet<>();

    public SubjectEntity toEntity(String subjectModel) {
        JsonReader reader = Json.createReader(new StringReader(subjectModel));

        JsonObject jsonObject = reader.readObject();

        SubjectEntity subject = new SubjectEntity();

        if ( jsonObject.containsKey("subject")) {

            subject.setTitle(jsonObject.getString("subject"));
        } else {
            subject.setTitle("");
        }

        return subject;
    }
}
