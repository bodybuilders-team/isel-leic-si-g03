package pt.isel.utils;

import java.io.IOException;
import java.sql.SQLException;

/**
 * This class is used to create the database.
 */
public class CreateDatabase {

    public static void main(String[] args) throws SQLException, IOException {
        Utils.createDatabase(true);
    }
}
