package vn.brine.haileader.musicjena.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

import vn.brine.haileader.musicjena.utils.Config;

/**
 * Created by HaiLeader on 7/5/2016.
 */
public class SearchDataLMD extends AsyncTask<String, Void, ResultSet> {

    public final static String TAG = "SearchData";
    private OnTaskCompleted mOnTaskCompleted;
    private int mTypeSearch;

    public SearchDataLMD(OnTaskCompleted onTaskCompleted, int typeSearch){
        this.mOnTaskCompleted = onTaskCompleted;
        this.mTypeSearch = typeSearch;
    }

    public interface OnTaskCompleted {
        void onAsyncTaskCompletedLMD(ResultSet resultSet, int typeSearch);
    }

    @Override
    protected ResultSet doInBackground(String... params) {
        String queryString = params[0];
        Log.d(TAG, queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.createServiceRequest(Config.LINKEDMDB_ENDPOINT, query);
        ResultSet resultSet = queryExecution.execSelect();
        queryExecution.close();
        return resultSet;
    }

    @Override
    protected void onPostExecute(ResultSet resultSet) {
        super.onPostExecute(resultSet);
        mOnTaskCompleted.onAsyncTaskCompletedLMD(resultSet, mTypeSearch);
    }
}
