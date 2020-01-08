package com.munish.saferfoodproject.Service;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.ArrayList;

public class Constants {
 //   public static final String BaseUrl="https://safer-food.firebaseio.com/";
    public static final String BaseUrl="https://safer-food.firebaseio.com/";
    public static final String userDetail="users-details/";

    public static final String json=".json";
    public static int count=0;
    public static ArrayList<String> arrayList;
    public static int checkResume=0;
   public static ProgressDialog showProgressDialog(Context context, String message){
      ProgressDialog m_Dialog = new ProgressDialog(context);
      m_Dialog.setMessage(message);
      m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      m_Dialog.setCancelable(false);
      m_Dialog.show();
      return m_Dialog;
   }
}
