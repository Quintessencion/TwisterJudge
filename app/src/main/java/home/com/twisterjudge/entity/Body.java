package home.com.twisterjudge.entity;

import lombok.Data;

@Data
public class Body {

    private String textBody;
    private int idBody;

    public Body() {
    }

    public Body(String textBody, int idBody) {
        this.textBody = textBody;
        this.idBody = idBody;
    }
}
