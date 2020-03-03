package Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static Client.Constants.*;

public class IOHandler {
    private static BufferedReader inputStream;
    private static BufferedWriter outputStream;
    private static Socket handler;

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(IO_HANDLER_PORT);
        for (int i = 0; i < 8; i++) {
            handler = ss.accept();
            inputStream = new BufferedReader(new InputStreamReader(handler.getInputStream()));
            outputStream = new BufferedWriter(new OutputStreamWriter(handler.getOutputStream()));
            if (inputStream.read() ==  Byte.parseByte(IO_READ_ORDER)) {
                readFromFileAndSend();
            } else {
                receiveAndWriteInFile();
            }
            handler.close();
        }
        ss.close();
    }

    public static void readFromFileAndSend() throws IOException {
        File file = new File(TRANSITION_TABLE_FILE_PATH);
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        String in = "";
        while ((in = fileReader.readLine()) != null) {
            outputStream.write(in);
            outputStream.flush();
        }
        outputStream.write("\n"+IO_EOF);
        outputStream.flush();
        fileReader.close();
    }

    public static void receiveAndWriteInFile() throws IOException {
        File file = new File(TRANSITION_TABLE_FILE_PATH);
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
        String in = "";
        while (true) {
            in = inputStream.readLine();
            if (in.equals(IO_EOF)) {
                break;
            } else {
                fileWriter.write(in);
            }
        }
        fileWriter.close();
    }
}
