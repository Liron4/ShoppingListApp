package com.example.test3hit.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.test3hit.R;
import com.example.test3hit.activities.MainActivity;
import com.example.test3hit.models.ShoppingItem;
import com.example.test3hit.models.ShoppingListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class Fragment3 extends Fragment {

    private EditText newItemEditText;
    private ListView shoppingListView;
    private Button clearListButton;
    private ArrayList<ShoppingItem> shoppingList;
    private ShoppingListAdapter adapter;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public Fragment3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            databaseReference = FirebaseDatabase.getInstance("https://fir-test-df050-default-rtdb.europe-west1.firebasedatabase.app/").getReference("shoppingList").child(currentUser.getUid());
            retrieveShoppingList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, false);

        newItemEditText = view.findViewById(R.id.newItemEditText);
        shoppingListView = view.findViewById(R.id.shoppingListView);
        clearListButton = view.findViewById(R.id.clearListButton);

        adapter = new ShoppingListAdapter(getActivity(), shoppingList);
        shoppingListView.setAdapter(adapter);

        newItemEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String itemName = newItemEditText.getText().toString().trim();
                    if (!itemName.isEmpty()) {
                        ShoppingItem newItem = new ShoppingItem(itemName, false);
                        shoppingList.add(newItem);
                        adapter.notifyDataSetChanged();
                        newItemEditText.setText("");
                    } else {
                        Toast.makeText(getActivity(), "Item name cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        clearListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.saveShoppingListToDatabase(shoppingList, databaseReference);
        }
    }

    private void retrieveShoppingList() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shoppingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ShoppingItem item = snapshot.getValue(ShoppingItem.class);
                    shoppingList.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to load shopping list.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}