package com.akto.action;


import com.akto.billing.UsageMetricUtils;
import com.akto.dao.AccountSettingsDao;
import com.akto.dao.AccountsDao;
import com.akto.dao.JiraIntegrationDao;
import com.akto.dao.RBACDao;
import com.akto.dao.UsersDao;
import com.akto.dao.billing.OrganizationsDao;
import com.akto.dao.context.Context;
import com.akto.dto.Account;
import com.akto.dto.AccountSettings;
import com.akto.dto.JiraIntegration;
import com.akto.dto.RBAC;
import com.akto.dto.User;
import com.akto.dto.UserAccountEntry;
import com.akto.dto.ApiToken.Utility;
import com.akto.dto.billing.FeatureAccess;
import com.akto.dto.billing.Organization;
import com.akto.listener.InitializerListener;
import com.akto.log.LoggerMaker;
import com.akto.util.Constants;
import com.akto.util.EmailAccountName;
import com.akto.utils.Intercom;
import com.akto.util.DashboardMode;
import com.akto.utils.billing.OrganizationUtils;
import com.akto.utils.cloud.Utils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;

import io.micrometer.core.instrument.util.StringUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.in;

public class ProfileAction extends UserAction {

    private static final LoggerMaker loggerMaker = new LoggerMaker(ProfileAction.class);
    private static final Logger logger = LoggerFactory.getLogger(ProfileAction.class);


    private int accountId;

    @Override
    public String execute() {

        return SUCCESS.toUpperCase();
    }

