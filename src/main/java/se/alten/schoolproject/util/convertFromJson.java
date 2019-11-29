package se.alten.schoolproject.util;

import se.alten.schoolproject.entity.StudentEntity;
import se.alten.schoolproject.entity.SubjectEntity;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class convertFromJson {

    public static SubjectEntity subjectJsonToSubjectEntity(String jsonData) {
        JsonReader jsonReader = Json.createReader(new StringReader(jsonData));
        JsonObject jsonObject = jsonReader.readObject();
        SubjectEntity subjectEntity = new SubjectEntity();

        if ( jsonObject.containsKey("subject")) {
            subjectEntity.setTitle(jsonObject.getString("subject"));
        } else {
            subjectEntity.setTitle("");
        }
        return subjectEntity;
    }

    public static StudentEntity studentJsonToStudentEntity(String jsonData) {
        JsonReader reader = Json.createReader(new StringReader(jsonData));
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

        return student;
    }
}
