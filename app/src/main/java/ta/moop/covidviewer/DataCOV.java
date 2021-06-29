package ta.moop.covidviewer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataCOV {
    public String wilayah;
    public String positif;
    public String sembuh;
    public String meninggal;

    public  DataCOV(String wil, String pos, String sem, String men){
        wilayah=wil;
        positif=pos;
        sembuh=sem;
        meninggal=men;
    }

    public DataCOV(JSONObject json){
        try{
            this.wilayah = json.getJSONObject("attributes").getString("Provinsi");
            this.sembuh = json.getJSONObject("attributes").getString("Kasus_Semb");
            this.positif = json.getJSONObject("attributes").getString("Kasus_Posi");
            this.meninggal = json.getJSONObject("attributes").getString("Kasus_Meni");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<DataCOV> fromJson(JSONArray json) {
        ArrayList<DataCOV> data = new ArrayList<DataCOV>();
        for(int i=0;i< json.length();i++) {
            try {
                data.add(new DataCOV(json.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
