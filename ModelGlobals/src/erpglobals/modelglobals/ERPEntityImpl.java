package erpglobals.modelglobals;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import oracle.jbo.AttributeList;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.TransactionEvent;


public class ERPEntityImpl extends EntityImpl {
    public ERPEntityImpl() {
        super();
    }

    String ERPTableName;
    Integer ERPPrimaryKeyColumn=null;
    
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
    
    public void doERPSetPrimaryKeyValue() {
    //checking if any sequence name is given or not
        ERPTableName=(ERPTableName==null?this.getEntityDef().getSource()+"_SEQ":ERPTableName);
        CallableStatement cs=getDBTransaction().createCallableStatement("call proc_get_sequence_no('"+ERPTableName+"',?)", 1);
        try {
            cs.registerOutParameter(1, Types.INTEGER);
            cs.executeUpdate();
            for (int i = 0; i < this.getEntityDef().getAttributeCount(); i++) {
                if (this.getEntityDef().getAttributeDefs()[i].isPrimaryKey() ||ERPPrimaryKeyColumn !=null) {
                    
                    ERPPrimaryKeyColumn=this.getEntityDef().getAttributeDefs()[i].getIndex();
                    setAttribute(ERPPrimaryKeyColumn, cs.getInt(1));
                    ERPPrimaryKeyColumn=null;
                    ERPTableName=null;
                break;
                }
            }

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }
        finally{
            try {
                cs.close();
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

}
