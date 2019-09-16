package com.etao.mobile.glutton;

import com.etao.mobile.websocket.WebSocketServer;
import com.etao.mobile.websocket.WebSocketServerHandler;
import com.sun.org.apache.bcel.internal.generic.RET;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.HashMap;

public class GlutonChatMap {

    public static HashMap<String,GlutonSnack> snackMap = new
            HashMap<String, GlutonSnack>();

    public static void addNewSnack(WebSocketServerHandler han){

        GlutonSnack snack = new GlutonSnack(han);
        snackMap.put(snack.snackID,snack);


    }

    public static void  removeSnack(WebSocketServerHandler han){
        snackMap.remove(han.ID);

    }

    public static GlutonSnack getSnack(String ID){

        if (snackMap.keySet().contains(ID)){

            return  snackMap.get(ID);
        }
        return  null;
    }
    public static JSONObject getAllSnack(){

        JSONObject reObj = new JSONObject();

        int index = 0;

        for (GlutonSnack jb :snackMap.values()){

            reObj.put(index,jb.snackID);
            index++;
        }

        return  reObj;

    }


    public static JSONObject getAllSnackPosition() {

        JSONObject reobj = new JSONObject();
        int index = 0;
        for (GlutonSnack snack : snackMap.values()) {
            reobj.put(index,snack.toVecJSON());
            index++;
        }
       // System.out.println(reobj.toString());
        return reobj;
    }
}
