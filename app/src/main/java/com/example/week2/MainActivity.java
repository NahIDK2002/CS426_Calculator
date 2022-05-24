package com.example.week2;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private View.OnClickListener myAction = null;
    private double res=0;
    private boolean pressPoint = false;
    private boolean newNum = true;    
    private enum Operation{
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE
    }

    Operation lastOperation = Operation.PLUS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        create24ButtonsLayout();
    }

    private void create24ButtonsLayout() {
        GridLayout gridLayout = createGridLayout();
        Button[] buttons = create24Buttons();
        add24ButtonToGridLayout(gridLayout, buttons);
    }

    private void add24ButtonToGridLayout(GridLayout gridLayout, Button[] buttons) {
        for (int i = 0; i < buttons.length; ++i) {
            gridLayout.addView(buttons[i]);
        }

    }

    private Button[] create24Buttons() {
        Button[] buttons = new Button[24];

        String[] captions = getCaptions();

        for (int i = 0; i < 24; ++i) {
            buttons[i] = createButton(captions[i], 6400 + i);
        }

        return buttons;
    }

    @NonNull
    private String[] getCaptions() {
        return new String[]{
                "%", "CE", "C", "BkSp",
                "1/x", "x^2", "sqrt", "./.",
                "7", "8", "9", "x",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "+/-", "0", ".", "="
        };
    }

    private Button createButton(String caption, int id) {
        Button button = new Button(this);
        button.setId(id);
        button.setText(caption.toString());
        button.setTag(false);
        button.setOnClickListener(this);

        return button;
    }

    private GridLayout createGridLayout() {
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setId((int) 65000);
        gridLayout.setColumnCount(4);
        gridLayout.setRowCount(6);
        addGridLayerToMainView(gridLayout);

        return gridLayout;
    }

    private void addGridLayerToMainView(GridLayout gridLayout) {
        LinearLayout mainView = findViewById(R.id.mainActivity);
        mainView.addView(gridLayout);
    }

    public void onClickButton(View view) {
        Button button = (Button) view;
        if (button.getId() == R.id.buttonMC) {
            button.setText("Clicked!");
        } else if (button.getId() == R.id.buttonMR) {
            button.setText("Me");
        }
    }

    @Override
    public void onClick(View view){
        Button b = (Button) view;

        if (b.getTag() == null) return;

        boolean bButtonState = (boolean) b.getTag();
        b.setTextColor(bButtonState ? Color.BLACK : Color.BLUE);
        b.setTag(!bButtonState);

        int id = b.getId();
        TextView editText = (TextView) findViewById(R.id.textView);

        if (id == 6422) editText.setText(clickPoint(editText.getText().toString()));
        else if (id == 6403) editText.setText(clickBackspace(editText.getText().toString()));
        else if (id<6407 || id == 6420) return;
        else if (id%4 == 3){
            editText.setText(doOperator(editText.getText().toString(), lastOperation));
            pressPoint = false;
            newNum = true;
            switch (id){
                case 6407:
                    Log.d("op", "div");
                    lastOperation = Operation.DIVIDE;
                    break;

                case 6411:
                    Log.d("op", "mult");
                    lastOperation = Operation.MULTIPLY;
                    break;

                case 6415:
                    Log.d("op", "minus");
                    lastOperation = Operation.MINUS;
                    break;

                case 6419:
                    Log.d("op", "plus");
                    lastOperation = Operation.PLUS;
                    break;

                case 6423:
                    Log.d("op", "equal");
                    equal();
                    break;
            }
        }
        else{
            String txt = b.getText().toString();
            editText.setText(addNum(editText.getText().toString(), txt));
        }
    }

    private String doOperator(String text, Operation operator) {
        Log.d("doOperator", operator.toString());
        double n = Double.parseDouble(text);
        if (newNum) n=0;
        switch (operator){
            case PLUS:
                return plus(n);
            
            case MINUS:
                return minus(n);

            case MULTIPLY:
                return multiply(n);

            default:
                return divide(n);
        }
    }

    private String clickBackspace(String txt){
        if (newNum) return txt;
        if ((txt != null) && (txt.length() > 1)) {
            return txt.substring(0, txt.length() - 1);
        }
        return "0";
    }

    private String addNum(String s, String num){
        if (newNum){
            s=num;
            newNum = false;
        }
        else s+=num;

        return s;
    }

    private String clickPoint(String s){
        if (pressPoint) return s;
        if (newNum){
            newNum  = false;
            pressPoint = true;
            return "0.";
        }
        pressPoint = true;
        return s+'.';
    }

    //TODO: calculate sum
    private String plus(double n){
        res += n;
        Log.d("plus", "plus: " + Double.toString(res));
        return Double.toString(res);
    }

    //TODO: calculate minus
    private String minus(double n){
        res -= n;
        return Double.toString(res);
    }

    //TODO: calculate multiply
    private String multiply(double n){
        res *= n;
        return Double.toString(res);
    }

    //TODO: calculate divide
    private String divide (double n){
        if (n==0){
            return "Cannot divide by zero";
        }
        res/=n;
        return Double.toString(res);        
    }

    //TODO: calculate equal
    private void equal (){
        newNum=true;
        pressPoint=false;
        lastOperation = Operation.PLUS;
    }
}