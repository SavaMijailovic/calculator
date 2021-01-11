public class Expression {

    public static boolean isInt(double br) {
        return (long)br == br;
    }

    private static String toPrint(double br) {
        if (Double.isInfinite(br)) {
            return "∞";
        }
        if (isInt(br)) {
            return "" + (long)br;
        }
        return "" + br;
    }

    private static String superscript(double br) {
        char[] digits = (toPrint(br)).toCharArray();
        for (int i = 0; i < digits.length; i++) {
            digits[i] = superscriptOf(digits[i]);
        }
        return String.valueOf(digits);
    }

    private static char superscriptOf(char c) {
        switch(c) {
            case '0': return '⁰';
            case '1': return '¹';
            case '2': return '²';
            case '3': return '³';
            case '4': return '⁴';
            case '5': return '⁵';
            case '6': return '⁶';
            case '7': return '⁷';
            case '8': return '⁸';
            case '9': return '⁹';
            case '-': return '⁻';
            default: return '⋅';
        }
    }

    public static Expression expression = null;

    public static Expression get() {
        if (expression == null) {
            expression = new Expression();
        }
        return expression;
    }

    private double x;
    private double y;
    private Operation op;

    private Expression() {}

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        if (x == -0.0) {
            x = 0;
        }
        this.x = x;
    }

    public void setY(double y) {
        if (y == -0.0) {
            y = 0;
        }
        this.y = y;
    }

    public void setOp(Operation op) {
        this.op = op;
    }

    public double getValue() {
        switch(op) {
            case ADD: return x + y;
            case SUB: return x - y;
            case MUL: return x * y;
            case DIVISION: return x / y;
            case DIV: return (double)(((long)x) / ((long)y));
            case MOD: return ((long)x) % ((long)y);
            case EXP: return Math.pow(x, y);
            case SQRT: return Math.sqrt(x);
            default: return 0;
        }
    }

    @Override
    public String toString() {
        if (op == Operation.SQRT) {
            return Operation.SQRT + toPrint(x) + " = " + toPrint(getValue());
        }
        else if (op == Operation.EXP) {
            return toPrint(x) + superscript(y) + " = " + toPrint(getValue());
        }
        else {
            return toPrint(x) + " " + op + " " + (y < 0 ? "(" + toPrint(y) + ")" : toPrint(y)) + " = " + toPrint(getValue());
        }
    }
}
