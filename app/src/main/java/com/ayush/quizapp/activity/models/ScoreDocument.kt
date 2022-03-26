package com.ayush.quizapp.activity.models

data class ScoreDocument(var id: String = "",  var userScoreMap: MutableMap<String, UserScoreDetail> = mutableMapOf()  )
