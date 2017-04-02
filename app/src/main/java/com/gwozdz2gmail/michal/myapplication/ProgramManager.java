package com.gwozdz2gmail.michal.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Locale;

public class ProgramManager {
    /**
     * Loading photo.
     * @param activity chooser's owner
     * @param image drawable image to be send as a backup. It may be null if we don't want to send something.
     */
    public static void showChooser(final Activity activity, BitmapDrawable image){
        byte[] byteArray = null;
        if(image != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        }
        Intent loadingIntent = new Intent(activity, LoadingSavingPhotoActivity.class);
        loadingIntent.putExtra("image", byteArray);
        activity.startActivity(loadingIntent);
    }

    /**
     * Shows popup menu with settings options: language, about, help.
     * @param activity activity
     * @param view menu's owner
     */
    public static void showPopup(final Activity activity, View view){
        PopupMenu popup = new PopupMenu(activity, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.settings_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.language:
                        showLanguageDialog(activity);
                        return true;
                    case R.id.about:
                        showAboutDialog(activity);
                        return true;
                    case R.id.help:
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }

    private static void showAboutDialog(Activity activity){
        Resources resources = activity.getResources();
        TextView about = new TextView(activity);
        about.setText(resources.getString(R.string.authors) + "\n" + resources.getString(R.string.version));
        about.setGravity(Gravity.CENTER_HORIZONTAL);
        about.setPadding(0, 30, 0, 0);
        AlertDialog dialog = new AlertDialog.Builder(activity).setTitle(resources.getString(R.string.about))
                .setView(about).setPositiveButton(resources.getString(R.string.ok), null).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private static void showLanguageDialog(Activity activity){
        String language = Locale.getDefault().getDisplayLanguage();
        Resources resources = activity.getResources();
        final String[] languages = resources.getStringArray(R.array.languages);
        int defaultLanguageIndex = 0;
        for(int i = 0; i < languages.length; i++)
            if(language.equalsIgnoreCase(languages[i]))
                defaultLanguageIndex = i;
        AlertDialog dialog = new AlertDialog.Builder(activity).setTitle(activity.getResources()
                .getString(R.string.language_chooser)).setSingleChoiceItems(languages, defaultLanguageIndex,
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(languages[which].equalsIgnoreCase("polski")){
                            
                        }else{
                            if(languages[which].equalsIgnoreCase("angielski")){

                            }
                        }
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    private void setLocalization(String language, Resources resources){
        Locale locale = new Locale("en");
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
        conf.setLocale(locale);
        resources.updateConfiguration(conf, metrics);
    }
}
