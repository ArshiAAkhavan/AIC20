package Client;

import static Client.Constants.*;

public class StartLearn {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= NUMBER_OF_TESTS_RUNNING; i++) {
            long startTime = System.nanoTime();
            Runner onesRun = new Runner(CMD,false, SINGLE_RUN_EVERYTHING_ABDI_COMMAND);
            onesRun.start();
            onesRun.join();
            long endTime = System.nanoTime();
            long totalTime = (endTime - startTime) / 1000000;
            System.out.println("run " + i + " takes " + totalTime + "ms");
        }
    }
}
