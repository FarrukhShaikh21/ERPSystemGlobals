
package erpglobals.modelglobals;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.adf.share.ADFContext;

import oracle.jbo.ApplicationModule;
import oracle.jbo.JboException;
import oracle.jbo.JboSQLException;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.server.DBTransaction;

public class ERPGlobalPLSQLClass {
    public ERPGlobalPLSQLClass() {
        super();
    }
    public static String doGetPrimaryKeyValueModel(DBTransaction pDbt, String pColumnName, String pTableName,
                                                 String pWhereColumn, String pWhereColumnValue) {
        String qry = "";
        try {
            ApplicationModule am = pDbt.getRootApplicationModule();
            ViewObject vo = am.findViewObject("ERPpkGen");
            if (vo != null) {
                vo.remove();
            }
            qry = "select max(" + pColumnName + ")+1 as pk from " + pTableName;
            if (pWhereColumn != null) {
                qry += " where " + pWhereColumn + "='" + pWhereColumnValue + "'";
            }
            vo = am.createViewObjectFromQueryStmt("ERPpkGen", qry);
            vo.executeQuery();
            
            if (vo.first().getAttribute(0)==null) {
                return "1";
           }
            return vo.first().getAttribute(0).toString();
        } catch (Exception e) {
            throw new JboException("Error While Execution Primary Key Query (" + qry + ")");
        }
    }
    
 public static oracle.jbo.domain.Date doGetOracleJBODate() {
     return new oracle.jbo.domain.Date(new java.sql.Timestamp(System.currentTimeMillis()));
 }
    public static java.sql.Timestamp doGetOracleSQLTSDate() {
        return new java.sql.Timestamp(System.currentTimeMillis());
    }
    public static void main(String[] args) {
        System.out.println(new java.sql.Timestamp(System.currentTimeMillis()));
   }
    public static Integer doGetModelUserSno() {
        /*if (1==1) {
            return 1;
       }*/
        return Integer.parseInt(ADFContext.getCurrent().getPageFlowScope().get("G_USER_SNO").toString());
    }

    public static String doGetERPModelTempCompAccessTable() {
        /*if (1==1) {
            return "temp_admin_company_access";
       }*/
        return ADFContext.getCurrent().getPageFlowScope().get("G_TEMP_COMP_ACCESS_TABLE").toString();
    }
 
    public static String doGetERPModelTempDepartAccessTable() {
        /*
        if (1==1) {
            return "temp_admin_department_access";
       }*/
        
        return ADFContext.getCurrent().getPageFlowScope().get("G_TEMP_DEPT_ACCESS_TABLE").toString();
    }
       
    public static Integer doGetModelGlobalDefCompany() {
        /*
        if (1==1) {
            return 1;
        }*/
        return 
          Integer.parseInt(ADFContext.getCurrent().getPageFlowScope().get("G_GLOBAL_DEF_COMPANY").toString());
    }
    
    public static String doGetModelUserCode() {
        return ADFContext.getCurrent().getPageFlowScope().get("G_USER_CODE").toString();
    }
 

    
    public static String doGetErpModuleAction() {
        /*if (1==1) {
            return"SEC_0008";
       }*/
        return ADFContext.getCurrent().getPageFlowScope().get("G_ERP_MODULE_ACTION").toString();
    }
    
    public static String doExecuteSQLQueryModel(DBTransaction pDbt, String pSqlQuery) {
        String qry = "";
        try {
            ApplicationModule am = pDbt.getRootApplicationModule();
            ViewObject vo = am.findViewObject("ERPSQLGen");
            if (vo != null) {
                vo.remove();
            }
            qry = pSqlQuery;

            vo = am.createViewObjectFromQueryStmt("ERPSQLGen", qry);
            vo.executeQuery();
            
            //System.out.println("psqlq>"+pSqlQuery);
            //System.out.println(vo.getRowCount());
            if (vo.getRowCount()==0) {
               System.out.println("now row");  
                return "-";
           }
           //System.out.println("first row::"+vo.first().getAttribute(0).toString()); 
            
            return vo.first().getAttribute(0).toString();
            
        } catch (Exception e) {
            throw new JboException("Error While Execution SQL Query (" + qry + ")");
        }
    }

