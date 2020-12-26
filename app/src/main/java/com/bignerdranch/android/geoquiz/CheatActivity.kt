package com.bignerdranch.android.geoquiz

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_cheat.*

private const val TAG="CheatActivity"
class CheatActivity : AppCompatActivity() {

    var answerShown:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        if(savedInstanceState!=null)
        {
            answer_text_view.text=savedInstanceState.getString("answer")
            answerShown=savedInstanceState.getBoolean("shown")
            if(answerShown)
            {
                setResult(Activity.RESULT_OK)
            }

        }
        val intent=getIntent()
        var answer:Boolean=intent.getBooleanExtra("answerToQuestion",false)
        show_answer_button.setOnClickListener {

            answer_text_view.text=answer.toString()
            answerShown=true
        setResult(Activity.RESULT_OK)

    }


    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("answer",answer_text_view.text.toString())
        outState.putBoolean("shown",answerShown)
        Log.i(TAG,"$answerShown")

    }
}
