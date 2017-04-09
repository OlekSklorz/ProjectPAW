package com.gwozdz2gmail.michal.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

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
            whiteBeardButton, sepiaButton, blackBeardButton, backButton, faceDetectorButton;
    private static boolean shouldDeleted = false;
    private static Bitmap lastImage;
    /**
     * Loading photo.
     * @param activity chooser's owner
     * @param image bitmap which shoulb be saved as a backup. It may be null if we don't want to saveFileAction anything.
     */
    public static void showChooser(final Activity activity, Bitmap image){
        if(image != null) {
            lastImage = image;
        }
        activity.startActivity(new Intent(activity, LoadingSavingPhotoActivity.class));
    }

    public static Bitmap getLastImage(){
        return lastImage;
    }

    public static void initBackButton(Activity activity) {
        backButton = (ImageButton) activity.findViewById(R.id.returnCameraButton);
        initBackListener(activity);
    }

    public static void initBackListener(final Activity activity) {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        });
    }

    /**
     * Creates buttons for settings menu.
     * @param activity activity that calls method
     */
    public static void initSettingsButtons(Activity activity){
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
        faceDetectorButton = (ImageButton) activity.findViewById(R.id.face_detector_filter);

        initSettingsListeners(activity);
        initFiltersListeners(activity);
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

    public static BitmapDrawable detectFace(Activity activity, Bitmap map){
        /* ATTENTION!!: setTracking should be set on false, when we have single photo, because it
         gives more accurate result. But when we have consecutive images for example live video,
         it should be set on true, because it gives more accurate and faster result.*/
        FaceDetector detector = new FaceDetector.Builder(activity).setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS).build();
        if(!detector.isOperational()){
            Log.e("Missing library", "Native library can't be downloaded");
            return null;
        }else{
            SparseArray<Face> faces = detector.detect(new Frame.Builder().setBitmap(map).build());
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5.0f);
            Bitmap tempBitmap = Bitmap.createBitmap(map.getWidth(), map.getHeight(), Bitmap.Config.RGB_565);
            Canvas tempCanvas = new Canvas(tempBitmap);
            tempCanvas.drawBitmap(map, 0, 0, null);
            for (int i = 0; i < faces.size(); ++i) {
                Face face = faces.valueAt(i);
                for (Landmark landmark : face.getLandmarks()) {
                    int cx = (int) (landmark.getPosition().x * 1);
                    int cy = (int) (landmark.getPosition().y * 1);
                    tempCanvas.drawCircle(cx, cy, 10, paint);
                }
            }
            // detector uses native resources, so it is necessary to release the detector
            detector.release();
            return new BitmapDrawable(activity.getResources(), tempBitmap);
        }
    }

    private static void initSettingsListeners(final Activity activity){
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
                    faceDetectorButton.setVisibility(filtersButton.getVisibility());
                }
            }
        });
    }

    private static void initFiltersListeners(final Activity activity){
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
        faceDetectorButton.setVisibility(visible_filters);
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
        if(LoadingSavingPhotoActivity.getSelectedImageURI() != null) { // check if image is already saved
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
            shareIntent.putExtra(Intent.EXTRA_STREAM, LoadingSavingPhotoActivity.getSelectedImageURI());
            activity.startActivity(Intent.createChooser(shareIntent, resources.getString(R.string.choosing_sender)));
        }
    }
}
