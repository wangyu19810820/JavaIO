package wangyu1981;

public enum  State {
    AAA(100),
    BBB(200);

    private int code;
    private State(int code) {
        this.code = code;
    }
}
