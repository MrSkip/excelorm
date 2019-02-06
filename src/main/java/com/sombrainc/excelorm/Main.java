package com.sombrainc.excelorm;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.enumeration.CellType;
import com.sombrainc.excelorm.implementor.CellIndexTracker;
import com.sombrainc.excelorm.model.bind.ForCollection;
import com.sombrainc.excelorm.model.bind.ForMap;
import com.sombrainc.excelorm.model.bind.decorator.BasicDecorator;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author <a href=dimon.mula@gmail.com>Dmytro Mula</a>
 * Date: 30.01.19
 */
public class Main {

    static {
        BigDecimal a1 = new Excelorm2("", "")
                .forObject(BigDecimal.class)
                .resolve(conf -> conf.query(q -> q.select("A1")))
                .read();

        Main read = new Excelorm2("", "")
                .forObject(Main.class)
                .resolve(conf -> conf
                        .bind("test", new ForCollection<>(new ArrayList<String>())
                                .resolve(conf1 -> conf1
                                        .query(q -> q
                                                .select("A1")
                                                .as(BasicDecorator::getValue)
                                                .from(CellType.RANGE)
                                                .where(criteria -> criteria.getValue().isEmpty())
                                                .limit(1))
                                        .bind("name", new ForCollection<>(new ArrayList<>()))
                                        .bind("name", new ForCollection<>(new ArrayList<>()))
                                        .bind("name", new ForCollection<>(new ArrayList<>()))
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

        List<String> map12111 = new Excelorm2("", "")
                .forCollection(new ArrayList<String>(), new TypeReference<String>())
                .resolve(conf -> conf
                        .bind("test",
                                new ForMap<>(new HashMap<String, String>(), new HashMap<String, String>())
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
    }

    @Cell("sdf")
    private String test;

    public static void main(String[] args) {

//        Excelorm.read(null, new TypeReference<Map<String, String>>(), new CellMapObserver());
//        Excelorm.read(null, new TypeReference<Map<String, String>>(), new CellCollectionObserver());

        // value range with various combination
        // converter
        // filter
        //

        // don't move option

        new Excelorm(sheet)
                .forType(new TypeReference<Map<String, List<String>>>())
                .configureForMap((keyClass, valueClass) -> {
                            keyClass
                                    .select("A1")
                                    .as(valueCell -> valueCell.getValue())
                                    .from("ROW")
                                    .where(valueCell -> valueCell.get.equals("1"), 1)
                                    .until(valueCell -> valueCell.equals("1"))
                                    .limit(2);

                            keyClass.select("B1").from("COLUMN");

                            valueClass
                                    .select("C1")
                                    .configureForUserObject(
                                            (valueChildClass1, childData) -> {
//                                        valueChildClass1.select("B1");
                                                valueChildClass1.bind("name", "C1");
                                                valueChildClass1.bind("someMap",
                                                        new ForType(new TypeReference<Map<String, List<String>>>())
                                                                .
                                                );
                                            }
                                    );
                        }
                )

                .listener();

        Map<String, String> ma1p = new Excelorm(sheet)
                .forType(new TypeReference<Map<String, String>>())
                .configureForMap(
                        conf -> conf
                                .select("B1").from("COLUMN")
                                .value("C2")
                ).read();

        Map<String, String> map = new Excelorm(sheet)
                .forType(new TypeReference<Map<String, String>>())
                .useMap(conf -> conf
                        .key(key -> key.select("B1").from("COLUMN"))
                        .value(value -> value.select("C2"))
                ).read();

        Map<String, String> map1 = new Excelorm(sheet)
                .forMap(new TypeReference<Map<String, String>>())
                .resolveKey((key, conf) ->
                        key.select("A1")
                                .as(valueCell -> valueCell.getValue())
                                .from("ROW")
                                .where(valueCell -> valueCell.get.equals("1"), 1)
                                .until(valueCell -> valueCell.equals("1"))
                                .limit(2)
                ).resolveValue((value, conf) -> {
                    value.select("B1").from("COLUMN");
                })
                .read();

        Map<String, String> map12 = new Excelorm(sheet)
                .forCollection(new TypeReference<List<String>>())
                .resolve((conf) ->
                        conf.select("A1")
                                .as(valueCell -> valueCell.getValue())
                                .from("ROW")
                                .where(valueCell -> valueCell.get.equals("1"), 1)
                                .until(valueCell -> valueCell.equals("1"))
                                .limit(2)
                ).read();

        Map<String, String> map121 = new Excelorm2("", "")
                .forObject(Main.class)
                .resolve(conf -> {
                    conf
                            .bind("test",
                                    new ForCollection<List<String>>(new TypeReference<>())
                                            .resolve(conf1 -> conf1.select("A1")))
                            .query(cellSelect ->);
                }).read();

        List<String> map1211 = new Excelorm2("", "")
                .forCollection(new ArrayList<String>(), new TypeReference<String>())
                .resolve(conf -> conf
                        .bind("test",
                                new ForMap<>(new HashMap<String, String>(), new HashMap<String, String>())
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

        Excelorm.read(null, new TypeReference<Map<String, String>>(), new CellSingleObserver());
    }

    public static void query(Consumer<CellIndexTracker> queryConsumer) {
//            return this;
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

    public static class TypeReference<T> {

    }

    @Data
    public static class CellConfiguration<I, O> {
        private
    }

}