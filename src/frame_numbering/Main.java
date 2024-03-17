package frame_numbering;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static List<String> lines;
    public static void main(String[] args) {
        String text = "data/DB_min_step_clean_add.nc";

        try {
            lines = Files.readAllLines(Paths.get(text));
            SimpleDateFormat sdf = new SimpleDateFormat("y-M-d_H-m");
            Date now = new Date();
            File file = new File("data/" + lines.get(0).substring(2) + "_" + sdf.format(now) + ".nc");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            List<String> finalLines = new ArrayList<>();
            String regex = "^N[0-9]+";
            int i = 1;
            for ( String line : lines
            ) {
                Pattern patternX = Pattern.compile("^[^N]{1}");
                Matcher matcherX = patternX.matcher(line);
                while (matcherX.find()) {
                    line="N1 ".concat(line);
                }
                line=line.replaceAll(regex,"N" + i);
                i += 2;
                finalLines.add(line);
                finalLines.add("\n");
            }
            String finallineStr = "";
            for (String finalline : finalLines
            ) {
                finallineStr = finallineStr.concat(finalline);
            }
            writer.write(finallineStr);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
