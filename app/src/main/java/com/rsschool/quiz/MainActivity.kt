package com.rsschool.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(), QuizFragment.QuizFragmentActionListener, SubmitFragment.SubmitFragmentActionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // создание фрагмента и заполение его первым квизом.<quiz1>
        openFragment(quizList.get(0))
    }

    // через него открываем фрагмент
    private fun openFragment(quiz: Quiz) {
        val quizFragment: QuizFragment = QuizFragment.newInstance(quiz)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.quiz_fragment, quizFragment)
        transaction.commit()
    }

    // через него открываем Submit фрагмент
    private fun openSubmitFragment() {
        val percentageOfCorrectAnswers: Long = (quizList.stream().filter { quiz -> quiz.correctAnswer.equals(quiz.userAnswer) }.count() * 100) / quizList.size
        val submitFragment: SubmitFragment = SubmitFragment.newInstance(percentageOfCorrectAnswers.toInt())
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.quiz_fragment, submitFragment)
        transaction.commit()
    }

    // переходим к следующуму вопросу
    override fun goToNextQuestionByNextButton(quiz: Quiz?) {
        // если нажали кнопку "next" для последнего квиза, переходи мна фрагмент SubmitFragment
        if (quizList.indexOf(quiz) + 1 == quizList.size) {
            openSubmitFragment()
        } else {
            openFragment(quizList.get(quizList.indexOf(quiz) + 1))
        }
    }

    // переходим к предыдуему вопросу
    override fun goToPreviousQuestionByPreviousButton(quiz: Quiz?) {
        if (quizList.indexOf(quiz) != 0) {
            openFragment(quizList.get(quizList.indexOf(quiz) - 1))
        } else {
            // если нажали кнопку "назад" для первого квиза, показать сообщение/toast
            Toast.makeText(this, "// TODO Необходимо реализовать мягкий выход:? , уточнить",Toast.LENGTH_LONG).show()
            finishAndRemoveTask()
        }
    }

    companion object {
        // обьявление вопросов в квиз
        val quiz1: Quiz = Quiz("Кто лучше_1:?", arrayListOf("1","2","3","4","5"), null, "1")
        val quiz2: Quiz = Quiz("Кто лучше_2:?", arrayListOf("1","2","3","4","5"), null, "1")
        val quiz3: Quiz = Quiz("Кто лучше_3:?", arrayListOf("1","2","3","4","5"), null, "1")
        val quiz4: Quiz = Quiz("Кто лучше_4:?", arrayListOf("1","2","3","4","5"), null, "1")
        val quiz5: Quiz = Quiz("Кто лучше_5:?", arrayListOf("1","2","3","4","5"), null, "1")
        val quizList: List<Quiz> = listOf(quiz1, quiz2, quiz3, quiz4, quiz5)
        val statusBarColorsList: List<Int> = listOf(R.color.pink_dark, R.color.green_dark, R.color.purple_dark,
            R.color.deep_orange_dark, R.color.brown_dark)
    }

    override fun finishQuiz() {
        finishAndRemoveTask()
    }

    override fun restartQuiz() {
        for (quiz in quizList) {
            quiz.userAnswer = null
        }
        openFragment(quizList.get(0))
    }

    override fun shareResult() {
        val percentageOfCorrectAnswers: Long = (quizList.stream().filter { quiz -> quiz.correctAnswer.equals(quiz.userAnswer) }.count() * 100) / quizList.size
        var text: String = resources.getString(R.string.your_result, percentageOfCorrectAnswers)
        for (quiz in quizList) {
            text += " \n\n${quizList.indexOf(quiz) + 1}) " + quiz.question + "\n${resources.getString(R.string.your_answer)} " + quiz.userAnswer
        }
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Quiz results")
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(intent)
    }
}