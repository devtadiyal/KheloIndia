package kheloindia.com.assessment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kheloindia.com.assessment.adapter.ListViewDialogAdapter;
import kheloindia.com.assessment.model.CreateProfileModel;
import kheloindia.com.assessment.model.ListViewDialogModel;
import kheloindia.com.assessment.util.ConnectionDetector;
import kheloindia.com.assessment.util.DBManager;
import kheloindia.com.assessment.util.ResponseListener;
import kheloindia.com.assessment.util.Utility;
import kheloindia.com.assessment.webservice.CreateProfileRequest;

import kheloindia.com.assessment.functions.Constant;

public class CreateProfileActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, ResponseListener, CompoundButton.OnCheckedChangeListener {
    private EditText name_et, email_et, phone_et, address_et, state_et, city_et, district_et, block_et, qualification_et, tot_code_et;
    private RadioGroup gender_rg;
    private RadioButton male_rb, female_rb, trans_gender_rb;
    private CheckBox terms_condition_cbx, above_eighteen_years_cbx, attended_tot_cbx;
    private Button save_details_btn;
    private Toolbar toolbar;
    private String name_text, email_text, phone_text, qualification_text, address_text, city_text, gender_text, district_txt, block_txt, tot_code_text = "";
    private String message;
    private ConnectionDetector connectionDetector;
    private ImageView info_iv;
    private LinearLayout tot_code_layout;
    ListView statesListView;
    ListViewDialogAdapter statesListAdapter;
    ArrayList<ListViewDialogModel> statesArrayList = new ArrayList<>();
    AlertDialog statesAlertDialog;
    int selectedStateId = 0;
    String selectedStateName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        init();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Create Profile");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Utility.showActionBar(this, toolbar, "");
        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText("Create Profile");

        name_et = (EditText) findViewById(R.id.name_et);
        email_et = (EditText) findViewById(R.id.email_et);
        phone_et = (EditText) findViewById(R.id.phone_et);
        address_et = (EditText) findViewById(R.id.address_et);
        state_et = (EditText) findViewById(R.id.state_et);
        city_et = (EditText) findViewById(R.id.city_et);
        district_et = (EditText) findViewById(R.id.district_et);
        block_et = (EditText) findViewById(R.id.block_et);
        gender_rg = (RadioGroup) findViewById(R.id.gender_rg);
        male_rb = (RadioButton) findViewById(R.id.male_rb);
        female_rb = (RadioButton) findViewById(R.id.female_rb);
        trans_gender_rb = (RadioButton) findViewById(R.id.trans_gender_rb);
        qualification_et = (EditText) findViewById(R.id.qualification_et);
        save_details_btn = (Button) findViewById(R.id.save_details_btn);
        terms_condition_cbx = (CheckBox) findViewById(R.id.terms_condition_cbx);
        above_eighteen_years_cbx = (CheckBox) findViewById(R.id.above_eighteen_years_cbx);
        info_iv = (ImageView) findViewById(R.id.info_iv);
        attended_tot_cbx = (CheckBox) findViewById(R.id.attended_tot);
        tot_code_layout = (LinearLayout) findViewById(R.id.tot_code_layout);
        tot_code_et = (EditText) findViewById(R.id.tot_code);
        //stateLayout = (LinearLayout) findViewById(R.id.state_layout);
        //stateParentLayout = (LinearLayout) findViewById(R.id.state_parent_layout);

        save_details_btn.setOnClickListener(this);
        info_iv.setOnClickListener(this);
        gender_rg.setOnCheckedChangeListener(this);
        state_et.setOnClickListener(this);
        attended_tot_cbx.setOnCheckedChangeListener(this);
        //stateParentLayout.setOnClickListener(this);
        //stateLayout.setOnClickListener(this);

        male_rb.setChecked(true);
        connectionDetector = new ConnectionDetector(this);
        getStates();

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        int id = radioGroup.getCheckedRadioButtonId();

