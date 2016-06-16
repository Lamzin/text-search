import javax.print.Doc;
import java.sql.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.zip.CRC32;


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

    private int StringToCRC32(String s) {
        CRC32 crc = new CRC32();
        crc.update(s.getBytes());
        return (int) crc.getValue();
    }

    public void InsertToWords(String documentId, HashMap<String, Vector<Integer>> map) {
        try {
            StringBuilder SQLbuilder = new StringBuilder()
                    .append("INSERT INTO Words (hash, document, positions) VALUES (?, ?, ?)\n");
            for (int i = 1; i < map.size(); i++) {
                SQLbuilder.append(", (?, ?, ?)\n");
            }
            String SQL = SQLbuilder.toString();

            PreparedStatement statement = this.conn.prepareStatement(SQL);

            int i = 0;
            for (HashMap.Entry<String, Vector<Integer>> entry : map.entrySet()) {
                int hash = StringToCRC32(entry.getKey());
                statement.setInt(i + 1, hash);
                statement.setString(i + 2, documentId);
                statement.setObject(i + 3, entry.getValue());
                i += 3;
            }

            statement.execute();

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void InsertToDocuments(Document doc) {
        try {
            String SQL = new StringBuilder()
                    .append("INSERT INTO Documents (id, url, title, full_text, clean_text)\n")
                    .append("     VALUES (?, ?, ?, ?, ?)\n")
                    .toString();
            PreparedStatement statement = this.conn.prepareStatement(SQL);

            statement.setString(1, doc.Id);
            statement.setString(2, doc.Url);
            statement.setString(3, doc.Title);
            statement.setString(4, doc.FullText);
            statement.setString(5, doc.CleanFullText);
            statement.execute();
            System.out.printf("`%s` inserted!\n", doc.Title);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public Document GetNotProceededDocument() {
        Document doc = null;

        try {
            String SQL = new StringBuilder()
                    .append("SELECT d.id, d.url, d.title, d.full_text, d.clean_text     \n")
                    .append("  FROM Documents AS d                                      \n")
                    .append("  LEFT JOIN Queue AS q ON d.id = q.id                      \n")
                    .append("  WHERE q.id IS NULL                                       \n")
                    .append("  LIMIT 1                                                  \n")
                    .toString();
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

    public void MarkAsDone(String documentId) {
        try {
            String SQL = new StringBuilder()
                    .append("INSERT INTO Queue (id, status)   \n")
                    .append("     VALUES (?, ?)               \n")
                    .toString();
            PreparedStatement statement = this.conn.prepareStatement(SQL);

            statement.setString(1, documentId);
            statement.setString(2, "done");
            statement.execute();
//            System.out.printf("`%s` mark as done!\n", documentId);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void CreateDocuments() {
        try {
            Statement statement = this.conn.createStatement();
            String SQL = new StringBuilder()
                    .append("CREATE TABLE Documents (              \n")
                    .append("   `id`         INT          NOT NULL,\n")
                    .append("   `url`        VARCHAR(128) NOT NULL,\n")
                    .append("   `title`      VARCHAR(128) NOT NULL,\n")
                    .append("   `full_text`  MEDIUMTEXT   NOT NULL,\n")
                    .append("   `clean_text` MEDIUMTEXT   NOT NULL,\n")
                    .append("   PRIMARY KEY (`id`))                \n")
                    .toString();
            statement.execute(SQL);
            System.out.println("`Documents` created!");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void CreateWords() {
        try {
            Statement statement = this.conn.createStatement();
            String SQL = new StringBuilder()
                    .append("CREATE TABLE Words (                         \n")
                    .append("   `hash`       INT          NOT NULL,       \n")
                    .append("   `document`   INT          NOT NULL,       \n")
                    .append("   `positions`  MEDIUMBLOB   NOT NULL,       \n")
                    .append("   INDEX hash_documents(`hash`, `document`)) \n")
                    .toString();
            statement.execute(SQL);
            System.out.println("`Words` created!");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void CreateQueue() {
        try {
            Statement statement = this.conn.createStatement();
            String SQL = new StringBuilder()
                    .append("CREATE TABLE Queue (               \n")
                    .append("   `id`     INT          NOT NULL, \n")
                    .append("   `status` VARCHAR(32)  NOT NULL, \n")
                    .append("   PRIMARY KEY (`id`))             \n")
                    .toString();
            statement.execute(SQL);
            System.out.println("`Queue` created!");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

}
