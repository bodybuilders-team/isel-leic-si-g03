package pt.isel.dataProcessors;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import pt.isel.utils.Utils;

public class GpsDataProcessorTest {
    @Test
    public void test() throws SQLException, IOException, InterruptedException {
        Utils.createDatabase(true);

        int numThreads = 100;
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            threads.add(new Thread(() -> {
                try {
                    GpsDataProcessor processor = new GpsDataProcessor();
                    processor.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
