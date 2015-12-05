package tr.edu.boun.cmpe.sculture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;

import static tr.edu.boun.cmpe.sculture.BaseApplication.baseApplication;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_LOGIN;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_REGISTER;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_ACCESS_TOKEN;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_EMAIL;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_ID;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_PASSWORD;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_USERNAME;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_LOGIN;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_REGISTER;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;
import static tr.edu.boun.cmpe.sculture.Utils.isEmailValid;
import static tr.edu.boun.cmpe.sculture.Utils.isPasswordValid;
import static tr.edu.boun.cmpe.sculture.Utils.isUserNameValid;
import static tr.edu.boun.cmpe.sculture.Utils.removeRequests;

public class LoginRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int STATUS_LOGIN = 1;
    private static final int STATUS_REGISTRATION = 2;
    private static int STATUS = STATUS_LOGIN;
    private final RequestQueue requestQueue = baseApplication.mRequestQueue;
    private TextInputLayout passwordConfirmationInputLayout;
    private TextInputLayout emailInputLayout;
    private TextInputLayout usernameInputLayout;
    private Button loginRegistrationButton;
    private Button loginRegistrationSwitchButton;
    private AutoCompleteTextView mEmailView;
    private EditText mNameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private LoginRegistrationActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);
        mActivity = this;
        if (baseApplication.checkLogin()) {
            Toast.makeText(this, R.string.already_logged, Toast.LENGTH_SHORT).show();
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


            removeRequests(REQUEST_TAG_LOGIN);
            removeRequests(REQUEST_TAG_REGISTER);

            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put(FIELD_EMAIL, email);
                requestBody.put(FIELD_PASSWORD, password);
                requestBody.put(FIELD_USERNAME, username);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            addRequest(API_USER_REGISTER, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String email = response.getString(FIELD_EMAIL);
                                String username = response.getString(FIELD_USERNAME);
                                String access_token = response.getString(FIELD_ACCESS_TOKEN);
                                long user_id = response.getLong(FIELD_ID);
                                baseApplication.setUserInfo(access_token, username, email, user_id);
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
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
                    },
                    REQUEST_TAG_REGISTER);
        }
    }

    private void clickLogin() {
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean isError = false;

        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            isError = true;
        }

        if (!isEmailValid(email)) {
            mNameView.setError(getString(R.string.error_field_required));
            isError = true;
        }
        if (!isError) {


            removeRequests(REQUEST_TAG_LOGIN);
            removeRequests(REQUEST_TAG_REGISTER);

            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put(FIELD_EMAIL, email);
                requestBody.put(FIELD_PASSWORD, password);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            addRequest(API_USER_LOGIN, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String email = response.getString(FIELD_EMAIL);
                                String username = response.getString(FIELD_USERNAME);
                                String access_token = response.getString(FIELD_ACCESS_TOKEN);
                                long user_id = response.getLong(FIELD_ID);
                                baseApplication.setUserInfo(access_token, username, email, user_id);
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
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
                    },
                    REQUEST_TAG_LOGIN);
        }
    }
}