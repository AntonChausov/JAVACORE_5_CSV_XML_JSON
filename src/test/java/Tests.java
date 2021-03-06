import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tests {
    private static long suiteStartTime;
    private long testStartTime;

    @BeforeAll
    public static void initSuite() {
        System.out.println("---Running Tests---");
        suiteStartTime = System.nanoTime();
    }

    @AfterAll
    public static void completeSuite() {
        System.out.println("---Test complete: " + (System.nanoTime() - suiteStartTime) + "---");
    }

    @BeforeEach
    public void initTest() {
        System.out.println("Starting new nest");
        testStartTime = System.nanoTime();
    }

    @AfterEach
    public void finalizeTest() {
        System.out.println("Test complete:" + (System.nanoTime() - testStartTime));
    }

    @Test
    public void correctReadListFromJson(){
        // given:
        String jsonString = getJSONString();
        List<Employee> correctList = getCorrectEmployeeList();
        // when:
        List<Employee> listFromJSON = WorkWithFiles.jsonToList(jsonString);
        // then:
        assertThat(listFromJSON, equalTo(correctList));
    }

    @Test
    public void correctCreateJSONFromList(){
        // given:
        String correctJsonString = getJSONString();
        List<Employee> correctList = getCorrectEmployeeList();
        // when:
        String jsonFromList = WorkWithFiles.listToJson(correctList);
        // then:
        assertThat(jsonFromList, equalTo(correctJsonString));
    }

    @Test
    public void correctWriteFile(){
        // given:
        String fileName = "TestFile.json";
        String jsonString = getJSONString();
        // when:
        WorkWithFiles.writeString(jsonString, fileName);
        // then:
        File file = new File(fileName);
        assertThat(file.exists(), equalTo(true));
        assertThat(WorkWithFiles.readString(fileName), equalTo(jsonString));
        file.delete();
    }

    @ParameterizedTest
    @ValueSource (strings = {"data.csv", "data.xml"})
    public void filesExist(String fileName) {
        // given:
        File file = new File(fileName);
        // expect:
        assertThat(file.exists(), equalTo(true));
    }

    @ParameterizedTest
    @ValueSource (strings = {"data.csv", "data.xml"})
    public void testFileData(String fileName) {
        // given:
        String fileData = WorkWithFiles.readString(fileName);
        // expect:
        String result;
        switch (fileName){
            case ("data.csv"):
                result = getCSVData();
                result = result.replaceAll("\n", "");
                break;
            case ("data.xml"):
                result = getXMLData();
                result = result.replaceAll("\n", "");
                break;
            default:
                result = "Incorrect file name!";
                break;
        }

        //Hamcrest
        assertThat(result, MyStringMatcher.containJohnAndIvan());

        assertThat(result, equalTo(fileData));

    }

    private String getXMLData() {
        return "<staff>\n" +
                "    <employee>\n" +
                "        <id>1</id>\n" +
                "        <firstName>John</firstName>\n" +
                "        <lastName>Smith</lastName>\n" +
                "        <country>USA</country>\n" +
                "        <age>25</age>\n" +
                "    </employee>\n" +
                "    <employee>\n" +
                "        <id>2</id>\n" +
                "        <firstName>Ivan</firstName>\n" +
                "        <lastName>Petrov</lastName>\n" +
                "        <country>RU</country>\n" +
                "        <age>23</age>\n" +
                "    </employee>\n" +
                "</staff>";
    }

    private String getCSVData() {
        return "1,John,Smith,USA,25\n" +
                "2,Ivan,Petrov,RU,23";
    }

    @ParameterizedTest
    @ValueSource (strings = {"data.csv", "data.xml"})
    public void testParseFiles(String fileName){
        // given:
        List<Employee> correctList = getCorrectEmployeeList();
        // expect:
        List<Employee> listFromFile = new ArrayList<>();
        switch (fileName) {
            case ("data.csv"):
                String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
                listFromFile = WorkWithFiles.parseCSV(columnMapping, fileName);
                break;
            case ("data.xml"):
                listFromFile = WorkWithFiles.parseXML(fileName);
                break;
            default:
                break;
        }
        Comparator<Employee> lambda;
        lambda = (Employee a, Employee b) -> (int) (a.getId() - b.getId());
        correctList.sort(lambda);
        listFromFile.sort(lambda);

        //Hamcrest
        assertThat(listFromFile, is(not(empty())));
        assertThat(listFromFile, hasSize(2));

        assertThat(listFromFile, equalTo(correctList));
    }

    private List<Employee> getCorrectEmployeeList() {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee(1, "John", "Smith", "USA", 25));
        list.add(new Employee(2, "Ivan", "Petrov", "RU", 23));
        return list;
    }

    private String getJSONString() {
        return "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25}," +
                "{\"id\":2,\"firstName\":\"Ivan\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";
    }
}
