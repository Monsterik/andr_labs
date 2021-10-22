package com.example.walkingnotes;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.walkingnotes.databinding.FragmentFirstBinding;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    SQLiteDatabase db;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        db = SQLiteDatabase.openDatabase("/data/data/com.example.walkingnotes/DB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.execSQL("CREATE TABLE IF NOT EXISTS notes (name TEXT, description TEXT)");
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Button> buttons = new ArrayList();
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.linar_layout);
        long size = DatabaseUtils.queryNumEntries(db,"notes");
        for(int i = 0; i < size; i++) {
            int buttonStyle = R.style.NoteButton;
            Button button = new Button(new ContextThemeWrapper(getContext(), buttonStyle), null, buttonStyle);
            //Button button = new Button(getView().getContext());




            Cursor c = db.rawQuery("SELECT ROWID,* FROM notes LIMIT 1 OFFSET " + i + ";", null);
            c.moveToLast();
            int rowid = (int) c.getLong(0);
            String name = c.getString(1);
            String description = c.getString(2);
            button.setId(rowid);

            int titleLength = name.length();
            int subtitleLength = description.length();
            Spannable span = new SpannableString(name + "\n" + description);
            span.setSpan(new RelativeSizeSpan(0.7f), titleLength, (titleLength + subtitleLength + 1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            button.setText(span);

            //button.setText(name);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("Id", rowid);
                    bundle.putString("Name", name);
                    bundle.putString("Description", description);

                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);

                    Toast.makeText(view.getContext(),
                            "Button clicked index = " + rowid, Toast.LENGTH_SHORT)
                            .show();
                }
            });
            buttons.add(button);
            layout.addView(button);
        }

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.execSQL("INSERT INTO notes(name, description) values ('Название', 'Описание');");

                int id = (int) DatabaseUtils.queryNumEntries(db,"notes");

                Bundle bundle = new Bundle();
                bundle.putInt("Id", id);

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);

                Toast.makeText(view.getContext(),
                        "Button add index = " + id, Toast.LENGTH_SHORT)
                        .show();

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}