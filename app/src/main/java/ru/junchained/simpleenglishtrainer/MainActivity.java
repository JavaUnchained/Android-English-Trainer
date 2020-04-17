package ru.junchained.simpleenglishtrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String ITEM_MENU_1 = "Show liked words";
    private static final String ITEM_MENU_2 = "Show my progress";
    private static final String ITEM_MENU_3 = "Shuffle and restart";

    private static final String CURRENT_INDEX_PREF = "current";
    private static final String GOOD_PREF = "good";
    private static final String BAD_PREF = "bad";

    private static final String ENG_ARR_KEY = "eng";
    private static final String RUS_ARR_KEY = "rus";
    private static final String RUS_INX_KEY = "rusidx";
    private static int instanceFlag;

    Button trueButton, falseButton;
    ImageButton nextButton, previousButton, likeButton;
    TextView questionText;
    TextView iterateText;
    TextView goodAndBadWords;
    TextView randomAnswerText;
    QuestionModel questionModel; // 2 массива, слова и их перевод
    int currentIndex;
    int russianIndex;
    String maxIterateIndex;
    int good,bad;
    boolean buttonIsChecked = false;

    SharedPreferences sPref;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPref();
        if(savedInstanceState != null){
            questionModel = new QuestionModel();
            questionModel.setEnglishWords(savedInstanceState.getStringArray(ENG_ARR_KEY));
            questionModel.setRussianWords(savedInstanceState.getStringArray(RUS_ARR_KEY));
            russianIndex = savedInstanceState.getInt(RUS_INX_KEY);
//            currentIndex = savedInstanceState.getInt(CURRENT_INDEX_PREF);
//            bad = savedInstanceState.getInt(BAD_PREF);
//            good = savedInstanceState.getInt(GOOD_PREF);

        }else{
            questionModel = new QuestionModel(
                    getResources().getStringArray(R.array.english_words),
                    getResources().getStringArray(R.array.russian_words));
            russianIndex = questionModel.getRussianIndex(currentIndex);
        }

        trueButton = findViewById(R.id.buttonTrue);
        falseButton = findViewById(R.id.buttonFalse);
        nextButton = findViewById(R.id.next_btn);
        previousButton = findViewById(R.id.previous_btn);
        likeButton = findViewById(R.id.like_btn);

        questionText = findViewById(R.id.question);
        goodAndBadWords = findViewById(R.id.good_and_bad);
        goodAndBadWords.setText(good + " / " + bad);
        randomAnswerText = findViewById(R.id.random_a_text_view);
        maxIterateIndex = "/" +questionModel.getEnglishWords().length;
        iterateText = findViewById(R.id.iterate_words);
        if(currentIndex != 0){
            iterateText.setText((currentIndex+1)  + maxIterateIndex );
        }


