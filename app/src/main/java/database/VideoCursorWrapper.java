package database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.youtubedataapp.video.VideoItem;

import database.VideoDbSchema.VideoEntry;

public class VideoCursorWrapper extends CursorWrapper {

    public VideoCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public VideoItem getVideo() {
        String title = getString(getColumnIndex(VideoEntry.COLUMN_TITLE));
        String link = getString(getColumnIndex(VideoEntry.COLUMN_LINK));
        int likesCount = getInt(getColumnIndex(VideoEntry.COLUMN_LIKES));
        int goal = getInt(getColumnIndex(VideoEntry.COLUMN_GOAL));
        long id = getLong(getColumnIndex(VideoEntry._ID));
        String thumbnailUrl = getString(getColumnIndex(VideoEntry.COLUMN_THUMBNAIL));

        VideoItem videoItem = new VideoItem(link);
        videoItem.setTitle(title);
        videoItem.setLikesCount(likesCount);
        videoItem.setGoal(goal);
        videoItem.setThumbnailUrl(thumbnailUrl);
        videoItem.setId(id);
        return videoItem;
    }
}
