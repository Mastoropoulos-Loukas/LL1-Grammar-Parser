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


class TernaryEvaluator {
    private final InputStream in;

    private int lookahead;

    public TernaryEvaluator(InputStream in) throws IOException {
        this.in = in;
        lookahead = in.read();
    }

    private void consume(int symbol) throws IOException, ParseError {
        if (lookahead == symbol)
            lookahead = in.read();
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
        int value = Tern();

        if (lookahead != -1 && lookahead != '\n')
            throw new ParseError();

        return value;
    }

    private int Tern() throws IOException, ParseError {
        if (isDigit(lookahead)) {
            int cond = evalDigit(lookahead);

            consume(lookahead);
            return TernTail(cond); 
        } 

        throw new ParseError();
    }

    private int TernTail(int condition) throws IOException, ParseError {
        switch (lookahead) {
            case '?':
                consume('?');
                int left = Tern();
                consume(':');
                int right = Tern();

                return condition != 0 ? left : right;
            case -1:
            case '\n':
            case ':':
                return condition;
        }

        throw new ParseError();
    }
}
