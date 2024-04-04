package com.example.ipwizard20;

import android.text.TextWatcher;
import android.widget.EditText;

public class RemoveTextWatcher extends  Thread{
    EditText edit_text;
    TextWatcher text_watcher;
    public RemoveTextWatcher( EditText edit_text_, TextWatcher text_watcher_) {
        edit_text = edit_text_;
        text_watcher = text_watcher_;
    }
    public void run() {
        edit_text.removeTextChangedListener(text_watcher);
    }
}
