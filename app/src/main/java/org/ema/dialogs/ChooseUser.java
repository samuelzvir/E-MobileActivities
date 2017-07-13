package org.ema.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class ChooseUser extends DialogFragment {

    private static final String TAG = "ChooseUser";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(org.ema.R.string.Choose_an_user)
                .setPositiveButton(org.ema.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Please choose an user");
                        getActivity().finish();
                    }
                });
        return builder.create();
    }
}
