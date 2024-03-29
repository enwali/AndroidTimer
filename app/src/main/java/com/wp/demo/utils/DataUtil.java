package com.wp.demo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class DataUtil {
    public static <T extends Serializable> ArrayList<T> deepCopy(ArrayList<T> list) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(list);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            ArrayList<T> copiedList = (ArrayList<T>) in.readObject();

            return copiedList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
