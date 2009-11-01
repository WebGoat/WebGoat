<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
    
<% String coupon = request.getParameter("coupon");

if (coupon.equalsIgnoreCase("PLATINUM")){
	out.print(".25");
}
else if (coupon.equalsIgnoreCase("GOLD")){
	out.print(".5");
}
else if (coupon.equalsIgnoreCase("SILVER")){
	out.print(".75");
}
else if (coupon.equalsIgnoreCase("BRONZE")){
	out.print(".8");
}
else if (coupon.equalsIgnoreCase("PRESSONE")){
	out.print(".9");
}
else if (coupon.equalsIgnoreCase("PRESSTWO")){
	out.print(".95");
}



%>    
    
