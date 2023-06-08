package org.sitenv.referenceccda.configuration;

import okhttp3.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * KeycloakTokenValidationClient class used for validating token
 */
@Component("keycloakTokenValidationClient")
public class KeycloakTokenValidationClient {

    /** The LOGGERGER */
    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakTokenValidationClient.class);
    private static final String APPLICATION_URL_FORM_ENCODED = "application/x-www-form-urlencoded";

    /**
     *
     * @param request
     * @param authUrl : Auth base URL for keycloak
     * @param realm : keycloak realm for introspect
     * @param clientId : client id for authenticating token
     * @param clientSecret
     * @return
     */
    public boolean validateToken(HttpServletRequest request, String authUrl, String realm, String clientId, String clientSecret) {

        LOGGER.info("Entry - validateToken Method in KeyCloakTokenValidationClient ");
        boolean validationResponse = false;
        final String authorizationHeaderValue = request.getHeader("Authorization");
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer"));
            String token = authorizationHeaderValue.substring(7, authorizationHeaderValue.length());

        String url = authUrl + "/realms/" + realm + "/protocol/openid-connect/token/introspect";
        LOGGER.info("KeyCloakTokenValidationClient URL = "+url );

        OkHttpClient client = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier()
        {
            @Override
            public boolean verify(String hostname, SSLSession session)
            {
                return true;
            }
        }).build();

        MediaType mediaType = MediaType.parse(APPLICATION_URL_FORM_ENCODED);

        RequestBody body = RequestBody.create(mediaType, "token="+token+"&client_id="+clientId+"&client_secret="+clientSecret);

        Request requestOne = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response clientResponse;
        try {
            clientResponse = client.newCall(requestOne).execute();
            clientResponse.body();
            if (!clientResponse.isSuccessful()) {
                LOGGER.error("Failed to authenticate");
            }
            String response = clientResponse.body().string();
            JSONObject jsonObj = new JSONObject(response);
            validationResponse = (boolean) jsonObj.get("active");
            LOGGER.info("Access Token Validation Status ::::" + validationResponse);
        } catch (IOException e) {
            LOGGER.info("Exception - validateToken Method in KeyCloakTokenValidationClient",e);
            e.printStackTrace();
        }
        LOGGER.info("Exit - validateToken Method in KeyCloakTokenValidationClient ");
        return validationResponse;

    }

}
