package com.example.quizapp_java;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private QuesFragment quesFragment;
    private Button trueButton;
    private Button falseButton;
    private ProgressBar progressBar;
    private ListPopupWindow listPopupWindow;
    private List<QuestionModel> questionList;
    private int selectedIndex;
    private List<ResultModel> result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trueButton = findViewById(R.id.buttonTrue);
        falseButton = findViewById(R.id.buttonFalse);
        progressBar = findViewById(R.id.progressBar);

        questionList = initializeQuestionList();

        selectedIndex = 0;
        result = new ArrayList<>();

        progressBar.setMax(questionList.size());
        progressBar.setProgress(selectedIndex+1);

        createFragmentWithNewQues();

        falseButton.setOnClickListener(view -> ansButtonClicked(getString(R.string.f)));
        trueButton.setOnClickListener(view -> ansButtonClicked(getString(R.string.t)));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_item_1:
                getAverage();
                return true;

            case R.id.menu_item_2:
                showNumberSelectionDialog();
                return true;

            case R.id.menu_item_3:
                resetFile();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // click events
    private void getAverage(){
        List<List<ResultModel>> resultList = FileStorage.retrieveResultList(this);
        int attempts = resultList.size();
        long correctAnsCount = resultList.stream()
                .flatMap(List::stream)// Flatten the list of lists to a single stream of ResultModel
                .filter(resultObj -> resultObj.getAnsweredCorrect())
                .count();
        showAlertDialogForAverage(correctAnsCount+"",attempts+"");
    }
    private void showNumberSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.number_selection, null);

        final NumberPicker numberPicker = view.findViewById(R.id.numberPicker);

        // Set the minimum and maximum values for the NumberPicker
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);

        builder.setView(view);
        builder.setTitle("Select Number of Questions");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedNumber = numberPicker.getValue();
                // Do something with the selectedNumber
                questionList = getRandomQuestions(selectedNumber);
                progressBar.setMax(questionList.size());
                progressBar.setProgress(selectedIndex+1);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
    private void resetFile(){
        FileStorage.resetResultList(this);
    }
    private void ansButtonClicked(String ans){
        //check if ans is correct or incorrect
        if (questionList.get(selectedIndex).getAnswer() == ans){
            storeResultModel(true);
            showToast(getString(R.string.correct));
        }else {
            storeResultModel(false);
            showToast(getString(R.string.incorrect));
        }
        //check if displayed ques is last or not
        if (selectedIndex == questionList.size()-1){
            long correctAns = result.stream().filter(resultObj -> resultObj.getAnsweredCorrect() == true).count();
            showAlertDialogForResult(correctAns+"",questionList.size()+"");
        }else {
            selectedIndex = selectedIndex + 1;
            createFragmentWithNewQues();
            progressBar.setProgress(selectedIndex+1);
        }

    }

    //initialization function
    private List<QuestionModel> initializeQuestionList() {
        List<QuestionModel> allQuestionList = new ArrayList<>();

        QuestionModel Q1 = new QuestionModel( getString(R.string.and_q1),getString(R.string.f),ColorUtils.generateRandomColor());
        QuestionModel Q2 = new QuestionModel( getString(R.string.and_q2),getString(R.string.f),ColorUtils.generateRandomColor());
        QuestionModel Q3 = new QuestionModel( getString(R.string.and_q3),getString(R.string.f),ColorUtils.generateRandomColor());
        QuestionModel Q4 = new QuestionModel( getString(R.string.and_q4),getString(R.string.f),ColorUtils.generateRandomColor());

        QuestionModel Q5 = new QuestionModel( getString(R.string.and_q5),getString(R.string.t),ColorUtils.generateRandomColor());
        QuestionModel Q6 = new QuestionModel( getString(R.string.and_q6),getString(R.string.t),ColorUtils.generateRandomColor());
        QuestionModel Q7 = new QuestionModel( getString(R.string.and_q7),getString(R.string.t),ColorUtils.generateRandomColor());
        QuestionModel Q8 = new QuestionModel( getString(R.string.and_q8),getString(R.string.t),ColorUtils.generateRandomColor());
        QuestionModel Q9 = new QuestionModel( getString(R.string.and_q9),getString(R.string.t),ColorUtils.generateRandomColor());
        QuestionModel Q10 = new QuestionModel( getString(R.string.and_q10),getString(R.string.t),ColorUtils.generateRandomColor());

        allQuestionList = new ArrayList<>();
        allQuestionList.add(Q1);
        allQuestionList.add(Q2);
        allQuestionList.add(Q3);
        allQuestionList.add(Q4);
        allQuestionList.add(Q5);
        allQuestionList.add(Q6);
        allQuestionList.add(Q7);
        allQuestionList.add(Q8);
        allQuestionList.add(Q9);
        allQuestionList.add(Q10);

        Collections.shuffle(allQuestionList);

        return allQuestionList;
    }
    private List<QuestionModel> getRandomQuestions(int selectedQuestions) {
        List<QuestionModel> mainList = initializeQuestionList();
        Collections.shuffle(mainList);
        List<QuestionModel> selectedQuestionList = new ArrayList<>();
        // to make sure selectedQuestions is within the bounds of the total number of questions
        selectedQuestions = Math.min(selectedQuestions, mainList.size());
        for (int i = 0; i < selectedQuestions; i++) {
            selectedQuestionList.add(mainList.get(i));
        }
        return selectedQuestionList;
    }
    private void createFragmentWithNewQues(){
        QuestionModel ques = questionList.get(selectedIndex);
        if (ques != null) {
            if (quesFragment == null){
                quesFragment = new QuesFragment(ques);
                // Add the fragment to the activity
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, quesFragment)
                        .commit();
            }
            else {
                quesFragment.setQuestion(ques);
            }
        }
    }

    //file manager function
    private void storeResultModel(Boolean isAnsweredCorrect){
        QuestionModel Q = questionList.get(selectedIndex);
        result.add(new ResultModel(Q.getQuestionText(),Q.getAnswer(),Q.getColor(),isAnsweredCorrect));
    }
    private void resetQuiz(){
        result = new ArrayList<>();
        initializeQuestionList();
        quesFragment = null;
        createFragmentWithNewQues();
        selectedIndex = 0;
        progressBar.setProgress(selectedIndex+1);
    }
    private void saveToFileStorage(){
        if (FileStorage.checkIfFileExist(this)){
            FileStorage.addNewResult(this,result);
        }
        else {
            List<List<ResultModel>> resultList = new ArrayList<>();
            resultList.add(result);
            FileStorage.saveResultList(this, resultList);
        }
    }

    // alert / toast functions
    private void showAlertDialogForResult(String correctAns, String numOfQues) {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the title and message for the dialog
        builder.setTitle(getString(R.string.result));
        builder.setMessage(getString(R.string.your_score,correctAns,numOfQues));

        builder.setPositiveButton(getString(R.string.save), (dialog, id) -> {
            saveToFileStorage();
            resetQuiz();
        });
        builder.setNegativeButton(getString(R.string.ignore), (dialog, id) -> {
            resetQuiz();
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showAlertDialogForAverage(String correctAns, String numOfAttempt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.correct_ans,correctAns,numOfAttempt));
        builder.setPositiveButton(getString(R.string.ok), (dialog, id) -> {
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}