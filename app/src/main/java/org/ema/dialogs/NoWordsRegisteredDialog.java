package org.ema.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class NoWordsRegisteredDialog extends DialogFragment {

    private static final String TAG = "NoWordsRegisteredDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(org.ema.R.string.noWordsMessage)
                .setPositiveButton(org.ema.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "There's no words registered for this user!");
                        getActivity().finish();
                    }
                });
        return builder.create();
    }
}
