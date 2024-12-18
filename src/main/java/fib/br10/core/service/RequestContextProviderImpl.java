package fib.br10.core.service;

import fib.br10.core.dto.RequestState;
import fib.br10.core.utility.RequestContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import static fib.br10.core.utility.RequestContextEnum.*;

@Service
public class RequestContextProviderImpl implements RequestContextProvider {

    @Override
    public RequestState getRequestState() {
        return new RequestState(
                getActivityId(),
                getUserId()
        );
    }

    @Override
    public Long getUserId() {
        return RequestContext.get(USER_ID, Long.class);
    }

    @Override
    public String getActivityId() {
        return RequestContext.get(ACTIVITY_ID, String.class);
    }

    @Override
    public String getPhoneNumber() {
        return RequestContext.get(PHONE_NUMBER, String.class);
    }

    @Override
    public Integer getTokenId() {
        return RequestContext.get(TOKEN_ID, Integer.class);
    }

    @Override
    public Date getJwtExpiration() {
        return RequestContext.get(JWT_EXPIRATION);
    }

    @Override
    public String getTimeZone() {
        return RequestContext.get(TIME_ZONE);
    }

    @Override
    public String getAuthorizationHeader() {
        return RequestContext.get(AUTHORIZATION_HEADER, String.class);
    }

    @Override
    public Boolean getIsPublicEndpoint() {
        return RequestContext.get(IS_PUBLIC_ENDPOINT,Boolean.class);
    }

    @Override
    public String getLang() {
        return RequestContext.get(LANG, String.class);
    }

    @Override
    public String getIpAddress() {
        return RequestContext.get(CLIENT_IP, String.class);
    }

    @Override
    public void setIpAddress(String ipAddress) {
         RequestContext.set(CLIENT_IP, String.class);
    }

    @Override
    public void setLang(String lang) {
        RequestContext.set(LANG,lang);
    }

    @Override
    public void setAuthorizationHeader(String token) {
        RequestContext.set(AUTHORIZATION_HEADER, token);
    }

    @Override
    public void setUserId(Long userId) {
        RequestContext.set(USER_ID, userId);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        RequestContext.set(PHONE_NUMBER, phoneNumber);
    }

    @Override
    public void setActivityId(String activityId) {
        RequestContext.set(ACTIVITY_ID, activityId);
    }

    @Override
    public void setRequestPath(String path) {
        RequestContext.set(REQUEST_PATH, path);
    }

    @Override
    public void setIsPublicEnpoint(Boolean isPublicEnpoint) {
        RequestContext.set(IS_PUBLIC_ENDPOINT,isPublicEnpoint);
    }
}
