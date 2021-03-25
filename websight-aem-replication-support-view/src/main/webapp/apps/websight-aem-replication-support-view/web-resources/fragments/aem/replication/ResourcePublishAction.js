import React from 'react';
import RestClient from 'websight-rest-atlaskit-client/RestClient';
import ReplicationActionService from '../services/ReplicationActionService.js';
import ResourcePublishModal from './ResourcePublishModal.js'

const ResourcePublishAction = {
    restClient: new RestClient('websight-aem-replication-support-service'),
    label: 'Publish',
    icon: 'public',
    onClick(resource) {
        this.actionModal.open(resource);
    },
    isApplicable(path, callback) {
        ReplicationActionService.isApplicable(path, callback, callback(false));
    },
    publishResource(path) {
        this.restClient.post({
            action: 'publish',
            data: {
                path: path,
                type: 'resource'
            },
            always: () => {
                this.actionModal.close();
            }
        });
    },
    modal() {
        return (
            <ResourcePublishModal
                ref={(element) => this.actionModal = element}
            />
        )
    }
};
export default ResourcePublishAction;
