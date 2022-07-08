package erpglobals.modelglobals;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import oracle.jbo.AttributeList;
import oracle.jbo.JboException;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.TransactionEvent;


public class ERPEntityImpl extends EntityImpl {
    public ERPEntityImpl() {
        super();
    }

    String ERPTableName;///table name of current transactuib
    Integer ERPPrimaryKeyColumn=null;//primary key column of transaction table
    String ERPGenerateSequence="ERPYES";
    
    @Override
    protected void create(AttributeList attributeList) {
        // TODO Implement this method
        
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
        if (getERPGenerateSequence().equals("ERPYES")) {//checking need to generte sequence or not
            doERPSetPrimaryKeyValue();
        }
        super.create(attributeList);
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
    
    public void doERPSetPrimaryKeyValue() {
    //checking if any sequence name is given or not
        ERPTableName=(ERPTableName==null?this.getEntityDef().getSource()+"_SEQ":ERPTableName);
        
        CallableStatement ERPcs;
        String ErpTransType= ERPGlobalPLSQLClass.doErpGetConnTypeModel(getDBTransaction());
       
        if (ErpTransType.equals("ERPORACLE")) {
            ERPcs =
                getDBTransaction().createCallableStatement("begin proc_get_sequence_no('" + ERPTableName +
                                                           "',?); end; ", 1);
        } else {
            ERPcs = getDBTransaction().createCallableStatement("call proc_get_sequence_no('" + ERPTableName + "',?)", 1);
        }
        
        try {
            ERPcs.registerOutParameter(1, Types.INTEGER);
            ERPcs.executeUpdate();
            for (int i = 0; i < this.getEntityDef().getAttributeCount(); i++) {
                //checking primary column or if have passet the primary key column then system will work accordingly
                if (this.getEntityDef().getAttributeDefs()[i].isPrimaryKey() ||ERPPrimaryKeyColumn !=null) {
                    ERPPrimaryKeyColumn=this.getEntityDef().getAttributeDefs()[i].getIndex();
                    setAttribute(ERPPrimaryKeyColumn, ERPcs.getInt(1));//assigning the sequence to column
                    ERPPrimaryKeyColumn=null;
                    ERPTableName=null;
                    break;
                }
            }

        } catch (SQLException sqle) {
            // TODO: Add catch code
            throw new JboException("Unable To Create New Record"+sqle.getMessage());
        }
        finally{
            try {
                ERPcs.close();
            } catch (SQLException e) {
            }
        }
    }


    public void setERPTableName(String ERPTableName) {
        this.ERPTableName = ERPTableName;
    }

    public String getERPTableName() {
        return ERPTableName;
    }

    public void setERPPrimaryKeyColumn(Integer ERPPrimaryKeyColumn) {
        this.ERPPrimaryKeyColumn = ERPPrimaryKeyColumn;
    }

    public Integer getERPPrimaryKeyColumn() {
        return ERPPrimaryKeyColumn;
    }

    public void setERPGenerateSequence(String ERPGenerateSequence) {
        this.ERPGenerateSequence = ERPGenerateSequence;
    }

    public String getERPGenerateSequence() {
        return ERPGenerateSequence;
    }
}
