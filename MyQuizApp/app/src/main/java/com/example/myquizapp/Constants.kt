package com.example.myquizapp

object Constants {

    const val USER_NAME : String = "user_name"
    const val TOTAL_QUESTIONS: String = "total_questions"
    const val CORRECT_ANSWERS: String = "correct_answers"

    fun getQuestions():ArrayList<Question> {
        val questionList = ArrayList<Question>()

        val que1 = Question(1,"Q1: What country does this flag belong to?",
        R.drawable.ic_flag_of_argentina,"Argentina","Austrialia",
        "Armenia","Austria",1)
        questionList.add(que1)

        val que2 = Question(2,"Q2: What country does this flag belong to?",
            R.drawable.ic_flag_of_australia,"Argentina","Austrialia",
            "Armenia","australia",4)
        questionList.add(que2)

        val que3 = Question(3,"Q3: What country does this flag belong to?",
            R.drawable.ic_flag_of_belgium,"Belgium","Austrialia",
            "Armenia","Austria",1)
        questionList.add(que3)

        val que4 = Question(4,"Q4: What country does this flag belong to?",
            R.drawable.ic_flag_of_brazil,"Argentina","Austrialia",
            "Armenia","Drazil",4)
        questionList.add(que4)

        val que5 = Question(5,"Q5: What country does this flag belong to?",
            R.drawable.ic_flag_of_denmark,"Argentina","Denmark",
            "Armenia","Austria",2)
        questionList.add(que5)

        val que6 = Question(6,"Q6: What country does this flag belong to?",
            R.drawable.ic_flag_of_fiji,"Argentina","Austrialia",
            "fiji","Austria",3)
        questionList.add(que6)

        val que7 = Question(7,"Q7: What country does this flag belong to?",
            R.drawable.ic_flag_of_germany,"Germany","Austrialia",
            "Armenia","fiji",1)
        questionList.add(que7)

        val que8 = Question(8,"Q8: What country does this flag belong to?",
            R.drawable.ic_flag_of_india,"Argentina","Austrialia",
            "Armenia","India",4)
        questionList.add(que8)

        val que9 = Question(9,"Q9: What country does this flag belong to?",
            R.drawable.ic_flag_of_kuwait,"Kuwait","Austrialia",
            "Armenia","Austria",1)
        questionList.add(que9)

//        val que10 = Question(10,"Q10: What country does this flag belong to?",
//            R.drawable.ic_flag_of_new_zealand,"Kuwait","New_zealand",
//            "Armenia","Germany",2)
//        questionList.add(que10)

        return questionList
    }
}