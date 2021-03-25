package pl.ds.websight.aem.replication.support.rest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ds.websight.aem.replication.support.ReplicationActionType;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public abstract class ReplicateRestAction {

    private static final Logger LOG = LoggerFactory.getLogger(ReplicateRestAction.class);
    private static final String AEM_AUTHENTICATION_COOKIE_NAME = "login-token";
    private static final String AEM_REPLICATION_COMMAND_PARAM = "cmd";
    private static final String AEM_REPLICATION_PATH_PARAM = "path";
    private static final String AEM_REPLICATION_ENDPOINT = "/bin/replicate.json";

    public boolean performReplication(ReplicateRestModel model, ReplicationActionType actionType) {
        return performReplicationAction(model.getRequest(), model.getPath(), actionType);
    }

    public boolean performReplicationAction(SlingHttpServletRequest request, String path, ReplicationActionType actionType) {
        BasicCookieStore cookieStore = prepareCookieStore(request);
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
             CloseableHttpResponse response = httpClient.execute(preparePost(request, path, actionType))) {
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        } catch (IOException e) {
            LOG.warn("Failed to replicate path " + path, e);
        }
        return false;
    }

    protected String getAemReplicationEndpoint() {
        return AEM_REPLICATION_ENDPOINT;
    }

    protected List<NameValuePair> createPostParameters(String path, ReplicationActionType actionType) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(AEM_REPLICATION_PATH_PARAM, path));
        if (actionType != null) {
            params.add(new BasicNameValuePair(AEM_REPLICATION_COMMAND_PARAM, actionType.toString()));
        }
        return params;
    }

    private HttpPost preparePost(SlingHttpServletRequest request, String path, ReplicationActionType actionType)
            throws UnsupportedEncodingException {
        String uri = StringUtils.substringBefore(request.getRequestURL().toString(),
                request.getPathInfo()) + getAemReplicationEndpoint();
        HttpPost post = new HttpPost(uri);
        List<NameValuePair> params = createPostParameters(path, actionType);
        post.setEntity(new UrlEncodedFormEntity(params));
        return post;
    }

    private BasicCookieStore prepareCookieStore(SlingHttpServletRequest request) {
        Cookie loginCookie = request.getCookie(AEM_AUTHENTICATION_COOKIE_NAME);
        BasicCookieStore cookieStore = new BasicCookieStore();
        if (loginCookie != null) {
            BasicClientCookie cookie = new BasicClientCookie(loginCookie.getName(), loginCookie.getValue());
            if (StringUtils.isBlank(loginCookie.getDomain())) {
                cookie.setDomain(request.getServerName());
                cookie.setAttribute(ClientCookie.DOMAIN_ATTR, Boolean.TRUE.toString());
            } else {
                cookie.setDomain(loginCookie.getDomain());
            }
            cookie.setPath(loginCookie.getPath());
            cookie.setSecure(loginCookie.getSecure());
            cookieStore.addCookie(cookie);
        }
        return cookieStore;
    }
}
