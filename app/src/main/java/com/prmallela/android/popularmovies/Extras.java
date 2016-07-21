package com.prmallela.android.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Extras {
    private static Toast toast;

    //Take's DateFormat ex:"2016-07-15" return year
    static String getYear(String date) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(calendar.get(Calendar.YEAR));

    }

    //To check whether device is connected to internet or not.
    static boolean connected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {

            showToast(context, R.string.check_internet);
            return false;
        } else {
            return true;
        }

    }

    //To display Toast Msgs
    static void showToast(Context ctx, int message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(ctx, ctx.getString(message), Toast.LENGTH_SHORT);
        toast.show();
    }
}