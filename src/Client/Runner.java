package Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import static Client.Constants.*;

public class Runner extends Thread {
    private String[] commands;

    public Runner(String... commands) {
        this.commands = commands;
    }

    @Override
    public void run() {
        Process p;
        try {
            p = Runtime.getRuntime().exec(CMD);
            new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
            new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
            PrintWriter stdin = new PrintWriter(p.getOutputStream());
            for (String command : commands) {
                stdin.println(command);
            }
            stdin.close();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class SyncPipe implements Runnable {
    public SyncPipe(InputStream istrm, OutputStream ostrm) {
        istrm_ = istrm;
        ostrm_ = ostrm;
    }

    public void run() {
        try {
            final byte[] buffer = new byte[1024];
            for (int length = 0; (length = istrm_.read(buffer)) != -1; ) {
                ostrm_.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final OutputStream ostrm_;
    private final InputStream istrm_;
}

