package com.telitel.tiwari.mflix;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements DiscreteScrollView.OnItemChangedListener,
        View.OnClickListener {


    private int STORAGE_PERMISSION_CODE = 1;

    private DrawerLayout menuDrawer;
    private ActionBarDrawerToggle menuToggle;




    ViewPager viewPager;
    BottomNavigationView navigationView;

    public static DiscreteScrollView mySongsRecyclerView;
    public static InfiniteScrollAdapter infiniteAdapter;
    public static songs_recyclerView_adapter songAdapter;

   public static   List<song_template> songsList;

    public static View player_View_layout;


    //Bottom Player
     private BottomSheetBehavior mBottomSheetBehavior;


    //FOR LOADING SONG
   public static   Context context;
    ContentResolver contentResolver;
    Cursor cursor;
    Uri uri;

    //DATABASE VARIABLES
    public static database_helper _songs_database_helper;
    SQLiteDatabase songs_database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //ToolBar Setup
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //Check and request for permission
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Permition Granted Already", Toast.LENGTH_SHORT).show();
        }
        else {
            requestStoragePermition();
        }

        //Proceed to this only if the permission is granted
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {




            //SIDE NAVIGATION DRAWER
            menuDrawer = (DrawerLayout) findViewById(R.id.side_nav_drawer);
            menuToggle = new ActionBarDrawerToggle(this, menuDrawer, R.string.open, R.string.close);
            menuDrawer.addDrawerListener(menuToggle);
            menuToggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            menuToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.sideMenuToogleIconColor));




            navigationView = findViewById(R.id.bottomNavigationView);
            navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



            View bottomSheet = findViewById(R.id.bottom_sheet);;
            mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            mBottomSheetBehavior.setHideable(false);
            mBottomSheetBehavior.setPeekHeight(150);



            viewPager = findViewById(R.id.page_container);
            setupFm(getSupportFragmentManager(), viewPager);

            viewPager.setCurrentItem(0);
            viewPager.setOnPageChangeListener(mOnPageChangeListener);







            //GET ALL SONGS
            context = this;

            contentResolver = context.getContentResolver();

            contentResolver = context.getContentResolver();

            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            cursor = contentResolver.query(
                    uri, // Uri
                    null,
                    null,
                    null,
                    null
            );

            if (cursor == null) {

                Toast.makeText(context, "Something Went Wrong.", Toast.LENGTH_LONG);

            } else if (!cursor.moveToFirst()) {

                Toast.makeText(context, "No Music Found on SD Card.", Toast.LENGTH_LONG);

            }else{




                int id=0;
                int title=0;//= cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int artist=0;// = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
//                int genre=0;//= cursor.getColumnIndex(MediaStore.Audio.Genres.NAME);
//                int album=0;//= cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int albumid=0;// = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                int path=0;//= cursor.getColumnIndex(String.valueOf(MediaStore.Audio.Media.DATA));
                int typeid=0;//= cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE);



                if( cursor.getColumnIndex(MediaStore.Audio.Media._ID)!=-1)
                {
                    id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

                }
                if( cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)!=-1)
                {
                    title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);

                }
                if( cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)!=-1)
                {
                    artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                }
