package sk2a_2190073.intentwork07

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class EditActivity : AppCompatActivity() {

    data class MEMOdata(val Onum: Int, val Mnum: Int, val memo: String, var backColor: Int)

    private var _etmemo: EditText? = null
    private var MEMO: Array<MEMOdata> = arrayOf()
    private var Onum: Int = 0
    private var _btncolor: Button? = null
    private val C_id: Array<Int> = arrayOf(
        R.color.C_generate,
        R.color.C_coral,
        R.color.C_citrus,
        R.color.C_rapeBlossoms,
        R.color.C_brightGreen,
        R.color.C_bamboo,
        R.color.C_sky,
        R.color.C_hakuai,
        R.color.C_wisteria,
        R.color.C_dull
    )
    private val C_draw: Array<Int> = arrayOf(
        R.drawable.btn_color_generate,
        R.drawable.btn_color_coral,
        R.drawable.btn_color_citrus,
        R.drawable.btn_color_rapeblossoms,
        R.drawable.btn_color_brightgreen,
        R.drawable.btn_color_bamboo,
        R.drawable.btn_color_sky,
        R.drawable.btn_color_hakuai,
        R.drawable.btn_color_wisteria,
        R.drawable.btn_color_dull
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        /** インテントからデータを取得する */
        val intent = intent
        if (intent != null) {
            if (intent.hasExtra("orderN")) {
                Onum = intent.getIntExtra("orderN", 0)
            }
        }
        /** メモデータ取得 */
        if(Onum == 0){
            //新規作成
            MEMO += MEMOdata(Onum, 0, "", 0)
        }else{
            //編集
            val sqlite = SQLiteProcess(this@EditActivity)
            MEMO = sqlite.selectMEMO(Onum)
        }
        _etmemo = findViewById(R.id.etmemo)
        _etmemo!!.background = getDrawable(C_id[MEMO[0].backColor])
        _etmemo!!.setText(MEMO[0].memo)
        val _btnsave: Button = findViewById(R.id.btnsave)
        _btnsave.setOnClickListener(BClick())
        _btncolor = findViewById(R.id.btncolor)
        _btncolor!!.background = getDrawable(C_draw[MEMO[0].backColor])
        _btncolor!!.setOnClickListener(BClick())

    }
    /** アクションバーの読み込み */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }
    /** アクションバーのボタンの処理 */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.actRemove -> {
            /** 削除ボタン */
//            val toast = Toast.makeText(applicationContext, "アクションが押されました。", Toast.LENGTH_SHORT)
//            toast.show()
            val dlg = AlertDialog.Builder(this@EditActivity)

            dlg.setTitle("削除")
            dlg.setMessage("削除しますか")
            dlg.setPositiveButton("削除") { _, _ ->
                val sqlite = SQLiteProcess(this@EditActivity)
                sqlite.deleteMEMO(MEMO)
                //付箋リストに戻る
                onBackPressed()
            }
            dlg.setNegativeButton("いいえ") { _, _ ->

            }
            dlg.show()

            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    /** バックボタンを押したら付箋リストに戻る */
    override fun onBackPressed() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }

    private inner class BClick: View.OnClickListener{
        @SuppressLint("ResourceAsColor")
        override fun onClick(v: View) {
            when(v.id){
                R.id.btnsave -> {
                    //保存
                    if(Onum == 0){
                        /** 新規保存 */
//                        val dlg = AlertDialog.Builder(this@EditActivity)
//
//                        dlg.setTitle("新規保存")
//                        dlg.setMessage("保存しますか")
//                        dlg.setPositiveButton("保存") { _, _ ->
//                            var sb = _etmemo!!.text.toString()
//                            val sqlite = SQLiteProcess(this@EditActivity)
//                            sqlite.insertMEMO(sb, MEMO[0].backColor)
//                        }
//                        dlg.setNegativeButton("いいえ") { _, _ ->
//
//                        }
//                        dlg.show()
                        var sb = _etmemo!!.text.toString()
                        val sqlite = SQLiteProcess(this@EditActivity)
                        sqlite.insertMEMO(sb, MEMO[0].backColor)
                    }else{
//                        val dlg = AlertDialog.Builder(this@EditActivity)
//
//                        dlg.setTitle("保存")
//                        dlg.setMessage("保存しますか")
//                        dlg.setPositiveButton("保存") { _, _ ->
//                            var sb = _etmemo!!.text.toString()
//                            val sqlite = SQLiteProcess(this@EditActivity)
//                            sqlite.updateMEMO(MEMO[0].Mnum, sb, MEMO[0].backColor)
//                        }
//                        dlg.setNegativeButton("いいえ") { _, _ ->
//
//                        }
//                        dlg.show()
                        var sb = _etmemo!!.text.toString()
                        val sqlite = SQLiteProcess(this@EditActivity)
                        sqlite.updateMEMO(MEMO[0].Mnum, sb, MEMO[0].backColor)
                    }

                    //付箋リストに戻る
                    onBackPressed()
                }
                R.id.btncolor -> {
                    if(++MEMO[0].backColor == C_id.count())
                        MEMO[0].backColor = 0

                    _etmemo!!.background = getDrawable(C_id[MEMO[0].backColor])
                    _btncolor!!.background = getDrawable(C_draw[MEMO[0].backColor])
                }

            }
        }
    }


}
