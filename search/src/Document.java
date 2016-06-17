import java.util.Vector;


public class Document {

    public String Id;
    public String Url;
    public String Title;

    public String FullText;
    public String CleanFullText;


    Document(String id, String url, String title, String full_text, String clean_full_text) {
        this.Id = id;
        this.Url = url;
        this.Title = title;
        this.FullText = full_text;
        this.CleanFullText = clean_full_text;
    }

}
