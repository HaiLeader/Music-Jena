package vn.brine.haileader.musicjena;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class MainActivity extends ListActivity {

    public static final String TAG = MainActivity.class.getName();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        new RequestTask().execute();
    }


    private class RequestTask extends AsyncTask<Void, Void, ResultSet> {

        @Override
        protected ResultSet doInBackground(Void... params) {
            String queryString = "" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "SELECT DISTINCT * WHERE {\n" +
                    "?s a owl:Class.\n" +
                    "?s rdfs:label ?label\n" +
                    "FILTER ( lang(?label) = \"en\" )\n" +
                    "} ORDER BY ?label LIMIT 1000\n";

//            String queryString =
//                    "SELECT ?resource WHERE { ?resource movie:filmid ?uri . ?resource dc:title \"Forrest Gump\" . }";
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.createServiceRequest("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            return results;
        }

        @Override
        protected void onPostExecute(ResultSet resultSet) {
            if(resultSet != null){
                while (resultSet.hasNext()) {
                    QuerySolution solution = resultSet.nextSolution();
                    String label = solution.getLiteral("label").getString();
                    if(label != null)
                        adapter.add(label);
                }
            }else{
                Log.d("FUCK", "NULL FUCK");
            }
        }
    }
}