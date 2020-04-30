package com.zyot.fung.shyn.server.main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zyot.fung.shyn.server.main.model.Player;
import com.zyot.fung.shyn.server.packet.AddConnectionPacket;
import com.zyot.fung.shyn.server.packet.RemoveConnectionPacket;
import com.zyot.fung.shyn.server.packet.Request;
import com.zyot.fung.shyn.server.packet.request.JoinRoomRequest;
import com.zyot.fung.shyn.server.packet.response.JoinRoomResponse;
import com.zyot.fung.shyn.server.packet.response.RoomStateResponse;

public class EventListener {
	
	private HashMap<Class<? extends Request>, RequestHandlerMethod> mRequestTypeMap = new HashMap<>();
		
	private static final String METHOD_PREFIX = "onRequest";
	
	class RequestHandlerMethod {

	    private Method mMethod;
	    Class<? extends Request> request;

	    RequestHandlerMethod(Method method, Class<? extends Request> request) {
	        mMethod = method;
	        mMethod.setAccessible(true);
	        this.request = request;
	    }

	    public void invoke(Request request, Connection connection)
		    throws InvocationTargetException, IllegalAccessException {
		    mMethod.invoke(new EventListener(), request, connection);
		}

	    @Override
	    public String toString() {
	        return mMethod.getName() + "(" + request.getSimpleName() + ")";
	    }
	}
	
	
	
	public EventListener() {
		super();
		
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method m : methods) {
            Class<?>[] parameterTypes = m.getParameterTypes();
            if (isValidEventBusHandlerMethod(m, parameterTypes)) {
                Class<? extends Request> requestType = (Class<? extends Request>) parameterTypes[0];
                RequestHandlerMethod method = new RequestHandlerMethod(m, requestType);
                mRequestTypeMap.put(requestType, method);
            }
        }
		System.out.println("Server EventListener: Number of request handler methods: " + mRequestTypeMap.size());
	}
	
	private boolean isValidEventBusHandlerMethod(Method method, Class<?>[] parameterTypes) {
	        int modifiers = method.getModifiers();
	        if (Modifier.isPublic(modifiers) &&
	            method.getReturnType().equals(Void.TYPE) &&
	            parameterTypes.length == 2) {
	            if (Request.class.isAssignableFrom(parameterTypes[0]) &&
	            	Connection.class.isAssignableFrom(parameterTypes[1]) &&
	                method.getName().startsWith(METHOD_PREFIX)) {
	            	System.out.println(method.getName() + " is a VALID handler");
	                return true;
	            } else {
	            	System.out.println(method.getName() + " Cannot assign paramtype to Request");
	            }
	        } else {
	        	System.out.println("Method " + method.getName() + " is not valid handler");
	        }
	        return false;
	    }



	public void received(Object p,Connection connection) {
		System.out.println("Server EventListener: "+ p.getClass());
		
//		if(p instanceof AddConnectionPacket) {
//			AddConnectionPacket packet = (AddConnectionPacket)p;
//			packet.id = connection.id;
//			for(int i=0; i<ConnectionHandler.connections.size(); i++) {
//				Connection c = ConnectionHandler.connections.get(i);
//				if(c != connection) {
//					c.sendObject(packet);
//				}
//			}
//			
//		}else if(p instanceof RemoveConnectionPacket) {
//			RemoveConnectionPacket packet = (RemoveConnectionPacket)p;
//			System.out.println("Connection: " + packet.id + " has disconnected");
//			ConnectionHandler.connections.get(packet.id).close();
//			ConnectionHandler.connections.remove(packet.id);
//		} else if (p instanceof JoinRoomRequest) {
////			if (ConnectionHandler.connections.size() > ConnectionHandler.maxConcurentConnections) {
////				JoinRoomResponse response = new JoinRoomResponse(connection.id, false, "Phong da du nguoi choi", null);
////				connection.sendObject(response);
////				ConnectionHandler.connections.get(connection.id).close();
////				ConnectionHandler.connections.remove(connection.id);
////			} else {
////				JoinRoomRequest request = (JoinRoomRequest)p;
////				ConnectionHandler.players.put(connection.id, new Player(connection.id, request.playerName, request.isMaster));
////				List<Player> currentPlayers = new ArrayList<Player>(ConnectionHandler.players.values());
////				JoinRoomResponse response = new JoinRoomResponse(connection.id, true, "Ket noi thanh cong", currentPlayers);
////				connection.sendObject(response);
////				RoomStateResponse roomState = new RoomStateResponse(currentPlayers);
////				for(Connection c :ConnectionHandler.connections.values()) {
////					if(c != connection) {
////						c.sendObject(roomState);
////					}
////				}
////			}
//			handleRequest((Request)p, connection);
//		} else {
//			System.out.println("Server - Cannot identify the request");
//		}
		
		handleRequest((Request)p, connection);
	}
	
	
	
	private void handleRequest(final Request request, Connection connection) {
		RequestHandlerMethod requestHandler = mRequestTypeMap.get(request.getClass());
		if (requestHandler != null) {
			processEvent(requestHandler, request, connection);
        }
	}
	
	private void processEvent(final RequestHandlerMethod requestHandler, final Request request, Connection connection) {
        try {
        	requestHandler.invoke(request, connection);
        } catch (IllegalAccessException | InvocationTargetException e) {
        	System.out.println("Failed to invoke method - " + e.getStackTrace());
        }
    }
	
	public void onRequest(JoinRoomRequest joinRoomRequest, Connection connection) {
		System.out.println("New Request: JoinRoomRequest ");
		if (ConnectionHandler.connections.size() > ConnectionHandler.maxConcurentConnections) {
			JoinRoomResponse response = new JoinRoomResponse(connection.id, false, "Phong da du nguoi choi", null);
			connection.sendObject(response);
			ConnectionHandler.connections.get(connection.id).close();
			ConnectionHandler.connections.remove(connection.id);
		} else {
			JoinRoomRequest request = (JoinRoomRequest)joinRoomRequest;
			ConnectionHandler.players.put(connection.id, new Player(connection.id, request.playerName, request.isMaster));
			List<Player> currentPlayers = new ArrayList<Player>(ConnectionHandler.players.values());
			JoinRoomResponse response = new JoinRoomResponse(connection.id, true, "Ket noi thanh cong", currentPlayers);
			connection.sendObject(response);
			RoomStateResponse roomState = new RoomStateResponse(currentPlayers);
			for(Connection c :ConnectionHandler.connections.values()) {
				if(c != connection) {
					c.sendObject(roomState);
				}
			}
		}
	}

}
