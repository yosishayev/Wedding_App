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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class addSupplier extends AppCompatActivity {
    EditText editTextName;
    EditText editTextPhone;
    EditText editTextUrl;
    Spinner profSpinner;
    ArrayAdapter<String> profAdapter;
    String[] profOptions = {"dj", "photographer", "place"};
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supplier);

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

        //init spinner values in addSupplier activity
        profSpinner = findViewById(R.id.prof);
        profAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, profOptions);
        profAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profSpinner.setAdapter(profAdapter);

        editTextName = findViewById(R.id.editTextTextPersonName2);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextUrl = findViewById(R.id.img_url);

        Button buttonRegister = findViewById(R.id.button6);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the values from the views and keep it
                String name = editTextName.getText().toString();
                String phone = editTextPhone.getText().toString();
                String prof = profSpinner.getSelectedItem().toString();
                String url = editTextUrl.getText().toString();
                //connect to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference suppliersRef = database.getReference("Suppliers");
                //init supplier count for make unique key for different suppliers
                DatabaseReference supplierCountRef = database.getReference("SupplierCount");
                supplierCountRef.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Integer supplierCount = mutableData.getValue(Integer.class);
                        if (supplierCount == null) {
                            // First supplier, initialize the count to 1
                            mutableData.setValue(1);
                        } else {
                            // Increment the count and update the value
                            supplierCount++;
                            mutableData.setValue(supplierCount);
                        }
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                        if (committed) {
                            // Get the updated supplier count
                            Integer supplierCount = dataSnapshot.getValue(Integer.class);
                            if (supplierCount != null) {
                                // Generate the child name for the new supplier
                                String childName = "Supplier" + supplierCount;

                                // Create a new supplier reference under the generated child name
                                DatabaseReference newSupplierRef = suppliersRef.child(childName);

                                // Set the value of the new supplier under the generated child name
                                newSupplierRef.setValue(new Supplier(name, phone, prof, url));

                                Toast.makeText(addSupplier.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(addSupplier.this, "Failed to retrieve supplier count.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(addSupplier.this, "Failed to increment supplier count.", Toast.LENGTH_SHORT).show();
                        }
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
                Intent loginIntent = new Intent(addSupplier.this, login.class);
                startActivity(loginIntent);
                return true;
            case R.id.djs:
                //set the next page we going to move in is dj for self activity to display it
                editor.putString("Display", "dj");
                editor.apply();
                // Start self activity with dj values
                Intent djsIntent = new Intent(addSupplier.this, self.class);
                startActivity(djsIntent);
                return true;
            case R.id.photographer:
                //set the next page we going to move in is photographer for self activity to display it
                editor.putString("Display", "photographer");
                editor.apply();
                // Start self activity with photographer values
                Intent photographerIntent = new Intent(addSupplier.this, self.class);
                startActivity(photographerIntent);
                return true;
            case R.id.contant:
                // Start contact activity
                Intent contant = new Intent(addSupplier.this, contact.class);
                startActivity(contant);
                return true;
            case R.id.register:
                // Start Register activity
                Intent register = new Intent(addSupplier.this, Register.class);
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
                Intent place = new Intent(addSupplier.this, self.class);
                startActivity(place);
                return true;
            case R.id.faq:
                // Start faq activity
                Intent faq = new Intent(addSupplier.this, faq.class);
                startActivity(faq);
                return true;
            case R.id.home:
                // Start home activity
                Intent home = new Intent(addSupplier.this, MainActivity.class);
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
                        Intent main = new Intent(addSupplier.this, MainActivity.class);
                        startActivity(main);
                        Toast.makeText(addSupplier.this,"התנתקת בהצלחה",Toast.LENGTH_LONG).show();
                    })
                    .setNegativeButton("לא", null) // User canceled logout, do nothing
                    .show();
        else {
            Toast.makeText(addSupplier.this,"אינך מחובר",Toast.LENGTH_LONG).show();
        }
    }
}
