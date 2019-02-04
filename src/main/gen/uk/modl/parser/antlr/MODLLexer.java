package uk.modl.parser.antlr;

// Generated from /Users/alex/code/NUM/MODL/interpreter/grammar/MODLLexer.g4 by ANTLR 4.7
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MODLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, NULL=2, TRUE=3, FALSE=4, NEWLINE=5, COLON=6, EQUALS=7, SC=8, LBRAC=9, 
		RBRAC=10, LSBRAC=11, RSBRAC=12, NUMBER=13, COMMENT=14, STRING=15, HASH_PREFIX=16, 
		QUOTED=17, GRAVED=18, LCBRAC=19, CWS=20, QMARK=21, FSLASH=22, GTHAN=23, 
		LTHAN=24, ASTERISK=25, AMP=26, PIPE=27, EXCLAM=28, CCOMMENT=29, RCBRAC=30;
	public static final int
		CONDITIONAL=1;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE", "CONDITIONAL"
	};

	public static final String[] ruleNames = {
		"WS", "NULL", "TRUE", "FALSE", "NEWLINE", "COLON", "EQUALS", "SC", "LBRAC", 
		"RBRAC", "LSBRAC", "RSBRAC", "NUMBER", "INT", "EXP", "COMMENT", "INSIDE_COMMENT", 
		"STRING", "UNRESERVED", "RESERVED_CHARS", "ESCAPED", "UNICODE", "HEX", 
		"HASH_PREFIX", "QUOTED", "INSIDE_QUOTES", "GRAVED", "INSIDE_GRAVES", "LCBRAC", 
		"CWS", "CNULL", "CTRUE", "CFALSE", "CNEWLINE", "CCOLON", "CEQUALS", "CSC", 
		"CLBRAC", "CRBRAC", "CLSBRAC", "CRSBRAC", "CNUMBER", "QMARK", "FSLASH", 
		"GTHAN", "LTHAN", "ASTERISK", "AMP", "PIPE", "EXCLAM", "CLCBRAC", "CSTRING", 
		"CUNRESERVED", "CRESERVED_CHARS", "CESCAPED", "CCOMMENT", "CQUOTED", "CGRAVED", 
		"RCBRAC"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, "'{'", null, "'?'", "'/'", "'>'", 
		"'<'", "'*'", "'&'", "'|'", "'!'", null, "'}'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "WS", "NULL", "TRUE", "FALSE", "NEWLINE", "COLON", "EQUALS", "SC", 
		"LBRAC", "RBRAC", "LSBRAC", "RSBRAC", "NUMBER", "COMMENT", "STRING", "HASH_PREFIX", 
		"QUOTED", "GRAVED", "LCBRAC", "CWS", "QMARK", "FSLASH", "GTHAN", "LTHAN", 
		"ASTERISK", "AMP", "PIPE", "EXCLAM", "CCOMMENT", "RCBRAC"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MODLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MODLLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2 \u0202\b\1\b\1\4"+
		"\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n"+
		"\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\3\2"+
		"\6\2|\n\2\r\2\16\2}\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\5\3\u008d\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u0099\n\4"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5\u00a7\n\5\3\6\3\6"+
		"\3\6\5\6\u00ac\n\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3"+
		"\r\3\r\3\16\5\16\u00bd\n\16\3\16\3\16\3\16\6\16\u00c2\n\16\r\16\16\16"+
		"\u00c3\5\16\u00c6\n\16\3\16\5\16\u00c9\n\16\3\17\3\17\3\17\7\17\u00ce"+
		"\n\17\f\17\16\17\u00d1\13\17\5\17\u00d3\n\17\3\20\3\20\5\20\u00d7\n\20"+
		"\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\7\22\u00e3\n\22\f\22"+
		"\16\22\u00e6\13\22\3\23\3\23\5\23\u00ea\n\23\3\23\3\23\3\23\3\23\6\23"+
		"\u00f0\n\23\r\23\16\23\u00f1\3\23\5\23\u00f5\n\23\3\23\6\23\u00f8\n\23"+
		"\r\23\16\23\u00f9\3\23\5\23\u00fd\n\23\3\23\3\23\3\23\6\23\u0102\n\23"+
		"\r\23\16\23\u0103\7\23\u0106\n\23\f\23\16\23\u0109\13\23\3\24\3\24\3\25"+
		"\3\25\3\26\3\26\3\26\5\26\u0112\n\26\3\26\3\26\3\26\3\26\5\26\u0118\n"+
		"\26\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3"+
		"\32\5\32\u0128\n\32\3\32\3\32\3\33\7\33\u012d\n\33\f\33\16\33\u0130\13"+
		"\33\3\34\3\34\3\34\3\34\3\35\7\35\u0137\n\35\f\35\16\35\u013a\13\35\3"+
		"\36\3\36\3\36\3\36\3\37\6\37\u0141\n\37\r\37\16\37\u0142\3\37\3\37\3 "+
		"\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \5 \u0152\n \3 \3 \3!\3!\3!\3!\3!\3!\3!"+
		"\3!\3!\3!\5!\u0160\n!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3"+
		"\"\3\"\5\"\u0170\n\"\3\"\3\"\3#\3#\3#\5#\u0177\n#\3#\3#\3$\3$\3$\3$\3"+
		"%\3%\3%\3%\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3)\3)\3)\3)\3*\3*\3"+
		"*\3*\3+\5+\u0198\n+\3+\3+\3+\6+\u019d\n+\r+\16+\u019e\5+\u01a1\n+\3+\5"+
		"+\u01a4\n+\3+\3+\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62"+
		"\3\63\3\63\3\64\3\64\3\64\3\64\3\64\3\65\3\65\3\65\6\65\u01c0\n\65\r\65"+
		"\16\65\u01c1\3\65\6\65\u01c5\n\65\r\65\16\65\u01c6\3\65\3\65\3\65\6\65"+
		"\u01cc\n\65\r\65\16\65\u01cd\7\65\u01d0\n\65\f\65\16\65\u01d3\13\65\3"+
		"\65\3\65\3\66\3\66\3\67\3\67\38\38\38\58\u01de\n8\38\38\38\38\58\u01e4"+
		"\n8\39\39\39\39\39\39\39\3:\3:\3:\5:\u01f0\n:\3:\3:\3:\3:\3;\3;\3;\5;"+
		"\u01f9\n;\3;\3;\3;\3;\3<\3<\3<\3<\2\2=\4\3\6\4\b\5\n\6\f\7\16\b\20\t\22"+
		"\n\24\13\26\f\30\r\32\16\34\17\36\2 \2\"\20$\2&\21(\2*\2,\2.\2\60\2\62"+
		"\22\64\23\66\28\24:\2<\25>\26@\2B\2D\2F\2H\2J\2L\2N\2P\2R\2T\2V\2X\27"+
		"Z\30\\\31^\32`\33b\34d\35f\36h\2j\2l\2n\2p\2r\37t\2v\2x \4\2\3\16\4\2"+
		"\13\13\"\"\4\2\f\f\17\17\3\2\62;\3\2\63;\4\2GGgg\4\2--//\16\2\n\f\16\17"+
		"\"\"$%*+<=??]]__bb}}\177\177\t\2\61\61^^ddhhppttvv\5\2\62;CHch\3\2$$\3"+
		"\2bb\r\2\n\f\16\17\"%((*+\61\61<A]]__bb}\177\2\u022e\2\4\3\2\2\2\2\6\3"+
		"\2\2\2\2\b\3\2\2\2\2\n\3\2\2\2\2\f\3\2\2\2\2\16\3\2\2\2\2\20\3\2\2\2\2"+
		"\22\3\2\2\2\2\24\3\2\2\2\2\26\3\2\2\2\2\30\3\2\2\2\2\32\3\2\2\2\2\34\3"+
		"\2\2\2\2\"\3\2\2\2\2&\3\2\2\2\2\62\3\2\2\2\2\64\3\2\2\2\28\3\2\2\2\2<"+
		"\3\2\2\2\3>\3\2\2\2\3@\3\2\2\2\3B\3\2\2\2\3D\3\2\2\2\3F\3\2\2\2\3H\3\2"+
		"\2\2\3J\3\2\2\2\3L\3\2\2\2\3N\3\2\2\2\3P\3\2\2\2\3R\3\2\2\2\3T\3\2\2\2"+
		"\3V\3\2\2\2\3X\3\2\2\2\3Z\3\2\2\2\3\\\3\2\2\2\3^\3\2\2\2\3`\3\2\2\2\3"+
		"b\3\2\2\2\3d\3\2\2\2\3f\3\2\2\2\3h\3\2\2\2\3j\3\2\2\2\3r\3\2\2\2\3t\3"+
		"\2\2\2\3v\3\2\2\2\3x\3\2\2\2\4{\3\2\2\2\6\u008c\3\2\2\2\b\u0098\3\2\2"+
		"\2\n\u00a6\3\2\2\2\f\u00ab\3\2\2\2\16\u00ad\3\2\2\2\20\u00af\3\2\2\2\22"+
		"\u00b1\3\2\2\2\24\u00b3\3\2\2\2\26\u00b5\3\2\2\2\30\u00b7\3\2\2\2\32\u00b9"+
		"\3\2\2\2\34\u00bc\3\2\2\2\36\u00d2\3\2\2\2 \u00d4\3\2\2\2\"\u00da\3\2"+
		"\2\2$\u00e4\3\2\2\2&\u00e9\3\2\2\2(\u010a\3\2\2\2*\u010c\3\2\2\2,\u0117"+
		"\3\2\2\2.\u0119\3\2\2\2\60\u011f\3\2\2\2\62\u0121\3\2\2\2\64\u0124\3\2"+
		"\2\2\66\u012e\3\2\2\28\u0131\3\2\2\2:\u0138\3\2\2\2<\u013b\3\2\2\2>\u0140"+
		"\3\2\2\2@\u0151\3\2\2\2B\u015f\3\2\2\2D\u016f\3\2\2\2F\u0176\3\2\2\2H"+
		"\u017a\3\2\2\2J\u017e\3\2\2\2L\u0182\3\2\2\2N\u0186\3\2\2\2P\u018a\3\2"+
		"\2\2R\u018e\3\2\2\2T\u0192\3\2\2\2V\u0197\3\2\2\2X\u01a7\3\2\2\2Z\u01a9"+
		"\3\2\2\2\\\u01ab\3\2\2\2^\u01ad\3\2\2\2`\u01af\3\2\2\2b\u01b1\3\2\2\2"+
		"d\u01b3\3\2\2\2f\u01b5\3\2\2\2h\u01b7\3\2\2\2j\u01bf\3\2\2\2l\u01d6\3"+
		"\2\2\2n\u01d8\3\2\2\2p\u01e3\3\2\2\2r\u01e5\3\2\2\2t\u01ec\3\2\2\2v\u01f5"+
		"\3\2\2\2x\u01fe\3\2\2\2z|\t\2\2\2{z\3\2\2\2|}\3\2\2\2}{\3\2\2\2}~\3\2"+
		"\2\2~\177\3\2\2\2\177\u0080\b\2\2\2\u0080\5\3\2\2\2\u0081\u0082\7\62\2"+
		"\2\u0082\u0083\7\62\2\2\u0083\u008d\7\62\2\2\u0084\u0085\7p\2\2\u0085"+
		"\u0086\7w\2\2\u0086\u0087\7n\2\2\u0087\u008d\7n\2\2\u0088\u0089\7P\2\2"+
		"\u0089\u008a\7W\2\2\u008a\u008b\7N\2\2\u008b\u008d\7N\2\2\u008c\u0081"+
		"\3\2\2\2\u008c\u0084\3\2\2\2\u008c\u0088\3\2\2\2\u008d\7\3\2\2\2\u008e"+
		"\u008f\7\62\2\2\u008f\u0099\7\63\2\2\u0090\u0091\7v\2\2\u0091\u0092\7"+
		"t\2\2\u0092\u0093\7w\2\2\u0093\u0099\7g\2\2\u0094\u0095\7V\2\2\u0095\u0096"+
		"\7T\2\2\u0096\u0097\7W\2\2\u0097\u0099\7G\2\2\u0098\u008e\3\2\2\2\u0098"+
		"\u0090\3\2\2\2\u0098\u0094\3\2\2\2\u0099\t\3\2\2\2\u009a\u009b\7\62\2"+
		"\2\u009b\u00a7\7\62\2\2\u009c\u009d\7h\2\2\u009d\u009e\7c\2\2\u009e\u009f"+
		"\7n\2\2\u009f\u00a0\7u\2\2\u00a0\u00a7\7g\2\2\u00a1\u00a2\7H\2\2\u00a2"+
		"\u00a3\7C\2\2\u00a3\u00a4\7N\2\2\u00a4\u00a5\7U\2\2\u00a5\u00a7\7G\2\2"+
		"\u00a6\u009a\3\2\2\2\u00a6\u009c\3\2\2\2\u00a6\u00a1\3\2\2\2\u00a7\13"+
		"\3\2\2\2\u00a8\u00ac\t\3\2\2\u00a9\u00aa\7\17\2\2\u00aa\u00ac\7\f\2\2"+
		"\u00ab\u00a8\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ac\r\3\2\2\2\u00ad\u00ae\7"+
		"<\2\2\u00ae\17\3\2\2\2\u00af\u00b0\7?\2\2\u00b0\21\3\2\2\2\u00b1\u00b2"+
		"\7=\2\2\u00b2\23\3\2\2\2\u00b3\u00b4\7*\2\2\u00b4\25\3\2\2\2\u00b5\u00b6"+
		"\7+\2\2\u00b6\27\3\2\2\2\u00b7\u00b8\7]\2\2\u00b8\31\3\2\2\2\u00b9\u00ba"+
		"\7_\2\2\u00ba\33\3\2\2\2\u00bb\u00bd\7/\2\2\u00bc\u00bb\3\2\2\2\u00bc"+
		"\u00bd\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\u00c5\5\36\17\2\u00bf\u00c1\7"+
		"\60\2\2\u00c0\u00c2\t\4\2\2\u00c1\u00c0\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3"+
		"\u00c1\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\u00c6\3\2\2\2\u00c5\u00bf\3\2"+
		"\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c8\3\2\2\2\u00c7\u00c9\5 \20\2\u00c8"+
		"\u00c7\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9\35\3\2\2\2\u00ca\u00d3\7\62\2"+
		"\2\u00cb\u00cf\t\5\2\2\u00cc\u00ce\t\4\2\2\u00cd\u00cc\3\2\2\2\u00ce\u00d1"+
		"\3\2\2\2\u00cf\u00cd\3\2\2\2\u00cf\u00d0\3\2\2\2\u00d0\u00d3\3\2\2\2\u00d1"+
		"\u00cf\3\2\2\2\u00d2\u00ca\3\2\2\2\u00d2\u00cb\3\2\2\2\u00d3\37\3\2\2"+
		"\2\u00d4\u00d6\t\6\2\2\u00d5\u00d7\t\7\2\2\u00d6\u00d5\3\2\2\2\u00d6\u00d7"+
		"\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8\u00d9\5\36\17\2\u00d9!\3\2\2\2\u00da"+
		"\u00db\7%\2\2\u00db\u00dc\7%\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00de\5$\22"+
		"\2\u00de\u00df\3\2\2\2\u00df\u00e0\b\21\2\2\u00e0#\3\2\2\2\u00e1\u00e3"+
		"\n\3\2\2\u00e2\u00e1\3\2\2\2\u00e3\u00e6\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e4"+
		"\u00e5\3\2\2\2\u00e5%\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e7\u00e8\7%\2\2\u00e8"+
		"\u00ea\7\"\2\2\u00e9\u00e7\3\2\2\2\u00e9\u00ea\3\2\2\2\u00ea\u00ef\3\2"+
		"\2\2\u00eb\u00f0\5,\26\2\u00ec\u00f0\5(\24\2\u00ed\u00f0\58\34\2\u00ee"+
		"\u00f0\5\62\31\2\u00ef\u00eb\3\2\2\2\u00ef\u00ec\3\2\2\2\u00ef\u00ed\3"+
		"\2\2\2\u00ef\u00ee\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1\u00ef\3\2\2\2\u00f1"+
		"\u00f2\3\2\2\2\u00f2\u0107\3\2\2\2\u00f3\u00f5\7%\2\2\u00f4\u00f3\3\2"+
		"\2\2\u00f4\u00f5\3\2\2\2\u00f5\u00f7\3\2\2\2\u00f6\u00f8\7\"\2\2\u00f7"+
		"\u00f6\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9\u00f7\3\2\2\2\u00f9\u00fa\3\2"+
		"\2\2\u00fa\u00fc\3\2\2\2\u00fb\u00fd\7%\2\2\u00fc\u00fb\3\2\2\2\u00fc"+
		"\u00fd\3\2\2\2\u00fd\u0101\3\2\2\2\u00fe\u0102\5,\26\2\u00ff\u0102\5("+
		"\24\2\u0100\u0102\58\34\2\u0101\u00fe\3\2\2\2\u0101\u00ff\3\2\2\2\u0101"+
		"\u0100\3\2\2\2\u0102\u0103\3\2\2\2\u0103\u0101\3\2\2\2\u0103\u0104\3\2"+
		"\2\2\u0104\u0106\3\2\2\2\u0105\u00f4\3\2\2\2\u0106\u0109\3\2\2\2\u0107"+
		"\u0105\3\2\2\2\u0107\u0108\3\2\2\2\u0108\'\3\2\2\2\u0109\u0107\3\2\2\2"+
		"\u010a\u010b\n\b\2\2\u010b)\3\2\2\2\u010c\u010d\t\b\2\2\u010d+\3\2\2\2"+
		"\u010e\u0111\7^\2\2\u010f\u0112\t\t\2\2\u0110\u0112\5.\27\2\u0111\u010f"+
		"\3\2\2\2\u0111\u0110\3\2\2\2\u0112\u0118\3\2\2\2\u0113\u0114\7^\2\2\u0114"+
		"\u0118\5*\25\2\u0115\u0116\7\u0080\2\2\u0116\u0118\5*\25\2\u0117\u010e"+
		"\3\2\2\2\u0117\u0113\3\2\2\2\u0117\u0115\3\2\2\2\u0118-\3\2\2\2\u0119"+
		"\u011a\7w\2\2\u011a\u011b\5\60\30\2\u011b\u011c\5\60\30\2\u011c\u011d"+
		"\5\60\30\2\u011d\u011e\5\60\30\2\u011e/\3\2\2\2\u011f\u0120\t\n\2\2\u0120"+
		"\61\3\2\2\2\u0121\u0122\7%\2\2\u0122\u0123\5&\23\2\u0123\63\3\2\2\2\u0124"+
		"\u0127\7$\2\2\u0125\u0128\5&\23\2\u0126\u0128\5\66\33\2\u0127\u0125\3"+
		"\2\2\2\u0127\u0126\3\2\2\2\u0128\u0129\3\2\2\2\u0129\u012a\7$\2\2\u012a"+
		"\65\3\2\2\2\u012b\u012d\n\13\2\2\u012c\u012b\3\2\2\2\u012d\u0130\3\2\2"+
		"\2\u012e\u012c\3\2\2\2\u012e\u012f\3\2\2\2\u012f\67\3\2\2\2\u0130\u012e"+
		"\3\2\2\2\u0131\u0132\7b\2\2\u0132\u0133\5:\35\2\u0133\u0134\7b\2\2\u0134"+
		"9\3\2\2\2\u0135\u0137\n\f\2\2\u0136\u0135\3\2\2\2\u0137\u013a\3\2\2\2"+
		"\u0138\u0136\3\2\2\2\u0138\u0139\3\2\2\2\u0139;\3\2\2\2\u013a\u0138\3"+
		"\2\2\2\u013b\u013c\7}\2\2\u013c\u013d\3\2\2\2\u013d\u013e\b\36\3\2\u013e"+
		"=\3\2\2\2\u013f\u0141\t\2\2\2\u0140\u013f\3\2\2\2\u0141\u0142\3\2\2\2"+
		"\u0142\u0140\3\2\2\2\u0142\u0143\3\2\2\2\u0143\u0144\3\2\2\2\u0144\u0145"+
		"\b\37\2\2\u0145?\3\2\2\2\u0146\u0147\7\62\2\2\u0147\u0148\7\62\2\2\u0148"+
		"\u0152\7\62\2\2\u0149\u014a\7p\2\2\u014a\u014b\7w\2\2\u014b\u014c\7n\2"+
		"\2\u014c\u0152\7n\2\2\u014d\u014e\7P\2\2\u014e\u014f\7W\2\2\u014f\u0150"+
		"\7N\2\2\u0150\u0152\7N\2\2\u0151\u0146\3\2\2\2\u0151\u0149\3\2\2\2\u0151"+
		"\u014d\3\2\2\2\u0152\u0153\3\2\2\2\u0153\u0154\b \4\2\u0154A\3\2\2\2\u0155"+
		"\u0156\7\62\2\2\u0156\u0160\7\63\2\2\u0157\u0158\7v\2\2\u0158\u0159\7"+
		"t\2\2\u0159\u015a\7w\2\2\u015a\u0160\7g\2\2\u015b\u015c\7V\2\2\u015c\u015d"+
		"\7T\2\2\u015d\u015e\7W\2\2\u015e\u0160\7G\2\2\u015f\u0155\3\2\2\2\u015f"+
		"\u0157\3\2\2\2\u015f\u015b\3\2\2\2\u0160\u0161\3\2\2\2\u0161\u0162\b!"+
		"\5\2\u0162C\3\2\2\2\u0163\u0164\7\62\2\2\u0164\u0170\7\62\2\2\u0165\u0166"+
		"\7h\2\2\u0166\u0167\7c\2\2\u0167\u0168\7n\2\2\u0168\u0169\7u\2\2\u0169"+
		"\u0170\7g\2\2\u016a\u016b\7H\2\2\u016b\u016c\7C\2\2\u016c\u016d\7N\2\2"+
		"\u016d\u016e\7U\2\2\u016e\u0170\7G\2\2\u016f\u0163\3\2\2\2\u016f\u0165"+
		"\3\2\2\2\u016f\u016a\3\2\2\2\u0170\u0171\3\2\2\2\u0171\u0172\b\"\6\2\u0172"+
		"E\3\2\2\2\u0173\u0177\t\3\2\2\u0174\u0175\7\17\2\2\u0175\u0177\7\f\2\2"+
		"\u0176\u0173\3\2\2\2\u0176\u0174\3\2\2\2\u0177\u0178\3\2\2\2\u0178\u0179"+
		"\b#\7\2\u0179G\3\2\2\2\u017a\u017b\7<\2\2\u017b\u017c\3\2\2\2\u017c\u017d"+
		"\b$\b\2\u017dI\3\2\2\2\u017e\u017f\7?\2\2\u017f\u0180\3\2\2\2\u0180\u0181"+
		"\b%\t\2\u0181K\3\2\2\2\u0182\u0183\7=\2\2\u0183\u0184\3\2\2\2\u0184\u0185"+
		"\b&\n\2\u0185M\3\2\2\2\u0186\u0187\7*\2\2\u0187\u0188\3\2\2\2\u0188\u0189"+
		"\b\'\13\2\u0189O\3\2\2\2\u018a\u018b\7+\2\2\u018b\u018c\3\2\2\2\u018c"+
		"\u018d\b(\f\2\u018dQ\3\2\2\2\u018e\u018f\7]\2\2\u018f\u0190\3\2\2\2\u0190"+
		"\u0191\b)\r\2\u0191S\3\2\2\2\u0192\u0193\7_\2\2\u0193\u0194\3\2\2\2\u0194"+
		"\u0195\b*\16\2\u0195U\3\2\2\2\u0196\u0198\7/\2\2\u0197\u0196\3\2\2\2\u0197"+
		"\u0198\3\2\2\2\u0198\u0199\3\2\2\2\u0199\u01a0\5\36\17\2\u019a\u019c\7"+
		"\60\2\2\u019b\u019d\t\4\2\2\u019c\u019b\3\2\2\2\u019d\u019e\3\2\2\2\u019e"+
		"\u019c\3\2\2\2\u019e\u019f\3\2\2\2\u019f\u01a1\3\2\2\2\u01a0\u019a\3\2"+
		"\2\2\u01a0\u01a1\3\2\2\2\u01a1\u01a3\3\2\2\2\u01a2\u01a4\5 \20\2\u01a3"+
		"\u01a2\3\2\2\2\u01a3\u01a4\3\2\2\2\u01a4\u01a5\3\2\2\2\u01a5\u01a6\b+"+
		"\17\2\u01a6W\3\2\2\2\u01a7\u01a8\7A\2\2\u01a8Y\3\2\2\2\u01a9\u01aa\7\61"+
		"\2\2\u01aa[\3\2\2\2\u01ab\u01ac\7@\2\2\u01ac]\3\2\2\2\u01ad\u01ae\7>\2"+
		"\2\u01ae_\3\2\2\2\u01af\u01b0\7,\2\2\u01b0a\3\2\2\2\u01b1\u01b2\7(\2\2"+
		"\u01b2c\3\2\2\2\u01b3\u01b4\7~\2\2\u01b4e\3\2\2\2\u01b5\u01b6\7#\2\2\u01b6"+
		"g\3\2\2\2\u01b7\u01b8\7}\2\2\u01b8\u01b9\3\2\2\2\u01b9\u01ba\b\64\3\2"+
		"\u01ba\u01bb\b\64\20\2\u01bbi\3\2\2\2\u01bc\u01c0\5p8\2\u01bd\u01c0\5"+
		"l\66\2\u01be\u01c0\5v;\2\u01bf\u01bc\3\2\2\2\u01bf\u01bd\3\2\2\2\u01bf"+
		"\u01be\3\2\2\2\u01c0\u01c1\3\2\2\2\u01c1\u01bf\3\2\2\2\u01c1\u01c2\3\2"+
		"\2\2\u01c2\u01d1\3\2\2\2\u01c3\u01c5\7\"\2\2\u01c4\u01c3\3\2\2\2\u01c5"+
		"\u01c6\3\2\2\2\u01c6\u01c4\3\2\2\2\u01c6\u01c7\3\2\2\2\u01c7\u01cb\3\2"+
		"\2\2\u01c8\u01cc\5p8\2\u01c9\u01cc\5l\66\2\u01ca\u01cc\5v;\2\u01cb\u01c8"+
		"\3\2\2\2\u01cb\u01c9\3\2\2\2\u01cb\u01ca\3\2\2\2\u01cc\u01cd\3\2\2\2\u01cd"+
		"\u01cb\3\2\2\2\u01cd\u01ce\3\2\2\2\u01ce\u01d0\3\2\2\2\u01cf\u01c4\3\2"+
		"\2\2\u01d0\u01d3\3\2\2\2\u01d1\u01cf\3\2\2\2\u01d1\u01d2\3\2\2\2\u01d2"+
		"\u01d4\3\2\2\2\u01d3\u01d1\3\2\2\2\u01d4\u01d5\b\65\21\2\u01d5k\3\2\2"+
		"\2\u01d6\u01d7\n\r\2\2\u01d7m\3\2\2\2\u01d8\u01d9\t\r\2\2\u01d9o\3\2\2"+
		"\2\u01da\u01dd\7^\2\2\u01db\u01de\t\t\2\2\u01dc\u01de\5.\27\2\u01dd\u01db"+
		"\3\2\2\2\u01dd\u01dc\3\2\2\2\u01de\u01e4\3\2\2\2\u01df\u01e0\7^\2\2\u01e0"+
		"\u01e4\5n\67\2\u01e1\u01e2\7\u0080\2\2\u01e2\u01e4\5n\67\2\u01e3\u01da"+
		"\3\2\2\2\u01e3\u01df\3\2\2\2\u01e3\u01e1\3\2\2\2\u01e4q\3\2\2\2\u01e5"+
		"\u01e6\7%\2\2\u01e6\u01e7\7%\2\2\u01e7\u01e8\3\2\2\2\u01e8\u01e9\5$\22"+
		"\2\u01e9\u01ea\3\2\2\2\u01ea\u01eb\b9\2\2\u01ebs\3\2\2\2\u01ec\u01ef\7"+
		"$\2\2\u01ed\u01f0\5&\23\2\u01ee\u01f0\5\66\33\2\u01ef\u01ed\3\2\2\2\u01ef"+
		"\u01ee\3\2\2\2\u01f0\u01f1\3\2\2\2\u01f1\u01f2\7$\2\2\u01f2\u01f3\3\2"+
		"\2\2\u01f3\u01f4\b:\22\2\u01f4u\3\2\2\2\u01f5\u01f8\7b\2\2\u01f6\u01f9"+
		"\5&\23\2\u01f7\u01f9\5:\35\2\u01f8\u01f6\3\2\2\2\u01f8\u01f7\3\2\2\2\u01f9"+
		"\u01fa\3\2\2\2\u01fa\u01fb\7b\2\2\u01fb\u01fc\3\2\2\2\u01fc\u01fd\b;\23"+
		"\2\u01fdw\3\2\2\2\u01fe\u01ff\7\177\2\2\u01ff\u0200\3\2\2\2\u0200\u0201"+
		"\b<\24\2\u0201y\3\2\2\2\62\2\3}\u008c\u0098\u00a6\u00ab\u00bc\u00c3\u00c5"+
		"\u00c8\u00cf\u00d2\u00d6\u00e4\u00e9\u00ef\u00f1\u00f4\u00f9\u00fc\u0101"+
		"\u0103\u0107\u0111\u0117\u0127\u012e\u0138\u0142\u0151\u015f\u016f\u0176"+
		"\u0197\u019e\u01a0\u01a3\u01bf\u01c1\u01c6\u01cb\u01cd\u01d1\u01dd\u01e3"+
		"\u01ef\u01f8\25\b\2\2\7\3\2\t\4\2\t\5\2\t\6\2\t\7\2\t\b\2\t\t\2\t\n\2"+
		"\t\13\2\t\f\2\t\r\2\t\16\2\t\17\2\t\25\2\t\21\2\t\23\2\t\24\2\6\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}