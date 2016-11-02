package com.example.wzy.passwordeditview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class MainActivity extends Activity {

    private PasswordInputView mPasswordInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        mPasswordInputView = (PasswordInputView) findViewById(R.id.password_input);
        mPasswordInputView.setFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mPasswordInputView, InputMethodManager.SHOW_IMPLICIT);

        mPasswordInputView.setTextFullListener(new PasswordInputView.TextFullListener() {
            @Override
            public void textFull() {
                Toast.makeText(MainActivity.this, mPasswordInputView.getText(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void textUnFull() {

            }
        });
    }
}
