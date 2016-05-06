package net.alqs.melayukeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.IBinder;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

/**
 * @author fikr4n
 */
public class ImService extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private InputMethodManager imManager;
    private KeyboardView keyboardView;
    private JawiKeyboard jawiKeyboard;
    private JawiKeyboard symbolKeyboard;
    private JawiKeyboard currentKeyboard;

    @Override
    public void onCreate() {
        super.onCreate();
        imManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    @Override
    public void onInitializeInterface() {
        super.onInitializeInterface();
        jawiKeyboard = new JawiKeyboard(this, R.xml.keyboard_jawi);
        symbolKeyboard = new JawiKeyboard(this, R.xml.keyboard_symbol);
    }

    @Override
    public View onCreateInputView() {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboardView.setOnKeyboardActionListener(this);
        setKeyboard(jawiKeyboard);
        return keyboardView;
    }

    private IBinder getToken() {
        try {
            return getWindow().getWindow().getAttributes().token;
        } catch (NullPointerException e) {
            return null;
        }
    }

    private void setKeyboard(JawiKeyboard keyboard) {
        if (Build.VERSION.SDK_INT >= 19)
            keyboard.setLangSwitchKeyVisible(imManager.shouldOfferSwitchingToNextInputMethod(
                    getToken()));
        else if (Build.VERSION.SDK_INT >= 16)
            keyboard.setLangSwitchKeyVisible(true);
        keyboardView.setKeyboard(keyboard);
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        switch (attribute.inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_DATETIME:
            case InputType.TYPE_CLASS_NUMBER:
            case InputType.TYPE_CLASS_PHONE:
                currentKeyboard = symbolKeyboard;
                break;
            case InputType.TYPE_CLASS_TEXT:
            default:
                currentKeyboard = jawiKeyboard;
        }
        currentKeyboard.setImeOptions(getResources(), attribute.imeOptions);
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();
        currentKeyboard = jawiKeyboard;
        if (keyboardView != null)
            keyboardView.closing();
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        setKeyboard(currentKeyboard);
        keyboardView.closing();
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection conn = getCurrentInputConnection();
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                conn.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_DONE:
                if ((currentKeyboard.getImeAction() & EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0)
                    conn.commitText("\n", 1);
                else
                    conn.performEditorAction(EditorInfo.IME_ACTION_UNSPECIFIED);
                break;
            case JawiKeyboard.KEYCODE_LANG_SWITCH:
                if (Build.VERSION.SDK_INT >= 16)
                    imManager.switchToNextInputMethod(getToken(), false);
                break;
            case Keyboard.KEYCODE_MODE_CHANGE:
                if (keyboardView != null) {
                    if (keyboardView.getKeyboard() == symbolKeyboard)
                        setKeyboard(jawiKeyboard);
                    else
                        setKeyboard(symbolKeyboard);
                }
                break;
            case ' ':
                conn.commitText(" ", 1);
                if (keyboardView.getKeyboard() != jawiKeyboard)
                    setKeyboard(jawiKeyboard);
                break;
            default:
                char code = (char) primaryCode;
                conn.commitText(String.valueOf(code), 1);
        }
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }
}