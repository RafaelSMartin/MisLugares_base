package com.example.mislugares.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.mislugares.R;
import com.example.mislugares.activity.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import java.util.Arrays;

/**
 * Created by Rafael S Martin on 25/02/2018.
 */

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        login();   }

    private void login() {
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if (usuario != null) {
            String provider = usuario.getProviders().get(0).toString();
            if (provider.equals(AuthUI.EMAIL_PROVIDER)){ //Proveedor mail
                usuario.reload();
                if (usuario.isEmailVerified()){ //Mail verificado
                    goToMainActivity(usuario);
                } else { //Mail no verificado
                    Toast.makeText(this,
                            "tienes que verificar tu email, por favor, " +
                                    "revisa tu correo y confirma tu cuenta, gracias!",
                            Toast.LENGTH_LONG).show();
                    usuario.sendEmailVerification();
                    showViewLogin();
                }
            } else {// Otro proveedor diferente de mail, no necesita verificacion
                goToMainActivity(usuario);
            }
        } else {
            showViewLogin();
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == RC_SIGN_IN){
             if(resultCode == ResultCodes.OK){
                 login();
                 finish();
             } else{
                 IdpResponse response = IdpResponse.fromResultIntent(data);
                 if (response == null){
                     Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
                     return;
                 } else if(response.getErrorCode() == ErrorCodes.NO_NETWORK){
                     Toast.makeText(this, "Sin conexion a Internet", Toast.LENGTH_LONG).show();
                     return;
                 } else if(response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR){
                     Toast.makeText(this, "Error desconocido", Toast.LENGTH_LONG).show();
                     return;
                 }
             }
         }

    }

    private void goToMainActivity(FirebaseUser usuario){
        Toast.makeText(this, "inicia sesión: " +
                usuario.getDisplayName()+" - "+
                usuario.getEmail()+" - "+
                usuario.getProviders().get(0),Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void showViewLogin(){
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.mipmap.ic_launcher)
                .setTheme(R.style.FirebaseUITema)
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()))
                .setIsSmartLockEnabled(false)
                .build(), RC_SIGN_IN);
    }


}
