import { Modal, EmptySearchResult } from "@shopify/polaris";
import { useEffect, useState } from "react";
import GithubSimpleTable from "@/apps/dashboard/components/tables/GithubSimpleTable";
import { CellType } from "@/apps/dashboard/components/tables/rows/GithubRow"
import GetPrettifyEndpoint from "@/apps/dashboard/pages/observe/GetPrettifyEndpoint";
import TestingStore from "../testingStore";

const TestingEndpointsModal = ({ showTestingEndpointsModal, setShowTestingEndpointsModal }) => {

    const testingEndpointsApisList = TestingStore((state) => state.testingEndpointsApisList);

    const handleClose = () => {
        setShowTestingEndpointsModal(false);
    }

    const resourceName = {
        singular: 'API endpoint',
        plural: 'API endpoints',
    };

    const headers = [
         {
            text: 'API endpoint',
            title: 'API endpoint',
            value: 'apiEndpointComp'
        },
        {
            text: 'API collection name',
            title: 'API collection name',
            value: 'apiCollectionName',
            isText: CellType.TEXT
        }
    ]

    const emptyStateMarkup = (
        <EmptySearchResult
            title={'No APIs found'}
            withIllustration
        />
    );

    return (
        <Modal
            open={showTestingEndpointsModal}
            onClose={handleClose}
            title={"See APIs"}
            fullScreen={true}
        >
            <Modal.Section>
                <GithubSimpleTable
                    key="apisList"
                    data={testingEndpointsApisList}
                    resourceName={resourceName}
                    headers={headers}
                    useNewRow={true}
                    condensedHeight={true}
                    hideQueryField={true}
                    headings={headers}
                    pageLimit={10}
                    showFooter={false}
                    emptyStateMarkup={emptyStateMarkup}
                />
            </Modal.Section>
        </Modal>
    );
}

export default TestingEndpointsModal;