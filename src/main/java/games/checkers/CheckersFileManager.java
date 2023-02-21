package games.checkers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CheckersFileManager {

    private File fObj;
    private FileWriter fWriter;

    public CheckersFileManager() {
        super();
    }

    public void CreateFile(String fileName) {
        try {
            fObj = new File(fileName + ".csv");
            if (fObj.createNewFile()) {
                System.out.println("File created: " + fObj.getName());

            } else {
                System.out.println("File already exists.");
            }
            fWriter = new FileWriter(fObj.getName());
            WriteHeaders();
        } catch (IOException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }

    private void WriteHeaders() {
        if (fObj.exists()) {
            try {
                fWriter.write("Game ID,Winner,nMoves\n");
                fWriter.close();
            } catch (IOException e) {
                System.out.println("Error writing to file");
                e.printStackTrace();
            }
        }
    }
}
