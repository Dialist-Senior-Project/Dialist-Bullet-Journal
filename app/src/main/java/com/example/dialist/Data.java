package com.example.dialist;

public class Data {
    boolean checked;
    String content;

    public Data(boolean checked, String content) {
        this.checked = checked;
        this.content = content;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
