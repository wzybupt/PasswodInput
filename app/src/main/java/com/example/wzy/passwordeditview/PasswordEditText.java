package com.example.wzy.passwordeditview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

/**
 * This is a single editText for the whole password
 *
 * Created by wzy on 16/11/2.
 */
public class PasswordEditText extends EditText {

    private OnDelKeyEventListener mDelKeyEventListener;

    public PasswordEditText(Context context) {
        super(context);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new PinCodeInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    public void setDelKeyEventListener(OnDelKeyEventListener delKeyEventListener) {
        this.mDelKeyEventListener = delKeyEventListener;
    }

    private class PinCodeInputConnection extends InputConnectionWrapper {
        public PinCodeInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                if (mDelKeyEventListener != null) {
                    mDelKeyEventListener.onDeleteClick();
                }
            }
            return super.sendKeyEvent(event);
        }

        /**
         * We must override this method
         * In some strange phone,when you press delete button, this method will be triggered,
         * not the {@link #sendKeyEvent(KeyEvent)}
         */
        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }

            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    public interface OnDelKeyEventListener {

        void onDeleteClick();

    }
}
