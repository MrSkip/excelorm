# excel[orm]

[![Build Status](https://travis-ci.org/MrSkip/excelorm.svg?branch=master)](https://travis-ci.org/MrSkip/excelorm)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/009a149b5e8248968a6cee71e6b9990a)](https://www.codacy.com/app/MrSkip/excelorm?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=MrSkip/excelorm&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://coveralls.io/repos/github/MrSkip/excelorm/badge.svg?branch=master)](https://coveralls.io/github/MrSkip/excelorm?branch=master)
![Licence](https://img.shields.io/badge/apache.poi-4.0.0-brightgreen.svg)
![Licence1](https://img.shields.io/badge/license-MIT-blue.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.sombrainc/excelorm/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.sombrainc/excelorm)

The idea behind the project is to keep access to an **excel spreadsheet** 
in a more **human-readable format** and provide **advanced DSL syntax**, **instant access**, 
reading **complex data structures** in easiest way. Built on top of Apache POI 
this tiny library will bring into your project flexibility and robustness what you 
will see on the examples below.

### Maven dependency

At first, let's add the dependency
```xml
<dependency>
  <groupId>com.sombrainc</groupId>
  <artifactId>excelorm</artifactId>
  <version>2.0.0</version>
</dependency>
```
## Quick overview

![Excel](https://i.ibb.co/GWVhYcG/Selection-023.png "Excel doc")

*PROBLEM:* How would you read just a single cell from a spreadsheet `[A1] on the screenshot` 
using Apache POI? Your code might look something like:

```java
try {
   XSSFWorkbook sheets = new XSSFWorkbook(file);
   XSSFSheet sheet = sheets.getSheet(DEFAULT_SHEET);
   XSSFFormulaEvaluator formulaEvaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();

   String value = new DataFormatter().formatCellValue(sheet.getRow(0).getCell(0), formulaEvaluator);
} catch (IOException | InvalidFormatException e) {
   e.printStackTrace();
}
```

*SOLUTION:* In fact, it’s almost fine, except that's too many lines of code for reading 
just a single value. Here is how it can be done with a single line:

```java
String value = new EReader(file, DEFAULT_SHEET).single(String.class).pick("A1").go();
```

Sweat, right? Hold on, that’s just a little part of what we can do with this library.
Would you like to see how we can use *annotation* on POJO object in this example, here it is:

```java
public class Foo {
   @Cell("A1")
   private String value;

   // getters / setters
}
```
```java
Foo foo = Excelorm.read(file, DEFAULT_SHEET, Foo.class);
```

*excel[orm]* supports both approaches annotation processing and instant processing 
based on runtime configurations. 
Runtime processing has tools to work with a different data structure as well 
as the annotation approach and even more.

Class `EReader` has several constructors to suites multiple scenarios 
(you can find java docs written for all of them):

```java
  // Load specific {@code sheetName} sheet from the excel doc by path {@code path}
  public EReader(String path, String sheetName)

  // Load first sheet from the excel doc by path {@code path}
  public EReader(String path)

  // Load first sheet from the excel doc {@code inputStream}
  public EReader(InputStream inputStream)

  // Load specific {@code sheetName} sheet from the excel doc {@code inputStream} 
  public EReader(InputStream inputStream, String sheetName)

  // Load first sheet from the excel doc {@code file}
  public EReader(File file)

  // Load specific {@code sheetName} sheet from the excel doc {@code file}
  public EReader(File file, String sheetName)

  // Set the sheet to process
  public EReader(Sheet sheet)
```

After instantiating `EReader` class you get access to a few 
generalized methods which can define the data structure what is required for you:

```java
  // this method is responsible for creating an only single object which can be:
  // int, long, bigDecimal, enum type, double, float, string, boolean or user-defined object
  public <T> SinglePick<T> single(Class<T> aClass)

  // this method provides you a list of passing objects. It supports all the types which are supported by “@single()” method
  public <E> ListOfRange<E> listOf(Class<E> aClass)

  // by this method you will be able to map and create Map<K, V>
  public <K, V> MapOfRanges<K, V> mapOf(Class<K> key, Class<V> value)

  // this method almost the same as “@mapOf()” but with the difference that it provides a map of lists (Map<K, List<V>>)
  public <K, V> MapOfLists<K, V> mapOfList(Class<K> key, Class<V> value)
```

Each of the methods above has its own API to solve specifically 
related issues based on the chosen data structure. 
Let’s take a look at a few more examples:

1. Defining the value formatter at runtime

    ![Excel](https://i.ibb.co/MC90kFr/Selection-026.png "Excel doc")

    ```java
    String name = new EReader(file, DEFAULT_SHEET).single(String.class).pick("E2")
          .map(field -> field.toText().split(" ")[0]).go();
    ```
    *OUTPUT:* **Bill**
    
2. Converting to another type

    ![Excel](https://i.ibb.co/kScxLRL/Selection-027.png "Excel doc")
    
    ```java
    int result = new EReader(file, DEFAULT_SHEET).single(int.class).pick("E2")
           .map(field -> Integer.parseInt(field.toText().split("=")[0].trim())).go()
    ```
    *OUTPUT:* **5**

3. Read into a user-provided object
    
    ![Excel](https://i.ibb.co/MC90kFr/Selection-026.png "Excel doc")

    ```java
    public static class Foo {
       private String name;
       // getters / setters
    }
    ```
    ```java
    Foo foo = new EReader(file, DEFAULT_SHEET)
          .single(Foo.class).binds(new Bind("name", "E2")).go();
    ```
    *OUTPUT:* **Bill Gates**
    
    * Here you can also add your own mapper
    ```java
        Foo foo = new EReader(file, DEFAULT_SHEET).single(Foo.class)
               .binds(new Bind("name", "E2").map(BindField::toText))
               .go();
   ```
   
4. Read list into a user-provided object
    
    ![Excel](https://i.ibb.co/sFF1gyg/Selection-028.png "Excel doc")
    
    ```java
   public static class Foo {
      private String group;
      private List<String> students;
   
      // getters / setters
   }
    ```
   
    ```java
   Foo foo = new EReader(file, DEFAULT_SHEET).single(Foo.class)
          .binds(
                  new Bind("group", "E2"),
                  new Bind("students", "E3:G3")
          ).go();
    ```
   
   * You are also able to specify “until()”, “filter()” and “map()” 
   methods to any collection
   
   ```java
   Foo foo = new EReader(file, DEFAULT_SHEET).single(Foo.class)
          .binds(
                  new Bind("group", "E2"),
                  new Bind("students", "E3:G3")
                          .until(contains("Adrian"))
                          .filter(contains("Tom"))
                          .map(field -> "[" + field.toText() + "]")
          ).go();
   ```
   
   *OUTPUT:* **Foo(group=A, students=[[Tom]])**
   
   * Here is how you can read only student names
   
   ```java
   List<String> names = new EReader(file, DEFAULT_SHEET)
          .listOf(String.class).pick("E3:G3").go();
   ```
   
   *OUTPUT:* **[Tom, Roddy, Adrian]**
   
5. Reading a list with defined filter
    
    ![Excel](https://i.ibb.co/bKCq4JG/Selection-029.png "Excel doc")
    
    ```java
    List<String> names = new EReader(file, DEFAULT_SHEET)
           .listOf(String.class).pick("E3:K3").filter(BindField::isNotBlank).go()
    ```
    *OUTPUT:* **[Tom, Roddy, Adrian, Julia, Mishel]**
    
6. List of user-provided objects
    
    ![Excel](https://i.ibb.co/82WWGtq/Selection-030.png "Excel doc")
    
    ```java
   public class Foo {
      private long id;
      private String firstName;
      private String lastName;
      private int age;
      private Gender gender;
      private boolean driverLicense;
   
      // getters / setters
   
      public enum Gender {
          FEMALE,
          MALE
      }
   }
   ```
       
    ```java
   List<Foo> table = new EReader(file, DEFAULT_SHEET)
          .listOf(Foo.class)
          .binds(
                  new Bind("id", "A2"),
                  new Bind("firstName", "B2"),
                  new Bind("lastName", "C2"),
                  new Bind("age", "D2"),
                  new Bind("gender", "E2"),
                  new Bind("driverLicense", "F2")
          ).pick("A2:A5").go();
   ```
   
   *OUTPUT:* 
   ```json
   [
     {
       "id": 1,
       "firstName": "John",
       "lastName": "Travolta",
       "age": 12,
       "gender": "MALE",
       "driverLicense": true
     },
     {
       "id": 2,
       "firstName": "Piter",
       "lastName": "Pen",
       "age": 8,
       "gender": "MALE",
       "driverLicense": false
     },
     {
       "id": 3,
       "firstName": "Olia",
       "lastName": "Ududiak",
       "age": 20,
       "gender": "FEMALE",
       "driverLicense": true
     },
     {
       "id": 4,
       "firstName": "Marta",
       "lastName": "Chorna",
       "age": 28,
       "gender": "FEMALE",
       "driverLicense": true
     }
   ]
   ```

7. Map of a user-provided object `the same screenshot as for example 7`. 
Let’s use the **id** column as a key. 
As usual, you have access to map, filter, 
and special conditions to which point it will be iterating.
    
    ```java
    Map<Long, Foo> map = new EReader(file, DEFAULT_SHEET)
           .mapOf(Long.class, Foo.class)
           .binds(
                   new Bind("firstName", "B2"),
                   new Bind("lastName", "C2"),
                   new Bind("age", "D2"),
                   new Bind("gender", "E2"),
                   new Bind("driverLicense", "F2")
           )
           .pick("A2:A5").go()
    ```
   
    *OUTPUT:* 
    ```json
   {
     "1": {
       "firstName": "John",
       "lastName": "Travolta",
       "age": 12,
       "gender": "MALE",
       "driverLicense": true
     },
     "2": {
       "firstName": "Piter",
       "lastName": "Pen",
       "age": 8,
       "gender": "MALE",
       "driverLicense": false
     },
     "3": {
       "firstName": "Olia",
       "lastName": "Ududiak",
       "age": 20,
       "gender": "FEMALE",
       "driverLicense": true
     },
     "4": {
       "firstName": "Marta",
       "lastName": "Chorna",
       "age": 28,
       "gender": "FEMALE",
       "driverLicense": true
     }
   }
    ```
    
8. Matrices 
    
    ![Excel](https://i.ibb.co/nCYx4hP/Selection-031.png "Excel doc")
    
    ```java
    Map<Integer, Integer> map = new EReader(file, DEFAULT_SHEET)
           .mapOf(int.class, int.class)
           .pick("E2:G4", "I2:K4").go();
    ```
   
    *OUTPUT:* {**1**=10, **2**=11, **3**=12, **4**=13, 
    **5**=14, **6**=15, **7**=16, **8**=17, **9**=18}
    
9. Map of list `the same screenshot as for example 8` 
    
    ```java
   Map<Integer, List<Integer>> map = new EReader(file, DEFAULT_SHEET)
          .mapOfList(int.class, int.class)
          .pick("E2:E4", "F2:K2")
          .filterValue(BindField::isNotBlank).go()
    ```
   
    *OUTPUT:* {**1**=[2, 3, 10, 11, 12], **4**=[5, 6, 13, 14, 15], **7**=[8, 9, 16, 17, 18]}

More examples with annotation processing can be found at file `annotations.md` or go to the test folder to explore even more test examples.

If you like it, _please give a star_ to the project 
and feel free to submit a pull request to any part of the project that 
can be improved. 

`Enjoy :)`

### License
MIT