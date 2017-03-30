package com.gwozdz2gmail.michal.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class ProgramManager {
    /**
     * Loading photo.
     * @param activity dialog's owner
     */
    public static void load(final Activity activity, boolean loading){
        Intent loadingIntent = new Intent(activity, LoadingPhotoActivity.class);
        loadingIntent.putExtra("loading", loading);
        activity.startActivity(loadingIntent);
    }
}
