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
     * Creates dialog with three options: saving, loading photo and cancel.
     * @param activity dialog's owner
     */
    public static void makePhotoOptionsDialog(final Activity activity){
        Intent loadingIntent = new Intent(activity, LoadingPhotoActivity.class);
        loadingIntent.putExtra("loading", true);
        activity.startActivity(loadingIntent);
    }
}
