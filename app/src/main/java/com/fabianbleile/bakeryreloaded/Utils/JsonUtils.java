package com.fabianbleile.bakeryreloaded.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/* example of recipe object
[
  {
    "id": 1,
    "name": "Nutella Pie",
    "ingredients": [
      {
        "quantity": 2,
        "measure": "CUP",
        "ingredient": "Graham Cracker crumbs"
      },
      {
        "quantity": 6,
        "measure": "TBLSP",
        "ingredient": "unsalted butter, melted"
      }],
      "steps": [
      {
        "id": 0,
        "shortDescription": "Recipe Introduction",
        "description": "Recipe Introduction",
        "videoURL": "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
        "thumbnailURL": ""
      },
      {
        "id": 1,
        "shortDescription": "Starting prep",
        "description": "1. Preheat the oven to 350\u00b0F. Butter a 9\" deep dish pie pan.",
        "videoURL": "",
        "thumbnailURL": ""
      }],
      "servings": 8,
      "image": ""
   }
[
 */

public class JsonUtils {
    public JsonUtils(){

    }
    public static RecipeObject fromJson(JSONObject jsonObject){
        //initialising
        RecipeObject recipeObject;
        int id = 0;
        String name = "";
        ArrayList<RecipeObject.IngredientObject> ingredients = new ArrayList<>();
        ArrayList<RecipeObject.StepObject> steps = new ArrayList<>();
        int servings = 0;
        String image = "";

        try { id = jsonObject.getInt("id"); } catch (JSONException ignored) {}
        try { name = jsonObject.getString("name"); } catch (JSONException ignored) {}
        try { servings = jsonObject.getInt("servings"); } catch (JSONException ignored) {}
        try { image = jsonObject.getString("image"); } catch (JSONException ignored) {}
        try {
            int quantity = 0; String measure = ""; String ingredient= "";
            JSONArray ingredientsJsonArray = jsonObject.getJSONArray("ingredients");
            for (int i = 0; i < ingredientsJsonArray.length(); i++) {
                JSONObject ingredientJsonObject = new JSONObject(ingredientsJsonArray.get(i).toString());
                try { quantity = ingredientJsonObject.getInt("quantity"); } catch (JSONException ignored) {}
                try { measure = ingredientJsonObject.getString("measure"); } catch (JSONException ignored) {}
                try { ingredient = ingredientJsonObject.getString("ingredient"); } catch (JSONException ignored) {}
                RecipeObject.IngredientObject ingredientObject = new RecipeObject.IngredientObject(quantity, measure, ingredient);
                ingredients.add(ingredientObject);
            }
        } catch (JSONException ignored) {}
        try {
            int stepId = -1; String shortDescription = ""; String description = ""; String videoURL = ""; String thumbnailURL = "";
            JSONArray stepsJsonArray = jsonObject.getJSONArray("steps");
            for (int i = 0; i < stepsJsonArray.length(); i++) {
                JSONObject stepJsonObject = new JSONObject(stepsJsonArray.get(i).toString());
                try { stepId = stepJsonObject.getInt("id"); } catch (JSONException ignored) {}
                try { shortDescription = stepJsonObject.getString("shortDescription"); } catch (JSONException ignored) {}
                try { description = stepJsonObject.getString("description"); } catch (JSONException ignored) {}
                try { videoURL = stepJsonObject.getString("videoURL"); } catch (JSONException ignored) {}
                try { thumbnailURL = stepJsonObject.getString("thumbnailURL"); } catch (JSONException ignored) {}
                RecipeObject.StepObject stepObject = new RecipeObject.StepObject(stepId, shortDescription, description, videoURL, thumbnailURL);
                steps.add(stepObject);
            }
        } catch (JSONException ignored) {}

        recipeObject = new RecipeObject(id, name, ingredients, steps, servings, image);

        return recipeObject;
    }
}
