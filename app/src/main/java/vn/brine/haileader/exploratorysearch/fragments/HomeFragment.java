package vn.brine.haileader.exploratorysearch.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.List;

import vn.brine.haileader.exploratorysearch.R;
import vn.brine.haileader.exploratorysearch.adapters.MovieAdapter;
import vn.brine.haileader.exploratorysearch.asynctasks.SearchDataLMD;
import vn.brine.haileader.exploratorysearch.models.DividerItemDecoration;
import vn.brine.haileader.exploratorysearch.models.Movie;
import vn.brine.haileader.exploratorysearch.utils.Config;
import vn.brine.haileader.exploratorysearch.utils.DataAssistant;
import vn.brine.haileader.exploratorysearch.utils.QueryAssistant;

public class HomeFragment extends Fragment
        implements View.OnClickListener, SearchDataLMD.OnTaskCompleted{

    public static final String TAG = HomeFragment.class.getSimpleName();
    public static final int SEARCH_ACCURATE_DATA_LMD = 1;
    public static final int SEARCH_EXPAND_DATA_LMD = 2;
    public static final int SEARCH_TYPE_LMD = 3;
    public static final int SEARCH_TEST = 4;

    private EditText mSearchText;
    private Button mSearchAllButton;
    private RecyclerView mTopRecyclerView;
    private RecyclerView mRecommendRecyclerView;

    private MovieAdapter mTopAdapter;
    private MovieAdapter mRecommendAdapter;

    private List<Movie> movieTopList;
    private List<Movie> movieRecommendList;
    private List<String> mListKeyword;
    private List<String> mListMovieType;
    private List<String> mListAllMovieType;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchText = (EditText) view.findViewById(R.id.searchText);
        mSearchAllButton = (Button) view.findViewById(R.id.searchAll);
        mTopRecyclerView = (RecyclerView)view.findViewById(R.id.top_result_recycler_view);
        mRecommendRecyclerView = (RecyclerView)view.findViewById(R.id.recommend_result_recycler_view);

        mListKeyword = new ArrayList<String>();
        mListMovieType = new ArrayList<String>();
        mListAllMovieType = getAllMovieType();
        movieTopList = new ArrayList<Movie>();
        movieRecommendList = new ArrayList<Movie>();

        mTopAdapter = new MovieAdapter(getContext(), movieTopList);
        mRecommendAdapter = new MovieAdapter(getContext(), movieRecommendList);

        mTopRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mTopLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mTopRecyclerView.setLayoutManager(mTopLayoutManager);
        mTopRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        mTopRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTopRecyclerView.setAdapter(mTopAdapter);

        mRecommendRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mRecommendLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecommendRecyclerView.setLayoutManager(mRecommendLayoutManager);
        mRecommendRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        mRecommendRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecommendRecyclerView.setAdapter(mRecommendAdapter);

        mSearchAllButton.setOnClickListener(this);
        mTopRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mTopRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movieTopList.get(position);
                Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), "Long click", Toast.LENGTH_SHORT).show();
            }
        }));
        mRecommendRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecommendRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movieRecommendList.get(position);
                Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), "Long click", Toast.LENGTH_SHORT).show();
            }
        }));

        Toast.makeText(getContext(), mListAllMovieType.toString(), Toast.LENGTH_LONG).show();
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
                        updateDataTop(url.getURI());
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
                    updateDataRecommend(url.getURI());
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

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        String queryString = QueryAssistant.searchAccurateQuery(keyword);
        new SearchDataLMD(getContext(), this, SEARCH_ACCURATE_DATA_LMD).execute(queryString);
    }

    private void searchExpand(String keyword) {
        String queryString = QueryAssistant.searchExpandQuery(keyword);
        new SearchDataLMD(getContext(), this, SEARCH_EXPAND_DATA_LMD).execute(queryString);
    }

    private void getInfoFromType(String uriKey, String movieType) {
        movieType = DataAssistant.changeTextType(uriKey, movieType);
        if (movieType == null) return;
        String queryString = QueryAssistant.getInfoFromTypeQuery(uriKey, movieType);
        new SearchDataLMD(getContext(), this, SEARCH_TYPE_LMD).execute(queryString);
    }

    private void searchDataFreeBase(String uri){
        showLog("SearchDataFreeBase", uri);
    }

    public void testSearch(View v){
        String key = "http://data.linkedmdb.org/resource/film/3951";
        String queryString = Config.PREFIX_LINKEDMDB +
                "SELECT * WHERE{<"+key+"> owl:sameAs ?page }";
        new SearchDataLMD(getContext(), this, SEARCH_TEST).execute(queryString);
    }

    private void updateDataTop(String uri){
        Movie movie = new Movie(uri, null);
        movieTopList.add(movie);
        mTopAdapter.notifyDataSetChanged();
    }

    private void updateDataRecommend(String uri){
        Movie movie = new Movie(uri, null);
        movieRecommendList.add(movie);
        mRecommendAdapter.notifyDataSetChanged();
    }

    private void showLog(String tag, String message){
        Log.d(tag, message);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private HomeFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final HomeFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
