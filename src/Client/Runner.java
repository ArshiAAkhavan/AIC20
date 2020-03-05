package Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import static Client.Constants.*;

public class Runner extends Thread {
    private String[] commands;
    private String console;
    private boolean showOutput;

    public Runner(String console, boolean showOutPut, String... commands) {
        this.console = console;
        this.commands = commands;
        this.showOutput = showOutPut;
    }

    @Override
    public void run() {
        Process p;
        try {
            p = Runtime.getRuntime().exec(console);
            new Thread(new SyncPipe(p.getErrorStream(), System.err, showOutput)).start();
            new Thread(new SyncPipe(p.getInputStream(), System.out, showOutput)).start();
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
    private final OutputStream ostrm_;
    private final InputStream istrm_;
    private boolean showOutput;

    public SyncPipe(InputStream istrm, OutputStream ostrm, boolean showOutput) {
        istrm_ = istrm;
        ostrm_ = ostrm;
        this.showOutput = showOutput;
    }

    public void run() {
        try {
            final byte[] buffer = new byte[1024];
            for (int length = 0; (length = istrm_.read(buffer)) != -1; ) {
                if (showOutput) {
                    ostrm_.write(buffer, 0, length);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

