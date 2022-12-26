package com.ipca.mytravelmemory.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.ipca.mytravelmemory.R
import com.ipca.mytravelmemory.models.DiaryDayModel
import com.ipca.mytravelmemory.utils.ParserUtil
import java.util.*

class DiaryDayCreateActivity : AppCompatActivity() {
    private lateinit var diaryDay: DiaryDayModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_day_create)

        // UI
        val editTextTitle = findViewById<EditText>(R.id.editText_diaryDayCreate_title)
        val editTextBody = findViewById<EditText>(R.id.editText_diaryDayCreate_body)
        val editTextDate = findViewById<EditText>(R.id.editText_diaryDayCreate_date)
        //val textViewEndDate = findViewById<TextView>(R.id.editText_diaryDayCreate_date)
        //val buttonEndDate = findViewById<Button>(R.id.button_diaryDayCreate_date)
        val buttonSave = findViewById<Button>(R.id.button_diaryDayCreate_save)

        // dados do selecionador da data
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        """
        // ao clicar no botão de selecionar data de início
        buttonStartDate.setOnClickListener {
            DatePickerDialog(
                this@,
                DatePickerDialog.OnDateSetListener { view, y, m, d ->
                    textViewStartDate.text =  
                },
                year,
                month,
                day
            ).show()
        }
        """

        // ao clicar no botão de criar viagem
        buttonSave.setOnClickListener {
            // definir viagem
            diaryDay = DiaryDayModel(
                editTextTitle.text.toString(),
                editTextBody.text.toString(),
                ParserUtil.convertStringToDate("30-12-2022")
            )

            // enviar dados do diário criado para a página dos diários
            val intent = Intent()
            intent.putExtra(EXTRA_DIARY_DAY_SAVED, diaryDay)

            setResult(RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        const val EXTRA_DIARY_DAY_SAVED = "EXTRA_DIARY_DAY_SAVED"
    }
}