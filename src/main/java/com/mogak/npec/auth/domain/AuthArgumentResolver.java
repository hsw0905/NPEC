package com.mogak.npec.auth.domain;

import com.mogak.npec.auth.ValidToken;
import com.mogak.npec.auth.exception.TokenExtractFailException;
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

    public AuthArgumentResolver(TokenExtractor tokenExtractor) {
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(String.class) &&
                parameter.hasParameterAnnotation(ValidToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = tokenExtractor.extractAccessToken(request.getHeader("Authorization"));

        if (accessToken == null) {
            throw new TokenExtractFailException("추출할 토큰이 없습니다.");
        }

        return accessToken;
    }
}
