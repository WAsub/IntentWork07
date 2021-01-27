package sk2a_2190073.intentwork07

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

class SQLiteProcess(private val con: Context){

    data class MemoOrder(val Onum: Int, val Mnum: Int)
    fun selectMEMOList(): Array<MainActivity.MEMOListdata>{
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        var items: Array<MainActivity.MEMOListdata> = arrayOf()
        try{
            val sqlSEL = "SELECT mO._id, SUBSTR(m.memo, 1, 20) AS memoV " +
                         "FROM memoOrder AS mO " +
                         "INNER JOIN memo AS m " +
                         "ON mO.memoId = m._id"
            val cursor = db.rawQuery(sqlSEL, null)
            while(cursor.moveToNext()){
                val idxID = cursor.getColumnIndex("_id")
                val idxmemoV = cursor.getColumnIndex("memoV")
                items += MainActivity.MEMOListdata(
                    cursor.getInt(idxID),
                    cursor.getString(idxmemoV)
                )
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
            val sqlSEL = "SELECT memoOrder._id, memoOrder.memoId, memo.memo " +
                    "FROM memoOrder " +
                    "INNER JOIN memo " +
                    "ON memoOrder.memoId = memo._id " +
                    "WHERE memoOrder._id = ${orderN}"
            val cursor = db.rawQuery(sqlSEL, null)
            while(cursor.moveToNext()){
                val idxID = cursor.getColumnIndex("_id")
                val idxmemoID = cursor.getColumnIndex("memoId")
                val idxmemo = cursor.getColumnIndex("memo")
                items.add(EditActivity.MEMOdata(
                    cursor.getInt(idxID),
                    cursor.getInt(idxmemoID),
                    cursor.getString(idxmemo)
                ))
            }
        }finally {
            db.close()
        }
        return items
    }
    fun updateMEMO(ID: Int, memo: String) {
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        try {
            val sqlUP = "UPDATE memo SET memo = \"${memo}\" WHERE _id = ${ID}"
            val stmt = db.compileStatement(sqlUP)
            stmt.executeUpdateDelete()
        } finally {
            db.close()
        }

    }
    fun selectMemoOrder(): MutableList<MemoOrder>{
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        val items: MutableList<MemoOrder> = mutableListOf()
        try{
            val sqlSEL = "SELECT * FROM memoOrder"
            val cursor = db.rawQuery(sqlSEL, null)
            while(cursor.moveToNext()){
                val idxID = cursor.getColumnIndex("_id")
                val idxmemoID = cursor.getColumnIndex("memoId")
                items.add(MemoOrder(
                    cursor.getInt(idxID),
                    cursor.getInt(idxmemoID)
                ))
            }
        }finally {
            db.close()
        }
        return items
    }
    private fun renewMemoOrder(MO: MutableList<MemoOrder>){
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        try{
            val sqlDEL = "DELETE FROM memoOrder"
            var stmt = db.compileStatement(sqlDEL)
            stmt.executeUpdateDelete()

            for(i in MO) {
                val sqlIN = "INSERT INTO memoOrder(memoId) VALUES(${i.Mnum})"
                stmt = db.compileStatement(sqlIN)
                stmt.executeInsert()
            }
        }finally {
            db.close()
        }
    }
    private fun renewMemoOrder(MO: ArrayList<Int>){
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        try{
            val sqlDEL = "DELETE FROM memoOrder"
            var stmt = db.compileStatement(sqlDEL)
            stmt.executeUpdateDelete()

            for(i in MO) {
                val sqlIN = "INSERT INTO memoOrder(memoId) VALUES(${i})"
                stmt = db.compileStatement(sqlIN)
                stmt.executeInsert()
            }
        }finally {
            db.close()
        }
    }
    private fun getM_id(): ArrayList<Int>{
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        var M_id: ArrayList<Int> = arrayListOf()
        try{
            val sqlSEL = "SELECT memoId FROM memoOrder ORDER BY _id asc"
            val cursor = db.rawQuery(sqlSEL, null)
            while(cursor.moveToNext()){
                val idxmemoID = cursor.getColumnIndex("memoId")
                M_id.add(cursor.getInt(idxmemoID))
            }
        }catch(e: Exception){
        }finally {
            db.close()
        }
        return M_id
    }
    private fun getM_id(O_id: Int): Int{
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        var M_id: Int = 0;
        try{
            val sqlSEL = "SELECT memoId FROM memoOrder WHERE _id = ${O_id}"
            val cursor = db.rawQuery(sqlSEL, null)
            while(cursor.moveToNext()){
                val idxmemoID = cursor.getColumnIndex("memoId")
                M_id = cursor.getInt(idxmemoID)
            }
        }finally {
            db.close()
        }
        return M_id
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun deleteMEMO(O_id: Int){
        val M_id: Int = getM_id(O_id)

        var MO: MutableList<MemoOrder> = selectMemoOrder()
        MO.removeIf{ it.Mnum == M_id }
        renewMemoOrder(MO)

        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        try {
            var sqlDEL = "DELETE FROM memo WHERE _id = ${M_id}"
            var stmt = db.compileStatement(sqlDEL)
            stmt.executeUpdateDelete()
        } finally {
            db.close()
        }
    }
    private fun getMAXM_id(): Int{
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        var M_id: Int = 0;
        try{
            val sqlSEL = "SELECT MAX(_id) FROM memo"
            val cursor = db.rawQuery(sqlSEL, null)
            while(cursor.moveToNext()){
                val idxmemoID = cursor.getColumnIndex("MAX(_id)")
                M_id = cursor.getInt(idxmemoID)
            }
        }finally {
            db.close()
        }
        return M_id
    }
    fun insertMEMO(memo: String){
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        try {
            var sqlIN = "INSERT INTO memo(memo) VALUES(\"${memo}\")"
            var stmt = db.compileStatement(sqlIN)
            stmt.executeInsert()

            val M_id: Int = getMAXM_id()

            sqlIN = "INSERT INTO memoOrder(memoId) VALUES(${M_id})"
            stmt = db.compileStatement(sqlIN)
            stmt.executeInsert()
        } finally {
            db.close()
        }
    }

    fun sortMemoOrder(items: Array<MainActivity.MEMOListdata>,_lvMain: SortableListView,mDraggingPosition: Int){
        var M_id: ArrayList<Int> = getM_id()
        var newM_id: ArrayList<Int> = arrayListOf()
        var newitems: Array<MainActivity.MEMOListdata> = arrayOf()

        var j = 1
        for(i in items) {
            newM_id.add(M_id[i.Onum-1])
            newitems += MainActivity.MEMOListdata(j,i.memo)
            j++
        }
        renewMemoOrder(newM_id)

        _lvMain.adapter = CustomAdapter2(con as Activity,newitems,mDraggingPosition)
    }


}