    public static ERPUserAttribute doFuncCheckErpLogin(DBTransaction pDbt, String pUserCode, String pPassword) {
        ApplicationModule am = pDbt.getRootApplicationModule();
        ERPUserAttribute ua = new ERPUserAttribute();
        ViewObject vod = am.findViewObject("ErpdmyVO");
        if (vod != null) {
            vod.remove();
        }
        try {
            
            vod =
                am.createViewObjectFromQueryStmt("ErpdmyVO",
                                                 "select sys_context('USERENV','CURRENT_USER'),ua.user_id UserId,ua.Is_Lock IsLock,to_char(nvl(ua.EXPIRY_DATE,sysdate+100),'fmDD-Month-yyyy') ExpiryDate,ua.IS_EXPIRED ISEXPIRED,ua.USER_CODE USERCODE,sugc.company_id GLOBALCOMPANYID, sgv.value_description TEMPCOMPACCTAB,sgvdep.value_description TEMPDEPTACCTAB,ua.picture_name PICTURENAME,UA.User_Name USERNAME from SYS_USERS ua, sys_user_password up,sys_user_global_company sugc,sys_general_value sgv,sys_general_value sgvdep  WHERE ua.user_id=up.user_id and up.is_active='Y' and sugc.is_default='Y' and sugc.is_active='Y' and sugc.user_id=ua.user_id and sgv.value_id=4 and sgvdep.value_id=23  and ua.USER_CODE='"+pUserCode+"' AND up.PASSWORDD=DBMS_CRYPTO.encrypt (UTL_I18N.string_to_raw ('" +
                                                 pPassword +"', 'AL32UTF8'),(6+ 256+ 4096),UTL_I18N.string_to_raw ('$rPl0G!NK$Ysyste', 'AL32UTF8'))");
            vod.executeQuery();
            //System.out.println("select sys_context('USERENV','CURRENT_USER'),user_id UserId,Is_Lock IsLock,to_char(nvl(EXPIRY_DATE,sysdate+100),'fmDD-Month-yyyy') ExpiryDate,IS_EXPIRED ISEXPIRED  from SYS_USERS WHERE USER_CODE='\"+pUserCode+\"' AND PASSWORDD=DBMS_CRYPTO.encrypt (UTL_I18N.string_to_raw ('" +pPassword +"'");
            //System.out.println("erpglobal>>vod.getRowCount()" + vod.getRowCount());
            if (vod.getRowCount() == 0) { //if login is unsuccessful then zero
                ua.setErpLoginStatus("ERPNO"); //return NO
                //System.out.println("this is zero rows");

            } else //successful login
            {
                ua.setErpLoginStatus("ERPYES");
                ua.setUserId((Integer)vod.first().getAttribute("USERID"));
                ua.setUserCode(vod.first().getAttribute("USERCODE").toString());
                ua.setIsLock(vod.first().getAttribute("ISLOCK").toString());
                ua.setExpireDate(vod.first().getAttribute("EXPIRYDATE").toString());
                ua.setErpIsPwdExpired(vod.first().getAttribute("ISEXPIRED").toString());
                ua.setErpDefGloalCompany((Integer)vod.first().getAttribute("GLOBALCOMPANYID"));
                ua.setErpTempCompanyAccessTable((String)vod.first().getAttribute("TEMPCOMPACCTAB"));
                ua.setErpTempDepartAccessTable((String)vod.first().getAttribute("TEMPDEPTACCTAB"));
                ua.setERPUserName((String)vod.first().getAttribute("USERNAME"));
                ua.setERPUserPicture((String)vod.first().getAttribute("PICTURENAME"));
            }
            vod.remove();
        } catch (JboSQLException e) { //if database is not Oracle system will come here
            if (vod != null) {
                vod.remove();
            }
            vod =
                am.createViewObjectFromQueryStmt("ErpdmyVO",
                                                 "select ua.user_id USERID,ua.is_lock ISLOCK,date_format(ifnull(ua.expiry_date,date_add(sysdate(), interval 100 day)),'%d-%M-%Y') EXPIRYDATE,ua.IS_EXPIRED ISEXPIRED,ua.USER_CODE USERCODE,sugc.company_id GLOBALCOMPANYID, sgv.value_description TEMPCOMPACCTAB,sgvdep.value_description TEMPDEPTACCTAB,ua.picture_name PICTURENAME,UA.User_Name USERNAME from sys_users ua,sys_user_password up,sys_user_global_company sugc,sys_general_value sgv, sys_general_value sgvdep  where up.user_id=ua.user_id and up.is_active='Y' and sugc.is_default='Y' and sugc.is_active='Y' and sugc.user_id=ua.user_id and sgv.value_id=4 and sgvdep.value_id=23 and ua.USER_CODE='"+pUserCode+"' AND up.passwordd=AES_ENCRYPT('" +pPassword + "','$rPl0G!NK$Ysyste')");
            vod.executeQuery();
            if (vod.getRowCount() == 0) {//invalid password
                //System.out.println("Invalid User Password MYSQL");
                ua.setErpLoginStatus("ERPNO");
            }
            else {
                ua.setErpLoginStatus("ERPYES");
                ua.setUserId((Integer)vod.first().getAttribute("USERID"));
                ua.setUserCode(vod.first().getAttribute("USERCODE").toString());
                ua.setIsLock(vod.first().getAttribute("ISLOCK").toString());
                ua.setExpireDate(vod.first().getAttribute("EXPIRYDATE").toString());
                ua.setErpIsPwdExpired(vod.first().getAttribute("ISEXPIRED").toString());
                ua.setErpDefGloalCompany((Integer)vod.first().getAttribute("GLOBALCOMPANYID"));
                ua.setErpTempCompanyAccessTable((String)vod.first().getAttribute("TEMPCOMPACCTAB"));
                ua.setErpTempDepartAccessTable((String)vod.first().getAttribute("TEMPDEPTACCTAB"));
                ua.setERPUserName((String)vod.first().getAttribute("USERNAME"));
                ua.setERPUserPicture((String)vod.first().getAttribute("PICTURENAME"));
                 
            }
            vod.remove();
        }      
        //System.out.println("return ua");
        return ua;
    }
    
