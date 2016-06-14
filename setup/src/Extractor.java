import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Extractor {

    private String path;

    public Extractor(String path) {
        this.path = path;
    }

    public Vector<Document> GetDocuments() {
        Vector<Document> documents = new Vector<Document>();

        File currentDir = new File(this.path);
        Vector<File> files = new Vector<File>();
        this.GetFilesRecursive(currentDir, files);

        for (File file : files) {
            String content = this.GetFileContent(file.toString());
            Vector<Document> docs = this.ExtractDocuments(content);
            documents.addAll(docs);
        }

        return documents;
    }

    private String GetFileContent(String filePath) {
        String everything = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
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

    private Vector<Document> ExtractDocuments(String content) {
        Vector<Document> documents = new Vector<Document>();

        Pattern paramsPattern = Pattern.compile("\"(.+?)\"");
        String[] lines = content.split("\n");
        Document doc = null;

        for (String line : lines) {
            if (line.startsWith("<doc id=")) {
                Matcher matcher = paramsPattern.matcher(line);
                Vector<String> matches = new Vector<String>();

                while (matcher.find()) {
                    matches.add(matcher.group(1));
                }
                if (matches.size() == 3) {
                    doc = new Document(matches.elementAt(0), matches.elementAt(1), matches.elementAt(2));
                }

            } else if (line.startsWith("</doc")) {
                if (doc.paragraphs.size() > 22) {
                    documents.add(doc);
                }
                doc = null;
            } else {
                doc.AddParagraph(line);
            }
        }
        return documents;
    }

    private void GetFilesRecursive(File dir, Vector<File> allFiles) {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    GetFilesRecursive(file, allFiles);
                } else {
                    allFiles.add(file);
                }
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

}
