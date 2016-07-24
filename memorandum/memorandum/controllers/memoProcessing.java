package memorandum.controllers;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import jpersonnel.dto.SimpleEmployeeDTO;
import memorandum.Defaults;
import memorandum.daoHandler;
import memorandum.dataModel.dto.memoDTO;
import memorandum.dataModel.dto.recipientDeptDTO;
import org.apache.commons.fileupload.*;
import org.apache.log4j.Logger;

public class memoProcessing extends AbstractController {
    /**
     * ������ ������� ������������ ������ ������ ��������� ������� "POST". ��
     * �������� ������ ���� "multipart/form-data" �� ����� � ������������ �� -
     * ����� ����������� � ��, ���� ����������� �� ����� �������. ������� �� �����
     * ����������� ����� - ������ � ������� ������������ - ����� ��������� ������
     * (��������/�� ��������) ������ ���������������� (����� ����������) �� JSP-��������,
     * ������� ���������� ��������� ���������� ��������� � �������� �� ��������� �����
     * ������� ����������. ��� ��������� ����������� �� ���� �� ���������. ���� �������� �����������
     * ��������, �� ���������� ����� - ���������� ��������� �� �����������.
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ��������� �������, ������������������� ��� ������� ����������
        Logger logger = Logger.getLogger(getClass().getName());
        logger.debug("ENTERING newMemoProcessing servlet.");
        String FileName; // �������� ��� ����� (� �����������)
        String SavedFileName = null; // ��� ������������ ����� (� �����������)
        // ������ ��� �������� ��������� � �������� ������ �������� ������. ���� ����� �������� null -
        // ������� ��������� ��� ������, ���� �� ���� ������, �� ��������� - � ������.
        List errorsList = null;
        // ��� ������������ ������������ ��������
        String remoteUser = null;
        // ������ �������-����������� ��������
        List recipientsList = null;
        // ���������-����������� ��������
        //SimpleEmployeeDTO simpleEmployee;
        // ��������������� ���� ��������
        memoDTO memo = null;
        // ���������� ��� �������� ����� �� ��� ������/��������. ���� ������ ���������� ����� �������� null,
        // �� ���� ��� �� �������������, ���� �� �� null, �� ���� ���� ��� ��� ������ - ����������� �� ��������
        // ������ ������ �����.
        FileItem file = null;
        // ������������� ������ ��� ������������������ �������� (������ ���� > 0)
        int newMemoID = -1;

        // �������� �������� ����� ��������, �� ��������� ��
        int createnew = 1;

        // ��������� ������ �� ��� DAO-���������� ����������
        //daoHandler dH = null;

        logger.debug("Recieving [upload_dir] and [upload_temp_dir].");
        // �������� �������� ��� ������� � ��������� ������
        String upload_dir = getServletContext().getInitParameter(Defaults.UPLOAD_DIR);
        String temp_dir = getServletContext().getInitParameter(Defaults.UPLOAD_TEMP_DIR);
        // ������(� ������) ������ ������ ��� ���������� �������� ������ (i*(1024b*1024b)Kb -> i Mb)
        final int MemBuffer = 1024;
        // ����. ������(����������� � ������) ������������ ����� (i*(1024b*1024b)Kb -> i Mb)
        final int MaxFileSize = (Integer.parseInt(Defaults.MAX_FILE_SIZE)) * 1024 * 1024;
        logger.debug("Starting data processing.");

        // ��������������� ��������� �������, ���������� ������� ����.
        try {           
            logger.debug("Recieved remoteUser = " + simpleEmployee.getShortName());

            // �.�. �� ��������� ��������� �������� - ������������������ ������������, ��
            // �������� ����������� ������ "��������" ����� ����� �������� ����������� ������������
            // (��������� ������������� ������������ � ������������� ��� ������)
            memo = new memoDTO();
            memo.setExecutorDeptID(simpleEmployee.getDepartmentId());
            memo.setExecutorUserID(simpleEmployee.getPersonnelId());
            memo.setUpdateUserID(simpleEmployee.getPersonnelId());

            // ���� ������ ������������ ����� ������. �������� � ������������� ��� (���� ����).
            if (!upload_dir.endsWith("\\")) upload_dir = upload_dir.concat("\\");
            // ����� � ���� � �������� ��� ��������� ������
            if (!temp_dir.endsWith("\\")) temp_dir = temp_dir.concat("\\");
            logger.debug("Checking [upload_dir] and [upload_temp_dir].");
            // �������� ������������� �������� - ��� ������: ������ � ��� � �� �������
            if (!new File(upload_dir).exists()) throw new Exception("Upload dir [" + upload_dir + "] doesn't exists!");
            // ����� �������� ������������� �������� ��� ��������� ������
            if (!new File(temp_dir).exists()) throw new Exception("Temporary dir [" + temp_dir + "] doesn't exists!");
            logger.debug("Checking form data type (data must be multipart).");
            // �������� ��� ������ �� ����� - ���� ������ �� multipart -> �����
            if (!FileUpload.isMultipartContent(request)) {
                throw new Exception("FORM DATA IS NOT MULTIPART!");
            }

            logger.debug("Creating DiskFileUpload object.");
            // �������� ������-���������� �������� �����
            DiskFileUpload upload = new DiskFileUpload();
            // ������� �������� ������� �������:
            upload.setSizeThreshold(MemBuffer);
            upload.setSizeMax(MaxFileSize);
            //������� ��� ���������� �������� ������
            upload.setRepositoryPath(temp_dir);
            // ��������������� ������ �������
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();
            logger.debug("Starting form data processing (in cycle).");

            // ���������������� ��������� ������, �������� �� �����...
            while (iter.hasNext()) {

                FileItem item = (FileItem) iter.next();

                // ---------- ������ ����� ����� ----------
                if (item.isFormField()) {

                    // ������� ��� � �������� ���� �� ����� (���� ����� �������������:
                    // new String(item.getString().getBytes(), encoding);)
                    String name = item.getFieldName();
                    String value = item.getString("windows-1251");
                    logger.debug("Recieved data pair: [" + name + " = " + value + "].");

                    // ���� ��� ������������� �������� � �� > 0, �� ���� ��� ��������� � �������� � �������� (��� ������,
                    // ��� �� �� ������� � �������� ��������)
                    if (name.equals("memoID")) {
                        // ������� ��������� ������������� � ����� �����
                        try {
                            newMemoID = Integer.parseInt(value);
                        } catch (Exception e) {
                            logger.error("MemoID is invalid [" + value + "]!");
                        }
                        // ���� ���������� ������������� ������ ���� - ��������� ��� � ��������
                        if (newMemoID > 0) memo.setId(newMemoID);
                    }

                    // ���� ��� ���� ������ �� ��������, ������� ��� � ��� ������ � � ����������
                    // (���� ���� ���������� - ���� �������-����������� ���� ���������� ���� "realized"[���������] = 0)
                    else if ((name.equals("realizedDate")) && (value != null) && (!value.trim().equals(""))) {
                        memo.setRealizedDate(value);
                    }
                    // ���� ��� �����-���������� - ������� ��� � ������
                    else if (name.equals("recipient")) {
                        int deptID = -1;
                        // ������� �������� ������������� ������-����������
                        try {
                            deptID = Integer.parseInt(value);
                        } catch (Exception e) {
                            logger.error("deptID [" + value + "] is invalid!");
                        }
                        // ���� ������� �������� ������������� - ��������� ���������� � ������
                        if (deptID > 0) {

                            // ����� ������ - �����-���������� ��������
                            recipientDeptDTO recipientDept = new recipientDeptDTO();
                            // ������������� ������-����������
                            recipientDept.setRecipientDeptID(deptID);
                            // ������������� ������������, ���������� ������ ������
                            recipientDept.setUpdateUserID(simpleEmployee.getPersonnelId());
                            if (recipientsList == null) recipientsList = new ArrayList();
                            recipientsList.add(recipientDept);

                        }
                    }
                    // ���� ��� ���� �������� - ������� �� � ��������
                    else if (name.equals("subject")) {
                        if ((value != null) && (!value.trim().equals(""))) {
                            memo.setSubject(value.replaceAll("'", "\""));
                        } else throw new Exception("Subject is empty!");
                    }
                    // ���� ��� ���������� �������� - ������� �� � ��������
                    else if (name.equals("note")) {
                        if ((value != null) && (!value.trim().equals(""))) memo.setNote(value);
                        else throw new Exception("Note is empty!");
                    }
                    // ���� ��� ����� �������� - ������� ��� � ��������
                    else if (name.equals("text")) {
                        if ((value != null) && (!value.trim().equals(""))) {
                            memo.setText(value.trim().replaceAll("'", "\""));
                        } else throw new Exception("Text is empty!");
                    }
                    // ���� ������ �������� - ����� �� ������, �� �������� ID ������������ ��������
                    else if (name.equals("parentID")) {
                        int parentID = 0;
                        try {
                            parentID = Integer.parseInt(value);
                        }
                        catch (Exception e) {
                            logger.error("Invalid parentID value!");
                        }
                        // ���� �������� ���������� ����������������� �������� - ��������� ���
                        if (parentID > 0) memo.setParentID(parentID);
                    }
                    // �������� ���� �� ���� � ������������� ���������� ����� ����������. ������� ��� ����, ����� ���
                    // �������������� �������� �� ��������� ������ �� ����������, �.�. ���� �� ������� ����
                    // �� ��� update �������� ���� �� ��������� ���������
                    else if (name.equals("fileExt")) {
                        if ((value != null) && (!value.trim().equals(""))) {
                            memo.setAttachedFile(value.trim());
                        }
                    }
                }

                // ---------- ������ �������� ����� ----------
                else {

                    // ������� ������ �����
                    long sizeInBytes = item.getSize();

                    // ���� ������ ����� <= 0 -> ��� �� �������� ����. ���� ���� �� ������������, ����� - ������.
                    if ((sizeInBytes > 0) && (file == null)) {

                        logger.debug("File size OK [" + sizeInBytes + "]! This is first recieved file. Processing.");
                        // �������� ���� � ���������� (� ������)
                        file = item;
                        // ������� ������ ��� ����� � ������ �������� ������� ������� � �������� ��� \ �� /
                        String FullFileName = new String(file.getName().getBytes(), Defaults.ENCODING).replace('\\', '/');
                        // ��������� ������ ��� ����� (��� ����� - ��, ��� ��������� ������ �� ������ ������� ����� /)
                        FileName = FullFileName.substring(FullFileName.lastIndexOf('/') + 1);
                        // �������� ���������� ��� �����
                        if ((FileName == null) || (FileName.trim().equals("")))
                            throw new Exception("Invalid FileName[" + FileName + "] parameter!");
                        // �������� ���������� ����� (��, ��� ��������� ������ �� ������ ������� ����� .)
                        String fileExt = FileName.substring(FileName.lastIndexOf('.') + 1);
                        if ((fileExt == null) || (fileExt.trim().equals("")))
                            throw new Exception("Invalid fileExt[" + fileExt + "] parameter!");
                        // ������� � ������ "��������" ���������� ������������ ��� �����
                        memo.setAttachedFile(fileExt);
                        // ��������� ��� ����� ��� ���������� (�� ����� �������)
                        SavedFileName = "." + fileExt;
                    } // ����� ��������� ��������� ����� (if (sizeInBytes > 0))
                }
            } // end of while <- ����� ����� ��������� ������ �����
            logger.debug("Form dataprocessing finished. Processing memo.");

            // ���� �� ��������� ��������, � �� ������� ��, �� ������� ���������� ���� ���������� �� �������������.
            if ((newMemoID > 0) && (recipientsList != null))
                for (Object aObject : recipientsList) {
                    recipientDeptDTO recipientDept = (recipientDeptDTO) aObject;
                    recipientDept.setMemoID(newMemoID);
                }

            // ���������������� �������� �������� ��� ��������� ��������
            logger.debug("Trying create/update memo.");

            // ��� �������� �������� ���������� ������ � �������� ��������� � ��� (������)
            try {
                logger.debug("Selecting operation mode.");
                // ���� ���������� newMemoID ��� ��������� �������� - �� ��������� ������������ ��������
                if (newMemoID > 0) {
                    logger.debug("OPERATION MODE: *MEMO UPDATING*");
                    dH.getMemoDAO().update(dH, memo, recipientsList);
                    createnew = 0;
                } else {
                    logger.debug("OPERATION MODE: *NEW MEMO CREATING*");
                    newMemoID = dH.getMemoDAO().create(dH, memo, recipientsList);
                }
            }
            catch (Exception e) {
                // ���� �������� ��, �� ��� �������� ��� ��������� �������� �������� ������ - ���� ���������� �������� ������
                // ���� ��� ����, ����� ����, �������� ����, �� ��� �������� �� �����, � ��� �� ������.
                newMemoID = -1;
                throw new Exception("ERROR IN memoDAO.create()/update() FUNCTION [" + e.getMessage() + "]!");
            }
            logger.debug("Memo created/updated successfuly. MemoID = " + newMemoID);

            // ���� �������� �������� ����������� ������ - ��������� ���������� ����(���� �� ����)
            if ((newMemoID > 0) && (file != null)) {
                File TmpFile = new File(upload_dir + "/" + newMemoID + SavedFileName);
                file.write(TmpFile);
            }
            // ���� �� �������� �������� ����������� �������� - ������ ���� (���� �� ���)
            else if (file != null) file.delete();
        }// end of try statement

        // ���������� �� � ���������� ��������� �� ������ (���� ��� ����)
        catch (Exception e) {
            // ������� ������ � ���
            logger.error("ERROR occured while new memo processing: [" + e.getMessage() + "]! user: " + remoteUser);
            // �� �������� ���������� ��������� ������ ��������� ������
            errorsList = new ArrayList();
            errorsList.add(e.getMessage());
        }
        // ��������, �-�� ���� ��������� � ����� ������
        finally {
            // �������� �������� EDIT ���������, ��� �������� ���� ��������������� (���� �� �������)
            request.setAttribute("edit", request.getAttribute("edit"));
            // ����� ���������������� ������� ��������� ������� - ��������� � ����������. ���� ��������� ������ - ��� � �������.
            request.setAttribute("error", errorsList);
            // ������������(����� ����������) ������ �� ���������, ������� ���������� ��������� ���������� ��������
            // ���� ��� ����� �� ��������, �� �������� ������� ����, ������� �� � ���� ��������� � ���� ��,
            // �� ������� �� ���� ���������, � ���� ��� ������ ����������� ����
            RequestDispatcher dispatcher;
            if (createnew == 1 && memo.getParentID() > 0 && memo.getExecutorUserID() > 0) {
                dispatcher = getServletContext().getRequestDispatcher("/editor/controller?action=answerAppointResult&memoID=" + memo.getParentID()
                        + "&recipientUserID=" + memo.getExecutorUserID() + "&answer=1&newMemoID=" + newMemoID);
            } else {
                dispatcher = getServletContext().getRequestDispatcher("/editor/controller?action=saveMemoRes&memoID=" + newMemoID);
            }

            //RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/controller?action=viewMemo&memoID=" + newMemoID);
            dispatcher.forward(request, response);
        }
        logger.debug("LEAVING newMemoProcessing servlet.");
    } // end of doPost method

}