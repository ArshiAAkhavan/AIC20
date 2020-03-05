package Client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public interface UsefulMethods {
    static <E> boolean compareTwoArrayList(ArrayList<E> array1, ArrayList<E> array2) {
        if(array1.size()!=array2.size()){
            return false;
        }
        ArrayList<E> temp1 = new ArrayList<E>(array1);
        ArrayList<E> temp2 = new ArrayList<E>(array2);
        for (int i = 0; i < temp1.size(); i++) {
            for (int j = 0; j < temp2.size(); j++) {
                if (!temp1.get(i).equals(temp2.get(j))) {
                    return false;
                } else {
                    temp1.remove(i);
                    temp2.remove(j);
                    i--;
                    break;
                }
            }
        }
        return true;
    }

    static void sendMessageToSomewhere(OutputStream server, Byte order, String message) {
        try {
            server.write((new String(new byte[]{order}) + message + "\n").getBytes());
            server.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void sendDataToSomewhere(OutputStream server, String message) {
        try {
            server.write((message + "\n").getBytes());
            server.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
