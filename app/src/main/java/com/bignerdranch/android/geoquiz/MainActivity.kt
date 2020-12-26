package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG="MainActivity"
private const val KEY_INDEX="index"
private const val MY_SCORE="score"
private const val REQUEST_CODE_CHEAT=0
class MainActivity : AppCompatActivity() {


    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    private lateinit var questionTextView: TextView
    private lateinit var nextButton: ImageButton
    private  lateinit var trueButton:Button
    private lateinit var falseButton:Button
    private lateinit var prevButton:ImageButton

    private var score:Int=0

    private var answeredIndexes= arrayListOf<Int>()



    var myImage:ImageView?=null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"OnCreate() was called !")
        setContentView(R.layout.activity_main)

        /*
           The ViewModelProviders class (note the plural “Providers”) provides instances of the ViewModelProvider class.
           Your call to ViewModelProviders.of(this) creates and returns a ViewModelProvider associated with the activity.

           ViewModelProvider (no plural), on the other hand, provides instances of ViewModel to the activity.
            Calling provider.get(QuizViewModel::class.java) returns an instance of QuizViewModel.
            You will most often see these functions chained together, like so:
       */
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        /*
              The ViewModelProvider acts like a registry of ViewModels.
              When the activity queries for a QuizViewModel for the first time,
              ViewModelProvider creates and returns a new QuizViewModel instance.
              When the activity queries for the QuizViewModel after a configuration change,
               the instance that was first created is returned.When the activity is finished
               (such as when the user presses the Back button), the ViewModel-Activity pair is removed from memory.

         */

        /*
              The relationship between MainActivity and QuizViewModel is unidirectional.
               The activity references the ViewModel, but the ViewModel does not access the activity.
                Your ViewModel should never hold a reference to an activity or a view,
                 otherwise you will introduce a memory leak.
                 Your ViewModel instance stays in memory across rotation,
                 while your original activity instance gets destroyed.
                 If the ViewModel held a strong reference to the original activity instance, two problems would occur:
                  First, the original activity instance would not be removed from memory, and thus the activity would be leaked.
                  Second, the ViewModel would hold a reference to a stale activity.
                    If the ViewModel tried to update the view of the stale activity,
                    it would trigger an IllegalStateException.
         */

        myImage=findViewById(R.id.imageView) as ImageView

       trueButton=findViewById(R.id.true_button) as Button
        falseButton=findViewById(R.id.false_button) as Button
        nextButton=findViewById(R.id.next_button) as ImageButton
        prevButton=findViewById(R.id.prev_button) as ImageButton
        prevButton.visibility=View.INVISIBLE

        questionTextView=findViewById(R.id.question_text_view) as TextView
          if(savedInstanceState!=null)
          {

              val uscore=savedInstanceState?.getInt(MY_SCORE)?:0
              Log.i(TAG,"CURRENT SCORE IN ONCREATE() "+uscore)

              val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
              quizViewModel.currentIndex = currentIndex


              var newIndexes=savedInstanceState.getIntegerArrayList("answeredIndexes")
              Log.i(TAG,"indexes inside new indexes :"+newIndexes)
              if(newIndexes.isNullOrEmpty()==false)
              {
                  for(index in newIndexes!!.iterator())
                  {
                      answeredIndexes.add(index)

                  }
              }

              score=uscore

              updateQuestion()





          }
        else
          {
              updateQuestion()
          }


        nextButton.setOnClickListener{view:View->

            Log.i(TAG,"answered indexes size"+answeredIndexes.size)

            if(answeredIndexes.size>5)
            {

                Log.i(TAG,"going to next activitiy")


                val pref=getSharedPreferences("SaveScore",Context.MODE_PRIVATE)
                val editor=pref.edit()
                editor.putInt("Score",score)
                if(score>pref.getInt("HighScore",0))
                {
                    editor.putInt("HighScore",score)

                }
                editor.commit()

                val newIntent=Intent(this,HighScoreActivity::class.java)
                startActivity(newIntent)
            }
            else
            {
                quizViewModel.moveToNext()
                quizViewModel.isCheater=false
                updateQuestion()
            }



        }

        trueButton.setOnClickListener{view: View ->

           checkAnswer(true)
            answeredIndexes.add(quizViewModel.currentIndex)
            Log.i(TAG,"Saved Index :"+answeredIndexes)
            trueButton.isEnabled=false
            falseButton.isEnabled=false
            cheat_button.isEnabled=false
        }


        falseButton.setOnClickListener{view:View->
         checkAnswer(false)

            answeredIndexes.add(quizViewModel.currentIndex)

            Log.i(TAG,"Saved Index :"+answeredIndexes)
            trueButton.isEnabled=false
            falseButton.isEnabled=false
            cheat_button.isEnabled=false

        }


        prevButton.setOnClickListener{view:View ->

            quizViewModel.moveBack()
            quizViewModel.isCheater=false


            updateQuestion()
        }
        cheat_button.setOnClickListener { view:View ->
            val intent= Intent(this, CheatActivity::class.java)
            intent.putExtra("answerToQuestion",quizViewModel.currentQuestionAnswer)
            //Extras are arbitrary data that the calling activity can include with an intent
            //The OS forwards the intent to the recipient activity, which can then access the extras and retrieve the data
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
            //An intent is an object that a component can use to communicate with the OS. component such as activity , content provider,broadcast receivers
        }

    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =true
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart() is called !")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume() is called ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause() is called ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop() is called ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG,"onRestart() is called ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy() is called ")
    }

    fun updateQuestion()
    {


        questionTextView.setText(quizViewModel.currentQuestionText)
        if(quizViewModel.currentIndex==0)
        {
            myImage?.setImageResource(R.drawable.can)
        }
        else if(quizViewModel.currentIndex==1)
        {
            myImage?.setImageResource(R.drawable.pac)
        }
        else if(quizViewModel.currentIndex==2)
        {
            myImage?.setImageResource(R.drawable.suez)
        }
        else if(quizViewModel.currentIndex==3)
        {
            myImage?.setImageResource(R.drawable.nile)
        }
        else if(quizViewModel.currentIndex==4)
        {
            myImage?.setImageResource(R.drawable.amazon)
        }
        else if(quizViewModel.currentIndex==5)
        {
            myImage?.setImageResource(R.drawable.baiklal)
        }

        if(answeredIndexes.contains(quizViewModel.currentIndex))
        {
            true_button.isEnabled=false
            false_button.isEnabled=false
            cheat_button.isEnabled=false
        }
        else
        {
            true_button.isEnabled=true
            false_button.isEnabled=true
            cheat_button.isEnabled=true
        }
        textView2.text=score.toString()+"/"+(6)
    }

    fun checkAnswer(userAnswer:Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        var messageResId:Int;
        if(quizViewModel.isCheater)
        {
            messageResId=R.string.judgment_toast
        }
        else if(userAnswer==correctAnswer)
        {
            score++;
            messageResId=R.string.correct_toast
        }
        else
        {
            messageResId=R.string.incorrect_toast
        }

        val myToast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
        myToast.setGravity(Gravity.TOP, 0, 40)
        myToast.show()

        if(answeredIndexes.size == 6)
        {

            print("final toast shown !")
            val finalToast=Toast.makeText(this,"final score "+score,Toast.LENGTH_LONG)
            finalToast.setGravity(Gravity.BOTTOM,0,0)
            finalToast.show()


        }
        textView2.text=score.toString()+"/"+(6)

    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(MY_SCORE,score)
        savedInstanceState.putIntegerArrayList("answeredIndexes",answeredIndexes)
        savedInstanceState.putInt(KEY_INDEX,  quizViewModel.currentIndex)


    }



}
