package is.ru.CodeGeneration;

import java.io.*;

public class MyMain {
    public static final boolean TRACE = false;

    public static void main(String [] args) throws IOException {
        Lexer lexer = new Lexer(new FileReader(args[0]));
        Parser parser = new Parser(lexer, args[0]);
        parser.program();

    }
}