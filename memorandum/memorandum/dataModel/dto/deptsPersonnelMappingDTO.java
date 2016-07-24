package memorandum.dataModel.dto;

/**
 * ����� ��������� ���� ������ �� ������� �������������(mapping) ����������� ��������������.
 * ��� ���������� ����������� � ������� �������� ������������ ����� deptsPersonnelMappingDAO.
 * @author Gusev Dmitry
 * @version 1.0
 */
public class deptsPersonnelMappingDTO
 {
  /***/
  private int id;
  /** ������������� ������������� �� �� Personnel*/
  private int deptID;
  /** ������������� ��������� �� �� Personnel */
  private int personnelID;
  /** ������� ���������� �������������*/
  private int isChief;     
  /** ��� ������������� �� �� "�����"*/
  private String deptCode;
  /** ������ �.�. ���� �� �� "�����"*/
  private String shortName;

  public deptsPersonnelMappingDTO(int id, int deptID, int personnelID, int isChief, String deptCode, String shortName)
   {
    this.id            = id;
    this.deptID        = deptID;
    this.personnelID   = personnelID;
    this.isChief       = isChief;
    this.deptCode      = deptCode;
    this.shortName     = shortName;
   }

  public deptsPersonnelMappingDTO()
   {
    this.id            = -1;
    this.deptID        = 0;
    this.personnelID   = 0;
    this.isChief       = 0;
    this.deptCode      = "";
    this.shortName     = "";
   }

  public int getId()                          {return id;}
  public void setId(int id)                   {this.id = id;}

  public int getDeptID()                      {return deptID;}
  public void setDeptID(int deptID)           {this.deptID = deptID;}

  public int getPersonnelID()                 {return personnelID;}
  public void setPersonnelID(int personnelID) {this.personnelID = personnelID;}

  public int getIsChief()                     {return isChief;}
  public void setIsChief(int isChief)         {this.isChief = isChief;}

  public String getDeptCode()                 {return deptCode;}
  public void setDeptCode(String deptCode)    {this.deptCode = deptCode;}

  public String getShortName()                {return shortName;}
  public void setShortName(String shortName)  {this.shortName = shortName;}


 }