package com.ayush.quizapp.activity.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ayush.quizapp.R
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    lateinit var btnGetStart: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val auth = FirebaseAuth.getInstance()

        btnGetStart = findViewById(R.id.btnGetStart)
        btnGetStart.setOnClickListener {
            //if current user is already logged in then page is direct to main Activity
            if (auth.currentUser != null) {
                redirect("main")
                Toast.makeText(this,"Loged In", Toast
                    .LENGTH_SHORT).show()
            } else
                redirect("login")
        }

    }

    private fun redirect(name: String) {
        val intent = when (name) {
            "login" -> Intent(this, LoginActivity::class.java)
            "main" -> Intent(this, MainActivity::class.java)
            else -> throw Exception("no path exist")
        }
        startActivity(intent)
        finish()
    }
}