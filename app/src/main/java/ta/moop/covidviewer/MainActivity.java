package ta.moop.covidviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView idPos, idSe, idMe, idRa, log, wilPos, wilMe, wilSe, txtWil;
    String url ="https://api.kawalcorona.com/indonesia/";
    ArrayList<DataCOV> arrayOC;
    RequestQueue queue ;
    Spinner spinner;
    SharedPreferences sharedPref;
    SharedPreferences.Editor prefEdit;
    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy  HH:mm:ss", Locale.getDefault());

    StringRequest getData = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.d("volleyR",response);
                        Date c = Calendar.getInstance().getTime();
                        JSONArray json = new JSONArray(response);
                        JSONObject obj = json.getJSONObject(0);
                        idSe.setText(obj.getString("sembuh"));
                        idPos.setText(obj.getString("positif"));
                        idMe.setText(obj.getString("meninggal"));
                        idRa.setText(obj.getString("dirawat"));
                        log.setText("Updated at: "+ df.format(c));
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

    StringRequest getProvinsi = new StringRequest(Request.Method.GET, url+"provinsi/",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.d("volleyR",response);
                        JSONArray json = new JSONArray(response);
                        arrayOC = DataCOV.fromJson(json);
                        populateWil();
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
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        arrayOC = new ArrayList<DataCOV>();
        spinner= findViewById(R.id.spinner);
        idPos = findViewById(R.id.idnPos);
        idSe = findViewById(R.id.idnSem);
        idMe = findViewById(R.id.idnDie);
        idRa = findViewById(R.id.idnHos);
        log = findViewById(R.id.idnUp);
        wilMe = findViewById(R.id.wilDie);
        wilPos = findViewById(R.id.wilPos);
        wilSe = findViewById(R.id.wilSem);
        txtWil = findViewById(R.id.txtWil);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        prefEdit= sharedPref.edit();
        ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource(this,
                R.array.daerah, android.R.layout.simple_spinner_dropdown_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adp);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("spinner","ID : " + position);
                Log.d("spinner","VAL: "+spinner.getSelectedItem().toString());
                prefEdit.putInt("wilayah",position);
                prefEdit.apply();
                txtWil.setText(spinner.getSelectedItem().toString());
                populateWil();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(sharedPref.getInt("wilayah",0));
        idPos.setText("N/A");
        idSe.setText("N/A");
        idRa.setText("N/A");
        idMe.setText("N/A");
        txtWil.setText(spinner.getSelectedItem().toString());
        wilSe.setText("N/A");
        wilPos.setText("N/A");
        wilMe.setText("N/A");
        queue.add(getData);
        queue.add(getProvinsi);

    }

    public void populateWil(){
        if(arrayOC.size()>0){
            for(int i=0;i<arrayOC.size();i++){
                DataCOV x = arrayOC.get(i);
                Log.d("lopper",x.wilayah);
                if( x.wilayah.equals(spinner.getSelectedItem().toString())){
                    Log.d("lopperS",x.wilayah);
                    wilSe.setText(x.sembuh);
                    wilPos.setText(x.positif);
                    wilMe.setText(x.meninggal);
                    break;
                }
            }
        }
    }

    public void buttonListener(View v){
        switch (v.getId()){
            case R.id.btnRefresh:
                queue.add(getData);
                break;
            case R.id.btnAll:
                Intent provi =new Intent(this, provView.class);
                this.startActivity(provi);
                break;
        }
    }
}