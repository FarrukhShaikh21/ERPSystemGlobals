package erpglobals.modelglobals;


public class ERPUserAttribute {
    public ERPUserAttribute() {
        super();
    }
private String isLock="N";
private String ExpireDate;
private String ErpLoginStatus="ERPINIT";//initialize login status
private Integer UserId;
private String ErpErrorMessage;
private String ExppasswordComplexity;
private String ErpIsPwdExpired;
private Integer ErpPwdStrength;
private Integer ErpPwdExpiryDays=15;//default expiry days
private String UserCode;
private Integer ErpDefGloalCompany;
private String ErpTempCompanyAccessTable;//temp table name for location
private String ErpTempDepartAccessTable;//temp table name for department
private String ErpImageStorePath;
private String ErpImageStoreOn;
//private BigInteger ErpSessionId=new BigInteger("0");
private String ERPUserPicture;
private String ERPUserName;


    public void setERPUserPicture(String ERPUserPicture) {
        this.ERPUserPicture = ERPUserPicture;
    }

    public String getERPUserPicture() {
        return ERPUserPicture;
    }

    public void setERPUserName(String ERPUserName) {
        this.ERPUserName = ERPUserName;
    }

    public String getERPUserName() {
        return ERPUserName;
    }


    public void setErpImageStorePath(String ErpImageStorePath) {
        this.ErpImageStorePath = ErpImageStorePath;
    }

    public String getErpImageStorePath() {
        return ErpImageStorePath;
    }

    public void setErpImageStoreOn(String ErpImageStoreOn) {
        this.ErpImageStoreOn = ErpImageStoreOn;
    }

    public String getErpImageStoreOn() {
        return ErpImageStoreOn;
    }

    public void setUserCode(String UserCode) {
        this.UserCode = UserCode;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setErpPwdExpiryDays(Integer ErpPwdExpiryDays) {
        this.ErpPwdExpiryDays = ErpPwdExpiryDays;
    }

    public Integer getErpPwdExpiryDays() {
        return ErpPwdExpiryDays;
    }

    public void setErpPwdStrength(Integer ErpPwdStrength) {
        this.ErpPwdStrength = ErpPwdStrength;
    }

    public Integer getErpPwdStrength() {
        return ErpPwdStrength;
    }

    public void setErpIsPwdExpired(String ErpIsPwdExpired) {
        this.ErpIsPwdExpired = ErpIsPwdExpired;
    }

    public String getErpIsPwdExpired() {
        return ErpIsPwdExpired;
    }

    public void setErpErrorMessage(String ErpErrorMessage) {
        this.ErpErrorMessage = ErpErrorMessage;
    }

    public String getErpErrorMessage() {
        return ErpErrorMessage;
    }

    public void setExppasswordComplexity(String ExppasswordComplexity) {
        this.ExppasswordComplexity = ExppasswordComplexity;
    }

    public String getExppasswordComplexity() {
        return ExppasswordComplexity;
    }

    public void setUserId(Integer UserId) {
        this.UserId = UserId;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setExpireDate(String ExpireDate) {
        this.ExpireDate = ExpireDate;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public void setErpLoginStatus(String ErpLoginStatus) {
        this.ErpLoginStatus = ErpLoginStatus;
    }

    public String getErpLoginStatus() {
        return ErpLoginStatus;
    }

    public void setErpDefGloalCompany(Integer ErpDefGloalCompany) {
        this.ErpDefGloalCompany = ErpDefGloalCompany;
    }

    public Integer getErpDefGloalCompany() {
        return ErpDefGloalCompany;
    }

    public void setErpTempCompanyAccessTable(String ErpTempCompanyAccessTable) {
        this.ErpTempCompanyAccessTable = ErpTempCompanyAccessTable;
    }

    public String getErpTempCompanyAccessTable() {
        return ErpTempCompanyAccessTable;
    }

    public void setErpTempDepartAccessTable(String ErpTempDepartAccessTable) {
        this.ErpTempDepartAccessTable = ErpTempDepartAccessTable;
    }

    public String getErpTempDepartAccessTable() {
        return ErpTempDepartAccessTable;
    }
}
