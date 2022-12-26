package com.ipca.mytravelmemory.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.models.DiaryDayModel

class DiaryDayAllActivity : AppCompatActivity() {
    private var diaryDays = arrayListOf<DiaryDayModel>()
    private val adapter = DiaryDaysAdapter()

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_day_all)

        // lista dos diários
        val listViewDiaryDays = findViewById<ListView>(R.id.listView_diaryDayAll_diaryDays)
        listViewDiaryDays.adapter = adapter

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // quando um diário é criado com sucesso no ecrã de criar diário
            if (it.resultCode == Activity.RESULT_OK) {
                val diaryDay =
                    it.data?.getSerializableExtra(DiaryDayCreateActivity.EXTRA_DIARY_DAY_SAVED) as DiaryDayModel
                diaryDays.add(diaryDay)

                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_diary_day_all, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            // ao clicar no botão de criar dia do diário
            R.id.button_menuDiaryDayAll_add -> {
                resultLauncher?.launch(
                    Intent(
                        this@DiaryDayAllActivity,
                        DiaryDayCreateActivity::class.java
                    )
                )
                return true
            }
            else -> {
                return false
            }
        }
    }

    inner class DiaryDaysAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return diaryDays.size
        }

        override fun getItem(position: Int): Any {
            return diaryDays[position]
        }

        override fun getItemId(position: Int): Long {
            return 0L
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_diary, parent, false)

            val textViewName = rootView.findViewById<TextView>(R.id.textView_diaryAll_title)
            textViewName.text = diaryDays[position].title

            return rootView
        }
    }
}