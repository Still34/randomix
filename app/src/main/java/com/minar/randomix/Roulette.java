package com.minar.randomix;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class Roulette extends Fragment implements OnClickListener, View.OnLongClickListener, TextView.OnEditorActionListener {
    private List<String> options = new ArrayList<>();
    String currentOption = "";

    public Roulette() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_roulette, container, false);
        // Set the listener
        ImageView insert = (ImageView) v.findViewById(R.id.insertButton);
        ImageView delete = (ImageView) v.findViewById(R.id.deleteButton);
        ImageView spin = (ImageView) v.findViewById(R.id.buttonSpinRoulette);
        EditText textInsert = (EditText) v.findViewById(R.id.entryRoulette);

        insert.setOnClickListener(this);
        delete.setOnClickListener(this);
        delete.setOnLongClickListener(this);
        spin.setOnClickListener(this);
        spin.setOnLongClickListener(this);
        textInsert.setOnEditorActionListener(this);
        return v;
    }
    @Override
    public boolean onLongClick(View v) {
       Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
       LinearLayout optionsList = getView().findViewById(R.id.optionsListHorizontal);
       switch (v.getId()) {
           case R.id.deleteButton:
               // Start the animated vector drawable
               ImageView deleteAnimation = (ImageView) getView().findViewById(R.id.deleteButton);
               Drawable delete = deleteAnimation.getDrawable();
               if (delete instanceof Animatable) ((Animatable) delete).start();
               // Vibrate
               vib.vibrate(60);

               // Clear the options
               if (options.isEmpty()) return true;
               options.clear();
               optionsList.removeAllViews();
               break;

           case R.id.buttonSpinRoulette:
               TextView entry = getView().findViewById(R.id.entryRoulette);
               String option1 = getResources().getString(R.string.generic_option) + "1";
               String option2 = getResources().getString(R.string.generic_option) + "2";
               String option3 = getResources().getString(R.string.generic_option) + "3";
               // Vibrate
               vib.vibrate(60);

               // Insert three options manually and spin the roulette, or clear the options
               if(options.isEmpty()) {
                   entry.setText(option1);
                   InsertRouletteOption();
                   entry.setText(option2);
                   InsertRouletteOption();
                   entry.setText(option3);
                   InsertRouletteOption();
                   break;
               }
               else {
                   options.clear();
                   optionsList.removeAllViews();
                   break;
               }
       }
       return true;
    }
    @Override
    public void onClick(View v) {
        Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        LinearLayout optionsList = getView().findViewById(R.id.optionsListHorizontal);
        switch (v.getId()) {
            case R.id.deleteButton:
                // Start the animated vector drawable
                ImageView deleteAnimation = (ImageView) getView().findViewById(R.id.deleteButton);
                Drawable delete = deleteAnimation.getDrawable();
                if (delete instanceof Animatable) ((Animatable) delete).start();
                // Vibrate
                vib.vibrate(30);
                if (options.isEmpty()) return;
                options.remove(options.size() - 1);
                optionsList.removeView(getView().findViewById(options.size()));
                break;

            case R.id.insertButton:
                // Start the animated vector drawable
                ImageView insertAnimation = (ImageView) getView().findViewById(R.id.insertButton);
                Drawable insert = insertAnimation.getDrawable();
                if (insert instanceof Animatable) ((Animatable) insert).start();
                // Vibrate
                vib.vibrate(30);
                // Insert in both the list and the layout
                InsertRouletteOption();
                break;

            case R.id.buttonSpinRoulette:
                // Break the case if the list is empty to avoid crashes and null pointers
                if(options.isEmpty() || options.size() == 1) {
                    Toast.makeText(getContext(), getString(R.string.no_entry_roulette), Toast.LENGTH_SHORT).show();
                    break;
                }
                // Start the animated vector drawable, make the button unclickable during the execution
                final ImageView spinAnimation = (ImageView) getView().findViewById(R.id.buttonSpinRoulette);
                spinAnimation.setClickable(false);
                spinAnimation.setLongClickable(false);

                Drawable spin = spinAnimation.getDrawable();
                if (spin instanceof Animatable) ((Animatable) spin).start();

                // Vibrate
                vib.vibrate(30);
                Random ran = new Random();
                final int n = ran.nextInt(options.size());

                // Get the text view and set its value depending on n (using a delay)
                final TextView textViewResult = (TextView) getView().findViewById(R.id.resultRoulette);

                // Create the animations
                final Animation animIn = new AlphaAnimation(1.0f, 0.0f);
                animIn.setDuration(1500);
                textViewResult.startAnimation(animIn);
                final Animation animOut = new AlphaAnimation(0.0f, 1.0f);
                animOut.setDuration(1000);

                getView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textViewResult.setText(options.get(n));
                        textViewResult.startAnimation(animOut);
                        spinAnimation.setClickable(true);
                        spinAnimation.setLongClickable(true);
                    }
                }, 1500);
                break;
        }
    }

    // Handle the keyboard actions, like enter, done, send and so on.
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        LinearLayout optionsList = getView().findViewById(R.id.optionsListHorizontal);
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_SEND) {
            // Start the animated vector drawable
            ImageView insertAnimation = (ImageView) getView().findViewById(R.id.insertButton);
            Drawable insert = insertAnimation.getDrawable();
            if (insert instanceof Animatable) ((Animatable) insert).start();
            // Insert in both the list and the layout
            InsertRouletteOption();
            return true;
        }
        return false;
    }

    private void InsertRouletteOption() {
        LinearLayout optionsList = getView().findViewById(R.id.optionsListHorizontal);
        TextView entry = getView().findViewById(R.id.entryRoulette);
        // Delete the blank spaces between words and before and after them to avoid weird behaviors
        currentOption = entry.getText().toString().trim();
        currentOption = currentOption.replaceAll("\\s+"," ");
        // Return if the string entered is a duplicate
        if (options.contains(currentOption)) return;
        // Reset the text field eventually, it could contain whitespaces
        entry.setText("");
        // If the text field isn't empty, save the option in the list and create the preview
        if (currentOption.trim().length() > 0) {
            if (options.size() > 9) {
                Toast.makeText(getContext(), getString(R.string.too_much_entries_roulette), Toast.LENGTH_SHORT).show();
                return;
            }
            options.add(currentOption);
            TextView optionsListEntry = new TextView(getContext());
            optionsListEntry.setText(currentOption);
            // Other properties needed for a clean ui
            optionsListEntry.setBackgroundResource(R.drawable.rounded_corners_textview_bg);
            optionsListEntry.setPadding(22, 22, 24, 22);
            optionsListEntry.setTextSize(16);
            // Set margins using the layout params
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llp.setMargins(10, 0, 10, 0);
            optionsListEntry.setLayoutParams(llp);
            // Set an id
            optionsListEntry.setId(options.indexOf(currentOption));
            // Add the element to the linear layout
            optionsList.addView(optionsListEntry);
        }
    }

}
