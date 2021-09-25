package erpglobals.modelglobals;

import oracle.jbo.RowSet;
import oracle.jbo.server.ViewRowImpl;

public class ERPViewRowImpl extends ViewRowImpl {
    public ERPViewRowImpl() {
        super();
    }

    RowSet AccAdminCompanyForCompVO;
    RowSet AccSysSystemParameterVO;
        
    public byte ERPdoGetEntityState() {
        return getEntities()[0].getEntityState();
    }


    public RowSet getAccAdminCompanyForCompVO() {
        return AccAdminCompanyForCompVO;
    }

    public RowSet getAccSysSystemParameterVO() {
        return AccSysSystemParameterVO;
    }
    public Integer doGetCompanyIDByLocation(Integer pLocationId,Integer pGlobalCompanyId) {
                Integer erpCompanyId=-1;
                /*getting company type*/
                getAccSysSystemParameterVO().setNamedWhereClauseParam("P_ADF_GLOBAL_COMPANY_ID", pGlobalCompanyId);
                getAccSysSystemParameterVO().setNamedWhereClauseParam("P_ADF_PARAMETER_ID", "COMPANY_TYPE_SNO");
                getAccSysSystemParameterVO().executeQuery();
                Integer erpCompanyType=Integer.parseInt(getAccSysSystemParameterVO().first().getAttribute("ParameterValue").toString()) ;
                getAccAdminCompanyForCompVO().setNamedWhereClauseParam("P_ADF_COMP_CODE",pLocationId==null?-1:pLocationId);
                getAccAdminCompanyForCompVO().executeQuery();
                while(true && getAccAdminCompanyForCompVO().getRowCount()>0) {
                    //assigning companyid to company
                    //
                    Integer ccode= Integer.parseInt( getAccAdminCompanyForCompVO().first().getAttribute("ParentCompCode").toString() );
                    getAccAdminCompanyForCompVO().setNamedWhereClauseParam("P_ADF_COMP_CODE", ccode);
                    getAccAdminCompanyForCompVO().executeQuery();
                    if (Integer.parseInt( getAccAdminCompanyForCompVO().first().getAttribute("CompanyTypeSno").toString())== erpCompanyType) {
                        //setCompanyId(Integer.parseInt( getAccAdminCompanyForCompVO().first().getAttribute("CompCode").toString()));
                     erpCompanyId=  Integer.parseInt( getAccAdminCompanyForCompVO().first().getAttribute("CompCode").toString()); 
                       break;
                   }
                }
                return erpCompanyId;
                //System.out.println(getCompanyId() + " getcompanyid");
    }
}
