package com.fabianbleile.bakeryreloaded.Room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class IngredientWidget implements Parcelable{
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "measure")
    private String measure;

    @ColumnInfo(name = "ingredient")
    private String ingredient;

    public IngredientWidget(int quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public static Creator<IngredientWidget> getCREATOR() {
        return CREATOR;
    }

    protected IngredientWidget(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        quantity = in.readInt();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<IngredientWidget> CREATOR = new Creator<IngredientWidget>() {
        @Override
        public IngredientWidget createFromParcel(Parcel in) {
            return new IngredientWidget(in);
        }

        @Override
        public IngredientWidget[] newArray(int size) {
            return new IngredientWidget[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeInt(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }
}
