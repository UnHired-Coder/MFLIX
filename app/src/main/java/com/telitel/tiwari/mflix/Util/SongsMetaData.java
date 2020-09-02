package com.telitel.tiwari.mflix.Util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

abstract public class SongsMetaData {

    public static String getSongGenre(Context context, long song_id) {

        try {
            MediaMetadataRetriever mr = new MediaMetadataRetriever();
            Uri trackUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song_id);
            mr.setDataSource(context, trackUri);
            if (mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE) != null)
                return mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return "Un Known";
    }
//    public static String getAlbumArtPath(Context context, long androidAlbumId) {
//        String path = "";
//        Cursor c = context.getContentResolver().query(
//                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
//                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
//                MediaStore.Audio.Albums._ID + "=?",
//                new String[]{Long.toString(androidAlbumId)},
//                null);
//        if (c != null) {
//            if (c.moveToFirst()) {
//                path = c.getString(0);
//            }
//            c.close();
//        }
//        Log.i("Path Art ", "getAlbumArtPath: " + path);
//        return path;
//    }

    public static String getAlbumArtPath(Context context, long androidAlbumId) {
        String path = "";
        MediaMetadataRetriever mr = new MediaMetadataRetriever();
        Uri uri;
        try {
            final Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");

            uri = ContentUris.withAppendedId(sArtworkUri,
                    androidAlbumId);
            if (ContentUris.withAppendedId(sArtworkUri,
                    androidAlbumId) == null)
                uri = Uri.parse("Un Known");
        } catch (Exception e) {
            uri = Uri.parse("Un Known");
        }
        Log.i("Path Art ", "getAlbumArtPath: 1" + uri);
        return uri.toString();
    }

    //GET COVER ART/THUMBNAIL

    public static String getCoverArtPath(Context context, long androidAlbumId) {
        String path = "";
        MediaMetadataRetriever mr = new MediaMetadataRetriever();
        Uri uri;
        try {
            final Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");

            uri = ContentUris.withAppendedId(sArtworkUri,
                    androidAlbumId);
            if (ContentUris.withAppendedId(sArtworkUri,
                    androidAlbumId) == null)
                uri = Uri.parse("Un Known");
        } catch (Exception e) {
//            uri = "";
            uri = Uri.parse("Un Known");
        }
        Log.i("Path Art ", "getAlbumArtPath: 2" + uri);
        return uri.toString();
    }


    //GET ALBUM NAME IT BELONGS TO IF ANY

    public static String getAlbumName(Context context, long androidAlbumId) {
        String path = null;
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return "Un Known";
        }
    }


}
