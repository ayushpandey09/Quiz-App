package com.ayush.quizapp.activity.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayush.quizapp.R
import com.ayush.quizapp.activity.adapter.QuizAdapter
import com.ayush.quizapp.activity.models.Quiz
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var progressBar: ProgressBar
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var appbar: Toolbar
    lateinit var mainDrawer: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var quizRecyclerView: RecyclerView
    lateinit var btnDatePicker: FloatingActionButton

    var backPressedTime:Long = 0
    lateinit var backToast : Toast

    lateinit var adapter: QuizAdapter
    private var quizList = mutableListOf<Quiz>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appbar = findViewById(R.id.appBar)
        mainDrawer = findViewById(R.id.mainDrawer)
        navigationView = findViewById(R.id.naviView)
        quizRecyclerView = findViewById(R.id.quizRecyclerView)
        btnDatePicker = findViewById(R.id.btnDatePicker)
        progressBar = findViewById(R.id.progress_circular)
        //populateMyDummyData()
        setupView()
        setUpfireStore()
        setUpDatePicker()
    }
    override fun onBackPressed() {
        //moveTaskToBack(false) // false -> will not allow to go back
        // true -> exit on clicl back button
        backToast = Toast.makeText(this, "Press back again to leave the app.", Toast.LENGTH_LONG)
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishAffinity()
        } else {
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()

    }
    @SuppressLint("SimpleDateFormat")
    private fun setUpDatePicker() {
        btnDatePicker.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                Log.d("DatePicker2", datePicker.headerText)
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val date = dateFormatter.format(Date(it))
                Log.d("DatePicker1", date)
                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("date", date)
                startActivity(intent)
            }
            datePicker.addOnNegativeButtonClickListener {
                Log.d("DatePicker", datePicker.headerText)
            }
            datePicker.addOnCancelListener {
                Log.d("Date Picker", " Date Picker Cancelled")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpfireStore() {
        val db = Firebase.firestore
        val collectionReference = db.collection("quizzes")
        collectionReference.addSnapshotListener { value, error ->
            if (value == null || error != null) {
                Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
             progressBar.visibility = View.GONE
            // Log.d("DATA", value.toObjects(Quiz::class.java).toString())
            quizList.clear()
            quizList.addAll(value.toObjects(Quiz::class.java))
            adapter.notifyDataSetChanged()
        }

    }

//    private fun populateMyDummyData() {
//        quizList.add(Quiz("11-10-2021", "11-10-2021"))
//        quizList.add(Quiz("11-12-2021", "11-12-2021"))
//        quizList.add(Quiz("11-11-2021", "11-11-2021"))
//        quizList.add(Quiz("11-10-2021", "11-10-2021"))
//        quizList.add(Quiz("11-12-2021", "11-12-2021"))
//        quizList.add(Quiz("11-11-2021", "11-11-2021"))
//        quizList.add(Quiz("11-10-2021", "11-10-2021"))
//        quizList.add(Quiz("11-12-2021", "11-12-2021"))
//        quizList.add(Quiz("11-11-2021", "11-11-2021"))
//        quizList.add(Quiz("11-10-2021", "11-10-2021"))
//        quizList.add(Quiz("11-12-2021", "11-12-2021"))
//        quizList.add(Quiz("11-11-2021", "11-11-2021"))
//    }

    private fun setupView() {
        setUpDrawer()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

        adapter = QuizAdapter(this, quizList)
        quizRecyclerView.layoutManager = GridLayoutManager(this, 1)
        quizRecyclerView.adapter = adapter
    }

    private fun setUpDrawer() {
        setSupportActionBar(appbar) //appBar is id of Toolbar
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, mainDrawer,
            R.string.app_Name,
            R.string.app_Name
        )
        actionBarDrawerToggle.syncState() //sync the toolbar hamburger icon with navigation drawer
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuProfile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    mainDrawer.closeDrawers()
                    true
                }
                R.id.menuContactUs -> {
                    Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT).show()
                    mainDrawer.closeDrawers()
                    true
                }
                R.id.menuDashboard -> {
                    Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show()
                    mainDrawer.closeDrawers()
                    true
                }
                else -> {
                    mainDrawer.closeDrawers()
                    true
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

}