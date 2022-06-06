package pt.isel.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;
import org.postgresql.ds.PGSimpleDataSource;

/**
 * Utility class for the project.
 */
public class Utils {

    static final String DB_URL = "jdbc:postgresql://localhost/si_g03?user=postgres&password=postgres";

    public static void createDatabase(boolean insertData) throws SQLException, IOException {
        Utils.runSqlScript("src/main/sql/delete_database.sql");
        Utils.runSqlScript("src/main/sql/create_tables.sql");
        Utils.runSqlScript("src/main/sql/views/2i_list_alarms.sql");
        Utils.runSqlScript("src/main/sql/clear_data.sql");
        Utils.iterateFiles("src/main/sql/routines", scriptFileName -> {
            try {
                Utils.runSqlScript(scriptFileName);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        });

        if (insertData)
            Utils.runSqlScript("src/main/sql/insert_data.sql");
    }

    public static void createDatabase() throws SQLException, IOException {
        createDatabase(true);
    }

    // Utility function to run an SQL script file
    public static void runSqlScript(String scriptFileName) throws IOException, SQLException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(scriptFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        }


        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setURL(DB_URL);
        Connection conn = ds.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sb.toString());

        stmt.execute();

        stmt.close();
    }

    public static void iterateFiles(String dir, Consumer<String> runnable) {
        java.io.File folder = new java.io.File(dir);
        File[] files = folder.listFiles();
        if (files == null)
            return;

        for (java.io.File fileEntry : files) {
            if (!fileEntry.isDirectory())
                runnable.accept(fileEntry.getAbsolutePath());
        }
    }
}
