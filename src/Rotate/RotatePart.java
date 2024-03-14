package Rotate;

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

public class RotatePart {
    public static List<String> lines;
    public static void main(String[] args) {
        String text = "data/DB_min_step_clean_select.nc";
        List<String> repXW = new ArrayList<>();
        List<String> repYX = new ArrayList<>();
        List<String> repWY = new ArrayList<>();
        List<String> repYY = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(text));
            SimpleDateFormat sdf = new SimpleDateFormat("y-M-d_H-m");
            Date now = new Date();
            File file = new File("data/" + lines.get(0).substring(2) + "_" + sdf.format(now) + ".nc");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            repXW.addAll(Replacement("X", "W",lines));
            repYX.addAll(Replacement("Y", "X",repXW));
            repWY.addAll(Replacement("W", "Y",repYX));
            repYY.addAll(ReplacePolarityY(repWY));

            String finallineStr = "";
            for (String finalline : repYY
            ) {
                finallineStr = finallineStr.concat(finalline);
                finallineStr = finallineStr.concat("\n");
            }
            writer.write(finallineStr);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> Replacement (String prime, String second, List<String> partManufacturingCodeP)
    {
        List<String> partManufacturingCode = new ArrayList<>();
        for (String part : partManufacturingCodeP) {
                part=part.replaceAll(prime,second);
                partManufacturingCode.add(part);
        }
        return partManufacturingCode;
    }

    public static List<String> ReplacePolarityY (List<String> partManufacturingCodeP)
    {
        List<String> partManufacturingCode = new ArrayList<>();
        String regexFor = "Y[-]?";
        for (String part : partManufacturingCodeP) {
            Pattern patternFor = Pattern.compile(regexFor);
            Matcher matcherFor = patternFor.matcher(part);
            while (matcherFor.find()) { // возврящает true когда найдено соответствие
                int startP = matcherFor.start(); // находит начало части текста удовлетворяющего условиям
                // регулярного выражения
                int endP = matcherFor.end(); // находит конец части текста удовлетворяющего условиям
                // регулярного выражения
                String sourceString = part.substring(startP, endP);
                String originalString = part.substring(startP + 1, endP);
                if(originalString.equals("-")){
                    part = part.replaceAll(sourceString, part.substring(startP, endP - 1));
                } else {
                    part = part.replaceAll(sourceString, part.substring(startP, endP).concat("-"));
                }
            }
            partManufacturingCode.add(part);
        }
        return partManufacturingCode;
    }
}
