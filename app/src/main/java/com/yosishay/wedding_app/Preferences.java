package com.yosishay.wedding_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;

public class Preferences extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    ArrayList<String> namesList; // List to store supplier names
    ArrayList<String> urlsList; // List to store image URLs
    ArrayList<String>  suppliers;
    ArrayList<String> profList; // List to store prof job

    ArrayList<String> phoneList; // List to store phone numbers

    ArrayList<Supplier> supList; // List to store suppliers
    ArrayList<String> childList; // List to store suppliers

    ArrayList<String> ratingList; // List to store ratings


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // Initialize the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //check if the user is logged and write the name in the title
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String name_user = sharedPreferences.getString("userName", "");

        //check the phone of the user that logged in (if no user log in it will be empty string)
        String phone = sharedPreferences.getString("phone", "");
        toolbar.setTitle("hello "+name_user);

        // Call the method to retrieve supplier information from the database
        retrievePreferencesInformation(phone);

        listView = findViewById(R.id.listView1);
        //add the suppliers to preference page
        addSuppliers();




    }

    private void addSuppliers() {
        //connect to database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Retrieve suppliers information from the database
        DatabaseReference SuppliersList = database.getReference("Suppliers");

        namesList = new ArrayList<>();
        urlsList = new ArrayList<>();
        phoneList = new ArrayList<>();
        profList = new ArrayList<>();
        supList = new ArrayList<>();
        ratingList = new ArrayList<>();
        childList = new ArrayList<>();

        SuppliersList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot supplierSnapshot : dataSnapshot.getChildren()) {
                    Supplier supplier = supplierSnapshot.getValue(Supplier.class);
                    for (String supplierInList : suppliers){
                        if (supplierSnapshot.getKey().equals(supplierInList)){
                            namesList.add(supplier.getName());
                            urlsList.add(supplier.getUrl());
                            phoneList.add(supplier.getPhone());
                            profList.add(supplier.getProf());
                            ratingList.add(supplier.getAvg());
                            supList.add(supplier);
                        }
                    }
                }

                // Create and set the adapter with the retrieved data
                CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), namesList.toArray(new String[0]),ratingList.toArray(new String[0]), urlsList.toArray(new String[0]));
                listView.setAdapter(customBaseAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Preferences.this, "Database error occurred.", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void retrievePreferencesInformation(String userPhone) {
        //connect to database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Retrieve users information from the database
        DatabaseReference preference = database.getReference("Users");

        //check if there are changes in the database
        preference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String phoneUser = userSnapshot.child("phone").getValue(String.class);
                        if(phoneUser.equals(userPhone)){
                            User user = userSnapshot.getValue(User.class);
                            suppliers = user.getSuppliers();
                        }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Preferences.this, "Database error occurred.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (itemId) {
            case R.id.login:
                // Start LoginActivity
                Intent loginIntent = new Intent(Preferences.this, login.class);
                startActivity(loginIntent);
                return true;
            case R.id.djs:
                //set the next page we going to move in is dj for self activity to display it
                editor.putString("Display", "dj");
                editor.apply();
                // Start self activity with dj values
                Intent djsIntent = new Intent(Preferences.this, self.class);
                startActivity(djsIntent);
                return true;
            case R.id.photographer:
                //set the next page we going to move in is photographer for self activity to display it
                editor.putString("Display", "photographer");
                editor.apply();
                // Start self activity with photographer values
                Intent photographerIntent = new Intent(Preferences.this, self.class);
                startActivity(photographerIntent);
                return true;
            case R.id.contant:
                // Start contact activity
                Intent contant = new Intent(Preferences.this, contact.class);
                startActivity(contant);
                return true;
            case R.id.register:
                // Start Register activity
                Intent register = new Intent(Preferences.this, Register.class);
                startActivity(register);
                return true;
            case R.id.logout:
                //on logout reset username/isAdmin/phoneTitle
                showLogoutDialog();
                Intent main = new Intent(Preferences.this, MainActivity.class);
                startActivity(main);
                return true;
            case R.id.where:
                //set the next page we going to move in is place(locations) for self activity to display it
                editor.putString("Display", "place");
                editor.apply();
                Intent place = new Intent(Preferences.this, self.class);
                startActivity(place);
                return true;
            case R.id.faq:
                // Start faq activity
                Intent faq = new Intent(Preferences.this, faq.class);
                startActivity(faq);
                return true;
            case R.id.home:
                // Start home activity
                Intent home = new Intent(Preferences.this, MainActivity.class);
                startActivity(home);
                return true;
            case R.id.preferences:
                //check if user is connected
                String name_user = sharedPreferences.getString("userName", "");
                if(!name_user.equals("")){
                    // Start preference activity
                    Intent preference = new Intent(Preferences.this, Preferences.class);
                    startActivity(preference);
                }
                else{
                    Toast.makeText(this, "עלייך להתחבר על מנת לשמור העדפות", Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
                        Intent main = new Intent(Preferences.this, MainActivity.class);
                        startActivity(main);
                        Toast.makeText(Preferences.this,"התנתקת בהצלחה",Toast.LENGTH_LONG).show();
                    })
                    .setNegativeButton("לא", null) // User canceled logout, do nothing
                    .show();
        else {
            Toast.makeText(Preferences.this,"אינך מחובר",Toast.LENGTH_LONG).show();
        }
    }
}

