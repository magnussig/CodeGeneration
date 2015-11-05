package is.ru.CodeGeneration.Enums;
import is.ru.CodeGeneration.Enums.TokenCode;

public enum NonT {
    PROGRAM, VARIABLE_DECLARATIONS, TYPE, VARIABLE_LIST, VARIABLE_LIST_2, VARIABLE, VARIABLE2, METHOD_DECLARATIONS, MORE_METHOD_DECLARATIONS, METHOD_DECLARATION, METHOD_RETURN_TYPE, PARAMETERS, PARAMETER_LIST, PARAMETER_LIST2, STATEMENT_LIST, ID_STARTING_STATEMENT, REST_OF_ID_STARTING_STATEMENT, STATEMENT, OPTIONAL_EXPRESSION,STATEMENT_BLOCK,OPTIONAL_ELSE,EXPRESSION_LIST,MORE_EXPRESSIONS,EXPRESSION, EXPRESSION2, SIMPLE_EXPRESSION, SIMPLE_EXPRESSION2,TERM,TERM2,ID_STARTING_FACTOR,REST_OF_ID_STARTING_FACTOR,FACTOR,VARIABLE_LOC,VARIABLE_LOC_REST,SIGN;

    public static TokenCode [] firstOf(NonT nonT) {
        switch(nonT) {
            case TYPE:
                return new TokenCode [] { TokenCode.INT, TokenCode.REAL };
            case METHOD_DECLARATION:
                return new TokenCode [] { TokenCode.STATIC };
            case PARAMETER_LIST:
                return new TokenCode [] { TokenCode.INT, TokenCode.REAL };
            case STATEMENT:
                return new TokenCode [] { TokenCode.IDENTIFIER, TokenCode.IF, TokenCode.FOR, TokenCode.RETURN, TokenCode.BREAK, TokenCode.CONTINUE, TokenCode.LBRACE };
            case EXPRESSION:
                return new TokenCode [] { TokenCode.IDENTIFIER, TokenCode.NUMBER, TokenCode.LPAREN, TokenCode.NOT, TokenCode.ADDOP };
            case SIGN:
                return new TokenCode [] { TokenCode.ADDOP };
            default:
                throw new RuntimeException("First of not (yet) defined for " + nonT);
        }
    }

    public static String getMainDescr(NonT nonT) {
        switch(nonT) {
            case PROGRAM:
                return "program";
            case VARIABLE_DECLARATIONS:
                return "variable declaration";
            case METHOD_DECLARATION:
                return "method declaration";
            case STATEMENT_LIST:
                return "statement";
            case EXPRESSION:
                return "expression";
            default:
                return null;
        }
    }

