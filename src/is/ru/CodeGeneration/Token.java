package is.ru.CodeGeneration;

import is.ru.CodeGeneration.Enums.TokenCode;
import is.ru.CodeGeneration.Enums.DataType;
import is.ru.CodeGeneration.Enums.OpType;

public class Token {
    private TokenCode m_tc;
    private DataType m_dt;
    private OpType m_ot;
    private SymbolTableEntry m_ste;

    private int m_lineNum;
    private int m_colNum;

    public Token(TokenCode tc) {
        this(tc, DataType.NONE, OpType.NONE);
    }

    public Token(TokenCode tc, DataType dt, OpType ot) {
        this(tc, dt, ot, null);
    }

    public Token(TokenCode tc, DataType dt, OpType ot, SymbolTableEntry ste) {
        m_tc = tc;
        m_dt = dt;
        m_ot = ot;
        m_ste = ste;
    }

    public static Token createRaw(TokenCode keywordTokenCode) {
        return new Token(keywordTokenCode, DataType.NONE, OpType.NONE);
    }

    public static Token createOp(TokenCode opTokenCode, OpType opType) {
        return new Token(opTokenCode, DataType.OP, opType);
    }

    public static Token createId(SymbolTableEntry symTabEntry) {
        return new Token(TokenCode.IDENTIFIER, DataType.ID, OpType.NONE, symTabEntry);
    }

    public static Token createInt(SymbolTableEntry symTabEntry) {
        return new Token(TokenCode.NUMBER, DataType.INT, OpType.NONE, symTabEntry);
    }

    public static Token createReal(SymbolTableEntry symTabEntry) {
        return new Token(TokenCode.NUMBER, DataType.REAL, OpType.NONE, symTabEntry);
    }

    public static Token createRelOp(String lexeme) {
        OpType opType = OpType.NONE;
        if (lexeme.equals("=="))
            opType = OpType.EQUAL;
        else if (lexeme.equals("!="))
            opType = OpType.NOT_EQUAL;
        else if (lexeme.equals("<"))
            opType = OpType.LT;
        else if (lexeme.equals(">"))
            opType = OpType.GT;
        else if (lexeme.equals("<="))
            opType = OpType.LTE;
        else if (lexeme.equals("=="))
            opType = OpType.GTE;
        return Token.createOp(TokenCode.RELOP, opType);
    }

    public static Token createMulOp(String lexeme) {
        OpType opType = OpType.NONE;
        if (lexeme.equals("*"))
            opType = OpType.MULT;
        else if (lexeme.equals("/"))
            opType = OpType.DIV;
        else if (lexeme.equals("%"))
            opType = OpType.MOD;
        else if (lexeme.equals("&&"))
            opType = OpType.AND;
        return Token.createOp(TokenCode.MULOP, opType);
    }


    public static Token createAddOp(String lexeme) {
        OpType opType = OpType.NONE;
        if (lexeme.equals("+"))
            opType = OpType.PLUS;
        else if (lexeme.equals("-"))
            opType = OpType.MINUS;
        else if (lexeme.equals("||"))
            opType = OpType.OR;
        return Token.createOp(TokenCode.ADDOP, opType);
    }

    public void setLineColumn(int line, int column) {
        m_lineNum = line;
        m_colNum = column;
    }

    public int getLineNum() {
        return m_lineNum + 1;
    }

    public int getColumnNum() {
        return m_colNum + 1;
    }

    public TokenCode getTokenCode() {
        return m_tc;
    }

    public DataType getDataType() {
        return m_dt;
    }

    public OpType getOpType() {
        return m_ot;
    }

    public SymbolTableEntry getSymTabEntry() {
        return m_ste;
    }
}