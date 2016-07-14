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
 * Created by HaiLeader on 7/5/2016.
 */
public class SearchDataLMD extends AsyncTask<String, Void, ResultSet> {

    public final static String TAG = "SearchData";
    private OnTaskCompleted mOnTaskCompleted;
    private int mTypeSearch;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    public SearchDataLMD(Context context, OnTaskCompleted onTaskCompleted, int typeSearch){
        this.mContext = context;
        this.mOnTaskCompleted = onTaskCompleted;
        this.mTypeSearch = typeSearch;
        mProgressDialog = new ProgressDialog(mContext);
    }

    public interface OnTaskCompleted {
        void onAsyncTaskCompletedLMD(ResultSet resultSet, int typeSearch);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
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
        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        mOnTaskCompleted.onAsyncTaskCompletedLMD(resultSet, mTypeSearch);
    }
}
