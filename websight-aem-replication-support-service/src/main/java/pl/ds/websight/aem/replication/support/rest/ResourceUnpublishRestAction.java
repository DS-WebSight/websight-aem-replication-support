package pl.ds.websight.aem.replication.support.rest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.osgi.service.component.annotations.Component;
import pl.ds.websight.aem.replication.support.ReplicationActionType;
import pl.ds.websight.rest.framework.RestAction;
import pl.ds.websight.rest.framework.RestActionResult;
import pl.ds.websight.rest.framework.annotations.SlingAction;

import java.util.ArrayList;
import java.util.List;

import static pl.ds.websight.rest.framework.annotations.SlingAction.HttpMethod.POST;

@Component
@SlingAction(POST)
public class ResourceUnpublishRestAction extends ReplicateRestAction implements RestAction<ReplicateRestModel, Void> {

    @Override
    public RestActionResult<Void> perform(ReplicateRestModel model) {
        String failedPath = performReplication(model, null) ?
                null : model.getPath();
        String type = model.getType();

        return failedPath == null ?
                RestActionResult.success(String.format("%s unpublished", StringUtils.capitalize(type)),
                        String.format("%s at %s successfully unpublished with all child resources", StringUtils.capitalize(type),
                                model.getPath())) :
                RestActionResult.failure(String.format("%s unpublication failed", StringUtils.capitalize(type)),
                        String.format("Failed to unpublished %s at %s", type, model.getPath()));
    }

    private static final String AEM_DELETE_REPLICATION_ENDPOINT = "/crx/de/replication.jsp";
    private static final String AEM_REPLICATION_ACTION_PARAM_NAME = "action";
    private static final String AEM_REPLICATION_ACTION_DELETE_PARAM = "replicatedelete";

    @Override
    protected String getAemReplicationEndpoint() {
        return AEM_DELETE_REPLICATION_ENDPOINT;
    }

    @Override
    protected List<NameValuePair> createPostParameters(String path, ReplicationActionType command) {
        List<NameValuePair> params = super.createPostParameters(path, command);
        params.add(new BasicNameValuePair(AEM_REPLICATION_ACTION_PARAM_NAME, AEM_REPLICATION_ACTION_DELETE_PARAM));
        return params;
    }
}