package erpglobals.viewglobals;


import erpglobals.modelglobals.ERPDataEncryption;
import erpglobals.modelglobals.ERPGlobalPLSQLClass;
import erpglobals.modelglobals.ERPUserAttribute;

import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.URL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.imageio.ImageIO;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.event.PopupCanceledEvent;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.ApplicationModule;
import oracle.jbo.JboException;
import oracle.jbo.ViewObject;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.BlobDomain;
import oracle.jbo.server.DBTransaction;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.ViewRowImpl;

import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class ERPGlobalsClass {
    public ERPGlobalsClass() {
        super();
    }
private RichPopup erpConfPwdChange;
private Integer ErpUserId;
private String ErpUserCode;
private String ErpGlobalDefCompany;
private ERPUserAttribute ERPUserAttributes;

private static final String POST_URL = "http://smsctp3.eocean.us:24555/api?action=sendmessage&username=digicom9913&password=DgMCm786&recipient=923337116635&originator=9913&messagedata=thisis=test-jdev";

    public void setERPUserAttributes(ERPUserAttribute ERPUserAttributes) {
        this.ERPUserAttributes = ERPUserAttributes;
    }

    public ERPUserAttribute getERPUserAttributes() {
        return ERPUserAttributes;
    }

    public void setErpGlobalDefCompany(String ErpGlobalDefCompany) {
        this.ErpGlobalDefCompany = ErpGlobalDefCompany;
    }

    public String getErpGlobalDefCompany() {
        return ErpGlobalDefCompany;
    }

    public void setErpUserCode(String ErpUserCode) {
        this.ErpUserCode = ErpUserCode;
    }

    public String getErpUserCode() {
        return ErpUserCode;
    }

    public void setErpUserId(Integer ErpUserId) {
        this.ErpUserId = ErpUserId;
    }

    public Integer getErpUserId() {
        return ErpUserId;
    }

    public void setErpConfPwdChange(RichPopup erpConfPwdChange) {
        this.erpConfPwdChange = erpConfPwdChange;
    }

    public RichPopup getErpConfPwdChange() {
        return erpConfPwdChange;
    }

    /*public static void doSetErpUserValues(ERPUserAttribute erpUserAttribute) {
        System.out.println("doSetErpUserValues");
        //return ERPGlobalPLSQLClass.doGetModelUserSno();
        ///System.out.println("G_USER_SNO"+getErpUserId());
        //System.out.println("G_USER_CODE"+getErpUserCode());
        ADFContext.getCurrent().getPageFlowScope().put("G_USER_SNO",erpUserAttribute.getUserId());
        ADFContext.getCurrent().getPageFlowScope().put("G_USER_CODE",erpUserAttribute.getUserCode());
        ADFContext.getCurrent().getPageFlowScope().put("G_GLOBAL_DEF_COMPANY",erpUserAttribute.getErpDefGloalCompany());
        
    }*/
    
    public static Integer doGetUserSno() {
        
        //return ERPGlobalPLSQLClass.doGetModelUserSno();
        return 
          Integer.parseInt(ADFContext.getCurrent().getPageFlowScope().get("G_USER_SNO").toString());
    }

    public static String doGetUserCode() {
        
        //return ERPGlobalPLSQLClass.doGetModelUserSno();
        return ADFContext.getCurrent().getPageFlowScope().get("G_USER_CODE").toString();
    }

    public static String doGetUserName() {
        //return ERPGlobalPLSQLClass.doGetModelUserSno();
        return ADFContext.getCurrent().getPageFlowScope().get("G_USER_NAME").toString();
    }

    public static String doGetUserPicture() {
        //return ERPGlobalPLSQLClass.doGetModelUserSno();
        try {
            return ADFContext.getCurrent().getPageFlowScope().get("G_USER_PICTURE").toString();
        } catch (NullPointerException e) {
            // TODO: Add catch code
            return null;
        }
    }
    
        
    public static Integer doGetGlobalDefCompany() {
        return 
          Integer.parseInt(ADFContext.getCurrent().getPageFlowScope().get("G_GLOBAL_DEF_COMPANY").toString());
   }
    
    public static BindingContainer doGetERPBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public static OperationBinding doGetERPOperation(String pOperationName) {
        OperationBinding operation = doGetERPBindings().getOperationBinding(pOperationName);
        if (operation == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage("Operation (" + pOperationName +
                                                                          ") Doesn't Exist."));
            return null;
        }
        return operation;
    }

    public static String isRecordChanged(String pIteratorName) {
        DCIteratorBinding iterator = (DCIteratorBinding) doGetERPBindings().get(pIteratorName);
        if (iterator == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                                                         new FacesMessage("Iterator (" + pIteratorName +
                                                                          ") Doesn't Exist."));
            return null;
        }
        try {
            ViewObject vo = iterator.getViewObject();
            ViewRowImpl rimpl = (ViewRowImpl) vo.getCurrentRow();
            EntityImpl entity = rimpl.getEntity(0);
            byte state = entity.getEntityState();
            System.out.println(state + "<state");
            if (state != 1 /*entity.STATUS_UNMODIFIED*/) {
                return "YES";
            }
            return "NO";
        } catch (NullPointerException e) {
            // TODO: Add catch code
            return "NO";
        }
    }

    public static String doExecutePKSQLQuery(DBTransaction pDBT, String pColumnName, String pTableName,
                                           String pWhereColumn, String pWhereColumnValue) {

        try {
            String lResult =
                ERPGlobalPLSQLClass.doGetPrimaryKeyValueModel(pDBT, pColumnName, pTableName, pWhereColumn, pWhereColumnValue);
            return lResult;
        } catch (Exception e) {
            // TODO: Add catch code
            throw new JboException("Error While Execution Primary Key Query (" + e.getMessage() + ").");
        }
    }
 
    public static String doExecuteSQLQuery(DBTransaction pDBT, String pSqlQuery) {
        try {
            String lResult =
                ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT, pSqlQuery);
            return lResult;
        } catch (Exception e) {
            // TODO: Add catch code
            throw new JboException("Error While Execution SQL Query (" + e.getMessage() + ").");
        }
    }

   public static ERPUserAttribute doFuncCheckErpLoginView(DBTransaction pDBT, String pUserCode, String pPassword)  {
        try {
           // System.out.println("i am doFuncCheckErpLoginView");
            ERPUserAttribute lResult = ERPGlobalPLSQLClass.doFuncCheckErpLogin(pDBT, pUserCode, pPassword);
            return lResult;
        } catch (Exception e) {
            // TODO: Add catch code
            throw new JboException("Error While Login Authentication (" + e.getMessage() + ").");
        }
    }

    public static ERPUserAttribute doFuncCheckPwdComplaxityView(DBTransaction pDBT, Integer pUserID, String pPassword)  {
         try {
           // System.out.println("i am doFuncCheckPwdComplaxityView");
            ERPUserAttribute lResult = ERPGlobalPLSQLClass.doFuncCheckPwdComplaxity(pDBT, pUserID, pPassword);
            return lResult;
         } catch (Exception e) {
             // TODO: Add catch code
             throw new JboException("Error While Setting Password Complexity (" + e.getMessage() + ").");
         }
     }
    
    public static void doShowERPMessage(String pMessage,FacesMessage.Severity pSeverity) {
        try {
            FacesMessage erpfm = new FacesMessage(pMessage);
            erpfm.setSeverity(pSeverity);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(pMessage));

        } catch (Exception e) {
            // TODO: Add catch code
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erorr While Showing Message. Please Set Severity"));
        }      
    } 
   public String doERPPasswordChange() {
       
       OperationBinding ob=this.doGetERPOperation("doERPChangePassword");
       ob.execute();
       if (!ob.getErrors().isEmpty()) {
            return null;
       }
       RichPopup.PopupHints hints = new RichPopup.PopupHints();
       getErpConfPwdChange().show(hints);
       //return "ERPACTPWDCHANGEDONE";
       return null;
   }

    public String doErpPasswordChangeConfirm() {
        getErpConfPwdChange().hide();
        return "ERPACTPWDCHANGEDONE";
    }
    public void erpPopupCancelListner(PopupCanceledEvent ce) {
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        getErpConfPwdChange().show(hints);
    }
    public void doSetErpGlobals() {
        /*
        ADFContext.getCurrent().getPageFlowScope().put("G_USER_SNO",1);
        ADFContext.getCurrent().getPageFlowScope().put("G_USER_CODE","FARRUKH");
        ADFContext.getCurrent().getPageFlowScope().put("G_GLOBAL_DEF_COMPANY",1);
        */
        System.out.println("global-01");
        ADFContext.getCurrent().getPageFlowScope().put("G_USER_SNO",getERPUserAttributes().getUserId());
        System.out.println("global-02");
        ADFContext.getCurrent().getPageFlowScope().put("G_USER_CODE",getERPUserAttributes().getUserCode());
        System.out.println("global-03");
        ADFContext.getCurrent().getPageFlowScope().put("G_GLOBAL_DEF_COMPANY",getERPUserAttributes().getErpDefGloalCompany());
        System.out.println("global-04");

    }
    
    public void doSetErpApplicationGlobals(ERPUserAttribute erpUserAttributes) {
        setERPUserAttributes(erpUserAttributes);
        doSetErpGlobals();
    }

    public static boolean doCheckERPTransactionDirty() {
        //this will check do we have any unsaved changes on form
        BindingContext context = BindingContext.getCurrent();
        DCBindingContainer binding = (DCBindingContainer) context.getCurrentBindingsEntry();
        DCDataControl dc = binding.getDataControl();

        return dc.isTransactionDirty();
    } 



    public static void ErpuploadImage(UploadedFile file, String pUploadPath,String pColumnName, String pStoreOn, String pIteratorName,String pSourceId, String pSourceType) {
           UploadedFile Erpmyfile = file;
           String path =pUploadPath;
            
            if (Erpmyfile == null) {

           } 
           else {
               if (Erpmyfile.getContentType().equalsIgnoreCase("image/jpeg") ||
                   Erpmyfile.getContentType().equalsIgnoreCase("image/jpg") ||
                   Erpmyfile.getContentType().equalsIgnoreCase("image/png") ||
                   Erpmyfile.getContentType().equalsIgnoreCase("image/bmp") ||
                   Erpmyfile.getContentType().equalsIgnoreCase("image/gif")) {
                    //Path of folder on drive
                   String type = "PNG";
                   String TypeVal = ".png";
                   if (Erpmyfile.getContentType().equalsIgnoreCase("image/jpeg")) {
                       type = "JPEG";
                       TypeVal = ".jpeg";
                   } else if (Erpmyfile.getContentType().equalsIgnoreCase("image/png")) {
                       type = "PNG";
                       TypeVal = ".png";
                   } else if (Erpmyfile.getContentType().equalsIgnoreCase("image/bmp")) {
                       type = "PNG";
                       TypeVal = ".png";
                   } else if (Erpmyfile.getContentType().equalsIgnoreCase("image/gif")) {
                       type = "GIF";
                       TypeVal = ".gif";
                   }
                   else if (Erpmyfile.getContentType().equalsIgnoreCase("image/jpg")) {
                                       type = "JPG";
                                       TypeVal = ".jpg";
                                   }                

                   InputStream inputStream = null;
                   try {
                       //Generate a unique name for uploaded image with date time
                       DateFormat dateFormat = new SimpleDateFormat("yyMMdd_HHmmss");
                       Date date = new Date();
                       String dtTime = dateFormat.format(date);
                       dtTime = dtTime.replace(" ", "_");

                       String name = "DOC" + "_" + dtTime;
                       System.out.println("File name is-" + name);
                       if (pStoreOn.equals("FILE_SYSTEM")) {
                           System.out.println("this is storing");
                           inputStream = Erpmyfile.getInputStream();
                           BufferedImage input = ImageIO.read(inputStream);
                           File outputFile = new File(path + name + TypeVal);
                           ImageIO.write(input, type, outputFile);
                           //imagePath = outputFile.getAbsolutePath();
                           
                       }
                       //System.out.println("this is uploading>>"+name + TypeVal+file);
                        ErpdoSaveDocName(pColumnName,name + TypeVal, Erpmyfile,pStoreOn,pIteratorName, pSourceId, pSourceType);
                    
                      // System.out.println("this is uploading- completed");
                   } catch (Exception ex) {
                       // handle exception
                       ex.printStackTrace();
                   } finally {
                       try {
                           inputStream.close();
                       } catch (Exception e) {
                       }
                   }
               }
               else if(Erpmyfile.getContentType().equalsIgnoreCase("application/pdf")) {
                   
                   DateFormat dateFormat = new SimpleDateFormat("yyMMdd_HHmmss");
                   Date date = new Date();
                   String dtTime = dateFormat.format(date);
                   dtTime = dtTime.replace(" ", "_");

                   String name = "DOC" + "_" + dtTime;
                   System.out.println("PDF File name is-" + path + name + ".pdf");
                   if (pStoreOn.equals("FILE_SYSTEM")) {
                     //  doCreateFile(myfile, path + name + ".pdf");
                   }
                //   doSaveImage(name+".pdf",file);
               }
               
           }
       }
 
    public static void ErpdoSaveDocName(String pColumnName,String pImageName,UploadedFile pUploadFile, String pStoreOn, String pIteratorName,String pSourceId, String pSourceType) {
        BindingContainer bindings = doGetERPBindings();
        DCIteratorBinding ib=(DCIteratorBinding)bindings.get(pIteratorName);
        ViewObject vo=ib.getViewObject();
        ib.getCurrentRow().setAttribute(pColumnName,pImageName);
        if (pStoreOn.equals("DATABASE")) {
            ErpStoreFileToDB(pUploadFile,(DBTransaction)vo.getApplicationModule().getTransaction(),pSourceId,pSourceType);
        }
    }
    
    private static void ErpStoreFileToDB(UploadedFile pFile,DBTransaction pERpDBT,String pSourceId, String pSourceType) {

        InputStream in = null;
        BlobDomain blobDomain = null;
        OutputStream out = null;

        try {
            in = pFile.getInputStream();

            blobDomain = new BlobDomain();
            out = blobDomain.getBinaryOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead = 0;

            while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }

        try {
            //String updateSQL = "UPDATE TBL_PHYSICAL_FILES " + "SET image_blob = ? " + "WHERE image_id=1";
            String ErpSQL = "select 1 from TBL_PHYSICAL_FILES where source_id='"+pSourceId+"' and source_type='"+pSourceType+"'";
            
            PreparedStatement pstmt = pERpDBT.createPreparedStatement(ErpSQL,1);
            ResultSet erpResultSet = pstmt.executeQuery();
            erpResultSet.next();
                
            if(erpResultSet.getRow()==1)//if document already exists against document type
            {   
            //System.out.println(erpResultSet.getInt(1) + " this is resultset");
            ErpSQL = "UPDATE TBL_PHYSICAL_FILES SET IMAGE_BLOB=? where source_id='"+pSourceId+"' and source_type='"+pSourceType+"'";
            }
            else 
            {//if document does not exist then system will copy it
                ErpSQL = "select coalesce(max(image_id),0)+1 from TBL_PHYSICAL_FILES";
                pstmt = pERpDBT.createPreparedStatement(ErpSQL,1);
                erpResultSet = pstmt.executeQuery();
                erpResultSet.next();
                ErpSQL = "INSERT INTO TBL_PHYSICAL_FILES (image_id, \n" + 
                "source_id, \n" + 
                "image_blob, \n" + 
                "source_type, \n" + 
                "doc_type_id) VALUES ("+erpResultSet.getInt(1)+","+pSourceId+",?,"+pSourceType+",null)";
            }
                pstmt = pERpDBT.createPreparedStatement(ErpSQL,1);
                pstmt.setBinaryStream(1, blobDomain.getInputStream());
                pstmt.executeUpdate();
            } catch (Exception sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }
    }
  
    
    public static void ErpdoOpenUrl(String pUrl) {
        ExtendedRenderKitService erks =
            Service.getRenderKitService(FacesContext.getCurrentInstance(), ExtendedRenderKitService.class);
        StringBuilder strb = new StringBuilder("window.open('" + pUrl + "');");
        erks.addScript(FacesContext.getCurrentInstance(), strb.toString());
    }  
  
  public static void main(String[] args) {
      //////////
      ADFContext oldContext = ADFContext.initADFContext(null, null, null, null);
        try {
            String amDef = "erpglobals.modelglobals.globalsam.ERPGlobalsModule";
            String config = "ERPGlobalsModuleLocal";
            ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
            // Work with your appmodule and view object here
            
           // System.out.println("sss"); 
            
            
      /////////
       //          (pDBT,pFromEmailName,pFromEmailAddress,String[][] pToEmailAddress,String[][] pCCEmailAddress,String[][] pBCCEmailAddress,String pSubject,String pEmailText,String pSmtpHost, Integer pPort,String pPassword) {   
      //ErpDoSendEmail((DBTransaction)am.getTransaction(),"GIGI PVT","gigipvt@gmail.com",new String[][]{{"FARRUKH SHAIKH","mefarrukh@hotmail.com"},{"FARRUKH SHAIKH GMAIL","me.farrukhshaikh@gmail.com"}},new String[][]{{"FARRUKH SHAIKH ADF","adfprofessional2@gmail.com"}},new String[][]{{"COOL MAN","itis_coooool@hotmail.com"}},"THIS IS TEST EMAIL-CC","THIS IS MY EMAIL <b>TEXT</b>","smtp.googlemail.com",587,"GiGipvt_123");
            //ErpDoSendEmail((DBTransaction)am.getTransaction(),null,null,new String[][]{{"FARRUKH SHAIKH","mefarrukh@hotmail.com"},{"FARRUKH SHAIKH GMAIL","me.farrukhshaikh@gmail.com"}},null,null,"THIS IS TEST EMAIL-CC","THIS IS MY EMAIL <b>TEXT</b>",null,null,null);
       //ERPdoSendSMS(DBTransaction pDBT,String pSMSHost,String pUserId,String pPassword, String pURL,String pReceiption,String pSMSMASK,String pSMSText,String pOtherPara ) throws IOException {     
            //ERPdoSendSMS((DBTransaction)am.getTransaction(), null, null, null, null, "923337116635", null, "hello message", "&originator=9913");
            ERPdoSendSMS((DBTransaction)am.getTransaction(), null, null, null, null, "923337116635", null, "hello message", null);
            Configuration.releaseRootApplicationModule(am, true);
       } catch (IOException e) {
            System.out.println("this is test");
        } finally {
           ADFContext.resetADFContext(oldContext);
       }
   }
    
    public static void ErpDoSendEmail(DBTransaction pDBT,String pFromEmailName,String pFromEmailAddress,String[][] pToEmailAddress,String[][] pCCEmailAddress,String[][] pBCCEmailAddress,String pSubject,String pEmailText,String pSmtpHost, Integer pPort,String pPassword) {
        
        try {
       
            if (pToEmailAddress==null) {
             doShowERPMessage("To Address Is Required.", FacesMessage.SEVERITY_ERROR);
                return;
            }
            
            if (pSubject==null) {
             doShowERPMessage("Email Subject Is Required.", FacesMessage.SEVERITY_ERROR);
                return;
            } 
              
            
            if (pEmailText==null) {
             doShowERPMessage("Email Text Is Required.", FacesMessage.SEVERITY_ERROR);
            return;
            }     
            
         String erpFromEmailName=pFromEmailName;
         String erpEmailText=pEmailText;
         String erpSubject=pSubject;
         Integer erpPort=pPort;
         String erpSmtpHost=pSmtpHost;
         String erpPassword=pPassword;
         String erpFromEmailAddress=pFromEmailAddress;
         
            /*
        s[0]=new String[]{"Name","Email"};
        s[1]=new String[]{"Name1","Email1"};
        s[2]=new String[]{"Name2","Email2"};
             */
           
         String[][] ErpToAddress=pToEmailAddress;/*[emailaddress@domain.com][Mr.name]*/
         String[][] ErpCCAddress=(pCCEmailAddress==null?new String[0][0]:pCCEmailAddress);/*[emailaddress@domain.com][Mr.name]*/
         String[][] ErpBCCAddress=(pBCCEmailAddress==null?new String[0][0]:pBCCEmailAddress);/*[emailaddress@domain.com][Mr.name]*/
        
         if (pDBT != null && erpPassword==null) {
             erpPassword =
                 ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT,"select gv.value_description from sys_general_value gv where  gv.is_active='Y' and gv.value_set_id=21");
            try {
                    erpPassword = ERPDataEncryption.ERPdoDecrypt(erpPassword);
                System.out.println(erpPassword + " erppassword");
                } catch (Exception e) {
                    // TODO: Add catch code
                    e.printStackTrace();
                }
         }
        
        
         if (pDBT != null && erpSmtpHost==null) {
             erpSmtpHost =
                 ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT,"select gv.value_description from sys_general_value gv where  gv.is_active='Y' and gv.value_set_id=17");
         }

            if (pDBT != null && erpPort==null) {
                erpPort =
                    Integer.parseInt(ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT,"select gv.value_description from sys_general_value gv where  gv.is_active='Y' and gv.value_set_id=20"));
            }

          
            if (pDBT != null && pFromEmailAddress==null) {
               erpFromEmailAddress=ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT, "select gv.value_description from sys_general_value gv where  gv.is_active='Y' and gv.value_set_id=18");
           }
      
            if (pDBT != null && pFromEmailName==null) {
                erpFromEmailName =
                    ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT,"select gv.value_description from sys_general_value gv where  gv.is_active='Y' and gv.value_set_id=19");
            }
       
            
            if (erpFromEmailAddress==null) {
                doShowERPMessage("From Email Address Is Required.", FacesMessage.SEVERITY_ERROR);
                return;
            }
            
            
            
            if (erpPassword==null) {
                doShowERPMessage("Password Is Required.", FacesMessage.SEVERITY_ERROR);
                return;
               }
            
            if (erpPort==null) {
                doShowERPMessage("Port Is Required.", FacesMessage.SEVERITY_ERROR);
                return;
               }
            
         Properties Erpprops =  new Properties();
         //Erpprops.put("mail.smtp.host", "smtp.googlemail.com");
         Erpprops.put("mail.smtp.host", erpSmtpHost);
         Erpprops.put("mail.smtp.port", erpPort);
            //Erpprops.put("mail.smtp.port", "587");
         Erpprops.put("mail.smtp.ssl.trust","*");
         //Erpprops.put("mail.smtp.ssl.trust","smtp.googlemail.com");
         Erpprops.put("mail.smtp.starttls.enable", "true");
          Erpprops.put("mail.smtp.auth", "true");
          Erpprops.put("mail.smtp.connectiontimeout", "10000");
          final String EmailUser = erpFromEmailAddress;//"gigipvt@gmail.com";
          final String EmailPassword = erpPassword;//"GiGipvt_123";
          Session session = Session.getInstance(Erpprops, new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                  return new PasswordAuthentication(
                                          EmailUser,EmailPassword);
                               }
                     });
          session.setDebug(true); 
          //InternetAddress fromAddress = new InternetAddress("gigipvt@gmail.com","GIGI PVT");
            InternetAddress fromAddress = new InternetAddress(erpFromEmailAddress,erpFromEmailName);
            InternetAddress toAddress =null;
            InternetAddress ccAddress =null;
            InternetAddress bccAddress =null;
            
          //String msgSubject = pSubject;
          String msgBody =erpEmailText;// "<i> This Is Message Body  With Address </i>";
            
          Message msg = new MimeMessage(session);
          msg.setFrom(fromAddress);
            
            ///////////////////////
            
            for (int i = 0;( i < ErpToAddress.length); i++) {//for multiple to address
                for (int j = 0; j < ErpToAddress[i].length-1; j++) {
                   toAddress =new InternetAddress(ErpToAddress[i][j+1],ErpToAddress[i][j]);
                    System.out.print(ErpToAddress[i][j] );
               }
                
                msg.addRecipient(Message.RecipientType.TO,toAddress); 
            }
           
            for (int i = 0; (ErpCCAddress !=null && i < ErpCCAddress.length); i++) {//for multiple to address
                for (int j = 0; j < ErpCCAddress[i].length-1; j++) {
                   ccAddress =new InternetAddress(ErpCCAddress[i][j+1],ErpCCAddress[i][j]);
                    
               }
                msg.addRecipient(Message.RecipientType.CC,ccAddress); 
            }

            
            
            for (int i = 0;(ErpBCCAddress!=null && i < ErpBCCAddress.length ); i++) {//for multiple to address
                for (int j = 0; j < ErpBCCAddress[i].length-1; j++) {
                   bccAddress =new InternetAddress(ErpBCCAddress[i][j+1],ErpBCCAddress[i][j]);
                    
               }
                msg.addRecipient(Message.RecipientType.BCC,bccAddress); 
            }            
            ///////////////////////
            
          msg.setSubject(erpSubject);
          msg.setText(msgBody);
          msg.setContent(msgBody, "text/html");
          Transport transport = session.getTransport("smtp");
          transport.connect();
          transport.sendMessage(msg, msg.getAllRecipients());
             
        }
        catch (MessagingException e) {
          System.out.println(e.getMessage()+ e.getStackTrace());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Email Not Sent."));
        }
        catch (UnsupportedEncodingException e) {
          System.out.println(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Email Not Sent."));
        }
    }
    
    // function to generate a random string of length n 
    public static String getERPAlphaNumericString(Integer pPasswordLength) 
    { 
    
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+ "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
    
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(pPasswordLength); 
    
        for (int i = 0; i < pPasswordLength; i++) { 
    
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index = (int)(AlphaNumericString.length() * Math.random()); 
    
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString.charAt(index)); 
        } 
    
        return sb.toString(); 
    }
    public static String erpDoGetForceAlphaNumPass(Integer pPasswordLength) {
        //int erpPasswordLength = 8;
        int counter = 0;
        boolean strongPassword = false;
        String erpAlphanum = "", pwd = "", numberpattern = "[0-9]", stringpattern = "[a-zA-Z]";
        Matcher matchstring, matchnumber;
        Pattern pat;
        while (!strongPassword) {
            erpAlphanum = ERPGlobalsClass.getERPAlphaNumericString(pPasswordLength);
            while (true && erpAlphanum.length() > 0) {
                pwd += erpAlphanum.substring(counter, counter + 1);
                if (counter == erpAlphanum.length() - 1) {
                    counter = 0;
                    //strongPassword=true;
                    break;
                }
                pat = Pattern.compile(stringpattern);
                matchstring = pat.matcher(pwd);
                pat = Pattern.compile(numberpattern);
                matchnumber = pat.matcher(pwd);
                if (matchstring.find(0) && matchnumber.find(0)) {
                    strongPassword = true;
                    break;
                }
                counter++;
            }
            System.out.println("attempt:" + erpAlphanum);
            counter = 0;
        }
        System.out.println(erpAlphanum);
            return erpAlphanum;
    }


    public static void ERPdoSendSMS(DBTransaction pDBT,String pSMSHost,String pUserId,String pPassword, String pURL,String pReceiption,String pSMSMASK,String pSMSText,String pOtherPara ) throws IOException {
            String ErpUserId=pUserId;
            String ErpPassword=pPassword;
            String ErpSmtpHost=pSMSHost;
            String ErpReceiptPara;
            String ErpUrl=pURL;
            String ERPOtherPara=null;//=pOtherPara;
            String ErpSMSMask=pSMSMASK; 
            String ERPSmsText;
            
            if (pDBT != null && (ErpSmtpHost==null && pURL==null)) {
                ErpSmtpHost =ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT,"select gv.value_description from sys_general_value gv where  gv.is_active='Y' and gv.value_set_id=22");
                ErpUrl=ErpSmtpHost;
            }  
           
            if (pDBT != null && (ErpUserId==null && pURL==null)) {
                ErpUserId =ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT,"select gv.value_description from sys_general_value gv where  gv.is_active='Y' and gv.value_set_id=23");
                ErpUrl=ErpUrl+ErpUserId;
                ErpUserId =ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT,"select gv.value_description from sys_general_value gv where  gv.is_active='Y' and gv.value_set_id=30");
            try {
                ErpUserId = ERPDataEncryption.ERPdoDecrypt(ErpUserId);
                ErpUrl=ErpUrl+ErpUserId;
            } catch (Exception e) {
            }
            }  
           
            if (pDBT != null && (ErpPassword==null && pURL==null)) {
                ErpPassword =ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT,"select gv.value_description from sys_general_value gv where  gv.is_active='Y' and gv.value_set_id=24");
                ErpUrl=ErpUrl+ErpPassword;
                ErpPassword =ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT,"select gv.value_description from sys_general_value gv where  gv.is_active='Y' and gv.value_set_id=29");
            try {
               ErpPassword= ERPDataEncryption.ERPdoDecrypt(ErpPassword);
                ErpUrl=ErpUrl+ErpPassword;
            } catch (Exception e) {
            }
            //
            }  


            if (pDBT != null && (ErpSMSMask==null && pURL==null)) {//to get sms mask parameter value like receiption=
                ErpSMSMask =ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT,"select gv.value_description from sys_general_value gv where  gv.is_active='Y' and gv.value_set_id=25");
               // ErpUrl=ErpUrl+(ErpSMSMask.equals("-")?"":ErpSMSMask);
                System.out.println(ErpSMSMask+"ErpSMSMask>"+ErpUrl);
            }

       
            if (pDBT != null && (pURL==null)) {//we might pass other parameters
                ERPOtherPara =ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT,"select gv.value_description from sys_general_value gv where  gv.is_active='Y' and gv.value_set_id=26");
                ErpUrl=ErpUrl+(pOtherPara==null?"":pOtherPara)+(ERPOtherPara.equals("-")?"":ERPOtherPara);
            } 

            if (pDBT != null && (pURL==null)) {//to get receiption parameter 
                ErpReceiptPara =ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT,"select gv.value_description from sys_general_value gv where  gv.is_active='Y' and gv.value_set_id=28");
                ErpUrl=ErpUrl+ErpReceiptPara.replace("<P_RECIPIENT_NO>",pReceiption);
                
            }


            if (pDBT != null && (pURL==null)) {//we might pass other parameters
                ERPSmsText =ERPGlobalPLSQLClass.doExecuteSQLQueryModel(pDBT,"select gv.value_description from sys_general_value gv where  gv.is_active='Y' and gv.value_set_id=27");
                System.out.println("erpsmstext "+ERPSmsText);
                ErpUrl=ErpUrl+ERPSmsText.replace("<P_MESSAGE_TEXT>", pSMSText.replace(" ", "%20"));//"%20 for space"
            }
            
          System.out.println("this is url"+ErpUrl);
             final String ERP_USER_AGENT = "Mozilla/5.0";
                URL obj = new URL(ErpUrl);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", ERP_USER_AGENT);

                // For POST only - START
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                os.flush();
                os.close();
                // For POST only - END

                int responseCode = con.getResponseCode();
                //System.out.println("POST Response Code :: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                        con.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                        }
                        in.close();

                        // print result
                        System.out.println(response.toString());
                } else {
                        System.out.println("POST request not worked");
                }
        }   
}