    public static TokenCode [] followTokens(NonT nonT) {
        switch(nonT) {
            case PROGRAM:
                return new TokenCode [] { TokenCode.EOF };
            case VARIABLE_DECLARATIONS:
                return new TokenCode [] { TokenCode.IDENTIFIER, TokenCode.IF, TokenCode.FOR, TokenCode.RETURN, TokenCode.BREAK, TokenCode.CONTINUE, TokenCode.LBRACE, TokenCode.STATIC };
            case TYPE:
                return new TokenCode [] { TokenCode.IDENTIFIER };
            case VARIABLE_LIST:
                return new TokenCode [] { TokenCode.SEMICOLON };
            case VARIABLE_LIST_2:
                return new TokenCode [] { TokenCode.SEMICOLON };
            case VARIABLE:
                return new TokenCode [] { TokenCode.COMMA, TokenCode.SEMICOLON };
            case VARIABLE2:
                return new TokenCode [] { TokenCode.COMMA, TokenCode.SEMICOLON };
            case METHOD_DECLARATIONS:
                return new TokenCode [] { TokenCode.RBRACE };
            case MORE_METHOD_DECLARATIONS:
                return new TokenCode [] { TokenCode.RBRACE };
            case METHOD_DECLARATION:
                return new TokenCode [] { TokenCode.RBRACE, TokenCode.STATIC };
            case METHOD_RETURN_TYPE:
                return new TokenCode [] { TokenCode.IDENTIFIER };
            case PARAMETERS:
                return new TokenCode [] { TokenCode.RPAREN };
            case PARAMETER_LIST:
                return new TokenCode [] { TokenCode.RPAREN };
            case PARAMETER_LIST2:
                return new TokenCode [] { TokenCode.RPAREN };
            case STATEMENT_LIST:
                return new TokenCode [] { TokenCode.RBRACE };
            case ID_STARTING_STATEMENT:
                return new TokenCode [] { TokenCode.IF, TokenCode.FOR, TokenCode.RETURN, TokenCode.BREAK, TokenCode.CONTINUE, TokenCode.IDENTIFIER, TokenCode.RBRACE, TokenCode.LBRACE };
            case REST_OF_ID_STARTING_STATEMENT:
                return new TokenCode [] { TokenCode.SEMICOLON };
            case STATEMENT:
                return new TokenCode [] { TokenCode.IF, TokenCode.FOR, TokenCode.RETURN, TokenCode.BREAK, TokenCode.CONTINUE, TokenCode.IDENTIFIER, TokenCode.RBRACE, TokenCode.LBRACE };
            case OPTIONAL_EXPRESSION:
                return new TokenCode [] { TokenCode.SEMICOLON };
            case STATEMENT_BLOCK:
                return new TokenCode [] { TokenCode.ELSE, TokenCode.IF, TokenCode.FOR, TokenCode.RETURN, TokenCode.BREAK, TokenCode.CONTINUE, TokenCode.IDENTIFIER, TokenCode.RBRACE, TokenCode.LBRACE };
            case OPTIONAL_ELSE:
                return new TokenCode [] { TokenCode.IF, TokenCode.FOR, TokenCode.RETURN, TokenCode.BREAK, TokenCode.CONTINUE, TokenCode.IDENTIFIER, TokenCode.RBRACE, TokenCode.LBRACE };
            case EXPRESSION_LIST:
                return new TokenCode [] { TokenCode.RPAREN };
            case MORE_EXPRESSIONS:
                return new TokenCode [] { TokenCode.RPAREN };
            case EXPRESSION:
                return new TokenCode [] { TokenCode.COMMA, TokenCode.SEMICOLON, TokenCode.RBRACKET, TokenCode.RPAREN };
            case EXPRESSION2:
                return new TokenCode [] { TokenCode.COMMA, TokenCode.SEMICOLON, TokenCode.RBRACKET, TokenCode.RPAREN };
            case SIMPLE_EXPRESSION:
                return new TokenCode [] { TokenCode.RELOP, TokenCode.COMMA, TokenCode.SEMICOLON, TokenCode.RBRACKET, TokenCode.RPAREN };
            case SIMPLE_EXPRESSION2:
                return new TokenCode [] { TokenCode.RELOP, TokenCode.COMMA, TokenCode.SEMICOLON, TokenCode.RBRACKET, TokenCode.RPAREN };
            case TERM:
                return new TokenCode [] { TokenCode.ADDOP, TokenCode.RELOP, TokenCode.COMMA, TokenCode.SEMICOLON, TokenCode.RBRACKET, TokenCode.RPAREN };
            case TERM2:
                return new TokenCode [] { TokenCode.ADDOP, TokenCode.RELOP, TokenCode.COMMA, TokenCode.SEMICOLON, TokenCode.RBRACKET, TokenCode.RPAREN };
            case ID_STARTING_FACTOR:
                return new TokenCode [] { TokenCode.MULOP, TokenCode.ADDOP, TokenCode.RELOP, TokenCode.COMMA, TokenCode.SEMICOLON, TokenCode.RBRACKET, TokenCode.RPAREN };
            case REST_OF_ID_STARTING_FACTOR:
                return new TokenCode [] { TokenCode.MULOP, TokenCode.ADDOP, TokenCode.RELOP, TokenCode.COMMA, TokenCode.SEMICOLON, TokenCode.RBRACKET, TokenCode.RPAREN };
            case FACTOR:
                return new TokenCode [] { TokenCode.MULOP, TokenCode.ADDOP, TokenCode.RELOP, TokenCode.COMMA, TokenCode.SEMICOLON, TokenCode.RBRACKET, TokenCode.RPAREN };
            case VARIABLE_LOC:
                return new TokenCode [] { TokenCode.ASSIGNOP, TokenCode.INCDECOP };
            case VARIABLE_LOC_REST:
                return new TokenCode [] { TokenCode.ASSIGNOP, TokenCode.INCDECOP };
            case SIGN:
                return new TokenCode [] { TokenCode.IDENTIFIER, TokenCode.NUMBER, TokenCode.LPAREN, TokenCode.NOT };

            default:
                return null;
        }
    }
}