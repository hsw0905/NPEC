package com.mogak.npec.auth.domain;

import com.mogak.npec.auth.annotation.ValidToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenExtractor tokenExtractor;
    private final TokenProvider tokenProvider;

    public AuthArgumentResolver(TokenExtractor tokenExtractor, TokenProvider tokenProvider) {
        this.tokenExtractor = tokenExtractor;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class) &&
                parameter.hasParameterAnnotation(ValidToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        String token = tokenExtractor.extractToken(request.getHeader("Authorization"));
        return tokenProvider.getParsedClaims(token);
    }
}
