package com.ess.essandroidbaselibrary;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ess.essandroidbaselibrary.communication.InternetConnection;
import com.ess.essandroidbaselibrary.util.uIUtilities.CustomToastMessage;
import com.ess.essandroidbaselibrary.util.uIUtilities.WaitingView;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
{
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView mSignUpLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        mSignUpLabel = (TextView) findViewById(R.id.signUnTextView);
//        mSignUpLabel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                mSignUpLabel.setTextColor(Color.BLUE);
//                Intent nextIntent = new Intent(LoginActivity.this, ProfileDataForm.class);
//                nextIntent.putExtra(ProfileDataForm.EXTRA_PROFILE_FORM_STATE, FormState.SIGN_UP.name());
//                startActivity(nextIntent);
//                finish();
//            }
//        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                attemptLogin();
            }
        });
    }

    private void populateAutoComplete()
    {
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS)
        {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString();

        // perform the user login attempt.
        if (!InternetConnection.getInstance(this).isNetworkAvailable())
        {
            CustomToastMessage.getInstance().showMessage(this,
                    CustomToastMessage.MessageType.ERROR,
                    CustomToastMessage.MessageTimeOut.SHORT,
                    LoginActivity.this.getResources().getString(R.string.error_app_no_internet));
            return;
        }

        if (email.isEmpty())
        {
            CustomToastMessage.getInstance().showMessage(this,
                    CustomToastMessage.MessageType.ERROR,
                    CustomToastMessage.MessageTimeOut.SHORT,
                    "Enter the email, contact number or your NIC");
            return;
        }

        if (password.isEmpty())
        {
            CustomToastMessage.getInstance().showMessage(this,
                    CustomToastMessage.MessageType.ERROR,
                    CustomToastMessage.MessageTimeOut.SHORT,
                    "Enter the password");
            return;
        }

        new LogInTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class LogInTask extends AsyncTask<Void,Void, Boolean>
    {
        String message = "";
        CustomToastMessage.MessageType messageType = CustomToastMessage.MessageType.ERROR;

        String email;
        String password;
        protected void onPreExecute()
        {
            WaitingView.getInstance().showWaitingActivity(LoginActivity.this);
            WaitingView.getInstance().setCancelable(false);

            // Store values at the time of the login attempt.

            email = mEmailView.getText().toString().trim();
            password = mPasswordView.getText().toString();
        }

        protected Boolean doInBackground(Void... v)
        {
//            ProfileDataModel res;
//
//            if (email.contains("@"))
//            {
//                // email
//                res = ServerDBManager.getInstance().getProfileDataForUserEmailAndPwd(email, password);
//            }
//            else
//            {
//                try
//                {
//                    String mobileNo = email.replace("-", "");
//                    Integer.parseInt(mobileNo);
//                    res = ServerDBManager.getInstance().getProfileDataForUserContactAndPwd(mobileNo, password);
//                }
//                catch (NumberFormatException e)
//                {
//                    res = ServerDBManager.getInstance().getProfileDataForUserNICAndPwd(email, password);
//                }
//            }

//            if (res != null)
//            {
//                Configuration.getInstance().getApplicationData().setUserProfile(res);
//                PrivateDatabaseConnection.getInstance(LoginActivity.this).insertOrUpdateAppData(
//                        Configuration.getInstance().getApplicationData());

//                message = "Log-in success!";
//                messageType = CustomToastMessage.MessageType.INFO;
//                return Boolean.TRUE;
//            }
//            else
//            {
//               message = "User name or password invalid!";
//            }
            return Boolean.FALSE;
        }

        protected void onPostExecute(Boolean result)
        {
            // close waiting view
            WaitingView.getInstance().closeWaitingActivity();

            // show message
            if (!message.isEmpty())
            {
                CustomToastMessage.getInstance().showMessage(LoginActivity.this,
                        messageType,
                        CustomToastMessage.MessageTimeOut.LONG,
                        message);
            }

            // do needful changes in UI
            if (result == Boolean.TRUE)
            {
                // Start next activity if needed
//                Intent intent = new Intent(LoginActivity.this, IsolatedConductorActivity.class);
//                startActivity(intent);
//                finish();
            }
        }
    }
}

