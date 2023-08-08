package my.agro.uffizio_practical_task.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import my.agro.uffizio_practical_task.data.ItemDataModel
import my.agro.uffizio_practical_task.data.ItemModel

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + id + " INTEGER PRIMARY KEY, " +
                itemId + " Integer," +
                itemName + " TEXT," +
                itemFullName + " TEXT," +
                itemDesc + " TEXT," +
                itemImage + " TEXT," +
                language + " TEXT," +
                totalStar + " TEXT," +
                totalFork + " TEXT" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addItems(list:ItemModel ){
        val values = ContentValues()
        values.put(itemId, list.itemId)
        values.put(itemName, list.itemName)
        values.put(itemFullName, list.itemFullName)
        values.put(itemDesc, list.itemData.get(0).itemDesc)
        values.put(itemImage, list.itemImage)
        values.put(language, list.itemData.get(0).language)
        values.put(totalStar, list.itemData.get(0).totalStar)
        values.put(totalFork, list.itemData.get(0).totalFork)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllItems(): ArrayList<ItemModel> {
        val db = this.readableDatabase
        val list = ArrayList<ItemModel>()
        try {
            val c = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
            if (c.moveToFirst()) {
                do {
                    val idIndex = c.getColumnIndex(id)
                    val itemIdIndex = c.getColumnIndex(itemId)
                    val itemNameIndex = c.getColumnIndex(itemName)
                    val itemFullNameIndex = c.getColumnIndex(itemFullName)
                    val itemDescIndex = c.getColumnIndex(itemDesc)
                    val itemImageIndex = c.getColumnIndex(itemImage)
                    val langIndex = c.getColumnIndex(language)
                    val starIndex = c.getColumnIndex(totalStar)
                    val forkIndex = c.getColumnIndex(totalFork)
                    val itemDataModel = ItemDataModel(
                        c.getString(itemDescIndex),
                        c.getString(langIndex),
                        c.getInt(starIndex),
                        c.getInt(forkIndex)
                    )
                    val listData = ArrayList<ItemDataModel>()
                    listData.add(itemDataModel)
                    val itemMode = ItemModel(
                        c.getInt(idIndex),
                        c.getInt(itemIdIndex),
                        c.getString(itemNameIndex),
                        c.getString(itemFullNameIndex),
                        c.getString(itemImageIndex),
                        listData
                    )
                    list.add(itemMode)
                } while (c.moveToNext())
            }
            c.close()
            return list
        } catch (e: Exception) {
            e.printStackTrace()
            return list
        }
    }

    fun clearAllItems(){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
        db.close()
    }

    companion object{
        private const val DATABASE_NAME = "MyDatabase"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "AllItems"
        const val id = "id"
        const val itemId = "itemId"
        const val itemName = "itemName"
        const val itemFullName = "itemFullName"
        const val itemDesc = "itemDesc"
        const val itemImage = "itemImage"
        const val language = "language"
        const val totalStar = "totalStar"
        const val totalFork = "totalFork"
    }
}
