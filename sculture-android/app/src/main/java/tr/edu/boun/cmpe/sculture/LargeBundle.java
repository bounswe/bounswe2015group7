package tr.edu.boun.cmpe.sculture;


import java.util.ArrayList;

/**
 * A class which helps to send object between activities
 */
public class LargeBundle {
    /**
     * The arraylist which will be store data
     */
    private static ArrayList<Object> data = new ArrayList<>();


    /**
     * Adds an object to the data
     *
     * @param o Object which will be added
     * @return The address of the data
     */
    public static int addItem(Object o) {
        data.add(o);
        return data.size() - 1;
    }

    /**
     * Retrieves and removes an object from data
     *
     * @param i Index of the object
     * @return Retrieved object
     */
    public static Object getItem(int i) {
        Object o = data.get(i);
        data.add(i, null);
        boolean empty = false;
        for (Object obj : data)
            empty |= obj != null;
        if (empty)
            data.clear();
        return o;
    }
}
