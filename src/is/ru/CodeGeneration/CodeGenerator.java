package is.ru.CodeGeneration;

import is.ru.CodeGeneration.Enums.TacCode;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.rmi.UnexpectedException;
import java.util.ArrayList;

/**
 * Created by maggi on 5.11.2015.
 * This class should be the actual code generator, containing methods that will be called by the parser to actually
 * generate code.
 */
public class CodeGenerator {

    public CodeGenerator() {

    }

    protected static void generate(TacCode op, SymbolTableEntry param1, SymbolTableEntry param2, SymbolTableEntry result){
        QuadrupleList.insert(op, param1, param2, result);
        print();
    }

    protected static void print(){
        boolean isLabel = false;
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("tacCode.tac", "UTF-8");
        }catch(Exception e){
            System.exit(1);
        }
        for(Quadruple q:QuadrupleList.QList) {

            String p1;
            String p2;
            String result;
            String label;



            if (q.Op == TacCode.LABEL) {
                label = q.Result.getLexeme() + ":";
                writer.printf("%10s", label);
                isLabel = true;
                continue;
            }
            else if(isLabel==false){
                writer.printf("%10s","");
            }
            isLabel = false;


            if (q.Param1 == null) {
                p1 = "";
            } else {
                p1 = q.Param1.getLexeme();

            }

            if (q.Param2 == null) {
                p2 ="";
            } else {
                p2 = q.Param2.getLexeme();

            }

            if (q.Result == null) {

                result ="";
            } else {
                result = q.Result.getLexeme();

            }
            writer.printf("%10s%10s%10s %10s\n",q.Op, p1,p2,result);

        }
        writer.close();

    }

    /**
     *
     * @param paramList
     */
    protected static void addFormalParameters(TacCode param, ArrayList<SymbolTableEntry> paramList) {
        for (SymbolTableEntry temp : paramList) {
            generate(param,null,null,temp);
        }
    }
}
