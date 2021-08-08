import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String inputFileName1 = "data.csv";
        String outputFileName1 = "data.json";
        String inputFileName2 = "data.xml";
        String outputFileName2 = "data2.json";

        //Task1
        List<Employee> list = parseCSV(columnMapping, inputFileName1);
        String json = listToJson(list);
        writeString(json, outputFileName1);

        //Task2
        List<Employee> list2 = parseXML(inputFileName2);
        String json2 = listToJson(list2);
        writeString(json2, outputFileName2);

        //Task3
        String json3 = readString(outputFileName1);
        List<Employee> list3 = jsonToList(json3);
        list3.forEach(System.out::println);
    }

    private static List<Employee> jsonToList(String json3) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type itemsListType = new TypeToken<List<Employee>>() {}.getType();
        List<Employee> list = gson.fromJson(json3, itemsListType);
        return list;
    }

    private static String readString(String outputFileName1) {
        try (BufferedReader br = new BufferedReader(new FileReader(outputFileName1))){
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static List<Employee> parseXML(String inputFileName2) {
        List<Employee> staff = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(inputFileName2));
            Node root = doc.getDocumentElement();
            staff = readNode(root);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return staff;
    }

    private static List<Employee> readNode(Node node) {
        List<Employee> staff = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nodeEmployee = nodeList.item(i);
            if (Node.ELEMENT_NODE == nodeEmployee.getNodeType() && nodeEmployee.getNodeName().equals("employee")) {
                staff.add(readEmployee((Element) nodeEmployee));
            }
        }
        return staff;
    }

    private static Employee readEmployee(Element node) {
        Employee employee = new Employee();
        Element element = node;
        employee.setId(Long.parseLong(getTagValue("id", element)));
        employee.setFirstName(getTagValue("firstName", element));
        employee.setLastName(getTagValue("lastName", element));
        employee.setCountry(getTagValue("country", element));
        employee.setAge(Integer.parseInt(getTagValue("age", element)));
        return employee;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    private static void writeString(String json, String outputFileName) {
        try (FileWriter writer = new FileWriter(outputFileName, false)) {
            writer.write(json);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping[0], columnMapping[1], columnMapping[2], columnMapping[3], columnMapping[4]);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;
    }
}
