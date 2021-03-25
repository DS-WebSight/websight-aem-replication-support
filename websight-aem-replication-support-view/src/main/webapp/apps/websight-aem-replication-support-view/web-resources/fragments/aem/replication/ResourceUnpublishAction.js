import React from 'react';
import RestClient from 'websight-rest-atlaskit-client/RestClient';
import ReplicationActionService from '../services/ReplicationActionService.js';
import ResourceUnpublishModal from './ResourceUnpublishModal.js';

const ResourceUnpublishAction = {
    restClient: new RestClient('websight-aem-replication-support-service'),
    label: 'Unpublish',
    icon: 'public_off',
    onClick(resource) {
        this.actionModal.open(resource);
    },
    isApplicable(path, callback) {
        ReplicationActionService.isApplicable(path, callback, callback(false));
    },
    unpublishResource(path) {
        this.restClient.post({
            action: 'resource-unpublish',
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
            <ResourceUnpublishModal
                ref={(element) => this.actionModal = element}
            />
        )
    }
};
export default ResourceUnpublishAction;
