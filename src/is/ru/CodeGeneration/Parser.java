package is.ru.CodeGeneration;

import com.sun.org.apache.bcel.internal.classfile.Code;
import is.ru.CodeGeneration.Enums.TacCode;
import is.ru.CodeGeneration.Enums.TokenCode;
import is.ru.CodeGeneration.Enums.NonT;
import is.ru.CodeGeneration.Enums.OpType;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;

import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    private Lexer m_lexer;
    private Token m_current;
    private Token m_prev;
    private CodeGenerator m_genCode;
    private ArrayList<SymbolTableEntry> m_parameterList;
    private int tempCounter;
    private int labelCounter;

    private ErrorHandler m_errorHandler;

    public Parser(Lexer lexer, String sourceFile) {
        m_errorHandler = new ErrorHandler(lexer, sourceFile);
        m_lexer = lexer;
        SymbolTable.insert("0");
        SymbolTable.insert("1");
        m_genCode = new CodeGenerator();
        tempCounter = 0;
        readNextToken();
    }

    protected SymbolTableEntry newTemp() {
        tempCounter++;
        String temp = "t" + tempCounter;
        SymbolTableEntry tempEntry = SymbolTable.insert(temp);
        CodeGenerator.generate(TacCode.VAR, null, null, tempEntry);
        return tempEntry;
    }

    protected SymbolTableEntry newLabel() {
        labelCounter++;
        String tmp = "lab" + labelCounter;
        SymbolTableEntry label = SymbolTable.insert(tmp);
        CodeGenerator.generate(TacCode.LABEL, null, null, label);
        return label;
    }
    /*
      Reads the next token.
      If the compiler is in error recovery we do not actually read a new token, we just pretend we do. We will get match failures which the ErrorHandler will supress. When we leave the procedure with the offending non-terminal, the ErrorHandler will go out of recovery mode and start reading tokens again.
    */
    protected void readNextToken() {
        try {
            // If the Error handler is in recovery mode, we don't read new tokens!
            // We simply use current tokens until the Error handler exits the recovery mode
            if (!m_errorHandler.inRecovery()) {
                m_prev = m_current;
                m_current = m_lexer.yylex();
                trace("++ Next token read: " + m_current.getTokenCode());
                if (MyMain.TRACE)
                    if (m_prev != null && m_prev.getLineNum() != m_current.getLineNum())
                        System.out.println("Line " + m_current.getLineNum());
            }
            else
                trace("++ Next token skipped because of recovery: Still: " + m_current.getTokenCode());
            // System.out.println(m_current.getTokenCode() + String.valueOf(m_current.getLineNum()) + ", col: " + String.valueOf(m_current.getColumnNum()));
        }
        catch(IOException e) {
            System.out.println("IOException reading next token");
            System.exit(1);
        }
    }

    /* Returns the next token of the input, without actually reading it */
    protected Token lookahead() {
        return m_current;
    }

    /* Returns true if the lookahead token has the given tokencode */
    protected boolean lookaheadIs(TokenCode tokenCode) {
        return m_current.getTokenCode() == tokenCode;
    }

    /* Returns true if the lookahed token is included in the given array of token codes */
    protected boolean lookaheadIn(TokenCode[] tokenCodes) {
        for(int n=0;n<tokenCodes.length;n++)
            if (tokenCodes[n] == m_current.getTokenCode())
                return true;
        return false;
    }

    /* Returns true if the lookahed token is in the FIRST of EXPRESSION.
       Need to specially check if the token is ADDOP to make sure the token is +/-
       (by checking the OpType of the token)
     */
    protected boolean lookaheadIsFirstOfExpression() {
        if (!lookaheadIn(NonT.firstOf(NonT.EXPRESSION)))
            return false;
        if (lookaheadIs(TokenCode.ADDOP) && lookahead().getOpType() != OpType.PLUS && lookahead().getOpType() != OpType.MINUS)
            return false;
        else
            return true;
    }

    /*
    Return true if the lookahed is the first of sign (actually if the lexeme for the token was '+' or '-')
    */
    protected boolean lookaheadIsFirstOfSign() {
        return (lookaheadIs(TokenCode.ADDOP) && (lookahead().getOpType() == OpType.PLUS || lookahead().getOpType() == OpType.MINUS));
    }

    /*
    Match the the token and read next token if match is successful.

    If the match is unsuccessfull we let the ErrorHandler report the error and supply us with the next token to use. This next token will then not be used until we leave the parsing method where the mismatch occured.

    If the ErrorHandler is in the recovery state, it will suppress the error (not report it).
    */
    protected void match(TokenCode tokenCode) {
        if (m_current.getTokenCode() != tokenCode)
        {
            Token[] tokens = m_errorHandler.tokenMismatch(tokenCode, m_current, m_prev);
            m_current = tokens[0];
            m_prev = tokens[1];
            trace("  failed match for " + tokenCode + ". current: " + m_current.getTokenCode() + ", prev: " + m_prev.getTokenCode());
        }
        else {
            trace("  Matched " + tokenCode);

            readNextToken();
        }
    }

    /*
    Called when none the next token is none of the possible tokens for some given part of the non-terminal.
    Behaviour is the same as match except that we have no specific token to match against.
    */
    protected void noMatch() {
        Token[] tokens = m_errorHandler.noMatch(m_current, m_prev);
        m_current = tokens[0];
        m_prev = tokens[1];
    }


    // *** Start of nonTerminal functions ***

    protected void program() {
        m_errorHandler.startNonT(NonT.PROGRAM);
        match(TokenCode.CLASS);
        match(TokenCode.IDENTIFIER);
        match(TokenCode.LBRACE);
        variableDeclarations();
        methodDeclarations();
        match(TokenCode.RBRACE);
        m_errorHandler.stopNonT();
    }

    protected void variableDeclarations() {
        m_errorHandler.startNonT(NonT.VARIABLE_DECLARATIONS);
        if (lookaheadIn(NonT.firstOf(NonT.TYPE))) {
            type();
            variableList();
            match(TokenCode.SEMICOLON);
            variableDeclarations();
        }
        m_errorHandler.stopNonT();
    }

    protected void type() {
        m_errorHandler.startNonT(NonT.TYPE);
        if (lookaheadIs(TokenCode.INT))
            match(TokenCode.INT);
        else if (lookaheadIs(TokenCode.REAL))
            match(TokenCode.REAL);
        else // TODO: Add error context, i.e. type
            noMatch();
        m_errorHandler.stopNonT();
    }

    protected void variableList() {
        m_errorHandler.startNonT(NonT.VARIABLE_LIST);
        variable();
        variableList2();
        m_errorHandler.stopNonT();
    }

    protected void variableList2() {
        m_errorHandler.startNonT(NonT.VARIABLE_LIST_2);
        if (lookaheadIs(TokenCode.COMMA)) {
            match(TokenCode.COMMA);
            variable();
            variableList2();
        }
        m_errorHandler.stopNonT();
    }

    protected void variable() {
        m_errorHandler.startNonT(NonT.VARIABLE);
        match(TokenCode.IDENTIFIER);
        m_genCode.generate(TacCode.VAR, null, null, m_prev.getSymTabEntry());
        if (lookaheadIs(TokenCode.LBRACKET)) {
            match(TokenCode.LBRACKET);
            match(TokenCode.NUMBER);
            match(TokenCode.RBRACKET);
        }
        m_errorHandler.stopNonT();
    }

    protected void methodDeclarations() {
        String gotoMain1 = "main";
        SymbolTableEntry gotoMain = SymbolTable.insert(gotoMain1);
        m_genCode.generate(TacCode.GOTO, null, null, gotoMain);
        m_errorHandler.startNonT(NonT.METHOD_DECLARATIONS);
        methodDeclaration();
        moreMethodDeclarations();
        m_errorHandler.stopNonT();
    }

    protected void moreMethodDeclarations() {
        m_errorHandler.startNonT(NonT.MORE_METHOD_DECLARATIONS);
        if (lookaheadIn(NonT.firstOf(NonT.METHOD_DECLARATION))) {
            methodDeclaration();
            moreMethodDeclarations();
        }
        m_errorHandler.stopNonT();
    }

    protected void methodDeclaration() {
        m_errorHandler.startNonT(NonT.METHOD_DECLARATION);
        match(TokenCode.STATIC);
        methodReturnType();
        match(TokenCode.IDENTIFIER);
        m_genCode.generate(TacCode.LABEL, null, null, m_prev.getSymTabEntry());
        match(TokenCode.LPAREN);
        //create list
        m_parameterList = new ArrayList<SymbolTableEntry>();
        parameters();
        m_genCode.addFormalParameters(m_parameterList);
        match(TokenCode.RPAREN);
        match(TokenCode.LBRACE);
        variableDeclarations();
        statementList();
        match(TokenCode.RBRACE);
        m_errorHandler.stopNonT();
    }

    protected void methodReturnType() {
        m_errorHandler.startNonT(NonT.METHOD_RETURN_TYPE);
        if (lookaheadIs(TokenCode.VOID))
            match(TokenCode.VOID);
        else
            type();
        m_errorHandler.stopNonT();
    }

    protected void parameters() {
        m_errorHandler.startNonT(NonT.PARAMETERS);
        if (lookaheadIn(NonT.firstOf(NonT.PARAMETER_LIST))) {
            parameterList();
        }
        m_errorHandler.stopNonT();
    }

    protected void parameterList() {
        m_errorHandler.startNonT(NonT.PARAMETER_LIST);
        type();
        match(TokenCode.IDENTIFIER);
        m_parameterList.add(m_prev.getSymTabEntry());
        parameterList2();
        m_errorHandler.stopNonT();
    }

    protected void parameterList2() {
        m_errorHandler.startNonT(NonT.PARAMETER_LIST2);
        if (lookaheadIs(TokenCode.COMMA) && !m_errorHandler.inRecovery()) {
            match(TokenCode.COMMA);
            type();
            match(TokenCode.IDENTIFIER);
            m_parameterList.add(m_prev.getSymTabEntry());
            parameterList2();
        }
        m_errorHandler.stopNonT();
    }

    protected void statementList() {
        m_errorHandler.startNonT(NonT.STATEMENT_LIST);
        if (lookaheadIn(NonT.firstOf(NonT.STATEMENT))  && !m_errorHandler.inRecovery()) {
            statement();
            statementList();
        }
        m_errorHandler.stopNonT();
    }

    protected void idStartingStatement() {
        m_errorHandler.startNonT(NonT.ID_STARTING_STATEMENT);
        match(TokenCode.IDENTIFIER);

        restOfIdStartingStatement();
        match(TokenCode.SEMICOLON);
        m_errorHandler.stopNonT();
    }

    protected void restOfIdStartingStatement() {
        m_errorHandler.startNonT(NonT.REST_OF_ID_STARTING_STATEMENT);
        if (lookaheadIs(TokenCode.LPAREN)) {
            match(TokenCode.LPAREN);
            expressionList();
            match(TokenCode.RPAREN);
        }
        else if (lookaheadIs(TokenCode.INCDECOP)) {
            match(TokenCode.INCDECOP);
           // SymbolTableEntry temp1 = newTemp();
           // SymbolTableEntry temp2 = newTemp();
           // m_genCode.generate(TacCode.ASSIGN,temp2,null,);
           // m_genCode.generate(TacCode.ADD, m_prev.getSymTabEntry(), temp2, temp1);
        }
        else if (lookaheadIs(TokenCode.ASSIGNOP)) {
            match(TokenCode.ASSIGNOP);
           // SymbolTableEntry tx = newTemp();
           // m_genCode.generate(TacCode.VAR,tx,null,m_prev.getSymTabEntry());
            expression();
        }
        else if (lookaheadIs(TokenCode.LBRACKET)) {
            match(TokenCode.LBRACKET);
            expression();
            match(TokenCode.RBRACKET);
            match(TokenCode.ASSIGNOP);
            expression();
        }
        else // TODO: Add error context, i.e. idStartingStatement
            noMatch();
        m_errorHandler.stopNonT();
    }

    protected void statement() {
        boolean noMatch = false;
        m_errorHandler.startNonT(NonT.STATEMENT);
        if (lookaheadIs(TokenCode.IDENTIFIER))
        {
            trace("idStartingStmt");
            idStartingStatement();
        }
        else if (lookaheadIs(TokenCode.IF)) {
            trace("if");
            match(TokenCode.IF);
            newLabel();
            match(TokenCode.LPAREN);
            expression();
            match(TokenCode.RPAREN);
            statementBlock();
            optionalElse();
        }
        else if (lookaheadIs(TokenCode.FOR)) {
            trace("for");
            match(TokenCode.FOR);
            newLabel();
            match(TokenCode.LPAREN);
            variableLoc();
            match(TokenCode.ASSIGNOP);
            expression();
            match(TokenCode.SEMICOLON);
            expression();
            match(TokenCode.SEMICOLON);
            variableLoc();
            match(TokenCode.INCDECOP);
            match(TokenCode.RPAREN);
            statementBlock();
        }
        else if (lookaheadIs(TokenCode.RETURN)) {
            trace("return");
            match(TokenCode.RETURN);
            optionalExpression();
            match(TokenCode.SEMICOLON);
        }
        else if (lookaheadIs(TokenCode.BREAK)) {
            trace("break");
            match(TokenCode.BREAK);
            match(TokenCode.SEMICOLON);
        }
        else if (lookaheadIs(TokenCode.CONTINUE)) {
            trace("continue");
            match(TokenCode.CONTINUE);
            match(TokenCode.SEMICOLON);
        }
        else if (lookaheadIs(TokenCode.RBRACE)) {
            trace("block");
            statementBlock();
        }
        else {// TODO: Add error context, i.e. statement
            trace("noMatch");
            noMatch = true;
            m_errorHandler.stopNonT();
            noMatch();
        }
        if (!noMatch)
            m_errorHandler.stopNonT();
    }

    protected void optionalExpression() {
        m_errorHandler.startNonT(NonT.OPTIONAL_EXPRESSION);
        if (lookaheadIsFirstOfExpression()) {
            expression();
        }
        m_errorHandler.stopNonT();
    }

    protected void statementBlock() {
        m_errorHandler.startNonT(NonT.STATEMENT_BLOCK);
        match(TokenCode.LBRACE);
        statementList();
        match(TokenCode.RBRACE);
        m_errorHandler.stopNonT();
    }

    protected void optionalElse() {
        m_errorHandler.startNonT(NonT.OPTIONAL_ELSE);
        if (lookaheadIs(TokenCode.ELSE)) {
            match(TokenCode.ELSE);
            newLabel();
            statementBlock();
        }
        m_errorHandler.stopNonT();
    }

    protected void expressionList() {
        m_errorHandler.startNonT(NonT.EXPRESSION_LIST);
        if (lookaheadIsFirstOfExpression()) {
            expression();
            moreExpressions();
        }
        m_errorHandler.stopNonT();
    }

    protected void moreExpressions() {
        m_errorHandler.startNonT(NonT.MORE_EXPRESSIONS);
        if (lookaheadIs(TokenCode.COMMA) && !m_errorHandler.inRecovery()) {
            match(TokenCode.COMMA);
            expression();
            moreExpressions();
        }
        m_errorHandler.stopNonT();
    }

    protected SymbolTableEntry expression() {
        SymbolTableEntry ret = null;
        m_errorHandler.startNonT(NonT.EXPRESSION);
        SymbolTableEntry simpleExpression_ste = simpleExpression();
        ret = expression2();
        m_errorHandler.stopNonT();
        return ret;
    }

    protected SymbolTableEntry expression2() {
        SymbolTableEntry ret= null;
        m_errorHandler.startNonT(NonT.EXPRESSION2);
        /* create 1 tmp og 2 labels and then check which relop it is*/
        if (lookaheadIs(TokenCode.RELOP)) {
            SymbolTableEntry t1 = newTemp();
            m_genCode.generate(TacCode.VAR, null, null, t1);
            SymbolTableEntry l1 = newLabel();
            SymbolTableEntry l2 = newLabel();
            TacCode tc;
            OpType currOp = lookahead().getOpType();
            if(currOp == OpType.EQUAL)
            {
                tc = TacCode.EQ;
            }
            else if(currOp == OpType.NOT_EQUAL)
            {
                tc = TacCode.NE;
            }
            else if(currOp == OpType.LT)
            {
                tc = TacCode.LT;
            }
            else if(currOp == OpType.GT)
            {
                tc = TacCode.GT;
            }
            else if(currOp == OpType.LTE)
            {
                tc = TacCode.LE;

            }
            else //if(currOp == OpType.GTE)
            {
                tc = TacCode.GE;
            }
            SymbolTableEntry prevSTE = m_prev.getSymTabEntry();
            match(TokenCode.RELOP);
            ret= simpleExpression();
            //generate with tc and currSTE
            m_genCode.generate(tc, prevSTE, ret, l1);
            m_genCode.generate(TacCode.ASSIGN, SymbolTable.lookup("0"), null, t1);
            m_genCode.generate(TacCode.GOTO, null, null, l2);
        }
        m_errorHandler.stopNonT();
        return ret;
    }
