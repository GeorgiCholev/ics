package com.example.ics.interceptions.interceptors;

import com.example.ics.interceptions.rate_limiters.PostRequestThrottle;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class PostRequestInterceptor implements HandlerInterceptor {

    private static final String BYPASS_THROTTLE_KEY = System.getenv("BYPASS_THROTTLE_KEY");
    private static final String APPLICABLE_PATH = "/images";
    private static final String APPLICABLE_METHOD = "POST";

    private final PostRequestThrottle throttle;

    @Autowired
    public PostRequestInterceptor(PostRequestThrottle throttle) {
        this.throttle = throttle;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (BYPASS_THROTTLE_KEY != null &&
                BYPASS_THROTTLE_KEY.equals(request.getHeader("X-BYPASS-THROTTLE-KEY"))) {
            return true;
        }

        if (notAllowedRequest(request)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false;
        }
        return true;
    }

    private boolean notAllowedRequest(HttpServletRequest request) {
        return (request.getServletPath().equals(APPLICABLE_PATH)) &&
                (request.getMethod().equalsIgnoreCase(APPLICABLE_METHOD)) &&
                (!throttle.tryConsume());
    }
}
