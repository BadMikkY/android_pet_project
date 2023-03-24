package el.fit.bstu.by.collectionapp.DBHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Provider extends ContentProvider {

    private DBHelper db;

    static final String CONTENT_AUTHORITY = "el.fit.bstu.by.provider";
    static final String ITEMS_LIST_PATH = "itemslist";
    static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY + "/" + ITEMS_LIST_PATH);

    static final int URI_ITEMS = 1;
    static final int URI_ITEM = 2;


    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, ITEMS_LIST_PATH, URI_ITEMS);
        uriMatcher.addURI(CONTENT_AUTHORITY, ITEMS_LIST_PATH + "/#", URI_ITEM);
    }

    @Override
    public boolean onCreate() {
        db = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (uriMatcher.match(uri)){
            case URI_ITEMS:
                if(TextUtils.isEmpty(sortOrder)){
                    sortOrder = "name" + " ASC";
                }
                break;
            case URI_ITEM:
                String id = uri.getLastPathSegment();

                if(TextUtils.isEmpty(selection)){
                    selection = "ID" + " = " + id;
                } else{
                    selection = selection + " AND " + "ID" + " = " + id;
                }
                break;
        }

        Cursor cursor = db.getWritableDatabase().query("Items",projection,selection,selectionArgs,null,null, sortOrder);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri result = null;

        long rowID = db.getWritableDatabase().insert("Items",null,values);

        result = ContentUris.withAppendedId(CONTENT_AUTHORITY_URI,rowID);

        getContext().getContentResolver().notifyChange(result,null);

        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)){
            case URI_ITEMS:
                break;
            case URI_ITEM:
                String id = uri.getLastPathSegment();
                Log.d("Lab11-15","URI_GROUP_ID = " + id );
                if(TextUtils.isEmpty(selection)){
                    selection = "ID" + " = " + id;
                } else{
                    selection = selection + " AND " + "ID" + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI " + uri);
        }

        int rowCount =0;
        rowCount = db.getWritableDatabase().delete("Items",selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);

        Log.d("Lab11-15","delete completed, " + uri.toString());
        return rowCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)){
            case URI_ITEMS:
                break;
            case URI_ITEM:
                String id = uri.getLastPathSegment();
                Log.d("Lab11-15","ID" + id);
                if(TextUtils.isEmpty(selection)){
                    selection = "ID" + " = " + id;
                } else{
                    selection = selection + " AND " + "ID" + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI " + uri);
        }

        int rowCount = 0;
        rowCount = db.getWritableDatabase().update("Items",values,selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);

        Log.d("Lab11-15","update" + uri.toString());
        return rowCount;
    }
}
