package kheloindia.com.assessment;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView forgot_password_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();
    }

    private void init() {

        forgot_password_tv = (TextView) findViewById(R.id.forgot_password_tv);
        forgot_password_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==forgot_password_tv){
            Intent i = new Intent(MainActivity.this, ForgotPasswordActivity.class);
            startActivity(i);
        }
    }
}
