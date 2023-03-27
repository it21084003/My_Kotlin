package com.example.myquizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import org.w3c.dom.Text

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    //Create global variables for the views in the layout
    private var mCurrrentPosition: Int = 1
    private var mQuestionList:ArrayList<Question>? = null
    private var mSelectedOptionPosition : Int = 0
    private var mUserName : String? = null
    private var mCorrectAnswer: Int = 0

    private var progressBar: ProgressBar? = null
    private var tvProgress : TextView? = null
    private var tvQuestion: TextView? = null
    private var ivImage : ImageView? = null

    private var tvOptionOne: TextView? = null
    private var tvOptionTwo: TextView? = null
    private var tvOptionThree: TextView? = null
    private var tvOptionFour: TextView? = null
    private var btnSubmit : Button? = null
    /**
     * This function is auto created by Android when the Activity Class is created.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tv_progress)
        tvQuestion = findViewById(R.id.tv_question)
        progressBar = findViewById(R.id.progressBar)
        ivImage = findViewById(R.id.iv_image)
        tvOptionOne = findViewById(R.id.tv_option_one)
        tvOptionTwo = findViewById(R.id.tv_option_two)
        tvOptionThree = findViewById(R.id.tv_option_three)
        tvOptionFour = findViewById(R.id.tv_option_four)
        btnSubmit = findViewById(R.id.btn_submit)


        mQuestionList = Constants.getQuestions()
        //END

        setQuestion()

        tvOptionOne?.setOnClickListener(this)
        tvOptionTwo?.setOnClickListener(this)
        tvOptionThree?.setOnClickListener(this)
        tvOptionFour?.setOnClickListener(this)
//        tvQuestion?.setOnClickListener(this)
        // TODO(STEP 1: Adding a click event for submit button.)
        btnSubmit?.setOnClickListener(this)


    }

    private fun setQuestion() {

        val question: Question = mQuestionList!![mCurrrentPosition - 1]
        defaultOptionsView()

        // TODO (STEP 6: Check here if the position of question is last then change the text of the button.)
        // START
        if(mCurrrentPosition == mQuestionList!!.size){
            btnSubmit?.text = "FINISH"
        }else{
            btnSubmit?.text = "SUBMIT"
        }
        //END

        // Now set the current question and the options in the UI
        ivImage?.setImageResource(question.image)
        progressBar?.progress = mCurrrentPosition
        tvProgress?.text = "$mCurrrentPosition/${progressBar?.max}"
        tvQuestion?.text = question.question
        tvOptionOne?.text = question.optionOne
        tvOptionTwo?.text = question.optionTwo
        tvOptionThree?.text = question.optionThree
        tvOptionFour?.text = question.optionFour



    }
    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        tvOptionOne?.let{
            options.add(0,it)
        }
        tvOptionTwo?.let{
            options.add(1,it)
        }
        tvOptionThree?.let{
            options.add(2,it)
        }
        tvOptionFour?.let{
            options.add(3,it)
        }
//        tvQuestion?.let{
//            options.add(4,it)
//        }

        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }

    }
    private fun selectedOptionView(tv:TextView,selectedOptionNum:Int){
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface,Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }


    override fun onClick(view: View?) {
        when(view?.id){
            R.id.tv_option_one -> {
                tvOptionOne?.let {
                    selectedOptionView(it,1)
                }
            }
            R.id.tv_option_two -> {
                tvOptionTwo?.let {
                    selectedOptionView(it,2)
                }
            }
            R.id.tv_option_three -> {
                tvOptionThree?.let {
                    selectedOptionView(it,3)
                }
            }
            R.id.tv_option_four -> {
                tvOptionFour?.let {
                    selectedOptionView(it,4)
                }
            }
//            R.id.tv_question-> {
//                tvQuestion?.let {
//                    selectedOptionView(it,5)
//                }
//            }
            // TODO(STEP 2: Adding a click event for submit button. And change the questions and check the selected answers.)
            // START
            R.id.btn_submit -> {
                if(mSelectedOptionPosition == 0){
                    mCurrrentPosition++

                    when{
                        mCurrrentPosition <= mQuestionList!!.size ->{
                            setQuestion()
                        }
                        else ->{
                          val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS,mCorrectAnswer)
                            intent.putExtra(Constants.TOTAL_QUESTIONS,mQuestionList?.size)
                            startActivity(intent)
                            finish()
                             }
                    }
                }else{
                    val question = mQuestionList?.get(mCurrrentPosition - 1)
                    // This is to check if the answer is wrong
                    if(question!!.correctAnswer != mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }else{
                        mCorrectAnswer++
                    }
                    // This is for correct answer
                    answerView(question.correctAnswer,R.drawable.correct_option_border_bg)

                    if(mCurrrentPosition == mQuestionList!!.size){
                        btnSubmit?.text = "FINISH"
                    }else{
                        btnSubmit?.text = "GO TO NEXT QUESTION"
                    }
                    mSelectedOptionPosition = 0

                }
            }
        }
    }

    // TODO (STEP 3: Create a function for answer view.)
    // START
    /**
     * A function for answer view which is used to highlight the answer is wrong or right.
     */
    private fun answerView(answer: Int,drawbleView: Int){
        when(answer){
            1->{
                tvOptionOne?.background = ContextCompat.getDrawable(
                    this,drawbleView
                )
            }
            2->{
                tvOptionTwo?.background = ContextCompat.getDrawable(
                    this,drawbleView
                )
            }
            3->{
                tvOptionThree?.background = ContextCompat.getDrawable(
                    this,drawbleView
                )
            }
            4->{
                tvOptionFour?.background = ContextCompat.getDrawable(
                    this,drawbleView
                )
            }
//            5->{
//                tvQuestion?.background = ContextCompat.getDrawable(
//                    this,drawbleView
//                )
//            }
        }
    }
}