package com.example.instagram;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePictureTab extends Fragment {

    ImageButton imageButton;
    EditText desc;
    Button share;
    Bitmap image;
    private static final int REQUEST_CODE = 2000;

    public SharePictureTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        imageButton = view.findViewById(R.id.imageButton);
        desc = view.findViewById(R.id.descript);
        share = view.findViewById(R.id.share);



        // open gallery

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
                } else {

                    getChosenImage();

                }

            }
        });

        // share button
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (image != null) {

                    if (desc.getText().toString().equals("")) {
                        FancyToast.makeText(getContext(),"add Description to image", FancyToast.LENGTH_LONG,FancyToast.INFO, false).show();
                    } else {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("imag.png",bytes);

                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture", parseFile);
                        parseObject.put("image_des", desc.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Loading...");
                        dialog.setCancelable(false);
                        dialog.show();

                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null) {
                                    FancyToast.makeText(getContext(),"Done !", FancyToast.LENGTH_LONG,FancyToast.SUCCESS, false).show();
                                } else {
                                    FancyToast.makeText(getContext(),"Unknown error", FancyToast.LENGTH_LONG,FancyToast.ERROR, false).show();
                                }
                                dialog.dismiss();

                            }
                        });


                    }

                } else {
                    FancyToast.makeText(getContext(),"you must select an image", FancyToast.LENGTH_LONG,FancyToast.INFO, false).show();
                }

            }
        });

        return view;
    }

    public void getChosenImage() {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,2000);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getChosenImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    image = BitmapFactory.decodeFile(picturePath);
                    imageButton.setImageBitmap(image);

                }catch (Exception e) {

                }


            }

        }

    }
}
