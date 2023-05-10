package com.example.a3_1_ans_project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "order.db"; // DB 이름

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 테이블 생성 (기본키 : id (상품번호) / 속성1 : name (상품명) / 속성2 : quantity (수량) / 속성3 : sales (판매량) / 속성4 : stock (재고량) / 속성5 : date (날짜)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Inventory (id INTEGER PRIMARY KEY, name TEXT NOT NULL, quantity INTEGER NOT NULL, sales INTEGER, stock INTEGER NOT NULL, date TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    // SELECT문
    public ArrayList<InventoryItem> getInventoryItems() {
        ArrayList<InventoryItem> inventoryItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Inventory ORDER BY stock DESC", null); // 재고량 내림차순
        if(cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                int sales = cursor.getInt(cursor.getColumnIndexOrThrow("sales"));
                int stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                InventoryItem inventoryItem = new InventoryItem();
                inventoryItem.setId(id);
                inventoryItem.setName(name);
                inventoryItem.setQuantity(quantity);
                inventoryItem.setSales(sales);
                inventoryItem.setStock(stock);
                inventoryItem.setDate(date);
                inventoryItems.add(inventoryItem);
            }
        }
        cursor.close();

        return inventoryItems;
    }

    // INSERT문
    public void InsertInventory(int _id, String _name, int _quantity, int _sales, int _stock, String _date) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO Inventory (id, name, quantity, sales, stock, date) VALUES ('"+_id+"', '"+_name+"', '"+_quantity+"', '"+_sales+"', '"+(_quantity - _sales)+"', '"+_date+"');");
    }

    // UPDATE문
    public void UpdateInventory(int _id, String _name, int _quantity, int _sales, int _stock, String _date) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE Inventory Set name='"+_name+"', quantity='"+_quantity+"', sales='"+_sales+"', stock='"+(_quantity - _sales)+"', date='"+_date+"' WHERE id='"+_id+"'");
    }

    // DELETE문
    public void DeleteInventory(int _id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM Inventory WHERE id='"+_id+"'");
    }
}
