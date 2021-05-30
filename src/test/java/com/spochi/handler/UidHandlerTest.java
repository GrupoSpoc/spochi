package com.spochi.handler;

import com.spochi.controller.handler.Uid;
import com.spochi.controller.handler.UidHandler;
import com.spochi.service.auth.JwtUtil;
import com.spochi.service.auth.JwtUtilForTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

import static com.spochi.auth.JwtFilter.AUTHORIZATION_HEADER;
import static com.spochi.auth.JwtFilter.BEARER_SUFFIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UidHandlerTest {

    @Test
    @DisplayName("supports parameter | when method is annotated with @Uid | should return true")
    void testSupportsParameterTrue() throws NoSuchMethodException {
        final UidHandler uidHandler = new UidHandler(new JwtUtil());
        final MethodParameter method = new MethodParameter(this.getClass().getDeclaredMethod("methodWithUidAnnotation", String.class), 0);

        assertTrue(uidHandler.supportsParameter(method));
    }

    @Test
    @DisplayName("supports parameter | when method is not annotated with @Uid | should return false")
    void testSupportsParameterFalse() throws NoSuchMethodException {
        final UidHandler uidHandler = new UidHandler(new JwtUtil());

        final MethodParameter method = new MethodParameter(this.getClass().getDeclaredMethod("methodWithoutUidAnnotation", String.class), 0);
        assertFalse(uidHandler.supportsParameter(method));
    }

    private void methodWithUidAnnotation(@Uid String uid) {}
    private void methodWithoutUidAnnotation(String uid) {}

    @Test
    @DisplayName("resolve argument | when authorization header is present | should return uid")
    void testResolveArgumentOk() {
        final JwtUtil jwtUtilMock = mock(JwtUtil.class);

        when(jwtUtilMock.extractUid(anyString())).thenReturn("uid");

        final UidHandler uidHandler = new UidHandler(jwtUtilMock);
        final NativeWebRequest nativeWebRequest = mock(NativeWebRequest.class);
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        when(httpServletRequest.getHeader(AUTHORIZATION_HEADER)).thenReturn(BEARER_SUFFIX +  "jwt");
        when(nativeWebRequest.getNativeRequest()).thenReturn(httpServletRequest);

        final Object uid = uidHandler.resolveArgument(mock(MethodParameter.class), mock(ModelAndViewContainer.class), nativeWebRequest, mock(WebDataBinderFactory.class));

        assertEquals("uid", uid.toString());
    }

    /**
     * Este test prueba que un token validado por JWTFilter tiene que seguir siendo válido
     * cuando llega hasta UidHandler. Por eso es que JWTUtil valida con el now() + 1 minuto
     */
    @Test
    @DisplayName("resolve argument | when token expires in two minutes | should return uid")
    void testResolveArgumentTokenExpiresInTwoMinutesOk() {
        final String uid = "uid";

        // creo el token con la fecha actual
        final long currentMillis = System.currentTimeMillis();
        final JwtUtilForTest jwtUtilNow = new JwtUtilForTest(() -> currentMillis);
        final String tokenCreatedNow = jwtUtilNow.generateToken(uid);

        // construyo UidHandler como si hubiesen pasado 2hs y 58 minutos (2 min antes de que venza el token)
        final long millisAfter2HoursAnd58Minutes = currentMillis + TimeUnit.HOURS.toMillis(3L) - TimeUnit.MINUTES.toMillis(2L);
        final JwtUtil jwtUtil2Hours58MinutesLater = new JwtUtilForTest(() -> millisAfter2HoursAnd58Minutes);
        final UidHandler uidHandler = new UidHandler(jwtUtil2Hours58MinutesLater);

        final NativeWebRequest nativeWebRequest = mock(NativeWebRequest.class);
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        // hago una petición pasando el token creado con la fecha actual
        when(httpServletRequest.getHeader(AUTHORIZATION_HEADER)).thenReturn(BEARER_SUFFIX +  tokenCreatedNow);
        when(nativeWebRequest.getNativeRequest()).thenReturn(httpServletRequest);

        final Object generatedUid = uidHandler.resolveArgument(mock(MethodParameter.class), mock(ModelAndViewContainer.class), nativeWebRequest, mock(WebDataBinderFactory.class));

        // el token debería ser válido
        assertTrue(jwtUtil2Hours58MinutesLater.isTokenValid(tokenCreatedNow));

        // el handler debería poder extraer el token
        assertEquals(uid, generatedUid.toString());
    }
}
