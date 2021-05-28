package com.spochi.handler;

import com.spochi.controller.handler.Uid;
import com.spochi.controller.handler.UidHandler;
import com.spochi.service.auth.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

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

    private void methodWithUidAnnotation(@Uid String uid) {}
    private void methodWithoutUidAnnotation(String uid) {}
}
