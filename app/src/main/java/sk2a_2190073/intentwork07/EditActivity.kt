package sk2a_2190073.intentwork07

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_edit.*
import java.security.AccessController.getContext

class EditActivity : AppCompatActivity() {

    data class MEMOdata(val Mnum: Int, val memo: String)
    private var orderNum: Int = 0;
    private lateinit var MEMO: MutableList<MEMOdata>;
    private val _etmemo: EditText= findViewById(R.id.etmemo)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        //インテントからデータを取得する
        val intent = intent
        if (intent != null) {
            if (intent.hasExtra("orderN")) {
                orderNum = intent.getIntExtra("orderN", 0)
            }
        }

        MEMO = getMEMO()
        _etmemo.setText(MEMO[0].memo)

    }

    private fun getMEMO(): MutableList<MEMOdata> {
//        var list: MutableList<MEMOdata>
//        val sqlite = SQLiteProcess(this@MainActivity)
//        list = sqlite.selectMEMO(orderNum)
//
//        return list
    }
}
