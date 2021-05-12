package com.spochi.controller.handler;

import com.spochi.service.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

import static com.spochi.auth.JwtFilter.AUTHORIZATION_HEADER;
import static com.spochi.auth.JwtFilter.BEARER_SUFFIX;

@Component
public class UidHandler implements HandlerMethodArgumentResolver {
    @Autowired
    private JwtUtil jwtUtil;

    public UidHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Chequeo si el método invocado tiene un parámetro anotado con @Uid
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(Uid.class) != null;
    }

    // si llegó hasta este punto es porque el JwtFilter validó que el token sea válido, entonces no puede faltar el header
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        String token = authorizationHeader.substring(BEARER_SUFFIX.length());

        return jwtUtil.extractUid(token);
    }

    // Ejemplo de uso
    /*
        @GetMapping("/something")
        public String getSomething(@Uid String uid) {
            return service.getSomething(uid);
        }
     */
    // El handler inyecta el parámetro uid extraído del token bearer
}