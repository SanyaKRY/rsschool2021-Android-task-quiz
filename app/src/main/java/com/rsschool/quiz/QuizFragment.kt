package com.rsschool.quiz

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.addCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentQuizBinding

class QuizFragment: Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private var listener: QuizFragmentActionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as QuizFragmentActionListener
    }

    private fun setTheme(inflater: LayoutInflater, quiz: Quiz?) {
        var quizNumber: Int = MainActivity.quizList.indexOf(quiz) + 1
        when (quizNumber) {
            1 -> {
                inflater.context.setTheme(R.style.Theme_Quiz_First)
            }
            2 -> {
                inflater.context.setTheme(R.style.Theme_Quiz_Second)
            }
            3 -> {
                inflater.context.setTheme(R.style.Theme_Quiz_Third)
            }
            4 -> {
                inflater.context.setTheme(R.style.Theme_Quiz_Fourth)
            }
            5 -> {
                inflater.context.setTheme(R.style.Theme_Quiz_Fifth)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val quiz: Quiz? = arguments?.getParcelable<Quiz>(QUIZ_KEY)
        setTheme(inflater, quiz)
        activity?.window?.statusBarColor = resources.getColor(MainActivity.statusBarColorsList[MainActivity.quizList.indexOf(quiz)])
        _binding = FragmentQuizBinding.inflate(inflater, container, false)

        setQuizToFragmentsView(quiz)
        setEnablerValueForButtons(quiz)

        // после выбора, кнопка нехт баттон доступна
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            binding.nextButton.isEnabled = true
        }
        // слушатель nextButton и перехода в класс MainActivity
        binding.nextButton.setOnClickListener {
            val checkedRadioButtonId = binding.radioGroup.checkedRadioButtonId
            var checkedRadioButton: RadioButton = binding.root.findViewById(checkedRadioButtonId)
            quiz?.userAnswer = checkedRadioButton.text.toString()
            listener?.goToNextQuestionByNextButton(quiz)
        }
        // слушатель previousButton и перехода в класс MainActivity
        binding.previousButton.setOnClickListener {
            listener?.goToPreviousQuestionByPreviousButton(quiz)
        }
        // слушатель toolbar "назад" и перехода в класс MainActivity
        binding.toolbar.setNavigationOnClickListener {
            listener?.goToPreviousQuestionByPreviousButton(quiz)
        }
        // слушатель "назад" и перехода в класс MainActivity
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            listener?.goToPreviousQuestionByPreviousButton(quiz)
        }
        return binding.root
    }

    // сетаем значения из моддели ui вьюхам
    private fun setQuizToFragmentsView(quiz: Quiz?) {
        val quiz: Quiz? = arguments?.getParcelable<Quiz>(QUIZ_KEY)
        binding.question.text = quiz?.question
        binding.optionOne.text = quiz?.answerlist?.get(0)
        binding.optionTwo.text = quiz?.answerlist?.get(1)
        binding.optionThree.text = quiz?.answerlist?.get(2)
        binding.optionFour.text = quiz?.answerlist?.get(3)
        binding.optionFive.text = quiz?.answerlist?.get(4)
        binding.toolbar.title = resources.getString(R.string.question_number, MainActivity.quizList.indexOf(quiz) + 1)
        if (quiz?.userAnswer == null) {
        } else {
            // находим radiobutton по тексту из выбора пользователя
            when(quiz?.userAnswer) {
                binding.optionOne.text -> binding.optionOne.isChecked = true
                binding.optionTwo.text -> binding.optionTwo.isChecked = true
                binding.optionThree.text -> binding.optionThree.isChecked = true
                binding.optionFour.text -> binding.optionFour.isChecked = true
                binding.optionFive.text -> binding.optionFive.isChecked = true
            }
        }
    }

    private fun setEnablerValueForButtons(quiz: Quiz?) {
        // если userAnswer = null, nextButton.isEnabled = false
        // кнопка нехт баттон недоступна
        if (quiz?.userAnswer == null) {
            binding.nextButton.isEnabled = false
        }
        //кастыль
        // если это первый вопрос, previousButton.isEnabled = false
        if (MainActivity.quizList.indexOf(quiz) == 0) {
            binding.previousButton.isEnabled = false
            binding.toolbar.navigationIcon = null
        }
        //кастыль
        // если это последний вопрос, nextButton.text = Submited
        if (MainActivity.quizList.indexOf(quiz) == MainActivity.quizList.size - 1) {
            binding.nextButton.text = "Submit"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(quiz: Quiz): QuizFragment {
            val fragment = QuizFragment()
            val args = Bundle()
            args.putParcelable(QUIZ_KEY, quiz)
            fragment.arguments = args
            return fragment
        }
        private const val QUIZ_KEY = "QUIZ_KEY"
    }

    interface QuizFragmentActionListener {
        fun goToNextQuestionByNextButton(quiz: Quiz?)
        fun goToPreviousQuestionByPreviousButton(quiz: Quiz?)
    }
}