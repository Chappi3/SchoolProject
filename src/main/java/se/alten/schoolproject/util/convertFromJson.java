package se.alten.schoolproject.util;

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
}
