package el.fit.bstu.by.collectionapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import el.fit.bstu.by.collectionapp.DBHelper.DBHelper;
import el.fit.bstu.by.collectionapp.entity.Item;

public class MainActivity extends AppCompatActivity {

    RecyclerView itemsListRV;
    EditText searchStr;
    Spinner categories;

    List<Item> items;
    List<Item> itemsWithCat;

    DBHelper db;

    String[] Categories = {"Все", "Монета", "Марка", "Книга", "Комикс", "Значки", "Другое"};
    String chosenCategory = "Все";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Убрать каличный хедер
        getSupportActionBar().hide();
        Test();
        itemsWithCat = new ArrayList<>();

        itemsListRV = findViewById(R.id.itemsListRV);
        searchStr = findViewById(R.id.searchStr);
        categories = findViewById(R.id.categories);

        db = new DBHelper(this);

        loadItems();
        loadCategories();

        ItemAdapter.OnStateClickListener stateClickListener = new ItemAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(Item item, int position) {
                loadCurrentItem(item.ID);
            }
        };

        searchStr.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<1){
                    if(chosenCategory.equals("Все")){
                        ItemAdapter itemAdapter = new ItemAdapter(MainActivity.this,items,stateClickListener);
                        itemsListRV.setAdapter(itemAdapter);
                    }else{
                        ItemAdapter itemAdapter = new ItemAdapter(MainActivity.this,itemsWithCat,stateClickListener);
                        itemsListRV.setAdapter(itemAdapter);
                    }

                } else{
                    searchQuestions(s.toString());
                }
            }
        });

        //Listener for categories
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                chosenCategory = (String) parent.getItemAtPosition(position);
                if(chosenCategory != "Все"){
                    changeCategories();
                }else{
                    ItemAdapter itemAdapter = new ItemAdapter(MainActivity.this,items,stateClickListener);
                    itemsListRV.setAdapter(itemAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        categories.setOnItemSelectedListener(itemSelectedListener);
    }

    private void searchQuestions(String s){
        ArrayList<Item> foundItems = new ArrayList<>();
        if(chosenCategory == "Все"){
            for(Item item:items){
                if(item.Name.toLowerCase().contains(s.toLowerCase())){
                    foundItems.add(item);
                }
            }
        }else{
            for(Item item:itemsWithCat){
                if(item.Name.toLowerCase().contains(s.toLowerCase())){
                    foundItems.add(item);
                }
            }
        }

        ItemAdapter.OnStateClickListener stateClickListener = new ItemAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(Item item, int position) {
                loadCurrentItem(item.ID);
            }
        };

        ItemAdapter questionAdapter = new ItemAdapter(this,foundItems, stateClickListener);
        itemsListRV.setAdapter(questionAdapter);


    }

    private void Test(){

    }

    private void changeCategories(){
        itemsWithCat.clear();
        for(Item item:items){
            if(item.Category.equals(chosenCategory)){
                itemsWithCat.add(item);
            }
        }

        ItemAdapter.OnStateClickListener stateClickListener = new ItemAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(Item item, int position) {
                loadCurrentItem(item.ID);
            }
        };

        ItemAdapter questionAdapter = new ItemAdapter(this,itemsWithCat, stateClickListener);
        itemsListRV.setAdapter(questionAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        items = db.SelectAll();
        try{
            ItemAdapter.OnStateClickListener stateClickListener = new ItemAdapter.OnStateClickListener() {
                @Override
                public void onStateClick(Item item, int position) {
                    loadCurrentItem(item.ID);
                }
            };

            ItemAdapter questionAdapter = new ItemAdapter(this,items, stateClickListener);
            itemsListRV.setAdapter(questionAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadItems(){
        items = db.SelectAll();
        try{
            ItemAdapter.OnStateClickListener stateClickListener = new ItemAdapter.OnStateClickListener() {
                @Override
                public void onStateClick(Item item, int position) {
                    loadCurrentItem(item.ID);
                }
            };

            ItemAdapter questionAdapter = new ItemAdapter(this,items, stateClickListener);
            itemsListRV.setAdapter(questionAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void loadCurrentItem(int ID){
        try{
            Intent addItemActivity = new Intent(this,ItemActivity.class);
            addItemActivity.putExtra("ID", ID);
            startActivity(addItemActivity);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void addItemBtn(View view) {
        Intent addItemActivity = new Intent(this,AddItemActivity.class);
        startActivity(addItemActivity);
    }

    private void loadCategories() {
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);
    }

    public void providerBtn(View view) {
        Intent CP_Activity = new Intent(this,CP_Activity.class);
        startActivity(CP_Activity);
    }
}