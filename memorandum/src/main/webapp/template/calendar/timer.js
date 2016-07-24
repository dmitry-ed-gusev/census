var cmin, csec;

cmin=9;
csec=59;

function DownRepeat() 
{

   csec--;

   if(csec==-1) {csec=59; cmin--;}

   window.status = "timeout - " + ((cmin < 10)?"0"+cmin:cmin)+':'+((csec < 10)?"0"+csec:csec);

   if((cmin==0) && (csec==0)) 
   {alert("Время создания/редактирования служебной записки истекло!");}
   else
   {setTimeout("DownRepeat()",1000);}

}		

