package org.example.configs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AppConfig {
    public static final String DATA_STORAGE_DIR = "data_store";

    public static final String DATE_FORMAT = "dd/MM/yyyy";

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

}
