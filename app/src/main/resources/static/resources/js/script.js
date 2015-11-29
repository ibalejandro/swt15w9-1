<!--
// By Lennart Schmidt 29.11.2015


var id1 = "log";
var id2 = "reg";

/*
*/
function toggle(id){
	if(!both())
	{
		return true;
	}
	if(id == id1)
	{
		setVisibility(id1,'none');
		setVisibility(id2,'block');
	}
	else if(id == id2)
	{
		setVisibility(id1,'block');
		setVisibility(id2,'none');
	}
	return false;
}

function both(){
	try
	{
		if(document.getElementById(id1) && document.getElementById(id2))
		{
			return true;	
		}
		else
		{
			return false;
		}
	}
	catch(e)
	{
		return false;
	}
}

/*
*   Funktion zur Fehler Behandlung. Falls eine nicht gültige ID auftritt wird das
*   Script nicht gestoppt sondern läuft normal weiter. Des weiteren gibt es 
*   eine Fehlerausgabe woran man einen Fehler erkennen kann.
*/
function setVisibility(id, typ){
    try
	{
        document.getElementById(id).style.display=typ;
    } 
    catch (e) 
	{
        console.log("Error function setVisibility(id,typ) with: |" + id + "| |" + typ + "|\t" + e.message);
    }
}
-->