package kheloindia.com.assessment.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import kheloindia.com.assessment.R;

public class ProgressDialogUtility {
	Activity activity1;
	private ProgressDialog pDialog;

	public ProgressDialogUtility(Activity activity) {

		this.activity1 = activity;
		pDialog = new ProgressDialog(activity1);
		pDialog.setMessage(activity.getString(R.string.progress_loader));
		pDialog.setCancelable(false);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				pDialog.dismiss();
				activity1.finish();

			}
		});
	}

	public void setMessage(String message) {
		pDialog.setMessage(message);
	}


	public void showProgressDialog() {

		if (pDialog == null) {
			pDialog = new ProgressDialog(activity1);
			pDialog.setMessage("Loading the data please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
		}
		pDialog.show();
	}

	public void dismissProgressDialog() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
		}
	}

}
