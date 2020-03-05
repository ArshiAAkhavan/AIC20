package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static Client.Constants.*;
import static Client.Constants.CLIENT_RUN_ABDI_COMMAND_PART2;

public class OnesRun {
    private Runner server;
    private Runner TableHandler;
    private Runner[] clients;
    private static final boolean SHOW_OUT_PUT = false;
    public static void main(String[] args) throws IOException {
        OnesRun program = new OnesRun(PORT_RUNNER_MAKER);
        program.getServer().start();
        program.getTableHandler().start();
        waitForBeingTableReady();
        for (Runner client : program.getClients()) {
            client.start();
        }
    }

    public OnesRun(int port) {
        //set server
        server = new Runner(CMD,SHOW_OUT_PUT,SERVER_RUN_ABDI_COMMAND);
        //set IOHandler
        TableHandler = new Runner(CMD, SHOW_OUT_PUT,TABLE_HANDLER_RUN_ABDI_COMMAND_PART1 + port + TABLE_HANDLER_RUN_ABDI_COMMAND_PART2);
        //set clients
        clients = new Runner[NUMBER_OF_PARALLEL_RUNNING_CLIENTS];
        for (int i = 1; i <= clients.length; i++) {
            port += i;
            clients[i-1] = new Runner(CMD,SHOW_OUT_PUT,CLIENT_RUN_ABDI_COMMAND_PART1 + port + CLIENT_RUN_ABDI_COMMAND_PART2);
        }
    }

    public static void waitForBeingTableReady() throws IOException {
        Socket socket = new Socket("localhost", TABLE_HANDLER_PORT); // waiting until transition table field in TableHandler is ready
        Scanner scanner = new Scanner(socket.getInputStream());
        while (scanner.hasNext()){
            if (scanner.nextLine().equals(TABLE_READY_MESSAGE)){
                System.out.println("Table is ready");
                break;
            }
        }
        socket.close();
    }

    public Runner getServer() {
        return server;
    }

    public void setServer(Runner server) {
        this.server = server;
    }

    public Runner getTableHandler() {
        return TableHandler;
    }

    public void setTableHandler(Runner tableHandler) {
        this.TableHandler = tableHandler;
    }

    public Runner[] getClients() {
        return clients;
    }

    public void setClients(Runner[] clients) {
        this.clients = clients;
    }
}
