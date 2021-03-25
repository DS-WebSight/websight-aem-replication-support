package pl.ds.websight.aem.replication.support.rest;

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
public class UserPublishRestAction extends ReplicateRestAction implements RestAction<UserPublishRestModel, Void> {

    @Override
    public RestActionResult<Void> perform(UserPublishRestModel model) {
        String failedUserPath = performReplication(model, ReplicationActionType.ACTIVATE) ?
                null : model.getPath();
        List<String> failedGroupPaths = new ArrayList<>();
        if (failedUserPath == null && model.getPublishGroups() != null) {
            for (String group : model.getPublishGroups()) {
                if (!performReplicationAction(model.getRequest(), group, ReplicationActionType.ACTIVATE)) {
                    failedGroupPaths.add(group);
                }
            }
        }
        return failedUserPath == null && failedGroupPaths.isEmpty() ?
                RestActionResult.success("User published", String.format("User at %s successfully published", model.getPath())) :
                RestActionResult.failure("Publication failed", getFailureMessage(failedUserPath, failedGroupPaths));
    }

    private String getFailureMessage(String failedUser, List<String> failedGroups) {
        if (failedUser != null) {
            return "Failed to publish user at " + failedUser;
        } else {
            return "User published but failed to publish the following groups: " + String.join(", ", failedGroups);
        }
    }
}
