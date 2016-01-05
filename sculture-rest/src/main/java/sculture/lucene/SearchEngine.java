package sculture.lucene;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchEngine {
    static Directory index;
    static SynonymAnalyzer analyzer;
    static WordNetDatabase database;

    public static void initialize() {
        String u = SearchEngine.class.getClassLoader().getResource("WordNet-3.0").getPath();
        System.out.println(u + "\n\n\n\n\n\n\n\n\n\n\n");
        System.setProperty("wordnet.database.dir", u + File.separator + "dict");
        database = WordNetDatabase.getFileInstance();

        if (database == null) {
            System.out.println("null" + "\n\n\n\n\n\n\n\n\n\n\n");
        } else
            System.out.println("notnull" + "\n\n\n\n\n\n\n\n\n\n\n");


        analyzer = new SynonymAnalyzer();
        try {
            index = FSDirectory.open(new File("index").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addDoc(long id, String title, String content, String tags) {
        if (database == null)
            initialize();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w;
        try {
            w = new IndexWriter(index, config);
            Document doc = new Document();
            doc.add(new TextField("id", id + "", Field.Store.YES));
            doc.add(new TextField("title", title, Field.Store.YES));
            doc.add(new TextField("content", content, Field.Store.YES));
            doc.add(new TextField("tags", tags, Field.Store.YES));
            w.addDocument(doc);
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeDoc(long id) {
        if (database == null)
            initialize();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w;
        try {
            w = new IndexWriter(index, config);
            Query q = new QueryParser("id", analyzer).parse(id + "");
            w.deleteDocuments(q);
            w.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static List<Long> search(String search_term, int page, int size) {
        if (database == null)
            initialize();

        String wordnet = "";
        String[] searches = search_term.split(" ");

        NounSynset nounSynset;
        NounSynset[] hyponyms;
        NounSynset[] hypernyms;

        for (String s : searches) {
            Synset[] synsets = database.getSynsets(s, SynsetType.NOUN);
            for (Synset synset : synsets) {
                nounSynset = (NounSynset) (synset);
                hyponyms = nounSynset.getHyponyms();
                hypernyms = nounSynset.getHypernyms();

                System.out.println(hypernyms.length);
                System.out.println(hyponyms.length);
                for (NounSynset n : hypernyms) {
                    wordnet = n.getWordForms()[0] + " ";
                }

                for (NounSynset n : hyponyms) {
                    wordnet = n.getWordForms()[0] + " ";
                }
            }
        }

        List<Long> story_ids = new ArrayList<>();
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"id", "title", "content", "tags"}, analyzer);
        Query query = null;
        try {
            int startIndex = (page - 1) * size;
            int endIndex = page * size;
            query = parser.parse(search_term + " " + wordnet);
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector.create(endIndex + 1);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            for (int i = startIndex; i < hits.length; i++) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                story_ids.add(Long.valueOf(d.get("id")));
            }


        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return story_ids;
    }

    public static void removeAll() {
        if (database == null)
            initialize();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w;
        try {
            w = new IndexWriter(index, config);
            w.deleteAll();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
