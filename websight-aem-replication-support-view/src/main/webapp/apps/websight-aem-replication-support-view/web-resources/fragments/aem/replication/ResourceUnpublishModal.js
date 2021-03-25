import React from 'react';
import Button, { ButtonGroup } from '@atlaskit/button';
import { Section } from '@atlaskit/menu';
import ModalDialog, { ModalTransition } from '@atlaskit/modal-dialog';
import Textfield from '@atlaskit/textfield';
import Form, { FormFooter } from 'websight-rest-atlaskit-client/Form';
import RestClient from 'websight-rest-atlaskit-client/RestClient';
import styled from 'styled-components';

const Message = styled.div`
    padding-top: 10px;
`;

export default class ResourceUnpublishModal extends React.PureComponent {
    constructor(props) {
        super(props);
        this.restClient = new RestClient('websight-aem-replication-support-service')

        this.state = {
            isOpen: false,
            submitDisabled: true,
            resource: null
        }

        this.open = this.open.bind(this);
        this.close = this.close.bind(this);
        this.onFormSubmit = this.onFormSubmit.bind(this);
    }

    open(resource) {
        this.setState({ isOpen: true, resource: resource, submitDisabled: true });
    }

    close() {
        this.setState({ isOpen: false });
    }

    onFormSubmit() {
        this.restClient.post({
            action: 'resource-unpublish',
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
        const { isOpen, submitDisabled, resource } = this.state;

        return (
            <ModalTransition>
                {isOpen && (
                    <>
                        <ModalDialog
                            onClose={this.close}
                            heading='Unpublish resource'
                            width='large'
                        >
                            <Form
                                onSubmit={this.onFormSubmit}
                                onSuccess={this.close}
                            >
                                {({ submitted }) => (
                                    <>
                                        <Section hasSeparator={true}>
                                            <Message>
                                                {'The resource and all it\'s children will be removed from publish instances'}
                                            </Message>
                                            <Message>
                                                {'Type the following path to confirm: '}
                                                <span style={{ fontWeight: 'bold' }}>{resource.path}</span>
                                            </Message>
                                            <Textfield
                                                name='path'
                                                label='Resource path'
                                                isRequired
                                                autoFocus
                                                onChange={event => {
                                                    this.setState({ submitDisabled: resource.path !== event.target.value })
                                                }}/>
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
                                                    Unpublish
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
