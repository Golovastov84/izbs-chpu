package frame_numbering;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static List<String> lines;
    public static void main(String[] args) {
        String text = "data/fr10r0_пс-4-40(S70)_Гл70_.nc";

        try {
            lines = Files.readAllLines(Paths.get(text));
            Date now = new Date();
            File file = new File("data/" + lines.get(0) + "_" + now.getTime() + ".nc");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write("text");
            writer.flush();
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
