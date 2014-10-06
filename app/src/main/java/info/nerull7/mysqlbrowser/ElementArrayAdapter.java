package info.nerull7.mysqlbrowser;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        init(context, resource, fields);
        values = new ArrayList<String>();
        for(String field: fields){
            values.add("");
        }
    }

    public ElementArrayAdapter(Context context, int resource, List<String> fields, List<String> values) {
        super(context, resource, fields);
        init(context, resource, fields);
        this.values = new ArrayList<String>();
        this.values.addAll(values); // Copy
    }

    private void init(Context context, int resource, List<String> fields){
        this.context = context;
        this.fields = fields;
        layout = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = layoutInflater.inflate(layout, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.textFieldName);
        textView.setText(fields.get(position));
        TextView textFieldName = (TextView) rowView.findViewById(R.id.editFieldValue);
        textFieldName.setText(values.get(position));
        textFieldName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String tmp = String.valueOf(((TextView) v).getText());
                    values.set(position, tmp);
                }
            }
        });
        return rowView;
    }

    public List<String> getFieldArray(){
        return fields;
    }

    public List<String> getValues() {
        return values;
    }
}
