package vn.brine.haileader.musicjena;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vn.brine.haileader.musicjena.asynctasks.SearchAllMovieType;
import vn.brine.haileader.musicjena.utils.Config;
import vn.brine.haileader.musicjena.utils.DataAssistant;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SearchAllMovieType.OnTaskCompleted{

    public static final String TAG = MainActivity.class.getName();

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
    public void onAsyncTaskCompleted(ArrayList<String> arrayList) {
        this.mArrayListMovieType = arrayList;
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
        new SearchAllMovieType(this).execute();
    }

    private void getMovieTypeFromTextSearch(){
        mArrayListType.clear();
        if(mArrayListKeyword.isEmpty()) return;

        for(String movieType : mArrayListMovieType){
            for(String keyword : mArrayListKeyword){
                if(isStopWord(keyword)) continue;
                if(movieType.contains(keyword)){
                    mArrayListType.add(movieType + keyword);
                }
            }
        }
    }

    private boolean isStopWord(String word) {
        List<String> listStopWord = Arrays.asList(Config.STOP_WORD);
        if (listStopWord.contains(word)) return true;
        return false;
    }
}