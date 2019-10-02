package com.etao.mobile.glutton;

import com.etao.mobile.templates.Vector3;
import com.etao.mobile.websocket.WebSocketServer;
import com.etao.mobile.websocket.WebSocketServerHandler;
import com.sun.org.apache.bcel.internal.generic.NEW;
import net.sf.json.JSONObject;

public class GlutonSnack {

    WebSocketServerHandler handler;
    Vector3 position;
    String snackID;
    String bodyMessage = "";
    public GlutonSnack(WebSocketServerHandler han){

        System.out.println("构造的ID = "+han.ID);
        snackID = han.ID;
        position = new Vector3();

    }
    public void updatePosition(JSONObject posObj){

        Vector3 tempPos = new Vector3();

        tempPos.x = new Float(posObj.get("x").toString());
        tempPos.y = new Float(posObj.get("y").toString());
        tempPos.z = new Float(posObj.get("z").toString());
        position.x += tempPos.x;
        position.y += tempPos.y;
        position.z += tempPos.z;

        bodyMessage = posObj.get("bodyMsg").toString();
        //System.out.println("输出 = "+bodyMessage);

    }

    public JSONObject toVecJSON(){
        JSONObject reObj = new JSONObject();

        reObj.put("snackID",snackID);
        reObj.put("vec",position.toJson());
        reObj.put("bodyMsg",bodyMessage);
        return reObj;
    }


}
