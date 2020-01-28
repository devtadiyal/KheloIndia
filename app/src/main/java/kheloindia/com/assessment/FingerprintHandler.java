package kheloindia.com.assessment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by whit3hawks on 11/16/16.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    TextView textView;

    // Constructor
    public FingerprintHandler(Context mContext, TextView tv) {
        context = mContext;
        textView = tv;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        String FingerPrintresult = result.toString();
        Log.e("FingerprintHandler","FingerPrintresult=> "+FingerPrintresult);
        ((Activity) context).finish();

        LoginActivity.CallMethodInLoginClass();
       /* Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);*/
    }

    private void update(String e){
        Toast.makeText(context, e, Toast.LENGTH_LONG).show();

        try {
            // TextView textView = (TextView) ((Activity) context).findViewById(R.id.errorText);
            textView.setText(e);
        } catch (Exception e1){
            e1.printStackTrace();
        }
    }

}
