package com.example.instagram;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {


    public ProfileTab() {
        // Required empty public constructor
    }

    EditText name, bio, prof, hobbies, fav;
    Button save;
    TextView userText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);


       save = view.findViewById(R.id.saveEdit);
       name = view.findViewById(R.id.profileName);
       bio = view.findViewById(R.id.bio);
       prof = view.findViewById(R.id.profession);
       hobbies = view.findViewById(R.id.hobbies);
       fav = view.findViewById(R.id.sport);
       userText = view.findViewById(R.id.username);




        final ParseUser user = ParseUser.getCurrentUser();

        userText.setText(user.get("username")+ "");
        name.setText(user.get("Profile")+ "");
        bio.setText(user.get("ProfileBio")+ "");
        prof.setText(user.get("Prssion")+ "");
        hobbies.setText(user.get("Hobbies")+ "");
        fav.setText(user.get("FavSport")+ "");

       save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                user.put("Profile", name.getText().toString());
                user.put("ProfileBio", bio.getText().toString());
                user.put("Prssion", prof.getText().toString());
                user.put("Hobbies", hobbies.getText().toString());
                user.put("FavSport", fav.getText().toString());

                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            FancyToast.makeText(getActivity(), "Added Success",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                        } else {
                            FancyToast.makeText(getActivity(), e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                        }
                    }
                });
           }
       });

       return view;
    }

}
