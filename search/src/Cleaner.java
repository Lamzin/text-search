import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Cleaner {

    private static Pattern wordPattern = Pattern.compile("[a-zA-Z-']{3,}");

    static String LeaveOnlyWords(String s) {
        Vector<String> words = GetWords(s);
        return String.join(" ", words);
    }

    static Vector<String> GetWords(String s) {
        Matcher matcher = wordPattern.matcher(s);
        Vector<String> words = new Vector<String>();
        while (matcher.find()) {
            words.add(matcher.group());
        }
        return words;
    }


}
