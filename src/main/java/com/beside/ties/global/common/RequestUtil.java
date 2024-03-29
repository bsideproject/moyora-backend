package com.beside.ties.global.common;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
    public static String getAuthorizationToken(String header) {
        if(header == null){
            return null;
        }
        header.replace("Bearer ", "");
        // Authorization: Bearer <access_token>
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        // Remove Bearer from string
        String[] parts = header.split(" ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        // Get token
        return parts[1];
    }

    public static String getAuthorizationToken(HttpServletRequest request) {
        return getAuthorizationToken(request.getHeader("Authorization"));
    }


    public static String parseRegion(String regionName){
        if(regionName.contains("_")){
            String[] strings = regionName.split("_");
            regionName = strings[1];
        }
        return regionName;
    }
}