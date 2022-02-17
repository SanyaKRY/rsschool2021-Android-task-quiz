package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentSubmitBinding

class SubmitFragment: Fragment() {

    private var _binding: FragmentSubmitBinding? = null
    private val binding get() = _binding!!

    private var listener: SubmitFragmentActionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as SubmitFragmentActionListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSubmitBinding.inflate(inflater, container, false)

        // сетаем результат
        val result: Int? = arguments?.getInt(RESULT_KEY)
        binding.resultTextView.text = resources.getString(R.string.your_result, result)

        binding.closeImageButton.setOnClickListener {
            listener?.finishQuiz()
        }
        binding.arrowBackImageButton.setOnClickListener {
            listener?.restartQuiz()
        }
        binding.shareImageButton.setOnClickListener {
            listener?.shareResult()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            listener?.restartQuiz()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(result: Int): SubmitFragment {
            val fragment = SubmitFragment()
            val args = Bundle()
            args.putInt(RESULT_KEY, result)
            fragment.arguments = args
            return fragment
        }
        private const val RESULT_KEY = "RESULT_KEY"
    }

    interface SubmitFragmentActionListener {
        fun finishQuiz()
        fun restartQuiz()
        fun shareResult()
    }
}