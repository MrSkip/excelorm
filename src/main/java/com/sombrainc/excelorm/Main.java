package com.sombrainc.excelorm;

import com.sombrainc.excelorm.e2.EReader;
import com.sombrainc.excelorm.e2.utils.EFilters;
import com.sombrainc.excelorm.enumeration.CellType;
import com.sombrainc.excelorm.model.bind.ForList;
import com.sombrainc.excelorm.model.bind.ForMap;
import com.sombrainc.excelorm.model.bind.ForObject;
import com.sombrainc.excelorm.model.bind.decorator.BasicDecorator;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.sombrainc.excelorm.e2.utils.EFilters.contains;

/**
 * @author <a href=dimon.mula@gmail.com>Dmytro Mula</a>
 * Date: 30.01.19
 */
public class Main {
    private static String test = "";

    public static class T {
        public static void main(String[] args) {
            Object main = new Main();
            for (Field field : main.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                System.out.println(field.getName());
            }
        }
    }

    static {
        BigDecimal a1 = new EReader(test)
                .single(BigDecimal.class).pick("A1").go();

        BigDecimal a11 = new EReader(test)
                .single(BigDecimal.class).pick("A1").map(cell -> BigDecimal.valueOf(cell.getNumericCellValue())).go();

        List<BigDecimal> list1 = new EReader(test)
                .listOf(BigDecimal.class)
                .pick("A1:B2")
                .filter(EFilters::isBlank)
                .until(contains(""))
                .map(c -> BigDecimal.valueOf(c.getNumericCellValue()))
                .go();

        Map<String, BigDecimal> go = new EReader(test)
                .mapOf(String.class, BigDecimal.class)
                .pick("A1:A2", "B1:B2")
                .filter(EFilters::isBlank)
                .map(Cell::getStringCellValue)
                .go();

        Map<String, List<String>> go1 = new EReader(test)
                .mapOfList(String.class, String.class)
                .pick("A1:A2", "B1:B2")
                .filter(EFilters::isBlank)
                .filterValue(contains(""))
                .map(Cell::getStringCellValue)
                .go();

        new Excelorm2("", "")
                .forObject(BigDecimal.class)
                .resolve(conf -> conf.query(q -> q.select("A1")))
                .read();

//        BigDecimal a1123 = new Excelorm2("", "")
//                .forObject(BigDecimal.class)
//                .resolve(eSelect("").filter())
//                .read();

        Main read = new Excelorm2("", "")
                .forObject(Main.class)
                .resolve(conf -> conf
                        .bind("test",
                                new ForList<>(new ArrayList<String>())
                                        .resolve(conf1 -> conf1
                                                .query(q -> q
                                                        .select("A1")
                                                        .as(BasicDecorator::getValue)
                                                        .from(CellType.RANGE)
                                                        .where(criteria -> criteria.getValue().isEmpty())
                                                        .limit(1))
                                                .bind("name", new ForList<>(new ArrayList<String>()))
                                                .bind("name", new ForList<>(new ArrayList<String>()))
                                                .bind("name", new ForList<>(new ArrayList<String>()))
                                        )
                        ))
                .read();

        Main map1211 = new Excelorm2("", "")
                .forObject(Main.class)
                .resolve(conf -> conf
                        .bind("test",
                                new ForMap<>(new HashMap<String, String>())
                                        .resolveKey(conf1 -> conf1.query(q -> q
                                                .select("A1")
                                                .as(BasicDecorator::getValue)
                                                .from(CellType.RANGE)
                                                .where(criteria -> criteria.getValue().isEmpty())))
                                        .resolveValue((conf1, key) -> conf1.query(q -> q
                                                .select("A1")
                                                .as(BasicDecorator::getValue)
                                                .from(CellType.RANGE)
                                                .where(criteria -> criteria.getValue().isEmpty())
                                                .limit(1)))
                        ))
                .read();

//        List<String> map12111 = new Excelorm2("", "")
//                .forList(new ArrayList<String>())
//                .resolve(conf -> conf
//                        .bind("test",
//                                new ForMap<>(new HashMap<String, String>())
//                                        .resolveKey(conf1 -> conf1
//                                                .query(q -> q
//                                                        .select("A1")
//                                                        .as(BasicDecorator::getValue)
//                                                        .from(CellType.RANGE)
//                                                        .where(criteria -> criteria.getValue().isEmpty())))
//                                        .resolveValue((conf1, key) ->
//                                                conf1.query(
//                                                        q -> q
//                                                                .select("A1")
//                                                                .as(BasicDecorator::getValue)
//                                                                .from(CellType.RANGE)
//                                                                .where(criteria -> criteria.getValue().isEmpty())
//                                                                .limit(1)))
//                        )
//                )
//                .read();
    }

//    @Cell("sdf")
//    private String test;

    public static void main(String[] args) {

//        Excelorm.read(null, new TypeRef<Map<String, String>>(), new CellMapObserver());
//        Excelorm.read(null, new TypeRef<Map<String, String>>(), new CellCollectionObserver());

        // value range with various combination
        // converter
        // filter
        //

        // don't move option

        Map<Object, Object> map12111 = new Excelorm2("", "")
                .forMap(new HashMap<>())
                .resolveKey(conf -> conf
                        .bind("test", new ForMap<>(new HashMap<String, String>())
                                .resolveKey(conf1 -> conf1
                                        .query(q -> q
                                                .select("A1")
                                                .as(BasicDecorator::getValue)
                                                .from(CellType.RANGE)
                                                .where(criteria -> criteria.getValue().isEmpty())))
                                .resolveValue((conf1, key) -> conf1
                                        .query(q -> q
                                                .select("B1")
                                                .as(BasicDecorator::getValue)))
                        ))
                .read();

        Map<Object, Object> map12112 = new Excelorm2("", "")
                .forMap(new HashMap<>())
                .resolveKey(conf -> conf
                        .bind("test", new ForMap<>(new HashMap<String, Main>())
                                .resolveKey(conf1 -> conf1
                                        .query(q -> q
                                                .select("A1")
                                                .as(BasicDecorator::getValue)
                                                .from(CellType.RANGE)
                                                .where(criteria -> criteria.getValue().isEmpty())))
                                .resolveValue((conf1, key) ->
                                        conf1.bind("name", new ForObject<>(BigDecimal.class)
                                                .resolve(conf2 -> conf2.query(q -> q.select("B1")))
                                        )
                                )
                        ))
                .read();

    }

    public interface CellSelect<I, O> extends CellSelectOnly<I, O> {
        CellSelect<I, O> select(String address);

        CellSelect<I, O> as(Function<I, O> mapper);

        CellSelect<I, O> from(CellType type);

        CellSelect<I, O> where(Function<I, Boolean> criteria);

        CellSelect<I, O> until(Function<I, Boolean> criteria);

        void limit(int limit);
    }

    public interface CellSelectOnly<I, O> {
        CellSelectOnly<I, O> select(String address);

        CellSelectOnly<I, O> as(Function<I, O> mapper);
    }

    public static class TypeRef<T> {

    }

}