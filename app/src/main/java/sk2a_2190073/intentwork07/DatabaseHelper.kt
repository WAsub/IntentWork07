package sk2a_2190073.intentwork07

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        //データベースファイル名の定数フィールド
        private const val DATABASE_NAME = "memomemo.db"
        //バージョン情報の定数フィールド
        private const val DATABASE_VERSION = 1
        //テーブル作成用
        const val CREATE_TABLE_MEMO = ("CREATE TABLE memo ("
                + "_id INTEGER PRIMARY KEY,"
                + "memo TEXT"
                + ");")
        const val CREATE_TABLE_ORDER = ("CREATE TABLE memoOrder ("
                + "_id INTEGER PRIMARY KEY,"
                + "memoId INTEGER,"
                + "FOREIGN KEY(memoId) REFERENCES memo(_id)"
                + ");")
        const val INSERT_MEMO = (
                "INSERT INTO memo (memo) VALUES (\"あいうえおかきくけこさしすせそ\nあいうえお\")"
                )
        const val INSERT_MEMO_ORDER = (
                "INSERT INTO memoOrder (memoId) VALUES (1)"
                )
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_MEMO) //メモ表
        db.execSQL(CREATE_TABLE_ORDER) //メモの順番
        db.execSQL(INSERT_MEMO)
        db.execSQL(INSERT_MEMO_ORDER)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
    }
}