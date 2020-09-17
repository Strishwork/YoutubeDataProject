package database;

import android.provider.BaseColumns;

public class VideoDbSchema {

    public VideoDbSchema(){}

    public static final class VideoEntry implements BaseColumns {

        public static final String TABLE_NAME = "videosList";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_GOAL = "goal";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }

}