//                if( cursor.getColumnIndex(MediaStore.Audio.Genres.Members.DISPLAY_NAME)!=-1)
//                {
//                    genre = cursor.getColumnIndex(MediaStore.Audio.Genres.Members.DISPLAY_NAME);
//
//                }
//                if( cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)!=-1)
//                {
//                    album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
//
//                }
                if( cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)!=-1)
                {
                    albumid = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

                }
                if( cursor.getColumnIndex(String.valueOf(MediaStore.Audio.Media.DATA))!=-1)
                {
                    path = cursor.getColumnIndex(String.valueOf(MediaStore.Audio.Media.DATA));

                }
                if( cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE)!=-1)
                {
                    typeid = cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE);

                }




                //Getting Song ID From Cursor.
                //int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                //List<String> ListElementsPath
                long songId = 0L;
                String songTitle = "";
                String songArtist = "";
                String songGenre = "";
                String songAlbum = "";
                long songAlbumID = 0L;
                String songAlbumArtPath="";
                String songArtPath = "";
                String songPath = "";
                String songType = "";




                _songs_database_helper= new database_helper(this);
                songs_database = _songs_database_helper.getWritableDatabase();


                _songs_database_helper.refreshData(songs_database);

                songsList = new ArrayList<>();

                Cursor cursorall = null;
                String isFavourite="false";

                do {





                        songId = cursor.getLong(id);
                        songTitle = cursor.getString(title);
                        songArtist = cursor.getString(artist);
                        songGenre = getSongGenre(context,songId);
                        songAlbumID = cursor.getLong(albumid);
                        songAlbum =getAlbumName(context,songAlbumID);
                        songAlbumArtPath= getAlbumArtPath(context,songAlbumID);
                        songArtPath = getCoverArtPath(context, songAlbumID);
                        songPath = cursor.getString(path);


                    songType = cursor.getString(typeid);
                    if (songType.contains("mpeg") || songType.contains("song") || songType.contains("mp3")) {




                        if(songArtist==null)
                        {
                            songArtist="No";
                        }

                        if(songGenre==null)
                        {
                            songGenre="No";
                        }

                        if(songAlbum==null)
                        {
                            songArtist="No";
                        }

                        if(songAlbumArtPath==null)
                        {
                            songAlbumArtPath="No";
                        }

                        if(songArtPath==null)
                        {
                            songArtPath="No";
                        }
                        if(songGenre==null)
                        {
                            songGenre="No";
                        }

                        Log.i("song--- :   ",songTitle);




                        String sql ="SELECT _is_favourite FROM  _songs_tb WHERE _song_id="+(songId);
                        cursorall= songs_database.rawQuery(sql,null);
                        if(cursorall!=null)
                        {
                            if(cursorall.moveToFirst())
                            {
                              isFavourite=cursorall.getString(cursorall.getColumnIndex("_is_favourite"));
                            }
                        }
                        song_template song = new song_template(songId, songTitle, songArtist, songGenre,"false",songAlbumID, songAlbum, songAlbumArtPath,songArtPath, songPath,isFavourite);
                        _songs_database_helper.insertDataSongs(songId, songTitle, songArtist, songGenre,songAlbumID, songAlbum, songAlbumArtPath,songArtPath, songPath,songs_database,isFavourite);

                        songsList.add(song);



                    }
                } while (cursor.moveToNext());
            }

        }




//Discrete Player Recycler View




        mySongsRecyclerView = findViewById(R.id.songs_recyclerView_2);
        mySongsRecyclerView.addOnItemChangedListener(this);
        setPlayerSongsRecyclerView(songsList,0);
        infiniteAdapter = InfiniteScrollAdapter.wrap(songAdapter);
        mySongsRecyclerView.setAdapter(songAdapter);


        mySongsRecyclerView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build());







        player_View_layout = findViewById(R.id.player_view_layout);
        player_View_layout.setVisibility(View.VISIBLE);



        song_template song = new song_template(0L,"",""," "," ",0L," "," ","No","","");
//
//
//        Log.i("Inside----------","this");
//
//
//
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);


//        mySongsRecyclerView.getViewHolder(mySongsRecyclerView.getCurrentItem()).itemView.setAlpha(1f);

//        onItemChanged(songsList.get(0));





        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

                switch (i){

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        player_View_layout.findViewById(R.id.collapsed_player_view).setAlpha(1f);
                        player_View_layout.findViewById(R.id.collapsed_player_view).setVisibility(View.VISIBLE);
                        player_View_layout.findViewById(R.id.playpause_button_player_collapsed).setAlpha(1f);

                        navigationView.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:

                        player_View_layout.findViewById(R.id.collapsed_player_view).setAlpha(0f);
                        player_View_layout.findViewById(R.id.collapsed_player_view).setVisibility(View.GONE);

                        player_View_layout.findViewById(R.id.playpause_button_player_collapsed).setAlpha(0f);
                        player_View_layout.findViewById(R.id.collapsed_player_view).setClickable(false);
                        player_View_layout.findViewById(R.id.playpause_button_player_collapsed).setClickable(false);
                        player_View_layout.getRootView().setBackgroundColor(Color.BLACK);

                        navigationView.setVisibility(View.GONE);

                        break;

                    case BottomSheetBehavior.STATE_HIDDEN:

                        break;

                    case BottomSheetBehavior.STATE_SETTLING:

                        break;


                }

            }



            @Override
            public void onSlide(@NonNull View view, float v) {

                if(v>0.4f)
                player_View_layout.findViewById(R.id.collapsed_player_view).setVisibility(View.VISIBLE);
                player_View_layout.findViewById(R.id.collapsed_player_view).setAlpha(1f-v);
                player_View_layout.findViewById(R.id.playpause_button_player_collapsed).setAlpha(1f-v);
                ObjectAnimator animation = ObjectAnimator.ofFloat(navigationView, "translationY", v*80f);
                animation.setDuration(400);
                animation.start();
            }




        });




    }




