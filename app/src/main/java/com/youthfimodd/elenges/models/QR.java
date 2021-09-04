package com.youthfimodd.elenges.models;

public class QR {

    private String Question;
    private String Reponse;
    private boolean expandable;

    public QR() {
    }

    public QR(String question, String reponse) {
        Question = question;
        Reponse = reponse;
        this.expandable = false;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getReponse() {
        return Reponse;
    }

    public void setReponse(String reponse) {
        Reponse = reponse;
    }
    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }
}
