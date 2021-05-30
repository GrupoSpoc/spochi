package com.spochi.auth;

import com.spochi.service.auth.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("!disable-jwt-filter")
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    public static final String INVALID_TOKEN_MESSAGE = "Invalid or expired token";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String INVALID_CLIENT_MESSAGE = "Client not authorized";
    public static final String ID_CLIENT_HEADER = "client_id";
    public static final String BEARER_SUFFIX = "Bearer ";

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    protected static final List<String> client_list;
    // Endpoints que NO necesitan ser autorizados con JWT
    private static final List<String> skippedEndpoints;

    static {
        skippedEndpoints = new ArrayList<>();

        skippedEndpoints.add("/authenticate");
        skippedEndpoints.add("/ping");
        skippedEndpoints.add("/admin/login");
        skippedEndpoints.add("/initiative/all"); // todo esto es para pedirlas desde el admin, BORRAR

    }
    static {
        client_list = new ArrayList<>();
        client_list.add("ANDROIDvYjfU7ff2oCiWazVKbEt2xJ");
    }


    /*
     * Este método se ejecuta en todas las peticiones antes de llegar al controller correspondiente.
     * Cuando se trata de un endpoint que no está en la lista 'skippedEndpoints', lo que hace es extraer el JWT del header y verificar su validez.
     * Si es nulo/vacío/inválido/vencido arroja una AuthorizationException que luego cachea para enviar la respuesta de error (status 406)
     * Si es válido llama al filterChain.doFilter() lo cual significa seguir el curso natural de la petición (ir al controller)
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {

            final String invokedEndpoint = httpServletRequest.getRequestURI();

            if(!invokedEndpoint.equals("/ping") && !validateClient(httpServletRequest)){
                throw new ClientAuthorizationException();
            }

            if (!skippedEndpoints.contains(invokedEndpoint)) {
                String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

                if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_SUFFIX)) {
                    throw new AuthorizationException();
                }

                String token = authorizationHeader.substring(BEARER_SUFFIX.length());

                if (!jwtUtil.isTokenValid(token)) {
                    throw new AuthorizationException();
                }
            }

            filterChain.doFilter(httpServletRequest, httpServletResponse);

        }catch (AuthorizationException | JwtException e) {
            httpServletResponse.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            httpServletResponse.getWriter().write(INVALID_TOKEN_MESSAGE);

        } catch (ClientAuthorizationException e){
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.getWriter().write(INVALID_CLIENT_MESSAGE);
        }
    }

    private boolean validateClient(HttpServletRequest request){
        return (request.getHeader(ID_CLIENT_HEADER) != null && client_list.contains(request.getHeader(ID_CLIENT_HEADER)));
    }
}
