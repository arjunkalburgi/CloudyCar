package com.cloudycrew.cloudycar.fileservices;

/**
 * Created by George on 2016-10-24.
 */

public interface IFileService {
    String loadFileAsString(String filename);
    void saveStringToFile(String filename, String contents);
}
