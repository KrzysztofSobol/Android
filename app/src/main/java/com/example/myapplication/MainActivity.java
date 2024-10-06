package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.question.Question;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    private static final String KEY_CURRENT_OPTION = "currentOption";
    private static final String KEY_CURRENT_POINTS = "currentPoints";
    private static final String KEY_WAS_ANSWER_SHOWN = "wasAnswerShown";
    public static final String KEY_EXTRA_ANSWER = "correctAnswer";
    private static final int REQUEST_CODE_PROMPT = 0;

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button tipButton;
    private TextView questionTextView;

    private int currentIndex = 0; // obecny indeks pytania
    private int currentPoints = 0; // liczba punktów
    private Boolean option; // opcja wybrana przez uzytkownika
    private Boolean answerWasShown = false;

    private Question[] questions = new Question[] {
            new Question(R.string.q_activity, true),
            new Question(R.string.q_find_resources, false),
            new Question(R.string.q_listener, true),
            new Question(R.string.q_resources, true),
            new Question(R.string.q_version, false)
    };

    private void checkAnswerCorrectness(boolean userAnswer){
        boolean correctAnswer = questions[currentIndex].getTrueAnswer();
        int resultMessageId = 0;
        if(answerWasShown){
            resultMessageId = R.string.answer_was_shown;
        } else {
            if(userAnswer == correctAnswer) {
                currentPoints++;
                resultMessageId = R.string.correct_answer;
            } else {
                resultMessageId = R.string.incorrect_answer;
            }
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
    }

    private void setNextQuestion(int i) {
        currentIndex = (currentIndex + i)%questions.length;
        if(currentIndex == 0 && i != 0){
            Toast.makeText(this, currentPoints + "/5 punktów", Toast.LENGTH_SHORT).show();
            currentPoints = 0;
        }
        questionTextView.setText(questions[currentIndex].getQuestionId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // buttons etc
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        tipButton = findViewById(R.id.tip_button);
        questionTextView = findViewById(R.id.question_text_view);

        // wczytaj stan
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
            currentPoints = savedInstanceState.getInt(KEY_CURRENT_POINTS);
            answerWasShown = savedInstanceState.getBoolean(KEY_WAS_ANSWER_SHOWN);
            if (savedInstanceState.containsKey(KEY_CURRENT_OPTION)) {
                option = savedInstanceState.getBoolean(KEY_CURRENT_OPTION);
                if (option) {
                    trueButton.setBackgroundColor(Color.GREEN);
                } else {
                    falseButton.setBackgroundColor(Color.GREEN);
                }
            } else {
                option = null;
            }
        }

        // Button listeners
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                falseButton.setBackgroundColor(Color.rgb(103, 80, 164));
                trueButton.setBackgroundColor(Color.GREEN);
                option = true;
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                trueButton.setBackgroundColor(Color.rgb(103, 80, 164));
                falseButton.setBackgroundColor(Color.GREEN);
                option = false;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(option != null){
                    checkAnswerCorrectness(option);
                    option = null;
                    answerWasShown = false;
                    falseButton.setBackgroundColor(Color.rgb(103, 80, 164));
                    trueButton.setBackgroundColor(Color.rgb(103, 80, 164));
                    setNextQuestion(1);
                }
            }
        });

        tipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, PromptActivity.class);
                boolean correctAnswer = questions[currentIndex].getTrueAnswer();
                intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
                startActivityForResult(intent, REQUEST_CODE_PROMPT);
            }
        });

        setNextQuestion(0);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("customLog","Wywolano onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("customLog","Wywolano onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("customLog","Wywolano onPause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("customLog","Wywolano onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("customLog","Wywolano onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.d("customLog", "Wywolano onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
        if(option != null)
            outState.putBoolean(KEY_CURRENT_OPTION, option);
        outState.putInt(KEY_CURRENT_POINTS, currentPoints);
        outState.putBoolean(KEY_WAS_ANSWER_SHOWN, answerWasShown);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) { return; }
        if(requestCode == REQUEST_CODE_PROMPT){
            if (data == null) { return; }
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }
}