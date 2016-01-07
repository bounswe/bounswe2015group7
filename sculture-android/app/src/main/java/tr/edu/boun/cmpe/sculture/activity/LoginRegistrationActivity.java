package tr.edu.boun.cmpe.sculture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.CallbackManager;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.models.response.ErrorResponse;
import tr.edu.boun.cmpe.sculture.models.response.LoginResponse;

import static tr.edu.boun.cmpe.sculture.BaseApplication.baseApplication;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_LOGIN;
import static tr.edu.boun.cmpe.sculture.Constants.API_USER_REGISTER;
import static tr.edu.boun.cmpe.sculture.Constants.ERROR_INVALID_EMAIL;
import static tr.edu.boun.cmpe.sculture.Constants.ERROR_INVALID_PASSWORD;
import static tr.edu.boun.cmpe.sculture.Constants.ERROR_INVALID_USERNAME;
import static tr.edu.boun.cmpe.sculture.Constants.ERROR_USER_ALREADY_EXISTS;
import static tr.edu.boun.cmpe.sculture.Constants.ERROR_USER_NOT_EXIST;
import static tr.edu.boun.cmpe.sculture.Constants.ERROR_WRONG_PASSWORD;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_EMAIL;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_PASSWORD;
import static tr.edu.boun.cmpe.sculture.Constants.FIELD_USERNAME;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_LOGIN;
import static tr.edu.boun.cmpe.sculture.Constants.REQUEST_TAG_REGISTER;
import static tr.edu.boun.cmpe.sculture.Utils.addRequest;
import static tr.edu.boun.cmpe.sculture.Utils.isEmailValid;
import static tr.edu.boun.cmpe.sculture.Utils.isPasswordValid;
import static tr.edu.boun.cmpe.sculture.Utils.isUserNameValid;
import static tr.edu.boun.cmpe.sculture.Utils.removeRequests;

/**
 * Login and registration screens
 */
public class LoginRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int STATUS_LOGIN = 1;
    private static final int STATUS_REGISTRATION = 2;
    private int STATUS = STATUS_LOGIN;
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
    private CallbackManager callbackManager;
    private LoginButton facebookLoginButton;

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_registration);
        mActivity = this;


        //init_facebook_login();

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

    /*private void init_facebook_login() {
        //Facebook Login
        facebookLoginButton = (LoginButton)findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));

        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginRegistrationActivity.this, "User cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginRegistrationActivity.this, "Error on Login, check your facebook app_id", Toast.LENGTH_LONG).show();
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
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
                            LoginResponse loginResponse = new LoginResponse(response);
                            baseApplication.setUserInfo(loginResponse.access_token, loginResponse.username, loginResponse.email, loginResponse.id);
                            Intent i = new Intent(mActivity.getApplicationContext(), MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ErrorResponse errorResponse = new ErrorResponse(error);

                            switch (errorResponse.message) {
                                case ERROR_USER_ALREADY_EXISTS:
                                    mEmailView.setError(getResources().getString(R.string.user_exist));
                                    break;
                                case ERROR_INVALID_EMAIL:
                                    mEmailView.setError(getString(R.string.error_invalid_email));
                                    break;
                                case ERROR_INVALID_PASSWORD:
                                    mPasswordView.setError(getString(R.string.error_invalid_password));
                                    break;
                                case ERROR_INVALID_USERNAME:
                                    mNameView.setError(getString(R.string.error_invalid_username));
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                                    Log.e("REGISTRATION", errorResponse.toString());
                                    break;
                            }
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
            mEmailView.setError(getString(R.string.error_invalid_email));
            isError = true;
        }
        if (!isError) {


            removeRequests(REQUEST_TAG_LOGIN);
            removeRequests(REQUEST_TAG_REGISTER);

            final JSONObject requestBody = new JSONObject();
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
                            LoginResponse loginResponse = new LoginResponse(response);
                            baseApplication.setUserInfo(loginResponse.access_token, loginResponse.username, loginResponse.email, loginResponse.id);
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ErrorResponse errorResponse = new ErrorResponse(error);

                            switch (errorResponse.message) {
                                case ERROR_USER_NOT_EXIST:
                                    mEmailView.setError(getResources().getString(R.string.user_not_exist));
                                    break;
                                case ERROR_INVALID_EMAIL:
                                    mEmailView.setError(getString(R.string.error_invalid_email));
                                    break;
                                case ERROR_INVALID_PASSWORD:
                                    mPasswordView.setError(getString(R.string.error_invalid_password));
                                    break;
                                case ERROR_WRONG_PASSWORD:
                                    mPasswordView.setError(getString(R.string.wrong_password));
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), R.string.error_occurred, Toast.LENGTH_SHORT).show();
                                    Log.e("LOGIN", errorResponse.toString());
                                    break;
                            }
                        }
                    },
                    REQUEST_TAG_LOGIN);
        }
    }
}