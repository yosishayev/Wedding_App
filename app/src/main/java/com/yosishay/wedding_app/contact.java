package com.yosishay.wedding_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class contact extends AppCompatActivity {
Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

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

        //get the values from the views and send sms to our phone(developers)
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText msgEdit = findViewById(R.id.editTextTextEmailAddress);
                String msgStr = msgEdit.getText().toString();
                EditText mail = findViewById(R.id.editTextTextPersonName);
                String msgstr = msgStr + " " + mail.getText().toString();
                sendSMS("0526560257", msgstr);
            }

            // This method sends an SMS message to the specified phone number.
            public void sendSMS(String phoneNo, String msg) {
                try {
                    // Get the default SMS manager instance.
                    SmsManager smsManager = SmsManager.getDefault();

                    // Send the SMS with the given message to the specified phone number.
                    smsManager.sendTextMessage(phoneNo, null, msg, null, null);

                    // Show a toast message indicating that the SMS has been sent successfully.
                    Toast.makeText(getApplicationContext(), "ההודעה נשלחה, ניצור איתך קשר בהקדם.",
                            Toast.LENGTH_LONG).show();

                    // After sending the SMS successfully, navigate to the MainActivity (login screen).
                    Intent loginIntent = new Intent(contact.this, MainActivity.class);
                    startActivity(loginIntent);
                } catch (Exception ex) {
                    // If an exception occurs during the process, show a toast message with the error.
                    Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
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
                Intent loginIntent = new Intent(contact.this, login.class);
                startActivity(loginIntent);
                return true;
            case R.id.djs:
                //set the next page we going to move in is dj for self activity to display it
                editor.putString("Display", "dj");
                editor.apply();
                // Start self activity with dj values
                Intent djsIntent = new Intent(contact.this, self.class);
                startActivity(djsIntent);
                return true;
            case R.id.photographer:
                //set the next page we going to move in is photographer for self activity to display it
                editor.putString("Display", "photographer");
                editor.apply();
                // Start self activity with photographer values
                Intent photographerIntent = new Intent(contact.this, self.class);
                startActivity(photographerIntent);
                return true;
            case R.id.contant:
                // Start contact activity
                Intent contant = new Intent(contact.this, contact.class);
                startActivity(contant);
                return true;
            case R.id.register:
                // Start Register activity
                Intent register = new Intent(contact.this, Register.class);
                startActivity(register);
                return true;
            case R.id.logout:
                //on logout reset username/isAdmin/phoneTitle
                showLogoutDialog();
                Intent main = new Intent(contact.this, MainActivity.class);
                startActivity(main);
                return true;
            case R.id.where:
                //set the next page we going to move in is place(locations) for self activity to display it
                editor.putString("Display", "place");
                editor.apply();
                Intent place = new Intent(contact.this, self.class);
                startActivity(place);
                return true;
            case R.id.faq:
                // Start faq activity
                Intent faq = new Intent(contact.this, faq.class);
                startActivity(faq);
                return true;
            case R.id.home:
                // Start home activity
                Intent home = new Intent(contact.this, MainActivity.class);
                startActivity(home);
                return true;
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
                        Toast.makeText(contact.this,"התנתקת בהצלחה",Toast.LENGTH_LONG).show();
                    })
                    .setNegativeButton("לא", null) // User canceled logout, do nothing
                    .show();
        else {
            Toast.makeText(contact.this,"אינך מחובר",Toast.LENGTH_LONG).show();
        }
    }
}
