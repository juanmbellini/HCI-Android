package hci.tiendapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import hci.tiendapp.R;
import hci.tiendapp.TiendApp;
import hci.tiendapp.backend.Account;
import hci.tiendapp.backend.Notificator;
import hci.tiendapp.constants.Constants;
import hci.tiendapp.util.UtilClass;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends MyDrawerActivity implements LoaderCallbacks<Cursor> {

    public LoginActivity() {
        super(R.layout.activity_login, R.id.login_layout);
        super.setContext(this);
    }

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private TextInputLayout userNameWrapper;
    private TextInputLayout passwordWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText password = (EditText) findViewById(R.id.password);
        password.setTransformationMethod(new PasswordTransformationMethod());

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        userNameWrapper = (TextInputLayout) findViewById(R.id.user_name_wrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.password_wrapper);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            startService(new Intent(LoginActivity.this, Notificator.class));
            return;
        }

        // Reset errors.
        //userNameWrapper.setError(null);
        //passwordWrapper.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {


            passwordWrapper.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            userNameWrapper.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isUserNameValid(email)) {
            userNameWrapper.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute(email,password);
        }
    }

    private boolean isUserNameValid(String username) {
        //TODO: Replace this with your own logic
        return true;//Pattern.matches("/^([a-zA-Z0-9]){6,15}$/", username);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<String, Void, Account> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        private String login(String username, String password) {

            String requestURL = "http://eiffel.itba.edu.ar/hci/service3/Account.groovy?method=SignIn&username=";
            requestURL += username + "&password=" + password;

            System.out.println(requestURL);

            URL url = null;
            try {
                url = new URL(requestURL);      // Creates an URL object based on the filter sent
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();   // Get's data from API
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();                     // Finishes connection
                }
                System.out.println("Disconnected");
            }

            String token = "";
            String account = "";
            String error = "";
            JSONObject aux;
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String result = UtilClass.readStream(in);
                System.out.println(result);
                aux = new JSONObject(result);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;

            }


            try {
                token = aux.getString("authenticationToken");
                account = aux.getString("account");
            } catch (JSONException e) {
                try {
                    error = aux.getString("error");

                } catch (JSONException j) {
                    j.printStackTrace();
                    return null;
                }
                return "error:" + error;
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.authenticationToken, token);
            editor.commit();
            return account;



        }

        @Override
        protected Account doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            /*for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }
            // TODO: register the new account here.
            return false;
            /*
          */
            String result = login(params[0], params[1]);
            if (result.contains("error")) {
                if (result.contains("\"code\":104")) {

                    return new Account(-1,null,null,null,null,null,null,null);
                }
                if (result.contains("\"code\":105")) {
                    return new Account(-2,null,null,null,null,null,null,null);
                }
            }
            return new Gson().fromJson(result,Account.class);


        }

        @Override
        protected void onPostExecute(final Account account) {

            showProgress(false);


            if (account == null) {
                return;
            }

            Boolean cancel = false;
            if(account.getId() == -1) {

                userNameWrapper.setError(getString(R.string.error_invalid_email));
                mEmailView.requestFocus();
                cancel = true;
            }

            if (account.getId() == -2) {

                passwordWrapper.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                cancel = true;
            }

            if (cancel) {
                return;
            }

            saveAccount(account);
            setAccountAsDefault(account);
            finish();

        }

        private void saveAccount(Account account) {

            Set<Account> accounts = new HashSet<Account>();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            String savedAccounts = preferences.getString(Constants.accounts, "");

            if(!savedAccounts.equals("")) {
                Gson parser = new Gson();
                Type dataSetListType = new TypeToken<Set<Account>>() {}.getType();
                accounts = parser.fromJson(savedAccounts, dataSetListType);
            }

            accounts.add(account);

            savedAccounts = new Gson().toJson(accounts);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.accounts, savedAccounts);
            editor.commit();

        }

        private void setAccountAsDefault(Account account) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            SharedPreferences.Editor editor = preferences.edit();
            String stringifyAccount = new Gson().toJson(account);
            editor.putString(Constants.lastLoginAccount, stringifyAccount);
            editor.commit();



        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

