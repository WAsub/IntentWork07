package sk2a_2190073.intentwork07

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getDrawable

class CustomAdapter2(
    private val activity: Activity,
    private val items: Array<MainActivity.MEMOListdata>,
    private val mDraggingPosition: Int,
    private val con: Context
) : BaseAdapter() {

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
        (convertView1!!.findViewById(R.id.lv_memo) as TextView).background = getDrawable(con, C_id[ITEM.backColor])
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
