package ta.moop.covidviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class provView extends AppCompatActivity {

    ArrayList<DataCOV> arrayOfCovid;
    DataAdapter adapter;
    ListView listview;
    String url ="https://api.kawalcorona.com/indonesia/provinsi/";
    RequestQueue queue ;

    StringRequest getProvinsi = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.d("volleyR",response);
                        JSONArray json = new JSONArray(response);
                        ArrayList<DataCOV> nd = DataCOV.fromJson(json);
                        adapter.clear();
                        adapter.addAll(nd);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volleyE",error.toString());
            }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prov_view);
        arrayOfCovid = new ArrayList<DataCOV>();
        adapter= new DataAdapter(this,R.layout.listitems, arrayOfCovid);
        listview = findViewById(R.id.listview);
        listview.setAdapter(adapter);
        queue = Volley.newRequestQueue(this);
        queue.add(getProvinsi);
    }
}