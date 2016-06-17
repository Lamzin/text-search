import java.util.*;

public class Main {

    public static void main(String[] args) {
        long startTime = System.nanoTime();

//        DbInit();

//        InsertDocuments();

        StartDocumentProcessing();


        System.out.println((double) (System.nanoTime() - startTime) / 1000000000.0f);

        return;
    }

    private static void DbInit() {
        Database db = new Database();
        db.CreateDocuments();
        db.CreateWords();
        db.CreateQueue();
    }

    private static void InsertDocuments() {
        Extractor extractor = new Extractor("wiki/wikiextractor/text/");
        Vector<Document> docs = extractor.GetDocuments();

        System.out.println("\n\nSize:");
        System.out.println(docs.size());

        Database db = new Database();

        for (Document doc : docs) {
            db.InsertToDocuments(doc);
        }
    }

    private static void StartDocumentProcessing() {
        Database db = new Database();
        Document doc;
        while ((doc = db.GetNotProceededDocument()) != null) {
            doc.StartProcessing(db);
            db.MarkAsDone(doc.Id);
        }
    }

}
