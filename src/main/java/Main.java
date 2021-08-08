import java.util.List;

public class Main {

    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String inputFileName1 = "data.csv";
        String outputFileName1 = "data.json";
        String inputFileName2 = "data.xml";
        String outputFileName2 = "data2.json";

        //Task1
        List<Employee> list = WorkWithFiles.parseCSV(columnMapping, inputFileName1);
        String json = WorkWithFiles.listToJson(list);
        WorkWithFiles.writeString(json, outputFileName1);

        //Task2
        List<Employee> list2 = WorkWithFiles.parseXML(inputFileName2);
        String json2 = WorkWithFiles.listToJson(list2);
        WorkWithFiles.writeString(json2, outputFileName2);

        //Task3
        String json3 = WorkWithFiles.readString(outputFileName1);
        List<Employee> list3 = WorkWithFiles.jsonToList(json3);
        list3.forEach(System.out::println);
    }

}
