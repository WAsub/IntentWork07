package sk2a_2190073.intentwork07

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.ParseException

class MainActivity : AppCompatActivity() {

    data class MEMOListdata(val Onum: Int, val memo: String)
    private lateinit var MEMO: MEMOListdata;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.lvMain)
        val listMEMO: List<MEMOListdata> = getMEMOList()

        listView.adapter = CustomAdapter(this,listMEMO)
        listView.onItemClickListener = LIClick()
    }

    private inner class LIClick: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (parent != null)
                MEMO = parent.getItemAtPosition(position) as MEMOListdata
            when (id.toInt()) {
                R.id.btn_del -> {
                    //確認ダイアログ表示
                    val toast = Toast.makeText(applicationContext, "ボタンが押されました。", Toast.LENGTH_LONG)
                    toast.show()
                }
                R.id.lv_memo -> {
                    //確認ダイアログ表示
                    val toast = Toast.makeText(applicationContext, "リストが押されました。", Toast.LENGTH_LONG)
                    toast.show()

                    try{
                        val intent = Intent(applicationContext, EditActivity::class.java)

                        intent.putExtra("orderN",MEMO.Onum)
                        startActivity(intent)
                    }catch (e: Exception){

                    }
                }
            }
        }
    }

    private fun getMEMOList(): MutableList<MEMOListdata> {
        var list: MutableList<MEMOListdata>
        val sqlite = SQLiteProcess(this@MainActivity)
        list = sqlite.selectMEMOList()

        return list
    }
}
