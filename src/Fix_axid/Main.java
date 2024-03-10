package Fix_axid;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static List<String> lines;
    public static void main(String[] args) {
        String text = "data/1.nc";

        try {
            lines = Files.readAllLines(Paths.get(text));
            SimpleDateFormat sdf = new SimpleDateFormat("y-M-d_H-m");
            Date now = new Date();
            File file = new File("data/" + lines.get(0).substring(2) + "_" + sdf.format(now) + ".nc");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            List<String> finalLines = new ArrayList<>();
            String axid = "Z";
            String regex = axid.concat("[-]*[0-9]+[.]*[0-9]+");
            float i = 1f;
            for ( String line : lines
            ) {
                Pattern patternFor = Pattern.compile(regex);
                Matcher matcherFor = patternFor.matcher(line);
                while (matcherFor.find()) { // возврящает true когда найдено соответствие
                    int startP = matcherFor.start(); // находит начало части текста удовлетворяющего условиям
                    // регулярного выражения
                    int endP = matcherFor.end(); // находит конец части текста удовлетворяющего условиям
                    // регулярного выражения
                    String originalString = line.substring(startP + 1, endP);
//                    String originalFloat = Float.toString(Float.parseFloat(originalString) + i);
                   // (float) (Math.round(1000 * (originalFloat + displacement))) / 1000
                    Float originalFloat = (float) (Math.round(1000 * (Float.parseFloat(originalString) + i))) / 1000;
                    String fixString = Float.toString(originalFloat);
                    line = line.replaceAll(regex, axid.concat(fixString));
                    // заданного интревала
                }

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
