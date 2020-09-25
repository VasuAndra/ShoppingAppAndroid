package com.example.proiecttest.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String name;
    private List<String> ingredients;
    private String imageURL;

    public Recipe() {

    }

    public Recipe(String name, List<String> ingredients, String imageURL) {
        this.name = name;
        this.ingredients = ingredients;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Recipe fromJSON(JSONObject recipeJSON) throws JSONException {
        String name = recipeJSON.getString("name");
        JSONArray ingredientsJSON = recipeJSON.getJSONArray("ingredients");
        List<String> ingredients = new ArrayList<>();
        if (ingredientsJSON != null) {
            int len = ingredientsJSON.length();
            for (int i = 0; i < len; i++) {
                ingredients.add(ingredientsJSON.getString(i));
            }
        }
        String imageURL = recipeJSON.getString("imageURL");
        return new Recipe(name, ingredients, imageURL);
    }
}
