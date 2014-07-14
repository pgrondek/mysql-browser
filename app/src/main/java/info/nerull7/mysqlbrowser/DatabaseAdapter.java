package info.nerull7.mysqlbrowser;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.HashMap;

/**
 * Created by nerull7 on 14.07.14.
 */
public class DatabaseAdapter extends ArrayAdapter<String>{
    HashMap<String, Integer> map = new HashMap<String, Integer>();

    public DatabaseAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    @Override
    public long getItemId(int position){
        String item = getItem(position);
        return map.get(item);
    }

    @Override
    public boolean hasStableIds(){
        return true;
    }
}
