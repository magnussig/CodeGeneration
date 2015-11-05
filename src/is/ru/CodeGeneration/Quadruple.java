package is.ru.CodeGeneration;
import is.ru.CodeGeneration.Enums.TacCode;

/**
 * Created by maggi on 5.11.2015.
 */
public class Quadruple {

    public  TacCode Op;
    public  SymbolTableEntry Param1;
    public  SymbolTableEntry Param2;
    public  SymbolTableEntry Result;

    public Quadruple(TacCode Op, SymbolTableEntry Param1, SymbolTableEntry Param2, SymbolTableEntry Result) {
        this.Op = Op;
        this.Param1 = Param1;
        this.Param2 = Param2;
        this.Result = Result;
    }

    public TacCode getOp() {
        return Op;
    }

    public void setOp(TacCode op) {
        Op = op;
    }

    public SymbolTableEntry getParam1() {
        return Param1;
    }

    public void setParam1(SymbolTableEntry param1) {
        Param1 = param1;
    }

    public SymbolTableEntry getParam2() {
        return Param2;
    }

    public void setParam2(SymbolTableEntry param2) {
        Param2 = param2;
    }

    public SymbolTableEntry getResult() {
        return Result;
    }

    public void setResult(SymbolTableEntry result) {
        Result = result;
    }

}
