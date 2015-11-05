package is.ru.CodeGeneration;
import is.ru.CodeGeneration.Enums.TacCode;

/**
 * Created by maggi on 5.11.2015.
 */
public class Quadruple {

    public static TacCode Op;
    public static SymbolTableEntry Param1;
    public static SymbolTableEntry Param2;
    public static SymbolTableEntry Result;

    public Quadruple(TacCode Op, SymbolTableEntry Param1, SymbolTableEntry Param2, SymbolTableEntry Result) {
        this.Op = Op;
        this.Param1 = Param1;
        this.Param2 = Param2;
        this.Result = Result;
    }

    public static TacCode getOp() {
        return Op;
    }

    public static void setOp(TacCode op) {
        Op = op;
    }

    public static SymbolTableEntry getParam1() {
        return Param1;
    }

    public static void setParam1(SymbolTableEntry param1) {
        Param1 = param1;
    }

    public static SymbolTableEntry getParam2() {
        return Param2;
    }

    public static void setParam2(SymbolTableEntry param2) {
        Param2 = param2;
    }

    public static SymbolTableEntry getResult() {
        return Result;
    }

    public static void setResult(SymbolTableEntry result) {
        Result = result;
    }

}
