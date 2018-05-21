package home.com.twisterjudge.entity;

import lombok.Data;

@Data
public class Color {

    private String textColor;
    private int color;

    public Color() {
    }

    public Color(String textColor, int color) {
        this.textColor = textColor;
        this.color = color;
    }
}
