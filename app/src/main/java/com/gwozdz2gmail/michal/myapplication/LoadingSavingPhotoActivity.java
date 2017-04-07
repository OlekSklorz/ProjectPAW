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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadingSavingPhotoActivity extends AppCompatActivity {
    private static byte[] byteArray = null;
    private static ImageView EXTRA_IMAGE;
    private static Uri EXTRA_SELECTED_IMAGE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_photo);

        EXTRA_IMAGE = (ImageView) findViewById(R.id.imageView);
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images
                .Media.EXTERNAL_CONTENT_URI), 1);
        ProgramManager.initButtons(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode) {
            case 1 : {
                if(resultCode == RESULT_OK){
                    if(requestCode == 1){
                        EXTRA_SELECTED_IMAGE = data.getData();
                        String[] filePath = {MediaStore.Images.Media.DATA};
                        Cursor c = getContentResolver().query(EXTRA_SELECTED_IMAGE, filePath, null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        String picturePath = c.getString(columnIndex);
                        c.close();
                        Bitmap thumbnail = BitmapFactory.decodeFile(picturePath);
                        EXTRA_IMAGE.setImageBitmap(thumbnail);
                    }
                }else{
                    byte[] backupByteArray = getIntent().getByteArrayExtra("image");
                    if(backupByteArray != null)
                        EXTRA_IMAGE.setImageBitmap(BitmapFactory.decodeByteArray(backupByteArray, 0, backupByteArray.length));
                }
                break;
            }
            case 2 : {
                if(resultCode == 1 && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("image");

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();

                    Intent loadingActivity = new Intent(this, LoadingSavingPhotoActivity.class);
                    loadingActivity.putExtra("image", byteArray);
                    startActivity(loadingActivity);
                } else { }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRestart(){
        super.onRestart();
        if(ProgramManager.isExtraShouldDeleted()) { // deleted temporary file after shared
            new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/temp.jpeg")
                    .delete();
            ProgramManager.setExtraShouldDeleted(false);
        }
    }
    /**
     * Loading photo.
     * @param view method's owner
     */
    public void load(View view){
        Intent loadingIntent = new Intent(this, LoadingSavingPhotoActivity.class);
        this.startActivityForResult(loadingIntent, 2);
    }

    /**
     * Saves EXTRA_IMAGE to default folder named "Pictures" in Gallery.
     * @param view method's owner
     */
    public void save(View view) {
        MediaStore.Images.Media.insertImage(getContentResolver(), ((BitmapDrawable) EXTRA_IMAGE.getDrawable()).getBitmap(), createFileName(), "Created file");
        Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns URI from selected EXTRA_IMAGE recently.
     * @return URI of selected EXTRA_IMAGE
     */
    public static Uri getExtraSelectedImage(){
        return EXTRA_SELECTED_IMAGE;
    }

    /**
     * Sets URI for selected EXTRA_IMAGE recently.
     * @param uri URI of selected EXTRA_IMAGE
     */
    public static void setExtraSelectedImage(Uri uri){
        EXTRA_SELECTED_IMAGE = uri;
    }

    /**
     * Returns bitmap of actual selected EXTRA_IMAGE.
     * @return bitmap of selected EXTRA_IMAGE
     */
    public static Bitmap getExtraImage(){
        return ((BitmapDrawable) EXTRA_IMAGE.getDrawable()).getBitmap();
    }

    private  String createFileName(){
        return "FILE" + (new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date())) + ".jpeg";
    }
}
