package Database;

import DTO.PermissionListDTO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DBPermissionListAdapter {
	private static final String DATABASE_NAME = "permissionList.db";
	private static final String DATABASE_TABLE = "PERMISSION_LIST";
	private static final int DATABASE_VERSION = 1;
	public static final int TASK_COLUMN = 1;
	private final Context context;
	private SQLiteDatabase db;
	private DBOpenHelper dbHelper;

	public DBPermissionListAdapter(Context paramContext) {
		this.context = paramContext;
		this.dbHelper = new DBOpenHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public long addPermission(PermissionListDTO _permission) {
		ContentValues values = new ContentValues();
		values.put("PERMISSION", _permission.getPermission());
		values.put("NAME", _permission.getName());
		values.put("INFORMATION", _permission.getInformation());
		return db.insert(DATABASE_TABLE, null, values);
	}

	public void close() {
		db.close();
	}

	public boolean deleteItem(long paramLong) {
		return this.db.delete("PERMISSION_LIST", "PERMISSION=" + paramLong,
				null) > 0;
	}

	public String getPermissionInfo(String paramString) throws SQLException {
		String str = "SELECT * from PERMISSION_LIST where NAME='" + paramString
				+ "'";
		Cursor localCursor = this.db.rawQuery(str, null);
		if (localCursor.getCount() == 0)
			return "NOT_FOUND";
		localCursor.moveToFirst();
		return localCursor.getString(2);
	}

	public String getPermissionName(String paramString) throws SQLException {
		String str = "SELECT * from PERMISSION_LIST where PERMISSION='"
				+ paramString + "'";
		Cursor localCursor = this.db.rawQuery(str, null);
		if (localCursor.getCount() == 0)
			return paramString;
		localCursor.moveToFirst();
		PermissionListDTO localPermissionListDTO = new PermissionListDTO(
				localCursor.getString(0), localCursor.getString(1),
				localCursor.getString(2));
		localCursor.close();
		return localPermissionListDTO.getName();
	}

	public void open() throws SQLiteException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException localSQLiteException) {
			db = this.dbHelper.getReadableDatabase();
		}
	}

	public Cursor setCursorPermission(long paramLong) throws SQLException {
		Cursor localCursor = this.db.query(true, "PERMISSION_LIST",
				new String[] { "PERMISSION", "NAME", "INFORMATION" },
				"PERMISSION=" + paramLong, null, null, null, null, null);
		if ((localCursor.getCount() == 0) || (!localCursor.moveToFirst()))
			throw new SQLException("NO Todo items found for row: " + paramLong);
		return localCursor;
	}

	public int updataItem(long paramLong,
			PermissionListDTO paramPermissionListDTO) {
		ContentValues localContentValues = new ContentValues();
		localContentValues.put("PERMISSION",
				paramPermissionListDTO.getPermission());
		localContentValues.put("NAME", paramPermissionListDTO.getName());
		localContentValues.put("INFORMATION", paramPermissionListDTO.getName());
		return this.db.update("PERMISSION_LIST", localContentValues,
				"PERMISSION=" + paramPermissionListDTO, null);
	}
}
