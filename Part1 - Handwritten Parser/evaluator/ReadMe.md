# Evaluator
Reads from the input stream, and evaluates the input according to the following grammar:<br>
1.exp -> term exp2<br>
2.exp2 -> '^' term exp2<br>
3.exp2 -> ε<br>
<br>
4.term -> num term2<br>
5.term2 -> '&' num term2<br>
6.term2 -> ε<br>
<br>
7.num -> '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9'<br>
8.num -> '(' exp ')'<br>
<br>
Recognizes only single digit numbers and no white space.<br>
<br>
Compile: javac Main.java<br>
Run: java Main 