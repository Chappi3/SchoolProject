package se.alten.schoolproject.model;

import lombok.*;
import se.alten.schoolproject.entity.StudentEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentModel {

    private Long id;
    private String foreName;
    private String lastName;
    private String email;

    public StudentModel toModel(StudentEntity student) {
        StudentModel studentModel = new StudentModel();
        switch (student.getForeName()) {
            case "empty":
                studentModel.setForeName("empty");
                return studentModel;
            case "duplicate":
                studentModel.setForeName("duplicate");
                return studentModel;
            default:
                studentModel.setForeName(student.getForeName());
                studentModel.setLastName(student.getLastName());
                studentModel.setEmail(student.getEmail());
                return studentModel;
        }
    }
}
