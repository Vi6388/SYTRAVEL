package com.sy.travel.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.sy.travel.common.AjaxResult;

@ServerEndpoint("/sy/refresh")
@Component
public class SYWebsocketService {
	private static SYProjectService sYProjectService;
	private static SYTeamService syTeamService;
	private static SYProductService syProductService;
	private ScheduledExecutorService scheduledService;
	@Autowired
	public void get(SYProjectService sYProjectService, SYTeamService syTeamService, SYProductService syProductService) {
		SYWebsocketService.sYProjectService = sYProjectService;
		SYWebsocketService.syTeamService = syTeamService;
		SYWebsocketService.syProductService = syProductService;
	}
	@OnOpen
	public void onOpen(Session session){
		scheduledService =  Executors.newSingleThreadScheduledExecutor();
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		scheduledService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					Gson gson = new Gson();
					session.getBasicRemote().sendText(gson.toJson(getMonitor()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0, Integer.valueOf(message), TimeUnit.MILLISECONDS);
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
	}
	
	@OnError
	public void onError(Throwable t) {
		
	}
	
	public AjaxResult<Map<String, Object>> getMonitor(){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> projectMap = SYWebsocketService.sYProjectService.queryAll("",1, Integer.MAX_VALUE);
		if(projectMap == null) {
			resultMap.put("project", new ArrayList<>());
		} else {
			resultMap.put("project", (List<Map<String,Object>>)projectMap.get("documents"));
		}
		Map<String,Object> teamMap = SYWebsocketService.syTeamService.findAll("",1,Integer.MAX_VALUE);
		if(projectMap == null) {
			resultMap.put("team", new ArrayList<>());
		} else {
			resultMap.put("team", (List<Map<String,Object>>)teamMap.get("documents"));
		}
		Map<String,Object> productMap = SYWebsocketService.syProductService.findAll("", 1, Integer.MAX_VALUE);
		if(projectMap == null) {
			resultMap.put("product", new ArrayList<>());
		} else {
			resultMap.put("product", (List<Map<String,Object>>)productMap.get("documents"));
		}
		return new AjaxResult<Map<String,Object>>(200, "success", resultMap);
	}
}
