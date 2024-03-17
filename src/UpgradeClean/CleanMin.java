package UpgradeClean;

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

public class CleanMin {
    public static List<String> lines;
    public static void main(String[] args) {
        String text = "data/7.nc";
        List<String> finalList = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(text));
            SimpleDateFormat sdf = new SimpleDateFormat("y-M-d_H-m");
            Date now = new Date();
            File file = new File("data/" + lines.get(0).substring(2) + "_" + sdf.format(now) + ".nc");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            String regexStar = "Z-4.05";
            String regexStarZ = "Z5.";
            String regexStop =  "Z-2.05";
            String regexStopZ =  "Z-3.05";
            boolean targetZ = true;
            for (String part : lines) {
                Pattern patternStart = Pattern.compile(regexStar);
                Matcher matcherStart = patternStart.matcher(part);
                Pattern patternStartZ = Pattern.compile(regexStarZ);
                Matcher matcherStartZ = patternStartZ.matcher(part);
                Pattern patternStop = Pattern.compile(regexStop);
                Matcher matcherStop = patternStop.matcher(part);
                Pattern patternStopZ = Pattern.compile(regexStopZ);
                Matcher matcherStopZ = patternStopZ.matcher(part);
                while (matcherStop.find() || matcherStopZ.find()) {
                    targetZ = false;
                }
                while (matcherStart.find() || matcherStartZ.find()) {
                    targetZ = true;
                }
                if(targetZ){
                    finalList.add(part);
                }
            }
                String finallineStr = "";
                for (String finalline : finalList
                ) {
                    finallineStr = finallineStr.concat(finalline);
                    finallineStr = finallineStr.concat("\n");
                }
                writer.write(finallineStr);
                writer.flush();
                writer.close();

            } catch(IOException e){
                throw new RuntimeException(e);
            }
        }
}
