package info.nerull7.mysqlbrowser;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by nerull7 on 25.07.14.
 */
public class CustomScrollView extends ScrollView {

    private OnScrollChangedListener listener;

    public void setOnScrollChangedListener(OnScrollChangedListener listener){
        this.listener = listener;
    }

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(listener!=null)
            listener.onScrollChanged(l,t,oldl,oldt);
    }

    public interface OnScrollChangedListener{
        public void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
