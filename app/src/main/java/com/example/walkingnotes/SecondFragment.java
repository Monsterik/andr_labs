package com.example.walkingnotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.walkingnotes.databinding.FragmentSecondBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    int id;
    String name;
    String description;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        Bundle arguments = getArguments();
        id = (Integer) arguments.get("Id");
        name = (String) arguments.get("Name");
        description = (String) arguments.get("Description");

        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.walkingnotes/DB", null, SQLiteDatabase.OPEN_READWRITE);

        binding.fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(),
                        "Button delete index = " + id, Toast.LENGTH_SHORT)
                        .show();

                db.delete("notes","ROWID=?",new String[]{String.valueOf(id)});
                db.close();

                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        binding.fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(),
                        "Button update index = " + id, Toast.LENGTH_SHORT)
                        .show();

                EditText editName =(EditText) getView().findViewById(R.id.editName);
                String name = editName.getText().toString();

                EditText editDescription =(EditText) getView().findViewById(R.id.editDescription);
                String description = editDescription.getText().toString();

                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("description", description);
                db.update("notes", values, "ROWID=" + id, null);

                db.close();

                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        binding.fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MapsActivity.class);
                startActivity(myIntent);
            }
        });

        return binding.getRoot();

    }





    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText editName =(EditText) getView().findViewById(R.id.editName);
        editName.setText(name, TextView.BufferType.EDITABLE);

        EditText editDesc =(EditText) getView().findViewById(R.id.editDescription);
        editDesc.setText(description, TextView.BufferType.EDITABLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}