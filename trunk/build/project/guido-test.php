<?php
  header("Cache-Control: no-cache, must-revalidate"); // HTTP/1.1
  header("Expires: Thu, 01 Jan 1970 00:00:01 GMT"); // Former times
  header("Content-type: application/x-java-jnlp-file");
  echo '<?xml version="1.0" encoding="utf-8"?>';
?>
<!-- JNLP File for JHAVE -->
<jnlp spec="1.0+"
    codebase="WEBSTART_CODEBASE">
    <information>
        <title>JHAVE Client</title>
        <vendor>JHAVE</vendor>
        <!--homepage href="docs/help.html"/-->
        <description>JHAVE Client</description>
        <description kind="short">JHAVE Client</description>
        <!-- icon href="images/swingset2.jpg"/-->
        <!-- icon kind="splash" href="images/splash.gif"/ -->
        <offline-allowed/>
        </information>
    <security>
        <all-permissions/>
    </security>
    <resources>
        <j2se version="1.4+"/>
        <jar href="client.jar"/>
        <jar href="core.jar"/>
        <jar href="gaigs.jar"/>
        <jar href="CatalogManager.jar"/>
        <jar href="resolver-1.0.jar"/>
       <jar href="Animal-2.3.7.jar"/>
        <jar href="jsapi.jar"/>
        <jar href="freetts.jar"/>
        <jar href="en_us.jar"/>
        <jar href="cmutimelex.jar"/>
        <jar href="cmu_us_kal.jar"/>
        <jar href="cmulex.jar"/>
        <jar href="cmudict04.jar"/>
        <jar href="cmu_time_awb.jar"/>
        <property name="freetts.voices"

value="com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory"/>
        <!-- Add more extensions here -->
    </resources>
<!--            <application-desc main-class="jhave.client.Client"/> -->
               <application-desc main-class="jhave.client.Client">
               <argument>-debug</argument>
               <argument>-c</argument>
<?php
  echo "\t\t<argument>" . $_REQUEST["algoName"] . "</argument>";
?>
               <argument>-r</argument>
               </application-desc>
</jnlp>

