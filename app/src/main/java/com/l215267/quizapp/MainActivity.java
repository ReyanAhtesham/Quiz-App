package com.l215267.quizapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvTimer, tvScore, tvQuestion;
    private RadioGroup rgOptions;
    private RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    private Button btnNext, btnPrev, btnShowAnswer, btnEndExam;

    private String[] questions;
    private String[][] options;
    private String[] correctAnswers;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean answered = false;
    private CountDownTimer timer;
    private long timeLeftInMillis = 600000; // 10 minutes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTimer = findViewById(R.id.tvTimer);
        tvScore = findViewById(R.id.tvScore);
        tvQuestion = findViewById(R.id.tvQuestion);
        rgOptions = findViewById(R.id.rgOptions);
        rbOption1 = findViewById(R.id.rbOption1);
        rbOption2 = findViewById(R.id.rbOption2);
        rbOption3 = findViewById(R.id.rbOption3);
        rbOption4 = findViewById(R.id.rbOption4);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnShowAnswer = findViewById(R.id.btnShowAnswer);
        btnEndExam = findViewById(R.id.btnEndExam);

        // Initialize questions, options, and correct answers
        questions = getResources().getStringArray(R.array.questions);
        correctAnswers = getResources().getStringArray(R.array.correct_answers);
        options = new String[][]{
                getResources().getStringArray(R.array.options1),
                getResources().getStringArray(R.array.options2),
                getResources().getStringArray(R.array.options3),
                getResources().getStringArray(R.array.options4),
                getResources().getStringArray(R.array.options5),
                getResources().getStringArray(R.array.options6),
                getResources().getStringArray(R.array.options7),
                getResources().getStringArray(R.array.options8),
                getResources().getStringArray(R.array.options9),
                getResources().getStringArray(R.array.options10),
                getResources().getStringArray(R.array.options11),
                getResources().getStringArray(R.array.options12),
                getResources().getStringArray(R.array.options13),
                getResources().getStringArray(R.array.options14),
                getResources().getStringArray(R.array.options15),
                getResources().getStringArray(R.array.options16),
                getResources().getStringArray(R.array.options17),
                getResources().getStringArray(R.array.options18),
                getResources().getStringArray(R.array.options19),
                getResources().getStringArray(R.array.options20),
                getResources().getStringArray(R.array.options21),
                getResources().getStringArray(R.array.options22),
                getResources().getStringArray(R.array.options23),
                getResources().getStringArray(R.array.options24),
                getResources().getStringArray(R.array.options25),
        };

        // Display the first question
        displayQuestion();

        // Timer logic
        startTimer();

        // Next button logic
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rgOptions.getCheckedRadioButtonId() != -1 && !answered) {
                    checkAnswer();
                    if (currentQuestionIndex < questions.length - 1) {
                        currentQuestionIndex++;
                        displayQuestion();
                        rgOptions.clearCheck();
                        answered = false; // Reset for next question
                    } else {
                        endExam();
                    }
                } else if (answered) {
                    Toast.makeText(MainActivity.this, "Already answered, click Next to continue.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Please select an option.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Previous button logic
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--;
                    displayQuestion();
                    rgOptions.clearCheck();
                    answered = false; // Reset for previous question
                }
            }
        });


        // Show Correct Answer button logic
        btnShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    // Get the correct answer for the current question
                    String correctAnswer = correctAnswers[currentQuestionIndex];

                    // Display the correct answer
                    Toast.makeText(MainActivity.this, "Correct Answer: " + correctAnswer, Toast.LENGTH_SHORT).show();

                    // Deduct 1 point from the score
                    score -= 1;

                    // Update the score display
                    tvScore.setText("Score: " + score);

                } else {
                    Toast.makeText(MainActivity.this, "You have already answered this question.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // End Exam button logic
        btnEndExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endExam();
            }
        });
    }

    private void displayQuestion() {
        tvQuestion.setText(questions[currentQuestionIndex]);
        rbOption1.setText(options[currentQuestionIndex][0]);
        rbOption2.setText(options[currentQuestionIndex][1]);
        rbOption3.setText(options[currentQuestionIndex][2]);
        rbOption4.setText(options[currentQuestionIndex][3]);
    }

    private void checkAnswer() {
        int selectedId = rgOptions.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedAnswer = selectedRadioButton.getText().toString();

        if (answered) {
            Toast.makeText(MainActivity.this, "You have already answered this question.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedAnswer.equals(correctAnswers[currentQuestionIndex])) {
            score += 5;
        } else {
            score -= 1;
        }

        tvScore.setText("Score: " + score);
        answered = true;
    }


    private void startTimer() {
        timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                endExam();
            }
        }.start();
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        tvTimer.setText("Time Remaining: " + timeFormatted);
    }

    private void endExam() {
        timer.cancel();
        int percentage = (score * 100) / (questions.length * 5);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Exam Finished")
                .setMessage("Your Score: " + score + "\nPercentage: " + percentage + "%")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }
}
