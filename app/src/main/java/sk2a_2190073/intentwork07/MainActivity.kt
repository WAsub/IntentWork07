package sk2a_2190073.intentwork07

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    data class MEMOListdata(val Onum: Int, val memo: String, var backColor: Int)
    private lateinit var MEMO: MEMOListdata
    var _lvMain: SortableListView? = null
    var mDraggingPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Load()
        val _btnsave: Button = findViewById(R.id.btncreate)
        _btnsave.setOnClickListener(BClick())

    }

    /** EditActivityから戻ってきたときの動作 */
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?){
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK && requestCode == 1000 && null != intent) {
            Load() //リストを再読み込み
        }
    }

    /** リストの再読み込みをまとめたやつ */
    fun Load(){
        val sqlite = SQLiteProcess(this)
        val listMEMO: Array<MEMOListdata> = sqlite.selectMEMOList()

        _lvMain = findViewById<View>(R.id.lvMain) as SortableListView
        _lvMain!!.setDragListener(DragListener(mDraggingPosition, _lvMain, listMEMO,this, _lvMain))
        _lvMain!!.setSortable(true)
        _lvMain!!.adapter = CustomAdapter2(this, listMEMO, mDraggingPosition, this@MainActivity)
        _lvMain!!.onItemClickListener = LIClick()
    }

    /** 付箋の編集へ */
    private inner class LIClick: AdapterView.OnItemClickListener{
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (parent != null)
                MEMO = parent.getItemAtPosition(position) as MEMOListdata

            //確認ダイアログ表示
            try{
                val intent = Intent(this@MainActivity, EditActivity::class.java)
                intent.putExtra("orderN",MEMO.Onum)
                startActivityForResult( intent, 1000 )
            }catch (e: Exception){
            }

        }
    }

    /** 新規作成 */
    private inner class BClick: View.OnClickListener{
        override fun onClick(v: View) {
            when(v.id){
                R.id.btncreate -> {
                    val intent = Intent(this@MainActivity, EditActivity::class.java)
                    startActivityForResult( intent, 1000 )
                }
            }
        }
    }

}
