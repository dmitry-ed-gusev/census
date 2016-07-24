package memorandum.dataModel.dto;

import jpersonnel.dto.SimpleEmployeeDTO;

import java.util.List;

/** ������ - ��������� �������. */
public class memoDTO
 {
  // ���������� ������������� ��������� �������.
  // DEFAULT: -1
  private int    id;
  // ����� ��������� �������.
  // DEFAULT: <none>
  private int    memoNumber;
  // ������������� ������������ �������, �� ������� ������ ������� �������� �������
  // DEFAULT: 0
  private int    parentID;
  // ���� ��������/�������������� �������.
  // DEFAULT: ������� ����/����� - ��������������� �������� ��.
  private String timeStamp;
  // ���� ��������� �������
  // DEFAULT: <none>
  private String subject;
  // ����������� �������. ���� �������� ������������� �� �� "�����".
  // ��� �������� ������� � ���� ��������� ������������� ����������������� ���������,
  // ���������� (�� ��� ������) �� �� "�����". � ����������, ��� �������������� �������,
  // �������� � ������ ���� ����� ���� ��������.
  // DEFAULT: <none>
  private int    executorUserID;
  //������������� ������� ������ � ������� ������ ������������
  private int    execWorkgroupId;
  // ���������� � �������.
  // DEFAULT: null
  private String note;
  // �����-�����������. ���� �������� ������������� ������ ��� (�� �� "�����"), �
  // ������� �������� ������������-��������� �� ������ �������� �� ��������.
  // � ����������, ��� �������������� �������, �������� ���� ������ �������� �
  // ������������ � ����������� ���� creatorID.
  // DEFAULT: <none>
  private int    executorDeptID;
  // ������������� ����.
  // DEFAULT: null
  private String attachedFile;
  // ���� (����) ���������� �������� �������� ������������ (� ������� ����������� �������� ���� ���� �� ����)
  private String realizedDate;
  // �������/������� ������� ��
  // DEFAULT: 0 (�� �������)
  private int    deleted;
  // ������������, ��������� ������� ��� ��������� ������������, ���������� �������.
  // ���� �������� ������������� ������������ �� �� "�����". ��� �������� ������� ��������
  // � ������ ���� ������������� �������� � ���� executorUserID.
  // DEFAULT: <none>
  private int    updateUserID;
  // ���� �������� �������� (���� ��� ������ ���� ����������)
  // DEFAULT: null
  private String sendDate;
  // ������������ ������� ������ �������� ��������
  private int    sendUserId;
  // ��� �������� �� ��� �������� ������
  private int    year;

  // --- ����, �� ������������ � ������� memos ---
  // ����� �������. ����� ��������� � ������ ������� memos_text (��� �� �� "��������"),
  // ������� ������� � �������� memos �� �������������� ��������. ����� ������� ����������
  // ��� ���������� ������������������ �������.
  // DEFAULT: <none>
  private String text;
  // ������� �.�. ����������� ��������. ������ ���� ����������� �� ������� personnel � �� "�����".
  private String executorShortName;
  // ������� �.�. ����������� ��������. ������ ���� ����������� �� ������� personnel � �� "�����".
  private String sendShortName;
  // ����������� ��� ������-�����������. ������ ���� ����������� �� ������� departments � �� "�����".
  private String executorDeptCode;
  // ��������� ������ ����������� ��������. ������ ���� ����������� �� ������� personnel � �� "�����".
  private SimpleEmployeeDTO chiefdeptexecutor;
  // ������ �������� ���� recipientDTO - ������-���������� ��������
  private List   recipientsDepts;
  // ������ �������� ���� memoDTO - ������ �������� ��������, �������� ������� �� �������
  private List   memoChild;
  // ������ �������� ���� recipientDTO - ������������-���������� ��������
  private List   recipientsUsers;
  // ����������� �������� - ���������� �������� ��� ���: 0-���, 1-��
  private int   overdue;

  public memoDTO()
   {
    this.id                   = -1;
    this.memoNumber           = 0;
    this.parentID             = 0;
    this.timeStamp            = null;
    this.subject              = null;
    this.executorUserID       = 0;
    this.execWorkgroupId      = 0;
    this.note                 = null;
    this.text                 = null;
    this.executorDeptID       = 0;
    this.attachedFile         = null;
    this.deleted              = 0;
    this.updateUserID         = 0;
    this.sendDate             = null;
    this.sendUserId           = 0;
    this.executorShortName    = null;
    this.sendShortName        = null;
    this.executorDeptCode     = null;
    this.recipientsDepts      = null;
    this.recipientsUsers      = null;
    this.realizedDate         = null;
    this.chiefdeptexecutor    = null;
    this.overdue              = 0;
    this.memoChild            = null;
    this.year                 = 0;
   }

  public int getId() {
   return id;
  }

  public void setId(int id) {
   this.id = id;
  }

  public int getMemoNumber() {
   return memoNumber;
  }

  public void setMemoNumber(int memoNumber) {
   this.memoNumber = memoNumber;
  }

  public int getParentID() {
   return parentID;
  }

  public void setParentID(int parentID) {
   this.parentID = parentID;
  }

  public String getUpdTimestamp() {
   return timeStamp;
  }

  public void setTimeStamp(String timeStamp) {
   this.timeStamp = timeStamp;
  }

  public String getSubject() {
   return subject;
  }

  public void setSubject(String subject) {
   this.subject = subject;
  }

  public int getExecutorUserID() {
   return executorUserID;
  }

  public void setExecutorUserID(int executorUserID) {
   this.executorUserID = executorUserID;
  }

  public String getNote() {
   return note;
  }

  public void setNote(String note) {
   this.note = note;
  }

  public int getExecutorDeptID() {
   return executorDeptID;
  }

  public void setExecutorDeptID(int executorDeptID) {
   this.executorDeptID = executorDeptID;
  }

  public String getAttachedFile() {
   return attachedFile;
  }

  public void setAttachedFile(String attachedFile) {
   this.attachedFile = attachedFile;
  }

  public int getDeleted() {
   return deleted;
  }

  public void setDeleted(int deleted) {
   this.deleted = deleted;
  }

  public int getUpdateUserID() {
   return updateUserID;
  }

  public void setUpdateUserID(int updateUserID) {
   this.updateUserID = updateUserID;
  }

  public String getSendDate() {
   return sendDate;
  }

  public void setSendDate(String sendDate) {
   this.sendDate = sendDate;
  }

  public int getSendUserId() {
     return sendUserId;
  }

  public void setSendUserId(int sendUserId) {
     this.sendUserId = sendUserId;
  }

  public String getText()
  {
    if (text != null) return text.trim(); 
    else              return text;
  }

  public void setText(String text) {
   this.text = text;
  }

  public String getExecutorShortName() {
   return executorShortName;
  }

  public void setExecutorShortName(String executorShortName) {
   this.executorShortName = executorShortName;
  }

  public String getSendShortName() {
   return sendShortName;
  }

  public void setSendShortName(String sendShortName) {
    this.sendShortName = sendShortName;
  }

     public String getExecutorDeptCode() {
   return executorDeptCode;
  }

  public void setExecutorDeptCode(String executorDeptCode) {
   this.executorDeptCode = executorDeptCode;
  }

  public List getRecipientsDepts() {
   return recipientsDepts;
  }

  public void setRecipientsDepts(List recipientsDepts) {
   this.recipientsDepts = recipientsDepts;
  }

  public List getRecipientsUsers() {
   return recipientsUsers;
  }

  public void setRecipientsUsers(List recipientsUsers) {
   this.recipientsUsers = recipientsUsers;
  }

  public String getRealizedDate() {
   return realizedDate;
  }

  public void setRealizedDate(String realizedDate) {
   this.realizedDate = realizedDate;
  }

     public SimpleEmployeeDTO getChiefdeptexecutor() {
         return chiefdeptexecutor;
     }

     public void setChiefdeptexecutor(SimpleEmployeeDTO chiefdeptexecutor) {
         this.chiefdeptexecutor = chiefdeptexecutor;
     }

     public int getOverdue() {
   return overdue;
  }

  public void setOverdue(int overdue) {
   this.overdue = overdue;
  }


  public List getMemoChild() {
     return memoChild;
  }

  public void setMemoChild(List memoChild) {
     this.memoChild = memoChild;
  }

     public int getYear() {
         return year;
     }

     public void setYear(int year) {
         this.year = year;
     }

     public int getExecWorkgroupId() {
         return execWorkgroupId;
     }

     public void setExecWorkgroupId(int execWorkgroupId) {
         this.execWorkgroupId = execWorkgroupId;
     }
 }