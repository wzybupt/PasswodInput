package com.example.wzy.passwordeditview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * This is the whole view of password input
 * <p/>
 * Created by wzy on 16/11/2.
 */
public class PasswordInputView extends LinearLayout implements View.OnClickListener {
    private int[] editId = new int[]{R.id.pin_edit_1, R.id.pin_edit_2, R.id.pin_edit_3, R.id.pin_edit_4};
    private final int COUNT = editId.length;
    private PasswordEditText[] mPinEditTexts = new PasswordEditText[COUNT];
    private int mActiveIndex = 0;
    private TextFullListener mTextFullListener;
    private Context mContext;

    public PasswordInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PasswordInputView(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new PinCodeInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    @SuppressWarnings("unused")
    public void setTextFullListener(TextFullListener textFullListener) {
        this.mTextFullListener = textFullListener;
    }

    private void initViews() {
        TextWatcher watcher = new PinCodeTextWatcher();
        for (int i = 0; i < COUNT; i++) {
            mPinEditTexts[i] = (PasswordEditText) findViewById(editId[i]);
            mPinEditTexts[i].addTextChangedListener(watcher);
            mPinEditTexts[i].setOnClickListener(this);
            mPinEditTexts[i].setDelKeyEventListener(mOnDelKeyEventListener);
        }
        this.setOnClickListener(this);
    }

    class PinCodeTextWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            setFocus();
            if (mPinEditTexts[COUNT - 1].getText().length() != 0) {
                mTextFullListener.textFull();
            } else {
                mTextFullListener.textUnFull();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

    }

    @Override
    public void onClick(View v) {
        setFocus();
    }

    /**
     * handle the focus among the passwordEditText
     */
    public void setFocus() {
        for (int i = 0; i < mPinEditTexts.length; i++) {
            if (mPinEditTexts[i].getText().length() == 0) {
                mActiveIndex = i;
                break;
            }
        }
        if (mActiveIndex < 0) {
            mActiveIndex = 0;
        }
        mPinEditTexts[mActiveIndex].requestFocus();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mPinEditTexts[mActiveIndex], InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Get the string user input
     */
    @SuppressWarnings("unused")
    public String getText() {
        StringBuilder stringBuilder = new StringBuilder();
        for (EditText editText : mPinEditTexts) {
            if (editText.getText().length() != 0) {
                stringBuilder.append(editText.getText());
            }
        }
        return stringBuilder.toString();
    }

    private void clearSingleText(boolean hasTextInSelf) {
        if (!hasTextInSelf) {
            for (int i = 0; i < mPinEditTexts.length; i++) {
                if (mPinEditTexts[i].getText().length() == 0) {
                    if (i == 0) {
                        i = 1;
                    }
                    mPinEditTexts[i - 1].setText("");
                    break;
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public void clear() {
        for (EditText editText : mPinEditTexts) {
            editText.setText("");
        }
        setFocus();
    }

    public interface TextFullListener {
        void textFull();

        void textUnFull();
    }

    private class PinCodeInputConnection extends InputConnectionWrapper {
        public PinCodeInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                for (EditText editText : mPinEditTexts) {
                    if (editText.hasFocus()) {
                        clearSingleText(editText.getText().length() != 0);
                    }
                }
            }
            return super.sendKeyEvent(event);
        }
    }

    private PasswordEditText.OnDelKeyEventListener mOnDelKeyEventListener = new PasswordEditText.OnDelKeyEventListener() {
        @Override
        public void onDeleteClick() {
            clearSingleText(mPinEditTexts[mActiveIndex].getText().length() != 0);
        }
    };
}
