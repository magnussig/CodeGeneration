package is.ru.CodeGeneration;

import is.ru.CodeGeneration.Enums.TacCode;

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
           if(q.Op == TacCode.LABEL ) {
               if(q.Result.getLexeme() != null){
                   System.out.print(q.Result.getLexeme());
               }
               else{

                   System.out.print("lol");
               }
           }
           else {
               System.out.println(" \t \t " +  q.Op + " \t \t " + q.Param1 + " \t \t " + q.Param2 + " \t \t " + q.Result.getLexeme());
           }
        }

        System.out.println("NEXT");
    }

}
