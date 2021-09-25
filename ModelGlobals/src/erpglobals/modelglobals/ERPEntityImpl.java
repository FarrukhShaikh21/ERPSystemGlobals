package erpglobals.modelglobals;

import oracle.jbo.AttributeList;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.TransactionEvent;

public class ERPEntityImpl extends EntityImpl {
    public ERPEntityImpl() {
        super();
    }

    @Override
    protected void create(AttributeList attributeList) {
        // TODO Implement this method
        super.create(attributeList);
        try {
            setAttribute("IsSupervised", "N");
            
        } catch (Exception e) {
        }
        try {
               setAttribute("IsUnsupervised", "N");
           } catch (Exception e) {
                // TODO: Add catch code
                
            }    
        try {
               setAttribute("IsActive", "Y");
           } catch (Exception e) {
                // TODO: Add catch code
                
            }       
    }

    @Override
    protected void doDML(int i, TransactionEvent transactionEvent) {
        if (i == DML_INSERT) {
           // System.out.println("dml_insert_new_02");
            populateAttributeAsChanged(getAttributeIndexOf("CreatedBy"),ERPGlobalPLSQLClass.doGetModelUserSno());
            populateAttributeAsChanged(getAttributeIndexOf("CreatedDate"),ERPGlobalPLSQLClass.doGetOracleSQLTSDate());
            
        } else if (i == DML_UPDATE) {
           // System.out.println("dml_update_new_01");
            String lsupervisedDate;
            String lUnsupervisedDate;
            String lisSupervised="N" ;
            String lisUnSupervised="N";
            String loldIsSupervised="N";
            try {
                lisSupervised= getAttribute("IsSupervised").toString(); //checking is supervised;
                 lisUnSupervised= getAttribute("IsUnsupervised").toString(); //checking is unsupervised;
                loldIsSupervised = getPostedAttribute(getAttributeIndexOf("IsSupervised")).toString();

            } catch (Exception e) {
                // TODO: Add catch code
                e.printStackTrace();
            }  if (lisSupervised.equals("N") && loldIsSupervised.equals("N")) {
                //if record is not supervised then change the last update columns
                populateAttributeAsChanged(getAttributeIndexOf("LastUpdatedBy"),ERPGlobalPLSQLClass.doGetModelUserSno());
                populateAttributeAsChanged(getAttributeIndexOf("LastUpdatedDate"),ERPGlobalPLSQLClass.doGetOracleSQLTSDate());
            }
            try {
               lsupervisedDate =getAttribute("SupervisedDate").toString();//checking already supervised
                
           } catch (Exception e) {
                if (lisSupervised.equals("Y")) {
                    // it exception occurs it means record is not supervised
                populateAttributeAsChanged(getAttributeIndexOf("SupervisedDate"),ERPGlobalPLSQLClass.doGetOracleSQLTSDate());
                populateAttributeAsChanged(getAttributeIndexOf("SupervisedBy"),ERPGlobalPLSQLClass.doGetModelUserSno());
               }
            }
    
            try {
               lUnsupervisedDate =getAttribute("UnSupervisedDate").toString();//checking already supervised
                
            } catch (Exception e) {
                if (lisUnSupervised.equals("Y")) {
                    // it exception occurs it means record is not supervised
                populateAttributeAsChanged(getAttributeIndexOf("UnSupervisedDate"),ERPGlobalPLSQLClass.doGetOracleSQLTSDate());
                populateAttributeAsChanged(getAttributeIndexOf("UnSupervisedBy"),ERPGlobalPLSQLClass.doGetModelUserSno());
               }
            }        
        }
        super.doDML(i, transactionEvent);
    }

}
