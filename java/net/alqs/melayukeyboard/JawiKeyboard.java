package net.alqs.melayukeyboard;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;
import android.view.inputmethod.EditorInfo;

/**
 * @author fikr4n
 */
public class JawiKeyboard extends Keyboard {

    public static final int KEYCODE_LANG_SWITCH = -100;

    private Key enterKey;
    private int imeAction = EditorInfo.IME_ACTION_UNSPECIFIED;

    public JawiKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public JawiKeyboard(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        super(context, xmlLayoutResId, modeId, width, height);
    }

    public JawiKeyboard(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
    }

    public JawiKeyboard(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }

    @Override
    protected Key createKeyFromXml(Resources res, Row parent, int x, int y, XmlResourceParser parser) {
        Key key = super.createKeyFromXml(res, parent, x, y, parser);
        int primaryCode = key.codes[0];
        switch (primaryCode) {
            case Keyboard.KEYCODE_DONE:
                enterKey = key;
                break;
        }
        return key;
    }

    public void setLangSwitchKeyVisible(boolean visible) {
        // TODO implement
    }

    public void setImeOptions(Resources res, int options) {
        if (enterKey == null)
            return;

        int action = options & (EditorInfo.IME_MASK_ACTION | EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        imeAction = action;
        switch (action) {
            case EditorInfo.IME_ACTION_GO:
                enterKey.iconPreview = null;
                enterKey.icon = null;
                enterKey.label = res.getText(R.string.label_go_key);
                break;
            case EditorInfo.IME_ACTION_NEXT:
                enterKey.iconPreview = null;
                enterKey.icon = null;
                enterKey.label = res.getText(R.string.label_next_key);
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                enterKey.iconPreview = null;
                enterKey.icon = null;
                enterKey.label = res.getText(R.string.label_search_key);
                break;
            case EditorInfo.IME_ACTION_SEND:
                enterKey.iconPreview = null;
                enterKey.icon = null;
                enterKey.label = res.getText(R.string.label_send_key);
                break;
            case EditorInfo.IME_ACTION_DONE:
                enterKey.iconPreview = null;
                enterKey.icon = null;
                enterKey.label = res.getText(R.string.label_done_key);
                break;
            default:
//                enterKey.icon = res.getDrawable(R.drawable.sym_keyboard_return);
//                enterKey.label = null;
                enterKey.iconPreview = null;
                enterKey.icon = null;
                enterKey.label = res.getText(R.string.label_return_key);
                break;
        }
    }

    public int getImeAction() {
        return imeAction;
    }
}
