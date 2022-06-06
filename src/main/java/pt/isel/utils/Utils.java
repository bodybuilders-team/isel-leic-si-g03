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

    /**
     * Creates the database, running the SQL scripts to create the tables, views, procedures, functions and triggers.
     *
     * @param insertData if true, the data will be inserted into the database.
     * @throws SQLException if an error occurs while creating the database.
     * @throws IOException  if an error occurs while reading the SQL scripts.
     */
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

    /**
     * Runs an SQL script file
     *
     * @param scriptFileName the name of the script file
     * @throws IOException  if an error occurs while reading the SQL script.
     * @throws SQLException if an error occurs while running the SQL script.
     */
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

    /**
     * Iterates over the files in the given directory, calling the given consumer for each file.
     *
     * @param dir      the directory to iterate over.
     * @param runnable the consumer to call for each file.
     */
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
