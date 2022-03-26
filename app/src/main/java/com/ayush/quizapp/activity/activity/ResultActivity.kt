package com.ayush.quizapp.activity.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ayush.quizapp.R
import com.ayush.quizapp.activity.models.Quiz
import com.ayush.quizapp.activity.models.ScoreDocument
import com.ayush.quizapp.activity.models.UserScoreDetail
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson


class ResultActivity : AppCompatActivity() {
    lateinit var quiz: Quiz
    lateinit var tvscore: TextView
    lateinit var txtAnswer: TextView
    lateinit var db: FirebaseFirestore
    private var players: MutableList<ScoreDocument>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        txtAnswer = findViewById(R.id.txtAnswer)
        tvscore = findViewById(R.id.tvscore)
        db = FirebaseFirestore.getInstance()
        setUpViews()

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }

    private fun setUpViews() {
        val quizData = intent.getStringExtra("quiz")
        quiz = Gson().fromJson(
            quizData,
            Quiz::class.java
        ) //converting json ( in string) to quiz object
        calculateResult()
        showResult()
    }

    private fun showResult() {
        val builder = StringBuilder("")
        for (it in quiz.question.entries) {
            val question = it.value
            builder.append("<font color'#18206F'><b>Question: ${question.description}</b></font><br/><br/>")
            builder.append("<font color='#009688'><b>Correct Answer: ${question.answer}</b></font><br/><br/>")
            builder.append("<font color='#009688'><b>Your Answer: ${question.userAnswer}</b></font><br/><br/>")
            builder.append("<font color='#009688'><b>------------------------------</b></font><br/><br/>")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtAnswer.text = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT)
        } else {
            txtAnswer.text = Html.fromHtml(builder.toString())
        }
    }

    private fun calculateResult() {
        var score = 0
        for (it in quiz.question.entries) {  //entries == Number of quiz question
            //val question = it.value
            if (it.value.userAnswer == it.value.answer) {   //it.value = question 1=Question(description=Capital of India,
                score += 10                                // option1=MP, option2=delhi, option3=UP, option4=Punjab, answer=delhi, userAnswer=MP)
            }
        }
        tvscore.text = "SCORE: ${score}"


        setResultInFirestore(score)
        leaderBoardResult()
    }

    private fun leaderBoardResult() {
        val firestore = FirebaseFirestore.getInstance()
        val scoreRef = firestore.collection("score")
        scoreRef.addSnapshotListener { value, error ->

        }


    }

    private fun setResultInFirestore(score: Int) {
        val dbScore: CollectionReference = db.collection("score")

        val scoreObj =
            UserScoreDetail(Firebase.auth.currentUser?.email.toString(), score, quiz.title)
        val map = HashMap<String, UserScoreDetail>()
        map[quiz.title] = scoreObj
        dbScore.document("${Firebase.auth.currentUser?.email.toString()}")
            .set(map, SetOptions.merge())
            .addOnSuccessListener { // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(
                    this,
                    "Your Score has been added to Firebase Firestore",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener(OnFailureListener { e -> // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(this, "Fail to add score \n$e", Toast.LENGTH_SHORT)
                    .show()
            })
    }
}

