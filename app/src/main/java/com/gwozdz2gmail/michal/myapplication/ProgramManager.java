package com.gwozdz2gmail.michal.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;

public class ProgramManager {
    /**
     * Shows dialog with three options: saving, loading photo and cancel.
     * @param activity dialog's owner
     */
    public static void makePhotoOptionsDialog(final Activity activity){
        Resources strings = activity.getResources();
        final String choosingOption = strings.getString(R.string.choosing),
                savingOption = strings.getString(R.string.saving);
        final CharSequence[] options = {choosingOption, savingOption, strings.getString(R.string.cancel)};
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(R.string.selecting_photo);
        dialog.setItems(options, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int item){
                if(options[item].equals(choosingOption)){
                    Intent loadingIntent = new Intent(activity, LoadingPhotoActivity.class);
                    activity.startActivity(loadingIntent);
                }else if(options[item].equals(savingOption)){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    activity.startActivityForResult(intent, 2);
                }else dialog.dismiss();
            }
        });
        dialog.show();
    }
}
