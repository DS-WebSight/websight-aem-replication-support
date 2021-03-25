import React from 'react';
import Button, { ButtonGroup } from '@atlaskit/button';
import ModalDialog, { ModalTransition } from '@atlaskit/modal-dialog';
import { Section } from '@atlaskit/menu';
import { Checkbox } from '@atlaskit/checkbox';
import Textfield from '@atlaskit/textfield';
import RestClient from 'websight-rest-atlaskit-client/RestClient';
import Form, { FormFooter } from 'websight-rest-atlaskit-client/Form';
import styled from 'styled-components';

const Message = styled.div`
    padding-top: 10px;
`;

export default class ResourcePublishModal extends React.PureComponent {
    constructor(props) {
        super(props);
        this.restClient = new RestClient('websight-aem-replication-support-service')

        this.state = {
            isOpen: false,
            resource: null,
            replicateChildren: false,
            displayWarning: false,
            submitDisabled: false
        }

        this.open = this.open.bind(this);
        this.close = this.close.bind(this);
        this.onFormSubmit = this.onFormSubmit.bind(this);
    }

    open(resource) {
        this.setState({ isOpen: true, resource: resource, displayWarning: (resource.path.split('/').length-1 <= 3) });
    }

    close() {
        this.setState({ isOpen: false });
    }

    onFormSubmit(data) {
        const action = data.replicateChildren === true ? 'recursive-publish' : 'publish';
        this.restClient.post({
            action: action,
            data: {
                path: this.state.resource.path,
                type: 'resource'
            },
            always: () => {
                this.close();
            }
        });
    }

    render() {
        const { isOpen, submitDisabled, displayWarning, replicateChildren, resource } = this.state;

        return (
            <ModalTransition>
                {isOpen && (
                    <>
                        <ModalDialog
                            onClose={this.close}
                            width='large'
                            heading='Publish resource'
                        >
                            <Form
                                onSubmit={(data) => this.onFormSubmit(data) }
                                onSuccess={this.close}
                            >
                                {({ submitted }) => (
                                    <>
                                        <Section hasSeparator={true}>
                                            <Checkbox
                                                defaultChecked={false}
                                                hideLabel={true}
                                                label={'Replicate children'}
                                                name='replicateChildren'
                                                onChange={(e) => {
                                                    this.setState({ replicateChildren : e.currentTarget.checked,
                                                        submitDisabled: (displayWarning && e.currentTarget.checked) });
                                                }}
                                            />
                                            {displayWarning && replicateChildren && (
                                                <>
                                                    <Message>
                                                        {'You are going to publish high level resource that potentially can have a lot of children'}
                                                    </Message>
                                                    <Message>
                                                        {'Type the following path to confirm: '}
                                                        <span style={{ fontWeight: 'bold' }}>{resource.path}</span>
                                                    </Message>
                                                    <Textfield
                                                        autoFocus
                                                        name='resPath'
                                                        label='Resource path'
                                                        isRequired
                                                        onChange={event => {
                                                            this.setState({ submitDisabled: resource.path !== event.target.value })
                                                        }}/>
                                                </>
                                            )}
                                        </Section>
                                        <FormFooter>
                                            <ButtonGroup>
                                                <Button
                                                    appearance='primary'
                                                    autoFocus={true}
                                                    type='submit'
                                                    isLoading={submitted}
                                                    isDisabled={submitDisabled}
                                                >
                                                    Publish
                                                </Button>
                                                <Button
                                                    appearance='subtle'
                                                    onClick={this.close}
                                                    isDisabled={submitted}
                                                >
                                                    Cancel
                                                </Button>
                                            </ButtonGroup>
                                        </FormFooter>
                                    </>
                                )}
                            </Form>
                        </ModalDialog>
                    </>
                )}
            </ModalTransition>
        );
    }
}
