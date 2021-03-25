package pl.ds.websight.aem.replication.support.rest;

import org.apache.jackrabbit.api.security.JackrabbitAccessControlManager;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ds.websight.rest.framework.RestAction;
import pl.ds.websight.rest.framework.RestActionResult;
import pl.ds.websight.rest.framework.annotations.SlingAction;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.security.Privilege;

import static pl.ds.websight.rest.framework.annotations.SlingAction.HttpMethod.GET;

@Component
@SlingAction(GET)
public class IsActionApplicableRestAction implements RestAction<ReplicateRestModel, Boolean> {

    private static final Logger LOG = LoggerFactory.getLogger(IsActionApplicableRestAction.class);

    private static final String REPLICATION_PRIVILEGE_NAME = "crx:replicate";

    @Override
    public RestActionResult<Boolean> perform(ReplicateRestModel model) {
        boolean result = false;
        String path = model.getPath();
        Session session = model.getResolver().adaptTo(Session.class);
        try {
            JackrabbitAccessControlManager accessControlManager = (JackrabbitAccessControlManager) session.getAccessControlManager();
            Privilege replicatePrivilege = accessControlManager.privilegeFromName(REPLICATION_PRIVILEGE_NAME);
            result = accessControlManager.hasPrivileges(path, new Privilege[] { replicatePrivilege });
            if (!result && replicatePrivilege.isAggregate()) {
                result = accessControlManager
                        .hasPrivileges(path, replicatePrivilege.getAggregatePrivileges());
            }

        } catch (RepositoryException e) {
            LOG.warn("Failed to check user permissions", e);
        }
        return RestActionResult.success(result);
    }
}
