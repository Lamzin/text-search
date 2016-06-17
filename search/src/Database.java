import com.sun.deploy.util.StringUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


public class Database {


    private Connection conn;


    public Database() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/text-search", "wiki_bot", "31415");

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }


    public Vector<Integer> GetDocumentsWithWords(Set<Integer> hashes) {
        Vector<Integer> answer = new Vector<Integer>();

        Vector<String> hashesS = new Vector<String>();
        for (Integer item : hashes) {
            hashesS.add(item.toString());
        }

        try {
            String SQL = new StringBuilder()
                    .append("    SELECT document                                      \n")
                    .append("    FROM (                                               \n")
                    .append("        SELECT document, count(*) as cnt                 \n")
                    .append("          FROM Words                                     \n")
                    .append("         WHERE hash IN (%s)                              \n")
                    .append("         GROUP BY document                               \n")
                    .append("    ) as tmp                                             \n")
                    .append("    WHERE cnt = %d                                       \n")
                    .toString();
            SQL = String.format(
                    SQL,
                    StringUtils.join(hashesS, ","),
                    hashes.size());

            PreparedStatement statementR = this.conn.prepareStatement(SQL);
            ResultSet result = statementR.executeQuery();

            Integer doc = null;
            while (result.next()) {
                doc = result.getInt("document");
                answer.add(doc);
            }
            result.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        return answer;
    }

    public Map<Integer, Index> GetDataForIndexes(Set<Integer> hashes, Vector<Integer> documents) {
        Map<Integer, Index> answer = new HashMap<Integer, Index>();

        Vector<String> hashesS = new Vector<String>();
        for (Integer item : hashes) {
            hashesS.add(item.toString());
        }

        Vector<String> documentsS = new Vector<String>();
        for (Integer item : documents) {
            documentsS.add(item.toString());
        }

        try {
            String SQL = new StringBuilder()
                    .append("    SELECT hash, document, positions                     \n")
                    .append("    FROM Words                                           \n")
                    .append("    WHERE hash     IN (%s)                               \n")
                    .append("      AND document IN (%s)                               \n")
                    .toString();
            SQL = String.format(
                    SQL,
                    StringUtils.join(hashesS, ","),
                    StringUtils.join(documentsS, ","));

            PreparedStatement statement = this.conn.prepareStatement(SQL);
            ResultSet result = statement.executeQuery();

            Integer hash = null;
            Integer document = null;
            Blob positions = null;
            while (result.next()) {
                hash = result.getInt("hash");
                document = result.getInt("document");
                positions = result.getBlob("positions");

                byte[] blobAsBytes = positions.getBytes(1, (int)positions.length());
                positions.free();
                int[] intPositions = Utils.bytesToInts(blobAsBytes);

                if (!answer.containsKey(document)) {
                    answer.put(document, new Index(document));
                }
                answer.get(document).positions.add(new Position(hash, intPositions));

            }
            result.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        return answer;
    }

    public Document GetDocument(Integer id) {
        Document doc = null;

        try {
            String SQL = new StringBuilder()
                    .append("SELECT id, url, title, full_text, clean_text     \n")
                    .append("  FROM Documents AS d                            \n")
                    .append("  WHERE id = %d                                  \n")
                    .toString();
            SQL = String.format(SQL, id);
            PreparedStatement statement = this.conn.prepareStatement(SQL);

            ResultSet result = statement.executeQuery();
            result.next();
            doc = new Document(
                    result.getString("id"),
                    result.getString("url"),
                    result.getString("title"),
                    result.getString("full_text"),
                    result.getString("clean_text")
            );
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        return doc;
    }

}
