package vn.brine.haileader.exploratorysearch.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

import java.util.ArrayList;
import java.util.List;

import vn.brine.haileader.exploratorysearch.fragments.HomeFragment;
import vn.brine.haileader.exploratorysearch.utils.Config;
import vn.brine.haileader.exploratorysearch.utils.DataAssistant;
import vn.brine.haileader.exploratorysearch.utils.QueryAssistant;

/**
 * Created by HaiLeader on 7/13/2016.
 */
public class SearchListKeywordLMD extends AsyncTask<List<String>, Void, List<ResultSet>>{

    public final static String TAG = SearchListKeywordLMD.class.getSimpleName();
    private Context mContext;
    private int mTypeSearch;
    private ProgressDialog mProgressDialog;
    private OnTaskCompleted mOnTaskCompleted;

    public SearchListKeywordLMD(Context context, OnTaskCompleted onTaskCompleted, int typeSearch){
        this.mContext = context;
        this.mOnTaskCompleted = onTaskCompleted;
        this.mTypeSearch = typeSearch;
        mProgressDialog = new ProgressDialog(mContext);
    }

    public interface OnTaskCompleted {
        void onAsyncTaskCompletedListLMD(List<ResultSet> resultSetList, int typeSearch);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
    }

    @Override
    protected List<ResultSet> doInBackground(List<String>... params) {
        List<ResultSet> resultSetList = new ArrayList<ResultSet>();
        List<String> keywordList = params[0];
        Log.d(TAG, keywordList.toString());

        for(String keyword : keywordList){
            if (DataAssistant.isStopWord(keyword)) continue;
            String queryString = "";
            if(mTypeSearch == HomeFragment.SEARCH_ACCURATE){
                queryString = QueryAssistant.searchAccurateQuery(keyword);
            }else if(mTypeSearch == HomeFragment.SEARCH_EXPAND){
                queryString = QueryAssistant.searchExpandQuery(keyword);
            }
            Log.d(TAG, queryString);
            Query query = QueryFactory.create(queryString);
            QueryExecution queryExecution = QueryExecutionFactory.createServiceRequest(Config.LINKEDMDB_ENDPOINT, query);
            ResultSet resultSet = queryExecution.execSelect();
            if(resultSet != null){
                resultSetList.add(resultSet);
            }
            queryExecution.close();
        }
        return resultSetList;
    }

    @Override
    protected void onPostExecute(List<ResultSet> resultSets) {
        super.onPostExecute(resultSets);
        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        mOnTaskCompleted.onAsyncTaskCompletedListLMD(resultSets, mTypeSearch);
    }
}
