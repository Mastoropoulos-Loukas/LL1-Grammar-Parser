import java.io.InputStream;
import java.io.IOException;
/*
1.exp -> term exp2
2.exp2 -> '^' term exp2
3.exp2 -> ε

4.term -> num term2
5.term2 -> '&' num term2
6.term2 -> ε

7.num -> '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9'
8.num -> '(' exp ')'

            $       ^       &       (       )      0...9
exp        error   error   error    #1     error     #1 
exp2        #3      #2     error   error    #3      error
term       error   error   error    #4     error     #4
term2       #6      #6      #5     error    #6      error
num        error   error   error    #8     error     #7

*/


class NumericEvaluator {
    private final InputStream in;

    private int lookahead;

    public NumericEvaluator(InputStream in) throws IOException {
        this.in = in;
        lookahead = in.read();
    }

    private void consume(int symbol) throws IOException, ParseError {
        if (lookahead == symbol){
            lookahead = in.read();
        }
        else
            throw new ParseError();
    }

    private boolean isDigit(int c) {
        return '0' <= c && c <= '9';
    }

    private int evalDigit(int c) {
        return c - '0';
    }

    public int eval() throws IOException, ParseError {
        int value = exp();

        if (lookahead != -1 && lookahead != '\n' && lookahead != 13)
            throw new ParseError();

        return value;
    }

    private int exp() throws IOException, ParseError{
        // System.out.println(String.format("Exp. Lookahead = %c", lookahead));
        if(isDigit(lookahead) || lookahead == '('){
            int termVal = term();
            return exp2(termVal);
        }
        throw new ParseError();
    }

    private int exp2(int termVal) throws IOException, ParseError{
        // System.out.println(String.format("Exp2. Lookahead = %c, termVal = %d", lookahead, termVal));
        switch (lookahead) {
            case '^':
                consume('^');
                int right = term();

                return exp2(termVal ^ right);
            case -1:
            case 13:
            case '\n':
            case ')':
                return termVal;
        }

        throw new ParseError();
    }
    private int term() throws IOException, ParseError{
        // System.out.println(String.format("Term. Lookahead = %c", lookahead));
        if(isDigit(lookahead) || lookahead == '('){
            int numVal = num();
            return term2(numVal);
        }
        throw new ParseError();
    }
    private int term2(int numVal) throws IOException, ParseError{
        // System.out.println(String.format("Term2. Lookahead = %c, numVal = %d", lookahead, numVal));
        switch (lookahead) {
            case '&':
                consume('&');
                int right = num();

                return term2(numVal & right);
            case -1:
            case 13:
            case '\n':
            case '^':
            case ')':
                return numVal;
        }

        throw new ParseError();
    }
    private int num() throws IOException, ParseError{
        // System.out.println(String.format("Num. Lookahead = %c", lookahead));
        if(isDigit(lookahead)){
            int digit = evalDigit(lookahead);
            consume(lookahead);
            return digit;
        }
        else if(lookahead == '('){
            consume(lookahead);
            int expVal = exp();
            consume(')');
            return expVal;
        }
        throw new ParseError();
    }
}
