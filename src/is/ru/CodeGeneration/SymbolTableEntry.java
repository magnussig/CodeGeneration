package is.ru.CodeGeneration;

public class SymbolTableEntry {
    private String m_lexeme;

    public SymbolTableEntry(String lexeme) {
        m_lexeme = lexeme;
    }

    public String getLexeme() {
        return m_lexeme;
    }
}