package vn.brine.haileader.musicjena;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.List;

import vn.brine.haileader.musicjena.asynctasks.SearchDataLMD;
import vn.brine.haileader.musicjena.utils.Config;
import vn.brine.haileader.musicjena.utils.DataAssistant;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, SearchDataLMD.OnTaskCompleted {
    public static final String TAG = MainActivity.class.getName();

    public static final int SEARCH_ACCURATE_DATA_LMD = 1;
    public static final int SEARCH_EXPAND_DATA_LMD = 2;
    public static final int SEARCH_TYPE_LMD = 3;
    public static final int SEARCH_TEST = 4;

    private EditText mSearchText;
    private Button mSearchAllButton;
    private List<String> mListKeyword;
    private List<String> mListMovieType;
    private List<String> mListAllMovieType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validIdLayout();

        mSearchAllButton.setOnClickListener(this);

        mListKeyword = new ArrayList<String>();
        mListMovieType = new ArrayList<String>();
        mListAllMovieType = getAllMovieType();
        Toast.makeText(getApplicationContext(), mListAllMovieType.toString(), Toast.LENGTH_LONG).show();
    }

    private void validIdLayout() {
        mSearchText = (EditText) findViewById(R.id.searchText);
        mSearchAllButton = (Button) findViewById(R.id.searchAll);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchAll:
                String textSearch = mSearchText.getText().toString();
                analyzeInputData(textSearch);
                searchAll();
                break;
        }
    }

    @Override
    public void onAsyncTaskCompletedLMD(ResultSet resultSet, int typeSearch) {
        if (resultSet == null) return;

        switch (typeSearch) {
            case SEARCH_ACCURATE_DATA_LMD:
                while (resultSet.hasNext()) {
                    QuerySolution binding = resultSet.nextSolution();
                    Resource url = (Resource) binding.get("url");
                    if (url.getURI().contains("freebase")) {
                        showLog("searchAccurateKeyword", "Xu ly");
                        if (!mListMovieType.isEmpty()) {
                            for (String movieType : mListMovieType) {
                                getInfoFromType(url.getURI(), movieType);
                            }
                        }
                        searchDataFreeBase(url.getURI());
                    }
                }
                break;
            case SEARCH_EXPAND_DATA_LMD:
                while (resultSet.hasNext()) {
                    QuerySolution binding = resultSet.nextSolution();
                    Resource url = (Resource) binding.get("url");
                    if (url.getURI().contains("freebase")) {
                        showLog("SearchExpand", "Xu ly : " + url.getLocalName());
                        searchDataFreeBase(url.getURI());
                    }
                }
                break;
            case SEARCH_TYPE_LMD:
                break;
            case SEARCH_TEST:
                while (resultSet.hasNext()) {
                    QuerySolution binding = resultSet.nextSolution();
                    Resource url = (Resource) binding.get("page");
                    if (url.getURI().contains("dbpedia")) {
                        showLog("URITEST", url.getURI());
                    }
                }
                break;
        }
    }

    private void analyzeInputData(String textSearch) {
        if (textSearch.equals("")) return;
        splitDataToArrayKey(textSearch);
        expandSearchKeywordType();
    }

    private void splitDataToArrayKey(String textSearch) {
        mListKeyword.clear();
        mListKeyword = DataAssistant.splitTextSearchToPhrase(textSearch);
        showLog("mListKeyword", mListKeyword.toString());
    }

    private void expandSearchKeywordType() {
        if (mListAllMovieType.isEmpty()) {
            getAllMovieType();
        }
        getMovieTypeFromTextSearch();
    }

    private List<String> getAllMovieType() {
        return DataAssistant.convertStringArrayToList();
    }

    private void getMovieTypeFromTextSearch() {
        mListMovieType.clear();
        if (mListKeyword.isEmpty()) return;

        for (String movieType : mListAllMovieType) {
            for (String keyword : mListKeyword) {
                if (DataAssistant.isStopWord(keyword)) continue;
                if (movieType.contains(keyword)) {
                    String rdfMovieType = "movie:" + DataAssistant.replaceSpaceToUnderline(keyword);
                    if (!mListMovieType.contains(rdfMovieType))
                        mListMovieType.add(rdfMovieType);
                }
            }
        }
        showLog("mListMovieType", mListMovieType.toString());
    }

    private void searchAll() {
        if (mListKeyword.isEmpty()) return;
        for (String keyword : mListKeyword) {
            if (DataAssistant.isStopWord(keyword)) continue;
            searchAccurate(keyword);
            searchExpand(keyword);
        }
    }

    private void searchAccurate(String keyword) {
        String queryString = Config.PREFIX_LINKEDMDB +
                "SELECT DISTINCT * WHERE " +
                "{" +
                " {" +
                "{?s dc:title " + "\"" + keyword + "\"" + "} " +
                "UNION {?s movie:actor_name " + "\"" + keyword + "\"" + "} " +
                "UNION {?s movie:editor_name " + "\"" + keyword + "\"" + "} " +
                "UNION {?s movie:director_name " + "\"" + keyword + "\"" + "} " +
                "UNION {?s movie:writer_name " + "\"" + keyword + "\"" + "} " +
                "UNION {?s movie:producer_name " + "\"" + keyword + "\"" + "} " +
                "UNION {?s movie:film_character_name " + "\"" + keyword + "\"" + "} " +
                "UNION {?s movie:film_genre_name " + "\"" + keyword + "\"" + "}" +
                "}. " +
                "?s foaf:page ?url.?s rdfs:label ?label" +
                "}";
        new SearchDataLMD(this, this, SEARCH_ACCURATE_DATA_LMD).execute(queryString);
    }

    private void searchExpand(String keyword) {
        String queryString = Config.PREFIX_LINKEDMDB +
                "SELECT DISTINCT * WHERE " +
                "{ ?s rdfs:label ?o . FILTER regex(?o, " + "\"" + keyword + "\"" + " ,'i'). " +
                "?s foaf:page ?url} limit 16";
        new SearchDataLMD(this, this, SEARCH_EXPAND_DATA_LMD).execute(queryString);
    }

    private void getInfoFromType(String uriKey, String movieType) {
        movieType = DataAssistant.changeTextType(uriKey, movieType);
        if (movieType == null) return;
        String queryString = Config.PREFIX_LINKEDMDB +
                "SELECT * WHERE {" +
                "{<" + uriKey + "> " + movieType + " ?s}" +
                "UNION{?s " + movieType + "<" + uriKey + "> }. " +
                "?s foaf:page ?url}";
        new SearchDataLMD(this, this, SEARCH_TYPE_LMD).execute(queryString);
    }

    private void searchDataFreeBase(String uri){

    }

    public void testSearch(View v){
        String key = "http://data.linkedmdb.org/resource/film/3951";
        String queryString = Config.PREFIX_LINKEDMDB +
                "SELECT * WHERE{<"+key+"> owl:sameAs ?page }";
        new SearchDataLMD(this, this, SEARCH_TEST).execute(queryString);
    }

    private void showLog(String tag, String message){
        Log.d(tag, message);
    }
}