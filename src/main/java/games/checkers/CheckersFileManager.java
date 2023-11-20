package games.checkers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CheckersFileManager {

    private File fObj;

    public CheckersFileManager() {
        super();
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
            writer = new FileWriter(fObj);
        } catch (IOException e) {
            System.out.println("WriteHeaders(): FileWriter error");
            return false;
        }

        try {
            writer.write("Game,Winner,Checkers left,Moves\n");
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
}
