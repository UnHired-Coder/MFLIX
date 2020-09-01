//package com.telitel.tiwari.mflix;
//
//import android.app.AlertDialog;
//import android.content.ComponentName;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Handler;
//import android.os.IBinder;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.BottomSheetBehavior;
//import android.support.design.widget.BottomSheetDialog;
//import android.support.design.widget.BottomSheetDialogFragment;
//import android.os.Bundle;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.telitel.tiwari.mflix.Database.DatabaseHelper;
//import com.telitel.tiwari.mflix.Database.StorageUtil;
//import com.telitel.tiwari.mflix.Models.SongModel;
//import com.telitel.tiwari.mflix.RecyclerViewAdapters.SongsRecyclerViewAdapter;
//import com.telitel.tiwari.mflix.Screens.Fragments.FavouriteFragment;
//import com.telitel.tiwari.mflix.Screens.Fragments.PlayListFragment;
//import com.telitel.tiwari.mflix.Util.PlayerService;
//import com.telitel.tiwari.mflix.Util.SongsMetaData;
//import com.yarolegovich.discretescrollview.DiscreteScrollView;
//import com.yarolegovich.discretescrollview.transform.Pivot;
//import com.yarolegovich.discretescrollview.transform.ScaleTransformer;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//public class PlayerView extends BottomSheetDialogFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
//
//
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
//        Toast.makeText(getContext(), "Now Playing PlayerView" + StorageUtil.getInstance(getContext()).loadAudioIndex(), Toast.LENGTH_LONG).show();
//    }
//
//    ImageView collapsedImageView;
//    TextView collapsedTextView;
//    View collapsedPlayerView;
//
//    ImageView playPauseButton_collapsed;
//    ImageView playPauseButton_expanded;
//    ImageView playNextButton_expanded;
//    ImageView playPreviousButton_expanded;
//    ImageView createPlaylistButton_expanded;
//    ImageView favouriteThisButton_expanded;
//    ImageView shuffleSongsButton_expanded;
//    ImageView repeatSongsButton_expanded;
//    TextView startTimeView_expanded;
//    TextView endTimeView_expanded;
//    SeekBar songSeekBar;
//
//
//    public ArrayList<SongModel> songsList;
//    public ArrayList<SongModel> songsListFinal;
//
//    private boolean boundedService = false;
//    PlayerService playerService;
//    MEDIA_STATUS mediaStatus = MEDIA_STATUS.STOPPED;
//
//    Handler myHandler = new Handler();
//    DiscreteScrollView mySongsRecyclerView;
//    View bottomSheet;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.activity_player_view, container, false);
//        Toast.makeText(getContext(), "PlayerView" + StorageUtil.getInstance(getContext()).loadAudioIndex(), Toast.LENGTH_LONG).show();
//        collapsedPlayerView = v.findViewById(R.id.collapsed_player_view);
//        playPauseButton_collapsed = v.findViewById(R.id.playpause_button_player_collapsed);
//        collapsedImageView = v.findViewById(R.id.song_art_player_collapsed);
//        collapsedTextView = v.findViewById(R.id.song_title_player_collapsed);
//
//        playPauseButton_expanded = v.findViewById(R.id.play_pause_button_expanded);
//        playNextButton_expanded = v.findViewById(R.id.play_next_song_button);
//        playPreviousButton_expanded = v.findViewById(R.id.play_previous_song_button);
//        createPlaylistButton_expanded = v.findViewById(R.id.add_to_playlist_Button);
//        favouriteThisButton_expanded = v.findViewById(R.id.favourite_this_button);
//        startTimeView_expanded = v.findViewById(R.id.start_time_tv);
//        endTimeView_expanded = v.findViewById(R.id.end_time_tv);
//        shuffleSongsButton_expanded = v.findViewById(R.id.shuffle_songs_button);
//        repeatSongsButton_expanded = v.findViewById(R.id.repeat_song_button);
//        songSeekBar = v.findViewById(R.id.seekBar_song);
//
//
//        mySongsRecyclerView = v.findViewById(R.id.songs_recyclerView_2);
////        bottomSheet = (View) getActivity().findViewById(R.id.bottom_sheet);
////        Log.i("Bottom Sheet", "onCreateView: " + bottomSheet.getId());
//
//
//        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
//                FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
//                CoordinatorLayout layout = (CoordinatorLayout) bottomSheet.getParent();
//                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
//                behavior.setHideable(false);
//                behavior.setPeekHeight(450);
//                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//                    @Override
//                    public void onStateChanged(@NonNull View view, int i) {
//                        switch (i) {
//
//                            case BottomSheetBehavior.STATE_COLLAPSED:
//                                collapsedImageView.setAlpha(1f);
//                                collapsedImageView.setVisibility(View.VISIBLE);
//                                playPauseButton_collapsed.setAlpha(1f);
//                                playPauseAction(playPauseButton_collapsed);
//                                break;
//                            case BottomSheetBehavior.STATE_EXPANDED:
//                                collapsedPlayerView.setAlpha(0f);
//                                collapsedPlayerView.setVisibility(View.GONE);
//
//                                playPauseButton_collapsed.setAlpha(0f);
//                                collapsedPlayerView.setClickable(false);
//                                playPauseButton_collapsed.setClickable(false);
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onSlide(@NonNull View view, float v) {
//                        Log.i("TAG", "onSlide: ");
//                    }
//                });
//             }
//        });
//
//
//        _initSongs();
//        _initRecyclerView();
//
//        playPauseAction(playPauseButton_collapsed);
//        playPauseAction(playPauseButton_expanded);
//        playNextAction(playNextButton_expanded);
//        playPreviousAction(playPreviousButton_expanded);
//
//        createPlaylistButton_expanded.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final StorageUtil storageUtil = StorageUtil.getInstance(getContext());
//                SongModel favourite_song = storageUtil.loadAudio().get(storageUtil.loadAudioIndex());
//                SongModel s = new SongModel();
//                s.setSongId(favourite_song.getSongId());
//                s.setSongTitle(favourite_song.getSongTitle());
//                s.setSongArtist(favourite_song.getSongArtist());
//                s.setSongGener(favourite_song.getSongGener());
//                s.setSongAlbumId(favourite_song.getSongAlbumId());
//                s.setSongAlbum(favourite_song.getSongAlbum());
//                s.setSongPath(favourite_song.getSongPath());
//                s.setSongAlbumArtPath(favourite_song.getSongAlbumArtPath());
//                s.setSongArtPath(favourite_song.getSongArtPath());
//                s.setIsFavourite(favourite_song.getIsFavourite());
//                addToPlaylist(s);
//            }
//        });
//
//        favouriteThisButton_expanded.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final StorageUtil storageUtil = StorageUtil.getInstance(getContext());
//                SongModel favourite_song = storageUtil.loadAudio().get(storageUtil.loadAudioIndex());
//
//                SongModel s = new SongModel();
//                s.setSongId(favourite_song.getSongId());
//                s.setSongTitle(favourite_song.getSongTitle());
//                s.setSongArtist(favourite_song.getSongArtist());
//                s.setSongGener(favourite_song.getSongGener());
//                s.setSongAlbumId(favourite_song.getSongAlbumId());
//                s.setSongAlbum(favourite_song.getSongAlbum());
//                s.setSongPath(favourite_song.getSongPath());
//                s.setSongAlbumArtPath(favourite_song.getSongAlbumArtPath());
//                s.setSongArtPath(favourite_song.getSongArtPath());
//                s.setIsFavourite(favourite_song.getIsFavourite());
//
//                DatabaseHelper.getInstance(getContext()).addThisToFavourites(s, DatabaseHelper.getInstance(getContext()).getWritableDatabase());
//                if (FavouriteFragment.songAdapter != null) {
//                    FavouriteFragment.songsList.add(s);
//                    FavouriteFragment.songAdapter.notifyDataSetChanged();
//                }
//            }
//        });
//
//        shuffleSongsButton_expanded.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Collections.shuffle(songsList);
//                StorageUtil storageUtil = StorageUtil.getInstance(getContext());
//                storageUtil.clearCachedAudioPlaylist();
//                storageUtil.storeAudio(songsList);
//                storageUtil.storeAudioIndex(0);
//                SongsRecyclerViewAdapter songAdapter = new SongsRecyclerViewAdapter(getContext(), songsList, 3);
//                mySongsRecyclerView.setAdapter(songAdapter);
//                playerService.playNewMedia();
//            }
//        });
//
//        repeatSongsButton_expanded.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            int seek = 0;
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                seek = i;
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                playerService.seekTo(seek);
//                songSeekBar.setProgress(seek);
//            }
//        });
//
//        return v;
//    }
//
//
//    private void playPauseAction(View v) {
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mediaStatus == MEDIA_STATUS.RUNNING) {
//
////                    Intent broadcastIntent = new Intent(Broadcast_PAUSE_AUDIO);
////                    getContext().sendBroadcast(broadcastIntent);
//                    playerService.pauseMedia();
//                    playPauseButton_collapsed.setImageResource(R.drawable.play_button);
//                    playPauseButton_expanded.setImageResource(R.drawable.play_button);
//                    mediaStatus = MEDIA_STATUS.PAUSED;
//
//                } else if (mediaStatus == MEDIA_STATUS.PAUSED) {
//
////                    Intent broadcastIntent = new Intent(Broadcast_RESUME_AUDIO);
////                    getContext().sendBroadcast(broadcastIntent);
//                    playerService.resumeMedia();
//                    playPauseButton_collapsed.setImageResource(R.drawable.pause_button);
//                    playPauseButton_expanded.setImageResource(R.drawable.pause_button);
//                    mediaStatus = MEDIA_STATUS.RUNNING;
//
//                } else if (mediaStatus == MEDIA_STATUS.STOPPED) {
//
////                    Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
////                    getContext().sendBroadcast(broadcastIntent);
//                    playerService.playNewMedia();
//                    myHandler.postDelayed(UpdateSongTime, 100);
//                    playPauseButton_collapsed.setImageResource(R.drawable.pause_button);
//                    playPauseButton_expanded.setImageResource(R.drawable.pause_button);
//                    mediaStatus = MEDIA_STATUS.RUNNING;
//                }
//            }
//        });
//    }
//
//    void playNextAction(View v) {
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final StorageUtil storageUtil = StorageUtil.getInstance(getContext());
//                if (storageUtil.loadAudioIndex() + 1 >= storageUtil.loadAudio().size())
//                    storageUtil.storeAudioIndex(0);
//                else
//                    storageUtil.storeAudioIndex(storageUtil.loadAudioIndex() + 1);
//                playerService.playNewMedia();
//            }
//        });
//    }
//
//    void playPreviousAction(View v) {
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final StorageUtil storageUtil = StorageUtil.getInstance(getContext());
//                if (storageUtil.loadAudioIndex() - 1 <= 0)
//                    storageUtil.storeAudioIndex(storageUtil.loadAudio().size() - 1);
//                else
//                    storageUtil.storeAudioIndex(storageUtil.loadAudioIndex() - 1);
//                playerService.playNewMedia();
//            }
//        });
//    }
//
//
//    void playPrevious() {
//
//        final StorageUtil storageUtil = StorageUtil.getInstance(getContext());
//        if (storageUtil.loadAudioIndex() - 1 <= 0)
//            storageUtil.storeAudioIndex(storageUtil.loadAudio().size() - 1);
//        else
//            storageUtil.storeAudioIndex(storageUtil.loadAudioIndex() - 1);
//
//        if (playerService != null && playerService.getPlayerInstance() != null) {
//            if (!playerService.getPlayerInstance().isPlaying()) {
//                playerService.getPlayerInstance().stop();
//                return;
//            }
//            playerService.playNewMedia();
//        }
//    }
//
//
//    void playNext() {
//
//        final StorageUtil storageUtil = StorageUtil.getInstance(getContext());
//        if (storageUtil.loadAudioIndex() + 1 >= storageUtil.loadAudio().size())
//            storageUtil.storeAudioIndex(0);
//        else
//            storageUtil.storeAudioIndex(storageUtil.loadAudioIndex() + 1);
//        if (playerService != null && playerService.getPlayerInstance() != null) {
//            if (!playerService.getPlayerInstance().isPlaying()) {
//                mediaStatus = MEDIA_STATUS.STOPPED;
//                return;
//            }
//            playerService.playNewMedia();
//        }
//    }
//
//    void playCurrent() {
//        if (playerService != null && playerService.getPlayerInstance() != null) {
//            if (!playerService.getPlayerInstance().isPlaying()) {
//                mediaStatus = MEDIA_STATUS.STOPPED;
//                return;
//            }
//            playerService.playNewMedia();
//        }
//    }
//
//
//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            // We've bound to LocalService, cast the IBinder and get LocalService instance
//            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
//            playerService = binder.getService();
//            playerService.registerReceiver();
//            Toast.makeText(getContext(), "Service Bound", Toast.LENGTH_SHORT).show();
//            boundedService = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Toast.makeText(getContext(), "Service UnBounded", Toast.LENGTH_SHORT).show();
//            boundedService = false;
//        }
//    };
//
//
//    private Runnable UpdateSongTime = new Runnable() {
//        public void run() {
//
//            if (playerService != null) {
//                MediaPlayer mediaPlayer = playerService.getPlayerInstance();
//                if (mediaPlayer != null) {
//                    double startTime = mediaPlayer.getCurrentPosition();
//                    double finalTime = mediaPlayer.getDuration();
//                    songSeekBar.setMax((int) finalTime);
//                    songSeekBar.setProgress((int) startTime);
//                    startTimeView_expanded.setText(String.format("%s: %s",
//                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
//                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
//                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
//                                            toMinutes((long) startTime)))
//                    );
//                    endTimeView_expanded.setText(String.format("%s: %s",
//                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
//                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
//                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
//                                            toMinutes((long) finalTime)))
//                    );
//                    myHandler.postDelayed(this, 1000);
//                }
//            }
//        }
//    };
//
//    private void addToPlaylist(final SongModel songModel) {
//
//        ImageView createNewPlayList;
//        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//        View promptView = layoutInflater.inflate(R.layout.select_playlist_popup, null);
//
//        final RecyclerView myPlaylistsSongRecyclerView;
//        final AlertDialog alertD = new AlertDialog.Builder(getContext()).create();
//        final List<SongModel> listOfPlayLists = new ArrayList<>();
//
//
//        createNewPlayList = promptView.findViewById(R.id.createPlaylistButtonPopup);
//        myPlaylistsSongRecyclerView = promptView.findViewById(R.id.selectPlaylistRecyclerView);
//
//        final SongsRecyclerViewAdapter playListsAdapter = new SongsRecyclerViewAdapter(getContext(), listOfPlayLists, 4);
//        myPlaylistsSongRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        myPlaylistsSongRecyclerView.setAdapter(playListsAdapter);
//
//
//        Cursor songs_cursor2 = DatabaseHelper.getInstance(getContext()).getWritableDatabase().rawQuery("SELECT * FROM _playlists_tb ", new String[]{});
//        if (songs_cursor2 != null) {
//            if (songs_cursor2.moveToFirst()) {
//                do {
//                    SongModel song = new SongModel(0L, songs_cursor2.getString(songs_cursor2.getColumnIndex("_playlist_name_")), "", " ", " ", 0L, " ", " ", " ", "", "");
//                    listOfPlayLists.add(song);
//                } while (songs_cursor2.moveToNext());
//            }
//
//            songs_cursor2.close();
//        }
//
//        createNewPlayList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getPlayList(songModel);
//                alertD.dismiss();
//            }
//        });
//
//        playListsAdapter.setOnItemClickListener(new SongsRecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                DatabaseHelper.getInstance(getContext()).addThisToPlaylist(listOfPlayLists.get(position).getSongTitle(), songModel, DatabaseHelper.getInstance(getContext()).getWritableDatabase());
//                alertD.dismiss();
//                playListsAdapter.notifyDataSetChanged();
//            }
//        });
//
//        alertD.setView(promptView);
//        alertD.show();
//
//    }
//
//    public void getPlayList(final SongModel songModel) {
//
//        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//        View promptView = layoutInflater.inflate(R.layout.create_playlist_name_popup, null);
//
//        final AlertDialog alertD = new AlertDialog.Builder(getContext()).create();
//
//        final EditText userInput = promptView.findViewById(R.id.playlistnameView);
//
//        ImageView btnAdd1 = promptView.findViewById(R.id.createPlaylistOk);
//        ImageView btnAdd2 = promptView.findViewById(R.id.createPlaylistCancel);
//
//        btnAdd1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                boolean success = DatabaseHelper.getInstance(getContext()).createPlayList(DatabaseHelper.getInstance(getContext()).getWritableDatabase(), userInput.getText().toString());
//                if (success)
//                    Toast.makeText(getContext(), "Created Playlist", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
//                DatabaseHelper.getInstance(getContext()).addThisToPlaylist(userInput.getText().toString(), songModel, DatabaseHelper.getInstance(getContext()).getWritableDatabase());
//                if (PlayListFragment.songAdapter != null) {
//                    SongModel newSt = new SongModel(0L, userInput.getText().toString(), "", " ", " ", 0L, " ", " ", " ", "", "");
//                    PlayListFragment.playListsList.add(newSt);
//                    PlayListFragment.songAdapter.notifyDataSetChanged();
//                }
//                alertD.dismiss();
//            }
//        });
//
//        btnAdd2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                alertD.dismiss();
//            }
//        });
//
//        alertD.setView(promptView);
//        alertD.show();
//
//    }
//
//    private void setPlayerSongsRecyclerView(List<SongModel> songsList2, int position) {
//        SongsRecyclerViewAdapter songAdapter = new SongsRecyclerViewAdapter(getContext(), songsList2, 3);
//        mySongsRecyclerView.setAdapter(songAdapter);
//        mySongsRecyclerView.scrollToPosition(position);
////        Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
////        getContext().sendBroadcast(broadcastIntent);
////        songChanged(position);
////        if (!mediaRunning)
////            playerViewLayout.findViewById(R.id.play_pause_button_expanded).callOnClick();
//    }
//
//    public void songChanged(int position) {
//        StorageUtil storageUtil = StorageUtil.getInstance(getContext());
//        storageUtil.storeAudioIndex(position);
//        collapsedImageView.setImageURI(Uri.parse(storageUtil.loadAudio().get(storageUtil.loadAudioIndex()).getSongArtPath()));
//        collapsedTextView.setText(storageUtil.loadAudio().get(storageUtil.loadAudioIndex()).getSongTitle());
//        mySongsRecyclerView.scrollToPosition(position);
//    }
//
//    private void _initRecyclerView() {
//        SongsRecyclerViewAdapter songAdapter = new SongsRecyclerViewAdapter(getContext(), songsListFinal, 3);
//        mySongsRecyclerView.setAdapter(songAdapter);
//
//        mySongsRecyclerView.addScrollStateChangeListener(new DiscreteScrollView.ScrollStateChangeListener<RecyclerView.ViewHolder>() {
//            int posFrom = 0;
//
//            @Override
//            public void onScrollStart(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {
//
//            }
//
//            @Override
//            public void onScrollEnd(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {
//
//                StorageUtil storageUtil = StorageUtil.getInstance(getContext());
//                storageUtil.storeAudioIndex(adapterPosition);
//                playCurrent();
//            }
//
//            @Override
//            public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {
//                posFrom = currentPosition;
//            }
//        });
//        final StorageUtil storageUtil = StorageUtil.getInstance(getContext());
//        if (storageUtil.loadAudio() != null) {
//            if (storageUtil.loadAudio().size() > 0)
//                setPlayerSongsRecyclerView(storageUtil.loadAudio(), storageUtil.loadAudioIndex());
//
//            else
//                setPlayerSongsRecyclerView(songsList, 0);
//        } else {
//            storageUtil.storeAudio(songsList);
//            setPlayerSongsRecyclerView(songsList, 0);
//
//        }
//        mySongsRecyclerView.setAdapter(songAdapter);
//        mySongsRecyclerView.setItemTransformer(new ScaleTransformer.Builder()
//                .setMaxScale(1.05f)
//                .setMinScale(0.8f)
//                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
//                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
//                .build());
//    }
//
//    private void _initSongs() {
//        ContentResolver contentResolver = null;
//        if (getContext() != null)
//            contentResolver = getContext().getContentResolver();
//        if (contentResolver == null)
//            return;
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Cursor cursor = contentResolver.query(
//                uri, // Uri
//                null,
//                null,
//                null,
//                null
//        );
//        songsList = new ArrayList<>();
//        songsListFinal = new ArrayList<>();
//        if (cursor == null) {
//            Toast.makeText(getContext(), "Something Went Wrong.", Toast.LENGTH_LONG).show();
//        } else if (!cursor.moveToFirst()) {
//            Toast.makeText(getContext(), "No Music Found on SD Card.", Toast.LENGTH_LONG).show();
//        } else if (cursor.getCount() > 0) {
//
//            int id = 0, title = 0, artist = 0, albumId = 0, path = 0, typeId = 0;
//            if (cursor.getColumnIndex(MediaStore.Audio.Media._ID) != -1) {
//                id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
//            }
//            if (cursor.getColumnIndex(MediaStore.Audio.Media.TITLE) != -1) {
//                title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
//            }
//            if (cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST) != -1) {
//                artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
//            }
//            if (cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID) != -1) {
//                albumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
//            }
//            if (cursor.getColumnIndex((MediaStore.Audio.Media.DATA)) != -1) {
//                path = cursor.getColumnIndex((MediaStore.Audio.Media.DATA));
//            }
//            if (cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE) != -1) {
//                typeId = cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE);
//            }
//
//
//            long songId, songAlbumID;
//            String songTitle, songArtist, songGenre, songAlbum, songAlbumArtPath, songArtPath, songPath, songType;
//
//            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
//            SQLiteDatabase db = databaseHelper.getWritableDatabase();
//            databaseHelper.refreshData(db);
//
//            String isFavourite = "false";
//
//            do {
//                songId = cursor.getLong(id);
//                songTitle = cursor.getString(title);
//                songArtist = cursor.getString(artist);
//                songGenre = SongsMetaData.getSongGenre(getContext(), songId);
//                songAlbumID = cursor.getLong(albumId);
//                songAlbum = SongsMetaData.getAlbumName(getContext(), songAlbumID);
//                songAlbumArtPath = SongsMetaData.getAlbumArtPath(getContext(), songAlbumID);
//                songArtPath = SongsMetaData.getCoverArtPath(getContext(), songAlbumID);
//                songPath = cursor.getString(path);
//                songType = cursor.getString(typeId);
//
//                if (songType.contains("mpeg") || songType.contains("song") || songType.contains("mp3")) {
//
//                    if (songArtist == null) {
//                        songArtist = "Un Known Artist";
//                    }
//
//                    if (songGenre == null) {
//                        songGenre = "Un Known";
//                    }
//
//                    if (songAlbum == null) {
//                        songArtist = "Un Known";
//                    }
//
//                    if (songAlbumArtPath == null) {
//                        songAlbumArtPath = "Un Known";
//                    }
//
//                    if (songArtPath == null) {
//                        songArtPath = "Un Known";
//                    }
//
//                    Log.i("Song ", songTitle);
//                    String sql = "SELECT _is_favourite FROM  _songs_tb WHERE _song_id=" + (songId);
//                    Cursor cursorSong = db.rawQuery(sql, null);
//                    if (cursorSong != null) {
//                        if (cursorSong.moveToFirst()) {
//                            isFavourite = cursorSong.getString(cursor.getColumnIndex("_is_favourite"));
//                            cursorSong.close();
//                        }
//                    }
//                    SongModel song = new SongModel(songId, songTitle, songArtist, songGenre, isFavourite, songAlbumID, songAlbum, songAlbumArtPath, songArtPath, songPath, isFavourite);
//                    databaseHelper.insertDataSongs(songId, songTitle, songArtist, songGenre, songAlbumID, songAlbum, songAlbumArtPath, songArtPath, songPath, db, isFavourite);
//                    songsList.add(song);
//                    songsListFinal.add(song);
//
//                }
//
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
//
//        final StorageUtil storageUtil = StorageUtil.getInstance(getContext());
//        storageUtil.storeAudio(songsList);
//        if (songsList.size() == 0)
//            storageUtil.storeAudioIndex(-1);
//        else storageUtil.storeAudioIndex(0);
//
//    }
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (getContext() != null)
//            StorageUtil.registerPres(getContext(), this);
//    }
//
//    @Override
//    public void onStop() {
//        if (getContext() != null)
//            StorageUtil.unRegisterPres(getContext(), this);
//        super.onStop();
//        if (getContext() != null)
//            StorageUtil.unRegisterPres(getContext(), this);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (boundedService) {
//            if (getActivity() != null)
//                getActivity().unbindService(serviceConnection);
//            boundedService = false;
//            if (playerService != null) {
//                playerService.unRegisterReceiver();
//                playerService.stopSelf();
//            }
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!boundedService && playerService == null) {
//            if (getContext() != null) {
//                Intent playerIntent = new Intent(getContext().getApplicationContext(), PlayerService.class);
//                if (getActivity() != null)
//                    getActivity().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//            }
//        }
//    }
//}
