package tr.edu.boun.cmpe.sculture.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import tr.edu.boun.cmpe.sculture.BaseApplication;
import tr.edu.boun.cmpe.sculture.R;


public class LoginRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int STATUS_LOGIN = 1;
    private static final int STATUS_REGISTRATION = 2;
    private static int STATUS = STATUS_LOGIN;

    TextInputLayout passwordTextInputLayout;
    TextInputLayout passwordConfirmationInputLayout;
    TextInputLayout emailInputLayout;
    TextInputLayout usernameInputLayout;
    Button loginRegistrationButton;
    Button loginRegistrationSwitchButton;
    private AutoCompleteTextView mEmailView;
    private EditText mNameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);

        if (ParseUser.getCurrentUser() != null) {
            Toast.makeText(this, "All ready logged in", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            return;
        }


        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordInputLayout);
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

    private void setVisibilities() {
        if (STATUS == STATUS_LOGIN) {
            passwordConfirmationInputLayout.setVisibility(View.GONE);
            emailInputLayout.setVisibility(View.GONE);
            loginRegistrationButton.setText(R.string.login);
            loginRegistrationSwitchButton.setText(R.string.wantRegister);
        } else if (STATUS == STATUS_REGISTRATION) {
            passwordConfirmationInputLayout.setVisibility(View.VISIBLE);
            emailInputLayout.setVisibility(View.VISIBLE);
            loginRegistrationButton.setText(R.string.action_register);
            loginRegistrationSwitchButton.setText(R.string.wantLogin);
        }
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isUserNameValid(String username) {
        //TODO Replace
        return username.length() < 3;
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

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            isError = true;
        } else if (!password.equals(password_confirmation)) {
            mPasswordView.setError(getString(R.string.error_unmatched_password));
            isError = true;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            isError = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            isError = true;
        } else if (isUserNameValid(username)) {
            mNameView.setError(getString(R.string.error_invalid_username));
            isError = true;
        }

        if (!isError) {
            final ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        SharedPreferences settings = getSharedPreferences(BaseApplication.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(BaseApplication.PREF_USERNAME, username);
                        editor.putString(BaseApplication.PREF_PASSWORD, password);
                        editor.apply();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void clickLogin() {
        final String username = mNameView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean isError = false;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            isError = true;
        }

        if (!isUserNameValid(username)) {
            mNameView.setError(getString(R.string.error_field_required));
            isError = true;
        }
        if (!isError) {
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        SharedPreferences settings = getSharedPreferences(BaseApplication.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(BaseApplication.PREF_USERNAME, username);
                        editor.putString(BaseApplication.PREF_PASSWORD, password);
                        editor.apply();

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
