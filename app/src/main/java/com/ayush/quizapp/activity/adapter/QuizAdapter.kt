package com.ayush.quizapp.activity.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ayush.quizapp.R
import com.ayush.quizapp.activity.activity.QuestionActivity
import com.ayush.quizapp.activity.models.Quiz
import com.ayush.quizapp.activity.util.ColorPicker
import com.ayush.quizapp.activity.util.IconPicker

class QuizAdapter(private val context: Context, private val quizzes: List<Quiz>):RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {
    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textViewTitle : TextView = itemView.findViewById(R.id.quizTitle)
        var iconView : ImageView = itemView.findViewById(R.id.quizIcon)
        var cardContainer:CardView = itemView.findViewById(R.id.cardContianer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view =LayoutInflater.from(context).inflate(R.layout.quiz_item,parent,false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.textViewTitle.text = quizzes[position].title
        holder.cardContainer.setCardBackgroundColor(Color.parseColor(ColorPicker.getColor()))
        holder.iconView.setImageResource(IconPicker.getIcons())
        holder.itemView.setOnClickListener {
            val intent = Intent(context, QuestionActivity::class.java)
            intent.putExtra("date",quizzes[position].title)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }
}