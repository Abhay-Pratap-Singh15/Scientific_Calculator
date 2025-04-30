public class ExpressionParser {
    private int pos = -1, ch;
    private String expression;

    public double parse(String expression) {
        this.expression = expression;
        pos = -1;
        nextChar();
        double x = parseExpression();
        if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
        return x;
    }

    private void nextChar() {
        ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
    }

    private boolean eat(int charToEat) {
        while (ch == ' ') nextChar();
        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    private double parseExpression() {
        double x = parseTerm();
        while (true) {
            if (eat('+')) x += parseTerm(); // addition
            else if (eat('-')) x -= parseTerm(); // subtraction
            else return x;
        }
    }

    private double parseTerm() {
        double x = parseFactor();
        while (true) {
            if (eat('*')) x *= parseFactor(); // multiplication
            else if (eat('/')) x /= parseFactor(); // division
            else if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation
            else return x;
        }
    }

    private double parseFactor() {
        if (eat('+')) return parseFactor(); // unary plus
        if (eat('-')) return -parseFactor(); // unary minus

        double x;
        int startPos = this.pos;

        if (eat('(')) { // parentheses
            x = parseExpression();
            eat(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
            while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
            x = Double.parseDouble(expression.substring(startPos, this.pos));
        } else if (ch >= 'a' && ch <= 'z') { // functions
            while (ch >= 'a' && ch <= 'z') nextChar();
            String func = expression.substring(startPos, this.pos);
            x = parseFactor();
            x = applyFunction(func, x);
        } else {
            throw new RuntimeException("Unexpected: " + (char) ch);
        }

        return x;
    }

    private double applyFunction(String func, double x) {
        return switch (func) {
            case "sqrt" -> Math.sqrt(x);
            case "cbrt" -> Math.cbrt(x);
            case "log" -> Math.log10(x);
            case "ln" -> Math.log(x);
            case "sin" -> Math.sin(Math.toRadians(x));
            case "cos" -> Math.cos(Math.toRadians(x));
            case "tan" -> Math.tan(Math.toRadians(x));
            case "asin" -> Math.toDegrees(Math.asin(x));
            case "acos" -> Math.toDegrees(Math.acos(x));
            case "atan" -> Math.toDegrees(Math.atan(x));
            case "abs", "|x|" -> Math.abs(x);
            case "%" -> x / 100.0;
            default -> throw new RuntimeException("Unknown function: " + func);
        };
    }
}
