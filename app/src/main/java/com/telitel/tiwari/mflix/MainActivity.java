package com.telitel.tiwari.mflix;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private int STORAGE_PERMISSION_CODE = 1;

    private DrawerLayout menuDrawer;
    private ActionBarDrawerToggle menuToggle;


    ViewPager viewPager;
    BottomNavigationView navigationView;


    //FOR LOADING SONG
    Context context;
    ContentResolver contentResolver;
    Cursor cursor;
    Uri uri;




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




            navigationView = findViewById(R.id.bottomNavigationView);
            navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


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
                int genre=0;//= cursor.getColumnIndex(MediaStore.Audio.Genres.NAME);
                int album=0;//= cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int art=0;// = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
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
                if( cursor.getColumnIndex(MediaStore.Audio.Genres.NAME)!=-1)
                {
                    genre = cursor.getColumnIndex(MediaStore.Audio.Genres.NAME);

                }
                if( cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)!=-1)
                {
                    album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);

                }
                if( cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)!=-1)
                {
                    art = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

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
                long songId = 0;
                String songTitle = "";
                String songArtist = "";
                String songGenre = "";
                String songAlbum = "";
                long songArt = 0L;
                String songAlbumArtPath="";
                String songArtPath = "";
                String songPath = "";
                String songType = "";


                int songArtPathId=0;
                int songPathId=0;








                boolean clear=true;


                do {


                    songType = cursor.getString(typeid);
                    if (songType.contains("mpeg") || songType.contains("song") || songType.contains("mp3")) {

                        songId = cursor.getLong(id);
                        songTitle = cursor.getString(title);
                        songArtist = cursor.getString(artist);
                        songGenre = cursor.getString(genre);
                        songArt = cursor.getLong(art);
//                        songAlbum =getAlbumName(context,songArt);
//                        songAlbumArtPath= getAlbumArtPath(context,songArt);
//                        songArtPath = getCoverArtPath(context, songArt);
                        songPath = cursor.getString(path);

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


                        Log.i("song :   ",songTitle);


                    }
                } while (cursor.moveToNext());
            }

        }
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

        if(menuDrawer.isDrawerOpen(GravityCompat.START))
             menuDrawer.closeDrawer(GravityCompat.START);
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