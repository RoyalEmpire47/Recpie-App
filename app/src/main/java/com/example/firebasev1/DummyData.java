package com.example.firebasev1;

import java.util.ArrayList;

public class DummyData {

    String RecipeName, Author, RecipeDescription, RecipeHowTo, Country, ImageURL, ingredientListStr;
    public static ArrayList<String> keyList = new ArrayList<>();
    public static ArrayList<DummyData> recipeList = new ArrayList<>();

    public DummyData() {
    }

    public DummyData(String recipeName, String author, String recipeDescription, String recipeHowTo, String country, String imageURL, String ingredientListStr) {
        RecipeName = recipeName;
        Author = author;
        RecipeDescription = recipeDescription;
        RecipeHowTo = recipeHowTo;
        this.Country = country;
        ImageURL = imageURL;
        this.ingredientListStr = ingredientListStr;

    }

    public String getRecipeName() {
        return RecipeName;
    }

    public String getAuthor() {
        return Author;
    }

    public String getIngredientListStr() {
        return ingredientListStr;
    }

    public String getCountry() {
        return Country;
    }

    public String getRecipeDescription() {
        return RecipeDescription;
    }

    public String getRecipeHowTo() {
        return RecipeHowTo;
    }

    public String getImageURL() {
        return ImageURL;
    }
}