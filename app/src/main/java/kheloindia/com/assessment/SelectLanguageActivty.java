package kheloindia.com.assessment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import kheloindia.com.assessment.util.Utility;

public class SelectLanguageActivty extends AppCompatActivity implements View.OnClickListener {

    private CheckBox eng_cb,hin_cb;
    private Button next_btn;
    private TextView preferred_lang_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_language_screen);
        eng_cb=(CheckBox) findViewById(R.id.eng_cb);
        hin_cb=(CheckBox)findViewById(R.id.hin_cb);
        next_btn=(Button)findViewById(R.id.next_btn);
        preferred_lang_tv=(TextView)findViewById(R.id.preferred_lang_tv);

        next_btn.setOnClickListener(this);
        hin_cb.setButtonDrawable(android.R.color.transparent);
        hin_cb.setTextColor(getResources().getColor(R.color.grey));
        eng_cb.setChecked(true);
        hin_cb.setOnClickListener(this);
        eng_cb.setOnClickListener(this);
       // preferred_lang_tv.setPaintFlags(preferred_lang_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Typeface font_semi_bold = Typeface.createFromAsset(getAssets(),
                "fonts/Barlow-SemiBold.ttf");
        next_btn.setTypeface(font_semi_bold);
        Utility.changeLanguage(this,"en");
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.next_btn:
//            if (eng_cb.isChecked()) {
//
//
//            } else {
//
//
//            }
                Intent i = new Intent(SelectLanguageActivty.this,KheloDashBoardActivity.class);
                startActivity(i);

                break;


            case R.id.eng_cb:
                hin_cb.setButtonDrawable(android.R.color.transparent);
                hin_cb.setTextColor(getResources().getColor(R.color.grey));
                eng_cb.setButtonDrawable(R.drawable.selected_i);
                eng_cb.setTextColor(getResources().getColor(R.color.white));
                Utility.changeLanguage(this,"en");
                preferred_lang_tv.setText(getString(R.string.pref_lan));
                next_btn.setText(getString(R.string.next));

                break;


            case R.id.hin_cb:
                eng_cb.setButtonDrawable(android.R.color.transparent);
                eng_cb.setTextColor(getResources().getColor(R.color.grey));
                hin_cb.setButtonDrawable(R.drawable.selected_i);
                hin_cb.setTextColor(getResources().getColor(R.color.white));
                Utility.changeLanguage(this,"hi");
                preferred_lang_tv.setText(getString(R.string.pref_lan));
                next_btn.setText(getString(R.string.next));

                break;

        }
    }
}
