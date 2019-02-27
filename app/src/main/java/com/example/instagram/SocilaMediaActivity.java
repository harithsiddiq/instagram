package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SocilaMediaActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabAdapter tabAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socila_media);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);




        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);


        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager,false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.postimageitem:
                if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE},  3000);
                } else {
                    captureImage();
                }
                break;
            case R.id.logoutuser:
                final ProgressDialog dialog = new ProgressDialog(SocilaMediaActivity.this);
                dialog.setMessage("Logged out");
                dialog.show();
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        dialog.dismiss();
                        finish();
                    }
                });

        }
        return super.onOptionsItemSelected(item);
    }

    private void captureImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 4000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4000 && resultCode == RESULT_OK) {
            try {
                Uri capturedImage = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), capturedImage);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();

                ParseFile file = new ParseFile("img.png", bytes);

                ParseObject parseObject = new ParseObject("Photo");
                parseObject.put("picture", file);
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                final ProgressDialog dialog = new ProgressDialog(SocilaMediaActivity.this);
                dialog.setMessage("Loading");
                dialog.setCancelable(false);
                dialog.show();

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            FancyToast.makeText(getApplicationContext(), "Uploaded success", FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show();
                        } else {
                            FancyToast.makeText(getApplicationContext(), "Error while uploading", FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show();
                        }
                        dialog.dismiss();

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 3000) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                captureImage();

            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

