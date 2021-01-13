package sk2a_2190073.intentwork07

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class CustomAdapter(private val activity: Activity,private val items: List<MainActivity.MEMOListdata>) : BaseAdapter() {

    private class ViewHolder(row: View) {
        val memo = row.findViewById(R.id.lv_memo) as TextView
        val btn_del  = row.findViewById(R.id.btn_del) as Button
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_layout, convertView) //リストレイアウト指定
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val ITEM = items[position]
        viewHolder.memo.text = ITEM.memo
        viewHolder.memo.setOnClickListener(){
            (parent as ListView).performItemClick(view , position , getItemId(R.id.lv_memo))
        }
        viewHolder.btn_del.text = "ー"
        viewHolder.btn_del.setOnClickListener(){
            (parent as ListView).performItemClick(view , position , getItemId(R.id.btn_del))
        }

        return view
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }
}
//import android.content.Context
//import android.media.SoundPool
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.animation.AlphaAnimation
//import android.widget.*
//import android.widget.BaseAdapter
//
//
//class CustomAdapter(
//    private val context: Context,
//    private var inflater: LayoutInflater,
//    private var layoutId: Int,
//    private var yz: List<MainActivity.Buttons>
//) : BaseAdapter()  {
//    internal data class ViewHolder(val _memo:TextView,val _del:Button)
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        var view = convertView
//        val viewHolder : ViewHolder
//        if (convertView == null) {
//            view = inflater.inflate(layoutId,parent,false)
//            viewHolder = ViewHolder(view.findViewById(R.id.lv_memo),view.findViewById(R.id.btn_del))
//            view.tag = viewHolder
//        } else {
//            viewHolder = view!!.tag as ViewHolder
//        }
//        viewHolder._memo.setText(yz[position].memo)
////        val soundId = soundPool.load(context, yz[position].sound, 1)
//        viewHolder._del.setText(yz[position].del)
////        fadeout(viewHolder.image,0)
//        viewHolder._del.setOnClickListener() {
////            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
////
////            fadeout(viewHolder.image,3000)
//        }
//        return view!!
//    }
//
//    override fun getCount(): Int {
//        return yz.size
//    }
//
//    override fun getItem(position: Int): Any {
//        return position
//    }
//
//    override fun getItemId(position: Int):Long {
//        return position.toLong()
//    }
//
//}
//private fun fadeout(imageView:ImageView,Duration:Long){
//    val alphaFadeOut = AlphaAnimation(1.0f,0.0f)
//    alphaFadeOut.setDuration(Duration)
//    alphaFadeOut.setFillAfter(true)
//    imageView.startAnimation(alphaFadeOut)
//}