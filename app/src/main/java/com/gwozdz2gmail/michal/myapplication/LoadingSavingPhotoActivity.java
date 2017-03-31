package com.gwozdz2gmail.michal.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadingSavingPhotoActivity extends AppCompatActivity {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_photo);

        image = (ImageView) findViewById(R.id.imageView);
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images
                .Media.EXTERNAL_CONTENT_URI), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = BitmapFactory.decodeFile(picturePath);
                Log.w("path of image ", picturePath+"");
                image.setImageBitmap(thumbnail);
            }
        }else{
            byte[] backupByteArray = getIntent().getByteArrayExtra("image");
            if(backupByteArray != null)
                image.setImageBitmap(BitmapFactory.decodeByteArray(backupByteArray, 0, backupByteArray.length));
        }
    }
    /**
     * Loading photo.
     * @param view method's owner
     */
    public void load(View view){
        ProgramManager.showChooser(this, ((BitmapDrawable)image.getDrawable()));
    }

    public void save(View view) {
        //It's first idea. It's working but we can't create our folder, because it creates default folder Pictures in Gallery
        MediaStore.Images.Media.insertImage(getContentResolver(), ((BitmapDrawable)image.getDrawable()).getBitmap(),"MyFile.png", "Created file");
        Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();

        /* This is where the second idea begins. It's better idea, because we can create our
        folder for this application for example OurApplicationFolder and this folder will be created
        in Gallery. But be careful - saving process is strange - to displayed new folder you must for
        example disconnect cabel from computer or restart phone and wait.
         */
        /*Bitmap map = ((BitmapDrawable)image.getDrawable()).getBitmap();
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("TAG",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            map.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }*/
    }

    /**
     * This is method for second idea of saving image.
     * @return image file
     */
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/thisFolder");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}
