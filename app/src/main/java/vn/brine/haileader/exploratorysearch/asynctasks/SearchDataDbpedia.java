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

import vn.brine.haileader.exploratorysearch.utils.Config;

/**
 * Created by HaiLeader on 7/12/2016.
 */
public class SearchDataDbpedia extends AsyncTask<String, Void, ResultSet> {

    public final static String TAG = "SearchDataDbPedia";
    private OnTaskCompleted mOnTaskCompleted;
    private int mTypeSearch;
    private Context mContext;
    private static ProgressDialog sProgressDialog;

    public SearchDataDbpedia(Context context, OnTaskCompleted onTaskCompleted, int typeSearch){
        this.mContext = context;
        this.mOnTaskCompleted = onTaskCompleted;
        this.mTypeSearch = typeSearch;
        sProgressDialog = new ProgressDialog(mContext);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(sProgressDialog == null){
            sProgressDialog = new ProgressDialog(mContext);
        }
        if(!sProgressDialog.isShowing()){
            sProgressDialog.setMessage("Loading...");
            sProgressDialog.show();
        }
    }

    public interface OnTaskCompleted{
        void onAsyncTaskCompletedDbpedia(ResultSet resultSet, int typeSearch);
    }

    @Override
    protected ResultSet doInBackground(String... params) {
        String queryString = params[0];
        Log.d(TAG, queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.createServiceRequest(Config.DBPEDIA_ENDPOINT, query);
        ResultSet resultSet = queryExecution.execSelect();
        queryExecution.close();
        return resultSet;
    }

    @Override
    protected void onPostExecute(ResultSet resultSet) {
        super.onPostExecute(resultSet);
        if(sProgressDialog.isShowing()){
            sProgressDialog.dismiss();
        }
        mOnTaskCompleted.onAsyncTaskCompletedDbpedia(resultSet, mTypeSearch);
    }
}

