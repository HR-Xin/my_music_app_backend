package com.ninetrees.musicapp.controller;

import com.ninetrees.musicapp.common.R;
import com.ninetrees.musicapp.entity.Room;
import com.ninetrees.musicapp.service.RoomManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Classname:RoomController
 * Description:
 */
@RestController
@RequestMapping("/api/room")
//@CrossOrigin(origins = "http://47.120.34.75")
public class RoomController {
    @Autowired
    private RoomManager roomManager;
    @PostMapping("/create")
    public R createRoom(){
        Room room = roomManager.createRoom();
        Map<String,Object>res=new HashMap<>();
        res.put("roomId",room.getRoomId());
        return R.ok().data("res",res);
    }


}
