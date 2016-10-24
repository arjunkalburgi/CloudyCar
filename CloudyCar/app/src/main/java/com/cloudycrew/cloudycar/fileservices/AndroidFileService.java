package com.cloudycrew.cloudycar.fileservices;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by George on 2016-10-24.
 */

public class AndroidFileService implements IFileService {
    private Context context;

    public AndroidFileService(Context context) {
        this.context = context;
    }

    @Override
    public String loadFileAsString(String filename) {
        FileInputStream inputStream;

        try {
            inputStream = context.openFileInput(filename);
            String out = convertStreamToString(inputStream);
            inputStream.close();

            return out;

        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void saveStringToFile(String filename, String contents) {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(contents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // From: http://stackoverflow.com/a/5445161
    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