        switch (id) {

            case R.id.male_rb:
                gender_text = "0";
                break;

            case R.id.female_rb:
                gender_text = "1";
                break;

            case R.id.trans_gender_rb:
                gender_text = "2";
                break;

        }
    }

    private void getStates() {
        ArrayList objectArrayList = DBManager.getInstance().getAllTableData(this, DBManager.TBL_STATE_MASTER, "", "", "", "");
        if (objectArrayList.size() > 0) {
            for (Object object : objectArrayList) {
                ListViewDialogModel listViewDialogModel = (ListViewDialogModel) object;
                statesArrayList.add(new ListViewDialogModel(listViewDialogModel.getListItemId(), listViewDialogModel.getListItemTitle(), false));
            }
        }
    }

    private void showStatesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.list_view_pop_up_layout, null);
        builder.setView(view);
        statesListView = view.findViewById(R.id.list_view);
        statesListAdapter = new ListViewDialogAdapter(this, statesArrayList);
        statesListView.setAdapter(statesListAdapter);

        if (selectedStateId != 0) {
            statesListView.setSelection(selectedStateId - 1);
        }

        selectState();

        statesAlertDialog = builder.create();
        statesAlertDialog.show();
    }

    private void selectState() {
        statesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < statesArrayList.size(); j++) {
                    if (i == j) {
                        if (!statesArrayList.get(i).isItemSelected()) {
                            statesArrayList.get(i).setItemSelected(true);
                        }
                    } else {
                        statesArrayList.get(j).setItemSelected(false);
                    }
                }

                statesListAdapter.notifyDataSetChanged();
                state_et.setText(statesArrayList.get(i).getListItemTitle());
                selectedStateId = statesArrayList.get(i).getListItemId();
                statesAlertDialog.dismiss();
            }
        });

    }

    private boolean validate() {

        name_text = name_et.getText().toString().trim();
        email_text = email_et.getText().toString().trim();
        phone_text = phone_et.getText().toString().trim();
        qualification_text = qualification_et.getText().toString().trim();
        //address_text=address_et.getText().toString().trim();
        city_text = city_et.getText().toString().trim();
        district_txt = district_et.getText().toString().trim();
        block_txt = block_et.getText().toString().trim();
        if (attended_tot_cbx.isChecked())
            tot_code_text = tot_code_et.getText().toString().trim();


        if (name_text.length() < 1 || !name_text.matches("[a-zA-Z ]+")) {
            name_et.requestFocus();
            name_et.setError(getString(R.string.validate_your_name));
            return false;
        } else if (email_text.length() < 1) {
            email_et.requestFocus();
            email_et.setError(getString(R.string.validate_email_id_not_empty));
            return false;
        } else if (!Constant.verify(email_text, CreateProfileActivity.this)) {
            email_et.requestFocus();
            email_et.setError(getString(R.string.validate_email_id));
            return false;
        } else if (phone_text.length() != 10) {
            phone_et.requestFocus();
            phone_et.setError(getString(R.string.validate_phone_number_not_empty));
            return false;

        }
//        else if(address_text.length()<1){
//            address_et.requestFocus();
//            address_et.setError(getString(R.string.validate_address_not_empty));
//            return false;
//
//        }
        else if (selectedStateId == 0) {
//            state_et.requestFocus();
//            state_et.setError(getString(R.string.validate_state_not_empty));
            Toast.makeText(this, getString(R.string.validate_state_not_empty), Toast.LENGTH_LONG).show();
            return false;
        } else if (district_txt.length() < 3 || !district_txt.matches("[a-zA-Z ]+")) {
            district_et.requestFocus();
            district_et.setError(getString(R.string.validate_string_not_empty, "District"));
            return false;
        } else if (city_text.length() < 3 || !city_text.matches("[a-zA-Z ]+")) {
            city_et.requestFocus();
            city_et.setError(getString(R.string.validate_string_not_empty, "City"));
            return false;
        } else if (block_txt.length() < 1) {
            block_et.requestFocus();
            block_et.setError(getString(R.string.validate_string_not_empty, "Block"));
            return false;
        } else if (qualification_text.length() < 1) {
            qualification_et.requestFocus();
            qualification_et.setError(getString(R.string.validate_qualification_not_empty));
            return false;

        } else if (attended_tot_cbx.isChecked() && tot_code_text.length() < 1) {
            tot_code_et.requestFocus();
            tot_code_et.setError(getString(R.string.validate_string_not_empty, "TOT Code"));
            return false;
        } else if (!terms_condition_cbx.isChecked() || !above_eighteen_years_cbx.isChecked()) {
            Toast.makeText(this, R.string.validate_terms_and_conditions, Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_details_btn:
                if (connectionDetector.isConnectingToInternet()) {
                    if (validate()) {
                        CreateProfileRequest createProfileRequest = new CreateProfileRequest
                                (this, name_text, email_text, gender_text, phone_text,
                                        address_et.getText().toString().trim(),
                                        qualification_text, city_text, selectedStateId, district_txt,
                                        block_txt, String.valueOf(attended_tot_cbx.isChecked()), tot_code_text, this);
                        createProfileRequest.hitUserRequest();
                    }
                } else
                    Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();

                break;

            case R.id.info_iv:
                startActivity(new Intent(this, WebViewPdf.class));
                break;

            case R.id.state_et:
                showStatesDialog();
                break;

        }

    }

    @Override
    public void onResponse(Object obj) {
        if (obj instanceof CreateProfileModel) {
            CreateProfileModel createProfileModel = (CreateProfileModel) obj;
            if (createProfileModel.getIsSuccess().equalsIgnoreCase("true")) {
                message = "Registration successful and your trainer-id is " + createProfileModel.getResult();
                showDataSaveDialog(message, true);

            } else {
                // message="Registration failed as "+createProfileModel.getMessage();
                message = createProfileModel.getMessage();
                showDataSaveDialog(message, false);
            }

            name_et.setText("");
            email_et.setText("");
            male_rb.setChecked(true);
            phone_et.setText("");
            address_et.setText("");
            city_et.setText("");
            district_et.setText("");
            block_et.setText("");
            state_et.setText("");
            tot_code_et.setText("");
            tot_code_layout.setVisibility(View.GONE);
            name_et.requestFocus();
            attended_tot_cbx.setChecked(false);
            if (selectedStateId != 0) {
                statesArrayList.get(selectedStateId - 1).setItemSelected(false);
                statesListAdapter.notifyDataSetChanged();
                selectedStateId = 0;
            }
            qualification_et.setText("");
        } else
            Toast.makeText(this, R.string.unable_connect_server, Toast.LENGTH_SHORT).show();
    }

    private void resetFields() {

    }


    private void showDataSaveDialog(String message, final boolean isRegistered) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Registration Status");
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.alarm);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isRegistered)
                    CreateProfileActivity.super.onBackPressed();
            }
        });

        alertDialog.show();


    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.attended_tot) {
            if (b)
                tot_code_layout.setVisibility(View.VISIBLE);
            else {
                tot_code_layout.setVisibility(View.GONE);
                tot_code_et.setText("");
                tot_code_et.clearFocus();
                tot_code_et.setError(null);
                Utility.hideKeyboard(this);
            }
        }
    }
}
