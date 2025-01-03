package com.example.test3hit.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.test3hit.R;
import com.example.test3hit.activities.MainActivity;

public class Fragment2 extends Fragment {

    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_2, container, false);

        EditText emailEditText = v.findViewById(R.id.emailEditText);
        EditText passwordEditText = v.findViewById(R.id.passwordEditText);
        EditText rePasswordEditText = v.findViewById(R.id.rePasswordEditText);
        Button registerButton = v.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String rePassword = rePasswordEditText.getText().toString().trim();

                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.validateAndRegister(email, password, rePassword, new MainActivity.RegisterCallback() {
                    @Override
                    public void onRegisterResult(boolean success) {
                        if (success) {
                            Navigation.findNavController(view).navigate(R.id.action_fragment2_to_fragment1);
                        }
                    }
                });
            }
        });

        return v;
    }
}