package com.cloudycrew.cloudycar.fileservices;

/**
 * Created by George on 2016-10-24.
 */
public interface IFileService {
    /**
     * Loads a file from a filename as a String
     *
     * @param filename the filename
     * @return the string
     */
    String loadFileAsString(String filename);

    /**
     * Save string to file.
     *
     * @param filename the filename
     * @param contents the contents
     */
    void saveStringToFile(String filename, String contents);
}
