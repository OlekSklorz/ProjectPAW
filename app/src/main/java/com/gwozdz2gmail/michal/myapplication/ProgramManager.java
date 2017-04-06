package com.gwozdz2gmail.michal.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Random;

public class ProgramManager {
    private static int visible = View.INVISIBLE;
    private static int visible_filters = View.INVISIBLE;
    public static ImageButton settingsButton, filtersButton, shareButton, languagesButton,
            creditsButton, bugButton, offButton, monoButton, negativeButton,
            whiteBeardButton, sepiaButton, blackBeardButton;
    private static boolean shouldDeleted = false;
    /**
     * Loading photo.
     * @param activity chooser's owner
     * @param image drawable image to be send as a backup. It may be null if we don't want to send something.
     */
    public static void showChooser(final Activity activity, BitmapDrawable image){
        byte[] byteArray = null;
        if(image != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();
        }
        Intent loadingIntent = new Intent(activity, LoadingSavingPhotoActivity.class);
        loadingIntent.putExtra("image", byteArray);
        activity.startActivity(loadingIntent);
    }

    /**
     * Creates buttons for settings menu.
     * @param activity activity that calls method
     */
    static void initSettingsButtons(Activity activity){
        settingsButton = (ImageButton) activity.findViewById(R.id.settings);
        filtersButton = (ImageButton) activity.findViewById(R.id.filters);
        if(!(activity instanceof MainActivity))
            shareButton = (ImageButton) activity.findViewById(R.id.share);
        languagesButton = (ImageButton) activity.findViewById(R.id.languages);
        creditsButton = (ImageButton) activity.findViewById(R.id.credits);
        bugButton = (ImageButton) activity.findViewById(R.id.bugs);

        offButton = (ImageButton) activity.findViewById(R.id.off_filter);
        monoButton = (ImageButton) activity.findViewById(R.id.mono_filter);
        negativeButton = (ImageButton) activity.findViewById(R.id.negative_filter);
        whiteBeardButton = (ImageButton) activity.findViewById(R.id.white_beard_filter);
        sepiaButton = (ImageButton) activity.findViewById(R.id.sepia_filter);
        blackBeardButton = (ImageButton) activity.findViewById(R.id.black_beard_filter);

        addActionToSetttingsButton(activity);
        initSettingsButtonListeners(activity);
    }

    /**
     * Return information about if file of shared image should be deleted from folder.
     * @return true when should be deleted, false otherwise
     */
    public static boolean isShouldDeleted(){
        return shouldDeleted;
    }

    /**
     * Sets obligation to delete file of shared image from folder.
     * @param deleted true when should be deleted, false otherwise
     */
    public static void setShouldDeleted(boolean deleted){
        shouldDeleted = deleted;
    }

    private static void addActionToSetttingsButton(final Activity activity){
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visible == View.INVISIBLE) {
                    visible = View.VISIBLE;
                } else {
                    visible = View.INVISIBLE;
                }

                filtersButton.setVisibility(visible);
                if(!(activity instanceof MainActivity))
                    shareButton.setVisibility(visible);
                languagesButton.setVisibility(visible);
                creditsButton.setVisibility(visible);
                bugButton.setVisibility(visible);

                if(filtersButton.getVisibility() == View.INVISIBLE) {
                    visible_filters = View.INVISIBLE;
                    offButton.setVisibility(filtersButton.getVisibility());
                    monoButton.setVisibility(filtersButton.getVisibility());
                    negativeButton.setVisibility(filtersButton.getVisibility());
                    whiteBeardButton.setVisibility(filtersButton.getVisibility());
                    sepiaButton.setVisibility(filtersButton.getVisibility());
                    blackBeardButton.setVisibility(filtersButton.getVisibility());
                }
            }
        });
    }

    private static void initSettingsButtonListeners(final Activity activity){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case (R.id.filters):
                        showFilters(activity);
                        break;
                    case (R.id.share):
                        share(activity);
                        break;
                    case (R.id.languages):
                        showLanguageDialog(activity);
                        break;
                    case (R.id.credits):
                        showAbout(activity);
                        break;
                    case (R.id.bugs):
                        showBugActivity(activity);
                        break;
                }

            }
        };

        filtersButton.setOnClickListener(listener);
        if(!(activity instanceof MainActivity))
        {
            shareButton.setOnClickListener(listener);
        }
        languagesButton.setOnClickListener(listener);
        creditsButton.setOnClickListener(listener);
        bugButton.setOnClickListener(listener);
    }

    private static void showFilters(Activity activity) {
        if(visible_filters == View.INVISIBLE) {
            visible_filters = View.VISIBLE;
        } else {
            visible_filters = View.INVISIBLE;
        }
        Toast.makeText(activity, activity.getResources().getString(R.string.filters), Toast.LENGTH_LONG).show();
        offButton.setVisibility(visible_filters);
        monoButton.setVisibility(visible_filters);
        negativeButton.setVisibility(visible_filters);
        whiteBeardButton.setVisibility(visible_filters);
        sepiaButton.setVisibility(visible_filters);
        blackBeardButton.setVisibility(visible_filters);
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
        Resources resources = activity.getResources();
        if(LoadingSavingPhotoActivity.getSelectedImage() != null) { // check if image is already saved
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/");
            if (!dir.exists()) dir.mkdirs();
            File file = new File(dir, "temp.jpeg");
            Toast.makeText(activity, resources.getString(R.string.share), Toast.LENGTH_LONG).show();
            try (OutputStream output = new FileOutputStream(file)) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                LoadingSavingPhotoActivity.getImage().compress(Bitmap.CompressFormat.JPEG, 100, output);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                activity.startActivity(Intent.createChooser(shareIntent, resources.getString(R.string.choosing_sender)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            shouldDeleted = true;
        }else{
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, LoadingSavingPhotoActivity.getSelectedImage());
            activity.startActivity(Intent.createChooser(shareIntent, resources.getString(R.string.choosing_sender)));
        }
    }
}
