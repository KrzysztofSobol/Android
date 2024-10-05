package com.example.myapplication;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.question.Question;

public class MainActivity extends AppCompatActivity {
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private TextView questionTextView;

    private int currentIndex = 0;
    private Integer currentPoints = 0;
    private Boolean option;

    private Question[] questions = new Question[] {
            new Question(R.string.q_activity, true),
            new Question(R.string.q_find_resources, false),
            new Question(R.string.q_listener, true),
            new Question(R.string.q_resources, true),
            new Question(R.string.q_version, false)
    };

    private void checkAnswerCorrectness(boolean userAnswer){
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;
        if(userAnswer == correctAnswer) {
            currentPoints++;
            resultMessageId = R.string.correct_answer;
        } else {
            resultMessageId = R.string.incorrect_answer;
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
        questionTextView = findViewById(R.id.question_text_view);

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
                    falseButton.setBackgroundColor(Color.rgb(103, 80, 164));
                    trueButton.setBackgroundColor(Color.rgb(103, 80, 164));
                    setNextQuestion(1);
                }
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
}