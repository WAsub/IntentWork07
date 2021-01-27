package sk2a_2190073.intentwork07

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class EditActivity : AppCompatActivity() {

    data class MEMOdata(val Onum: Int, val Mnum: Int, val memo: String)
    private var MEMO: MutableList<MEMOdata> = mutableListOf()
    private var Onum: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        //インテントからデータを取得する
        val intent = intent
        if (intent != null) {
            if (intent.hasExtra("orderN")) {
                Onum = intent.getIntExtra("orderN", 0)
            }
        }
        if(Onum == 0){
            MEMO.add(MEMOdata(Onum, 0, ""))
        }else{
            val sqlite = SQLiteProcess(this@EditActivity)
            var me:MutableList<MEMOdata> = sqlite.selectMEMO(Onum)
            MEMO.add(MEMOdata(me[0].Onum, me[0].Mnum, me[0].memo))
            val _etmemo: EditText= findViewById(R.id.etmemo)
            _etmemo.setText(MEMO[0].memo)
        }
        val _btnsave: Button = findViewById<Button>(R.id.btnsave)
        _btnsave.setOnClickListener(BClick())

    }
    override fun onBackPressed() {
        val toast = Toast.makeText(applicationContext, "ボタンが押されました。", Toast.LENGTH_SHORT)
        toast.show()

        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
    private inner class BClick: View.OnClickListener{
        override fun onClick(v: View) {
            when(v.id){
                R.id.btnsave -> {
                    //保存
                    if(Onum == 0){
                        /** 新規保存 */
                        val dlg = AlertDialog.Builder(this@EditActivity)

                        dlg.setTitle("新規保存")
                        dlg.setMessage("保存しますか")
                        dlg.setPositiveButton("保存") { _, _ ->
                            val _etmemo: EditText= findViewById(R.id.etmemo)
                            var sb = _etmemo.text.toString()
                            val sqlite = SQLiteProcess(this@EditActivity)
                            sqlite.insertMEMO(sb)
                        }
                        dlg.setNegativeButton("いいえ") { _, _ ->

                        }
                        dlg.show()
                    }else{
                        val dlg = AlertDialog.Builder(this@EditActivity)

                        dlg.setTitle("保存")
                        dlg.setMessage("保存しますか")
                        dlg.setPositiveButton("保存") { _, _ ->
                            val _etmemo: EditText= findViewById(R.id.etmemo)
                            var sb = _etmemo.text.toString()
                            val sqlite = SQLiteProcess(this@EditActivity)
                            sqlite.updateMEMO(MEMO[0].Mnum,sb)

                        }
                        dlg.setNegativeButton("いいえ") { _, _ ->

                        }
                        dlg.show()
                    }
                }
                R.id.btncolor -> {

                }

            }
        }
    }


}
