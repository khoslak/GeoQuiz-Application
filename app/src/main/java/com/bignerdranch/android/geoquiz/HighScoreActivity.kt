package com.bignerdranch.android.geoquiz

import android.app.PendingIntent.getActivity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_high_score.*
import androidx.core.app.ActivityCompat.finishAffinity
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.security.AccessController.getContext


private const val TAG="HighScoreActivity"
class HighScoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)
        val pref=getSharedPreferences("SaveScore",Context.MODE_PRIVATE)
        var hscore=pref.getInt("HighScore",0)
        var score=pref.getInt("Score",0)

        Log.i(TAG,"the value of highscore $score")
        txtHighScore.setText(hscore.toString())
        textView5.setText(score.toString())

        btnExit.setOnClickListener { view: View ->
            finish()
        }

        btnTryagain.setOnClickListener{view :View ->


        }
    }
}
