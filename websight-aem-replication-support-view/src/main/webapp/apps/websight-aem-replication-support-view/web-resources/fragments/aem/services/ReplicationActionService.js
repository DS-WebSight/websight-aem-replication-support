import RestClient from 'websight-rest-atlaskit-client/RestClient';

class ReplicationActionService {
    constructor() {
        this.client = new RestClient('websight-aem-replication-support-service');
    }

    isApplicable(path, onSuccess, onFailure) {
        this.client.get({
            action: 'is-action-applicable',
            parameters: {
                path: path
            },
            onSuccess: ({ entity }) => onSuccess(entity),
            onValidationFailure: onFailure,
            onFailure: onFailure,
            onError: onFailure,
            onNonFrameworkError: onFailure
        })
    }
}

export default new ReplicationActionService();