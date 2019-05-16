package id.odt.simposiumasiaoceania2019.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pixplicity.fontview.FontAppCompatTextView;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.odt.simposiumasiaoceania2019.BaseApp;
import id.odt.simposiumasiaoceania2019.R;

public class LoginActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    @BindView(R.id.pbUpload)
    ProgressBar pbUpload;
    @BindView(R.id.tvCreateWallet)
    FontAppCompatTextView tvCreateWallet;
    @BindView(R.id.email_sign_in_button)
    LinearLayout mEmailSignInButton;
    @BindView(R.id.email)
    MaterialEditText mEmailView;
    @BindView(R.id.password)
    MaterialEditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        //showAllPanitia();
        //showAllPeserta();
        mEmailSignInButton.setOnClickListener(view -> attemptLogin());
    }

    private void attemptLogin() {

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if(password.isEmpty()) {
            mPasswordView.setError("Password can not be empty");
            return;
        }

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
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
            //showProgress(true);
            loginProcess();
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

    private void loginProcess() {
        tvCreateWallet.setText("Harap tunggu");
        pbUpload.setVisibility(View.VISIBLE);
        mEmailSignInButton.setEnabled(false);

        final String str_email = mEmailView.getText().toString();
        final String str_password = mPasswordView.getText().toString();

        BaseApp.mAuth.signInWithEmailAndPassword(str_email, str_password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = BaseApp.mAuth.getCurrentUser();
                        Intent in = new Intent(LoginActivity.this, MainActivity.class);
                        in.putExtra("email", str_email);
                        startActivity(in);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        BaseApp.mAuth.createUserWithEmailAndPassword(str_email, str_password)
                                .addOnCompleteListener(LoginActivity.this, task1 -> {
                                    if (task1.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("", "createUserWithEmail:success");
                                        FirebaseUser user = BaseApp.mAuth.getCurrentUser();
                                        loginProcess();
                                        //updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("", "createUserWithEmail:failure", task1.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                        tvCreateWallet.setVisibility(View.VISIBLE);
                                        pbUpload.setVisibility(View.GONE);
                                        mEmailSignInButton.setEnabled(true);
                                        //updateUI(null);
                                    }

                                    // ...
                                });
                    }

                    // ...
                });
    }

    private String user = "";
    private void showAllPeserta() {
        BaseApp.db
                .collection("user")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            String nama = documentSnapshot.getData().get("nama").toString();
                            String email = documentSnapshot.getData().get("email").toString();
                            String uid = documentSnapshot.getData().get("uid").toString();
                            user += nama+","+email+","+uid+"\n";
                        }

                        Log.d("allUser", user);
                    }
                });
    }

    private String panitia = "";
    private void showAllPanitia() {
        BaseApp.db
                .collection("panitia")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            String nama = documentSnapshot.getData().get("nama").toString();
                            String email = documentSnapshot.getData().get("email").toString();
                            String uid = documentSnapshot.getData().get("uid").toString();
                            panitia += nama+","+email+","+uid+"\n";
                        }

                        Log.d("allPanitia", panitia);
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("peserta", panitia);
                        clipboard.setPrimaryClip(clip);
                    }
                });
    }
}
