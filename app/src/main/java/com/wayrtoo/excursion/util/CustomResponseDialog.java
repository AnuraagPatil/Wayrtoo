package com.wayrtoo.excursion.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import com.trncic.library.DottedProgressBar;
import com.wayrtoo.excursion.R;


public class CustomResponseDialog {
    private Context mContext;
    private Dialog dialog;

    public CustomResponseDialog(Context mContext) {
        this.mContext = mContext;
    }

    public void showCustomDialog(/*String message*/){
        try{
            dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progress_dialog);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            DottedProgressBar progressBar = (DottedProgressBar) dialog.findViewById(R.id.progress);
//        TextView txt_message = (TextView) dialog.findViewById(R.id.message);
//        txt_message.setText(message);
            progressBar.startProgress();
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
        }catch (Exception e){

        }

    }

    public void hideCustomeDialog(){
        dialog.cancel();
    }


}
