package com.hubli.imperium.imaperiumdiary.Main.MainFrags.Questions;

/**
 * Created by Faheem on 18-05-2017.
 */

public class QAData {

    private String type, questionText, opt1, opt2, opt3, opt4, answer;
    private int level;

    public QAData(String type, String questionText, String opt1, String opt2, String opt3, String opt4, String answer, int level) {
        this.type = type;
        this.questionText = questionText;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.opt4 = opt4;
        this.answer = answer;
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOpt1() {
        return opt1;
    }

    public String getOpt2() {
        return opt2;
    }

    public String getOpt3() {
        return opt3;
    }

    public String getOpt4() {
        return opt4;
    }

    public String getAnswer() {
        return answer;
    }

    public int getLevel() {
        return level;
    }
}
