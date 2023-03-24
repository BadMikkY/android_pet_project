package el.fit.bstu.by.collectionapp.entity;

import java.io.Serializable;

public class Item implements Serializable {
    public int ID;
    public String Name;
    public String Category;
    public String Description;
    public int State;
    public byte[] ByteImage;

    public Item(int id, String name, String category, String description, int state, byte[] byteImage) {
        ID = id;
        Name = name;
        Category = category;
        Description = description;
        State = state;
        ByteImage = byteImage;
    }

    public String getName() {
        return this.Name;
    }

    public String getCategory(){
        return this.Category;
    }

    public byte[] getImage(){
        return this.ByteImage;
    }

    @Override
    public String toString() {
        return "\n" + "Name: " + Name + " Category: " + Category;
    }
}
