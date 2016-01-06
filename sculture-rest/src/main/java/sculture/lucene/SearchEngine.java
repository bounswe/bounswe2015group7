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
import org.apache.lucene.search.*;
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

    private static final float ID_BOOST = 0;
    private static final float TITLE_BOOST = 1f;
    private static final float CONTENT_BOOST = 0.75f;
    private static final float TAG_BOOST = 1.5f;

    private static final float HYPONYM_BOOST = 0.7f;
    private static final float HYPERNYM_BOOST = 0.3f;

    public static void initialize() {
        String u = SearchEngine.class.getClassLoader().getResource("WordNet-3.0").getPath();
        System.setProperty("wordnet.database.dir", u + File.separator + "dict");
        database = WordNetDatabase.getFileInstance();

        analyzer = new SynonymAnalyzer();
        try {
            index = FSDirectory.open(new File("index").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();
    }

    public static void addDoc(long id, String title, String content, String tags) {
        if (database == null)
            initialize();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w;
        try {
            w = new IndexWriter(index, config);
            Document doc = new Document();

            TextField id_field = new TextField("id", id + "", Field.Store.YES);
            id_field.setBoost(ID_BOOST);

            TextField title_field = new TextField("title", title, Field.Store.YES);
            title_field.setBoost(TITLE_BOOST);

            TextField content_field = new TextField("content", content, Field.Store.YES);
            content_field.setBoost(CONTENT_BOOST);

            TextField tags_field = new TextField("tags", tags, Field.Store.YES);
            tags_field.setBoost(TAG_BOOST);

            doc.add(id_field);
            doc.add(title_field);
            doc.add(content_field);
            doc.add(tags_field);

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
        Synset[] synsets;
        for (String s : searches) {
            synsets = database.getSynsets(s, SynsetType.NOUN);
            for (Synset synset : synsets) {
                nounSynset = (NounSynset) (synset);
                hyponyms = nounSynset.getHyponyms();
                hypernyms = nounSynset.getHypernyms();

                System.out.println(hypernyms.length);
                System.out.println(hyponyms.length);
                for (NounSynset n : hypernyms) {
                    wordnet = "\"" + n.getWordForms()[0] + "\"^" + HYPERNYM_BOOST + " ";
                }

                for (NounSynset n : hyponyms) {
                    wordnet = "\"" + n.getWordForms()[0] + "\"^" + HYPONYM_BOOST + " ";
                }
            }
        }
        System.gc();
        List<Long> story_ids = new ArrayList<>();
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"id", "title", "content", "tags"}, analyzer);
        Query query;
        try {
            int endIndex = page * size;
            query = parser.parse(search_term + " " + wordnet);
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector.create(endIndex + 1);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs((page - 1) * size, size).scoreDocs;

            Document d;
            for (ScoreDoc hit : hits) {
                d = searcher.doc(hit.doc);
                story_ids.add(Long.valueOf(d.get("id")));
            }


        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        System.gc();
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
