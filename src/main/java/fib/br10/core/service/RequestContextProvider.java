package fib.br10.core.service;


import fib.br10.core.dto.RequestState;

import java.util.Date;

public interface RequestContextProvider {

     RequestState getRequestState();

     Long getUserId();
     String getAuthorizationHeader();

     String getActivityId();

     String getPhoneNumber();

     Integer getTokenId();

     Date getJwtExpiration();

     String getTimeZone();

     Boolean getIsPublicEndpoint();

     String getLang();

     String getIpAddress();

     void setIpAddress(String ipAddress);

     void setLang(String lang);

     void setAuthorizationHeader(String token);

     void setUserId(Long userId);

     void setPhoneNumber(String phoneNumber);

     void setActivityId(String activityId);

     void setRequestPath(String path);

     void setIsPublicEnpoint(Boolean isWhiteListedEndpoint);
}
