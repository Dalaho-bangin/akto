import React from 'react';
import { PromptComposer } from './PromptComposer';
import { AgentHeader } from './AgentHeader';
import { Box, Scrollable } from '@shopify/polaris';
import FlyLayout from '../../../components/layouts/FlyLayout';
import AgentWindowCore from './AgentWindowCore';
import AgentFinalCTA from './finalctas/AgentFinalCTA';

interface AgentWindowProps {
    onClose: () => void;
    open: boolean;
}

function AgentWindow({ onClose, open }: AgentWindowProps) {

    return (
        <FlyLayout
            show={open}
            setShow={() => { }}
            isHandleClose={true}
            handleClose={onClose}
            title={"Agent Details"}
            components={[
                <div>
                    <AgentHeader />
                    <div className="h-[calc(100vh-172px)] flex flex-col overflow-y-auto px-4 pb-5">
                        <div className="flex-1 min-h-0">
                            <Scrollable className="h-full">
                                <div className="pt-2 flex flex-col gap-2">
                                    <Box paddingBlockEnd={"8"}>
                                        <AgentWindowCore />
                                        <AgentFinalCTA />
                                    </Box>
                                </div>
                            </Scrollable>
                        </div>
                        <br/>
                        <PromptComposer onSend={console.log} />
                    </div>
                </div >
            ]}
        />
    )
}

export default AgentWindow;