package se.alten.schoolproject.entity;

import lombok.*;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.*;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="student")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StudentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "foreName")
    private String foreName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "student_subject",
            joinColumns=@JoinColumn(name="stud_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "subj_id", referencedColumnName = "id"))
    private Set<Subject> subject = new HashSet<>();

    @Transient
    private List<String> subjects = new ArrayList<>();

    public StudentEntity toEntity(String studentModel) {
        List<String> temp = new ArrayList<>();
        JsonReader reader = Json.createReader(new StringReader(studentModel));
        JsonObject jsonObject = reader.readObject();

        StudentEntity student = new StudentEntity();
        if ( jsonObject.containsKey("foreName")) {
            student.setForeName(jsonObject.getString("foreName"));
        } else {
            student.setForeName("");
        }

        if ( jsonObject.containsKey("lastName")) {
            student.setLastName(jsonObject.getString("lastName"));
        } else {
            student.setLastName("");
        }

        if ( jsonObject.containsKey("email")) {
            student.setEmail(jsonObject.getString("email"));
        } else {
            student.setEmail("");
        }

        if (jsonObject.containsKey("subject")) {
            JsonArray jsonArray = jsonObject.getJsonArray("subject");
            for ( int i = 0; i < jsonArray.size(); i++ ){
                temp.add(jsonArray.get(i).toString().replace("\"", ""));
                student.setSubjects(temp);
            }
        } else {
            student.setSubjects(null);
        }

        return student;
    }
}