   public static ERPUserAttribute doFuncCheckPwdComplaxity(DBTransaction pDbt,Integer pUserId,String pPassword) 
    {
         /*    var strongRegex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})");
                    var mediumRegex = new RegExp("^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{6,})");
          */
        ERPUserAttribute erpua = new ERPUserAttribute();
        //System.out.println("doFuncCheckPwdComplaxity-model");
        try {
                Integer occur = 0;
                //String config = "ERPSMSModuleLocal";
                //String password="SHAIKH_$#!@!s12";//"SHAIKH_3211123";
                //String password = "shaikh"; //"SHAIKH_3211123";
                //String userCode = "FARRUKH";
                String erpSqlwhere = "";
                //System.out.println(pPassword.indexOf(userCode));
                String special="&`.~!@#$%^*()-_=+/\'?:;[]{},<>\"|";
                String erpSpcChr;
                Pattern pattern = Pattern.compile("");
                Matcher matcher = pattern.matcher("");
                ApplicationModule am = pDbt.getRootApplicationModule();
                ViewObject vo = am.findViewObject("erpPwdPol");
                if (vo!=null) {
                    vo.remove();
               }
                vo=am.createViewObjectFromQueryStmt("erpPwdPol","SELECT PP.PASSWORD_PARAMETER_SNO,\n" + 
                "      PP.PASSWORD_PARAMETER_NAME,\n" + 
                "      REPLACE(PP.PASSWORD_PARAMETER_VALUE,'<br>','') PASSWORD_PARAMETER_VALUE,\n" +
                " PP.MESSAGE_ON_VALIDATION, \n" +
                "SU.USER_CODE \n" +                                               
                "       \n" + 
                "FROM\n" + 
                "    sys_password_parameter pp,\n" + 
                "    sys_user_password_policy sup,\n" + 
                "    sys_password_policy_header ph,\n" + 
                "    sys_password_policy_lines pl,\n" + 
                "    SYS_USERS SU\n" + 
                "WHERE      SUP.USER_SNO=SU.USER_ID\n" + 
                "           AND su.user_id="+pUserId+" \n" + 
                "        AND ph.policy_header_sno = sup.policy_header_sno\n" + 
                "        AND pl.policy_header_sno = ph.policy_header_sno\n" + 
                "        AND pp.password_parameter_sno = pl.password_parameter_sno\n" + 
                "        AND ph.is_supervised = 'Y'\n" + 
                "        AND ph.is_active = 'Y'\n" + 
                "        AND pl.is_active = 'Y'  order by PP.PASSWORD_PARAMETER_SNO\n");
                vo.executeQuery();
                //System.out.println(vo.getRowCount());
                erpwhilechkpol:
                while(vo.hasNext()) {//is record exists then loop will execute
                 Row r=vo.next();
                String erpUserCode=r.getAttribute("USER_CODE").toString();
                 erpua.setErpErrorMessage(r.getAttribute("MESSAGE_ON_VALIDATION").toString());
                    
                    if (r.getAttribute("PASSWORD_PARAMETER_SNO").toString().equals("1")) {
                    //password minumum length checking
                    if (pPassword.length()<Integer.parseInt(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString())) {
                        //System.out.println("length issue>:"+pPassword);
                           erpua.setErpLoginStatus("ERPNO"); 
                        break erpwhilechkpol;
                        
                       }
                   }
                   
                   ///////////// maximum length
                   if (r.getAttribute("PASSWORD_PARAMETER_SNO").toString().equals("2")) {
                   //password minumum length checking
                       
                   if (pPassword.length()>Integer.parseInt(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString())) {
                       //System.out.println("length maximum");
                          erpua.setErpLoginStatus("ERPNO"); 
                          break erpwhilechkpol;
                      }
                   }
                    
                    //////////// CHECKING NUMERIC LENGTH
                    if (r.getAttribute("PASSWORD_PARAMETER_SNO").toString().equals("3")) {
                        //System.out.println((Integer.parseInt(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString())>0 && matcher.find()==false) + " match find");
                    //password minumum length checking
                   
                   //this while look is counting the total no of numeric occurance     
                   matcher.reset();
                   pattern = Pattern.compile("[0-9]");
                   matcher = pattern.matcher(pPassword); 
                        occur=0;
                   while(matcher.find()) {
                        occur++;
                    }
                    
                    if (Integer.parseInt(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString()) > occur) {
                       // System.out.println("length numeric");
                           erpua.setErpLoginStatus("ERPNO"); 
                        break erpwhilechkpol;
                       }
                    }                
     
                    if (r.getAttribute("PASSWORD_PARAMETER_SNO").toString().equals("4")) {
                        //System.out.println((Integer.parseInt(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString())>0 && matcher.find()==false) + " match find");
                    //password minumum LOWER CHARACTER checking
                    matcher.reset();
                    pattern = Pattern.compile("[a-z]");
                    matcher = pattern.matcher(pPassword);
                        occur=0;
                        
                    while(matcher.find()) {
                             occur++;
                         } 
                    
                    if (Integer.parseInt(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString())> occur) {
                        //System.out.println("length small letter");
                           erpua.setErpLoginStatus("ERPNO"); 
                        break erpwhilechkpol;
                       }
                    }

                    if (r.getAttribute("PASSWORD_PARAMETER_SNO").toString().equals("5")) {
                        //System.out.println((Integer.parseInt(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString())>0 && matcher.find()==false) + " match find");
                    //password minumum LOWER CHARACTER checking
                    matcher.reset();
                    pattern = Pattern.compile("[A-Z]");
                    matcher = pattern.matcher(pPassword);
                    occur=0;
                    if (Integer.parseInt(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString())>0 && matcher.find()==false) {
                        //System.out.println("length capital letter");
                           erpua.setErpLoginStatus("ERPNO"); 
                           break erpwhilechkpol;
                       }
                    }

                    //password expiry days
                    if (r.getAttribute("PASSWORD_PARAMETER_SNO").toString().equals("7")) {
                        erpua.setErpPwdExpiryDays(Integer.parseInt(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString()));
                    }
                    
                 //checking white space
                 if (r.getAttribute("PASSWORD_PARAMETER_SNO").toString().equals("9")) {
                     //System.out.println((Integer.parseInt(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString())>0 && matcher.find()==false) + " match find");
                 //password minumum LOWER CHARACTER checking
                 matcher.reset();
                 pattern = Pattern.compile("\\s");
                 matcher = pattern.matcher(pPassword);
                 if (r.getAttribute("PASSWORD_PARAMETER_VALUE").toString().equals("NO") && matcher.find()==true) {
                     //System.out.println("can not contain white space");
                        erpua.setErpLoginStatus("ERPNO"); 
                        break erpwhilechkpol;
                    }
                 }
                    //checking white space
                    if (r.getAttribute("PASSWORD_PARAMETER_SNO").toString().equals("10")) {
                        //System.out.println((Integer.parseInt(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString())>0 && matcher.find()==false) + " match find");
                    //password minumum LOWER CHARACTER checking
                    //System.out.println("can not contain user name +++");
                    if (r.getAttribute("PASSWORD_PARAMETER_VALUE").toString().equals("NO") && pPassword.toUpperCase().indexOf(erpUserCode)>=0) {
                       // System.out.println("can not contain user name");
                           erpua.setErpLoginStatus("ERPNO"); 
                           break erpwhilechkpol;
                       }
                    }
     
                    if (r.getAttribute("PASSWORD_PARAMETER_SNO").toString().equals("6")) {
                        //System.out.println((Integer.parseInt(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString())>0 && matcher.find()==false) + " match find");
                    //password USER NAME CAN NOT BE REPEATED checking
                        ViewObject vopwd=am.findViewObject("erpUserPwdHis");
                        
                        if (vopwd!=null) {
                                vopwd.remove();
                           }
                        ///checking for MySQL or Oracle
                        try {
                            vopwd =
                                am.createViewObjectFromQueryStmt("erpUserPwdHis",
                                                                 "select sys_context('USERENV', 'CURRENT_USER') from dual");
                            vopwd.executeQuery();
                            erpSqlwhere=" DBMS_CRYPTO.encrypt (UTL_I18N.string_to_raw ('" +pPassword +"', 'AL32UTF8'),(6+ 256+ 4096),UTL_I18N.string_to_raw ('$rPl0G!NK$Ysyste', 'AL32UTF8')) and TRUNC(change_date)>=TRUNC(SYSDATE)-VW_NO_OF_DAYS.ERP_LAST_DAYS";
                        } catch (Exception e) {
                            // TODO: Add catch code
                            erpSqlwhere=" AES_encrypt('"+pPassword+"', '$rPl0G!NK$Ysyste') AND STR_TO_DATE(DATE_FORMAT(change_date,'%d-%m-%Y'),'%d-%m-%Y')>= DATE_SUB(STR_TO_DATE(DATE_FORMAT(sysdate(),'%d-%m-%Y'),'%d-%m-%Y') , INTERVAL VW_NO_OF_DAYS.ERP_LAST_DAYS DAY) ";
                        }  
                        vopwd.remove();//remove existing view object 
                        vopwd =
                            am.createViewObjectFromQueryStmt("erpUserPwdHis",
                                                             "select count(1) ERPPWDCOUNT from sys_user_password_history,(SELECT P.PASSWORD_PARAMETER_VALUE ERP_LAST_DAYS FROM SYS_PASSWORD_PARAMETER P WHERE P.PASSWORD_PARAMETER_SNO=13)VW_NO_OF_DAYS WHERE user_id='"+pUserId+"' AND USER_PASSWORD ="+erpSqlwhere);
                            //System.out.println("get q"+vopwd.getQuery());
                     //  System.out.println(vopwd.getQuery() + ">>get qyert");     
                        vopwd.executeQuery();
                        if (Integer.parseInt(vopwd.first().getAttribute("ERPPWDCOUNT").toString())> 0 && Integer.parseInt(vopwd.first().getAttribute("ERPPWDCOUNT").toString())>=Integer.parseInt(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString()) ) {
                           // System.out.println("password duplicate");
                           erpua.setErpLoginStatus("ERPNO");  
                           break erpwhilechkpol;
                       }
                        vopwd.remove();
                        }
                    
                    if (r.getAttribute("PASSWORD_PARAMETER_SNO").toString().equals("12")) {
                        //System.out.println((Integer.parseInt(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString())>0 && matcher.find()==false) + " match find");
                    //password special characters
                        //System.out.println(r.getAttribute("PASSWORD_PARAMETER_VALUE").toString().indexOf("&"));
                        foribreak:
                    for (int i = 0; i < special.length(); i++) {
                       erpSpcChr=special.substring(i, i+1);     
                           if (r.getAttribute("PASSWORD_PARAMETER_VALUE").toString().indexOf(erpSpcChr)==-1 && pPassword.indexOf(erpSpcChr)>=0) {
                           //System.out.println("can not contain this special characters");
                           erpua.setErpErrorMessage(r.getAttribute("MESSAGE_ON_VALIDATION").toString().concat("("+erpSpcChr+")"));
                           erpua.setErpLoginStatus("ERPNO");
                            break erpwhilechkpol;
                           //break foribreak;
                           }    
                       }
                    }
                }//while loop for policy
           
                vo.remove();
            if (erpua.getErpLoginStatus().equals("ERPNO")) {
                return erpua;
           }// in case password doesno match then system will return
              erpua.setErpLoginStatus("ERPYES");
            //System.out.println("password is match a::"+ pPassword);
                matcher.reset();
                
                //pattern=Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[\\\\\\[\\]'!@#$%:^/&*.~\\)\\(-_])(?=.{8,})");
                pattern=Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[\\\\ ` \\[ \\] \\'!@#\\$ %: \\^ / &* \\. ~ \\? \\) \\} \\{ ; ' \\+ \\= , \\< \\> \\| \" \\( \\- _ ])(?=.{8,})");
                matcher=pattern.matcher(pPassword);
                //System.out.println(matcher.find() + " medium");
               
               if (matcher.find()) {
                //System.out.println("Strong");
                //erpua.setExppasswordComplexity("ERPSTRONG");
                    erpua.setExppasswordComplexity("ERPStrong");
                   erpua.setErpPwdStrength(12);
               } 
               else {
                matcher.reset();
                pattern=Pattern.compile("^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{6,})");  
                matcher=pattern.matcher(pPassword); 
                  // System.out.println("password is match b");
                if (matcher.find()) {
                    //System.out.println("medium");
                    erpua.setExppasswordComplexity("ERPMedium");
                       erpua.setErpPwdStrength(7);
                   }
                 else {
                    //System.out.println("week");
                    erpua.setExppasswordComplexity("ERPWeak");
                    erpua.setErpPwdStrength(4);
                }
               }
               
            } finally {
            }
            return erpua;
       }

   public static void doErpExecutePLSQLModel(DBTransaction pDbt,String pPlsql,String pIsCommit) {
        //return Integer.parseInt(ADFContext.getCurrent().getPageFlowScope().get("G_USER_SNO").toString());
        PreparedStatement cs = pDbt.createPreparedStatement(pPlsql, 1);
        
        try {
            cs.executeUpdate();
            if (pIsCommit.equals("Y")) {
                pDbt.commit();
            }
        } catch (Exception e) {
            try {
                
                cs.close();
                throw new JboException("Error While Execution SQL PL/SQL."+e.getMessage());
            } catch (SQLException f) {
                throw new JboException("Error While Execution SQL PL/SQL."+e.getMessage());

            }
        }
        finally{
            try {
                cs.close();
            } catch (SQLException e) {
                throw new JboException("Error While Execution SQL PL/SQL."+e.getMessage());

            }
        }        
    }

    public static String doErpGetConnTypeModel(DBTransaction pDbt) {
        //return Integer.parseInt(ADFContext.getCurrent().getPageFlowScope().get("G_USER_SNO").toString());
        ApplicationModule am=pDbt.getRootApplicationModule();
        ViewObject vo=am.findViewObject("erpChkConn");
        if (vo!=null) {
            vo.remove();
       }
        
        try {
            vo=am.createViewObjectFromQueryStmt("erpChkConn", "SELECT SYS_CONTEXT ('USERENV', 'SESSION_USER') FROM DUAL");
            vo.executeQuery();
            vo.remove();
            return "ERPORACLE";
        }
        catch(Exception exc) {
            vo.remove();
            return "ERPMYSQL";
        }
    }
  
    public static String doGetModelUserName() {
        //return ERPGlobalPLSQLClass.doGetModelUserSno();
        return ADFContext.getCurrent().getPageFlowScope().get("G_USER_NAME").toString();
    }

    public static String doGetModelUserPicture() {
        //return ERPGlobalPLSQLClass.doGetModelUserSno();
        try {
            return ADFContext.getCurrent().getPageFlowScope().get("G_USER_PICTURE").toString();
        } catch (NullPointerException e) {
            // TODO: Add catch code
            return null;
        }
    }  
      
}
