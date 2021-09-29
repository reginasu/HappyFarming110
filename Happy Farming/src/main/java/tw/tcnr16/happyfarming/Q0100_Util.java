package tw.tcnr16.happyfarming;


import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

public class Q0100_Util {
    public static void showDialog(Context context, int messageResId, int btnTextResId, DialogInterface.OnClickListener onBtnClickListener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(messageResId)
                .setPositiveButton(btnTextResId, onBtnClickListener).create();
        dialog.show();
    }
}