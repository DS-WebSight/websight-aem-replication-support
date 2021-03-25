package pl.ds.ws.websight.aem.replication.support.fragments;

import org.osgi.service.component.annotations.Component;
import pl.ds.websight.fragments.registry.WebFragment;

@Component
public class AuthorizablePublishActionWebFragment implements WebFragment {

    @Override
    public String getKey() {
        return "websight.admin.usermanager.extra.actions";
    }

    @Override
    public String getFragment() {
        return "/apps/websight-aem-replication-support-view/web-resources/fragments/aem/replication/AuthorizablePublishAction.js";
    }

    @Override
    public int getRanking() {
        return 100;
    }
}
