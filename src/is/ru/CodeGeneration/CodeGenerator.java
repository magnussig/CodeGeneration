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
    }
}
