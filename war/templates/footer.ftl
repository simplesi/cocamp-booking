            </div> <!--! end of #content -->
        </div> <!--! end of #contentWrap -->
    </div>  <!--! end of #main -->
</div>  <!--! end of #wrap -->

<footer>
    <div id="footerInner">
        <span>&copy; CoCamp</span> <a href="/privacy">Privacy</a> | <a href="/terms">Terms</a>
    </div>
</footer>
        

  <!-- Javascript at the bottom for fast page loading -->
	
  <script src="/js/plugins.js?v=1"></script>
  <script src="/js/script.js?v=1"></script>

  <!--[if lt IE 7 ]>
    <script src="js/dd_belatedpng.js?v=1"></script>
  <![endif]-->

  <!-- yui profiler and profileviewer - remove for production -->
  <script src="/js/profiling/yahoo-profiling.min.js?v=1"></script>
  <script src="/js/profiling/config.js?v=1"></script>
  <!-- end profiling code -->

  <!-- asynchronous google analytics: mathiasbynens.be/notes/async-analytics-snippet change the UA-XXXXX-X to be your site's ID -->
  <script>
   var _gaq = [['_setAccount', 'UA-XXXXX-X'], ['_trackPageview']];
   (function(d, t) {
    var g = d.createElement(t),
        s = d.getElementsByTagName(t)[0];
    g.async = true;
    g.src = '//www.google-analytics.com/ga.js';
    s.parentNode.insertBefore(g, s);
   })(document, 'script');
  </script>
  
  <!-- for the css menu, small IE hack -->
  <script type="text/javascript">
    /*<![CDATA[*/
    function IEHoverPseudo() {

        var navItems = document.getElementById("primary-nav").getElementsByTagName("li");

        for (var i=0; i<navItems.length; i++) {
            if(navItems[i].className == "menubar") {
                navItems[i].onmouseover=function() { this.className += " over"; }
                navItems[i].onmouseout=function() { this.className = "menubar"; }
            }
        }
    }
    window.onload = IEHoverPseudo;
    /*]]>*/
  </script>
  
  
</body>
</html>