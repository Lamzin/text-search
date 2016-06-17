import java.util.HashMap;
import java.util.Vector;


public class Document {

    public String Id;
    public String Url;
    public String Title;

    public String FullText;
    public String CleanFullText;

    public Vector<String> Paragraphs;

    Document(String id, String url, String title) {
        this.Id = id;
        this.Url = url;
        this.Title = title;
        this.Paragraphs = new Vector<String>();
    }

    Document(String id, String url, String title, String full_text, String clean_full_text) {
        this.Id = id;
        this.Url = url;
        this.Title = title;
        this.FullText = full_text;
        this.CleanFullText = clean_full_text;
        this.Paragraphs = new Vector<String>();
    }

    public void ProceedParagraphs() {
        this.FullText = String.join("\n", this.Paragraphs);

        Vector<String> cleanParagraphs = new Vector<String>();
        for (String paragraph : this.Paragraphs) {
            cleanParagraphs.add(Cleaner.LeaveOnlyWords(paragraph));
        }
        this.CleanFullText = String.join("\n", cleanParagraphs);
    }

    public void AddParagraph(String s) {
        this.Paragraphs.add(s);
    }

    public void StartProcessing(Database db) {
        Vector<String> words = Cleaner.GetWords(this.CleanFullText);

        HashMap<String, Vector<Integer>> map = new HashMap<String, Vector<Integer>>();

        for (int i = 0; i < words.size(); i++) {
            if (!map.containsKey(words.elementAt(i))) {
                map.put(words.elementAt(i), new Vector<Integer>());
            }
            map.get(words.elementAt(i)).add(i);
        }

        db.InsertToWords(this.Id, map);

        System.out.printf("`%s`(`%s`) document finished\n", this.Id, this.Title);
    }

}
