package utils;

public enum Size {
    XS(0.04f),
    S(0.16f),
    XM(0.36f),
    M(0.64f);

    private final float value;
    Size(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}
