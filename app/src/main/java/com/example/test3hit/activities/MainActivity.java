package com.example.test3hit.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test3hit.R;
import com.example.test3hit.fragments.Fragment3;
import com.example.test3hit.fragments.Fragment1;
import com.example.test3hit.models.ShoppingItem;
import com.example.test3hit.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
// ...

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }



    public void login(String email, String password, LoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Save email and password to SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.apply();

                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            callback.onLoginResult(true);
                        } else {
                            Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                            callback.onLoginResult(false);
                        }
                    }
                });
    }

    public interface LoginCallback {
        void onLoginResult(boolean success);
    }

    public void validateAndRegister(String email, String password, String rePassword, RegisterCallback callback) {
        if (!password.equals(rePassword)) {
            Toast.makeText(MainActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            callback.onRegisterResult(false);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            callback.onRegisterResult(true);
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            callback.onRegisterResult(false);
                        }
                    }
                });
    }

    public interface RegisterCallback {
        void onRegisterResult(boolean success);
    }

    public void addData(String email, String phone) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-test-df050-default-rtdb.europe-west1.firebasedatabase.app/");
        // because we chose EU we need to pass the URL of the database
        DatabaseReference myRef = database.getReference("users").child(phone);

        User user = new User(email, phone);
        myRef.setValue(user);
    }
    public void readData(String phone) {
        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(phone);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.getValue(User.class);
                Toast.makeText(MainActivity.this, value.getEmail(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void saveShoppingListToDatabase(ArrayList<ShoppingItem> shoppingList, DatabaseReference databaseReference) {
        if (databaseReference != null) {
            databaseReference.setValue(shoppingList).addOnCompleteListener(task -> {
                if (task.isSuccessful() && !shoppingList.isEmpty()) {
                    Toast.makeText(this, "Shopping List saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Empty Shopping List", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "Database reference is null", Toast.LENGTH_SHORT).show();
        }
    }
}