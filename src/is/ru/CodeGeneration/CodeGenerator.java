package is.ru.CodeGeneration;

import is.ru.CodeGeneration.Enums.TacCode;

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
        for(Quadruple q:QuadrupleList.QList){

               System.out.println(" \t \t " +  q.Op + " \t \t " + q.Param1 + " \t \t " + q.Param2 + " \t \t " + q.Result.getLexeme());

        }

        System.out.println("NEXT");
    }

    /**
     *
     * @param paramList
     */
    protected static void addFormalParameters(ArrayList<SymbolTableEntry> paramList) {
        for (SymbolTableEntry temp : paramList) {
            generate(TacCode.FPARAM,null,null,temp);
        }
    }
}
