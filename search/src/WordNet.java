import net.sf.extjwnl.data.*;
import net.sf.extjwnl.dictionary.Dictionary;

import java.io.FileInputStream;
import java.util.*;


public class WordNet {

    private Dictionary dictionary;

    public WordNet() {
        try {
            FileInputStream inputStream = new FileInputStream("wnet/properties.xml");
            dictionary = Dictionary.getInstance(inputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> findSynonyms(String w) {
        List<String> words = new Vector<String>();
        Set<String> unique = new HashSet<String>();
        List<POS> poss = new Vector<POS>();
        poss.add(POS.NOUN);
        poss.add(POS.VERB);
        poss.add(POS.ADVERB);
        poss.add(POS.ADJECTIVE);
        words.add(w);
        for (POS pos: poss) {
            for (String s: findForPos(pos, w)) {
                unique.add(s);
            }
        }
        for (String s: unique) {
            words.add(s);
        }
        return words;
    }

    private List<String> findForPos(POS pos, String w) {
        List<String> words = new Vector<String>();
        try {
            IndexWord word = dictionary.lookupIndexWord(pos, w);
            words = findSynonymsForIndexWord(word);
        } catch (Exception ex) {}
        return words;
    }

    private List<String> findSynonymsForIndexWord(IndexWord indexWord) {
        Set<String> unique = new HashSet<String>();
        List<String> words = new Vector<String>();
        for (Synset s: indexWord.getSenses()) {
            for (Word w: s.getWords()) {
                unique.add(w.getLemma());
            }
        }
        for (String s: unique) {
            words.add(s);
        }
        return words;
    }
}
