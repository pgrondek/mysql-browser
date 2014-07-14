package info.nerull7.mysqlbrowser;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by nerull7 on 14.07.14.
 */
public class TableFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_table, container, false);
        TextView text = (TextView) rootView.findViewById(R.id.dumbText);
        text.setText(getArguments().getString("DatabaseName"));
        return rootView;
    }
}
