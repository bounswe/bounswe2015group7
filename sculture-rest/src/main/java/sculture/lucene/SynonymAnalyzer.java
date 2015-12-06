package sculture.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.util.PagedBytes;
import org.apache.lucene.util.Version;
import org.apache.lucene.util.packed.PackedInts;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Fatih on 06-Dec-15.
 */
public class SynonymAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String s) {

        Tokenizer source = new ClassicTokenizer();
        TokenStream filter = new StandardFilter(source);
        filter = new LowerCaseFilter(filter);
        SynonymMap synonymMap = null;

        try {
            synonymMap = buildSynonym();
        } catch (IOException e) {
            e.printStackTrace();
        }
        filter = new SynonymFilter(filter, synonymMap, false);
        return new TokenStreamComponents(source, filter);
    }

    private SynonymMap buildSynonym() throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("wn_s.pl");
        Reader rulesReader = new InputStreamReader(stream);
        WordnetSynonymParser parser = new WordnetSynonymParser(true, true, new StandardAnalyzer());
        try {
            parser.parse(rulesReader);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        SynonymMap synonymMap = parser.build();
        return synonymMap;
    }
}
