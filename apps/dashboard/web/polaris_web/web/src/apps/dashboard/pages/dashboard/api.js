import request from "@/util/request";
const api = {
    getIssuesTrend: async (startTimeStamp, endTimeStamp) => {
        return await request({
            url: '/api/getIssuesTrend',
            method: 'post',
            data: { startTimeStamp, endTimeStamp }
        })
    },

    findTotalIssues: async (startTimeStamp, endTimeStamp) => {
        return await request({
            url: '/api/findTotalIssues',
            method: 'post',
            data: { startTimeStamp, endTimeStamp }
        })
    },

    fetchEndpointsCount: async (startTimestamp, endTimestamp) => {
        return await request({
            url: '/api/fetchEndpointsCount',
            method: 'post',
            data: { startTimestamp, endTimestamp }
        })
    },

    fetchApiStats: async (startTimestamp, endTimestamp) => {
        return await request({
            url: '/api/fetchApiStats',
            method: 'post',
            data: { startTimestamp, endTimestamp }
        })
    },

    fetchCriticalIssuesTrend: async (startTimeStamp, endTimeStamp, severityToFetch = []) => {
        return await request({
            url: '/api/fetchCriticalIssuesTrend',
            method: 'post',
            data: { startTimeStamp, endTimeStamp, severityToFetch }
        })
    },

    fetchSubTypeCountMap(startTimestamp, endTimestamp) {
        return request({
            url: '/api/fetchSubTypeCountMap',
            method: 'post',
            data: {
                startTimestamp,
                endTimestamp
            }
        })
    },

    fetchRecentFeed(skip) {
        return request({
            url: '/api/getRecentActivities',
            method: 'post',
            data: {
                skip
            }
        })
    },

    getIntegratedConnections: async () => {
        return await request({
            url: '/api/getIntegratedConnectionsInfo',
            method: 'post',
            data: {}
        })
    },

    skipConnection: async (skippedConnection) => {
        return request({
            url: '/api/markConnectionAsSkipped',
            method: 'post',
            data: {
                connectionSkipped: skippedConnection
            }
        })
    },

    getApiInfoForMissingData: async (startTimeStamp, endTimeStamp) => {
        return request({
            url: '/api/getAPIInfosForMissingData',
            method: 'post',
            data: {
                startTimeStamp,
                endTimeStamp
            }
        })
    },

    fetchSensitiveAndUnauthenticatedValue: async () => {
        return await request({
            url: '/api/getSensitiveAndUnauthenticatedValue',
            method: 'post',
            data: {}
        })
    },

    fetchHighRiskThirdPartyValue: async () => {
        return await request({
            url: '/api/getHighRiskThirdPartyValue',
            method: 'post',
            data: {}
        })
    },

    fetchShadowApisValue: async () => {
        return await request({
            url: '/api/getShadowApis',
            method: 'post',
            data: {}
        })
    }
}

export default api;