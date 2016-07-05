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

import vn.brine.haileader.musicjena.asynctasks.SearchData;
import vn.brine.haileader.musicjena.utils.Config;
import vn.brine.haileader.musicjena.utils.DataAssistant;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, SearchData.onTaskCompleted{

    public static final String TAG = MainActivity.class.getName();
    public static final int SEARCH_ACCURATE_DATA = 1;
    public static final int SEARCH_EXPAND_DATA = 2;
    public static final int SEARCH_ALL_MOVIETYPE = 3;

    private EditText mSearchText;
    private Button mTypeMovieButton;
    private ArrayList<String> mArrayListKeyword;
    private ArrayList<String> mArrayListType;
    private ArrayList<String> mArrayListMovieType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validIdLayout();

        mTypeMovieButton.setOnClickListener(this);

        mArrayListKeyword = new ArrayList<String>();
        mArrayListMovieType = new ArrayList<String>();
        mArrayListType = new ArrayList<String>();

        getAllMovieType();
    }

    private void validIdLayout() {
        mSearchText = (EditText) findViewById(R.id.searchText);
        mTypeMovieButton = (Button) findViewById(R.id.allTypeMovieButton);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.allTypeMovieButton:
                String textSearch = mSearchText.getText().toString();
                analyzeInputData(textSearch);
                break;
        }
    }

    @Override
    public void onAsyncTaskCompleted(ResultSet resultSet, int typeSearch) {
        if(resultSet == null) return;

        switch (typeSearch){
            case SEARCH_ACCURATE_DATA:
                while (resultSet.hasNext()){
                    QuerySolution binding = resultSet.nextSolution();
                    Resource url = (Resource) binding.get("url");
                    if(url.getURI().contains("freebase")){
                        Log.d("SearchAccurate", "Xu ly");
                    }
                }
                break;
            case SEARCH_EXPAND_DATA:
                while (resultSet.hasNext()){
                    QuerySolution binding = resultSet.nextSolution();
                    Resource url = (Resource) binding.get("url");
                    if(url.getURI().contains("freebase")){
                        Log.d("SearchExpand", "Xu ly");
                    }
                }
                break;
            case SEARCH_ALL_MOVIETYPE:
                while (resultSet.hasNext()){
                    QuerySolution binding = resultSet.nextSolution();
                    Resource subject = (Resource) binding.get("o");
                    String localName = subject.getLocalName();
                    if(localName != null){
                        localName = DataAssistant.replaceUnderline(localName);
                        mArrayListMovieType.add(localName);
                    }
                }
                Toast.makeText(getApplicationContext(), mArrayListMovieType.toString(), Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void searchAccurate(View v){
        String keyword = "titanic";
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
        new SearchData(this, SEARCH_ACCURATE_DATA).execute(queryString);
    }

    public void searchExpand(View v){
        String keyword = "titanic";
        String queryString = Config.PREFIX_LINKEDMDB +
                "SELECT DISTINCT * WHERE " +
                "{ ?s rdfs:label ?o . FILTER regex(?o, " + "\"" + keyword + "\"" + " ,'i'). " +
                "?s foaf:page ?url} limit 16";
        new SearchData(this, SEARCH_EXPAND_DATA).execute(queryString);
    }

    private void analyzeInputData(String textSearch){
        if(textSearch == null) return;
        splitDataToArrayKey(textSearch);
        expandSearchKeywordType();
    }

    private void splitDataToArrayKey(String textSearch){
        mArrayListKeyword.clear();
        mArrayListKeyword = DataAssistant.splitTextSearchToPhrase(textSearch);
    }

    private void expandSearchKeywordType(){
        if(mArrayListMovieType.isEmpty()){
            getAllMovieType();
        }
        getMovieTypeFromTextSearch();
    }

    private void getAllMovieType(){
        String queryString =
                Config.PREFIX_LINKEDMDB +
                "SELECT distinct ?o WHERE {?s rdf:type ?o}";
        new SearchData(this, SEARCH_ALL_MOVIETYPE).execute(queryString);
    }

    private void getMovieTypeFromTextSearch(){
        mArrayListType.clear();
        if(mArrayListKeyword.isEmpty()) return;

        for(String movieType : mArrayListMovieType){
            for(String keyword : mArrayListKeyword){
                if(DataAssistant.isStopWord(keyword)) continue;
                if(movieType.contains(keyword)){
                    mArrayListType.add(movieType + keyword);
                }
            }
        }
    }
}