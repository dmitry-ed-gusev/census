package memorandum.dataModel.dto;

/**
 * ������ ����� ��������� ������ ������������, �������� ��������� �������� (���� ������ �� �������
 * recipientsUsers). ��� ���������� �������� � ������� ��������� (����������� �������� �� �������)
 * ������������ ����� recipientUserDAO.
 * @author Gusev Dmitry
 * @version 1.0
 */
public class recipientUserDTO
 {
  /** ������������� ���������� �������� (������ � �������). */
  private int    id;
  /** ������������� ������������ ����������� ������ �������� � �������������� �� �������. */
  private int    parentID;
  /** ������������� ��������. */
  private int    memoID;
  /** ������������� ������ ���������� �������� �� �� "�����". */
  private int    recipientDeptID;
  /** ������������� ������������ (�� �� "�����"), �������� ��������� ��������. */
  private int    recipientUserID;
  /** ����������� ��� ������������, �������� ��������� ��������. */
  private String subject;
  /** ����� ������������, ������������ ��������. */
  private String answer;
  /**
   * ������� ���������� ������ �������� (0 - �� ���������, 1 - ���������). ����� ����� ������
   * ��� ���������� ���� realizedDate.
  */
  private int    realized;
  /** ���� ������ �� ������ �������� (����). */
  private String realizedDate;
  /** ������ ���������� ��������(������ � �������)- �������/������. */
  private int    deleted;
  /** �����-���� ��������/��������� ������ ������. */
  private String timeStamp;
  /** ������������� ������������ (�� �� "�����"), ������� ��������� ������ ������ ������ ��� ������ ��. */
  private int    updateUserID;
  /** ������������� ������������ (�� �� "�����"), ������� ������� ������ ��*/
  private int    appointedUserID;
  /** ��� ������������ (�� �� "�����"), ������� ������� ������ ��*/
  private String appointedUserName;
  /** ������� �.�. ������������, �������� �������� ������ ��������. */
  private String recipientShortName;
  /** ������� �������� E-mail ���������� ������������� ��� ������ �� ���������. */
  private int sendemail;

  public recipientUserDTO()
   {
    this.id                 = -1;
    this.parentID           = 0;
    this.memoID             = 0;
    this.recipientDeptID    = 0;
    this.recipientUserID    = 0;
    this.subject            = null;
    this.answer             = null;
    this.realized           = 0;
    this.realizedDate       = null;
    this.deleted            = 0;
    this.timeStamp          = null;
    this.updateUserID       = 0;
    this.appointedUserID    = 0;
    this.appointedUserName  = null;
    this.recipientShortName = null;
    this.sendemail          = 0;
   }

  public int getId() {
   return id;
  }

  public void setId(int id) {
   this.id = id;
  }

  public int getParentID() {
   return parentID;
  }

  public void setParentID(int parentID) {
   this.parentID = parentID;
  }

  public int getMemoID() {
   return memoID;
  }

  public void setMemoID(int memoID) {
   this.memoID = memoID;
  }

  public int getRecipientDeptID() {
   return recipientDeptID;
  }

  public void setRecipientDeptID(int recipientDeptID) {
   this.recipientDeptID = recipientDeptID;
  }

  public int getRecipientUserID() {
   return recipientUserID;
  }

  public void setRecipientUserID(int recipientUserID) {
   this.recipientUserID = recipientUserID;
  }

  public int getRealized() {
   return realized;
  }

  public void setRealized(int realized) {
   this.realized = realized;
  }

  public String getRealizedDate() {
   return realizedDate;
  }

  public void setRealizedDate(String realizedDate) {
   this.realizedDate = realizedDate;
  }

  public int getDeleted() {
   return deleted;
  }

  public void setDeleted(int deleted) {
   this.deleted = deleted;
  }

  public String getUpdTimestamp() {
   return timeStamp;
  }

  public void setTimeStamp(String timeStamp) {
   this.timeStamp = timeStamp;
  }

  public int getUpdateUserID() {
   return updateUserID;
  }

  public void setUpdateUserID(int updateUserID) {
   this.updateUserID = updateUserID;
  }

  public int getAppointedUserID() {
   return appointedUserID;
  }

  public void setAppointedUserID(int appointedUserID) {
   this.appointedUserID = appointedUserID;
  }

  public String getAppointedUserName() {
   return appointedUserName;
  }

  public void setAppointedUserName(String appointedUserName) {
   this.appointedUserName = appointedUserName;
  }
 
  public String getSubject() {
   return subject;
  }

  public void setSubject(String subject) {
   this.subject = subject;
  }

  public String getAnswer() {
   return answer;
  }

  public void setAnswer(String answer) {
   this.answer = answer;
  }

  public String getRecipientShortName() {
   return recipientShortName;
  }

  public void setRecipientShortName(String recipientShortName) {
   this.recipientShortName = recipientShortName;
  }

  public int getSendemail() {
   return sendemail;
  }

  public void setSendemail(int sendemail) {
   this.sendemail = sendemail;
  }
 }