package com.yosishay.wedding_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;


public class faq extends AppCompatActivity {

    Toolbar toolbar;
    int numberofquestions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        // Initialize the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //check if the user is logged him and write the name in the title
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String name_user = sharedPreferences.getString("userName", "");
        toolbar.setTitle("hello "+name_user);

        CardView card1 = findViewById(R.id.card1);
        CardView card2 = findViewById(R.id.card2);
        CardView card3 = findViewById(R.id.card3);
        CardView card4 = findViewById(R.id.card4);
        CardView card5 = findViewById(R.id.card5);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toogleText((CardView) v);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toogleText((CardView) v);
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toogleText((CardView) v);
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toogleText((CardView) v);
            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toogleText((CardView) v);
            }
        });






    }

    private void toogleText(CardView card){
        TextView detailsTextView = null;
        ImageView image1 = null;
        ImageView image2 = null;
        switch (card.getId()) {
            case R.id.card1:
                detailsTextView = findViewById(R.id.answer1);
                image1 = findViewById(R.id.arrowquestion1);
                image2= findViewById(R.id.arrowupquestion1);
                break;
            case R.id.card2:
                detailsTextView = findViewById(R.id.answer2);
                image1 = findViewById(R.id.arrowquestion2);
                image2= findViewById(R.id.arrowupquestion2);
                break;
            case R.id.card3:
                detailsTextView = findViewById(R.id.answer3);
                image1 = findViewById(R.id.arrowquestion3);
                image2= findViewById(R.id.arrowupquestion3);
                break;
            case R.id.card4:
                detailsTextView = findViewById(R.id.answer4);
                image1 = findViewById(R.id.arrowquestion4);
                image2= findViewById(R.id.arrowupquestion4);
                break;
            case R.id.card5:
                detailsTextView = findViewById(R.id.answer5);
                image1 = findViewById(R.id.arrowquestion5);
                image2= findViewById(R.id.arrowupquestion5);
                break;
        }
        if(detailsTextView!=null){
            int visibilitytext = detailsTextView.getVisibility();
            detailsTextView.setVisibility(visibilitytext == View.VISIBLE ? View.GONE : View.VISIBLE);
        }
        if(image1!=null){
            int visibility = image1.getVisibility();
            image1.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
        }
        if(image2!=null){
            int visibility = image1.getVisibility();
            image2.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (itemId) {
            case R.id.login:
                // Start LoginActivity
                Intent loginIntent = new Intent(faq.this, login.class);
                startActivity(loginIntent);
                return true;
            case R.id.djs:
                //set the next page we going to move in is dj for self activity to display it
                editor.putString("Display", "dj");
                editor.apply();
                // Start self activity with dj values
                Intent djsIntent = new Intent(faq.this, self.class);
                startActivity(djsIntent);
                return true;
            case R.id.photographer:
                //set the next page we going to move in is photographer for self activity to display it
                editor.putString("Display", "photographer");
                editor.apply();
                // Start self activity with photographer values
                Intent photographerIntent = new Intent(faq.this, self.class);
                startActivity(photographerIntent);
                return true;
            case R.id.contant:
                // Start contact activity
                Intent contant = new Intent(faq.this, contact.class);
                startActivity(contant);
                return true;
            case R.id.register:
                // Start Register activity
                Intent register = new Intent(faq.this, Register.class);
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
                Intent place = new Intent(faq.this, self.class);
                startActivity(place);
                return true;
            case R.id.faq:
                // Start faq activity
                Intent faq = new Intent(faq.this, faq.class);
                startActivity(faq);
                return true;
            case R.id.home:
                // Start home activity
                Intent home = new Intent(faq.this, MainActivity.class);
                startActivity(home);
                return true;
            case R.id.preferences:
                //check if user is connected
                String name_user = sharedPreferences.getString("userName", "");
                if(!name_user.equals("")){
                    // Start preference activity
                    Intent preference = new Intent(faq.this, Preferences.class);
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
                        Intent main = new Intent(faq.this, MainActivity.class);
                        startActivity(main);
                        Toast.makeText(faq.this,"התנתקת בהצלחה",Toast.LENGTH_LONG).show();
                    })
                    .setNegativeButton("לא", null) // User canceled logout, do nothing
                    .show();
        else {
            Toast.makeText(faq.this,"אינך מחובר",Toast.LENGTH_LONG).show();
        }
    }


}