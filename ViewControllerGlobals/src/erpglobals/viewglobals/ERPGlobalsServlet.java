package erpglobals.viewglobals;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import javax.sql.DataSource;

@WebServlet(name = "ERPGlobalsServlet", urlPatterns = { "/erpglobalsservlet" })
public class ERPGlobalsServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            //String path = (request.getParameter("path"));
           // System.out.println(request.getParameter("filename")+"request.getParameter(filename)");
            String erpsourceId=request.getParameter("erpsourceid");//.toString();;
            String erpsourceType=request.getParameter("erpsourcetype");//.toString();;
            String erpfileName=request.getParameter("erpfilename");//.toString();
            System.out.println(erpfileName+ " fileName");
            OutputStream os = response.getOutputStream();
            InputStream inputStream = null;
            Connection conn = null;
            try {
                
                //////////////////////////////////////////////
                Context ctx = new InitialContext();
                
                Statement statement = null;
                ResultSet rs = null;
                //Datasource as defined in <res-ref-name> element of weblogic.xml
                DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/ERPConnGlobalsDS");
                //Blob blob=null;
                File outputFile=null;
                conn = ds.getConnection();
                statement = conn.createStatement();
                rs = statement.executeQuery("select X.VALUE_DESCRIPTION from SYS_GENERAL_VALUE X WHERE X.Value_Set_Id=6 AND IS_ACTIVE='Y'");
                rs.next();
                String storeOn=rs.getString(1);
                //System.out.println("storeOn"+storeOn);
                
                String docPath="";
                //if condition checking files are storing on file system or in database
                if (storeOn.equals("FILE_SYSTEM")) {
                   rs = statement.executeQuery("select X.VALUE_DESCRIPTION from SYS_GENERAL_VALUE X WHERE X.Value_Set_Id=7 AND IS_ACTIVE='Y'");
                   rs.next();
                   docPath=rs.getString(1);
                   //docPath="C:\\hello\\";
                   outputFile = new File(docPath+erpfileName);
                   inputStream =new FileInputStream(outputFile);
                 }
               else {
                    rs = statement.executeQuery("SELECT A.SOURCE_ID,A.IMAGE_BLOB FROM TBL_PHYSICAL_FILES A WHERE A.SOURCE_TYPE='"+erpsourceType +"' And A.SOURCE_ID="+erpsourceId);
                    rs.next();
                   // blob = rs.getBlob(2);
                    //inputStream=blob.getBinaryStream();                   
                    inputStream=rs.getBinaryStream(2);
                }
               /*
                else if(2==1)
               {
                    rs = statement.executeQuery("select parameter_value from SYS_PARAMETERS where parameter_id='REF_DOC_PATH'");
                       rs.next();
                       
                    docPath=rs.getString(1);
                    outputFile = new File(docPath+sourceType);
                    inputStream =new FileInputStream(outputFile);
               }*/
                   
               /////////////////////////////////////////////
                 System.out.println("calling me");
           
                //File outputFile = new File(path);
                //inputStream = new FileInputStream(outputFile);
                BufferedInputStream in = new BufferedInputStream(inputStream);
                int b;
                byte[] buffer = new byte[10240];
                while ((b = in.read(buffer, 0, 10240)) != -1) {
                    os.write(buffer, 0, b);
                }

            } catch (Exception e) {
            } finally {
    
               try {
                       if (os != null) {
                           os.close();
                       }
                       if (inputStream != null) {
                           inputStream.close();
                       }
                        if (conn!=null) {
                            conn.close();
                       }
                   } catch (Exception e) {
                        // TODO: Add catch code
                       // e.printStackTrace();
                    }
            }
        }
}
