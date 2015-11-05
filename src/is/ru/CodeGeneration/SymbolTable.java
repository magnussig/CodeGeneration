package is.ru.CodeGeneration;

import java.util.*;

public class SymbolTable {

    private static Hashtable<String, SymbolTableEntry> s_lookupList = new Hashtable<String, SymbolTableEntry>();
    private static ArrayList<SymbolTableEntry> s_seqList = new ArrayList<SymbolTableEntry>();

    public static SymbolTableEntry lookup(String lexeme) {
        return s_lookupList.get(lexeme);
    }

    public static SymbolTableEntry insert(String lexeme) {
        SymbolTableEntry symTabEntry = new SymbolTableEntry(lexeme);
        s_lookupList.put(lexeme, symTabEntry);
        s_seqList.add(symTabEntry);
        return symTabEntry;
    }

    public static int size() {
        return s_seqList.size();
    }

    public static SymbolTableEntry getEntry(int idx) {
        return s_seqList.get(idx);
    }
}