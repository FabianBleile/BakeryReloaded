package com.fabianbleile.bakeryreloaded;

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
public class RecipeObject {
    int id;
    String name;
    ArrayList<IngredientObject> ingredients;
    ArrayList<StepObject> steps;
    int servings;
    String image;

    public static class IngredientObject {
        int quantity;
        String measure;
        String ingredient;

        public IngredientObject(int quantity, String measure, String ingredient) {
            this.quantity = quantity;
            this.measure = measure;
            this.ingredient = ingredient;
        }
    }
    public static class StepObject {
        int id;
        String shortDescription;
        String description;
        String videoUrl;
        String thumbnailUrl;

        public StepObject(int id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
            this.id = id;
            this.shortDescription = shortDescription;
            this.description = description;
            this.videoUrl = videoUrl;
            this.thumbnailUrl = thumbnailUrl;
        }

        public StepObject() {
        }
    }

    public RecipeObject(int id, String name, ArrayList<IngredientObject> ingredients, ArrayList<StepObject> steps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }
}
