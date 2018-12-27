package com.sombrainc.excelorm;

import com.sombrainc.excelorm.models.modelmap.custom.*;
import com.sombrainc.excelorm.utils.ModelReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Test
public class CustomMapTest {

    @Test
    public void singleRowMapTest() {
        CustomMapModel1 model = ModelReader.getModel(CustomMapModel1.class);
        Assert.assertNotNull(model);
        Map<Integer, User> expectedMap = new HashMap<Integer, User>() {{
            put(1, new User("Roddy", "Wiliams", 34, Gender.MALE));
        }};
        Assert.assertEquals(model.getMap(), expectedMap);
    }

    @Test
    public void fixedRangeMapTest() {
        CustomMapModel2 model = ModelReader.getModel(CustomMapModel2.class);
        Assert.assertNotNull(model);
        Map<Integer, User> expectedMap = new HashMap<Integer, User>() {{
            put(1, new User("Roddy", "Wiliams", 34, Gender.MALE));
            put(2, new User("Max", "Tiff", 74, Gender.MALE));
            put(3, new User("Lili", "Abrams", 23, Gender.FEMALE));
        }};
        Assert.assertEquals(model.getMap(), expectedMap);
    }

    @Test
    public void rowUntilNullMapTest() {
        CustomMapModel3 model = ModelReader.getModel(CustomMapModel3.class);
        Assert.assertNotNull(model);
        Map<Integer, User> expectedMap = new HashMap<Integer, User>() {{
            put(1, new User("Roddy", "Wiliams", 34, Gender.MALE));
            put(2, new User("Max", "Tiff", 74, Gender.MALE));
            put(3, new User("Lili", "Abrams", 23, Gender.FEMALE));
        }};
        Assert.assertEquals(model.getMap(), expectedMap);
    }

    @Test
    public void rowUntilNullAndStep2MapTest() {
        CustomMapModel4 model = ModelReader.getModel(CustomMapModel4.class);
        Assert.assertNotNull(model);
        Map<Integer, User> expectedMap = new HashMap<Integer, User>() {{
            put(1, new User("Roddy", "Wiliams", 34, Gender.MALE));
            put(3, new User("Lili", "Abrams", 23, Gender.FEMALE));
        }};
        Assert.assertEquals(model.getMap(), expectedMap);
    }

    @Test
    public void fixedRangeAndStep2MapTest() {
        CustomMapModel5 model = ModelReader.getModel(CustomMapModel5.class);
        Assert.assertNotNull(model);
        Map<Integer, User> expectedMap = new HashMap<Integer, User>() {{
            put(1, new User("Roddy", "Wiliams", 34, Gender.MALE));
        }};
        Assert.assertEquals(model.getMap(), expectedMap);
    }

    @Test
    public void fixedRangeAndStep3overColumnsMapTest() {
        CustomMapModel6 model = ModelReader.getModel(CustomMapModel6.class);
        Assert.assertNotNull(model);
        Map<Integer, CustomMapModel6.User> expectedMap = new HashMap<Integer, CustomMapModel6.User>() {{
            put(1, new CustomMapModel6.User("Roddy", "Wiliams", 34, Gender.MALE));
            put(2, new CustomMapModel6.User("Max", "Tiff", 74, Gender.MALE));
            put(3, new CustomMapModel6.User("Lili", "Abrams", 23, Gender.FEMALE));
        }};
        Assert.assertEquals(model.getMap(), expectedMap);
    }

    @Test
    public void columnUntilNullAndStep3MapTest() {
        CustomMapModel7 model = ModelReader.getModel(CustomMapModel7.class);
        Assert.assertNotNull(model);
        Map<Integer, CustomMapModel7.User> expectedMap = new HashMap<Integer, CustomMapModel7.User>() {{
            put(1, new CustomMapModel7.User("Roddy", "Wiliams", 34, Gender.MALE));
            put(2, new CustomMapModel7.User("Max", "Tiff", 74, Gender.MALE));
            put(3, new CustomMapModel7.User("Lili", "Abrams", 23, Gender.FEMALE));
        }};
        Assert.assertEquals(model.getMap(), expectedMap);
    }

    @Test
    public void fixedRangeAndNestedLstInsideObjectMapTest() {
        CustomMapModel8 model = ModelReader.getModel(CustomMapModel8.class);
        Assert.assertNotNull(model);
        Map<Integer, CustomMapModel8.User> expectedMap = new HashMap<Integer, CustomMapModel8.User>() {{
            put(1, new CustomMapModel8.User("Roddy", "Wiliams", 34, Gender.MALE, Arrays.asList(1,2,3)));
            put(2, new CustomMapModel8.User("Max", "Tiff", 74, Gender.MALE, Arrays.asList(4,5,6)));
            put(3, new CustomMapModel8.User("Lili", "Abrams", 23, Gender.FEMALE, Arrays.asList(10,11,12)));
        }};
        Assert.assertEquals(model.getMap(), expectedMap);
    }

}
