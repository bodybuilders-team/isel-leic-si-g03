package pt.isel.dataProcessors;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import pt.isel.utils.Utils;

public class OptimisticGpsDataProcessorTest {

    @Test
    public void test() throws SQLException, IOException, InterruptedException {
        Utils.createDatabase(true);

        int numThreads = 10;
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            threads.add(new Thread(() -> {
                    OptimisticGpsDataProcessor processor = new OptimisticGpsDataProcessor();
                    processor.run();
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
