package com.beside.ties.global.auth.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WhiteList {
    public static final List<String> WHITE_LIST_EQUALS = Arrays.asList("/","","/api/v1/user/signin");
    public static final List<String> WHITE_LIST_STARTS = Arrays.asList("/api/v1/login","/swagger","/v3","/h2-console","/local/login");


    public static boolean checkWhiteList(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        boolean result = false;

        if(WHITE_LIST_EQUALS.contains(request.getServletPath()))
            result = true;

        for(int i=0; i< WHITE_LIST_STARTS.size(); i++){
            if(request.getServletPath().startsWith(WHITE_LIST_STARTS.get(i)))
                result = true;
        }
        return result;
    }
}
