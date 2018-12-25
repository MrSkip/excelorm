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
        Assert.assertEquals(map.size(), 1);
        Map<Integer, String> expectedMap = new HashMap<Integer, String>() {{
            put(1, "Roddy");
        }};
        Assert.assertEquals(map, expectedMap);
    }

    public void fixedMapTest() {
        FixedMapModel model = ModelReader.getModel(FixedMapModel.class);
        Assert.assertNotNull(model);
        Assert.assertNotNull(model.getMap());
        Assert.assertEquals(model.getMap().size(), 3);
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
        Assert.assertNotNull(map);
        Assert.assertEquals(map.size(), 3);
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
        Assert.assertNotNull(map);
        Assert.assertEquals(2, map.size());
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
        Assert.assertNotNull(map);
        Assert.assertEquals(2, map.size());
        Map<Integer, String> expectedMap = new HashMap<Integer, String>() {{
            put(1, "Wiliams");
            put(3, "Abrams");
        }};
        Assert.assertEquals(map, expectedMap);
    }

}
