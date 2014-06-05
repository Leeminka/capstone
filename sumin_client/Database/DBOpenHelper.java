package Database;

import java.util.ArrayList;

import DTO.PermissionListDTO;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String PERMISSIONLIST_CREATE = "create table PERMISSION_LIST(PERMISSION string primary key, NAME text not null, INFORMATION text not null);";
	//private static final int PERMISSION_LIST = 0;
	private ArrayList<PermissionListDTO> permissionList = new ArrayList();

	public DBOpenHelper(Context paramContext, String paramString,
			SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt) {
		super(paramContext, paramString, paramCursorFactory, paramInt);
	}

	private void regitAllItem() {
		
		this.permissionList.add(new PermissionListDTO("ACCESS_CHECKIN_PROPERTIES", "체크인데이터베이스의_속성테이블로_액세스",
				"체크인 데이터베이스의 속성 테이블의 읽고 쓰기 권한"));
		this.permissionList.add(new PermissionListDTO("ACCESS_COARSE_LOCATION", "코스_로케이션_액세스_(Cell-ID/WiFi)",
				"코스 위치 권한(Cell-ID/WIFI)-GPS사용시 선언"));
		this.permissionList.add(new PermissionListDTO("ACCESS_FINE_LOCATION", "파인로케이션_액세스(GPS)",
				"파인위치(find  location) 허용(gps)"));
		this.permissionList.add(new PermissionListDTO("BATTERY_STATS", "배터리_상태",
				"배터리_상태"));
		this.permissionList.add(new PermissionListDTO("BLUTOOTH", "블루투스",
				"블루투스"));
		this.permissionList.add(new PermissionListDTO("CALL_PHONE", "통화",
				"통화"));
		this.permissionList.add(new PermissionListDTO("CAMERA", "카메라",
				"카메라"));
		this.permissionList.add(new PermissionListDTO("FLASHLIGHT", "플래시라이트(권한)",
				"플래시라이트(권한)"));
		this.permissionList.add(new PermissionListDTO("INTERNET", "인터넷 권한",
				"인터넷 권한"));
		this.permissionList.add(new PermissionListDTO("READ_CONTACTS", "주소록_읽어오기",
				"주소록 관련 권한"));
		this.permissionList.add(new PermissionListDTO("READ_EXTERNAL_STORAGE", "보호된 저장소에 액세스 테스트",
				"보호된 저장소에 액세스 테스트"));
		this.permissionList.add(new PermissionListDTO("READ_SMS", "SMS문자 관련 권한",
				"내 문자 메시지 읽기, SMS읽어오기"));
		
		this.permissionList.add(new PermissionListDTO("NOT_FIND", "권한 없음",
				"권한 없음"));
	}

	public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
		paramSQLiteDatabase.execSQL(PERMISSIONLIST_CREATE);
		regitAllItem();
		for (int i = 0; i < permissionList.size(); i++) {
			ContentValues values = new ContentValues();
			values.put("PERMISSION", permissionList.get(i).getPermission());
			values.put("NAME", permissionList.get(i).getName());
			values.put("INFORMATION", permissionList.get(i).getInformation());
			paramSQLiteDatabase.insert("PERMISSION_LIST", null, values);
		}
	}

	public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1,
			int paramInt2) {
		Log.w("TaskDBAdapter", "Upgrading from version " + paramInt1 + " to"
				+ paramInt2 + ", which will destroy all old data");
		paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS PERMISSION_LIST");
		onCreate(paramSQLiteDatabase);
	}
}
