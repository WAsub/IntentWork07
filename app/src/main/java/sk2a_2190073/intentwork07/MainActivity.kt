package sk2a_2190073.intentwork07

import android.R.attr.data
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    data class MEMOListdata(val Onum: Int, val memo: String)
    private lateinit var MEMO: MEMOListdata
    var _lvMain: SortableListView? = null
    var adapter:CustomAdapter2? = null;
    var mDraggingPosition = -1
    private var _longClickedPosition = 0
    private var _droppedX = 0
    private var _droppedY = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Load()
        val _btnsave: Button = findViewById(R.id.btncreate)
        _btnsave.setOnClickListener(BClick())
    }
    fun Load(){
        val listView: ListView = findViewById(R.id.lvMain)
        val listMEMO: Array<MEMOListdata> = getMEMOList()

        _lvMain = findViewById<View>(R.id.lvMain) as SortableListView
        _lvMain!!.setDragListener(DragListener(mDraggingPosition, _lvMain, listMEMO,this, _lvMain))
        _lvMain!!.setSortable(true)
        _lvMain!!.adapter = CustomAdapter2(this,listMEMO,mDraggingPosition)
//        listView.adapter = CustomAdapter(this,listMEMO)
        _lvMain!!.onItemClickListener = LIClick()
//        _lvMain!!.setOnDragListener(Drag())
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

            //確認ダイアログ表示
            try{
                val intent = Intent(this@MainActivity, EditActivity::class.java)
                intent.putExtra("orderN",MEMO.Onum)
                startActivityForResult( intent, 1000 )
            }catch (e: Exception){
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

//    private inner class Drag: View.OnDragListener{
//        override fun onDrag(v: View?, event: DragEvent?): Boolean {
//            val result: Boolean
//            when (event!!.action) {
//                DragEvent.ACTION_DRAG_STARTED -> result = true
//                DragEvent.ACTION_DRAG_LOCATION -> result = true
//                DragEvent.ACTION_DROP -> {
//                    _droppedX = event!!.x.toInt()
//                    _droppedY = event!!.y.toInt()
//                    result = true
//                }
//                DragEvent.ACTION_DRAG_ENDED -> {
////                    val rect = Rect()
////
////                    // 各子viewの領域を取得して、ドロップした座標が含まれているかチェックする
////                    var i = 0
////                    while (i < getListView().getChildCount()) {
////                        getListView().getChildAt(i).getHitRect(rect)
////                        if (rect.contains(_droppedX, _droppedY)) {
////                            val droppedPosition: Int = getListView().getFirstVisiblePosition() + i
////                            Log.d(
////                                com.websarva.wings.android.draganddrop.TextListFragment.TAG,
////                                "droppedPosition:$droppedPosition"
////                            )
////
////                            // Itemをコピー
////                            val title: String =
////                                _itemArrayAdapter.getItem(_longClickedPosition).getTitle()
////                            val body: String =
////                                _itemArrayAdapter.getItem(_longClickedPosition).getBody()
////                            val copyItem = Item(title, body)
////
////                            // Itemを削除
////                            _itemArrayAdapter.remove(_itemArrayAdapter.getItem(_longClickedPosition))
////
////                            // Itemをドロップした位置に挿入
////                            _itemArrayAdapter.insert(copyItem, droppedPosition)
////                            break
////                        }
////                        i++
////                    }
//                    result = true
//                }
//                else -> result = false
//            }
//            return result
//        }
//
//    }


    private fun getMEMOList(): Array<MEMOListdata> {
        var list: Array<MEMOListdata>
        val sqlite = SQLiteProcess(this)
        list = sqlite.selectMEMOList()

        return list
    }

}
