package pl.ds.ws.websight.aem.replication.support.fragments;

import org.osgi.service.component.annotations.Component;
import pl.ds.websight.fragments.registry.WebFragment;

@Component
public class PackagePublishActionWebFragment implements WebFragment {

    @Override
    public String getKey() {
        return "websight.admin.packagemanager.extra.actions";
    }

    @Override
    public String getFragment() {
        return "/apps/websight-aem-replication-support-view/web-resources/fragments/aem/replication/PackagePublishAction.js";
    }

    @Override
    public int getRanking() {
        return 100;
    }
}
