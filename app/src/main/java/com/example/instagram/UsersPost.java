package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UsersPost extends AppCompatActivity {
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_post);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Intent i = getIntent();
        String receveUsername =  i.getStringExtra("name");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(receveUsername);

        linearLayout = findViewById(R.id.linear);




        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username", receveUsername);
        parseQuery.orderByAscending("createdAt");

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                for (ParseObject user : objects) {
                    final TextView postDescription = new TextView(UsersPost.this);
                    postDescription.setText(user.get("image_des")+"");
                    ParseFile postpicture = (ParseFile) user.get("picture");
                    postpicture.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {

                            if (data != null && e == null) {

                                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                ImageView postImageView = new ImageView(UsersPost.this);

                                LinearLayout.LayoutParams ImageView_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 450);
                                ImageView_params.setMargins(5,5,5,5);

                                postImageView.setLayoutParams(ImageView_params);
                                postImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                postImageView.setImageBitmap(bitmap);

                                LinearLayout.LayoutParams desc_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                desc_params.setMargins(5,5,5,5);
                                postDescription.setLayoutParams(desc_params);
                                postDescription.setGravity(Gravity.CENTER);
                                postDescription.setTextColor(Color.BLACK);
                                postDescription.setBackgroundColor(Color.LTGRAY);
                                postDescription.setTextSize(30f);
                                postDescription.setFontFeatureSettings(String.valueOf(R.font.oleo_script_swash_caps));

                                linearLayout.addView(postImageView);
                                linearLayout.addView(postDescription);
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                                finish();
                            }

                        }
                    });
                }

            }
        });



    }
}
