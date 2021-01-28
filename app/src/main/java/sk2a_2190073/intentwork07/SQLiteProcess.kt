package sk2a_2190073.intentwork07

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

class SQLiteProcess(private val con: Context){

    data class MemoOrder(val Onum: Int, val Mnum: Int)

    /** 付箋リスト取得用 *///TODO 背景色パラメータ
    fun selectMEMOList(): Array<MainActivity.MEMOListdata>{
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        var items: Array<MainActivity.MEMOListdata> = arrayOf()
        try{
            val sqlSEL = "SELECT mO._id, SUBSTR(m.memo, 1, 20) AS memoV, m.backColor " +
                         "FROM memoOrder AS mO " +
                         "INNER JOIN memo AS m " +
                         "ON mO.memoId = m._id"
            val cursor = db.rawQuery(sqlSEL, null)
            while(cursor.moveToNext()){
                val idxID = cursor.getColumnIndex("_id")
                val idxmemoV = cursor.getColumnIndex("memoV")
                val idxcolor = cursor.getColumnIndex("backColor")
                items += MainActivity.MEMOListdata(
                    cursor.getInt(idxID),
                    cursor.getString(idxmemoV),
                    cursor.getInt(idxcolor)
                )
            }
        }finally {
            db.close()
        }
        return items
    }
    /**********************************************************************************************/

    /** 編集するメモ取得用 */
    fun selectMEMO(orderN: Int): Array<EditActivity.MEMOdata>{
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        var items: Array<EditActivity.MEMOdata> = arrayOf()
        try{
            val sqlSEL = "SELECT memoOrder._id, memoOrder.memoId, memo.memo, memo.backColor " +
                    "FROM memoOrder " +
                    "INNER JOIN memo " +
                    "ON memoOrder.memoId = memo._id " +
                    "WHERE memoOrder._id = ${orderN}"
            val cursor = db.rawQuery(sqlSEL, null)
            while(cursor.moveToNext()){
                val idxID = cursor.getColumnIndex("_id")
                val idxmemoID = cursor.getColumnIndex("memoId")
                val idxmemo = cursor.getColumnIndex("memo")
                val idxcolor = cursor.getColumnIndex("backColor")
                items += EditActivity.MEMOdata(
                    cursor.getInt(idxID),
                    cursor.getInt(idxmemoID),
                    cursor.getString(idxmemo),
                    cursor.getInt(idxcolor)
                )
            }
        }finally {
            db.close()
        }
        return items
    }
    /**********************************************************************************************/

    /** 編集したメモ保存用 */
    fun updateMEMO(ID: Int, memo: String, backColor: Int) {
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        try {
            val sqlUP = "UPDATE memo SET memo = \"${memo}\", backColor = ${backColor} WHERE _id = ${ID}"
            val stmt = db.compileStatement(sqlUP)
            stmt.executeUpdateDelete()
        } finally {
            db.close()
        }
    }
    /**********************************************************************************************/

    /** 新規作成したメモの保存用 */
    fun insertMEMO(memo: String, backColor: Int){
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        try {
            /** memo表に登録 */
            var sqlIN = "INSERT INTO memo(memo, backColor) VALUES(\"${memo}\", ${backColor})"
            var stmt = db.compileStatement(sqlIN)
            stmt.executeInsert()

            /** 先ほど登録したメモのIDを取得 */
            val M_id: Int = getMAXM_id()

            /** memoOrder表の最後に登録 */
            sqlIN = "INSERT INTO memoOrder(memoId) VALUES(${M_id})"
            stmt = db.compileStatement(sqlIN)
            stmt.executeInsert()
        } finally {
            db.close()
        }
    }
    // 新規作成したメモIDを取得(memo　の主キーの一番大きい数字を取得)
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
    /**********************************************************************************************/

    /** メモの削除用 */
    fun deleteMEMO(MEMODATA: Array<EditActivity.MEMOdata>){
        /** 旧付箋リストのメモIDリストを取得 */
        val M_id: Array<Int> = getM_id()

        /** 削除するメモを取り除く */
        var newM_id: Array<Int> = arrayOf()
        for(M in M_id){
            if(M != MEMODATA[0].Mnum){
                newM_id += M
            }
        }
        /** 新しい memoOrder を登録 */
        renewMemoOrder(newM_id)

        /** メモ本体を削除 */
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        try {
            var sqlDEL = "DELETE FROM memo WHERE _id = ${MEMODATA[0].Mnum}"
            var stmt = db.compileStatement(sqlDEL)
            stmt.executeUpdateDelete()
        } finally {
            db.close()
        }
    }
    /**********************************************************************************************/

    /** メモの並び替え */
    fun sortMemoOrder(items: Array<MainActivity.MEMOListdata>, _lvMain: SortableListView, mDraggingPosition: Int, con: Context){
        /** 旧付箋リストのメモIDリストを取得 */
        val M_id: Array<Int> = getM_id()

        /** itemsを基準に新しいものに並び替え */
        var newM_id: Array<Int> = arrayOf()
        for(I in items) {
            newM_id += M_id[I.Onum - 1]
        }
        /** 新しい memoOrder を登録 */
        renewMemoOrder(newM_id)

        /** 新しい付箋リストを取得 */
        val newitems = selectMEMOList()
        /** 付箋リストを更新 */
        _lvMain.adapter = CustomAdapter2(con as Activity,newitems,mDraggingPosition, con)
        _lvMain.setDragListener(DragListener(mDraggingPosition, _lvMain, newitems,con, _lvMain))
        _lvMain.setSortable(true)
    }
    // 新しい順番のmemoOrderを登録
    private fun renewMemoOrder(newM_id: Array<Int>){
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        try{
            val sqlDEL = "DELETE FROM memoOrder"
            var stmt = db.compileStatement(sqlDEL)
            stmt.executeUpdateDelete()

            for(i in newM_id) {
                val sqlIN = "INSERT INTO memoOrder(memoId) VALUES(${i})"
                stmt = db.compileStatement(sqlIN)
                stmt.executeInsert()
            }
        }finally {
            db.close()
        }
    }
    // memoOrder 主キー昇順　に並んだ memoId のリスト
    private fun getM_id(): Array<Int>{
        val helper = DatabaseHelper(con)
        val db = helper.writableDatabase
        var M_id: Array<Int> = arrayOf()
        try{
            val sqlSEL = "SELECT memoId FROM memoOrder ORDER BY _id asc"
            val cursor = db.rawQuery(sqlSEL, null)
            while(cursor.moveToNext()){
                val idxmemoID = cursor.getColumnIndex("memoId")
                M_id += cursor.getInt(idxmemoID)
            }
        }catch(e: Exception){
        }finally {
            db.close()
        }
        return M_id
    }
    /**********************************************************************************************/

}