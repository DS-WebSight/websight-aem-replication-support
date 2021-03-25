package pl.ds.websight.aem.replication.support.rest;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.websight.request.parameters.support.annotations.RequestParameter;

import javax.validation.constraints.NotBlank;

@Model(adaptables = SlingHttpServletRequest.class)
public class ReplicateRestModel {

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private ResourceResolver resolver;

    @RequestParameter
    private String type;

    @RequestParameter
    @NotBlank(message = "Path can not be blank")
    private String path;

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public ResourceResolver getResolver() {
        return resolver;
    }

    public SlingHttpServletRequest getRequest() {
        return request;
    }
}