package com.rsschool.quiz

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.ArrayList

@Parcelize
data class Quiz constructor(val question: String,
                            val answerlist: ArrayList<String>,
                            var userAnswer: String?,
                            val correctAnswer: String): Parcelable
