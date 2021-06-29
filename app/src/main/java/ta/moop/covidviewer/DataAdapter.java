package ta.moop.covidviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class DataAdapter extends ArrayAdapter<DataCOV> {

    int resource;
    public DataAdapter(Context context, int resource, ArrayList<DataCOV> items) {
        super(context, resource, items);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        DataCOV item = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource,parent,false);
        }
        TextView wil = convertView.findViewById(R.id.txtWil);
        TextView pos = convertView.findViewById(R.id.txtPos);
        TextView sem = convertView.findViewById(R.id.txtSembuh);
        TextView die = convertView.findViewById(R.id.txtDie);

        wil.setText(item.wilayah);
        pos.setText(item.positif);
        sem.setText(item.sembuh);
        die.setText(item.meninggal);

        return convertView;
    }
}
