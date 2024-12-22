package com.example.calculator_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView textResult, secondaryText;
    private String inputNumber = "";
    private double firstNumber = 0;
    private String operation = "";
    private boolean isDegreeMode = true;
    private boolean isSecondaryMode = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textResult = findViewById(R.id.resultText);
        secondaryText = findViewById(R.id.secondaryText);

        findViewById(R.id.BtnZero).setOnClickListener(v -> appendNumber("0"));
        findViewById(R.id.BtnOne).setOnClickListener(v -> appendNumber("1"));
        findViewById(R.id.BtnTwo).setOnClickListener(v -> appendNumber("2"));
        findViewById(R.id.BtnThree).setOnClickListener(v -> appendNumber("3"));
        findViewById(R.id.BtnFour).setOnClickListener(v -> appendNumber("4"));
        findViewById(R.id.BtnFive).setOnClickListener(v -> appendNumber("5"));
        findViewById(R.id.BtnSix).setOnClickListener(v -> appendNumber("6"));
        findViewById(R.id.BtnSeven).setOnClickListener(v -> appendNumber("7"));
        findViewById(R.id.BtnEight).setOnClickListener(v -> appendNumber("8"));
        findViewById(R.id.BtnNine).setOnClickListener(v -> appendNumber("9"));
        findViewById(R.id.BtnPoint).setOnClickListener(v -> appendNumber("."));
        findViewById(R.id.btnParentese).setOnClickListener(v -> appendNumber("()"));

        findViewById(R.id.BtnPlus).setOnClickListener(v -> selectOperator("+"));
        findViewById(R.id.btnMinuss).setOnClickListener(v -> selectOperator("-"));
        findViewById(R.id.BtnMultiplication).setOnClickListener(v -> selectOperator("x"));
        findViewById(R.id.BtnDivision).setOnClickListener(v -> selectOperator("÷"));
        findViewById(R.id.btnPercent).setOnClickListener(v -> selectOperator("%"));

        findViewById(R.id.BtnAllCancel).setOnClickListener(v -> cancelAll());
        findViewById(R.id.BtnCancel).setOnClickListener(v -> cancel());
        findViewById(R.id.BtnResult).setOnClickListener(v -> calculateResult());

        findViewById(R.id.btnSin).setOnClickListener(v -> {
            if (isSecondaryMode) {
                calculateTrigFunction("sin⁻¹");
            } else {
                calculateTrigFunction("sin");
            }
        });
        findViewById(R.id.btnCos).setOnClickListener(v -> {
            if (isSecondaryMode) {
                calculateTrigFunction("cos⁻¹");
            } else {
                calculateTrigFunction("cos");
            }
        });
        findViewById(R.id.btnTan).setOnClickListener(v -> {
            if (isSecondaryMode) {
                calculateTrigFunction("tan⁻¹");
            } else {
                calculateTrigFunction("tan");
            }
        });

        findViewById(R.id.btnLg).setOnClickListener(v -> calculateLog("log"));
        findViewById(R.id.btnIn).setOnClickListener(v -> calculateLog("ln"));
        findViewById(R.id.btnPuissance).setOnClickListener(v -> selectOperator("^"));

        findViewById(R.id.btndeg).setOnClickListener(v -> toggleDegreeMode());
        findViewById(R.id.btn2nd).setOnClickListener(v -> toggleSecondaryMode());
    }

    public void appendNumber(String number) {
        if (number.equals(")")) {
            // Only append closing parenthesis if there's a matching opening parenthesis
            int openCount = 0;
            int closeCount = 0;

            // Count occurrences of opening and closing parentheses in the input
            for (char ch : inputNumber.toCharArray()) {
                if (ch == '(') openCount++;
                if (ch == ')') closeCount++;
            }

            // Allow closing parenthesis only if there is an unmatched opening parenthesis
            if (openCount > closeCount) {
                inputNumber += number;
            }
        } else {
            inputNumber += number; // Append other numbers or characters
        }

        // Update the UI with the current input
        textResult.setText(inputNumber);
        secondaryText.setText(secondaryText.getText().toString() + number);
    }



    public void cancelAll() {
        inputNumber = "";
        textResult.setText("");
        firstNumber = 0;
        operation = "";
    }

    public void cancel() {
        if (!inputNumber.isEmpty()) {
            inputNumber = inputNumber.substring(0, inputNumber.length() - 1);
            textResult.setText(inputNumber);
        }
    }

    public void selectOperator(String operatorSelected) {
        if (!inputNumber.isEmpty()) {
            if (operatorSelected.equals("%")) {
                // Calculate percentage
                double percentage = Double.parseDouble(inputNumber) / 100 * firstNumber;
                inputNumber = String.valueOf(percentage);
                secondaryText.setText(secondaryText.getText().toString() + " " + operatorSelected + " " + percentage);
                textResult.setText(inputNumber);
            } else {
                // Handle other operators
                firstNumber = Double.parseDouble(inputNumber);
                operation = operatorSelected;
                inputNumber = "";  // Clear input for the next number
                secondaryText.setText(secondaryText.getText().toString() + " " + operatorSelected + " ");
            }
        }
    }
    public void calculateResult() {
        try {
            if (!inputNumber.isEmpty() && !operation.isEmpty()) {
                double result = evaluateExpression(secondaryText.getText().toString());
                textResult.setText(String.valueOf(result));
                inputNumber = String.valueOf(result);

                // Show the full operation and result in secondaryText
                secondaryText.setText(secondaryText.getText().toString() + " = " + result);
            }
        } catch (IllegalArgumentException e) {
            textResult.setText("Error: " + e.getMessage());
        } catch (Exception e) {
            textResult.setText("Error: Invalid Expression");
        }
    }

    public double evaluateExpression(String expression) {
        // Remove spaces and handle parentheses
        expression = expression.replaceAll("\\s", "");

        // Ensure parentheses are balanced before proceeding
        int openCount = 0;
        int closeCount = 0;
        for (char ch : expression.toCharArray()) {
            if (ch == '(') openCount++;
            if (ch == ')') closeCount++;
        }

        if (openCount != closeCount) {
            throw new IllegalArgumentException("Mismatched parentheses");
        }

        try {
            // Evaluate parentheses first (recursively)
            while (expression.contains("(")) {
                int open = expression.lastIndexOf("(");
                int close = expression.indexOf(")", open);

                if (close == -1) {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }

                // Extract the expression inside the parentheses and evaluate it
                String innerExpression = expression.substring(open + 1, close);
                double result = evaluateExpression(innerExpression);  // Recursive evaluation

                // Replace the parentheses and inner expression with the result
                expression = expression.substring(0, open) + result + expression.substring(close + 1);
            }

            // Now evaluate basic operations (multiplication, division, etc.)
            return evaluateBasic(expression);

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Expression: " + expression, e);
        }
    }

    private double evaluateBasic(String expression) {
        // First, handle multiplication and division
        while (expression.contains("x") || expression.contains("÷")) {
            expression = handleOperation(expression, "x", "÷");
        }

        // Then handle addition and subtraction
        while (expression.contains("+") || expression.contains("-")) {
            expression = handleOperation(expression, "+", "-");
        }

        return Double.parseDouble(expression);
    }

    private String handleOperation(String expression, String op1, String op2) {
        int index1 = expression.indexOf(op1);
        int index2 = expression.indexOf(op2);

        // Find the first operation to handle
        int opIndex = (index1 >= 0 && index2 >= 0) ? Math.min(index1, index2) : Math.max(index1, index2);
        if (opIndex == -1) {
            return expression;
        }

        // Find the left and right operands
        int leftStart = opIndex - 1;
        while (leftStart >= 0 && (Character.isDigit(expression.charAt(leftStart)) || expression.charAt(leftStart) == '.')) {
            leftStart--;
        }

        int rightEnd = opIndex + 1;
        while (rightEnd < expression.length() && (Character.isDigit(expression.charAt(rightEnd)) || expression.charAt(rightEnd) == '.')) {
            rightEnd++;
        }

        double left = Double.parseDouble(expression.substring(leftStart + 1, opIndex));
        double right = Double.parseDouble(expression.substring(opIndex + 1, rightEnd));
        double result;

        // Perform the operation
        switch (expression.charAt(opIndex)) {
            case 'x':
                result = left * right;
                break;
            case '÷':
                if (right == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = left / right;
                break;
            case '+':
                result = left + right;
                break;
            case '-':
                result = left - right;
                break;
            default:
                throw new IllegalArgumentException("Unknown operator");
        }

        // Replace the operation in the expression with the result
        return expression.substring(0, leftStart + 1) + result + expression.substring(rightEnd);
    }


//    public void calculateResult() {
//        if (!inputNumber.isEmpty() && !operation.isEmpty()) {
//            double secondNumber = Double.parseDouble(inputNumber);
//            double result = 0.0;
//
//            switch (operation) {
//                case "+":
//                    result = firstNumber + secondNumber;
//                    break;
//                case "-":
//                    result = firstNumber - secondNumber;
//                    break;
//                case "x":
//                    result = firstNumber * secondNumber;
//                    break;
//                case "÷":
//                    if (secondNumber != 0) {
//                        result = firstNumber / secondNumber;
//                    } else {
//                        textResult.setText("Error: Division by 0");
//                        return;
//                    }
//                    break;
//                case "^":
//                    result = Math.pow(firstNumber, secondNumber);
//                    break;
//            }
//
//            textResult.setText(String.valueOf(result));
//            inputNumber = String.valueOf(result);
////            operation = "";
//
//            secondaryText.setText(firstNumber + " " + operation + " " + secondNumber + " = " + result);
//        }
//    }

    public void calculateTrigFunction(String function) {
        if (!inputNumber.isEmpty()) {
            double number = Double.parseDouble(inputNumber);
            double result = 0.0;

            switch (function) {
                case "sin":
                    result = Math.sin(isDegreeMode ? Math.toRadians(number) : number);
                    break;
                case "sin⁻¹":
                    result = Math.toDegrees(Math.asin(number));
                    break;
                case "cos":
                    result = Math.cos(isDegreeMode ? Math.toRadians(number) : number);
                    break;
                case "cos⁻¹":
                    result = Math.toDegrees(Math.acos(number));
                    break;
                case "tan":
                    result = Math.tan(isDegreeMode ? Math.toRadians(number) : number);
                    break;
                case "tan⁻¹":
                    result = Math.toDegrees(Math.atan(number));
                    break;
            }

            textResult.setText(String.valueOf(result));
            inputNumber = String.valueOf(result);
        }
    }

    public void calculateLog(String function) {
        if (!inputNumber.isEmpty()) {
            double number = Double.parseDouble(inputNumber);
            double result = 0.0;

            switch (function) {
                case "log":
                    if (number > 0) {
                        result = Math.log10(number);
                    } else {
                        textResult.setText("Error: Log of non-positive number");
                        return;
                    }
                    break;
                case "ln":
                    if (number > 0) {
                        result = Math.log(number);
                    } else {
                        textResult.setText("Error: Log of non-positive number");
                        return;
                    }
                    break;
            }

            textResult.setText(String.valueOf(result));
            inputNumber = String.valueOf(result);
        }
    }

    public void toggleDegreeMode() {
        isDegreeMode = !isDegreeMode;
        String mode = isDegreeMode ? "DEG" : "RAD";
        secondaryText.setText(mode);
    }

    public void toggleSecondaryMode() {
        isSecondaryMode = !isSecondaryMode; // Toggle the mode

        // Update the button text dynamically to show secondary functions
        if (isSecondaryMode) {
            ((Button) findViewById(R.id.btnSin)).setText("sin⁻¹");
            ((Button) findViewById(R.id.btnCos)).setText("cos⁻¹");
            ((Button) findViewById(R.id.btnTan)).setText("tan⁻¹");
            ((Button) findViewById(R.id.btnLg)).setText("10^x");
            ((Button) findViewById(R.id.btnIn)).setText("e^x");
        } else {
            ((Button) findViewById(R.id.btnSin)).setText("sin");
            ((Button) findViewById(R.id.btnCos)).setText("cos");
            ((Button) findViewById(R.id.btnTan)).setText("tan");
            ((Button) findViewById(R.id.btnLg)).setText("log");
            ((Button) findViewById(R.id.btnIn)).setText("ln");
        }
    }
}
