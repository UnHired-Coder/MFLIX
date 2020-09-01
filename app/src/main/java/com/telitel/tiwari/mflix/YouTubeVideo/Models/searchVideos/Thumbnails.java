
package com.telitel.tiwari.mflix.YouTubeVideo.Models.searchVideos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumbnails {

    @SerializedName("medium")
    @Expose
    private Medium medium;

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

}
