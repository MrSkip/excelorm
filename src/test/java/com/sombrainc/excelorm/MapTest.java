package com.sombrainc.excelorm;

import com.sombrainc.excelorm.models.modelmap.*;
import com.sombrainc.excelorm.utils.ModelReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Test
public class MapTest {

    public void oneItemMapTest() {
        FixedMapModel model = ModelReader.getModel(FixedMapModel.class);
        Assert.assertNotNull(model);
        Map<Integer, String> map = model.getMap();
        Assert.assertNotNull(map);
        Map<Integer, String> expectedMap = new HashMap<Integer, String>() {{
            put(1, "Roddy");
        }};
        Assert.assertEquals(map, expectedMap);
    }

    public void fixedMapTest() {
        FixedMapWithRangeModel model = ModelReader.getModel(FixedMapWithRangeModel.class);
        Assert.assertNotNull(model);
        Map<Integer, String> expectedMap = new HashMap<Integer, String>() {{
            put(1, "Roddy");
            put(2, "Max");
            put(3, "Lili");
        }};
        Assert.assertEquals(model.getMap(), expectedMap);
    }

    public void rowUntilNullMapTest() {
        RowUntilNullMapModel model = ModelReader.getModel(RowUntilNullMapModel.class);
        Assert.assertNotNull(model);
        Map<Integer, String> map = model.getMap();
        Map<Integer, String> expectedMap = new HashMap<Integer, String>() {{
            put(1, "Roddy");
            put(2, "Max");
            put(3, "Lili");
        }};
        Assert.assertEquals(map, expectedMap);
    }

    public void rowUntilNullWithStepMapTest() {
        RowUntilNullWithStepMapModel model = ModelReader.getModel(RowUntilNullWithStepMapModel.class);
        Assert.assertNotNull(model);
        Map<Integer, String> map = model.getMap();
        Map<Integer, String> expectedMap = new HashMap<Integer, String>() {{
            put(1, "Roddy");
            put(3, "Lili");
        }};
        Assert.assertEquals(map, expectedMap);
    }

    public void rowUntilNullWithStepMapAndSelectedCellForValuesTest() {
        MapModel1 model = ModelReader.getModel(MapModel1.class);
        Assert.assertNotNull(model);
        Map<Integer, String> map = model.getMap();
        Assert.assertNotNull(map);
        Assert.assertEquals(2, map.size());
        Map<Integer, String> expectedMap = new HashMap<Integer, String>() {{
            put(1, "Wiliams");
            put(3, "Abrams");
        }};
        Assert.assertEquals(map, expectedMap);
    }

    public void rowUntilNullWithStepMapAndSelectedRangeForValuesTest() {
        MapModel2 model = ModelReader.getModel(MapModel2.class);
        Assert.assertNotNull(model);
        Map<Integer, String> map = model.getMap();
        Map<Integer, String> expectedMap = new HashMap<Integer, String>() {{
            put(1, "Wiliams");
            put(3, "Abrams");
        }};
        Assert.assertEquals(map, expectedMap);
    }

    public void columnUntilNullMapTest() {
        MapModel3 model = ModelReader.getModel(MapModel3.class);
        Assert.assertNotNull(model);
        Map<String, Object> map = model.getMap();
        Map<String, Object> expectedMap = new HashMap<String, Object>() {{
            put("First Name", "Roddy");
            put("Last name", "Wiliams");
            put("age", (double) 34);
            put("gender", "male");
        }};
        Assert.assertEquals(map, expectedMap);
    }

    public void keyRangeAndSelectedCellForValuesMapTest() {
        MapModel4 model = ModelReader.getModel(MapModel4.class);
        Assert.assertNotNull(model);
        Map<String, Object> map = model.getMap();
        Map<String, Object> expectedMap = new HashMap<String, Object>() {{
            put("First Name", "Lili");
            put("Last name", "Abrams");
            put("age", (double) 23);
        }};
        Assert.assertEquals(map, expectedMap);
    }

    public void keyRangeAndSelectedCellForValuesWithStepMapTest() {
        MapModel5 model = ModelReader.getModel(MapModel5.class);
        Assert.assertNotNull(model);
        Map<String, Object> map = model.getMap();
        Map<String, Object> expectedMap = new HashMap<String, Object>() {{
            put("First Name", "Lili");
            put("age", (double) 23);
        }};
        Assert.assertEquals(map, expectedMap);
    }

    public void keyRangeWithStep3MapTest() {
        MapModel6 model = ModelReader.getModel(MapModel6.class);
        Assert.assertNotNull(model);
        Map<String, Object> map = model.getMap();
        Map<String, Object> expectedMap = new HashMap<String, Object>() {{
            put("First Name", "Dmytro");
            put("gender", "male");
            put("skype", "dimon.mula");
        }};
        Assert.assertEquals(map, expectedMap);
    }

    public void keyRangeAndSelectedCellForValuesWithStep3MapTest() {
        MapModel7 model = ModelReader.getModel(MapModel7.class);
        Assert.assertNotNull(model);
        Map<String, Object> map = model.getMap();
        Map<String, Object> expectedMap = new HashMap<String, Object>() {{
            put("First Name", "Dmytro");
            put("gender", "male");
            put("skype", "dimon.mula");
        }};
        Assert.assertEquals(map, expectedMap);
    }

    public void keyRangeOverRowsAndSelectedCellForValuesWithStep3MapTest() {
        MapModel8 model = ModelReader.getModel(MapModel8.class);
        Assert.assertNotNull(model);
        Map<String, Object> map = model.getMap();
        Map<String, Object> expectedMap = new HashMap<String, Object>() {{
            put("First Name", "Dmytro");
            put("gender", "male");
            put("skype", "dimon.mula");
        }};
        Assert.assertEquals(map, expectedMap);
    }

}
