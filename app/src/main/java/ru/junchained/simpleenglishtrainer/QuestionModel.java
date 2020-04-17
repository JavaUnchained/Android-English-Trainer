package ru.junchained.simpleenglishtrainer;

import java.util.ArrayList;

public class QuestionModel {
    private String[] englishWords;
    private String[] russianWords;

    public QuestionModel(String[] englishWords, String[] russianWords) {
        shakedWords(englishWords, russianWords);
    }

    public QuestionModel(){}

    public String[] getEnglishWords() {
        return englishWords;
    }

    public void setEnglishWords(String[] englishWords) {
        this.englishWords = englishWords;
    }

    public String[] getRussianWords() {
        return russianWords;
    }

    public void setRussianWords(String[] russianWords) {
        this.russianWords = russianWords;
    }

    public int getRussianIndex(int engIndex){
        // со случайной вероятностью дает случайный индекс
        return (int) (Math.random() * 10) > 5 ? engIndex : (int) (Math.random() * russianWords.length);
    }

    public  String getMergeString(int engIndex, int rusIndex){
        return englishWords[engIndex] + " - " + russianWords[rusIndex];
    }

    public void shakedWords(String[] eng, String[] rus){
        String[] nEng = new String[eng.length], nRus = new String[rus.length];

        int rndIndex;
        int x = 0;
        while (x < eng.length){
            rndIndex = (int) (Math.random() * eng.length);
            if(!eng[rndIndex].equals("0")) {
                nEng[x] = eng[rndIndex];
                nRus[x] = rus[rndIndex];
                eng[rndIndex] = "0";
                x++;
            }
        }
        setEnglishWords(nEng);
        setRussianWords(nRus);
    }
}

