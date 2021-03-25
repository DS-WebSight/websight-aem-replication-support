import React from 'react';
import RestClient from 'websight-rest-atlaskit-client/RestClient';
import ReplicationActionService from '../services/ReplicationActionService.js';
import ConfirmationModal from 'websight-admin/ConfirmationModal';

const PackageUnpublishAction = {
    restClient: new RestClient('websight-aem-replication-support-service'),
    label: 'Unpublish',
    onClick() {
        this.actionModal.open();
    },
    isApplicable(path, callback) {
        ReplicationActionService.isApplicable(path, callback, callback(false));
    },
    unpublishPackage(path) {
        this.restClient.post({
            action: 'unpublish',
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
                buttonText={'Unpublish'}
                heading={'Unpublish package'}
                message={(<span>Are you sure you want to unpublish <b>{packageData.name}</b>?</span>)}
                onConfirm={() => this.unpublishPackage(packageData.path)}
                ref={(element) => this.actionModal = element}
            />
        )
    }
};
export default PackageUnpublishAction;
