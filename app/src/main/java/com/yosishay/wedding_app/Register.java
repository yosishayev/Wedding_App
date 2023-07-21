package com.yosishay.wedding_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    EditText editTextName;
    EditText editTextPassword;
    EditText editTextPhone;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        // Get the password , phone and name information from the views
        editTextName = findViewById(R.id.editTextTextPersonName2);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        editTextPhone = findViewById(R.id.editTextPhone);

        Button buttonRegister = findViewById(R.id.button6);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String password = editTextPassword.getText().toString();
                String phone = editTextPhone.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference usersRef = database.getReference("Users");

                // Check if phone number already exists
                usersRef.orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Phone number already exists, show error message
                            Toast.makeText(Register.this, "Phone number already exists.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Phone number doesn't exist, proceed with registration
                            DatabaseReference userCountRef = database.getReference("UserCount");
                            userCountRef.runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData mutableData) {
                                    Integer userCount = mutableData.getValue(Integer.class);
                                    if (userCount == null) {
                                        // First user, initialize the count to 1
                                        mutableData.setValue(1);
                                    } else {
                                        // Increment the count and update the value
                                        userCount++;
                                        mutableData.setValue(userCount);
                                    }
                                    return Transaction.success(mutableData);
                                }

                                @Override
                                public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                                    if (committed) {
                                        // Get the updated user count
                                        Integer userCount = dataSnapshot.getValue(Integer.class);
                                        if (userCount != null) {
                                            // Generate the child name for the new user
                                            String childName = "User" + userCount;

                                            // Create a new user reference under the generated child name
                                            DatabaseReference newUserRef = usersRef.child(childName);

                                            // Set the value of the new user under the generated child name
                                            newUserRef.setValue(new User(name, phone, password));

                                            Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Register.this, "Failed to retrieve user count.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Register.this, "Failed to increment user count.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error if necessary
                        Toast.makeText(Register.this, "Database error occurred.", Toast.LENGTH_SHORT).show();
                    }
                });
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
                Intent loginIntent = new Intent(Register.this, login.class);
                startActivity(loginIntent);
                return true;
            case R.id.djs:
                //set the next page we going to move in is dj for self activity to display it
                editor.putString("Display", "dj");
                editor.apply();
                // Start self activity with dj values
                Intent djsIntent = new Intent(Register.this, self.class);
                startActivity(djsIntent);
                return true;
            case R.id.photographer:
                //set the next page we going to move in is photographer for self activity to display it
                editor.putString("Display", "photographer");
                editor.apply();
                // Start self activity with photographer values
                Intent photographerIntent = new Intent(Register.this, self.class);
                startActivity(photographerIntent);
                return true;
            case R.id.contant:
                // Start contact activity
                Intent contant = new Intent(Register.this, contact.class);
                startActivity(contant);
                return true;
            case R.id.register:
                // Start Register activity
                Intent register = new Intent(Register.this, Register.class);
                startActivity(register);
                return true;
            case R.id.logout:
                //on logout reset username/isAdmin/phoneTitle
                showLogoutDialog();
                Intent main = new Intent(Register.this, MainActivity.class);
                startActivity(main);
                return true;
            case R.id.where:
                //set the next page we going to move in is place(locations) for self activity to display it
                editor.putString("Display", "place");
                editor.apply();
                Intent place = new Intent(Register.this, self.class);
                startActivity(place);
                return true;
            case R.id.faq:
                // Start faq activity
                Intent faq = new Intent(Register.this, faq.class);
                startActivity(faq);
                return true;
            case R.id.home:
                // Start home activity
                Intent home = new Intent(Register.this, MainActivity.class);
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
                        Toast.makeText(Register.this,"התנתקת בהצלחה",Toast.LENGTH_LONG).show();
                    })
                    .setNegativeButton("לא", null) // User canceled logout, do nothing
                    .show();
        else {
            Toast.makeText(Register.this,"אינך מחובר",Toast.LENGTH_LONG).show();
        }
    }
}