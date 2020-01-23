package kheloindia.com.assessment.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

	private Context _context;

	public ConnectionDetector(Context context) {
		this._context = context;
	}

	public boolean isConnectingToInternet() {
		final ConnectivityManager connMgr = (ConnectivityManager) this._context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

		if (activeNetworkInfo != null) { // connected to the internet

			if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				// connected to wifi
				return true;
			} else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				// connected to the mobile provider's data plan
				return true;
			}
		}
		return false;
	}
}
