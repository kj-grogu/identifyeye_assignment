import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PatientExamDataAggregateService {

    //Type of instructions:
    //1. Add a patient record
    //  Ex: ADD PATIENT 123 JOHN DOE
    //2. Add an exam record
    //  Ex: ADD EXAM 123 456
    //3. Delete a patient record
    //  Ex: DEL PATIENT 123
    //4. Delete an exam record
    //  Ex: DEL EXAM 456

    public static Map<String, String> patientIdToName = new HashMap<>();
    public static Map<String, Set<String>> patientToExams = new HashMap<>();

    //Method to read input file and call patientDataAction() method for every line to execute its commant:
    public static void readFile(String fileName) {
        
        //Logic to read input file line by line:
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                //Method call to validate and execute commond mentioned at current line:
                patientDataAction(line);
            }

            //Constructing output result set:
            Set<String> result = new HashSet<>();
            StringBuilder temp = new StringBuilder();
            for (String key : patientIdToName.keySet()) {
                temp.append("Name: " + patientIdToName.get(key) + ", Id: " + key + ", Exam Count: ");
                temp.append(patientToExams.containsKey(key) ? patientToExams.get(key).size() : 0);
                result.add(temp.toString());
                temp.setLength(0);
            }
            //Printing the result set:
            for (String element : result) {
                System.out.println(element);
            }
        } catch (IOException e) {
            System.out.println("Failed to read the input.txt file.");
            e.printStackTrace();
        }
    }

    //Method to validate and execute commant mentioned on each line of input file
    public static void patientDataAction(String line) {
        String[] lineArr = line.split("\\s+");
        if(lineArr.length < 3){
            return; // not a valid input cannot process this line
        }
        String patientId, command, type;
        command = lineArr[0];
        type = lineArr[1];
        patientId = lineArr[2];

        //Logic to execute commant at current line:
        if (command.equals("ADD")) {
            if (type.equals("PATIENT")) {
                if (!patientIdToName.containsKey(patientId)) {
                    StringBuilder name = new StringBuilder();
                    for (int i = 3; i < lineArr.length; i++)
                        name.append(lineArr[i] + " ");
                    patientIdToName.put(patientId, name.toString().trim());
                }
            }
            if (type.equals("EXAM") && patientIdToName.containsKey(patientId) && lineArr.length > 3) {
                String examId = lineArr[3];
                if (!patientToExams.containsKey(patientId)) {
                    patientToExams.put(patientId, new HashSet<>());
                }
                patientToExams.get(patientId).add(examId);
            }
        } else if (command.equals("DEL") && patientIdToName.containsKey(patientId)) {
            if (type.equals("PATIENT")) {
                patientIdToName.remove(patientId);
                if (patientToExams.containsKey(patientId))
                    patientToExams.remove(patientId);
            }
            if (type.equals("EXAM")) {
                if (patientToExams.containsKey(patientId) && lineArr.length > 3) {
                    String examId = lineArr[3];
                    if (patientToExams.get(patientId).contains(examId)) {
                        patientToExams.get(patientId).remove(examId);
                    }
                }
            }
        }
    }


    public static void main(String[] args) {
        System.out.println("Hello world!");
        String dir = System.getProperty("user.dir");
        String os = System.getProperty("os.name");
        String fileName = dir + (os.toLowerCase().contains("window") ?  "\\input.txt" : "/input.txt");
        readFile(fileName);
    }
}



