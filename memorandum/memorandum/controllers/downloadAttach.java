package memorandum.controllers;

import memorandum.Defaults;
import memorandum.daoHandler;
import memorandum.dataModel.dto.memoDTO;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;


public class downloadAttach extends HttpServlet {
    // ��������� �������, ������������������� ��� ������� ����������
    Logger logger = Logger.getLogger(getClass().getName());

    // ������ ������� ������������ ������ ����� "GET"
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("ENTERING downloadAttach -> doGet().");
       
        String FullPathName;         // ������ ��� ����� �� HDD �������(� �����)
        int memoID = 0;    // ������ ������������� ��������
        memoDTO memo;                 // ������ ���� ��������
        daoHandler dH = null;

        // ��� �������� �������� ����������� � ����� try...catch - ��� ����� ���������
        // ������ ������ ���������������� �� ������� �������� (�������� �������� ���-�)
        try {

            // �������� �� ������ ������������ ������ DaoHandler
            Object object = request.getSession().getAttribute(Defaults.ATTRIBUTE_SESSION_DAO);
            if (object != null) {
                dH = (daoHandler) object;
            } else {
                logger.error("DaoHandler object in session not initialized!");
            }

            // ������ �� ������-����� ���������� ������� ��� �������� ������ �� ������
            String upload_dir = getServletContext().getInitParameter(Defaults.UPLOAD_DIR);
            // ���� ������ ������������ ����� ������. �������� � ������������� ��� (���� ����).
            if (!upload_dir.endsWith("\\")) upload_dir = upload_dir.concat("\\");
            // �������� ������������� �������� - ��� ������: ������ � ��� � �� �������
            if (!new File(upload_dir).exists()) throw new Exception("Upload dir [" + upload_dir + "] doesn't exists!");
            // ������� ������� �������� - ID ��������, ��� ���� ����� ���������
            try {
                memoID = Integer.parseInt(request.getParameter("memoID"));
            }
            catch (Exception e) {
                throw new Exception("Invalid memoID parameter [" + memoID + "]!");
            }
            // ������ ���� ��������
            memo = dH.getMemoDAO().findByID(dH, memoID);
            // ���� �������� �� ������� - �����.
            if (memo == null) throw new Exception("Memo with ID = " + memoID + " doesn't exists!");
            // ������� ������ ��� ����� (�� ������� ����� �������)
            FullPathName = upload_dir + memo.getId() + "." + memo.getAttachedFile();
            // ����� ��������� ����� ������� �������� ��� ��������
            File fileToDownload = new File(FullPathName);
            // ���� ���� ���������� �� ����� -> ��������
            if (fileToDownload.exists()) {
                // --- ��������������� �������� ����� ������������ ---
                response.setContentType("APPLICATION/OCTET-STREAM");
                response.setBufferSize(8192);
                String disHeader = "Attachment; filename=\"" + memo.getId() + "." + memo.getAttachedFile() + "\"";
                response.setHeader("Content-Disposition", disHeader);
                PrintWriter out = response.getWriter();
                // transfer the file byte-by-byte to the response object
                FileInputStream fileInputStream = new FileInputStream(fileToDownload);
                int i;
                while ((i = fileInputStream.read()) != -1) {
                    out.write(i);
                }
                fileInputStream.close();
                out.close();
            } else throw new Exception("File [" + FullPathName + "] doesn't exists!");
        } // <- END OF {TRY} SECTION
        // �������� ��
        catch (Exception e) {
            // ��� ������������� �� ������ ������ � ���
            logger.error("ERROR occured while download file: [" + e.getMessage() + "]");
            // ��������� �� ������� �������� ����������
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/controller?action=depts");
            dispatcher.forward(request, response);
        }
        logger.debug("LEAVING downloadAttach -> doGet().");
    } // END OF {doGET} METHOD

} // END OF {DOWNLOAD_ATTACH} SERVLET
