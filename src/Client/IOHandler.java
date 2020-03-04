package Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static Client.Constants.*;
import static Client.UsefulMethods.*;

public class IOHandler {
    private static int numberOfFinishedClients = 0;

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(IO_HANDLER_PORT);
        System.out.println("IO Started");
        while (numberOfFinishedClients < 4) {
            Socket handler = ss.accept();
            Scanner inputStream = new Scanner(handler.getInputStream());
            OutputStream outputStream = handler.getOutputStream();
            try {
                while (true) {
                    try {
                        if (inputStream.hasNext()) {
                            String query = inputStream.nextLine();
                            System.out.println(query.substring(1));
                            if (query.getBytes()[0] == IO_MESSAGE_VIEWER) {
                                continue;
                            }
                            if (query.getBytes()[0] == IO_CLIENT_DONE) {
                                numberOfFinishedClients++;
                                handler.close();
                                break;
                            }
                            if (query.getBytes()[0] == IO_CLIENT_CLOSE) {
                                handler.close();
                                break;
                            }
                            if (query.getBytes()[0] == IO_READ_ORDER) {
                                readFromFileAndSend(outputStream);
                                continue;
                            }
                            if (query.getBytes()[0] == IO_WRITE_ORDER) {
                                receiveAndWriteInFile(inputStream);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("IO Ended");
        ss.close();

    }

    public static void readFromFileAndSend(OutputStream outputStream) throws IOException {
        File file = new File(TRANSITION_TABLE_FILE_PATH);
        Scanner fileReader = new Scanner(new FileReader(file));
        while (fileReader.hasNext()) {
            sendDataToSomewhere(outputStream,fileReader.nextLine());
        }
        sendDataToSomewhere(outputStream,IO_EOF);
        fileReader.close();
    }

    public static void receiveAndWriteInFile(Scanner inputStream) throws IOException {
        File file = new File(TRANSITION_TABLE_FILE_PATH);
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
        String in = "";
        while (true) {
            in = inputStream.nextLine();
            if (in.equals(IO_EOF)) {
                break;
            } else {
                fileWriter.write(in + "\n");
            }
        }
        fileWriter.close();
    }
}
