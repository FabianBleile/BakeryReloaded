package com.fabianbleile.bakeryreloaded.Utils;

import android.os.Parcel;
import android.os.Parcelable;

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
public class RecipeObject implements Parcelable {
    private int id;
    private String name;
    private ArrayList<IngredientObject> ingredients;
    private ArrayList<StepObject> steps;
    private int servings;
    private String image;

    protected RecipeObject(Parcel in) {
        id = in.readInt();
        name = in.readString();
        steps = in.createTypedArrayList(StepObject.CREATOR);
        servings = in.readInt();
        image = in.readString();
    }

    public static final Creator<RecipeObject> CREATOR = new Creator<RecipeObject>() {
        @Override
        public RecipeObject createFromParcel(Parcel in) {
            return new RecipeObject(in);
        }

        @Override
        public RecipeObject[] newArray(int size) {
            return new RecipeObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedList(steps);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    public static class IngredientObject {
        private int quantity;
        private String measure;
        private String ingredient;

        public IngredientObject() {
        }

        public IngredientObject(int quantity, String measure, String ingredient) {
            this.quantity = quantity;
            this.measure = measure;
            this.ingredient = ingredient;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getMeasure() {
            return measure;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }

        public String getIngredient() {
            return ingredient;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }
    }
    public static class StepObject implements Parcelable {
        private int id;
        private String shortDescription;
        private String description;
        private String videoUrl;
        private String thumbnailUrl;

        public StepObject() {
        }

        public StepObject(int id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
            this.id = id;
            this.shortDescription = shortDescription;
            this.description = description;
            this.videoUrl = videoUrl;
            this.thumbnailUrl = thumbnailUrl;
        }

        private StepObject(Parcel in) {
            id = in.readInt();
            shortDescription = in.readString();
            description = in.readString();
            videoUrl = in.readString();
            thumbnailUrl = in.readString();
        }

        public static final Creator<StepObject> CREATOR = new Creator<StepObject>() {
            @Override
            public StepObject createFromParcel(Parcel in) {
                return new StepObject(in);
            }

            @Override
            public StepObject[] newArray(int size) {
                return new StepObject[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(id);
            parcel.writeString(shortDescription);
            parcel.writeString(description);
            parcel.writeString(videoUrl);
            parcel.writeString(thumbnailUrl);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<IngredientObject> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<IngredientObject> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<StepObject> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<StepObject> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
