package com.telitel.tiwari.mflix.Util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

abstract public class SongsMetaData {

    public static String getSongGenre(Context context, long song_id) {

        MediaMetadataRetriever mr = new MediaMetadataRetriever();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song_id);
        mr.setDataSource(context, trackUri);

        return mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
    }


    public static String getAlbumArtPath(Context context, long androidAlbumId) {
        String path = null;
        Cursor c = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{Long.toString(androidAlbumId)},
                null);
        if (c != null) {
            if (c.moveToFirst()) {
                path = c.getString(0);
            }
            c.close();
        }
        return path;
    }

    //GET COVER ART/THUMBNAIL

    public static String getCoverArtPath(Context context, long androidAlbumId) {
        String path = null;
        Cursor c = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{Long.toString(androidAlbumId)},
                null);
        if (c != null) {
            if (c.moveToFirst()) {
                path = c.getString(c.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                //  Log.i("artPath",path);
            }
            c.close();
        }
        return path;
    }


    //GET ALBUM NAME IT BELONGS TO IF ANY

    public static String getAlbumName(Context context, long androidAlbumId) {
        String path = null;
        Cursor c = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{Long.toString(androidAlbumId)},
                null);
        if (c != null) {
            if (c.moveToFirst()) {
                path = c.getString(0);
            }
            c.close();
        }
        return path;
    }


}
