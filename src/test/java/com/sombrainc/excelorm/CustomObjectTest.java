package com.sombrainc.excelorm;

import com.sombrainc.excelorm.models.customobject.CustomObjectModel;
import com.sombrainc.excelorm.models.customobject.CustomObjectModel2;
import com.sombrainc.excelorm.models.modelmap.custom.Gender;
import com.sombrainc.excelorm.utils.ModelReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

//@EReader
public class CustomObjectTest {

    public void customObjectTest() {
        CustomObjectModel model = ModelReader.getModel(CustomObjectModel.class);
        Assert.assertNotNull(model);
        CustomObjectModel.User expectedUser = new CustomObjectModel.User(
                "Roddy", "Wiliams", 34, Gender.MALE,
                new HashMap<String, Integer>(){{
                    put("key1", 1);
                    put("key2", 2);
                    put("key3", 3);
                    put("key4", 4);
                }});
        Assert.assertEquals(model.getUser(), expectedUser);
    }

    public void customObjectTestSuper() {
        CustomObjectModel2 model = ModelReader.getModel(CustomObjectModel2.class);
        Assert.assertNotNull(model);

        Map<String, CustomObjectModel2.Subject> subjects = new HashMap<String, CustomObjectModel2.Subject>() {{
            put("Science", new CustomObjectModel2.Subject(
                    34, new CustomObjectModel2.Student(
                            new HashMap<String, Integer>() {{
                                put("Student1", 12);
                                put("Student2", 5);
                                put("Student3", 10);
                            }}
            ), 9.0));
            put("ICT", new CustomObjectModel2.Subject(
                    23, new CustomObjectModel2.Student(
                            new HashMap<String, Integer>() {{
                                put("Student1", 9);
                                put("Student2", 9);
                                put("Student3", 9);
                            }}
            ), 9.0));
            put("History", new CustomObjectModel2.Subject(
                    35, new CustomObjectModel2.Student(
                            new HashMap<String, Integer>() {{
                                put("Student1", 10);
                                put("Student2", 11);
                                put("Student3", 12);
                            }}
            ), 11.0));
            put("Geography", new CustomObjectModel2.Subject(
                    43, new CustomObjectModel2.Student(
                            new HashMap<String, Integer>() {{
                                put("Student1", 6);
                                put("Student2", 5);
                                put("Student3", 4);
                            }}
            ), 5.0));
        }};

        CustomObjectModel2.Lesson lesson = new CustomObjectModel2.Lesson(
                subjects
        );

        Assert.assertEquals(model.getLesson(), lesson);
    }

}
