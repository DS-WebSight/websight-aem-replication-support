package pl.ds.websight.aem.replication.support.rest;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import pl.ds.websight.request.parameters.support.annotations.RequestParameter;

import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class)
public class UserPublishRestModel extends ReplicateRestModel {

    @RequestParameter
    private List<String> publishGroups;

    public List<String> getPublishGroups() {
        return publishGroups;
    }
}
