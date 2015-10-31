package com.jgetrost.automation.lighting;

import java.sql.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import org.json.JSONException;
import org.json.JSONObject;

@Path("light/{lightId}")
public class light {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getLight(@PathParam("lightId") int lightId) {
		System.out.println("get connection made");
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonStatus = new JSONObject();
		JSONObject jsonLight = new JSONObject();
		int id = lightId;
		String description = "";
		int statusId = -1;
		String statusDescription = "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection conn = DriverManager.getConnection("jdbc:mysql://jgetrost.com:3306/restful_lights", "lighter_user", "lighter_password");
		
			Statement query = conn.createStatement();
			
			ResultSet results = query.executeQuery("SELECT lights.lightId, lights.description, lights.statusId, statuses.statusDescription FROM lights INNER JOIN statuses ON lights.statusId = statuses.statusId WHERE lights.lightId = " + id);
			
			while (results.next()){
				description = results.getString("lights.description");
				statusId = results.getInt("lights.statusId");
				statusDescription = results.getString("statuses.statusDescription");
			}
			
			jsonLight.put("lightId", id);
			jsonLight.put("lightDescription", description);
			jsonStatus.put("statusId", statusId);
			jsonStatus.put("statusDescription", statusDescription);
			jsonObject.put("light", jsonLight);
			jsonObject.put("status", jsonStatus);
			
		}catch (Exception e){
			System.out.println(e.toString());
		}
		
		
		return Response.status(200).entity(jsonObject.toString()).build();
		
		
	}
	
	@Path("{statusId}")
	@PUT
	public void putLight(@PathParam("lightId") int lightId, @PathParam("statusId") int statusId) {
		try {
			System.out.println("put connection made");
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection conn = DriverManager.getConnection("jdbc:mysql://jgetrost.com:3306/restful_lights", "lighter_user", "lighter_password");
		
			Statement query = conn.createStatement();
			
			query.execute("UPDATE lights SET statusId=" + statusId + " WHERE lightId = " + lightId);
		
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}
	

}
