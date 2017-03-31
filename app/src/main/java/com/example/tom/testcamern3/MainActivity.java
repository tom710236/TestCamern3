package com.example.tom.testcamern3;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CONTACTS = 1;
    Uri imgUri;
    ImageView imv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imv = (ImageView)findViewById(R.id.imageView);
    }
    public void onGet (View v) {
        int permission = ActivityCompat.checkSelfPermission(this,
                WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(this,
                READ_EXTERNAL_STORAGE);
        int permission3 = ActivityCompat.checkSelfPermission(this,
                CAMERA);
            //未取得權限，向使用者要求允許權限
            if (permission != PackageManager.PERMISSION_GRANTED && permission2 != PackageManager.PERMISSION_GRANTED && permission3 != PackageManager.PERMISSION_GRANTED) {
                //若尚未取得權限，則向使用者要求允許聯絡人讀取與寫入的權限，REQUEST_CONTACTS常數未宣告則請按下Alt+Enter自動定義常數值。
                ActivityCompat.requestPermissions(this,
                        new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA},
                        REQUEST_CONTACTS);
            } else {
                //已有權限，可進行檔案存取
                readContacts();
            }
        }


    private void readContacts() {
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES
        ).toString();

        String fname = "p" + System.currentTimeMillis()+".jpg";
        imgUri = Uri.parse("file://"+dir+"/"+fname);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,100);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case REQUEST_CONTACTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //取得聯絡人權限，進行存取
                    readContacts();
                } else {
                    //使用者拒絕權限，顯示對話框告知
                    new AlertDialog.Builder(this)
                            .setMessage("必須允許權限才能拍照")
                            .setPositiveButton("OK", null)
                            .show();
                }
                return;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && requestCode==100){

            Bundle extras = data.getExtras();
            Bitmap bmp = (Bitmap) extras.get("data");
            ImageView imv = (ImageView)findViewById(R.id.imageView);
            imv.setImageBitmap(bmp);


            //Bitmap bmp = BitmapFactory.decodeFile(imgUri.getPath());
            //imv.setImageBitmap(bmp);
            //showImg();
        }else {
            Toast.makeText(this,"沒有拍到照片",Toast.LENGTH_SHORT).show();
        }
    }

}

