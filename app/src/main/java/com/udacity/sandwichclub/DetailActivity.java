package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.w3c.dom.Text;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    //Views
    private TextView mOrigins;
    private TextView mAlsoKnownAs;
    private TextView mDescription;
    private TextView mIngredients;
    //Sandwich model to display information
    Sandwich sandwich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Get id's for views
        ImageView ingredientsIv = findViewById(R.id.image_iv); //Image
        mOrigins = findViewById(R.id.origin_tv); //Place of origin
        mDescription = findViewById(R.id.description_tv); //Description
        mAlsoKnownAs = findViewById(R.id.also_known_tv); //Also known as
        mIngredients = findViewById(R.id.ingredients_tv); //Ingredients


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        //Get information of clicked sandwich
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        //Parse JSON String to Sandwich model
        sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        //Display all information
        populateUI();
        //Load image
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        //Set title of app bar to the name of the clicked sandwich
        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "There was a problem", Toast.LENGTH_SHORT).show();
    }

    //Method to generate a string from a list of strings
    //Return "Not provided" in case of empty list (case 1)
    //Return "item1, item2, item3, item4." if list has a set of items (case 2)
    private String generateString(List<String> listOfStrings){
        //Check for (case 1)
        if(listOfStrings == null) return "   Not provided.";
        //(case 2)
        String concat = "   "; //Indentation
        //For each item concat it to a single string
        for(String item: listOfStrings){
            concat = concat + item + ", ";
        }
        //Remove last 2 characters ", " to replace it for a "."
        //To indicate end of string
        concat = concat.substring(0, concat.length()-2) + ".";
        return concat;
    }

    private void populateUI() {
        //If place of origin is empty return "Unknown" otherwise return place of origin
        String placeOfOrigin = sandwich.getPlaceOfOrigin().equals("")  ? " Unknown." : " " + sandwich.getPlaceOfOrigin();
        //Generate string of list
        String otherNames = generateString(sandwich.getAlsoKnownAs());
        //If description is empty return "No description" otherwise return description
        String description = sandwich.getDescription().equals("") ? "No description." : "   " + sandwich.getDescription();
        //Generate string of list
        String ingredients = generateString(sandwich.getIngredients());

        //Display information
        mOrigins.setText(placeOfOrigin);
        mAlsoKnownAs.setText(otherNames);
        mDescription.setText(description);
        mIngredients.setText(ingredients);
    }
}
