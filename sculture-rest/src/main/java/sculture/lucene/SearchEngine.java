package sculture.lucene;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
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
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchEngine {
    static Directory index;
    static SynonymAnalyzer analyzer;

    public static void initialize() {
        analyzer = new SynonymAnalyzer();
        try {
            index = FSDirectory.open(new File("index").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addDoc(long id, String title, String content, String tags) {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w;
        try {
            w = new IndexWriter(index, config);
            Document doc = new Document();
            doc.add(new LongField("id", id, Field.Store.YES));
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
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w;
        try {
            w = new IndexWriter(index, config);
            Query q = new QueryParser("id", analyzer).parse(String.valueOf(id));
            w.deleteDocuments(q);
            w.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static List<Long> search(String search_term, int page, int size) {
        String wiki_data = "";
        String[] searches = search_term.split(" ");
        for (String s : searches) {

            try {
                HttpResponse<JsonNode> response = Unirest.get("https://www.wikidata.org/w/api.php?format=json&language=en&search=" + s + "&action=wbsearchentities")
                        .header("cache-control", "no-cache")
                        .header("postman-token", "02921fea-b341-d4b5-1516-4df99ccbf6ad")
                        .asJson();
                JSONArray results = response.getBody().getObject().getJSONArray("search");
                for (int i = 0; i < results.length(); i++) {
                    wiki_data += results.getJSONObject(i).getString("label") + " ";
                }

            } catch (UnirestException e) {
                e.printStackTrace();
            }

        }
        List<Long> story_ids = new ArrayList<>();
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"id", "title", "content", "tags"}, analyzer);
        Query query = null;
        try {
            int startIndex = (page - 1) * size;
            int endIndex = page * size;
            query = parser.parse(search_term + " " + wiki_data);
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
}
