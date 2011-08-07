<#assign menu=JspTaglibs["/WEB-INF/struts-menu.tld"]>
<!doctype html>
<html lang="en" class="no-js">
<head>
  <meta charset="utf-8">

  <!-- Using HTML5 Boilerplate v0.9.1 [Documented] http://html5boilerplate.com/ -->

  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

  <title>CoCamp - ${pagetitle}</title>
  <meta name="description" content="">
  <meta name="author" content="">
  <meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0;">

  <!-- Place favicon.ico and apple-touch-icon.png in the root of your domain and delete these references -->
  <link rel="shortcut icon" href="/graphics/favicon.png">

  <!-- CSS : implied media="all" -->
  <link rel="stylesheet" href="/css/style.css?v=1">
  <link rel="stylesheet" href="/css/custom-theme/jquery-ui-1.8.6.custom.css" />	
  <link rel="stylesheet" href="/css/custom-theme/ui.jqgrid.css" />
  <!-- For the less-enabled mobile browsers like Opera Mini -->
  <link rel="stylesheet" media="handheld" href="/css/handheld.css?v=1">
 
  <!-- All JavaScript at the bottom, except for Modernizr which enables HTML5 elements & feature detects -->
  <script src="/js/modernizr-1.5.min.js"></script>

  <!-- Grab Google CDN's jQuery. fall back to local if necessary -->
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
  <script>!window.jQuery && document.write('<script src="/js/jquery-1.4.2.min.js"><\/script>')</script>
  <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.6/jquery-ui.min.js"></script>
  <script>!window.jQuery && document.write('<script src="/js/jquery-ui-1.8.6.min.js"><\/script>')</script>

  <script src="/js/grid.locale-en.js"></script>
  <script src="/js/jquery.jqGrid.min.js"></script>
</head>

<!-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ -->

<!--[if lt IE 7 ]> <body class="ie6"> <![endif]-->
<!--[if IE 7 ]>    <body class="ie7"> <![endif]-->
<!--[if IE 8 ]>    <body class="ie8"> <![endif]-->
<!--[if IE 9 ]>    <body class="ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <body> <!--<![endif]-->

<div id="wrap">
        <header>
                <div id="headerInner">
            		<div id="nav">
            		  <ul>
            			<a href="/" title="CoCamp Home"><li id="title">CoCamp Bookings</li></a>
            			<div class="printhide">
            			<#if (Session?exists && ! Session.USER?? ) || !Session?exists >
            				<li class="right"><@s.url id="signupURL" namespace="/signup" action="signup"/><@s.a href="${signupURL}">Sign Up</@s.a></li>
            				<li class="right"><@s.url id="loginURL" namespace="/" action="login"/><@s.a href="${loginURL}">Log In</@s.a></li>
            			<#else>
            				<li class="right"><@s.url id="logoutURL" namespace="/" action="logout"/><@s.a href="${logoutURL}">Log Out</@s.a></li>
            			</#if>
            			</div>
                      </ul>
            		</div>
	                <div id="menu">              
	                <#if Session?exists && Session.USER??>
		                <@menu.useMenuDisplayer name="CSSListMenu" id="primary-nav">
						  <@menu.displayMenu name="ManageUnit"/>	
						  <#if Session.USER.accessLevel.canChangeUnit>				  
						  <@menu.displayMenu name="ManageOrg"/>
						  </#if>
						  <#if Session.USER.accessLevel.isSuperUser>
						  	<@menu.displayMenu name="ManageAll"/>
						  </#if>
						  <@menu.displayMenu name="MyAccount"/>					  
						</@menu.useMenuDisplayer>
					</#if>
					</div>
				</div>
        </header>
  
        <div id="main">
            <div id="contentWrap">
	            <#include "/templates/userbar.ftl"/>
	       
                    <div id="content">

