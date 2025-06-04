package games.checkers;

import com.formdev.flatlaf.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CheckersFileManager {

    private File fObj;

    public CheckersFileManager() {
        super();
    }

    public boolean FileExists(String fileName) {
        fObj = new File(fileName);
        return fObj.exists();
    }

    public boolean ReadFile(String inputFileName) {
        fObj = new File(inputFileName);
        if (!fObj.exists()) {
            System.out.println("File does not exist: " + inputFileName);
            return false;
        }
        return true;
    }

    public void CreateFile(String fileName) {
        try {
            fObj = new File(fileName);
            if (fObj.createNewFile()) {
                System.out.println("File created: " + fObj.getName());
                WriteHeaders();
            } else {
//                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }

    public boolean WriteHeaders() {
        if (!fObj.exists()) return false;

        FileWriter writer;

        try {
            writer = new FileWriter(fObj, true);
        } catch (IOException e) {
            System.out.println("WriteHeaders(): FileWriter error");
            return false;
        }

        try {
            writer.write("Winner,Checkers_left,Moves\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("WriteHeaders(): Writer error");
            return false;
        }

        return true;
    }

    public boolean WriteData(String data) {
        if (!fObj.exists()) return false;

        FileWriter writer;

        try {
            writer = new FileWriter(fObj, true);
        } catch (IOException e) {
            System.out.println("WriteData(): FileWriter error");
            return false;
        }

        try {
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            System.out.println("WriteData(): Writer error");
            return false;
        }

        return true;
    }

    public String[][] getData() {
        if (fObj == null || !fObj.exists()) {
            System.out.println("File does not exist.");
            return null;
        }
        String[][] tempData = new String[100][100]; // Assuming a max of 100 lines and 100 characters per line

        StringBuilder data = new StringBuilder();
        try (Scanner in = new Scanner(fObj)) {
            in.useDelimiter("\n");

            String line = "";
            int lineCount = 0;

            int lineWidth = 0;

            while (in.hasNextLine()) {
                line = in.nextLine();
                lineWidth = Math.max(lineWidth, line.length());
                
                for (int i = 0; i < line.length(); i++) {
                    tempData[lineCount][i] = String.valueOf(line.charAt(i));
                }
                lineCount++;
            }
            String[][] dataArray = new String[lineCount][lineWidth];
            for (int i = 0; i < lineCount; i++) {
                for (int j = 0; j < lineWidth; j++) {
                    if (j < tempData[i].length && tempData[i][j] != null) {
                        dataArray[i][j] = tempData[i][j];
                    } else {
                        dataArray[i][j] = ""; // Fill empty spaces with empty strings
                    }
                }
            }
            return dataArray;
        } catch (IOException e) {
            System.out.println("getData(): Error reading file");
        }

        return null;
    }
}
