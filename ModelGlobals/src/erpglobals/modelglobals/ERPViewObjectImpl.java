package erpglobals.modelglobals;

import oracle.jbo.ViewObject;
import oracle.jbo.server.TransactionEvent;
import oracle.jbo.server.ViewDefImpl;
import oracle.jbo.server.ViewObjectImpl;

public class ERPViewObjectImpl extends ViewObjectImpl {
    public ERPViewObjectImpl(String string, ViewDefImpl viewDefImpl) {
        super(string, viewDefImpl);
    }
String  erpPkColumn;
String erpViewObjectName;
String erpParameterId;
    public ERPViewObjectImpl() {
        super();
    }


    public void setErpPkColumn(String erpPkColumn) {
        this.erpPkColumn = erpPkColumn;
    }

    public String getErpPkColumn() {
        return erpPkColumn;
    }

    public void setErpViewObjectName(String erpViewObjectName) {
        this.erpViewObjectName = erpViewObjectName;
    }

    public String getErpViewObjectName() {
        return erpViewObjectName;
    }

    public void setErpParameterId(String erpParameterId) {
        this.erpParameterId = erpParameterId;
    }

    public String getErpParameterId() {
        return erpParameterId;
    }

    @Override
    public void afterRollback(TransactionEvent transactionEvent) {
        // TODO Implement this method
        super.afterRollback(transactionEvent);
        try {
            ViewObject vo = getRootApplicationModule().findViewObject(getErpViewObjectName());
            String erpPk= "0"+ (  vo.first().getAttribute(getErpPkColumn()) ==null?0:vo.first().getAttribute(getErpPkColumn()));
            Integer erpPkValue=Integer.parseInt(erpPk);
            vo.setNamedWhereClauseParam(getErpParameterId(),erpPkValue);
            vo.executeQuery();
        } catch (Exception ex) {
            // TODO: Add catch code
            //e.printStackTrace();
        }
    }    
}
