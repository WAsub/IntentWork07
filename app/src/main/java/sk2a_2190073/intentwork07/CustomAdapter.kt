package sk2a_2190073.intentwork07

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class CustomAdapter(private val activity: Activity,private val items: List<MainActivity.MEMOListdata>,private val mDraggingPosition: Int) : BaseAdapter() {

    private class ViewHolder(row: View) {
        val memon = row.findViewById(R.id.lv_memon) as TextView//
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
        viewHolder.memon.text = ITEM.Onum.toString()//
        viewHolder.memo.text = ITEM.memo
        viewHolder.memo.setOnClickListener(){
            (parent as SortableListView).performItemClick(view , position , 0)
        }
        viewHolder.memo.setOnLongClickListener(){
            (parent as SortableListView).performItemClick(view , position , getItemId(R.id.lv_memo))
        }
        viewHolder.btn_del.text = "ー"
        viewHolder.btn_del.setOnClickListener(){
            (parent as SortableListView).performItemClick(view , position , getItemId(R.id.btn_del))
        }
        view.visibility = if (position == mDraggingPosition) View.INVISIBLE else View.VISIBLE

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
