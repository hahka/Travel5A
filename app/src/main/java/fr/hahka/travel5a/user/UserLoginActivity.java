package fr.hahka.travel5a.user;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.hahka.travel5a.Config;
import fr.hahka.travel5a.R;
import fr.hahka.travel5a.utils.StringUtils;


/**
 * A login screen that offers login via email/password.
 */
public class UserLoginActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = UserLoginActivity.class.getSimpleName();
    private static final int NO_USER_FOUND = 4002;
    private static final int INVALID_PASSWORD = 4003;
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mLoginView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mLoginView = (AutoCompleteTextView) findViewById(R.id.email);
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
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Tentative de connexion à BetaSeries
     * Affiche les erreurs quand elle arrivent
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reinitialise les erreurs
        mLoginView.setError(null);
        mPasswordView.setError(null);

        // Récupère les valeurs entrées pour la connexion
        String login = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        } else if (!isLoginValid(login)) {
            mLoginView.setError(getString(R.string.error_invalid_login));
            focusView = mLoginView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            mAuthTask = new UserLoginTask(login, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isLoginValid(String login) {
        return login.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(UserLoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mLoginView.setAdapter(adapter);
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;


        private String userId = null;
        /*String token = null;
        String tokenExpiration = null;*/
        private int error = -1;

        private int id;


        /**
         * Constructeur de la tache pour se connecter
         * @param email : l'email utilisateur
         * @param password : le mot de passe utilisateur
         */
        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = StringUtils.md5(password);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            userId = null;
            id = -1;
            //token = null;

            HttpPost httppost = new HttpPost("http://thibautvirolle.fr/projet5atravel/user/auth.php");
            List<NameValuePair> postParameters = new ArrayList<>();
            //On crée la liste qui contiendra tous nos paramètres
            //Et on y rajoute nos paramètres
            postParameters.add(new BasicNameValuePair("auth", "auth"));
            postParameters.add(new BasicNameValuePair("email", mEmail));
            postParameters.add(new BasicNameValuePair("password", mPassword));

            // Parsing json
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(entity);
                HttpClient httpclient = new DefaultHttpClient();

                HttpResponse response = httpclient.execute(httppost);
                InputStream is = response.getEntity().getContent();

                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }

                userId = total.toString();

                try {
                    id = Integer.parseInt(userId.trim());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                is.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return ((userId != null) && (id > 0));
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra(Config.USER_ID, userId);
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {

                switch (error) {
                    case NO_USER_FOUND:
                        mLoginView.setError(getString(R.string.error_no_user_found));
                        mLoginView.requestFocus();
                        break;

                    case INVALID_PASSWORD:
                        mPasswordView.setError(getString(R.string.error_invalid_password));
                        mPasswordView.requestFocus();
                        break;

                    default:
                        mLoginView.setError(getString(R.string.error_no_match));
                        mLoginView.requestFocus();
                        break;

                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }

    }
}



