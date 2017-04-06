package com.gwozdz2gmail.michal.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadingSavingPhotoActivity extends AppCompatActivity {

    private static ImageView image;
    private static Uri selectedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_photo);

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
                Bitmap thumbnail = BitmapFactory.decodeFile(picturePath);
                image.setImageBitmap(thumbnail);
            }
        }else{
            byte[] backupByteArray = getIntent().getByteArrayExtra("image");
            if(backupByteArray != null)
                image.setImageBitmap(BitmapFactory.decodeByteArray(backupByteArray, 0, backupByteArray.length));
        }
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
