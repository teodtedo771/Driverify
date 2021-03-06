package com.demotxt.droidsrce.homedashboard.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CSVWriter {
    private static final String FILE_FORMAT = ".csv";
    private static String PATH;
    private String path;
    private BufferedWriter writer;
    public boolean isOpened;

    public String getPath() {
        return path;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public CSVWriter(String PATH) throws IOException {
        this.isOpened = true;
        this.PATH = PATH;
        File lvFile = new File(PATH);
        File lvLogFile = new File(PATH + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()) + FILE_FORMAT);
        if (!lvFile.exists()) {
            lvFile.mkdirs();
        }
        if (!lvLogFile.exists()) {
            lvLogFile.createNewFile();
        }

        this.path = lvLogFile.getAbsolutePath();

        this.writer = new BufferedWriter(new FileWriter(this.path));
    }

    /**
     * Function for string format
     *
     * @param string with space as separator
     * @return return string in CSV format
     */
    public static String formatCSV(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : string.split(" ")) {
            stringBuilder.append(item);
            stringBuilder.append(",");
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());

        return stringBuilder.toString();
    }

    public boolean append(String string) throws IOException {
        writer.append(formatCSV(string));
        this.newLine();
        return true;
    }

    public void newLine() throws IOException {
        writer.newLine();
    }

    public void close() throws IOException {
        writer.close();
        isOpened = false;
    }

    public void flush() throws IOException {
        writer.flush();
    }
}
