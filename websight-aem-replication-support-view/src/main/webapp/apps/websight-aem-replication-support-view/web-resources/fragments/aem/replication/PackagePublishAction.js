import React from 'react';
import RestClient from 'websight-rest-atlaskit-client/RestClient';
import ReplicationActionService from '../services/ReplicationActionService.js';
import ConfirmationModal from 'websight-admin/ConfirmationModal';

const PackagePublishAction = {
    restClient: new RestClient('websight-aem-replication-support-service'),
    label: 'Publish',
    onClick() {
        this.actionModal.open();
    },
    isApplicable(path, callback) {
        ReplicationActionService.isApplicable(path, callback, callback(false));
    },
    publishPackage(path) {
        this.restClient.post({
            action: 'publish',
            data: {
                path: path,
                type: 'package'
            },
            always: () => {
                this.actionModal.close();
            }
        });
    },
    modal(packageData) {
        return (
            <ConfirmationModal
                buttonText={'Publish'}
                heading={'Publish package'}
                message={(<span>Are you sure you want to publish <b>{packageData.name}</b>?</span>)}
                onConfirm={() => this.publishPackage(packageData.path)}
                ref={(element) => this.actionModal = element}
            />
        )
    }
};
export default PackagePublishAction;
