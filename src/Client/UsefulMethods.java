package Client;

import java.util.ArrayList;

public interface UsefulMethods {
    static <E> boolean compareTwoArrayList(ArrayList<E> array1, ArrayList<E> array2){
        ArrayList<E> temp1 = new ArrayList<E>(array1);
        ArrayList<E> temp2 = new ArrayList<E>(array2);
        for (int i = 0; i < temp1.size(); i++) {
            for (int j = 0; j < temp2.size(); j++) {
                if (!temp1.get(i).equals(temp2.get(j))) {
                    return false;
                }else {
                    temp1.remove(i);
                    temp2.remove(j);
                    i--;
                    break;
                }
            }
        }
        return true;
    }
}
