package com.etao.mobile.glutton;

import com.etao.mobile.templates.Vector3;
import com.etao.mobile.websocket.WebSocketServer;
import com.etao.mobile.websocket.WebSocketServerHandler;
import com.sun.org.apache.bcel.internal.generic.NEW;
import net.sf.json.JSONObject;

public class GlutonSnack {

    public WebSocketServerHandler handler;
    Vector3 dir;
    Vector3 position;
    String snackID;
    String bodyMessage = "";
    public GlutonSnack(WebSocketServerHandler han){

        System.out.println("构造的ID = "+han.ID);
        snackID = han.ID;
        position = new Vector3();
        dir = new Vector3();
        position = new Vector3();

    }

    public  void  updateDir(JSONObject posObj) {

        dir.x = new Float(posObj.get("x").toString());
        dir.y = new Float(posObj.get("y").toString());
        dir.z = new Float(posObj.get("z").toString());

        bodyMessage = posObj.get("bodyMsg").toString();


    }
    public void updatePosition(){


        position.x += dir.x;
        position.y += dir.y;
        position.z += dir.z;


        //System.out.println("输出 = "+bodyMessage);

    }

    public JSONObject toVecJSON(){

        updatePosition();
        JSONObject reObj = new JSONObject();
        reObj.put("snackID",snackID);
        reObj.put("vec",position.toJson());
        reObj.put("bodyMsg",bodyMessage);
        return reObj;
    }


}
