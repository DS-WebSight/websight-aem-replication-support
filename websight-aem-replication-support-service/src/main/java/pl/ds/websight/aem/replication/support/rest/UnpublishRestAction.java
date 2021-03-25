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
public class UnpublishRestAction extends ReplicateRestAction implements RestAction<ReplicateRestModel, Void> {

    @Override
    public RestActionResult<Void> perform(ReplicateRestModel model) {
        String failedPath = performReplication(model, ReplicationActionType.DEACTIVATE) ?
                null : model.getPath();
        String type = model.getType();

        return failedPath == null ?
                RestActionResult.success(String.format("%s unpublished", StringUtils.capitalize(type)),
                        String.format("%s at %s successfully unpublished", StringUtils.capitalize(type), model.getPath())) :
                RestActionResult.failure(String.format("%s unpublication failed", StringUtils.capitalize(type)),
                        String.format("Failed to unpublish %s at %s", type, model.getPath()));
    }
}