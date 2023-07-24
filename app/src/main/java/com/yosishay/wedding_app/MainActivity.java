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
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        moveimg();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start faq activity
                Intent intent = new Intent(MainActivity.this, faq.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set the next page we going to move in is dj for self activity to display it
                editor.putString("Display", "dj");
                editor.apply();
                // Start self activity
                Intent intent = new Intent(MainActivity.this, self.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set the next page we going to move in is photographer for self activity to display it
                editor.putString("Display", "photographer");
                editor.apply();
                // Start self activity
                Intent intent = new Intent(MainActivity.this, self.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set the next page we going to move in is place(locations) for self activity to display it
                editor.putString("Display", "place");
                editor.apply();
                // Start self activity
                Intent intent = new Intent(MainActivity.this, self.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.arch_wedding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //move to videoActivity
                Intent intent = new Intent(MainActivity.this, video.class);
                startActivity(intent);
            }
        });

    }
    public void moveimg() {
        //animation of the ring (moving)
        ImageView aniView = (ImageView) findViewById(R.id.imageView);
        ObjectAnimator mover = ObjectAnimator.ofFloat(aniView, "translationY", -1200f, 0f);
        mover.setDuration(1000);

        ////animation of the ring showing after was Clear
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(aniView, "alpha", 0f, 1f);
        fadeIn.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeIn).with(mover);


        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                int[] imageViewIds = {R.id.imageView18, R.id.imageView19, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView8,
                        R.id.imageView10, R.id.imageView11, R.id.imageView12, R.id.imageView13, R.id.imageView14, R.id.imageView15,
                        R.id.imageView16, R.id.imageView17, R.id.imageView18, R.id.imageView19, R.id.imageView20, R.id.imageView21, R.id.imageView22}; // Add all your ImageView IDs

                // Loop through the ImageViews and create sequential animations of steps
                for (int i = 0; i < imageViewIds.length; i++) {
                    final ImageView imageView = findViewById(imageViewIds[i]);
                    float startRotation = imageView.getRotation();

                    ObjectAnimator shakeAnimator = ObjectAnimator.ofFloat(imageView, "rotation",
                            startRotation, startRotation + 15f, startRotation - 15f, startRotation + 15f, startRotation - 15f, startRotation);
                    shakeAnimator.setDuration(100);
                    shakeAnimator.setRepeatCount(3);
                    shakeAnimator.setStartDelay(i * 550);
                    shakeAnimator.start();
                }

                // Create the scale animation for arch_wedding ImageView
                final ImageView archImageView = findViewById(R.id.arch_wedding);
                final int scaleDuration = 2000; // Duration of the scale animation in milliseconds

                // Set the initial visibility of arch_wedding ImageView to INVISIBLE
                archImageView.setVisibility(View.INVISIBLE);

                // Create the scale animation
                ValueAnimator scaleAnimator = ValueAnimator.ofFloat(0f, 1f); // Scale from 0 to 1
                scaleAnimator.setDuration(scaleDuration);
                scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float scaleValue = (float) animation.getAnimatedValue();
                        archImageView.setScaleX(scaleValue);
                        archImageView.setScaleY(scaleValue);
                    }
                });
                scaleAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        // Change the visibility of arch_wedding ImageView to VISIBLE when the scale animation starts
                        archImageView.setVisibility(View.VISIBLE);
                    }
                });

                // Start the scale animation after the previous animations
                scaleAnimator.setStartDelay(imageViewIds.length * 550); // Delay based on the number of ImageViews and their start delays
                scaleAnimator.start();
            }
        });

        animatorSet.start();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (itemId) {
            case R.id.login:
                // Start LoginActivity
                Intent loginIntent = new Intent(MainActivity.this, login.class);
                startActivity(loginIntent);
                return true;
            case R.id.djs:
                //set the next page we going to move in is dj for self activity to display it
                editor.putString("Display", "dj");
                editor.apply();
                // Start self activity with dj values
                Intent djsIntent = new Intent(MainActivity.this, self.class);
                startActivity(djsIntent);
                return true;
            case R.id.photographer:
                //set the next page we going to move in is photographer for self activity to display it
                editor.putString("Display", "photographer");
                editor.apply();
                // Start self activity with photographer values
                Intent photographerIntent = new Intent(MainActivity.this, self.class);
                startActivity(photographerIntent);
                return true;
            case R.id.contant:
                // Start contact activity
                Intent contant = new Intent(MainActivity.this, contact.class);
                startActivity(contant);
                return true;
            case R.id.register:
                // Start Register activity
                Intent register = new Intent(MainActivity.this, Register.class);
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
                Intent place = new Intent(MainActivity.this, self.class);
                startActivity(place);
                return true;
            case R.id.faq:
                // Start faq activity
                Intent faq = new Intent(MainActivity.this, faq.class);
                startActivity(faq);
                return true;
            case R.id.home:
                // Start home activity
                Intent home = new Intent(MainActivity.this, MainActivity.class);
                startActivity(home);
                return true;
            case R.id.preferences:
                //check if user is connected
                String name_user = sharedPreferences.getString("userName", "");
                if(!name_user.equals("")){
                    // Start preference activity
                    Intent preference = new Intent(MainActivity.this, Preferences.class);
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
                        Intent main = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(main);
                        Toast.makeText(MainActivity.this,"התנתקת בהצלחה",Toast.LENGTH_LONG).show();
                    })
                    .setNegativeButton("לא", null) // User canceled logout, do nothing
                    .show();
        else {
            Toast.makeText(MainActivity.this,"אינך מחובר",Toast.LENGTH_LONG).show();
        }
    }
}