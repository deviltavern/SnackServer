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
    public GlutonSnack(WebSocketServerHandler han){

        System.out.println("构造的ID = "+han.ID);
        snackID = han.ID;
        position = new Vector3();

    }
    public void updatePosition(JSONObject posObj){
        position.x = new Float(posObj.get("x").toString());
        position.y = new Float(posObj.get("y").toString());
        position.z = new Float(posObj.get("z").toString());

        //System.out.println("输出 = "+position.toJsonValue());

    }

    public JSONObject toVecJSON(){
        JSONObject reObj = new JSONObject();

        reObj.put("snackID",snackID);
        reObj.put("vec",position.toJson());
        return reObj;
    }


}
