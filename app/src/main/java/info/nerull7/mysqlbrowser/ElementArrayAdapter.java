package info.nerull7.mysqlbrowser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nerull7 on 2014-08-06.
 */
public class ElementArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> values;
    private int layout;

    public ElementArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        values = objects;
        layout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(layout, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.textFieldName);
        textView.setText(values.get(position));

        return rowView;
    }
}
