package com.example.calculator_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
//    private Button btn0 , btn1 ,btn2 ,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btnPoint,btnPlus,btnMoin,btnEgal,btnMultuple , btnDivision ,btnPercent,btnParentheses , btnCancel , btnCancelAll;
    private TextView textResult ;
    private String inputNumber = "" ;
    private double firstNumber = 0 ;
    private String operation = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        textResult = findViewById(R.id.resultText);
        findViewById(R.id.BtnZero).setOnClickListener(v -> {appendNumber("0");});
        findViewById(R.id.BtnOne).setOnClickListener(v -> {appendNumber("1");});
        findViewById(R.id.BtnTwo).setOnClickListener(v -> {appendNumber("2");});
        findViewById(R.id.BtnThree).setOnClickListener(v -> {appendNumber("3");});
        findViewById(R.id.BtnFour).setOnClickListener(v -> {appendNumber("4");});
        findViewById(R.id.BtnFive).setOnClickListener(v -> {appendNumber("5");});
        findViewById(R.id.BtnSix).setOnClickListener(v -> {appendNumber("6");});
        findViewById(R.id.BtnSeven).setOnClickListener(v -> {appendNumber("7");});
        findViewById(R.id.BtnEight).setOnClickListener(v -> {appendNumber("8");});
        findViewById(R.id.BtnNine).setOnClickListener(v -> {appendNumber("9");});
        findViewById(R.id.BtnPoint).setOnClickListener(v -> {appendNumber(".");});
        findViewById(R.id.BtnAllCancel).setOnClickListener(v -> {cancelAll();});
        findViewById(R.id.BtnCancel).setOnClickListener(v -> {cancel();});
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        findViewById(R.id.BtnPlus).setOnClickListener(v -> selectOperateur("+"));
        findViewById(R.id.btnMinuss ).setOnClickListener(v -> selectOperateur("-"));
        findViewById(R.id.BtnMultiplication).setOnClickListener(v -> selectOperateur("x"));
        findViewById(R.id.BtnDivision).setOnClickListener(v -> selectOperateur("÷"));
        findViewById(R.id.BtnPercent).setOnClickListener(v -> selectOperateur("%"));
        findViewById(R.id.BtnResult).setOnClickListener(v -> calculation());
     }
    public void appendNumber(String number){
        inputNumber+=number;
        textResult.setText(inputNumber);
    }

    public void cancelAll() {
        inputNumber = "";
        textResult.setText("");
        firstNumber = 0;
        operation = "";
    }
    public  void selectOperateur(String operationSelected){
        if (!inputNumber.isEmpty()){
            firstNumber = Double.parseDouble(inputNumber);
            operation = operationSelected;
            inputNumber = "";
        }
    }
    public void calculation(){
        if (!inputNumber.isEmpty() && !operation.isEmpty() ){
            double secondNumber = Double.parseDouble(inputNumber);
            double result = 0.0;
            switch (operation){
                case "+":
                    result = firstNumber + secondNumber ;
                    break ;
                case "-":
                    result = firstNumber - secondNumber ;
                    break ;
                case "x":
                    result = firstNumber * secondNumber ;
                    break ;
                case "÷":
                    if (secondNumber != 0) {
                        result = firstNumber / secondNumber;
                    } else {
                        textResult.setText("Error: Division by 0");
                        return;
                    }
                    break ;
                case "%":
                    result = (firstNumber * secondNumber) / 100;
                    break ;
            }
            textResult.setText(String.valueOf(result));
            inputNumber = String.valueOf(result);
            operation = "";
        }
    }
    public void cancel(){
        if (!inputNumber.isEmpty()){
            inputNumber = inputNumber.substring(0,inputNumber.length()-1);
            textResult.setText(inputNumber);
        }
    }
}