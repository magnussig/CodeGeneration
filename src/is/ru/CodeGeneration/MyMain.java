package is.ru.CodeGeneration;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;

public class MyMain {
    public static final boolean TRACE = false;

    public static void main(String [] args) throws IOException {
        URL url = MyMain.class.getResource("input.txt");
        FileReader fReader = new FileReader(new File(url.getFile()));
        Lexer lexer = new Lexer(fReader);
        String sourceFile = "C:\\Users\\HuldarBjarmi\\Documents\\GitHub\\CodeGeneration\\src\\is\\ru\\CodeGeneration\\input.txt";
        Parser parser = new Parser(lexer, sourceFile);
        parser.program();

    }
}