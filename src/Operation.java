public enum Operation {
    ADD("+"),
    SUB("-"),
    MUL("×"),
    DIVISION("÷"),
    DIV("/"),
    MOD("%"),
    EXP("xʸ"),
    SQRT("√");

    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
