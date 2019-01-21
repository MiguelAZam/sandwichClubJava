package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    //Key constants
    private static String JSON_NAME="name";
    private static String JSON_MAINNAME="mainName";
    private static String JSON_ALSOKNOWNAS="alsoKnownAs";
    private static String JSON_PLACEOFORIGIN="placeOfOrigin";
    private static String JSON_DESCRIPTION="description";
    private static String JSON_IMAGE="image";
    private static String JSON_INGREDIENTS="ingredients";


    //This method returns a Sandwich object from a formatted string as JSON
    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = new Sandwich(); //Sandwich object
        List<String>  alsoKnownAs = new ArrayList<>(); //List for alsoKnownAs key
        List<String>  ingredients = new ArrayList<>(); //List for ingredients key
        if(json != null){
            try{
                JSONObject sandwichJSON = new JSONObject(json); //Parse JSON string
                JSONObject name = sandwichJSON.getJSONObject(JSON_NAME); //Get "name" key values

                //take JSONArray and return list for ingredients and alsoKnownAs
                JSONArray alsoKnownAsArray = name.getJSONArray(JSON_ALSOKNOWNAS);
                for(int i=0; i<alsoKnownAsArray.length(); i++){
                    alsoKnownAs.add(alsoKnownAsArray.getString(i));
                }

                JSONArray ingredientsArray = sandwichJSON.getJSONArray(JSON_INGREDIENTS);
                for(int i=0; i<ingredientsArray.length(); i++){
                    ingredients.add(ingredientsArray.getString(i));
                }

                //Set values in the sandwich object
                sandwich.setMainName(name.getString(JSON_MAINNAME));
                sandwich.setAlsoKnownAs(alsoKnownAs);
                sandwich.setPlaceOfOrigin(sandwichJSON.getString(JSON_PLACEOFORIGIN));
                sandwich.setDescription(sandwichJSON.getString(JSON_DESCRIPTION));
                sandwich.setImage(sandwichJSON.getString(JSON_IMAGE));
                sandwich.setIngredients(ingredients);

            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return sandwich;
    }

}
