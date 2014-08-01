package info.nerull7.mysqlbrowser;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by nerull7 on 25.07.14.
 */
public class CustomScrollView extends ScrollView {

    private OnTouchEventListener onTouchEventListener;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(onTouchEventListener != null){
            onTouchEventListener.onTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    public void setOnTouchEventListener(OnTouchEventListener onTouchEventListener){
        this.onTouchEventListener = onTouchEventListener;
    }

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    public interface OnTouchEventListener {
        public boolean onTouchEvent(MotionEvent ev);
    }
}
