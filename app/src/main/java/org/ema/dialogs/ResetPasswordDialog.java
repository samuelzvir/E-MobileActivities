package org.ema.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import org.ema.R;
import org.ema.activities.admin.CreateTextActivity;
import org.ema.entities.Admin;
import org.ema.util.HashCodes;
import org.ema.util.SendMailTask;

import java.util.Date;

import io.realm.Realm;

public class ResetPasswordDialog extends DialogFragment {
    private static final String TAG = "ResetPasswordDialog";
    private CreateTextActivity createTextActivity;
    private Realm realm;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = getString(R.string.areYouSure) + " \n" + getString(R.string.anEmailWillbeSend);
        builder.setMessage(message)
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Resetting password...");
                        final Admin admin = realm.where(Admin.class).findFirst();
                        final String p = HashCodes.get_SHA_512_SecurePassword(new Date().toString(),"newPass").substring(5,11);
                        final String hashP = HashCodes.get_SHA_512_SecurePassword(p,"eMobileActivities");
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                admin.setPassword(hashP);
                                admin.setChangePassword(true);
                                realm.insertOrUpdate(admin);
                            }
                        });

                        new SendMailTask(getActivity()).execute("example@example.com",
                                "123456", admin.getEmail(), getString(R.string.ChangingPassword), getString(R.string.YourNewPasswordIs)+": "+p);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "User cancelled the the password reset.");
                    }
                });
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}