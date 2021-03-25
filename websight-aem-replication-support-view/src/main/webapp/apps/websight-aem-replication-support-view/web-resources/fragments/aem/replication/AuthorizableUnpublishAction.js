import React from 'react';
import RestClient from 'websight-rest-atlaskit-client/RestClient';
import ReplicationActionService from '../services/ReplicationActionService.js';
import ConfirmationModal from 'websight-admin/ConfirmationModal';

const AuthorizableUnpublishAction = {
    restClient: new RestClient('websight-aem-replication-support-service'),
    label: 'Unpublish',
    onClick() {
        this.actionModal.open();
    },
    isApplicable(path, callback) {
        ReplicationActionService.isApplicable(path, callback, callback(false));
    },
    unpublishAuthorizable(path, type) {
        this.restClient.post({
            action: 'unpublish',
            data: {
                path: path,
                type: type
            },
            always: () => {
                this.actionModal.close();
            }
        });
    },
    modal(authorizable) {
        const type = authorizable.path.startsWith('/home/users') ? 'user' : 'group';
        const heading = 'Unpublish ' + type;
        return (
            <ConfirmationModal
                buttonText={'Unpublish'}
                heading={heading}
                message={(<span>Are you sure you want to unpublish <b>{authorizable.displayName}</b>?</span>)}
                onConfirm={() => this.unpublishAuthorizable(authorizable.path, type)}
                ref={(element) => this.actionModal = element}
            />
        )
    }
};
export default AuthorizableUnpublishAction;