package home.com.twisterjudge.entity;

import home.com.twisterjudge.entity.Body;
import home.com.twisterjudge.entity.Color;
import lombok.Data;

@Data
public class StepObject {

    private Color color;
    private Body body;

    public StepObject() {
    }

    public StepObject(Color color, Body body) {
        this.color = color;
        this.body = body;
    }
}
