package sk2a_2190073.intentwork07

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*

class CustomAdapter2(private val activity: Activity,private val items: Array<MainActivity.MEMOListdata>, private val mDraggingPosition: Int) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView1 = convertView
        if (convertView1 == null) {
            convertView1 = (activity as Activity).getLayoutInflater().inflate(
                R.layout.list_layout, null
            )
        }
        val ITEM = items[position]
        (convertView1!!.findViewById(R.id.lv_memon) as TextView).text = ITEM.Onum.toString()
        (convertView1!!.findViewById(R.id.lv_memo) as TextView).text = ITEM.memo
        (convertView1!!.findViewById(R.id.lv_memo) as TextView).visibility = if (position == mDraggingPosition) View.INVISIBLE else View.VISIBLE

        return convertView1
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
