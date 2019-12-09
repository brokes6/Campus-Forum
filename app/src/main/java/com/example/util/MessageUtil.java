package com.example.util;

import java.util.HashMap;
import java.util.Map;

public class MessageUtil {
    public static Map<String,Integer> convertReplyMap(String mapId){
        System.out.println(mapId);
        Map<String,Integer> map=new HashMap<>();
        String [] content=mapId.split(",");
        map.put("postId",Integer.valueOf(content[0]));
        map.put("commentId",Integer.valueOf(content[1]));
//        map.put("replyId",content[2]);
        return map;
    }

}
