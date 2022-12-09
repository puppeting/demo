package com.inclusive.finance.widget;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.dyhdyh.widget.loading.factory.DialogFactory;
import com.inclusive.finance.R;


/**
 * Created by bin on 17/6/1.
 */

public class SimpleDialogFactory implements DialogFactory {

    @Override
    public Dialog onCreateDialog(Context context) {
        Dialog dialog = new Dialog(context,  R.style.loadstyle);
        dialog.setContentView( R.layout.dialog_progress);
        return dialog;
    }

    @Override
    public void setMessage(Dialog dialog, CharSequence message) {
        TextView tv= (TextView) dialog.findViewById( R.id.tv_progress_msg);
        if (tv!=null){
            tv.setText(message);
        }
    }

    @Override
    public int getAnimateStyleId() {
        return 0;
    }
}
