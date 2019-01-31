# excelorm

[![Build Status](https://travis-ci.org/MrSkip/excelorm.svg?branch=master)](https://travis-ci.org/MrSkip/excelorm)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/009a149b5e8248968a6cee71e6b9990a)](https://www.codacy.com/app/MrSkip/excelorm?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=MrSkip/excelorm&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://coveralls.io/repos/github/MrSkip/excelorm/badge.svg?branch=master)](https://coveralls.io/github/MrSkip/excelorm?branch=master)
![Licence](https://img.shields.io/badge/apache.poi-4.0.0-brightgreen.svg)
![Licence1](https://img.shields.io/badge/license-MIT-blue.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.sombrainc/excelorm/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.sombrainc/excelorm)

The idea behind this project is to use **Object Oriented** approach instead of tons of loops while reading 
some kind of complex data from excel files. I hope you will find it interesting and simple enough to use when fighting with excel.

### Maven dependency

At first you would need to add the following maven dependency
```xml
<dependency>
  <groupId>com.sombrainc</groupId>
  <artifactId>excelorm</artifactId>
  <version>1.0.2</version>
</dependency>
```
## Quick example

Let's take an example. How would you read all of the data from this spreadsheet?
Yep, you may think of direct reading using Apache POI and a few loops to read `comments` and `expenses` sections.

![Excel](https://i.ibb.co/4M4702x/Selection-055.png "Excel doc")

Here is how you can archive it with `excelorm` (I'm using `Lombok` to generate getters/setters):

Simple POJO object:
```java
@Data
public class Invoice {

    @Cell("A1")
    private String companyName;
    @Cell("A2")
    private String streetAddress;
    @Cell("A3")
    private String city;
    @Cell("A4")
    private String phone;
    @Cell("A5")
    private String fax;
    @Cell("A6")
    private String website;

    // same mapping for the rest pure fields ..

    @CellMark
    private Bill billTo;
    
    // will be iterating down to rows until first empty cell (A17, A18 ... n) is found
    @CellCollection(cells = "A17", strategy = CellStrategy.ROW_UNTIL_NULL)
    private List<Expenses> expenses;
    
    @CellCollection(cells = "A36", strategy = CellStrategy.ROW_UNTIL_NULL)
    private List<String> comments;

    @Data
    public static class Bill {
        @Cell("A10")
        private String name;
        @Cell("A11")
        private String companyName;
        @Cell("A12")
        private String streetAddress;
        @Cell("A13")
        private String city;
        @Cell("A14")
        private String phone;
    }
    
    @Data
    public static class Expenses {
        @Cell("A17")
        private String description;
        @Cell("E17")
        private Boolean taxed;
        @Cell("F17")
        private BigDecimal amount;
    }
}
```

Also we'd need to pass the input stream of target excel document with specified sheet name and POJO class which is `Invoice.class` in this case.
```java
InputStream resourceAsStream = ReadViaStreamTest.class.getResourceAsStream("/invoice-template.xlsx");
Invoice invoice = Excelorm.read(resourceAsStream, "Invoice 1", Invoice.class);
System.out.println(invoice);
```
That's all what you would need to do to retrieve the data! Pretty simple, right?!

## Functionality

### Abilities
- Supported types: `String`, `Integer` (`int`), `Double` (`double`), `BigDecimal`, `Boolean` (`boolean`), `Float` (`float`), **enum**
- Ability to work with collection interfaces such as `List`, `Set`, `Collection`
- Supports for `Map<>` interface as well as using a user-defined object as a value in pair with key `Map<String, UserObject>`
- Read rows/columns until a first empty/null cell is found
- Defined a step between rows/columns (works only with `Map` or collections)

### Annotations
- `@Cell` - use to mark single field to get direct value from the Sheet
- `@CellCollection` - use to construct the `Set` (`HashSet`) / `List` (`ArrayList`) / `Collection` (`ArrayList`)
- `@CellMap` - use to construct the `Map` based on `HashMap`
- `@CellMark` - Use to mark some user defined object

### API
- `Excelorm.read(Sheet sheet, Class<E> targetClass)`
- `Excelorm.read(InputStream docInputStream, String sheetName, Class<E> targetClass)`
