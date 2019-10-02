package com.etao.mobile.op;

import com.etao.mobile.client.BroadCastMessage;
import com.etao.mobile.client.ClientMap;
import com.etao.mobile.client.SingleMessager;
import com.etao.mobile.glutton.GlutonChatMap;
import com.etao.mobile.glutton.GlutonSnack;
import com.etao.mobile.websocket.WebSocketServerHandler;
import javafx.embed.swt.SWTFXUtils;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

public class OP_100  extends OPStreategyEX{

    public GlutonSnack snack;
    public JSONObject tempJson;
    public OP_100(WebSocketServerHandler handler) {
        super(handler);

    }

    @Override
    public void subOP(int subCode) {
        //System.out.println("100 op ->"+subCode);
        switch (subCode)
        {
            case 0:
              //  System.out.println("请求分配的ID");

                GlutonChatMap.addNewSnack(handler);

                SingleMessager.send(this.handler,100,0,handlerID);

                BroadCastMessage.broadCast(100,1, GlutonChatMap.getAllSnack());
                BroadCastMessage.broadCast(100,2, GlutonChatMap.getAllSnackPosition());
                break;

            case 202:
                snack = GlutonChatMap.getSnack(handlerID);

                if(snack!= null){
                    tempJson = JSONObject.fromObject(opObject.get("message").toString());
                   // System.out.println(tempJson.toString());
                    snack.updatePosition(tempJson);
                }else
                {
                   // System.out.println("该snack为空");
                }
                break;

            case 204:

                tempJson = JSONObject.fromObject(opObject.get("message").toString());

                BroadCastMessage.broadCast(100,204,tempJson);
                break;

        }
    }
}
