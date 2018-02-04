package com.training.android.undivided.GroupSender.Utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class CloseUtil {

    public static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
