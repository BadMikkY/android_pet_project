package el.fit.bstu.by.collectionapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import el.fit.bstu.by.collectionapp.DBHelper.DBHelper;
import el.fit.bstu.by.collectionapp.entity.Item;

public class CP_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cp);
    }

    public void getAllBtn(View view) {
        Uri test = Uri.parse("content://el.fit.bstu.by.provider/itemslist");

        Cursor cursor = getApplicationContext().getContentResolver().query(test,null,null,null,null);
        String result = "";
        if(cursor.moveToFirst()){
            do {
                result += cursor.getString(0) + " " + cursor.getString(2) + " " + cursor.getString(3) + "\n";
            } while(cursor.moveToNext());

        }

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(result);
        b.setTitle("Коллекция").setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog ad = b.create();
        ad.show();
    }

    public void getOneBtn(View view) {
        int id =0;
        try{
            EditText a = findViewById(R.id.idForSelect);
            id = Integer.parseInt(a.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        Uri test = Uri.parse("content://el.fit.bstu.by.provider/itemslist");
        Uri uri = ContentUris.withAppendedId(test,id);
        Cursor cursor = getApplicationContext().getContentResolver().query(uri,null,null,null,null);
        String result = "";
        if(cursor.moveToFirst()){
            do {
                result += cursor.getString(0) + " " + cursor.getString(2) + " " + cursor.getString(3) + "\n";
            } while(cursor.moveToNext());

        }

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(result);
        b.setTitle("Коллекция").setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog ad = b.create();
        ad.show();
    }

    public void insertBtn(View view) {
        ContentValues cv = new ContentValues();
        cv.put("ID",100);
        cv.put("name","test");
        cv.put("category","TEST");
        cv.put("description","test description");
        cv.put("state",3);
        cv.put("image","");
        Uri test = Uri.parse("content://el.fit.bstu.by.provider/itemslist");
        Uri newUri = getContentResolver().insert(test,cv);
        Log.d("Lab11-15", "insert, result Uri : " + newUri.toString());
    }

    public void deleteBtn(View view) {
        Uri test = Uri.parse("content://el.fit.bstu.by.provider/itemslist");
        Uri uri = ContentUris.withAppendedId(test,100);
        int cnt = getContentResolver().delete(uri,null,null);
        Log.d("Lab11-15", "delete, count = " + cnt);
    }

    public void updateBtn(View view) {
        ContentValues cv = new ContentValues();
        cv.put("ID",100);
        cv.put("name","test updated");
        cv.put("category","TEST updated");
        cv.put("description","test description updated");
        cv.put("state",1);
        cv.put("image","");

        Uri test = Uri.parse("content://el.fit.bstu.by.provider/itemslist");
        Uri uri = ContentUris.withAppendedId(test,100);
        int cnt = getContentResolver().update(uri,cv,null);
        Log.d("Lab11-15", "update, count = " + cnt);
    }

    public void writeJSONBtn(View view) {
        DBHelper db = new DBHelper(this);
        db.getWritableDatabase();
        List<Item> items = new ArrayList<>();
        items = db.SelectAll();

        JSONHelper.exportToJSON(this,items);

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("Информация о всех предметах записана в JSON");
        b.setTitle("").setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog ad = b.create();
        ad.show();
    }

    public void readJSONBtn(View view) {
        List<Item> items = JSONHelper.importFromJSON(this);
        if(items == null){
            items = new ArrayList<>();
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setMessage("Не удалось прочитатать из JSON");
            b.setTitle("").setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog ad = b.create();
            ad.show();
        }

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(items.toString());
        b.setTitle("").setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog ad = b.create();
        ad.show();
    }
}