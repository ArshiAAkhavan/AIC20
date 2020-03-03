package Client;

import static Client.Constants.*;
import static Client.Constants.CLIENT_RUN_ABDI_COMMAND_PART2;

public class StartLearn {
    public static void main(String[] args) {
        int port = PORT_RUNNER_MAKER;
        //run server
        new Runner(SERVER_RUN_ABDI_COMMAND).start();
        //run IOHandler
        new Runner(IO_RUN_ABDI_COMMAND_PART1 + port + IO_RUN_ABDI_COMMAND_PART2).start();
        //run clients
        for (int i = 1; i <= NUMBER_OF_PARALLEL_RUNNING_CLIENTS; i++) {
            port += i;
            new Runner(CLIENT_RUN_ABDI_COMMAND_PART1 + port + CLIENT_RUN_ABDI_COMMAND_PART2).start();
        }

    }
}
