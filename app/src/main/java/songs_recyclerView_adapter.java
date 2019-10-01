import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.telitel.tiwari.mflix.R;

public class songs_recyclerView_adapter extends RecyclerView.Adapter<songs_recyclerView_adapter.mySongsViewHolder> {



    public songs_recyclerView_adapter(){


    }




    @NonNull
    @Override
    public mySongsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull mySongsViewHolder mySongsViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



    public static class  mySongsViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_Name;
        private TextView tv_Artist;
        private ImageView iv_AlbumArt;




        public mySongsViewHolder(View itemView){

            super(itemView);

//            tv_Name=(TextView) itemView.findViewById(R.id.song_name);
//            tv_Artist=(TextView)itemView.findViewById(R.id.song_artist);
//            iv_AlbumArt=(ImageView)itemView.findViewById(R.id.playerSongArtView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });




        }

    }



}
