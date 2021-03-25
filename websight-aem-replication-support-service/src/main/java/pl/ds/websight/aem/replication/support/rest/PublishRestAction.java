package pl.ds.websight.aem.replication.support.rest;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import pl.ds.websight.aem.replication.support.ReplicationActionType;
import pl.ds.websight.rest.framework.RestAction;
import pl.ds.websight.rest.framework.RestActionResult;
import pl.ds.websight.rest.framework.annotations.SlingAction;

import static pl.ds.websight.rest.framework.annotations.SlingAction.HttpMethod.POST;

@Component
@SlingAction(POST)
public class PublishRestAction extends ReplicateRestAction implements RestAction<ReplicateRestModel, Void> {

    @Override
    public RestActionResult<Void> perform(ReplicateRestModel model) {
        String failedPath = performReplication(model, ReplicationActionType.ACTIVATE) ?
                null : model.getPath();
        String type = StringUtils.capitalize(model.getType());

        return failedPath == null ?
                RestActionResult.success(String.format("%s published", type),
                        String.format("%s at %s successfully published", type, model.getPath())) :
                RestActionResult.failure(String.format("%s publication failed", type),
                        String.format("Failed to publish %s at %s", StringUtils.uncapitalize(type), failedPath));
    }
}