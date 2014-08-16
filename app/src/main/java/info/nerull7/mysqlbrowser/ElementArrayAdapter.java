package info.nerull7.mysqlbrowser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nerull7 on 2014-08-06.
 */
public class ElementArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> fields;
    private List<String> values;
    private int layout;

    public ElementArrayAdapter(Context context, int resource, List<String> fields) {
        super(context, resource, fields);
        this.context = context;
        this.fields = fields;
        layout = resource;
        values = null;
    }

    public ElementArrayAdapter(Context context, int resource, List<String> fields, List<String> values) {
        this(context, resource, fields);
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(layout, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.textFieldName);
        textView.setText(fields.get(position));
        if(values != null){
            EditText textFieldName = (EditText) rowView.findViewById(R.id.editFieldValue);
            textFieldName.setText(values.get(position));
        }

        return rowView;
    }

    public List<String> getFieldArray(){
        return fields;
    }

    public List<String> getValueArray(){
        return values;
    }
}
