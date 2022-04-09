import java.io.IOException;

class Main {
    public static void main(String[] args) {
        try {
            System.out.println((new NumericEvaluator(System.in)).eval());
        } catch (IOException | ParseError e) {
            System.err.println(e.getMessage());
        }
    }
}

