package com.telitel.tiwari.mflix;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.telitel.tiwari.mflix.Database.DatabaseHelper;
import com.telitel.tiwari.mflix.Database.StorageUtil;
import com.telitel.tiwari.mflix.Models.SongModel;
import com.telitel.tiwari.mflix.RecyclerViewAdapters.SongsRecyclerViewAdapter;
import com.telitel.tiwari.mflix.Screens.Fragments.Adapters.FragmentPageAdapter;
import com.telitel.tiwari.mflix.Screens.Fragments.FavouriteFragment;
import com.telitel.tiwari.mflix.Screens.Fragments.PlayListFragment;
import com.telitel.tiwari.mflix.Screens.Fragments.UnImplemented.AboutFragment;
import com.telitel.tiwari.mflix.Screens.Fragments.UnImplemented.EqualizerFragment;
import com.telitel.tiwari.mflix.Screens.Fragments.UnImplemented.HelpFragment;
import com.telitel.tiwari.mflix.Screens.Fragments.UnImplemented.SettingsFragment;
import com.telitel.tiwari.mflix.Screens.MainScreens.FavouritesPage;
import com.telitel.tiwari.mflix.Screens.MainScreens.HomePage;
import com.telitel.tiwari.mflix.Screens.MainScreens.PlayListPage;
import com.telitel.tiwari.mflix.Screens.MainScreens.SearchPage;
import com.telitel.tiwari.mflix.Screens.MainScreens.ToolsPage;
import com.telitel.tiwari.mflix.Util.PlayerService;
import com.telitel.tiwari.mflix.Util.SongsMetaData;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

enum MEDIA_STATUS {RUNNING, PAUSED, STOPPED};

public class MainActivity extends AppCompatActivity implements SideNaviToggle, SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Toast.makeText(this, "Now Playing" + StorageUtil.getInstance(this).loadAudioIndex(), Toast.LENGTH_LONG).show();
    }

    private int STORAGE_PERMISSION_CODE = 1;


    public DrawerLayout menuDrawer;
    private ActionBarDrawerToggle menuToggle;

    ViewPager viewPager;
    BottomNavigationView navigationView;
    BottomSheetBehavior mBottomSheetBehavior;

    //Views
    View playerViewLayout;
    ImageView playPauseButton_collapsed;
    ImageView playPauseButton_expanded;
    ImageView playNextButton_expanded;
    ImageView playPreviousButton_expanded;
    ImageView createPlaylistButton_expanded;
    ImageView favouriteThisButton_expanded;
    ImageView shuffleSongsButton_expanded;
    ImageView repeatSongsButton_expanded;
    TextView startTimeView_expanded;
    TextView endTimeView_expanded;
    SeekBar songSeekBar;


    Context context;

//    public final String Broadcast_PLAY_NEW_AUDIO = " com.telitel.tiwari.mflix.PlayNewAudio";
//    public final String Broadcast_PAUSE_AUDIO = " com.telitel.tiwari.mflix.PauseAudio";
//    public final String Broadcast_RESUME_AUDIO = " com.telitel.tiwari.mflix.ResumeAudio";
//    public final String Broadcast_STOP_AUDIO = " com.telitel.tiwari.mflix.StopAudio";

    private boolean boundedService = false;
    PlayerService playerService;
    MEDIA_STATUS mediaStatus = MEDIA_STATUS.STOPPED;

    Handler myHandler = new Handler();
