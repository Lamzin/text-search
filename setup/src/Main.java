import java.util.*;


public class Main {

    public static void main(String[] args) {

        Extractor extractor = new Extractor("wiki/wikiextractor/text/");

        Vector<Document> docs = extractor.GetDocuments();

        System.out.println(docs.size());

        return;
    }


}
