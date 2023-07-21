package com.yosishay.wedding_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class dj extends AppCompatActivity {
    Toolbar toolbar;
    Supplier supplier;
    float rating;
    int voters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj);

        // Initialize the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //check if the user is logged him and write the name in the title
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String name_user = sharedPreferences.getString("userName", "");

        //check the phone of the user that logged in (if no user log in it will be empty string)
        String phone = sharedPreferences.getString("phone", "");
        toolbar.setTitle("hello "+name_user);

        //check with supplier is clicked by the Display that saved from previous page(self)
        String key = sharedPreferences.getString("key", "");

        //connect to database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference suppliersRef = database.getReference("Suppliers");
        //get the Reference of the supplier the fit to this key
        DatabaseReference supplierRef = suppliersRef.child(key);

        //initiate rating start with the colors and the values that write below
       final RatingBar simpleRatingBar = findViewById(R.id.ratingBar);
        simpleRatingBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFD700")));
        simpleRatingBar.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A5A690")));
        simpleRatingBar.setNumStars(5);
        simpleRatingBar.setRating(5f);




        supplierRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get the supplier from the Reference above
                supplier=dataSnapshot.getValue(Supplier.class);
                //get the image url
                ImageView imageView = findViewById(R.id.imageView);
                //load the url using Picasso
                Picasso.get().load(supplier.getUrl()).into(imageView);

                //get and load the name/professional/phone/rating
                TextView textView = findViewById(R.id.textView2);
                textView.setText(supplier.getName());
                textView = findViewById(R.id.textView3);
                textView.setText(supplier.getProf());
                textView = findViewById(R.id.textView4);
                textView.setText(supplier.getPhone());
                //check how many user vote
                voters= Integer.parseInt(supplier.getVoters());
                //get the total rating
                rating=Float.parseFloat(supplier.getAvg());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        Button submitButton = (Button) findViewById(R.id.button7);
        // perform click event on button for voting
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get all the phones that already vote
                ArrayList<String> temp= supplier.getPhones();

                //flag uses to get the state we in (logged in/logged out/already voted).
                int flag=0;

                //check if the phone is already votes if yes set the flag to be 1
                for(int i=0;i<temp.size();i++) {
                    if(temp.get(i).equals(phone))
                        flag=1;
                }
                //check if the user is not logged in
                if(phone.equals("")){
                    Toast.makeText(dj.this,"עליך להתחבר כדי להצביע.",Toast.LENGTH_LONG).show();
                    flag=2;
                }
                //check if the user is logged in and did not voted yet
                else if(flag==0) {
                    //set new avg by simple calculation of avg
                    supplier.setAvg(Float.toString((rating * voters + simpleRatingBar.getRating()) / (voters + 1)));

                    //set the number of voter to be 1 above before
                    supplier.setVoters(Integer.toString(voters + 1));

                    //add the phone to the users that already vote
                    temp.add(phone);

                    //update the data on the firebase
                    supplierRef.setValue(supplier);
                    Toast.makeText(dj.this,"הצבעתך נקלטה בהצלחה",Toast.LENGTH_LONG).show();
                }
                else if(flag==1){
                    Toast.makeText(dj.this,"לא ניתן להצביע פעמיים לאותו הספק",Toast.LENGTH_LONG).show();
                }
                // get values and then displayed in a toast

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (itemId) {
            case R.id.login:
                // Start LoginActivity
                Intent loginIntent = new Intent(dj.this, login.class);
                startActivity(loginIntent);
                return true;
            case R.id.djs:
                //set the next page we going to move in is dj for self activity to display it
                editor.putString("Display", "dj");
                editor.apply();
                // Start self activity with dj values
                Intent djsIntent = new Intent(dj.this, self.class);
                startActivity(djsIntent);
                return true;
            case R.id.photographer:
                //set the next page we going to move in is photographer for self activity to display it
                editor.putString("Display", "photographer");
                editor.apply();
                // Start self activity with photographer values
                Intent photographerIntent = new Intent(dj.this, self.class);
                startActivity(photographerIntent);
                return true;
            case R.id.contant:
                // Start contact activity
                Intent contant = new Intent(dj.this, contact.class);
                startActivity(contant);
                return true;
            case R.id.register:
                // Start Register activity
                Intent register = new Intent(dj.this, Register.class);
                startActivity(register);
                return true;
            case R.id.logout:
                //on logout reset username/isAdmin/phoneTitle
                showLogoutDialog();
                return true;
            case R.id.where:
                //set the next page we going to move in is place(locations) for self activity to display it
                editor.putString("Display", "place");
                editor.apply();
                Intent place = new Intent(dj.this, self.class);
                startActivity(place);
                return true;
            case R.id.faq:
                // Start faq activity
                Intent faq = new Intent(dj.this, faq.class);
                startActivity(faq);
                return true;
            case R.id.home:
                // Start home activity
                Intent home = new Intent(dj.this, MainActivity.class);
                startActivity(home);
                return true;
            case R.id.preferences:
                //check if user is connected
                String name_user = sharedPreferences.getString("userName", "");
                if(!name_user.equals("")){
                    // Start preference activity
                    Intent preference = new Intent(dj.this, Preferences.class);
                    startActivity(preference);
                }
                else{
                    Toast.makeText(this, "עלייך להתחבר על מנת לשמור העדפות", Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        //set the go back button
        onBackPressed();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //create the menu for the toolbar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example, menu);
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        //when resume to page check which user is logged in.
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String name_user = sharedPreferences.getString("userName", "");
        if(name_user.length()>0)
            toolbar.setTitle("Hello " + name_user);
        else
            toolbar.setTitle("");
    }
    private void showLogoutDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String name_user = sharedPreferences.getString("userName", "");
        if(!name_user.equals(""))
            new AlertDialog.Builder(this)

                    .setTitle("התנתקות")
                    .setMessage("האם ברצונך להתנתק?")
                    .setPositiveButton("כן", (dialog, which) -> {
                        // User confirmed logout, perform logout actions
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userName", "");
                        editor.putString("isAdmin", "");
                        editor.putString("phone", "");
                        editor.apply();
                        toolbar.setTitle("");
                        Intent main = new Intent(dj.this, MainActivity.class);
                        startActivity(main);
                        Toast.makeText(dj.this,"התנתקת בהצלחה",Toast.LENGTH_LONG).show();
                    })
                    .setNegativeButton("לא", null) // User canceled logout, do nothing
                    .show();
        else {
            Toast.makeText(dj.this,"אינך מחובר",Toast.LENGTH_LONG).show();
        }
    }
}