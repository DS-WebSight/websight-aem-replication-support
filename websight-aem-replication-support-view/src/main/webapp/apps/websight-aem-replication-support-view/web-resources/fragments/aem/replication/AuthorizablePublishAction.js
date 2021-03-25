import React from 'react';
import RestClient from 'websight-rest-atlaskit-client/RestClient';
import UserPublicationModal from './UserPublicationModal.js'
import ReplicationActionService from '../services/ReplicationActionService.js';
import ConfirmationModal from 'websight-admin/ConfirmationModal';

const AuthorizablePublishAction = {
    restClient: new RestClient('websight-aem-replication-support-service'),
    label: 'Publish',
    onClick() {
        this.actionModal.open();
    },
    isApplicable(path, callback) {
        ReplicationActionService.isApplicable(path, callback, callback(false));
    },
    publishGroup(path) {
        this.restClient.post({
            action: 'publish',
            data: {
                path: path,
                type: 'group'
            },
            always: () => {
                this.actionModal.close();
            }
        });
    },
    modal(authorizable) {
        if (authorizable.path.startsWith('/home/user')) {
            return (
                <UserPublicationModal
                    user={authorizable}
                    ref={(element) => this.actionModal = element}
                >
                </UserPublicationModal>
            )
        } else {
            return (
                <ConfirmationModal
                    buttonText={'Publish'}
                    heading={'Publish group'}
                    message={(<span>Are you sure you want to publish <b>{authorizable.displayName}</b>?</span>)}
                    onConfirm={() => this.publishGroup(authorizable.path)}
                    ref={(element) => this.actionModal = element}
                />
            )
        }
    }
};
export default AuthorizablePublishAction;
