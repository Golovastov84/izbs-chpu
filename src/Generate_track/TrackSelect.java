package Generate_track;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrackSelect {
        public static void main(String[] args) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("y-M-d_H-m");
                Date now = new Date();
                File file = new File("data/" +  "New_generate_" + sdf.format(now) + ".nc");
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                Float startX = -62f;
                Float finishX = 62f;
                Float startY = 103f;
                Float finishY = -103f;
                String finallineStr = "";
                int trackY = 1;
                for (Float i = startX; i < finishX; i+=3) {
                    if (i < -48f || (i > -10f && i < 18f) || i > 48f) {
                        if (trackY > 0) {
                            finallineStr = finallineStr.concat(RotateY(i, startY, finishY));
                        } else {
                            finallineStr = finallineStr.concat(RotateY(i, finishY, startY));
                        }
                        trackY *= -1;
                    }
                }
                    writer.write(finallineStr);
                    writer.flush();
                    writer.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    public static String RotateY (Float dataX, Float primeY, Float finalY){
        String finallineStrFn = "";
        String lokalStr ="N1 X".concat(Float.toString((float) (Math.round(1000 * (dataX))) / 1000));
        finallineStrFn = finallineStrFn.concat(lokalStr);
            finallineStrFn =
                    finallineStrFn.concat(" Y").concat(Float.toString(primeY)).concat("\n");
            finallineStrFn = finallineStrFn.concat(lokalStr);
            finallineStrFn = finallineStrFn.concat(" Y").concat(Float.toString(finalY)).concat("\n");
        return finallineStrFn;
    }
}