//
//    private void onItemChanged(song_template item) {
//
//
//
//    }


//Player view item  click listeners

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

        int positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition);
        Log.i("position",Integer.toString(adapterPosition));
        songChanged(adapterPosition);

    }



    public void songChanged(int position){


        ImageView iv= player_View_layout.findViewById(R.id.collapsed_player_view).findViewById(R.id.song_art_player_collapsed);
        TextView tv= player_View_layout.findViewById(R.id.collapsed_player_view).findViewById(R.id.song_title_player_collapsed);


        iv.setImageURI(Uri.parse(songsList.get(position).getSongAlbumArtPath()));
        tv.setText(songsList.get(position).getSongTitle());
    }




    public static void setPlayerSongsRecyclerView( List<song_template> songsList2,int position){

        songAdapter = new songs_recyclerView_adapter(context , songsList2, 3);
        infiniteAdapter = InfiniteScrollAdapter.wrap(songAdapter);
        mySongsRecyclerView.setAdapter(songAdapter);
        mySongsRecyclerView.scrollToPosition(position);
        songsList=songsList2;

    }






    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        return menuToggle.onOptionsItemSelected(item);
    }






 private String getSongGenre(Context context,long song_id){

     MediaMetadataRetriever mr = new MediaMetadataRetriever();

     Uri trackUri = ContentUris.withAppendedId(
             android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,song_id);

     mr.setDataSource(context, trackUri);

     String songGenre = mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);


     return songGenre;
 }


    private String getAlbumArtPath(Context context, long androidAlbumId) {
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

    private static String getCoverArtPath(Context context, long androidAlbumId) {
        String path = null;
        Cursor c = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID,MediaStore.Audio.Albums.ALBUM_ART},
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

    private static String getAlbumName(Context context, long androidAlbumId) {
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










    public static void setupFm(FragmentManager fragmentManager, ViewPager viewPager){

        fragment_pager_adapter Adapter = new fragment_pager_adapter(fragmentManager);

        Adapter.add(new homePage(),"Home");
        Adapter.add(new playlistPage(),"Playlist");
        Adapter.add(new favouritePage(),"Favourite");
        Adapter.add(new searchPage(),"Search");
        Adapter.add(new toolsPage(),"Tools");


        viewPager.setAdapter(Adapter);

    }








    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            switch (i) {
                case 0:
                    navigationView.setSelectedItemId(R.id.nav_home);
                    break;
                case 1:
                    navigationView.setSelectedItemId(R.id.nav_playlist);
                    break;
                case 2:
                    navigationView.setSelectedItemId(R.id.nav_favourite);
                    break;
                case 3:
                    navigationView.setSelectedItemId(R.id.nav_search);
                    break;
                case 4:
                    navigationView.setSelectedItemId(R.id.nav_tools);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener(){

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId())
            {
                case R.id.nav_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.nav_playlist:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.nav_favourite:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.nav_search:
                    viewPager.setCurrentItem(3);
                    return true;
                case R.id.nav_tools:
                    viewPager.setCurrentItem(4);
                    return true;

            }


            return false;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_nav, menu);
        return true;


    }

    @Override
    public void onBackPressed() {

        if(mBottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
            super.onBackPressed();

    }

    // Request permission
    private void requestStoragePermition() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permition is needed to proceed...")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            MainActivity.this.finish();
                            System.exit(0);
                        }
                    }).create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }


    //Check permission granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT);

                Intent i = new Intent(this,MainActivity.class);
                MainActivity.this.finish();
                startActivity(i);


                } else {
                MainActivity.this.finish();
            }
        }

    }



}