package el.fit.bstu.by.collectionapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import el.fit.bstu.by.collectionapp.DBHelper.DBHelper;
import el.fit.bstu.by.collectionapp.entity.Item;
import el.fit.bstu.by.collectionapp.middleware.DbBitmapUtility;

public class AddItemActivity extends AppCompatActivity {

    ImageView itemImage;
    EditText itemName;
    Spinner itemCategories;
    RatingBar itemState;
    EditText itemDescription;

    Uri uri;
    byte[] byteImage;

    String[] Categories = {"Монета", "Марка", "Книга", "Комикс", "Значки", "Другое"};
    String chosenCategory = "Другое";

    Float currentRating = Float.intBitsToFloat(0);

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        //Убрать каличный хедер
        getSupportActionBar().hide();

        itemImage = findViewById(R.id.itemImage);
        itemName = findViewById(R.id.itemName);
        itemCategories = findViewById(R.id.itemCategory);
        itemState = findViewById(R.id.itemState);
        itemDescription = findViewById(R.id.itemDescription);

        loadCategories();

        //Listener for categories
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                chosenCategory = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        itemCategories.setOnItemSelectedListener(itemSelectedListener);

        itemState.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                itemState.setRating(rating);
                currentRating = rating;
                Toast.makeText(AddItemActivity.this, "рейтинг: " + String.valueOf(rating),
                        Toast.LENGTH_LONG).show();
            }
        });

    db = new DBHelper(this);
    db.getWritableDatabase();
}

    private void loadCategories() {
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCategories.setAdapter(adapter);
    }


    public void addImageBtn(View view) {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            uri = data.getData();
            itemImage.setImageURI(uri);

            BitmapDrawable drawable = (BitmapDrawable) itemImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            byteImage = DbBitmapUtility.getBytes(bitmap);

        } else Toast.makeText(this, "You haven't picked image", Toast.LENGTH_LONG).show();
    }

    public void addNewItemBtn(View view) {
        if (itemName.getText().length() < 2 || itemName.getText().length() > 15) {
            Toast.makeText(this, "Название должно быть от 2 до 15 символов", Toast.LENGTH_LONG).show();
            return;
        }
        if(currentRating<=0){
            Toast.makeText(this, "Укажите состояние объекта", Toast.LENGTH_LONG).show();
            return;
        }

        String name = itemName.getText().toString();
        String category = chosenCategory;
        String description = itemDescription.getText().toString();
        int state = currentRating.intValue();

        Item item = new Item(0,name,category,description,state,byteImage);
        db.Insert(item);

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("Новый предмет добавлен");
        b.setTitle("").setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AddItemActivity.this.finish();
            }
        });
        AlertDialog ad = b.create();
        ad.show();
    }
}