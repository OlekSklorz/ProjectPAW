package com.gwozdz2gmail.michal.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.microedition.khronos.opengles.GL10;

public class LoadingSavingPhotoActivity extends AppCompatActivity {

    private static ImageView image;
    private static Uri selectedImage = null;
    private static ArrayList<Integer> counters = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_photo);
        if(counters == null) counters = new ArrayList<>();
        counters.add(counters.size(), 1);
        image = (ImageView) findViewById(R.id.imageView);
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images
                .Media.EXTERNAL_CONTENT_URI), 1);
        ProgramManager.initSettingsButtons(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
               /* Bitmap thumbnail = BitmapFactory.decodeFile(picturePath);
                Bitmap d = new BitmapDrawable(getResources() , thumbnail).getBitmap();
                int nh = (int) ( d.getHeight() * (4096.0 / d.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(d, 4096, nh, true); // first version
                //iv.setImageBitmap(scaled);
               // int[] maxTextureSize = new int[1];
               // GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
              //  Log.d("JESTEM",   " "  + maxTextureSize[0]);
                image.setImageBitmap(scaled);*/

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picturePath, options);
                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;
                //String imageType = options.outMimeType;
                options.inSampleSize = calculateInSampleSize(options, width, width);
                options.inJustDecodeBounds = false;
                image.setImageBitmap(BitmapFactory.decodeFile(picturePath, options));
            }
        }else{
            byte[] backupByteArray = getIntent().getByteArrayExtra("image");
            if(backupByteArray != null)
                image.setImageBitmap(BitmapFactory.decodeByteArray(backupByteArray, 0, backupByteArray.length));
        }
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    public void onRestart(){
        super.onRestart();
        if(ProgramManager.isShouldDeleted()) { // deleted temporary file after shared
            new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/temp.jpeg")
                    .delete();
            ProgramManager.setShouldDeleted(false);
        }
    }

    @Override
    public void onBackPressed(){
        Log.d("JESTEM", counters.get(0) + "");
        if(counters.get(0) == 1){
            startActivity(new Intent(this, MainActivity.class));
        }
        super.onBackPressed();
    }
    /**
     * Loading photo.
     * @param view method's owner
     */
    public void load(View view){

        ProgramManager.showChooser(this, ((BitmapDrawable)image.getDrawable()));
    }

    /**
     * Saves image to default folder named "Pictures" in Gallery.
     * @param view method's owner
     */
    public void save(View view) {
        MediaStore.Images.Media.insertImage(getContentResolver(), ((BitmapDrawable)image.getDrawable()).getBitmap(), createFileName(), "Created file");
        Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns URI from selected image recently.
     * @return URI of selected image
     */
    public static Uri getSelectedImage(){
        return selectedImage;
    }

    /**
     * Sets URI for selected image recently.
     * @param uri URI of selected image
     */
    public static void setSelectedImage(Uri uri){
        selectedImage = uri;
    }

    /**
     * Returns bitmap of actual selected image.
     * @return bitmap of selected image
     */
    public static Bitmap getImage(){
        return ((BitmapDrawable)image.getDrawable()).getBitmap();
    }

    public void returnToCamera(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    private  String createFileName(){
        return "FILE" + (new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date())) + ".jpeg";
    }
}
