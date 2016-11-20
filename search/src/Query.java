import java.util.List;
import java.util.Vector;

public class Query {

    public List<String> ExtendBySynonyms(String query) {
        WordNet wn = new WordNet();
        Vector<String> words = Cleaner.GetWords(query);
        List<List<String>> wordsSynonyms = new Vector<>();
        for (String w: words) {
            wordsSynonyms.add(wn.findSynonyms(w));
        }

        List<List<String>> queriesSplitted = getAllSynonymsCombinations(wordsSynonyms);
        List<String> queries = new Vector<>();
        for (List<String> q: queriesSplitted) {
            queries.add(String.join(" ", q));
        }

        //logging
        for (List<String> l: wordsSynonyms) {
            System.out.format("%s\n", l.toString());
        }
        for (List<String> l: wordsSynonyms) {
            System.out.format("%d x ", l.size());
        }
        System.out.format(" = %d\n", queries.size());
        System.out.format("queries: %s\n", queries.size(), queries.toString());
        //end logging

        return queries;
    }

    private List<List<String>> getAllSynonymsCombinations(List<List<String>> wordsSynonyms) {
        List<List<String>> queriesSplitted = new Vector<>();
        for (List<String> synonyms: wordsSynonyms) {
            List<List<String>> old = deepListCopy(queriesSplitted);
            queriesSplitted.clear();
            for (String s : synonyms) {
                List<List<String>> tmp = deepListCopy(old);
                if (tmp.size() == 0) {
                    List<String> v = new Vector<String>();
                    v.add(s);
                    queriesSplitted.add(v);
                } else {
                    for (List<String> v : tmp) {
                        v.add(s);
                        queriesSplitted.add(v);
                    }
                }
            }
        }
        return queriesSplitted;
    }

    private List<List<String>> deepListCopy(List<List<String>> lists) {
        List<List<String>> newLists = new Vector<>();
        for (List<String> list: lists) {
            List<String> newList = new Vector<>();
            for (String s: list) {
                newList.add(s);
            }
            newLists.add(newList);
        }
        return newLists;
    }

}
