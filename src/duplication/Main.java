package duplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
public static TreeSet<Integer> frameNumbersZ;
public static List<String> lines;
public static int primeNumLine;
public static int secondNumLine;
    public static void main(String[] args) {
        String text = "data/fr10r0_пс-4-40(S70)_Гл70_.nc";
        int i = 0;

        Scanner sc = new Scanner(System.in);
        // количество повторов по х
        int quantityByX = 0;
        // количество повторов по y
        int quantityByY = 0;
        // граница между заготовками
        int border = 0;
        try {
            System.out.println("Введите количество деталей по х");
            quantityByX = sc.nextInt();
            System.out.println("Введите количество деталей по y");
            quantityByY = sc.nextInt();
            System.out.println("Введите границу между заготовками в мм");
            border = sc.nextInt();
            System.out.println("Введите номер линии начала рабочего кода");
            primeNumLine = sc.nextInt();
            System.out.println("Введите номер линии окончания рабочего кода");
            secondNumLine = sc.nextInt();
            sc.close();
            lines = Files.readAllLines(Paths.get(text));
            List<String> partManufacturingCode = new ArrayList<>();
            //File file = new File("data/Example.nc");
            File file = new File("data/" + lines.get(0) + ".nc");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);

            List<String> startlines = new ArrayList<>();
            List<String> endlines = new ArrayList<>();
            List<String> finallines = new ArrayList<>();
            for (int j = 0; j < primeNumLine - 1; j++) {
                startlines.add(lines.get(j));
               startlines.add("\n");
            }
            for (int w = secondNumLine; w < lines.size() - 1; w++) {
                endlines.add(lines.get(w));
                endlines.add("\n");
            }
            // нахождение максимального подъёма по Z
            frameNumbersZ = new TreeSet<>();
            i = 0;
            for ( String line : lines
            ) {
                i++;
                String regexZ = "Z[-]?[0-9.]+"; // регулярное выражение
                Pattern patternZ = Pattern.compile(regexZ);
                Matcher matcherZ = patternZ.matcher(line);
                while (matcherZ.find()) { // возврящает true когда найдено соответствие
                    frameNumbersZ.add(i);
                }
            }
            // сохранение кода изготовления детали
            i=0;
            for ( String line : lines
            ) {
                i++;
                if(primeNumLine<=i && secondNumLine>=i) {
                    partManufacturingCode.add(line);
                    partManufacturingCode.add("\n");
                }
            }

            // определение габаритов детали по х
            float sizeX = DeterminingDimensionsPart(partManufacturingCode, "X[-]?[0-9.]+");
            System.out.println("Ширина движения центра фрезы по X: " + sizeX);

            // определение габаритов детали по y
            float sizeY = DeterminingDimensionsPart(partManufacturingCode, "Y[-]?[0-9.]+");
            System.out.println("Ширина движения центра фрезы по Y: " + sizeY);



            // формирование кода набора деталей по Х
            List<String> partManufacturingCodeX = new ArrayList<>();
            List<String> partManufacturingCodeXY = new ArrayList<>();
            String regexForX = "X[-]?[0-9.]+";
            String regexForY = "Y[-]?[0-9.]+";

            partManufacturingCodeX.addAll(duplicationAlongGivenAxis (quantityByX, border, sizeX, "X",
                    regexForX,  partManufacturingCode));
            partManufacturingCodeXY.addAll(duplicationAlongGivenAxis (quantityByY, border, sizeY, "Y",
                    regexForY,  partManufacturingCodeX));


            // запись данных в файл
            finallines.addAll(startlines);
            finallines.addAll(partManufacturingCodeXY);
            finallines.addAll(endlines);
            String finallineStr = "";
            for (String finalline : finallines
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
    public static float DeterminingDimensionsPart(List<String> partManufacturingCode, String regexX){
        TreeSet<Float> xCoordinates = new TreeSet<>();
        for ( String part : partManufacturingCode
        ) {
            Pattern patternX = Pattern.compile(regexX);
            Matcher matcherX = patternX.matcher(part);
            while (matcherX.find()) { // возврящает true когда найдено соответствие
                int startX = matcherX.start()+1; // находит начало части текста удовлетворяющего условиям
                // регулярного выражения
                int endX = matcherX.end(); // находит конец части текста удовлетворяющего условиям
                // регулярного выражения
                xCoordinates.add(Float.valueOf(part.substring(startX, endX))); // выводит часть текста
                // заданного интревала
            }
        }
        return xCoordinates.last() - xCoordinates.first();
    }

    public static List<String> duplicationAlongGivenAxis (int quantityBy, int borderP, float size, String a,
                                                          String regexFor,List<String> partManufacturingCodeP)
    {
        List<String> partManufacturingCode = new ArrayList<>();
        for (int j = 0; j < quantityBy; j++) {
            // рассчет сдвига
            float displacement = j * (borderP + size);
            //
            for (String part : partManufacturingCodeP
            ) {
                Pattern patternFor = Pattern.compile(regexFor);
                Matcher matcherFor = patternFor.matcher(part);
                while (matcherFor.find()) { // возврящает true когда найдено соответствие
                    int startP = matcherFor.start(); // находит начало части текста удовлетворяющего условиям
                    // регулярного выражения
                    int endP = matcherFor.end(); // находит конец части текста удовлетворяющего условиям
                    // регулярного выражения
                    String sourceString = part.substring(startP, endP);
                    String originalString = part.substring(startP + 1, endP);
                    float originalFloat = Float.valueOf(originalString);
                    String correctedString =
                            Float.toString((float) (Math.round(1000 * (originalFloat + displacement))) / 1000);
                    part = part.replaceAll(sourceString, a + correctedString);
                    // заданного интревала
                }
                partManufacturingCode.add(part);
            }
        }
        return partManufacturingCode;
    }
}
