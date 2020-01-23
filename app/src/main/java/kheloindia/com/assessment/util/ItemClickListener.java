package kheloindia.com.assessment.util;

import android.view.View;

/**
 * Created by CT13 on 2017-05-11.
 */

// parent activity will implement this method to respond to click events
public interface ItemClickListener {
    void onItemClick(View view, int position);
}
