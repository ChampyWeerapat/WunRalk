package com.example.champy.wunralk;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

public class ManageDB extends AppCompatActivity {
	private static ManageDB instance;
	private SQLiteDatabase mydatabase;
	
	private ManageDB(SQLiteDatabase mydatabase) throws SQLException {
		this.mydatabase = mydatabase;
	}

	public static ManageDB getInstance(SQLiteDatabase mydatabase) throws SQLException {
		if (instance == null){
			instance = new ManageDB(mydatabase);
		}
		return instance;
	}

	public void createTable(String nameTable, String format){
		mydatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(%s);",nameTable,format));
	}
	
	public ArrayList<ArrayList<String>> readDB(String nameTable) throws SQLException{
		ArrayList<ArrayList<String>> tmp = new ArrayList<ArrayList<String>>();
		Cursor resultSet = mydatabase.rawQuery("Select * from member",null);
		if (! resultSet.moveToFirst()){
			return tmp;
		}
		do {
			ArrayList<String> tmpIn = new ArrayList<String>();
			int i = 0;
			while(i < resultSet.getColumnCount()){
				tmpIn.add(resultSet.getString(i));
				i++;
			}
			tmp.add(tmpIn);
		}while(resultSet.moveToNext());
		return tmp;
	}
	
	public void insertDB(String nameTable, String[] data){
		String values = "";
		for (int i = 0 ; i< data.length ; i++ ){
			values += "'"+data[i]+"'";
			if(i!= data.length-1){
				values +=",";
			}
		}
		mydatabase.execSQL(String.format("INSERT INTO member VALUES(%s);",values));
	}
	
	public void deleteDB(String fromTable, String where){
		mydatabase.execSQL(String.format("DELETE FROM irDB.%s WHERE %s", fromTable, where));
	}
	
	public void updated(String nameTable, String set, String where){
		mydatabase.execSQL(String.format("UPDATE %s SET %s WHERE %s", nameTable, set, where));
	}

	public void addMember(String username, String password){
		Cursor resultSet = mydatabase.rawQuery(String.format("Select * from member where username='%s'",username),null);
		if (resultSet.getCount()>=1){
			return;
		}
		insertDB("member", new String[]{username, password});
	}

	public void delMember(String username){
		deleteDB("member", "username='" + username + "'");
	}

	public boolean login(String username, String password){
		Cursor resultSet;
		if (mydatabase==null){
			Log.d("ddd","databse null");
		}else{
			Log.d("ddd","databse assigned");
		}
		resultSet = mydatabase.rawQuery(String.format("Select * from member where username='%s' AND password='%s'",username,password),null);
		if (resultSet.getCount()>=1){
			return true;
		}
		return false;
	}

	public void addEvent(String username, String date, String place, double distanc, String time, double calories){
//		mydatabase.execSQL(String.format("INSERT INTO history VALUES('wunralker','2558-12-8 18:00:00','ku',4.5,'0:21:30',200);",username,date,place,distanc,time,calories));
//		createTable("history","username varchar(16),dateTime datetime,place varchar(50),distance double,time time,calories double");
		mydatabase.execSQL(String.format("INSERT INTO history VALUES('%s','%s','%s',%f,'%s',%f);", username, date, place, distanc, time, calories));
		Cursor a = mydatabase.rawQuery("Select * from member where 1",null);

		Log.d("ddd","insert yes"+ a.getCount());
	}
}
