import com.oracle.xmlns.internal.webservices.jaxws_databinding.ExistingAnnotationsType;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {

    private Database db;

    public static void main(String[] args) {
//        long startTime = System.nanoTime();
        Main main = new Main();
    }

    public Main() {
        db = new Database();

        long startTime = System.nanoTime();
        String s = GetInputDataFromFile();
        for (String paragraph : s.split("\n")) {
            if (paragraph.length() == 0) {
                continue;
            }
            FindDocumentsForParagraph(db, paragraph);
        }
        System.out.println((double) (System.nanoTime() - startTime) / 1000000000.0f);
    }

    private void FindDocumentsForParagraph(Database db, String input) {
        List<String> extendQueries = new Query().ExtendBySynonyms(input);
        for (String q: extendQueries) {
            FindDocsForQuery(q);
        }
    }

    private void FindDocsForQuery(String query) {
        Vector<String> words = Cleaner.GetWords(query);

        Set<Integer> hashes = new HashSet<Integer>();
        for (String word : words) {
            hashes.add(Utils.StringToCRC32(word));
        }

        Vector<Integer> documents = db.GetDocumentsWithWords(hashes);
        System.out.format("'%s': %s\n", query, documents.toString());

//        Map<Integer, Index> data = db.GetDataForIndexes(hashes, documents);
//        for (Map.Entry<Integer, Index> entry : data.entrySet()) {
//            entry.getValue().Build(input);
//        }
    }

    private static String GetInputDataFromFile() {
        String everything = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader("search/input2.txt"));
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
