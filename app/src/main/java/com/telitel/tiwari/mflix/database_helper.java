package com.telitel.tiwari.mflix;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class database_helper  extends SQLiteOpenHelper {


    private static final String  dbname="_songs_db";
    private static final String  _songs_tb="_songs_tb";
    private static final String  _playlists_tb="_playlists_tb";

    private static final int version=1;

    public database_helper(Context context) {
        super(context,dbname,null,version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {



        String sql="CREATE TABLE IF NOT EXISTS _songs_tb(_id INTEGER PRIMARY KEY AUTOINCREMENT, _song_id VARCHAR, _song_title VARCHAR, _song_artist VARCHAR,_song_genre VARCHAR, _song_album_id VARCHAR, _song_album VARCHAR, _song_album_art_path VARCHAR,_song_art_path VARCHAR, _song_path VARCHAR,_is_favourite VARCHAR)";
        db.execSQL(sql);

        sql="CREATE TABLE IF NOT EXISTS _playlists_tb(_id INTEGER PRIMARY KEY AUTOINCREMENT, _playlist_name_  VARCHAR)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS _favourite_list_00100_ (_id INTEGER PRIMARY KEY AUTOINCREMENT, _song_id VARCHAR, _song_title VARCHAR, _song_artist VARCHAR,_song_genre VARCHAR, _song_album_id VARCHAR, _song_album VARCHAR, _song_album_art_path VARCHAR,_song_art_path VARCHAR, _song_path VARCHAR,_is_favourite VARCHAR)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public static void refreshData(SQLiteDatabase database){

        database.execSQL("delete from "+ "_songs_tb");

    }


    public static boolean createPlayList(SQLiteDatabase db,String playlistName){

        if(playlistName.equals(""))
        {
            return  false;
        }


        String sql2 = "SELECT * FROM _playlists_tb WHERE _playlist_name_= " +"'"+ playlistName+"'";
        Cursor cursor = db.rawQuery(sql2, null);


        if(cursor.moveToFirst()) {

            //Playlist Already Exists With Same Name
            return false;
        }
        else {

            //Create New Playlist
            String sql = "CREATE TABLE IF NOT EXISTS '" + playlistName + "'(_id INTEGER PRIMARY KEY AUTOINCREMENT, _song_id VARCHAR, _song_title VARCHAR, _song_artist VARCHAR,_song_genre VARCHAR, _song_album_id VARCHAR, _song_album VARCHAR, _song_album_art_path VARCHAR,_song_art_path VARCHAR, _song_path VARCHAR,_is_favourite VARCHAR)";
            db.execSQL(sql);

            ContentValues Playlist_name=new ContentValues();
            Playlist_name.put("_playlist_name_ ",playlistName);
            db.insert(_playlists_tb,null,Playlist_name);

        }


        return true;
    }




    public void insertDataSongs(Long song_id,String song_title,String song_artist,String song_genre,Long song_album_id, String song_album,String song_album_art_path,String song_art_path,String song_path,SQLiteDatabase database,String is_favourite)
    {
        ContentValues Song_details=new ContentValues();


        Song_details.put("_song_id",song_id);
        Song_details.put("_song_title",song_title);
        Song_details.put("_song_artist",song_artist);
        Song_details.put("_song_genre",song_genre);
        Song_details.put("_song_album_id",song_album_id);
        Song_details.put("_song_album",song_album);
        Song_details.put("_song_album_art_path",song_album_art_path);
        Song_details.put("_song_art_path",song_art_path);
        Song_details.put("_song_path",song_path);
        Song_details.put("_is_favourite",is_favourite);

        database.insert(_songs_tb,null,Song_details);

    }


    public void updateDataSongs(Long song_id,String song_title,String song_artist,String song_genre,Long song_album_id, String song_album,String song_album_art_path,String song_art_path,String song_path,SQLiteDatabase database,String is_favourite)
    {
        ContentValues Song_details=new ContentValues();

        Song_details.put("_song_id",song_id);
        Song_details.put("_song_title",song_title);
        Song_details.put("_song_artist",song_artist);
        Song_details.put("_song_genre",song_genre);
        Song_details.put("_song_album_id",song_album_id);
        Song_details.put("_song_album",song_album);
        Song_details.put("_song_album_art_path",song_album_art_path);
        Song_details.put("_song_art_path",song_art_path);
        Song_details.put("_song_path",song_path);
        Song_details.put("_is_favourite",is_favourite);


        database.update(_songs_tb, Song_details, "_song_id="+song_id, null);


    }


    public static void addThisToPlaylist(String playlistName,song_template s,SQLiteDatabase database){

        ContentValues Song_details=new ContentValues();


        Song_details.put("_song_id",s.getSongId());
        Song_details.put("_song_title",s.getSongTitle());
        Song_details.put("_song_artist",s.getSongArtist());
        Song_details.put("_song_genre",s.getSongGener());
        Song_details.put("_song_album_id",s.getSongAlbumId());
        Song_details.put("_song_album",s.getSongAlbum());
        Song_details.put("_song_album_art_path",s.getSongAlbumArtPath());
        Song_details.put("_song_art_path",s.getSongArtPath());
        Song_details.put("_song_path",s.getSongPath());
        Song_details.put("_is_favourite",s.getIsFavourite());

        playlistName="'"+playlistName+"'";

        database.insert(playlistName,null,Song_details);

    }

    public static void addThisToFavourites(song_template s,SQLiteDatabase db){

        ContentValues Song_details=new ContentValues();


        Song_details.put("_song_id",s.getSongId());
        Song_details.put("_song_title",s.getSongTitle());
        Song_details.put("_song_artist",s.getSongArtist());
        Song_details.put("_song_genre",s.getSongGener());
        Song_details.put("_song_album_id",s.getSongAlbumId());
        Song_details.put("_song_album",s.getSongAlbum());
        Song_details.put("_song_album_art_path",s.getSongAlbumArtPath());
        Song_details.put("_song_art_path",s.getSongArtPath());
        Song_details.put("_song_path",s.getSongPath());
        Song_details.put("_is_favourite",s.getIsFavourite());


        String sql = "CREATE TABLE IF NOT EXISTS _favourite_list_00100_ (_id INTEGER PRIMARY KEY AUTOINCREMENT, _song_id VARCHAR, _song_title VARCHAR, _song_artist VARCHAR,_song_genre VARCHAR, _song_album_id VARCHAR, _song_album VARCHAR, _song_album_art_path VARCHAR,_song_art_path VARCHAR, _song_path VARCHAR,_is_favourite VARCHAR)";
        db.execSQL(sql);

        db.insert("_favourite_list_00100_",null,Song_details);

    }


    public static boolean deleteThisPlaylist(String playlistTableName,SQLiteDatabase database){


        database.execSQL("DROP TABLE IF EXISTS '" + playlistTableName + "'");
        return  database.delete(_playlists_tb,  "_playlist_name_="+"'"+playlistTableName+"'" ,null)> 0;


    }




}
