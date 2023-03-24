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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import el.fit.bstu.by.collectionapp.DBHelper.DBHelper;
import el.fit.bstu.by.collectionapp.entity.Item;
import el.fit.bstu.by.collectionapp.middleware.DbBitmapUtility;

public class ItemActivity extends AppCompatActivity {

    ImageView itemImage;
    EditText itemName;
    Spinner itemCategories;
    RatingBar itemState;
    EditText itemDescription;
    Button addImage,confirmChange;

    Uri uri;
    byte[] byteImage;

    String[] Categories = {"Монета", "Марка", "Книга", "Комикс", "Значки", "Другое"};
    String chosenCategory;

    Float currentRating = Float.intBitsToFloat(0);

    DBHelper db;

    int ID;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        //Убрать каличный хедер
        getSupportActionBar().hide();

        Bundle arguments = getIntent().getExtras();
        ID = (int) arguments.get("ID");

        db = new DBHelper(this);
        db.getWritableDatabase();

        item = db.Select(ID);

        chosenCategory = item.Category;


        itemImage = findViewById(R.id.itemImage2);
        itemName = findViewById(R.id.itemName2);
        itemCategories = findViewById(R.id.itemCategory2);
        itemState = findViewById(R.id.itemState2);
        itemDescription = findViewById(R.id.itemDescription2);
        addImage = findViewById(R.id.addImage2);
        confirmChange = findViewById(R.id.confirmAdd2);

        loadInfo();
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
            }
        });
    }

    private int getCategoryNumber() {
        switch (chosenCategory) {
            case "Монета":
                return 0;
            case "Марка":
                return 1;
            case "Книга":
                return 2;
            case "Комикс":
                return 3;
            case "Значки":
                return 4;
            case "Другое":
                return 5;
        }
        return 0;
    }

    private void loadInfo() {
        try{
            itemImage.setImageBitmap(DbBitmapUtility.getImage(item.ByteImage));
        }catch (Exception e){
            e.printStackTrace();
        }
        itemName.setText(item.Name);
        itemDescription.setText(item.Description);
        itemState.setRating(item.State);

    }

    private void loadCategories() {
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCategories.setAdapter(adapter);
        itemCategories.setSelection(getCategoryNumber());
    }

    public void ChangeItemBtn(View view) {

        if (itemName.getText().length() < 2 || itemName.getText().length() > 15) {
            Toast.makeText(this, "Название должно быть от 2 до 15 символов", Toast.LENGTH_LONG).show();
            return;
        }
        if(currentRating<=0){
            Toast.makeText(this, "Укажите состояние объекта", Toast.LENGTH_LONG).show();
            return;
        }

        item.Name = itemName.getText().toString();
        item.Description = itemDescription.getText().toString();
        item.Category = chosenCategory;
        item.ByteImage = byteImage;
        item.State = currentRating.intValue();

        db.Update(item);

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("Новый предмет добавлен");
        b.setTitle("").setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ItemActivity.this.finish();
            }
        });
        AlertDialog ad = b.create();
        ad.show();
    }

    public void changeImageBtn(View view) {
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

    public void enableEditItem(View view) {
        itemName.setEnabled(true);
        itemDescription.setEnabled(true);
        addImage.setEnabled(true);
        confirmChange.setEnabled(true);
    }

    public void deleteBtn(View view) {
        long result = db.Delete(item.ID);
        if (result == -1) {
            Toast.makeText(this, "Failed to delete:(", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
            this.finish();
        }

    }
}