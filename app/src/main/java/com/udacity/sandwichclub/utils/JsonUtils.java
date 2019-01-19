package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class JsonUtils {

    //This method returns a Sandwich object from a formatted string as JSON
    public static Sandwich parseSandwichJson(String json) {
        //Get values of json string to generate a Sandwich object
        String mainName = getValueFromKey(json, "mainName");
        List<String> alsoKnownAs = handleList(getValueFromKey(json, "alsoKnownAs"));
        String placeOfOrigin = getValueFromKey(json, "placeOfOrigin");
        String description = getValueFromKey(json, "description");
        String image = getValueFromKey(json, "image");
        List<String> ingredients = handleList(getValueFromKey(json, "ingredients"));
        //Return Sandwich object.
        return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
    }

    //Return list of strings from a formatted string ["item1", "item2", "item3", "item4"]
    //if formatted string is "[]" return null (case 1)
    //otherwise return list of strings (case 2)
    public static List<String> handleList(String array){
        //Check for (case 1)
        array = array.substring(1, array.length()-1);
        if(array.length()==0) return null;
        //(case 2)
        //remove all " from the string
        array = array.replaceAll("\"", "");
        //Split string by ","
        String[] values = array.split(",");
        List<String> listOfValues = new ArrayList<String>();
        //Add to list of strings
        for(String value: values){
            listOfValues.add(value);
        }
        return listOfValues;
    }

    //Helper method to handle a key value of a Object or Array
    //initialCharacter can be { or [ and finalCharacter can be } or ]
    //Generalized to object of objects, array of arrays, arrays of objects and so on.
    public static String getValueObject(String value, String initialCharacter, String finalCharacter){
        String finalValue=""; //key value it can be an object or array formatted as string
        Stack stack = new Stack(); //Stack used to handle object of objects or array of arrays
        //Go through all the string until final of object or array is found.
        for(int i=0; i<value.length(); i++){
            //Get character at index i
            String character = value.substring(i, i+1);
            //Concat character with final key value
            finalValue = finalValue + character;
            //If character equals initial character ( [ or { )
            if(character.equals(initialCharacter)){
                //Add character to the stack
                stack.push(character);
                //if character equals final character
            } else if(character.equals(finalCharacter)){
                //pop initial character to indicate final of an object or array
                stack.pop();
            }
            //if the stack is empty it means we are in the final of the object or array
            //then, break for loop
            if(stack.empty()){
                break;
            }

        }
        return finalValue;
    }

    //Helper method to handle a key value of a String
    //String format: "hello world."
    public static String getValueString(String value){
        String finalValue="";
        //Start in second character to avoid break the for loop with the first "
        for(int i=1; i<value.length(); i++) {
            //Get character at index i
            String character = value.substring(i, i + 1);
            //If character equals \ it means that the next character is a special character
            if(character.equals("\\")){
                //Skip actual character and go to next the next one
                i++;
                //Append character next to \ to final key value
                finalValue = finalValue + value.substring(i, i + 1);
                continue; //go to next character
                //Else if character equals " it means is end of string
            } else if(character.equals("\"")){
                break;
            }
            //Otherwise just append normal character
            finalValue = finalValue + character;
        }
        return finalValue;
    }

    //Method to extract the value of a given key in a string formatted as JSON
    public static String getValueFromKey(String json, String key){
        //Split JSON string at "key":
        //Raw value will be in the second part of the splitted array
        String rawValue = json.split("\""+key+"\":")[1];
        String value;
        //If the first character of the raw value is { it means it is an object
        if(rawValue.charAt(0) == '{'){
            value = getValueObject(rawValue, "{", "}"); //Call helper method
        } else if(rawValue.charAt(0) == '['){ //if the first character is [ it is an array
            value = getValueObject(rawValue, "[", "]"); //Call helper method
        } else{
            //Otherwise it can be a String, Integer or Double. As this project just manage strings I didn't handle integers or doubles
            value = getValueString(rawValue); //Handle String
        }
        return value;
    }
}
