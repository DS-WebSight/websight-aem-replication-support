package pl.ds.ws.websight.aem.replication.support.fragments;

import org.osgi.service.component.annotations.Component;
import pl.ds.websight.fragments.registry.WebFragment;

@Component
public class ResourcePublishActionWebFragment implements WebFragment {

    @Override
    public String getKey() {
        return "websight.admin.resourcebrowser.extra.actions";
    }

    @Override
    public String getFragment() {
        return "/apps/websight-aem-replication-support-view/web-resources/fragments/aem/replication/ResourcePublishAction.js";
    }

    @Override
    public int getRanking() {
        return 100;
    }
}
