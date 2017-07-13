package org.ema.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by samuel on 08/06/17.
 */

public class EmptyUsersWordsDialog extends DialogFragment {

    private static final String TAG = "EmptyUsersWordsDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(org.ema.R.string.NeedUserWords)
                .setPositiveButton(org.ema.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Need user(s) and word(s)");
                        getActivity().finish();
                    }
                });
        return builder.create();
    }
}