package com.ayush.quizapp.activity.models

data class Quiz(
    var id : String = "",
    var title: String = "",
    var question: MutableMap<String, Question> = mutableMapOf()
)
