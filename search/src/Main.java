import javax.rmi.CORBA.Util;
import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Main {

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        Database db = new Database();

        String s = GetInputDataFromFile();

        for (String paragraph : s.split("\n")) {
            if (paragraph.length() == 0) {
                continue;
            }
            FindDocumentsForParagraph(db, paragraph);
        }

        System.out.println((double) (System.nanoTime() - startTime) / 1000000000.0f);
    }

    private static void FindDocumentsForParagraph(Database db, String input) {
        Vector<String> words = Cleaner.GetWords(input);

        Set<Integer> hashes = new HashSet<Integer>();
        for (String word : words) {
            hashes.add(Utils.StringToCRC32(word));
        }

        Vector<Integer> documents = db.GetDocumentsWithWords(hashes);

        Map<Integer, Index> data = db.GetDataForIndexes(hashes, documents);

        for (Map.Entry<Integer, Index> entry : data.entrySet()) {
            entry.getValue().Build(input);
        }

//        System.out.println(documents);
    }

    private static String GetInputDataFromFile() {
        String everything = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader("search/input.txt"));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                everything = sb.toString();
            } catch (Exception e) {
                System.out.print(e.toString());
            } finally {
                br.close();
            }

        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return everything;
    }

}
