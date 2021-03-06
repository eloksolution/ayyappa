package ayyappa.eloksolutions.in.ayyappaap.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import ayyappa.eloksolutions.in.ayyappaap.CardViewActivity;
import ayyappa.eloksolutions.in.ayyappaap.CheckInternet;
import ayyappa.eloksolutions.in.ayyappaap.R;
import ayyappa.eloksolutions.in.ayyappaap.Registartion;
import ayyappa.eloksolutions.in.ayyappaap.beans.RegisterDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.RegisterHelper;
import ayyappa.eloksolutions.in.ayyappaap.util.Util;

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i("SignInActivity","in the SignInActivity starting ");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

     }

    public void signInClick(View view) {
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        signIn();
        // [END build_client]
    }

    public void onRegisterClick(View v) {
        startActivity(new Intent(LoginActivity.this, Registartion.class));
    }
    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult :"+resultCode);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(LoginActivity.this,"Signed_in_as"+ acct.getDisplayName(),Toast.LENGTH_LONG).show();
            Log.i(TAG,"Email "+acct.getEmail()+" Family name "+acct.getFamilyName());
            Log.i(TAG,"Email "+acct.getPhotoUrl()+" Account "+acct.getAccount());
            RegisterDTO registerDto=updateRegistrationOnServer(acct);
            updateUI(true, registerDto);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false, null);
        }
    }

    private RegisterDTO updateRegistrationOnServer(GoogleSignInAccount acct) {
            if (CheckInternet.checkInternetConenction(LoginActivity.this)) {
                RegisterHelper createRegisterHelper = new RegisterHelper(LoginActivity.this);
                String gurl = Config.SERVER_URL+"user/add";
                try {
                    RegisterDTO registerDto= buildDTOObject(acct);
                    createRegisterHelper.new CreateRegistration(registerDto, gurl).execute();
                    return registerDto;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                CheckInternet.showAlertDialog(LoginActivity.this, "No Internet Connection",
                        "You don't have internet connection.");
            }
        return null;
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Log.i(TAG, "signIn:");
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false, null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false, null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
    private RegisterDTO buildDTOObject(GoogleSignInAccount acct) {
        RegisterDTO registerDto=new RegisterDTO();
        String rname= acct.getDisplayName();
        registerDto.setFirstName(rname);
        String rlname= acct.getFamilyName()+","+acct.getGivenName();
        registerDto.setLastName(rlname);
        String rEmail= acct.getEmail();
        registerDto.setEmail(rEmail);
        Uri photoURI=acct.getPhotoUrl();
        Log.i(TAG," PHOTO URL"+photoURI);
        if(photoURI!=null){
            registerDto.setImgPath(photoURI.toString());
        }
        // String rcity=city.getText().toString();
        //registerDto.setCity(rcity);
        //String are=area.getText().toString();
        //registerDto.setArea(are);
        //String pass=password.getText().toString();
        //registerDto.setPassword(pass);
        //registerDto.setLongi(longi);
        //registerDto.setLati(latti);
       // registerDto.setImgPath(keyName);
        return registerDto;
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void updateUI(boolean signedIn, RegisterDTO registerDto) {
        Intent main=null;
        if(signedIn) {
            Util.setPreferances(this, registerDto);
            main = new Intent(this, CardViewActivity.class);
            startActivity(main);
        }
        else {
            Toast.makeText(LoginActivity.this,"Login Failed, Please login",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {

    }
}