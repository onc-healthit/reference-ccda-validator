package org.sitenv.referenceccda.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter class for intercepting incoming request
 */
@Component
@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {

    /** The LOGGERGER */
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenFilter.class);
    private String authUrl;
    private String realm;
    private String clientId;
    private String clientSecret;
    private String authEnabled;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        authEnabled = servletContext.getInitParameter("keycloak.enabled");
        authUrl = servletContext.getInitParameter("keycloak.auth.server");
        realm = servletContext.getInitParameter("keycloak.realm");
        clientId = servletContext.getInitParameter("keycloak.client.id");
        clientSecret = servletContext.getInitParameter("keycloak.client.secret");
        LOGGER.info("authEnabled"+authEnabled+"authUrl == "+authUrl +"\nrealm == "+realm+"\nclientId == "+clientId+"\nclientSecret == "+clientSecret);
        //System.out.println("authEnabled == "+authEnabled+"\nauthUrl == "+authUrl +"\nrealm == "+realm+"\nclientId == "+clientId+"\nclientSecret == "+clientSecret);
    }

    /**
     * (non-Javadoc) Details of the APIs and the request types that can be used with
     * this application
     *
     * @param req
     * @param res
     * @param chain
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        LOGGER.info("Entry - doFilter Method in TokenFilter ");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURL().toString();

        if (path.contains("/static/")) {
            chain.doFilter(request, response);
            LOGGER.info("Exit - doFilter Method in TokenFilter -- metadata endpoint");
        }else if ("true".equals(authEnabled)) {
            KeycloakTokenValidationClient keyCloakTokenValidationClient = new KeycloakTokenValidationClient();
            boolean responseStatus = keyCloakTokenValidationClient.validateToken(request, authUrl, realm, clientId, clientSecret);

            LOGGER.info("RESPONSE STATUS :: " + responseStatus);

            if (responseStatus) {
                chain.doFilter(request, response);
                LOGGER.info("Exit - doFilter Method in TokenFilter ");
            } else {
                response.sendError(401, "UnAuthorized User");
                LOGGER.error("Error in doFilter TokenFilter - UnAuthorized User ");
            }
        } else {
            chain.doFilter(request, response);
            LOGGER.info("Exit - doFilter Method in TokenFilter -- Keycloak authentication not enabled");
        }
    }

    /**
     * Destroy method
     */
    @Override
    public void destroy() {
        LOGGER.info("Entry - destroy Method in Web Filter ");
    }

}