//    int currentPos = 0;
//    boolean serviceBound;


    public ArrayList<SongModel> songsList;
    public ArrayList<SongModel> songsListFinal;

    DiscreteScrollView mySongsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission ", "onCreate: Permission Granted Already");
        } else {
            requestStoragePermission();
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            setContentView(R.layout.activity_main);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Toolbar toolbar = findViewById(R.id.toolbar);

            playerViewLayout = findViewById(R.id.player_view_layout);
            setSupportActionBar(toolbar);
            _initNav();
            _initBottomSheet();

            context = this;

            _initSongs();
            _initRecyclerView();
            _initViewHome();

//            if (MediaPlayerService.playing) {
//                playPauseButton_collapsed.setImageResource(R.drawable.pause_button);
//                playPauseButton_expanded.setImageResource(R.drawable.pause_button);
//                myHandler.postDelayed(UpdateSongTime, 100);
//                mediaRunning = true;
//            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        StorageUtil.registerPres(context,this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        StorageUtil.unRegisterPres(context,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!boundedService && playerService == null) {
            Intent playerIntent = new Intent(getApplicationContext(), PlayerService.class);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (boundedService) {
            unbindService(serviceConnection);
            boundedService = false;
            if (playerService != null) {
                playerService.unRegisterReceiver();
                playerService.stopSelf();
            }
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            playerService = binder.getService();
            playerService.registerReceiver();
            Toast.makeText(MainActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
            boundedService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(MainActivity.this, "Service UnBounded", Toast.LENGTH_SHORT).show();
            boundedService = false;
        }
    };

    private void _initViewHome() {

        playPauseButton_collapsed = playerViewLayout.findViewById(R.id.playpause_button_player_collapsed);
        playPauseButton_expanded = playerViewLayout.findViewById(R.id.play_pause_button_expanded);
        playNextButton_expanded = playerViewLayout.findViewById(R.id.play_next_song_button);
        playPreviousButton_expanded = playerViewLayout.findViewById(R.id.play_previous_song_button);
        createPlaylistButton_expanded = playerViewLayout.findViewById(R.id.add_to_playlist_Button);
        favouriteThisButton_expanded = playerViewLayout.findViewById(R.id.favourite_this_button);
        startTimeView_expanded = playerViewLayout.findViewById(R.id.start_time_tv);
        endTimeView_expanded = playerViewLayout.findViewById(R.id.end_time_tv);
        shuffleSongsButton_expanded = playerViewLayout.findViewById(R.id.shuffle_songs_button);
        repeatSongsButton_expanded = playerViewLayout.findViewById(R.id.repeat_song_button);
        songSeekBar = playerViewLayout.findViewById(R.id.seekBar_song);

        playPauseAction(playPauseButton_collapsed);
        playPauseAction(playPauseButton_expanded);
        playNextAction(playNextButton_expanded);
        playPreviousAction(playPreviousButton_expanded);

        createPlaylistButton_expanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StorageUtil storageUtil = StorageUtil.getInstance(context);
                SongModel favourite_song = storageUtil.loadAudio().get(storageUtil.loadAudioIndex());
                SongModel s = new SongModel();
                s.setSongId(favourite_song.getSongId());
                s.setSongTitle(favourite_song.getSongTitle());
                s.setSongArtist(favourite_song.getSongArtist());
                s.setSongGener(favourite_song.getSongGener());
                s.setSongAlbumId(favourite_song.getSongAlbumId());
                s.setSongAlbum(favourite_song.getSongAlbum());
                s.setSongPath(favourite_song.getSongPath());
                s.setSongAlbumArtPath(favourite_song.getSongAlbumArtPath());
                s.setSongArtPath(favourite_song.getSongArtPath());
                s.setIsFavourite(favourite_song.getIsFavourite());
                addToPlaylist(s);
            }
        });

        favouriteThisButton_expanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StorageUtil storageUtil = StorageUtil.getInstance(context);
                SongModel favourite_song = storageUtil.loadAudio().get(storageUtil.loadAudioIndex());

                SongModel s = new SongModel();
                s.setSongId(favourite_song.getSongId());
                s.setSongTitle(favourite_song.getSongTitle());
                s.setSongArtist(favourite_song.getSongArtist());
                s.setSongGener(favourite_song.getSongGener());
                s.setSongAlbumId(favourite_song.getSongAlbumId());
                s.setSongAlbum(favourite_song.getSongAlbum());
                s.setSongPath(favourite_song.getSongPath());
                s.setSongAlbumArtPath(favourite_song.getSongAlbumArtPath());
                s.setSongArtPath(favourite_song.getSongArtPath());
                s.setIsFavourite(favourite_song.getIsFavourite());

                DatabaseHelper.getInstance(context).addThisToFavourites(s, DatabaseHelper.getInstance(context).getWritableDatabase());
                if (FavouriteFragment.songAdapter != null) {
                    FavouriteFragment.songsList.add(s);
                    FavouriteFragment.songAdapter.notifyDataSetChanged();
                }
            }
        });

        shuffleSongsButton_expanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.shuffle(songsList);
                StorageUtil storageUtil = StorageUtil.getInstance(context);
                storageUtil.clearCachedAudioPlaylist();
                storageUtil.storeAudio(songsList);
                storageUtil.storeAudioIndex(0);
                SongsRecyclerViewAdapter songAdapter = new SongsRecyclerViewAdapter(context, songsList, 3);
                mySongsRecyclerView.setAdapter(songAdapter);
                playerService.playNewMedia();
            }
        });

        repeatSongsButton_expanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int seek = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seek = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playerService.seekTo(seek);
                songSeekBar.setProgress(seek);
            }
        });
    }


    private void playPauseAction(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaStatus == MEDIA_STATUS.RUNNING) {

//                    Intent broadcastIntent = new Intent(Broadcast_PAUSE_AUDIO);
//                    context.sendBroadcast(broadcastIntent);
                    playerService.pauseMedia();
                    playPauseButton_collapsed.setImageResource(R.drawable.play_button);
                    playPauseButton_expanded.setImageResource(R.drawable.play_button);
                    mediaStatus = MEDIA_STATUS.PAUSED;

                } else if (mediaStatus == MEDIA_STATUS.PAUSED) {

//                    Intent broadcastIntent = new Intent(Broadcast_RESUME_AUDIO);
//                    context.sendBroadcast(broadcastIntent);
                    playerService.resumeMedia();
                    playPauseButton_collapsed.setImageResource(R.drawable.pause_button);
                    playPauseButton_expanded.setImageResource(R.drawable.pause_button);
                    mediaStatus = MEDIA_STATUS.RUNNING;

                } else if (mediaStatus == MEDIA_STATUS.STOPPED) {

//                    Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
//                    context.sendBroadcast(broadcastIntent);
                    playerService.playNewMedia();
                    myHandler.postDelayed(UpdateSongTime, 100);
                    playPauseButton_collapsed.setImageResource(R.drawable.pause_button);
                    playPauseButton_expanded.setImageResource(R.drawable.pause_button);
                    mediaStatus = MEDIA_STATUS.RUNNING;
                }
            }
        });
    }

    void playNextAction(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final StorageUtil storageUtil = StorageUtil.getInstance(context);
                if (storageUtil.loadAudioIndex() + 1 >= storageUtil.loadAudio().size())
                    storageUtil.storeAudioIndex(0);
                else
                    storageUtil.storeAudioIndex(storageUtil.loadAudioIndex() + 1);
                playerService.playNewMedia();
            }
        });
    }

    void playPreviousAction(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final StorageUtil storageUtil = StorageUtil.getInstance(context);
                if (storageUtil.loadAudioIndex() - 1 <= 0)
                    storageUtil.storeAudioIndex(storageUtil.loadAudio().size() - 1);
                else
                    storageUtil.storeAudioIndex(storageUtil.loadAudioIndex() - 1);
                playerService.playNewMedia();
            }
        });
    }


    void playPrevious() {

        final StorageUtil storageUtil = StorageUtil.getInstance(context);
        if (storageUtil.loadAudioIndex() - 1 <= 0)
            storageUtil.storeAudioIndex(storageUtil.loadAudio().size() - 1);
        else
            storageUtil.storeAudioIndex(storageUtil.loadAudioIndex() - 1);

        if (playerService != null && playerService.getPlayerInstance() != null) {
            if (!playerService.getPlayerInstance().isPlaying()) {
                playerService.getPlayerInstance().stop();
                return;
            }
            playerService.playNewMedia();
        }
    }


    void playNext() {

        final StorageUtil storageUtil = StorageUtil.getInstance(context);
        if (storageUtil.loadAudioIndex() + 1 >= storageUtil.loadAudio().size())
            storageUtil.storeAudioIndex(0);
        else
            storageUtil.storeAudioIndex(storageUtil.loadAudioIndex() + 1);
        if (playerService != null && playerService.getPlayerInstance() != null) {
            if (!playerService.getPlayerInstance().isPlaying()) {
                mediaStatus = MEDIA_STATUS.STOPPED;
                return;
            }
            playerService.playNewMedia();
        }
    }

    void playCurrent() {
        if (playerService != null && playerService.getPlayerInstance() != null) {
            if (!playerService.getPlayerInstance().isPlaying()) {
                mediaStatus = MEDIA_STATUS.STOPPED;
                return;
            }
            playerService.playNewMedia();
        }
    }


    private void addToPlaylist(final SongModel songModel) {

        ImageView createNewPlayList;
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.select_playlist_popup, null);

        final RecyclerView myPlaylistsSongRecyclerView;
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        final List<SongModel> listOfPlayLists = new ArrayList<>();


        createNewPlayList = promptView.findViewById(R.id.createPlaylistButtonPopup);
        myPlaylistsSongRecyclerView = promptView.findViewById(R.id.selectPlaylistRecyclerView);

        final SongsRecyclerViewAdapter playListsAdapter = new SongsRecyclerViewAdapter(getApplicationContext(), listOfPlayLists, 4);
        myPlaylistsSongRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myPlaylistsSongRecyclerView.setAdapter(playListsAdapter);


        Cursor songs_cursor2 = DatabaseHelper.getInstance(context).getWritableDatabase().rawQuery("SELECT * FROM _playlists_tb ", new String[]{});
        if (songs_cursor2 != null) {
            if (songs_cursor2.moveToFirst()) {
                do {
                    SongModel song = new SongModel(0L, songs_cursor2.getString(songs_cursor2.getColumnIndex("_playlist_name_")), "", " ", " ", 0L, " ", " ", " ", "", "");
                    listOfPlayLists.add(song);
                } while (songs_cursor2.moveToNext());
            }

            songs_cursor2.close();
        }

        createNewPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlayList(songModel);
                alertD.dismiss();
            }
        });

        playListsAdapter.setOnItemClickListener(new SongsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                DatabaseHelper.getInstance(context).addThisToPlaylist(listOfPlayLists.get(position).getSongTitle(), songModel, DatabaseHelper.getInstance(context).getWritableDatabase());
                alertD.dismiss();
                playListsAdapter.notifyDataSetChanged();
            }
        });

        alertD.setView(promptView);
        alertD.show();

    }

    public void getPlayList(final SongModel songModel) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.create_playlist_name_popup, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        final EditText userInput = promptView.findViewById(R.id.playlistnameView);

        ImageView btnAdd1 = promptView.findViewById(R.id.createPlaylistOk);
        ImageView btnAdd2 = promptView.findViewById(R.id.createPlaylistCancel);

        btnAdd1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean success = DatabaseHelper.getInstance(context).createPlayList(DatabaseHelper.getInstance(context).getWritableDatabase(), userInput.getText().toString());
                if (success)
                    Toast.makeText(context, "Created Playlist", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                DatabaseHelper.getInstance(context).addThisToPlaylist(userInput.getText().toString(), songModel, DatabaseHelper.getInstance(context).getWritableDatabase());
                if (PlayListFragment.songAdapter != null) {
                    SongModel newSt = new SongModel(0L, userInput.getText().toString(), "", " ", " ", 0L, " ", " ", " ", "", "");
                    PlayListFragment.playListsList.add(newSt);
                    PlayListFragment.songAdapter.notifyDataSetChanged();
                }
                alertD.dismiss();
            }
        });

        btnAdd2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertD.dismiss();
            }
        });

        alertD.setView(promptView);
        alertD.show();

    }

    private void setPlayerSongsRecyclerView(List<SongModel> songsList2, int position) {
        SongsRecyclerViewAdapter songAdapter = new SongsRecyclerViewAdapter(context, songsList2, 3);
        mySongsRecyclerView.setAdapter(songAdapter);
        mySongsRecyclerView.scrollToPosition(position);
//        Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
//        context.sendBroadcast(broadcastIntent);
//        songChanged(position);
//        if (!mediaRunning)
//            playerViewLayout.findViewById(R.id.play_pause_button_expanded).callOnClick();
    }

    public void songChanged(int position) {
        StorageUtil storageUtil = StorageUtil.getInstance(context);
        storageUtil.storeAudioIndex(position);
        View v = playerViewLayout.findViewById(R.id.collapsed_player_view);
        ImageView iv = v.findViewById(R.id.song_art_player_collapsed);
        TextView tv = v.findViewById(R.id.song_title_player_collapsed);
        iv.setImageURI(Uri.parse(storageUtil.loadAudio().get(storageUtil.loadAudioIndex()).getSongArtPath()));
        tv.setText(storageUtil.loadAudio().get(storageUtil.loadAudioIndex()).getSongTitle());
        mySongsRecyclerView.scrollToPosition(position);
    }

    private void _initRecyclerView() {
        mySongsRecyclerView = findViewById(R.id.songs_recyclerView_2);
        SongsRecyclerViewAdapter songAdapter = new SongsRecyclerViewAdapter(context, songsListFinal, 3);
        mySongsRecyclerView.setAdapter(songAdapter);

        mySongsRecyclerView.addScrollStateChangeListener(new DiscreteScrollView.ScrollStateChangeListener<RecyclerView.ViewHolder>() {
            int posFrom = 0;
            @Override
            public void onScrollStart(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

            }

            @Override
            public void onScrollEnd(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

                StorageUtil storageUtil = StorageUtil.getInstance(context);
                storageUtil.storeAudioIndex(adapterPosition);
                playCurrent();
            }

            @Override
            public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {
             posFrom = currentPosition;
            }
        });
        final StorageUtil storageUtil = StorageUtil.getInstance(context);
        if (storageUtil.loadAudio() != null) {
            if (storageUtil.loadAudio().size() > 0)
                setPlayerSongsRecyclerView(storageUtil.loadAudio(), storageUtil.loadAudioIndex());

            else
                setPlayerSongsRecyclerView(songsList, 0);
        } else {
            storageUtil.storeAudio(songsList);
            setPlayerSongsRecyclerView(songsList, 0);

        }
        mySongsRecyclerView.setAdapter(songAdapter);
        mySongsRecyclerView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build());
    }

    private void _initSongs() {

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(
                uri, // Uri
                null,
                null,
                null,
                null
        );
        songsList = new ArrayList<>();
        songsListFinal = new ArrayList<>();
        if (cursor == null) {
            Toast.makeText(context, "Something Went Wrong.", Toast.LENGTH_LONG).show();
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(context, "No Music Found on SD Card.", Toast.LENGTH_LONG).show();
        } else if (cursor.getCount() > 0) {

            int id = 0, title = 0, artist = 0, albumId = 0, path = 0, typeId = 0;
            if (cursor.getColumnIndex(MediaStore.Audio.Media._ID) != -1) {
                id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            }
            if (cursor.getColumnIndex(MediaStore.Audio.Media.TITLE) != -1) {
                title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            }
            if (cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST) != -1) {
                artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            }
            if (cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID) != -1) {
                albumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            }
            if (cursor.getColumnIndex((MediaStore.Audio.Media.DATA)) != -1) {
                path = cursor.getColumnIndex((MediaStore.Audio.Media.DATA));
            }
            if (cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE) != -1) {
                typeId = cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE);
            }


            long songId, songAlbumID;
            String songTitle, songArtist, songGenre, songAlbum, songAlbumArtPath, songArtPath, songPath, songType;

            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            databaseHelper.refreshData(db);

            String isFavourite = "false";

            do {
                songId = cursor.getLong(id);
                songTitle = cursor.getString(title);
                songArtist = cursor.getString(artist);
                songGenre = SongsMetaData.getSongGenre(context, songId);
                songAlbumID = cursor.getLong(albumId);
                songAlbum = SongsMetaData.getAlbumName(context, songAlbumID);
                songAlbumArtPath = SongsMetaData.getAlbumArtPath(context, songAlbumID);
                songArtPath = SongsMetaData.getCoverArtPath(context, songAlbumID);
                songPath = cursor.getString(path);
                songType = cursor.getString(typeId);

                if (songType.contains("mpeg") || songType.contains("song") || songType.contains("mp3")) {

                    if (songArtist == null) {
                        songArtist = "Un Known Artist";
                    }

                    if (songGenre == null) {
                        songGenre = "Un Known";
                    }

                    if (songAlbum == null) {
                        songArtist = "Un Known";
                    }

                    if (songAlbumArtPath == null) {
                        songAlbumArtPath = "Un Known";
                    }

                    if (songArtPath == null) {
                        songArtPath = "Un Known";
                    }

                    Log.i("Song ", songTitle);
                    String sql = "SELECT _is_favourite FROM  _songs_tb WHERE _song_id=" + (songId);
                    Cursor cursorSong = db.rawQuery(sql, null);
                    if (cursorSong != null) {
                        if (cursorSong.moveToFirst()) {
                            isFavourite = cursorSong.getString(cursor.getColumnIndex("_is_favourite"));
                            cursorSong.close();
                        }
                    }
                    SongModel song = new SongModel(songId, songTitle, songArtist, songGenre, isFavourite, songAlbumID, songAlbum, songAlbumArtPath, songArtPath, songPath, isFavourite);
                    databaseHelper.insertDataSongs(songId, songTitle, songArtist, songGenre, songAlbumID, songAlbum, songAlbumArtPath, songArtPath, songPath, db, isFavourite);
                    songsList.add(song);
                    songsListFinal.add(song);

                }

            } while (cursor.moveToNext());
            cursor.close();
        }

        final StorageUtil storageUtil = StorageUtil.getInstance(context);
        storageUtil.storeAudio(songsList);
        if (songsList.size() == 0)
            storageUtil.storeAudioIndex(-1);
        else storageUtil.storeAudioIndex(0);

    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to proceed...")
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                MainActivity.this.finish();
                startActivity(i);
            } else {
                MainActivity.this.finish();
            }
        }

    }


    private Runnable UpdateSongTime = new Runnable() {
        public void run() {

            if (playerService != null) {
                MediaPlayer mediaPlayer = playerService.getPlayerInstance();
                if (mediaPlayer != null) {
                    double startTime = mediaPlayer.getCurrentPosition();
                    double finalTime = mediaPlayer.getDuration();
                    songSeekBar.setMax((int) finalTime);
                    songSeekBar.setProgress((int) startTime);
                    startTimeView_expanded.setText(String.format("%s: %s",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
                    );
                    endTimeView_expanded.setText(String.format("%s: %s",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) finalTime)))
                    );
                    myHandler.postDelayed(this, 1000);
                }
            }
        }
    };


    private void _initBottomSheet() {
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setPeekHeight(150);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        playerViewLayout.findViewById(R.id.collapsed_player_view).setAlpha(1f);
                        playerViewLayout.findViewById(R.id.collapsed_player_view).setVisibility(View.VISIBLE);
                        playerViewLayout.findViewById(R.id.playpause_button_player_collapsed).setAlpha(1f);
                        navigationView.setVisibility(View.VISIBLE);
                        playPauseAction(playPauseButton_collapsed);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        playerViewLayout.findViewById(R.id.collapsed_player_view).setAlpha(0f);
                        playerViewLayout.findViewById(R.id.collapsed_player_view).setVisibility(View.GONE);

                        playerViewLayout.findViewById(R.id.playpause_button_player_collapsed).setAlpha(0f);
                        playerViewLayout.findViewById(R.id.collapsed_player_view).setClickable(false);
                        playerViewLayout.findViewById(R.id.playpause_button_player_collapsed).setClickable(false);
                        navigationView.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    @Override
    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        menuDrawer.setDrawerLockMode(lockMode);
        menuToggle.setDrawerIndicatorEnabled(enabled);
    }


    private void _initNav() {

        viewPager = findViewById(R.id.page_container);
        setupFm(getSupportFragmentManager(), viewPager);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        });

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        viewPager.setCurrentItem(0);
                        playerViewLayout.setVisibility(View.VISIBLE);

                        return true;
                    case R.id.nav_playlist:
                        viewPager.setCurrentItem(1);
                        playerViewLayout.setVisibility(View.VISIBLE);

                        return true;
                    case R.id.nav_favourite:
                        viewPager.setCurrentItem(2);
                        playerViewLayout.setVisibility(View.VISIBLE);

                        return true;
                    case R.id.nav_search:
                        viewPager.setCurrentItem(3);
                        playerViewLayout.setVisibility(View.GONE);

                        return true;
                    case R.id.nav_tools:
                        viewPager.setCurrentItem(4);
                        playerViewLayout.setVisibility(View.GONE);

                        return true;

                }
                return false;
            }
        });


        menuDrawer = findViewById(R.id.side_nav_drawer);
        menuToggle = new ActionBarDrawerToggle(this, menuDrawer, R.string.open, R.string.close);
        menuDrawer.addDrawerListener(menuToggle);
        menuToggle.syncState();
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        menuToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.sideMenuToogleIconColor));


        NavigationView sideNavigationView = findViewById(R.id.sideNavView);
        sideNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("RtlHardcoded")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuDrawer.closeDrawer(Gravity.LEFT);

                Toast.makeText(MainActivity.this, "Under development", Toast.LENGTH_LONG).show();
                _collapseBottomSheet();
                switch (menuItem.getItemId()) {

                    case R.id.equlizer:
                        getSupportFragmentManager().beginTransaction().replace(R.id.side_nav_fragment_container, new EqualizerFragment()).addToBackStack(null).commit();
                        _collapseBottomSheet();
                        break;

                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.side_nav_fragment_container, new SettingsFragment()).commit();
                        break;

                    case R.id.info:
                        getSupportFragmentManager().beginTransaction().replace(R.id.side_nav_fragment_container, new AboutFragment()).commit();
                        break;

                    case R.id.help:
                        getSupportFragmentManager().beginTransaction().replace(R.id.side_nav_fragment_container, new HelpFragment()).commit();
                        break;
                }
                return true;
            }
        });
    }

    public static void setupFm(FragmentManager fragmentManager, ViewPager viewPager) {
        FragmentPageAdapter Adapter = new FragmentPageAdapter(fragmentManager);
        Adapter.add(new HomePage(), "Home");
        Adapter.add(new PlayListPage(), "Playlist");
        Adapter.add(new FavouritesPage(), "Favourite");
        Adapter.add(new SearchPage(), "Search");
        Adapter.add(new ToolsPage(), "Tools");

        viewPager.setAdapter(Adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuToggle.onOptionsItemSelected(item);
    }

    private void _collapseBottomSheet() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_nav, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
//        savedInstanceState.putBoolean("ServiceState", serviceBound);
//        StorageUtil storageUtil = new StorageUtil(getApplicationContext());
//        savedInstanceState.putInt("pos", storageUtil.loadAudioIndex());
//        savedInstanceState.putInt("seekpos", storageUtil.loadAudioPosition());
//        // savedInstanceState.putBoolean("mediaRunning", mediaRunning);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        serviceBound = savedInstanceState.getBoolean("ServiceState", false);
//        StorageUtil storageUtil = new StorageUtil(getApplicationContext());
//
//        storageUtil.storeAudioIndex(savedInstanceState.getInt("pos"));
//        currentPos = savedInstanceState.getInt("seekpos");
//        // mediaRunning = savedInstanceState.getBoolean("mediaRunning");
//        if (MediaPlayerService.playing) {
//            mediaRunning = true;
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (serviceBound)
//            unbindService(playerService);
//        Log.i("restarted", "restarted");
    }


    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
            super.onBackPressed();
    }
}