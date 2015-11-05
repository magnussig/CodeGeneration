package is.ru.CodeGeneration.Enums;
public enum TokenCode {
    IDENTIFIER, NUMBER, INCDECOP, RELOP, MULOP, ADDOP,
    ASSIGNOP,
    CLASS, VOID, IF, ELSE, FOR, RETURN, BREAK, CONTINUE,
    LBRACE, RBRACE, LBRACKET, RBRACKET, LPAREN, RPAREN,
    SEMICOLON, COMMA, NOT, INT, REAL,
    EOF, ERR_ILL_CHAR, ERR_LONG_ID,
    STATIC, NONE;

    public static String getReportableString(TokenCode tokenCode) {
        switch(tokenCode) {
            case IDENTIFIER:
                return "identifier";
            case NUMBER:
                return "number";
            case INCDECOP:
                return "'++'' or '--'";
            case RELOP:
                return "relational operator";
            case MULOP:
                return "multiplication operator";
            case ADDOP:
                return "addition operator";
            case ASSIGNOP:
                return "'='";
            case LBRACE:
                return "'{'";
            case RBRACE:
                return "'}'";
            case LBRACKET:
                return "'['";
            case RBRACKET:
                return "']'";
            case LPAREN:
                return "'('";
            case RPAREN:
                return "')'";
            case SEMICOLON:
                return "';'";
            case COMMA:
                return "','";
            case NOT:
                return "'!'";
            case EOF:
                return "end of file";
            default:
                return tokenCode.toString().toLowerCase();
        }
    }

}