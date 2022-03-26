package com.ayush.quizapp.activity.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayush.quizapp.R
import com.ayush.quizapp.activity.adapter.OptionAdapter
import com.ayush.quizapp.activity.models.Question
import com.ayush.quizapp.activity.models.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class QuestionActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var quesDes: TextView
    private lateinit var optionList: RecyclerView
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button
    private var index = 1
    private var quizzes: MutableList<Quiz>? = null
    private var questions: MutableMap<String, Question>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        quesDes = findViewById(R.id.description)
        optionList = findViewById(R.id.optionList)
        btnNext = findViewById(R.id.btnNext)
        btnPrev = findViewById(R.id.btnPrevious)
        progressBar = findViewById(R.id.questionProgress)

        progressBar.visibility = View.VISIBLE

        setUpfirestore()
        setUpEventListner()
    }

    private fun setUpEventListner() {
        btnPrev.setOnClickListener {
            index--
            bindViews()
        }

        btnNext.setOnClickListener {

            if (index >= questions!!.size){
                btnNext.visibility = View.GONE
                btnPrev.visibility = View.GONE
                Toast.makeText(this,"Submitted",Toast.LENGTH_SHORT).show()
                Log.d("submitted", questions.toString())
                val intent = Intent(this,ResultActivity::class.java)
                val json = Gson().toJson(quizzes!![0])
                intent.putExtra("quiz", json)

                startActivity(intent)
            }
            index++
            bindViews()
        }

    }

    private fun setUpfirestore() {
        val firestore = FirebaseFirestore.getInstance()
        val date = intent.getStringExtra("date")
        if (date != null) {
            firestore.collection("quizzes").whereEqualTo("title", date)
                .get().addOnSuccessListener {
                    if (it != null && !it.isEmpty)
                    {
                        quizzes = it.toObjects(Quiz::class.java)
                         progressBar.visibility = View.GONE
                    }
                    questions = quizzes!![0].question
                    bindViews()
                }
        }
        else
            Toast.makeText(this,"date not fetch", Toast.LENGTH_SHORT).show()
    }

    private fun bindViews() {
        btnPrev.visibility = View.GONE
        btnNext.visibility = View.GONE

         if (index == questions!!.size) {
            btnNext.visibility = View.VISIBLE
            btnNext.text = "SUBMIT"
        }
         else if(index == 1)
             btnNext.visibility = View.VISIBLE
         else if (index > 1) {
            btnNext.visibility = View.VISIBLE
            btnPrev.visibility = View.VISIBLE
        }

        val question = questions!!["question $index"]
        question?.let {
            quesDes.text = it.description
            optionList.layoutManager = LinearLayoutManager(this)
            optionList.adapter = OptionAdapter(this, it)
            optionList.setHasFixedSize(true)
        }
    }
}