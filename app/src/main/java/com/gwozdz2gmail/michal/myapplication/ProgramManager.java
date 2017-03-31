package com.gwozdz2gmail.michal.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class ProgramManager {
    static Bitmap b = null;
    /**
     * Loading photo.
     * @param activity chooser's owner
     */
    public static void showChooser(final Activity activity){
        Intent loadingIntent = new Intent(activity, LoadingSavingPhotoActivity.class);
        activity.startActivity(loadingIntent);
    }
}
