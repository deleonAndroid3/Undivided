package com.training.android.undivided.GroupSender.DB;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class GroupContactDbSchema {
    public static final String DATABASE_NAME = "contact.db";
    public static final int VERSION = 1;

    public static final class ContactTable {
        public static final String NAME = "contact";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String TIME = "time";
            public static final String SMS = "sms";
            public static final String PHONE_NUMBERS = "phone_numbers";
        }
    }

}
