package memorandum.dataModel.dto;

/**
 * ������ ����� ��������� ������ �����-���������� - ���� ������ �� ������� recipientsDepts.
 * ��� ������ � ������� ��������� ������������ ����� recipientDeptDAO.
 */
public class recipientDeptDTO
 {
  /** ������������� ������ ���������� �������� */
  private int    id;
  /** ������������� ������������� ������ ����������� ������ �������� � �������������� �� ������� */
  private int    parentID;
  /** ������������� �������� */
  private int    memoID;
  /** ������������� ������ ���������� �������� �� �� "�����" */
  private int    recipientDeptID;
  /**
   * ������� ���������� ������ �������� (0 - �� ���������, 1 - ���������).
   * ����� ����� ������ ��� ���������� ���� realizedDate.
  */
  private int    realized;
  /** ������ ���������� �������� - �������/������. */
  private int    deleted;
  /** �����-���� ��������/��������� ������ ������. */
  private String timeStamp;
  /** ������������� ������������ (�� �� "�����"), ������� ��������� ������ ������ ������ ��� ������ ��. */
  private int    updateUserID;

  /** ����������� ��� ������-���������� ��������. (������ ���� ���������� - ��� ��� � �������) */
  private String recipientDeptCode;
  /** ������������ ������-���������� ��������. (������ ���� ���������� - ��� ��� � �������) */
  private String recipientDeptName;
  /** ��������� ������-���������� ��������. (������ ���� ���������� - ��� ��� � �������) */
  private String recipientChief;

  public recipientDeptDTO()
   {
    this.id                = -1;
    this.parentID          = 0;
    this.memoID            = 0;
    this.recipientDeptID   = 0;
    this.realized          = 0;
    this.deleted           = 0;
    this.timeStamp         = null;
    this.updateUserID      = 0;
    this.recipientDeptCode = null;
    this.recipientDeptName = null;
    this.recipientChief    = null;
   }

  public int getRecipientDeptID() {
   return recipientDeptID;
  }

  public void setRecipientDeptID(int recipientDeptID) {
   this.recipientDeptID = recipientDeptID;
  }

  public String getRecipientDeptCode() {
   return recipientDeptCode;
  }

  public void setRecipientDeptCode(String recipientDeptCode) {
   this.recipientDeptCode = recipientDeptCode;
  }

  public String getRecipientDeptName() {
   return recipientDeptName;
  }

  public void setRecipientDeptName(String recipientDeptName) {
   this.recipientDeptName = recipientDeptName;
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

  public int getRealized() {
   return realized;
  }

  public void setRealized(int realized) {
   this.realized = realized;
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

  public String getRecipientChief() {
   return recipientChief;
  }

  public void setRecipientChief(String recipientChief) {
   this.recipientChief = recipientChief;
  }
 }