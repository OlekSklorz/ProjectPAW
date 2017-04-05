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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Random;

public class ProgramManager {
    private static int visible = View.INVISIBLE;
    private static ImageButton settingsButton, filtersButton, shareButton, languagesButton,
            creditsButton, bugButton;
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

    public static void initSettingsButtons(Activity activity){
        settingsButton = (ImageButton) activity.findViewById(R.id.settings);
        filtersButton = (ImageButton) activity.findViewById(R.id.filters);
        shareButton = (ImageButton) activity.findViewById(R.id.share);
        languagesButton = (ImageButton) activity.findViewById(R.id.languages);
        creditsButton = (ImageButton) activity.findViewById(R.id.credits);
        bugButton = (ImageButton) activity.findViewById(R.id.bugs);
        addActionToSetttingsButton();
        initSettingsButtonListeners(activity);
    }

    private static void addActionToSetttingsButton(){
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visible == View.INVISIBLE) {
                    visible = View.VISIBLE;
                } else {
                    visible = View.INVISIBLE;
                }

                filtersButton.setVisibility(visible);
                shareButton.setVisibility(visible);
                languagesButton.setVisibility(visible);
                creditsButton.setVisibility(visible);
                bugButton.setVisibility(visible);
            }
        });
    }

    private static void initSettingsButtonListeners(final Activity activity){
        filtersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, activity.getResources().getString(R.string.filters), Toast.LENGTH_LONG).show();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(activity);
            }
        });

        languagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageDialog(activity);
            }
        });

        creditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAbout(activity);
            }
        });

        bugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBugActivity(activity);
            }
        });
    }

    private static void showAbout(Activity activity){
        Toast.makeText(activity, activity.getResources().getString(R.string.credits), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(activity, CreditsActivity.class);
        activity.startActivity(intent);
    }

    private static void showLanguageDialog(final Activity activity){
        Toast.makeText(activity, activity.getResources().getString(R.string.languages), Toast.LENGTH_LONG).show();
        final String language = Locale.getDefault().getDisplayLanguage();
        final Resources resources = activity.getResources();
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
                        /* we need create new Languages object every time, to change languages in
                        map to names in choosen language*/
                       new Languages(activity).setLocalization(languages[which], resources);
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private static void showBugActivity(Activity activity){
        Resources resources = activity.getResources();
        Random random = new Random();
        Toast.makeText(activity, resources.getString(R.string.report), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.setData(Uri.parse("mailto:" + "zuliq94@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.bug_nr) + random.nextInt());
        intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.bug));
        activity.startActivity(Intent.createChooser(intent, resources.getString(R.string.send)));
    }

    private static void share(Activity activity){
        Bitmap image = LoadingSavingPhotoActivity.getImage();
        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath() + "/Share Image Tutorial/");
        dir.mkdirs();
        File file = new File(dir, "sample_wallpaper.png");
        Toast.makeText(activity, activity.getResources().getString(R.string.share), Toast.LENGTH_LONG).show();
        OutputStream output;
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpeg");
            output = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
            Uri uri = Uri.fromFile(file);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            activity.startActivity(shareIntent);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
