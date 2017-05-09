package p10.p10leapmotion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateNewFileDialog extends DialogFragment {

    public interface AddNewFileListener {
        void onAcceptNewFile(String _title);
    }

    AddNewFileListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (AddNewFileListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddNewNotitzListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_new_dialog, null);
        final EditText title = (EditText) view.findViewById(R.id.newFileTitle);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Add new file")
                .setPositiveButton("Accept", null)
                .setNegativeButton("Reject", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positive = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (title.getText().toString().equals("")) {
                            title.setError("File title cannot be empty!");
                        } else {
                            mListener.onAcceptNewFile(title.getText().toString());
                            dialog.dismiss();
                        }
                    }
                });

                Button negative = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });

        return dialog;
    }
}