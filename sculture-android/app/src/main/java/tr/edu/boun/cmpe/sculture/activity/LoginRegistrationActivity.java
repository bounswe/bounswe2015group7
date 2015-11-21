package tr.edu.boun.cmpe.sculture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.requests.JsonObjectWithParamsRequest;

import static tr.edu.boun.cmpe.sculture.BaseApplication.baseApplication;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_LOGIN;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_REGISTER;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_ACCESS_TOKEN;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_EMAIL;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_PASSWORD;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_USERNAME;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_LOGIN;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_REGISTER;
import static tr.edu.boun.cmpe.sculture.Utils.*;

public class LoginRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int STATUS_LOGIN = 1;
    private static final int STATUS_REGISTRATION = 2;
    private static int STATUS = STATUS_LOGIN;

    private TextInputLayout passwordConfirmationInputLayout;
    private TextInputLayout emailInputLayout;
    private TextInputLayout usernameInputLayout;
    private Button loginRegistrationButton;
    private Button loginRegistrationSwitchButton;
    private AutoCompleteTextView mEmailView;
    private EditText mNameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;

    private RequestQueue requestQueue = baseApplication.mRequestQueue;

    LoginRegistrationActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);
        mActivity = this;
        if (baseApplication.checkLogin()) {
            Toast.makeText(this, "All ready logged in", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            return;
        }

        passwordConfirmationInputLayout = (TextInputLayout) findViewById(R.id.passwordConfirmInputLayout);
        emailInputLayout = (TextInputLayout) findViewById(R.id.emailInputLayout);
        usernameInputLayout = (TextInputLayout) findViewById(R.id.usernameInoutLayout);
        loginRegistrationButton = (Button) findViewById(R.id.loginRegistrationButton);
        loginRegistrationSwitchButton = (Button) findViewById(R.id.loginRegistrationSwitchButton);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mNameView = (EditText) findViewById(R.id.username);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirmPassword);
        mPasswordView = (EditText) findViewById(R.id.password);

        loginRegistrationButton.setOnClickListener(this);
        loginRegistrationSwitchButton.setOnClickListener(this);

        setVisibilities();
    }

    protected void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(REQUEST_TAG_REGISTER);
        requestQueue.cancelAll(REQUEST_TAG_LOGIN);
    }

    private void setVisibilities() {
        if (STATUS == STATUS_LOGIN) {
            usernameInputLayout.setVisibility(View.GONE);
            passwordConfirmationInputLayout.setVisibility(View.GONE);
            emailInputLayout.setVisibility(View.VISIBLE);
            loginRegistrationButton.setText(R.string.login);
            loginRegistrationSwitchButton.setText(R.string.wantRegister);
        } else if (STATUS == STATUS_REGISTRATION) {
            usernameInputLayout.setVisibility(View.VISIBLE);
            passwordConfirmationInputLayout.setVisibility(View.VISIBLE);
            emailInputLayout.setVisibility(View.VISIBLE);
            loginRegistrationButton.setText(R.string.action_register);
            loginRegistrationSwitchButton.setText(R.string.wantLogin);
        }
    }


    //region Clicks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginRegistrationButton:
                if (STATUS == STATUS_LOGIN)
                    clickLogin();
                else if (STATUS == STATUS_REGISTRATION)
                    clickRegistration();
                break;
            case R.id.loginRegistrationSwitchButton:
                clickLoginRegistrationSwitch();
                break;
        }
    }

    private void clickLoginRegistrationSwitch() {
        if (STATUS == STATUS_LOGIN) {
            STATUS = STATUS_REGISTRATION;
            setVisibilities();
        } else if (STATUS == STATUS_REGISTRATION) {
            STATUS = STATUS_LOGIN;
            setVisibilities();
        }
    }

    private void clickRegistration() {
        String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        String password_confirmation = mConfirmPasswordView.getText().toString();
        final String username = mNameView.getText().toString();

        boolean isError = false;

        //PASSWORD
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            isError = true;
        } else if (!password.equals(password_confirmation)) {
            mPasswordView.setError(getString(R.string.error_unmatched_password));
            isError = true;
        }
        //EMAIL
        if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            isError = true;
        }
        // USERNAME
        if (!isUserNameValid(username)) {
            mNameView.setError(getString(R.string.error_invalid_username));
            isError = true;
        }

        if (!isError) {
            HashMap<String, String> params = new HashMap<>();
            params.put(FIELD_EMAIL, email);
            params.put(FIELD_PASSWORD, password);
            params.put(FIELD_USERNAME, username);


            requestQueue.cancelAll(REQUEST_TAG_REGISTER);
            requestQueue.cancelAll(REQUEST_TAG_LOGIN);

            JsonObjectWithParamsRequest registerRequest = new JsonObjectWithParamsRequest(Request.Method.POST, API_USER_REGISTER, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String email = response.getString(FIELD_EMAIL);
                                String username = response.getString(FIELD_USERNAME);
                                String access_token = response.getString(FIELD_ACCESS_TOKEN);
                                baseApplication.setUserInfo(access_token, username, email);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //TODO Error handling
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //TODO Error handling
                            Toast.makeText(mActivity, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
            registerRequest.setTag(REQUEST_TAG_REGISTER);


            requestQueue.add(registerRequest);
        }
    }

    private void clickLogin() {
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean isError = false;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            isError = true;
        }

        if (!isEmailValid(email)) {
            mNameView.setError(getString(R.string.error_field_required));
            isError = true;
        }
        if (!isError) {
            requestQueue.cancelAll(REQUEST_TAG_REGISTER);
            requestQueue.cancelAll(REQUEST_TAG_LOGIN);
            HashMap<String, String> params = new HashMap<>();
            params.put(FIELD_EMAIL, email);
            params.put(FIELD_PASSWORD, password);
            JsonObjectWithParamsRequest loginRequest = new JsonObjectWithParamsRequest(Request.Method.POST, API_USER_LOGIN, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String email = response.getString(FIELD_EMAIL);
                                String username = response.getString(FIELD_USERNAME);
                                String access_token = response.getString(FIELD_ACCESS_TOKEN);
                                baseApplication.setUserInfo(access_token, username, email);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } catch (JSONException e) {
                                //TODO Error handling
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //TODO Error handling
                            Toast.makeText(mActivity, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
            loginRequest.setTag(REQUEST_TAG_LOGIN);
            requestQueue.add(loginRequest);
        }
    }
}