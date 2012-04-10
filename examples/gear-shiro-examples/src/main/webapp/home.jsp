<%@ include file="include.jsp" %>

<html>
	<head>
		<link type="text/css" rel="stylesheet" href="<c:url value="/style.css"/>" />
		<title>Apache Shiro QuickStart</title>
	</head>
	<body>
		
		<h1>Apache Shiro QuickStart</h1>
		
		<p>Hi <shiro:guest>Guest</shiro:guest><shiro:user><shiro:principal/></shiro:user>!
		(<shiro:user><a href="<c:url value="/logout"/>">Log out</a></shiro:user> 
		<shiro:guest><a href="<c:url value="/login.jsp"/>">Log in</a> (sample accounts provided)</shiro:guest>)
		</p>
		
		<p>Welcome to the Apache Shiro Quickstart sample application.
    	This page represents the home page of any web application.</p>
		
		<shiro:user><p>Visit your <a href="<c:url value="/account"/>">account page</a>.</p></shiro:user>
		<shiro:guest><p>If you want to access the user-only <a href="<c:url value="/account"/>">account page</a>,
		you will need to log-in first.</p></shiro:guest>
		
		<h2>Roles you have</h2>
		
		<p>
			<shiro:hasRole name="admin">admin<br /></shiro:hasRole>
			<shiro:hasRole name="president">president<br /></shiro:hasRole>
			<shiro:hasRole name="darklord">darklord<br /></shiro:hasRole>
			<shiro:hasRole name="goodguy">goodguy<br /></shiro:hasRole>
			<shiro:hasRole name="schwartz">schwartz<br /></shiro:hasRole>
		</p>
		
		<h3>Roles you DON'T have</h3>
		
		<p>
			<shiro:lacksRole name="admin">admin<br /></shiro:lacksRole>
			<shiro:lacksRole name="president">president<br /></shiro:lacksRole>
			<shiro:lacksRole name="darklord">darklord<br /></shiro:lacksRole>
			<shiro:lacksRole name="goodguy">goodguy<br /></shiro:lacksRole>
			<shiro:lacksRole name="schwartz">schwartz<br /></shiro:lacksRole>
		</p>
		
	</body>
</html>
