package com.gwozdz2gmail.michal.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadingSavingPhotoActivity extends AppCompatActivity {
    private static ImageView image;
    private static Uri selectedImageURI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_photo);
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images
                .Media.EXTERNAL_CONTENT_URI), 1);
    }

    @Override
    public void onStart(){
        super.onStart();
        initImageView();
        initSettingsButtons();
        initBackButton();
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

    private void initImageView() {
        image = (ImageView) findViewById(R.id.imageView); // it must be here, to saveFileAction and share proper image
    }

    private void initSettingsButtons() {
        ProgramManager.initSettingsButtons(this); // init buttons when return to activity and when create this activity
    }

    private void initBackButton() {
        ProgramManager.initBackButton(this);
    }

    public void loadFileAction(View view){
        ProgramManager.showChooser(this, ((BitmapDrawable)image.getDrawable()).getBitmap());
    }

    /**
     * Saves image to default folder named "Pictures" in Gallery.
     * @param view method's owner
     */
    public void saveFileAction(View view) {
        MediaStore.Images.Media.insertImage(getContentResolver(), ((BitmapDrawable)image.getDrawable()).getBitmap(), createFileName(), "Created file");
        Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns URI from selected image recently.
     * @return URI of selected image
     */
    public static Uri getSelectedImageURI(){
        return selectedImageURI;
    }

    /**
     * Sets URI for selected image recently.
     * @param uri URI of selected image
     */
    public static void setSelectedImageURI(Uri uri){
        selectedImageURI = uri;
    }

    /**
     * Returns bitmap of actual selected image.
     * @return bitmap of selected image
     */
    public static Bitmap getImage(){
        return ((BitmapDrawable)image.getDrawable()).getBitmap();
    }

    private  String createFileName(){
        return "FILE" + (new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date())) + ".jpeg";
    }

    private static Bitmap adjustImageDimension(WindowManager manager, String picturePath){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);
        options.inSampleSize = calculateInSampleSize(options, width, width);
        options.inJustDecodeBounds = false;
        Bitmap scaledBitmap = BitmapFactory.decodeFile(picturePath, options);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(picturePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotateBitmap(scaledBitmap, exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED));
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                selectedImageURI = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImageURI, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                image.setImageBitmap(adjustImageDimension(getWindowManager(), picturePath));
            }
        } else {
            Bitmap map = ProgramManager.getLastImage();
            if(map != null)
                image.setImageBitmap(map);
        }
    }
}
