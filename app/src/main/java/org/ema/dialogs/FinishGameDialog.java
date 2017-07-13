package org.ema.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class FinishGameDialog extends DialogFragment {
    private static final String TAG = "FinishGameDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(org.ema.R.string.areYouSure)
                .setPositiveButton(org.ema.R.string.finish, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "finish the game");
                        getActivity().finish();
                    }
                })
                .setNegativeButton(org.ema.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "User cancelled the the end of the game.");
                    }
                });
        return builder.create();
    }

}
