package sk2a_2190073.intentwork07

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.View.OnDragListener


class MainActivity : AppCompatActivity() {

    data class MEMOListdata(val Onum: Int, val memo: String)
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
    fun Load(){
        val listView: ListView = findViewById(R.id.lvMain)
        val listMEMO: List<MEMOListdata> = getMEMOList()

        _lvMain = findViewById<View>(R.id.lvMain) as SortableListView
        _lvMain!!.setDragListener(DragListener(mDraggingPosition, _lvMain, listMEMO.toTypedArray()))
        _lvMain!!.setSortable(true)
        _lvMain!!.adapter = CustomAdapter(this,listMEMO,mDraggingPosition)
//        listView.adapter = CustomAdapter(this,listMEMO)
        _lvMain!!.onItemClickListener = LIClick()
//        _lvMain!!.onItemLongClickListener = LILongClick()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?){
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK && requestCode == 1000 && null != intent) {
            Load()
        }
    }

    private inner class LIClick: AdapterView.OnItemClickListener{
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (parent != null)
                MEMO = parent.getItemAtPosition(position) as MEMOListdata
            when (id.toInt()) {
                R.id.lv_memo ->{
                    val toast = Toast.makeText(applicationContext, position.toString() +"ロングが押されました。", Toast.LENGTH_SHORT)
                    toast.show()
//                    _lvMain!!.onItemLongClick(parent,view,position,id)
                }
                R.id.btn_del -> {
                    //確認ダイアログ表示
                    val toast = Toast.makeText(applicationContext, "ボタンが押されました。", Toast.LENGTH_LONG)
                    toast.show()

                    val dlg = AlertDialog.Builder(this@MainActivity)

                    dlg.setTitle("削除")
                    dlg.setMessage("削除しますか")
                    dlg.setPositiveButton("削除") { _, _ ->
                        val sqlite = SQLiteProcess(this@MainActivity)
                        sqlite.deleteMEMO(MEMO.Onum)
                        Load()
                    }
                    dlg.setNegativeButton("いいえ") { _, _ ->

                    }
                    dlg.show()
                }
                0 -> {
                    //確認ダイアログ表示
                    val toast = Toast.makeText(applicationContext, "リストが押されました。", Toast.LENGTH_LONG)
                    toast.show()
                    try{
                        val intent = Intent(this@MainActivity, EditActivity::class.java)
                        intent.putExtra("orderN",MEMO.Onum)
                        startActivityForResult( intent, 1000 )
                    }catch (e: Exception){

                    }
                }
            }
        }
    }
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

    fun onDrag(v: View?, event: DragEvent): Boolean {
        val result: Boolean
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> result = true
            DragEvent.ACTION_DRAG_LOCATION -> result = true
            DragEvent.ACTION_DROP -> {
                _droppedX = event.x.toInt()
                _droppedY = event.y.toInt()
                result = true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                val rect = Rect()

                // 各子viewの領域を取得して、ドロップした座標が含まれているかチェックする
                var i = 0
                while (i < getListView().getChildCount()) {
                    getListView().getChildAt(i).getHitRect(rect)
                    if (rect.contains(_droppedX, _droppedY)) {
                        val droppedPosition: Int = getListView().getFirstVisiblePosition() + i
                        Log.d(
                            com.websarva.wings.android.draganddrop.TextListFragment.TAG,
                            "droppedPosition:$droppedPosition"
                        )

                        // Itemをコピー
                        val title: String =
                            _itemArrayAdapter.getItem(_longClickedPosition).getTitle()
                        val body: String =
                            _itemArrayAdapter.getItem(_longClickedPosition).getBody()
                        val copyItem = Item(title, body)

                        // Itemを削除
                        _itemArrayAdapter.remove(_itemArrayAdapter.getItem(_longClickedPosition))

                        // Itemをドロップした位置に挿入
                        _itemArrayAdapter.insert(copyItem, droppedPosition)
                        break
                    }
                    i++
                }
                result = true
            }
            else -> result = false
        }
        return result
    }
    private fun getMEMOList(): MutableList<MEMOListdata> {
        var list: MutableList<MEMOListdata>
        val sqlite = SQLiteProcess(this)
        list = sqlite.selectMEMOList()

        return list
    }

}