//check sign, let simpleExpression receive symboltableentry as a parameter
    protected SymbolTableEntry simpleExpression() {
        SymbolTableEntry ret = null;
        m_errorHandler.startNonT(NonT.SIMPLE_EXPRESSION);
        if (lookaheadIn(NonT.firstOf(NonT.SIGN))) {
            sign();
            if(m_prev.getOpType() == OpType.MINUS)
            {
                SymbolTableEntry termSTE = term();
                SymbolTableEntry t = newTemp();
                m_genCode.generate(TacCode.UMINUS, termSTE, null, t);
            }
        }
        else
        {
            SymbolTableEntry termSTE = term();
        }

        ret = simpleExpression2();
        m_errorHandler.stopNonT();
        return ret;
    }

    protected SymbolTableEntry simpleExpression2() {
        SymbolTableEntry ret = null;
        m_errorHandler.startNonT(NonT.SIMPLE_EXPRESSION2);
        if (lookaheadIs(TokenCode.ADDOP)) {

            SymbolTableEntry t = newTemp();
            m_genCode.generate(TacCode.VAR, null, null, t);

            TacCode tc;
            if(m_prev.getOpType() == OpType.MINUS) {
                tc = TacCode.SUB;
            }
            else if(m_prev.getOpType() == OpType.PLUS) {
                tc = TacCode.ADD;
            }
            else {//if(m_prev.getOpType() == OpType.OR) {
                tc = TacCode.OR;
            }

            SymbolTableEntry prev = m_prev.getSymTabEntry();
            match(TokenCode.ADDOP);
            SymbolTableEntry termSTE2 = term();

            m_genCode.generate(tc, prev, termSTE2, t);

            ret = simpleExpression2();
        }
        m_errorHandler.stopNonT();
        return ret;
    }

    protected SymbolTableEntry term() {
        SymbolTableEntry ret= null;
        m_errorHandler.startNonT(NonT.TERM);
        SymbolTableEntry ste = factor();
        ret = term2();
        m_errorHandler.stopNonT();
        return ret;
    }

    protected SymbolTableEntry term2() {
        SymbolTableEntry ret = null;
        m_errorHandler.startNonT(NonT.TERM2);
        if (lookaheadIs(TokenCode.MULOP)) {

            SymbolTableEntry t1 = newTemp();
            m_genCode.generate(TacCode.VAR, null, null, t1);

            TacCode tc;
            OpType oc = lookahead().getOpType();
            if (oc == OpType.MULT)
            {
                tc = TacCode.MULT;
            }
            else if (oc == OpType.DIV)
            {
                tc = TacCode.DIVIDE;
            }
            else if (oc == OpType.MOD)
            {
                tc = TacCode.MOD;
            }
            else// if (oc == OpType.AND)
            {
                tc = TacCode.AND;
            }

            SymbolTableEntry prevSTE = m_prev.getSymTabEntry();
            match(TokenCode.MULOP);
            SymbolTableEntry ste = factor();

            m_genCode.generate(tc, prevSTE, ste, t1);
            // Was missing!
            ret = term2();
        }
        m_errorHandler.stopNonT();
        return ret;
    }

    protected SymbolTableEntry idStartingFactor() {
        SymbolTableEntry t = null;
        m_errorHandler.startNonT(NonT.ID_STARTING_FACTOR);
        match(TokenCode.IDENTIFIER);
        t = restOfIdStartingFactor();
        m_errorHandler.stopNonT();
        return t;
    }
