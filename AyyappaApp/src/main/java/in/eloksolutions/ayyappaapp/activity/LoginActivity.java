package in.eloksolutions.ayyappaapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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


import java.util.List;
import java.util.Locale;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.activities.CardViewActivity;

import in.eloksolutions.ayyappaapp.activities.Registartion;
import in.eloksolutions.ayyappaapp.beans.RegisterDTO;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.RegisterHelper;
import in.eloksolutions.ayyappaapp.recycleviews.CheckInternet;
import in.eloksolutions.ayyappaapp.util.Util;

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
    static final int REQUEST_LOCATION = 1;
    private int STORAGE_PERMISSION_CODE = 23;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    double latti=17.3833342,longi=78.4009543;
    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private String city;
    private String pinCode;

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
         locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        getLocation();
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
        if(result==null)return;
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

    @NonNull
    @Override
    public String getLocalClassName() {
        return super.getLocalClassName();
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

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }
    private boolean isLocatioinAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }
    // [END handleSignInResult]
    void getLocation() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (canMakeSmores() && !isLocatioinAllowed()){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
            return;
        }
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                Geocoder gc= new Geocoder(this, Locale.getDefault());
                // TextView addr = (TextView) main.findViewById(R.id.editText2);
                String result="x03";
                try {
                    latti = location.getLatitude();
                    longi = location.getLongitude();
                    List<Address> addressList = gc.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        city = address.getLocality();
                        Log.i(TAG," CITY IS "+city);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("Unable");
            }


    }

    private boolean canMakeSmores(){
        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                Log.i(TAG, "onRequestPermissionsResult :");
                getLocation();

                break;
        }
    }
    // [START signIn]
    private void signIn() {
        Log.i(TAG, "signIn:");
       // getLocation();
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
        registerDto.setCity(city);

        //String are=area.getText().toString();
        //registerDto.setArea(are);
        //String pass=password.getText().toString();
        //registerDto.setPassword(pass);
        registerDto.setLongi(longi);
        registerDto.setLati(latti);
       // registerDto.setImgPath(keyName);
        return registerDto;
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
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

        if(signedIn) {
            Util.setPreferances(this, registerDto);
            Intent main = new Intent(this, CardViewActivity.class);
            startActivity(main);
        }
        else {
            Toast.makeText(LoginActivity.this,"Login Failed, Please login",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG,"in the cliecked");
    }
}