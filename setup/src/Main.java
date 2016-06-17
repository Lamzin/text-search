import java.util.*;

public class Main {

    public static void main(String[] args) {

        DbInit();

        StartDocumentProcessing();

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
