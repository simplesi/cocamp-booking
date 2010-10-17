<!doctype html>
<html lang="en" class="no-js">
<head>
  <meta charset="utf-8">

  <!-- Using HTML5 Boilerplate v0.9.1 [Documented] http://html5boilerplate.com/ -->


  <!-- www.phpied.com/conditional-comments-block-downloads/ -->
  <!--[if IE]><![endif]-->

  <!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame 
       Remove this if you use the .htaccess -->
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

  <title>CoCamp - ${pagetitle}</title>
  <meta name="description" content="">
  <meta name="author" content="">

  <!--  Mobile Viewport Fix
        j.mp/mobileviewport & davidbcalhoun.com/2010/viewport-metatag 
  device-width : Occupy full width of the screen in its current orientation
  initial-scale = 1.0 retains dimensions instead of zooming out if page height > device height
  maximum-scale = 1.0 retains dimensions instead of zooming in if page width < device width
  -->
  <meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0;">

  <!-- Place favicon.ico and apple-touch-icon.png in the root of your domain and delete these references -->
  <link rel="shortcut icon" href="/graphics/favicon.png">

  <!-- CSS : implied media="all" -->
  <link rel="stylesheet" href="/css/style.css?v=1">

  <!-- For the less-enabled mobile browsers like Opera Mini -->
  <link rel="stylesheet" media="handheld" href="/css/handheld.css?v=1">

 
  <!-- All JavaScript at the bottom, except for Modernizr which enables HTML5 elements & feature detects -->
  <script src="/js/modernizr-1.5.min.js"></script>

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
            			<li id="title"><a href="/" title="CoCamp Home">CoCamp Bookings</a></li>
            			<li class="right"><@s.url id="signupURL" action="signup/"/><@s.a href="${signupURL}">Sign Up</@s.a></li>
            			<li class="right"><@s.url id="loginURL" action="login/"/><@s.a href="${loginURL}">Log In</@s.a></li>
                      </ul>
            		</div>
                </div>
        </header>
  
        <div id="main">
            <#include "/templates/userbar.ftl"/>
                <div id="contentWrap">
                    <div id="content">

