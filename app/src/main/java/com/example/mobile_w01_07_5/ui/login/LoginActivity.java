package com.example.mobile_w01_07_5.ui.login;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mobile_w01_07_5.MainActivity;
import com.example.mobile_w01_07_5.R;
import com.example.mobile_w01_07_5.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String debug_message = "from Login Activity: ";

    // declare auth
    private FirebaseAuth mAuth;

    private ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // Buttons
        mBinding.emailSignInButton.setOnClickListener(this);
        mBinding.emailCreateAccountButton.setOnClickListener(this);
        mBinding.signOutButton.setOnClickListener(this);  // move to profile activity
        mBinding.verifyEmailButton.setOnClickListener(this );
        mBinding.reloadButton.setOnClickListener(this);

        // initialize Firebase auth
        mAuth = FirebaseAuth.getInstance();

    }

    // check user
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    // end check user

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.emailCreateAccountButton) {
            createAccount(mBinding.fieldEmail.getText().toString(), mBinding.fieldPassword.getText().toString());
            hideKeyboard(v);
        } else if (i == R.id.emailSignInButton) {
            signIn(mBinding.fieldEmail.getText().toString(), mBinding.fieldPassword.getText().toString());
            hideKeyboard(v);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (i == R.id.signOutButton) {
            signOut();
        } else if (i == R.id.verifyEmailButton) {
            sendEmailVerification();
        } else if (i == R.id.reloadButton) {
            reload();
            if (mAuth.getCurrentUser().isEmailVerified()) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    public void createAccount(String email, String password) {
        Log.d(debug_message, "createAccount - " + email);
        if (!validateForm()) { return; }
        showProgressBar();

        // create user from email
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // sign in success
                    Log.d(debug_message, "createUserWithEmailAndPassword - SUCCESS!");
                    // update UI with the signed-in user's information
                    Toast.makeText(LoginActivity.this, "Creating new account",
                            Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else {
                    // sign in fails
                    Log.w(debug_message, "createUserWithEmailAndPassword - FAILED!",
                            task.getException());
                    // display a toast message to the user.
                    Toast.makeText(LoginActivity.this, "Account already exists!",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
//                    checkForMultiFactorFailure(task.getException());
                }
                hideProgressBar();
            }
        });
        // end create user from email
    }

    // check email account valid form
    private boolean validateForm() {
        boolean valid = true;

        String email = mBinding.fieldEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mBinding.fieldEmail.setError("Required.");
            valid = false;
        } else {
            mBinding.fieldEmail.setError(null);
        }

        String password = mBinding.fieldPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mBinding.fieldPassword.setError("Required.");
            valid = false;
        } else {
            mBinding.fieldPassword.setError(null);
        }

        return valid;
    }
    // end check email account valid form

    private void signIn(String email, String password) {
        Log.d(debug_message, "signIn - " + email);
        if (!validateForm()) { return; }

        showProgressBar();

        // sign in with email
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // sign in success
                    Log.d(debug_message, "signInWithEmailAndPassword - SUCCESS!");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else {
                    // sign in fails
                    Log.w(debug_message, "signInWithEmailAndPassword - FAILED!",
                            task.getException());
                    // display a message to the user
                    Toast.makeText(LoginActivity.this, "Login failed, check email " +
                            "and password again", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
                // sign in error
                if (!task.isSuccessful()) {mBinding.status.setText(R.string.auth_failed);}
                hideProgressBar();
            }
        });
    }

    public void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        // Disable button
        mBinding.verifyEmailButton.setEnabled(false);

        // send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Enable button
                mBinding.verifyEmailButton.setEnabled(true);

                if (task.isSuccessful()) {
                    // send verification email success
                    Log.d(debug_message, "send verification email - SUCCESS!");
                    // display a message to the user
                    Toast.makeText(LoginActivity.this, "Verification email sent to "
                    + user.getEmail(), Toast.LENGTH_SHORT).show();
                }
                else {
                    // send verification email fails
                    Log.e(debug_message, "send verification email - FAILED!",
                            task.getException());
                    Toast.makeText(LoginActivity.this, "Failed to send " +
                            "verification email", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // end send verification email
    }

    // reload activity ui after verified
    private void reload() {
        mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updateUI(mAuth.getCurrentUser());
                    Toast.makeText(LoginActivity.this,
                            "Reload successful!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(debug_message, "reload", task.getException());
                    Toast.makeText(LoginActivity.this,
                            "Failed to reload user.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        hideProgressBar();
        if (user != null) {
            mBinding.status.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mBinding.detail.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            mBinding.emailPasswordButtons.setVisibility(View.GONE);
            mBinding.emailPasswordFields.setVisibility(View.GONE);
            mBinding.signedInButtons.setVisibility(View.VISIBLE);

            if (user.isEmailVerified()) {
                mBinding.verifyEmailButton.setVisibility(View.GONE);
            } else {
                mBinding.verifyEmailButton.setVisibility(View.VISIBLE);
                mBinding.reloadButton.setText("Enter");
            }
        } else {
            mBinding.status.setText(R.string.signed_out);
            mBinding.detail.setText(null);

            mBinding.emailPasswordButtons.setVisibility(View.VISIBLE);
            mBinding.emailPasswordFields.setVisibility(View.VISIBLE);
            mBinding.signedInButtons.setVisibility(View.GONE);
        }
    }
}