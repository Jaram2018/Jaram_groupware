package jaram.groupware.groupware.model.value;

public enum Position {
    수습,
    준회원,
    정회원,
    준OB,
    OB,
    회장,
    부회장,
    회계,
    학술부장,
    섭외부장,
    서버관리자;

    public String getPosition(){
        return this.toString();
    }
}
