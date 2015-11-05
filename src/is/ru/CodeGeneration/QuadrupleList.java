package is.ru.CodeGeneration;

import is.ru.CodeGeneration.Enums.TacCode;

import java.util.ArrayList;

/**
 * Created by maggi on 5.11.2015.
 */
public class QuadrupleList {
    public static ArrayList<Quadruple> QList = new ArrayList<Quadruple>();
    /*public static Quadruple getQuadruple(String lexeme) {
        return s_lookupList.get(lexeme);
    }*/


    public static Quadruple insert(TacCode op, SymbolTableEntry param1, SymbolTableEntry param2, SymbolTableEntry result) {
        Quadruple q = new Quadruple(op, param1, param2, result);
        QList.add(q);
        return q;
    }

    public static int size() {
        return QList.size();
    }
}
