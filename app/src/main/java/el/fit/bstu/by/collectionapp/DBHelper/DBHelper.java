package el.fit.bstu.by.collectionapp.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import el.fit.bstu.by.collectionapp.entity.Item;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Project.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME_ITEMS = "Items";

    private static final String ID_ITEMS_COL = "ID";
    private static final String NAME_ITEMS_COL = "name";
    private static final String CATEGORY_ITEMS_COL = "category";
    private static final String DESCR_ITEMS_COL = "description";
    private static final String STATE_ITEMS_COL = "state";
    private static final String IMAGE_ITEMS_COL = "image";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createItemTable = "CREATE TABLE " + TABLE_NAME_ITEMS + " ("
                + ID_ITEMS_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_ITEMS_COL + " TEXT,"
                + CATEGORY_ITEMS_COL + " TEXT,"
                + DESCR_ITEMS_COL + " TEXT,"
                + STATE_ITEMS_COL + " INTEGER,"
                + IMAGE_ITEMS_COL + " TEXT)";

        db.execSQL(createItemTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ITEMS);
        onCreate(db);
    }

    public void Insert(Item item) {
        ContentValues cv = new ContentValues();
        cv.put(NAME_ITEMS_COL, item.Name);
        cv.put(CATEGORY_ITEMS_COL, item.Category);
        cv.put(DESCR_ITEMS_COL, item.Description);
        cv.put(STATE_ITEMS_COL, item.State);
        cv.put(IMAGE_ITEMS_COL, item.ByteImage);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME_ITEMS, null, cv);
    }

    public List<Item> SelectAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor itemsCursor = db.rawQuery("SELECT * FROM Items", null);
        List<Item> items = new ArrayList<>();
        if(itemsCursor.moveToFirst()){
            do{
                items.add(new Item(
                        itemsCursor.getInt(0),
                        itemsCursor.getString(1),
                        itemsCursor.getString(2),
                        itemsCursor.getString(3),
                        itemsCursor.getInt(4),
                        itemsCursor.getBlob(5)
                ));
            }while (itemsCursor.moveToNext());
        }
        return items;
    }

    public Item Select(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor itemsCursor = db.rawQuery("SELECT * FROM Items WHERE id = ?", new String[]{String.valueOf(id)});
        Item item = null;
        if(itemsCursor.moveToFirst()){
                item = new Item(
                        itemsCursor.getInt(0),
                        itemsCursor.getString(1),
                        itemsCursor.getString(2),
                        itemsCursor.getString(3),
                        itemsCursor.getInt(4),
                        itemsCursor.getBlob(5)
                );
        }
        return item;
    }

    public long Delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME_ITEMS,"ID=?", new String[]{String.valueOf(id)});
        return result;
    }

    public void Update(Item item){
        SQLiteDatabase db = this.getWritableDatabase();

        long delResult = db.delete(TABLE_NAME_ITEMS,"ID=?", new String[]{String.valueOf(item.ID)});

        ContentValues cv = new ContentValues();
        cv.put(NAME_ITEMS_COL, item.Name);
        cv.put(CATEGORY_ITEMS_COL, item.Category);
        cv.put(DESCR_ITEMS_COL, item.Description);
        cv.put(STATE_ITEMS_COL, item.State);
        cv.put(IMAGE_ITEMS_COL, item.ByteImage);

        long insertResult = db.insert(TABLE_NAME_ITEMS, null, cv);
    }
}
