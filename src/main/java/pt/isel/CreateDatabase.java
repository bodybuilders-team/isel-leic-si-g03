package pt.isel;

import java.io.IOException;
import java.sql.SQLException;
import pt.isel.utils.Utils;

public class CreateDatabase {

    public static void main(String[] args) throws SQLException, IOException {
        Utils.createDatabase(true);
    }
}
