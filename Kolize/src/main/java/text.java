
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class text {

    public static String load(String text) {
        String content = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(text));
            String newline = br.readLine();
            int i = 0;
            while (newline != null) {
                content += newline + "\n";
                newline = br.readLine();
                i++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}