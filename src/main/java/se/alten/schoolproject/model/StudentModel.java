package se.alten.schoolproject.model;

import lombok.*;
import se.alten.schoolproject.entity.StudentEntity;
import se.alten.schoolproject.entity.Subject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<String> subjects = new HashSet<>();

    public StudentModel toModel(StudentEntity student) {
        StudentModel studentModel = new StudentModel();
        studentModel.setForeName(student.getForeName());
        studentModel.setLastName(student.getLastName());
        studentModel.setEmail(student.getEmail());
        student.getSubject().forEach(subject -> studentModel.subjects.add(subject.getTitle()));
        return studentModel;
    }

    public List<StudentModel> toModelList(List<StudentEntity> students) {

        List<StudentModel> studentModels = new ArrayList<>();

        students.forEach(student -> {
            StudentModel studentModel = new StudentModel();
            studentModel.foreName = student.getForeName();
            studentModel.lastName = student.getLastName();
            studentModel.email = student.getEmail();
            student.getSubject().forEach(subject -> studentModel.subjects.add(subject.getTitle()));

            studentModels.add(studentModel);
        });
        return studentModels;
    }
}
