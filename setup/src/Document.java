import java.util.Vector;

public class Document {

    public String id;
    public String url;
    public String title;
    public Vector<String> paragraphs;

    Document(String id, String url, String title) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.paragraphs = new Vector<String>();
    }

    public void AddParagraph(String s) {
        this.paragraphs.add(s);
    }

}
