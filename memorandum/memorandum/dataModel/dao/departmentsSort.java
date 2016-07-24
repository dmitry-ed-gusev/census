package memorandum.dataModel.dao;

import memorandum.dataModel.dto.recipientDeptDTO;

import java.util.Comparator;

public class departmentsSort implements Comparator {


    private String typeSort;

    public departmentsSort(String typeSort){
        this.typeSort =  typeSort;
    }

     public int compare(Object user1, Object user2) {

         if(typeSort.equals("recipientDeptId")){
             recipientDeptDTO o1 = (recipientDeptDTO) user1;
             recipientDeptDTO o2 = (recipientDeptDTO) user2;
             return o1.getRecipientDeptCode().compareTo(o2.getRecipientDeptCode());
         }

         return 0;
     }

}
