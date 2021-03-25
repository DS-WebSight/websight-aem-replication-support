package pl.ds.websight.aem.replication.support.rest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.osgi.service.component.annotations.Component;
import pl.ds.websight.aem.replication.support.ReplicationActionType;
import pl.ds.websight.rest.framework.RestAction;
import pl.ds.websight.rest.framework.RestActionResult;
import pl.ds.websight.rest.framework.annotations.SlingAction;

import java.util.List;

import static pl.ds.websight.rest.framework.annotations.SlingAction.HttpMethod.POST;

@Component
@SlingAction(POST)
public class RecursivePublishRestAction extends PublishRestAction implements RestAction<ReplicateRestModel, Void> {

    private static final String AEM_RECURSIVE_REPLICATION_ENDPOINT = "/libs/replication/treeactivation.html";
    private static final String ONLY_MODIFIED_PARAM = "onlymodified";
    private static final String IGNORE_DEACTIVATED_PARAM = "ignoredeactivated";

    @Override
    public RestActionResult<Void> perform(ReplicateRestModel model) {
        if (model.getResolver().getResource(StringUtils.substringBefore(AEM_RECURSIVE_REPLICATION_ENDPOINT, ".html")) == null) {
            return RestActionResult.failure("Resource publication failed",
                    "User not allowed to execute AEM tree activation endpoint. Please try publishing without children");
        }
        return super.perform(model);
    }

    @Override
    protected String getAemReplicationEndpoint() {
        return AEM_RECURSIVE_REPLICATION_ENDPOINT;
    }

    @Override
    protected List<NameValuePair> createPostParameters(String path, ReplicationActionType command) {
        List<NameValuePair> params = super.createPostParameters(path, command);
        params.add(new BasicNameValuePair(ONLY_MODIFIED_PARAM, Boolean.FALSE.toString()));
        params.add(new BasicNameValuePair(IGNORE_DEACTIVATED_PARAM, Boolean.FALSE.toString()));
        return params;
    }
}
