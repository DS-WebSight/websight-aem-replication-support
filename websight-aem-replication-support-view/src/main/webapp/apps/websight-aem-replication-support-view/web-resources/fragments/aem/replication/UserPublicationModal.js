import React from 'react';
import Button, { ButtonGroup } from '@atlaskit/button';
import ModalDialog, { ModalTransition } from '@atlaskit/modal-dialog';
import { Section } from '@atlaskit/menu';
import { Checkbox } from '@atlaskit/checkbox';
import RestClient from 'websight-rest-atlaskit-client/RestClient';
import Form, { FormFooter } from 'websight-rest-atlaskit-client/Form';
import styled from 'styled-components';

const Message = styled.div`
    padding-top: 10px;
    padding-bottom: 10px;
`;

export default class UserPublicationModal extends React.PureComponent {
    constructor(props) {
        super(props);
        this.restClient = new RestClient('websight-aem-replication-support-service')

        this.state = {
            isOpen: false,
            publishGroups: new Set()
        }

        this.open = this.open.bind(this);
        this.close = this.close.bind(this);
        this.onFormSubmit = this.onFormSubmit.bind(this);
    }

    open() {
        this.setState({ isOpen: true });
    }

    close() {
        this.setState({ isOpen: false });
    }

    onFormSubmit() {
        this.restClient.post({
            action: 'user-publish',
            data: {
                path: this.props.user.path,
                type: 'user',
                publishGroups: Array.from(this.state.publishGroups)
            },
            always: () => {
                this.close();
            }
        });
    }

    render() {
        const { isOpen, publishGroups } = this.state;
        const { user } = this.props;

        return (
            <ModalTransition>
                {isOpen && (
                    <>
                        <ModalDialog
                            onClose={this.close}
                            heading='Publish user'
                            width='large'
                        >
                            <Form
                                onSubmit={(data) => this.onFormSubmit(data) }
                                onSuccess={this.close}
                            >
                                {({ submitted }) => (
                                    <>
                                        <Section hasSeparator={true}>
                                            <Message>
                                                {'Select groups to publish'}
                                            </Message>
                                            { user.groups.map((group, index) => {
                                                return (
                                                    <Checkbox
                                                        key={index}
                                                        defaultChecked={false}
                                                        hideLabel={true}
                                                        label={group.displayName}
                                                        value={group.path}
                                                        onChange={(e) => {
                                                            if (e.currentTarget.checked) {
                                                                publishGroups.add(e.currentTarget.value)
                                                            } else {
                                                                publishGroups.delete(e.currentTarget.value)
                                                            }
                                                            this.setState({ publishGroups : publishGroups })
                                                        }}
                                                    />)
                                            }) }
                                        </Section>
                                        <FormFooter>
                                            <ButtonGroup>
                                                <Button
                                                    autoFocus={true}
                                                    appearance='primary'
                                                    type='submit'
                                                    isLoading={submitted}
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