//        iterateText.setText(currentIndex  + maxIterateIndex );
        questionText.setText(questionModel.getMergeString(currentIndex, russianIndex));



        trueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buttonIsChecked) {
                    int color;
                    if (currentIndex == russianIndex) {
                        color = getResources().getColor(R.color.correct_answer);
                        good++;
                        goodAndBadWords.setText(good + " / " + bad);
                    } else {
                        questionText.setText(questionModel.getMergeString(currentIndex, currentIndex));
                        randomAnswerText.setTextColor(getResources().getColor(R.color.incorrect_answer));
                        randomAnswerText.setText(questionModel.getMergeString(russianIndex, russianIndex));
                        color = getResources().getColor(R.color.incorrect_answer);
                        bad++;
                        goodAndBadWords.setText(good + " / " + bad);
                    }
                    buttonIsChecked = true;
                    trueButton.getBackground().setTint(color);
                }
            }
        });
        falseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                currentIndex = (currentIndex + 1) % questionModel.getEnglishWords().length;
                if (!buttonIsChecked) {
                    int color;
                    if (currentIndex != russianIndex) {
                        color = getResources().getColor(R.color.correct_answer);
                        randomAnswerText.setTextColor(getResources().getColor(R.color.correct_answer));
                        randomAnswerText.setText(questionModel.getMergeString(russianIndex, russianIndex));
                        good++;
                        goodAndBadWords.setText(good + " / " + bad);
                    } else {
                        color = getResources().getColor(R.color.incorrect_answer);
                        bad++;
                        goodAndBadWords.setText(good + " / " + bad);
                    }

                    buttonIsChecked = true;
                    questionText.setText(questionModel.getMergeString(currentIndex, currentIndex));
                    falseButton.getBackground().setTint(color);
                }
            }
        });

        nextButton.setOnClickListener(new OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
//                trueButton.getBackground().setTint(R.color.d);
//                falseButton.getBackground().setTint(R.color.d);
                falseButton.getBackground().setTint(getResources().getColor(R.color.d));
                trueButton.getBackground().setTint(getResources().getColor(R.color.d));
                randomAnswerText.setText("");
                buttonIsChecked = false;
                currentIndex = (currentIndex + 1) % questionModel.getEnglishWords().length;

                russianIndex = questionModel.getRussianIndex(currentIndex);
                iterateText.setText((currentIndex+1)  + maxIterateIndex );
                questionText.setText(questionModel.getMergeString(currentIndex, russianIndex));
                savePref();
            }
        });

        previousButton.setOnClickListener(new OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
//                trueButton.getBackground().setTint(R.color.d);
//                falseButton.getBackground().setTint(R.color.d);
                falseButton.getBackground().setTint(getResources().getColor(R.color.d));
                trueButton.getBackground().setTint(getResources().getColor(R.color.d));
                randomAnswerText.setText("");
                if(currentIndex == 0){
                    currentIndex = questionModel.getEnglishWords().length-1;
                }else {
                    currentIndex = (currentIndex - 1) % questionModel.getEnglishWords().length;
                }
                iterateText.setText((currentIndex+1)  + maxIterateIndex );
                questionText.setText(questionModel.getMergeString(currentIndex, currentIndex));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(ENG_ARR_KEY, questionModel.getEnglishWords());
        outState.putStringArray(RUS_ARR_KEY, questionModel.getRussianWords());
        outState.putInt(RUS_INX_KEY, russianIndex);
//        outState.putInt(CURRENT_INDEX_PREF,currentIndex);
//        outState.putInt(BAD_PREF, bad);
//        outState.putInt(GOOD_PREF, good);
        savePref();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savePref();
    }

    @Override
    protected void onStop() {
        super.onStop();
        savePref();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(ITEM_MENU_1);
        menu.add(ITEM_MENU_2);
        menu.add(ITEM_MENU_3);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String itemTitle = item.getTitle().toString();
        switch (itemTitle) {
            case ITEM_MENU_1:
                startActivity(new Intent(this, LikedActivity.class));
                break;
            case ITEM_MENU_2:
                startActivity(new Intent(this, ProgressActivity.class));
                break;
            case ITEM_MENU_3:
                restartQuestions();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    private void restartQuestions(){
        questionModel.shakedWords(questionModel.getEnglishWords(), questionModel.getRussianWords());

        falseButton.getBackground().setTint(getResources().getColor(R.color.d));
        trueButton.getBackground().setTint(getResources().getColor(R.color.d));

        buttonIsChecked = false;
        currentIndex = 0;
        russianIndex = questionModel.getRussianIndex(currentIndex);
        iterateText.setText((currentIndex+1)  + maxIterateIndex );
        questionText.setText(questionModel.getMergeString(currentIndex, russianIndex));
        randomAnswerText.setText("");

        good = 0;
        bad = 0;
        goodAndBadWords.setText(good + " / " + bad);
        savePref();

    }

     void savePref() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(CURRENT_INDEX_PREF,currentIndex);
        ed.putInt(BAD_PREF, bad);
        ed.putInt(GOOD_PREF, good);
        ed.apply();
    }

    void loadPref() {
        sPref = getPreferences(MODE_PRIVATE);

        currentIndex = sPref.getInt(CURRENT_INDEX_PREF, 0);
        bad = sPref.getInt(BAD_PREF, 0);
        good = sPref.getInt(GOOD_PREF, 0);
    }

}
