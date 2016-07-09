package vn.brine.haileader.musicjena.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

import vn.brine.haileader.musicjena.utils.Config;

/**
 * Created by HaiLeader on 7/7/2016.
 */
public class SearchDataWiki extends AsyncTask<String, Void, ResultSet> {

    public final static String TAG = "SearchDataFreeBase";
    private OnTaskCompleted mOnTaskCompleted;
    private int mTypeSearch;
    private Context mContext;
    private static ProgressDialog sProgressDialog;

    public SearchDataWiki(Context context, OnTaskCompleted onTaskCompleted, int typeSearch){
        this.mContext = context;
        this.mOnTaskCompleted = onTaskCompleted;
        this.mTypeSearch = typeSearch;
    }

    public interface OnTaskCompleted{
        void onAsyncTaskCompletedWiki(ResultSet resultSet, int typeSearch);
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

    @Override
    protected ResultSet doInBackground(String... params) {
        String queryString = params[0];
        Log.d(TAG, queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.createServiceRequest(Config.WIKIDATA_ENDPOINT, query);
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
        mOnTaskCompleted.onAsyncTaskCompletedWiki(resultSet, mTypeSearch);
    }
}