    public static void executeMeta1(Utility utility, User user, HttpServletRequest request, HttpServletResponse response) {
        BasicDBObject userDetails = new BasicDBObject();
        BasicDBObject accounts = new BasicDBObject();
        Integer sessionAccId = Context.accountId.get();
        if (sessionAccId == null || sessionAccId == 0) {
            sessionAccId = 0;
        }

        List<Integer> accountIdInt = new ArrayList<>();
        for (UserAccountEntry uae: user.getAccounts().values()) {
            accountIdInt.add(uae.getAccountId());
            if (uae.isDefault() && sessionAccId == 0) {
                sessionAccId = uae.getAccountId();
            }
        }

        for (Account acc: AccountsDao.instance.findAll(in("_id", accountIdInt))) {
            accounts.append(acc.getId()+"", acc.getName());
            if (sessionAccId == 0) {
                sessionAccId = acc.getId();
            }
        };

        if (sessionAccId == 0) {
            throw new IllegalStateException("user has no accounts associated");
        } else {
            logger.error("setting session: " + sessionAccId);
            request.getSession().setAttribute("accountId", sessionAccId);
            Context.accountId.set(sessionAccId);
        }

        BasicDBList listDashboards = new BasicDBList();
        Account currAccount = AccountsDao.instance.findOne(Filters.eq(Constants.ID, sessionAccId),Projections.include("name"));

        AccountSettings accountSettings = AccountSettingsDao.instance.findOne(AccountSettingsDao.generateFilter());
        boolean showOnboarding = accountSettings == null ? true : accountSettings.isShowOnboarding();

        if (showOnboarding && request.getRequestURI().startsWith("/dashboard") && !request.getRequestURI().equals("/dashboard/onboarding")) {
            try {
                response.sendRedirect("/dashboard/onboarding"); 
            }  catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        String username = user.getLogin();

        EmailAccountName emailAccountName = new EmailAccountName(username); // username is the email id of the current user
        String accountName = emailAccountName.getAccountName();
        if(currAccount != null && !currAccount.getName().isEmpty() && !currAccount.getName().equals("My account")){
            accountName = currAccount.getName();
        }
        String timeZone = "US/Pacific";
        if(currAccount != null && !currAccount.getTimezone().isEmpty()){
            timeZone = currAccount.getTimezone();
        }
        String dashboardVersion = accountSettings.getDashboardVersion();
        String[] versions = dashboardVersion.split(" - ");
        User userFromDB = UsersDao.instance.findOne(Filters.eq(Constants.ID, user.getId()));
        RBAC.Role userRole = RBACDao.getCurrentRoleForUser(user.getId(), Context.accountId.get());

        boolean jiraIntegrated = false;
        try {
            JiraIntegration jiraIntegration = JiraIntegrationDao.instance.findOne(new BasicDBObject());
            if (jiraIntegration != null) {
                jiraIntegrated = true;
            }
        } catch (Exception e) {
        }

        long countUser = UsersDao.instance.count(Filters.and(
                Filters.gt(User.NAME_LAST_UPDATE, 0),
                Filters.in("login", username)
        ));

        Organization org = OrganizationsDao.instance.findOne(Filters.and(
                Filters.in(Organization.ACCOUNTS, sessionAccId),
                Filters.gt(Organization.NAME_LAST_UPDATE, 0)
        ));
        boolean shouldAskWelcomeBackDetails = DashboardMode.isMetered() && (countUser == 0 || (userRole.getName().equals(RBAC.Role.ADMIN.getName()) && org == null));

        userDetails.append("accounts", accounts)
                .append("username",username)
                .append("userFullName", user.getName())
                .append("avatar", "dummy")
                .append("activeAccount", sessionAccId)
                .append("dashboardMode", DashboardMode.getDashboardMode())
                .append("isSaas","true".equals(System.getenv("IS_SAAS")))
                .append("users", UsersDao.instance.getAllUsersInfoForTheAccount(Context.accountId.get()))
                .append("cloudType", Utils.getCloudType())
                .append("accountName", accountName)
                .append("aktoUIMode", userFromDB.getAktoUIMode().name())
                .append("jiraIntegrated", jiraIntegrated)
                .append("userRole", userRole.toString().toUpperCase())
                .append("currentTimeZone", timeZone)
                .append("shouldAskWelcomeBackDetails", shouldAskWelcomeBackDetails);

        if(org != null) {
            userDetails.append("organizationName", org.getName());
        }

        if (DashboardMode.isOnPremDeployment()) {
            userDetails.append("userHash", Intercom.getUserHash(user.getLogin()));
        }

        // only external API calls have non-null "utility"
        if (DashboardMode.isMetered() &&  utility == null) {
            Organization organization = OrganizationsDao.instance.findOne(
                    Filters.in(Organization.ACCOUNTS, sessionAccId)
            );
            if(organization == null){
                loggerMaker.infoAndAddToDb("Org not found for user: " + username + " acc: " + sessionAccId + ", creating it now!", LoggerMaker.LogDb.DASHBOARD);
                InitializerListener.createOrg(sessionAccId);
                organization = OrganizationsDao.instance.findOne(
                        Filters.in(Organization.ACCOUNTS, sessionAccId)
                );
            }
            String organizationId = organization.getId();

            HashMap<String, FeatureAccess> initialFeatureWiseAllowed = organization.getFeatureWiseAllowed();
            if(initialFeatureWiseAllowed == null) {
                initialFeatureWiseAllowed = new HashMap<>();
            }

            boolean isOverage = false;
            HashMap<String, FeatureAccess> featureWiseAllowed = new HashMap<>();
            int gracePeriod = organization.getGracePeriod();
            try {

                organization = InitializerListener.fetchAndSaveFeatureWiseAllowed(organization);
                gracePeriod = organization.getGracePeriod();
                featureWiseAllowed = organization.getFeatureWiseAllowed();
                if (featureWiseAllowed == null) {
                    featureWiseAllowed = new HashMap<>();
                }

                isOverage = OrganizationUtils.isOverage(featureWiseAllowed);
            } catch (Exception e) {
                loggerMaker.errorAndAddToDb(e,"Customer not found in stigg. User: " + username + " org: " + organizationId + " acc: " + accountIdInt, LoggerMaker.LogDb.DASHBOARD);
            }

            userDetails.append("organizationId", organizationId);
            userDetails.append("organizationName", organization.getName());
            userDetails.append("stiggIsOverage", isOverage);
            BasicDBObject stiggFeatureWiseAllowed = new BasicDBObject();
            for (String key : featureWiseAllowed.keySet()) {
                FeatureAccess featureAccess = featureWiseAllowed.get(key);
                featureAccess.setGracePeriod(gracePeriod);
                stiggFeatureWiseAllowed.append(key, new BasicDBObject()
                        .append(FeatureAccess.IS_GRANTED, featureAccess.getIsGranted())
                        .append(FeatureAccess.USAGE_LIMIT, featureAccess.getUsageLimit())
                        .append(FeatureAccess.USAGE, featureAccess.getUsage())
                        .append(FeatureAccess.OVERAGE_FIRST_DETECTED, featureAccess.getOverageFirstDetected())
                        .append(FeatureAccess.IS_OVERAGE_AFTER_GRACE, featureAccess.checkInvalidAccess()));
            }

            boolean dataIngestionPaused = UsageMetricUtils.checkActiveEndpointOverage(sessionAccId);
            boolean testRunsPaused = UsageMetricUtils.checkTestRunsOverage(sessionAccId);
            userDetails.append("usagePaused", new BasicDBObject()
                    .append("dataIngestion", dataIngestionPaused)
                    .append("testRuns", testRunsPaused));

            userDetails.append("stiggFeatureWiseAllowed", stiggFeatureWiseAllowed);

            userDetails.append("stiggCustomerId", organizationId);
            userDetails.append("stiggCustomerToken", OrganizationUtils.fetchSignature(organizationId, organization.getAdminEmail()));
            userDetails.append("stiggClientKey", OrganizationUtils.fetchClientKey(organizationId, organization.getAdminEmail()));
            userDetails.append("expired", organization.checkExpirationWithAktoSync());
            userDetails.append("hotjarSiteId", organization.getHotjarSiteId());
        }

        if (versions.length > 2) {
            if (versions[2].contains("akto-release-version")) {
                userDetails.append("releaseVersion", "akto-release-version");
            } else {
                userDetails.append("releaseVersion", versions[2]);
            }
        }

        for (String k: userDetails.keySet()) {
            request.setAttribute(k, userDetails.get(k));
        }

        return;
    }

    private String type;
    private String username;
    private String avatar;
    private Collection<UserAccountEntry> accounts;
    private UserAccountEntry activeAccount;
    private BasicDBList users;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Collection<UserAccountEntry> getAccounts() {
        return accounts;
    }

    public void setAccounts(Collection<UserAccountEntry> userAccountEntries) {
        this.accounts = userAccountEntries;
    }

    public UserAccountEntry getActiveAccount() {
        return activeAccount;
    }

    public void setActiveAccount(UserAccountEntry userAccountEntry) {
        this.activeAccount = userAccountEntry;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public BasicDBList getUsers() {
        return users;
    }

    private BasicDBObject subscription;

    public BasicDBObject getSubscription() {
        return subscription;
    }

    public void setSubscription(BasicDBObject subscription) {
        this.subscription = subscription;
    }

    public String saveSubscription() {
        User user = getSUser();
        UsersDao.instance.insertPushSubscription(user.getLogin(), subscription);
        subscription = new BasicDBObject("complete", true);
        return SUCCESS.toUpperCase();
    }
}
