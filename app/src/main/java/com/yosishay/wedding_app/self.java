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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class self extends AppCompatActivity {
    ArrayList<String> namesList; // List to store supplier names
    ArrayList<String> urlsList; // List to store image URLs

    ArrayList<String> profList; // List to store prof job

    ArrayList<String> phoneList; // List to store phone numbers

    ArrayList<Supplier> supList; // List to store suppliers
    ArrayList<String> childList; // List to store suppliers

    ArrayList<String> ratingList; // List to store ratings

    ListView listView;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String name_user = sharedPreferences.getString("userName", "");
        toolbar.setTitle("hello "+name_user);

        listView = findViewById(R.id.listView1);

        // Call the method to retrieve supplier information from the database
        retrieveSupplierInformation();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Supplier", namesList.get(position)+" "+profList.get(position)+" "+phoneList.get(position)+" "+urlsList.get(position));
                editor.putString("key", childList.get(position));
                editor.apply();
                Intent intent = new Intent(self.this, dj.class);
                startActivity(intent);
            }
        });
    }

    // Method to retrieve supplier information from the database
    private void retrieveSupplierInformation() {
        // Retrieve the value of "Display" from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String displayValue = sharedPreferences.getString("Display", "");

        // Retrieve supplier information from the database and populate the lists
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference suppliersRef = database.getReference("Suppliers");

        suppliersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namesList = new ArrayList<>();
                urlsList = new ArrayList<>();
                phoneList = new ArrayList<>();
                profList = new ArrayList<>();
                supList = new ArrayList<>();
                ratingList = new ArrayList<>();
                childList = new ArrayList<>();

                //check if the supplier is the the profession on the button click before (toolbar/MainActivity)
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Supplier supplier = snapshot.getValue(Supplier.class);
                    if (supplier != null && supplier.getProf().equals(displayValue)) {
                        //keep the values of the supplier
                        childList.add(snapshot.getKey());
                        namesList.add(supplier.getName());
                        urlsList.add(supplier.getUrl());
                        phoneList.add(supplier.getPhone());
                        profList.add(supplier.getProf());
                        ratingList.add(supplier.getAvg());
                        supList.add(supplier);
                    }
                }

                // Create and set the adapter with the retrieved data
                CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), namesList.toArray(new String[0]),ratingList.toArray(new String[0]), urlsList.toArray(new String[0]));
                listView.setAdapter(customBaseAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if necessary
                Toast.makeText(self.this, "Database error occurred.", Toast.LENGTH_SHORT).show();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //save the key for the next page(dj)
                editor.putString("key", childList.get(position));
                editor.apply();
                Intent intent = new Intent(self.this, dj.class);
                startActivity(intent);
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
                Intent loginIntent = new Intent(self.this, login.class);
                startActivity(loginIntent);
                return true;
            case R.id.djs:
                //set the next page we going to move in is dj for self activity to display it
                editor.putString("Display", "dj");
                editor.apply();
                // Start self activity with dj values
                Intent djsIntent = new Intent(self.this, self.class);
                startActivity(djsIntent);
                return true;
            case R.id.photographer:
                //set the next page we going to move in is photographer for self activity to display it
                editor.putString("Display", "photographer");
                editor.apply();
                // Start self activity with photographer values
                Intent photographerIntent = new Intent(self.this, self.class);
                startActivity(photographerIntent);
                return true;
            case R.id.contant:
                // Start contact activity
                Intent contant = new Intent(self.this, contact.class);
                startActivity(contant);
                return true;
            case R.id.register:
                // Start Register activity
                Intent register = new Intent(self.this, Register.class);
                startActivity(register);
                return true;
            case R.id.logout:
                //on logout reset username/isAdmin/phoneTitle
                showLogoutDialog();
                Intent main = new Intent(self.this, MainActivity.class);
                startActivity(main);
                return true;
            case R.id.where:
                //set the next page we going to move in is place(locations) for self activity to display it
                editor.putString("Display", "place");
                editor.apply();
                Intent place = new Intent(self.this, self.class);
                startActivity(place);
                return true;
            case R.id.faq:
                // Start faq activity
                Intent faq = new Intent(self.this, faq.class);
                startActivity(faq);
                return true;
            case R.id.home:
                // Start home activity
                Intent home = new Intent(self.this, MainActivity.class);
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
                        Toast.makeText(self.this,"התנתקת בהצלחה",Toast.LENGTH_LONG).show();
                    })
                    .setNegativeButton("לא", null) // User canceled logout, do nothing
                    .show();
        else {
            Toast.makeText(self.this,"אינך מחובר",Toast.LENGTH_LONG).show();
        }
    }
}