//skoda betur
    protected SymbolTableEntry restOfIdStartingFactor() {
        SymbolTableEntry t = null;
        m_errorHandler.startNonT(NonT.REST_OF_ID_STARTING_FACTOR);
        if (lookaheadIs(TokenCode.LPAREN)) {

            match(TokenCode.LPAREN);
            expressionList();

            match(TokenCode.RPAREN);
        }
        else if (lookaheadIs(TokenCode.LBRACKET)) {
            match(TokenCode.LBRACKET);
            t = expression();
            match(TokenCode.RBRACKET);
        }
        m_errorHandler.stopNonT();
        return t;
    }

    protected SymbolTableEntry factor() {
        SymbolTableEntry ret = null;
        m_errorHandler.startNonT(NonT.FACTOR);
        if (lookaheadIs(TokenCode.IDENTIFIER))
            ret = idStartingFactor();
        else if (lookaheadIs(TokenCode.NUMBER)) {
            match(TokenCode.NUMBER);
        }
        else if (lookaheadIs(TokenCode.LPAREN)) {
            match(TokenCode.LPAREN);
            ret = expression();
            match(TokenCode.RPAREN);
        }
        else if (lookaheadIs(TokenCode.NOT)) {
            match(TokenCode.NOT);
            ret = factor();
        }
        else // TODO: Add error context, i.e. factor
            noMatch();
        m_errorHandler.stopNonT();
        return ret;
    }

    protected void variableLoc() {
        SymbolTableEntry t = null;
        m_errorHandler.startNonT(NonT.VARIABLE_LOC);
        match(TokenCode.IDENTIFIER);
        variableLocRest();
        m_errorHandler.stopNonT();
    }

    protected void variableLocRest() {
        m_errorHandler.startNonT(NonT.VARIABLE_LOC_REST);
        if (lookaheadIs(TokenCode.LBRACKET)) {
            match(TokenCode.LBRACKET);
            expression();
            match(TokenCode.RBRACKET);
        }
        m_errorHandler.stopNonT();
    }

    protected void sign() {
        m_errorHandler.startNonT(NonT.SIGN);
        if (lookaheadIsFirstOfSign())
            match(TokenCode.ADDOP);
        else // TODO: Add error context, i.e. sign
            noMatch();
        m_errorHandler.stopNonT();
    }

    protected void trace(String msg) {
        if (MyMain.TRACE)
            System.out.println(msg);
    }
}