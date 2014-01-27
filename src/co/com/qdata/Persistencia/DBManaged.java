package co.com.qdata.Persistencia;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManaged extends SQLiteOpenHelper {

	private String tabla;
	
	public DBManaged(Context context, String nomDB, int version, String tabla) {
		super(context, nomDB, null, version);
		this.tabla = tabla;
	}

    @Override
    public void onCreate(SQLiteDatabase db) {
          db.execSQL(tabla);
    }
    
    @SuppressLint("NewApi")
	@Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(DBManaged.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + getDatabaseName());
        onCreate(database);
    }
	
    @SuppressLint("NewApi")
    public void insertarUrl(int id, String url, String tabla) throws SQLiteException{
    	SQLiteDatabase db = getWritableDatabase();
    	if(!(db == null)){
    		ContentValues valores = new ContentValues();
    		valores.put("ID", id);
    		valores.put("URL", url);
    		db.insert(tabla, null, valores);
    		db.close();
    	}
    }
    
    @SuppressLint("NewApi")
    public void modificarUrl(int id, String url, String tabla) throws SQLiteException{
        SQLiteDatabase db = getWritableDatabase();
        if(!(db == null)){
            ContentValues valores = new ContentValues();
    		valores.put("ID", id);
    		valores.put("URL", url);
            db.update(tabla, valores, "ID=" + id, null);
            db.close();   
        }
    }
    
    @SuppressLint("NewApi")
    public void borrarUrl(int id, String tabla) throws SQLiteException {
        SQLiteDatabase db = getWritableDatabase();
        if(!(db == null)){
            db.delete(tabla, "ID="+id, null);
            db.close();          	
        }
    }
    
    public String recuperarURL(String sentencia) throws SQLiteException{
    	SQLiteDatabase db = getReadableDatabase();
    	String url = "";
    	if(!(db == null)){
    		Cursor c = db.rawQuery(sentencia, null);
    		if(!(c == null)){
    			c.moveToFirst();
    		}
    		if(c.getCount() > 0){
    			url = c.getString(0);
    		}
    		db.close();
    		c.close();
    	}
    	return url;
    }
    
    public static String recuperarURL(SQLiteDatabase db, String sentencia) throws SQLiteException{
    	String url = "";
    	if(!(db == null)){
    		Cursor c = db.rawQuery(sentencia, null);
    		if(!(c == null)){
    			c.moveToFirst();
    		}
    		if(c.getCount() > 0){
    			url = c.getString(0);
    		}
    		db.close();
    		c.close();
    	}
    	return url;
    }
   
}
