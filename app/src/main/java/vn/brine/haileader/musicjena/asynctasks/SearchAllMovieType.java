package vn.brine.haileader.musicjena.asynctasks;

import android.os.AsyncTask;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import vn.brine.haileader.musicjena.utils.Config;

/**
 * Created by HaiLeader on 7/5/2016.
 */
public class SearchAllMovieType extends AsyncTask<Void, Void, String> {
    public static final String TAG = SearchAllMovieType.class.getName();

    private OnTaskCompleted mTaskCompletedListener;
    private ArrayList<String> mArrayListMovieType = new ArrayList<String>();

    private static final String TAG_RESULT = "results";
    private static final String TAG_BINDING = "bindings";
    private static final String TAG_O = "o";
    private static final String TAG_TYPE = "type";
    private static final String TAG_VALUE = "value";

    public SearchAllMovieType(OnTaskCompleted onTaskCompleted){
        this.mTaskCompletedListener = onTaskCompleted;
    }

    public interface OnTaskCompleted {
        void onAsyncTaskCompleted(ArrayList<String> arrayList);
    }

    @Override
    protected String doInBackground(Void... params) {

        String queryString =
                Config.PREFIX_LINKEDMDB +
                        "SELECT distinct ?o WHERE {?s rdf:type ?o}";

        Query queryAllTypeMovie = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.createServiceRequest(Config.LINKEDMDB_ENDPOINT, queryAllTypeMovie);
        ResultSet resultSet = queryExecution.execSelect();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ResultSetFormatter.outputAsJSON(outputStream, resultSet);
        String json = new String(outputStream.toByteArray());

        return json;
    }

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);
        try {
            mArrayListMovieType = parserJsonStringToArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mTaskCompletedListener.onAsyncTaskCompleted(mArrayListMovieType);
    }

    private ArrayList<String> parserJsonStringToArray(String json) throws JSONException {
        ArrayList<String> arrayList = new ArrayList<String>();

        if (json != null) {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray bindings = jsonObject.getJSONObject(TAG_RESULT).getJSONArray(TAG_BINDING);

            for (int i = 0; i < bindings.length(); i++) {
                JSONObject object = bindings.getJSONObject(i).getJSONObject(TAG_O);

                String value = object.getString(TAG_VALUE);
                String typeMovie = getLastNameOfUri(value);
                if (typeMovie != null) {
                    arrayList.add(typeMovie);
                }
            }
        }
        return arrayList;
    }

    private String getLastNameOfUri(String value) {
        String lastName = "";
        if (value == null) return null;

        String[] splitResult = value.split("/");
        lastName = splitResult[splitResult.length - 1];
        lastName = replaceUnderline(lastName);
        return lastName;
    }

    private String replaceUnderline(String lastName) {
        if (lastName != null) {
            lastName = lastName.replace("_", " ");
        }
        return lastName;
    }
}

