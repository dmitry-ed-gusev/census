package memorandum.controllers;

import jpersonnel.dto.SimpleEmployeeDTO;
import memorandum.Defaults;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.*;

/**
 * ����� ��������� ���������� ���������� "��������� �������", ������� ��������� ���� �������� - ����� ��������
 * �����������.
 * @author Gusev Dmitry
 * @version 1.0
*/

public class welcomeController extends AbstractController
{
 // ��� ��������: ��������� ����� ����� � ������� "��������"
 private static final String WELCOME_ACTION             = "welcome";
 // ��������� �������� ��� ����� � ������� "��������"
 private static final String WELCOME_PAGE               = "/welcome.jsp";
 // �������� � ����������� �� ������
 private static final String ERROR_PAGE                 = "/error.jsp";

 /**
  * ���������������� ��������� ������������ �������.
  * @param request HttpServletRequest http-������
  * @param response HttpServletResponse http-�����
  * @throws ServletException ������ � ������ ��������
  * @throws IOException ������ �����/������
 */
 protected void processRequest(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
   logger.debug("ENTERING welcomeController -> processRequest().");
   
   // ��������� ��������
   String destPage = ERROR_PAGE;
    
   // ��������� ����� ����� � �������
   if (WELCOME_ACTION.equals(ACTION)) {
       destPage = WELCOME_PAGE;
   }

   logger.debug("ACTION: " + ACTION); logger.debug("DEST PAGE: " + destPage);

   // �������� ��������� ������� ������� � �������������� ������
   RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(destPage);
   // ��������������� ���������������
   dispatcher.forward(request, response);

   logger.debug("LEAVING welcomeController -> processRequest().");
  }

}
