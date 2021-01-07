package sk2a_2190073.intentwork07

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val TAG: String = "toridge.main"


    data class HMdata(val number: String, val name: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.lvMain)
        val listHM: List<HMdata> = getHMList()

        val adapter = CustomAdapter(this,listHM)
        listView.adapter = adapter
        listView.onItemClickListener = LIClick()
        val toast = Toast.makeText(applicationContext, "が押されました。", Toast.LENGTH_LONG)
              toast.show()
    }

    private inner class LIClick: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

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
                }
            }
        }
    }

    private fun getHMList(): MutableList<HMdata> {
        val list: MutableList<HMdata> = mutableListOf()


        list.add(HMdata("AAaaaaa", "++"))
        list.add(HMdata("AAaaaaa", "++"))

        return list
    }
}
