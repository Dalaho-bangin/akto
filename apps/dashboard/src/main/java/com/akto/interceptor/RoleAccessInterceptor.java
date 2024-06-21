package com.akto.interceptor;

import com.akto.dao.RBACDao;
import com.akto.dao.billing.OrganizationsDao;
import com.akto.dto.RBAC;
import com.akto.dto.User;
import com.akto.dto.billing.FeatureAccess;
import com.akto.dto.billing.Organization;
import com.akto.dto.RBAC.Role;
import com.akto.dto.rbac.RbacEnums;
import com.akto.dto.rbac.RbacEnums.Feature;
import com.akto.dto.rbac.RbacEnums.ReadWriteAccess;
import com.akto.filter.UserDetailsFilter;
import com.akto.log.LoggerMaker;
import com.akto.util.DashboardMode;
import com.mongodb.client.model.Filters;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import java.util.HashMap;
import java.util.Map;

public class RoleAccessInterceptor extends AbstractInterceptor {

    private static final LoggerMaker loggerMaker = new LoggerMaker(RoleAccessInterceptor.class, LoggerMaker.LogDb.DASHBOARD);

    String featureLabel;
    String accessType;

    public void setFeatureLabel(String featureLabel) {
        this.featureLabel = featureLabel;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public final static String FORBIDDEN = "FORBIDDEN";
    private final static String USER_ID = "userId";
    private final static String USER = "user";
    private final static String FEATURE_LABEL_STRING = "RBAC_FEATURE";

    private boolean checkForPaidFeature(int accountId){
        if(!DashboardMode.isMetered()){
            return false;
        }
        Organization organization = OrganizationsDao.instance.findOne(Filters.in(Organization.ACCOUNTS, accountId));
        if(organization == null || organization.getFeatureWiseAllowed() == null || organization.getFeatureWiseAllowed().isEmpty()){
            return false;
        }

        HashMap<String, FeatureAccess> featureWiseAllowed = organization.getFeatureWiseAllowed();
        FeatureAccess featureAccess = featureWiseAllowed.getOrDefault(FEATURE_LABEL_STRING, FeatureAccess.noAccess);
        return featureAccess.getIsGranted();
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        try {

            if(featureLabel == null) {
                throw new Exception("Feature list is null or empty");
            }

            Map<String, Object> session = invocation.getInvocationContext().getSession();
            User user = (User) session.get(USER);
            int sessionAccId = (int) session.get(UserDetailsFilter.ACCOUNT_ID);

            if(!(checkForPaidFeature(sessionAccId) || featureLabel.equalsIgnoreCase(RbacEnums.Feature.ADMIN_ACTIONS.toString()))){
                return invocation.invoke();
            }

            if(user == null) {
                throw new Exception("User not found in session");
            }

            int userId = user.getId();

            String userRole = RBACDao.instance.findOne(Filters.eq(USER_ID, userId)).getRole().name().toUpperCase();

            if(userRole == null || userRole.isEmpty()) {
                throw new Exception("User role not found");
            }

            Role userRoleType = Role.valueOf(userRole.toUpperCase());
            Feature featureType = Feature.valueOf(this.featureLabel.toUpperCase());

            ReadWriteAccess accessGiven = userRoleType.getReadWriteAccessForFeature(featureType);
            boolean hasRequiredAccess = false;

            if(this.accessType.equalsIgnoreCase(ReadWriteAccess.READ.toString()) || this.accessType.equalsIgnoreCase(accessGiven.toString())){
                hasRequiredAccess = true;
            }
            
            if(!hasRequiredAccess) {
                ((ActionSupport) invocation.getAction()).addActionError("The role '" + userRoleType.getName() + "' does not have access.");
                return FORBIDDEN;
            }
        } catch(Exception e) {
            String api = invocation.getProxy().getActionName();
            String error = "Error in RoleInterceptor for api: " + api + " ERROR: " + e.getMessage();
            loggerMaker.errorAndAddToDb(e, error, LoggerMaker.LogDb.DASHBOARD);
        }

        return invocation.invoke();
    }
}
