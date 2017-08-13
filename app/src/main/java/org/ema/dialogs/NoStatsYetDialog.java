package org.ema.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;


/**
 * Created by samuel on 13/08/17.
 */

public class NoStatsYetDialog extends DialogFragment {

    private static final String TAG = "NoStatsYetDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        StringBuilder finalMessage = new StringBuilder();
        finalMessage.append(getResources().getString(org.ema.R.string.noStats));

        builder.setMessage(finalMessage.toString())
                .setPositiveButton(org.ema.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "No stats yet!");
                        getActivity().finish();
                    }
                });
        return builder.create();
    }
}