package sk2a_2190073.intentwork07

import android.content.Context
import java.security.AccessControlContext

class SQLiteProcess(private val con: Context){

    fun selectMEMOList(): MutableList<MainActivity.MEMOListdata>{
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        val items: MutableList<MainActivity.MEMOListdata> = mutableListOf()
        try{
            val sqlSEL = "SELECT mO._id, SUBSTR(m.memo, 1, 20) AS memoV " +
                         "FROM memoOrder AS mO " +
                         "INNER JOIN memo AS m " +
                         "ON mO.memoId = m._id"
            val cursor = db.rawQuery(sqlSEL, null)
            while(cursor.moveToNext()){
                val idxID = cursor.getColumnIndex("_id")
                val idxmemoV = cursor.getColumnIndex("memoV")
                items.add(MainActivity.MEMOListdata(
                    cursor.getInt(idxID),
                    cursor.getString(idxmemoV)
                ))
            }
        }finally {
            db.close()
        }
        return items
    }
    fun selectMEMO(orderN: Int): MutableList<EditActivity.MEMOdata>{
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        val items: MutableList<EditActivity.MEMOdata> = mutableListOf()
        try{
            val sqlSEL = "SELECT mO.memoId, m.memo " +
                    "FROM memoOrder AS mO " +
                    "INNER JOIN memo AS m " +
                    "ON mO.memoId = m._id" +
                    "WHERE mO._id = " + orderN
            val cursor = db.rawQuery(sqlSEL, null)
            while(cursor.moveToNext()){
                val idxmemoID = cursor.getColumnIndex("memoId")
                val idxmemo = cursor.getColumnIndex("memo")
                items.add(EditActivity.MEMOdata(
                    cursor.getInt(idxmemoID),
                    cursor.getString(idxmemo)
                ))
            }
        }finally {
            db.close()
        }
        return items
    }
}