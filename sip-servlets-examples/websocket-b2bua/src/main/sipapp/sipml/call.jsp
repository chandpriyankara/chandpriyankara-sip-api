<%@ page import="org.mobicents.servlet.sip.SipConnector" %>
<!DOCTYPE html>
<!--
* Copyright (C) 2012 Doubango Telecom <http://www.doubango.org>
* License: GPLv3
* This file is part of Open Source sipML5 solution <http://www.sipml5.org>
-->
<html>
<!-- head -->
<head>
    <meta charset="utf-8" />
    <title>sipML5 live demo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="Keywords" content="doubango, sipML5, VoIP, HTML5, WebRTC, RTCWeb, SIP, IMS, Video chat, VP8, live demo " />
    <meta name="Description" content="HTML5 SIP client using WebRTC framework" />
    <meta name="author" content="Doubango Telecom" />

    <!-- SIP API and utility functions -->
    <script src="src/tinySIP/src/tsip_api.js?svn=10" type="text/javascript"> </script>
    <!-- Styles -->
    <link href="./assets/css/bootstrap.css" rel="stylesheet" />
    <style type="text/css">
        body{
            padding-top: 80px;
            padding-bottom: 40px;
        }
        .full-screen{
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: hidden;
        }
        .normal-screen{
            position: relative;
        }
        .call-options {
          padding: 5px;
          background-color: #f0f0f0;
          border: 1px solid #eee;
          border: 1px solid rgba(0, 0, 0, 0.08);
          -webkit-border-radius: 4px;
          -moz-border-radius: 4px;
          border-radius: 4px;
          -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
          -moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
          box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
          -webkit-transition-property: opacity;
          -moz-transition-property: opacity; 
          -o-transition-property: opacity; 
          -webkit-transition-duration: 2s;
          -moz-transition-duration: 2s;
          -o-transition-duration: 2s;
        }
        .tab-video,
        .div-video{
            width: 100%; 
            height: 0px; 
            -webkit-transition-property: height;
            -moz-transition-property: height;
            -o-transition-property: height;
            -webkit-transition-duration: 2s;
            -moz-transition-duration: 2s;
            -o-transition-duration: 2s;
        }
        .label-align {
            display: block;
            padding-left: 15px;
            text-indent: -15px;
        }
        .input-align {
            width: 13px;
            height: 13px;
            padding: 0;
            margin:0;
            vertical-align: bottom;
            position: relative;
            top: -1px;
            *overflow: hidden;
        }

        
    </style>
    <link href="./assets/css/bootstrap-responsive.css" rel="stylesheet" />
    <!-- Le fav and touch icons -->
    <link rel="shortcut icon" href="./assets/ico/favicon.ico" />
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="./assets/ico/apple-touch-icon-114-precomposed.png" />
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="./assets/ico/apple-touch-icon-72-precomposed.png" />
    <link rel="apple-touch-icon-precomposed" href="./assets/ico/apple-touch-icon-57-precomposed.png" />
</head>
<!-- Javascript code -->
<script type="text/javascript">

    // to avoid caching
    //if (window.location.href.indexOf("svn=") == -1) {
    //    window.location.href += (window.location.href.indexOf("?") == -1 ? "?svn=10" : "&svn=10");
    //}

    var sTransferNumber;
    var oRingTone, oRingbackTone;
    var oSipStack, oSipSessionRegister, oSipSessionCall, oSipSessionTransferCall;
    var txtDisplayName, txtPrivateIdentity, txtPublicIdentity, txtPassword, txtRealm;
    var txtPhoneNumber;
    var btnCall, btnHangUp;
    var txtRegStatus, txtCallStatus;
    var btnRegister, btnUnRegister;
    var btnFullScreen, btnHoldResume, btnTransfer, btnKeyPad;
    var videoRemote, videoLocal;
    var divVideo, divCallOptions;
    var tdVideo;
    var bFullScreen = false;
    var oNotifICall;
    var oReadyStateTimer;
    var bDisableVideo = false;

    window.onload = function () {
        txtDisplayName = document.getElementById("txtDisplayName");
        txtPrivateIdentity = document.getElementById("txtPrivateIdentity");
        txtPublicIdentity = document.getElementById("txtPublicIdentity");
        txtPassword = document.getElementById("txtPassword");
        txtRealm = document.getElementById("txtRealm");

        txtPhoneNumber = document.getElementById("txtPhoneNumber");

        btnCall = document.getElementById("btnCall");
        btnHangUp = document.getElementById("btnHangUp");

        txtRegStatus = document.getElementById("txtRegStatus");
        txtCallStatus = document.getElementById("txtCallStatus");

        btnRegister = document.getElementById("btnRegister");
        btnUnRegister = document.getElementById("btnUnRegister");

        btnFullScreen = document.getElementById("btnFullScreen");
        btnHoldResume = document.getElementById("btnHoldResume");
        btnTransfer = document.getElementById("btnTransfer");
        btnKeyPad = document.getElementById("btnKeyPad");

        videoLocal = document.getElementById("video_local");
        videoRemote = document.getElementById("video_remote");

        divVideo = document.getElementById("divVideo");
        divCallOptions = document.getElementById("divCallOptions");
        //divVideo.style.height = '0px';

        tdVideo = document.getElementById("tdVideo");

        document.onkeyup = onKeyUp;
        document.body.onkeyup = onKeyUp;
        document.getElementById("divCallCtrl").onmousemove = onDivCallCtrlMouseMove;

        loadCredentials();
        loadCallOptions();

        oReadyStateTimer = setInterval(function () {
            if (document.readyState === "complete") {
                postInit();
                clearInterval(oReadyStateTimer);
            }
        },
        500);
    };

    function postInit() {
        // Init WebRtc engine
        tsk_utils_init_webrtc();

        // check webrtc4all version
        if (tsk_utils_have_webrtc4all()) {
            tsk_utils_log_info("WebRTC type = " + WebRtc4all_GetType() + " version = " + tsk_utils_webrtc4all_get_version());
            if (tsk_utils_webrtc4all_get_version() != "1.11.745") {
                if (confirm("Your WebRtc4all extension is outdated. A new version with critical bug fix is available. Do you want to install it?\nIMPORTANT: You must restart your browser after the installation.")) {
                    window.location = 'http://code.google.com/p/webrtc4all/downloads/list';
                    return;
                }
            }
        }

        // checks for WebRTC support
        if (!tsk_utils_have_webrtc()) {
            // is it chrome?
            if (navigator.userAgent.toLowerCase().indexOf("chrome") > -1) {
                    if (confirm("You're using an old Chrome version or WebRTC is not enabled.\nDo you want to see how to enable WebRTC?")) {
                        window.location = 'http://www.webrtc.org/running-the-demos';
                    }
                    else {
                        window.location = "index.html";
                    }
                    return;
            }
                
            // for now the plugins (WebRTC4all only works on Windows)
            if (navigator.appVersion.indexOf("Win") != -1) {
                // Internet explorer
                if (navigator.appName == 'Microsoft Internet Explorer') {
                    // Check for IE version 
                    var rv = -1;
                    var ua = navigator.userAgent;
                    var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
                    if (re.exec(ua) != null) {
                        rv = parseFloat(RegExp.$1);
                    }
                    if (rv < 9.0) {
                        if (confirm("You are using an old IE version. You need at least version 9. Would you like to update IE?")) {
                            window.location = 'http://windows.microsoft.com/en-us/internet-explorer/products/ie/home';
                        }
                        else {
                            window.location = "index.html";
                        }
                    }

                    // check for WebRTC4all extension
                    if (!tsk_utils_have_webrtc4all()) {
                        if (confirm("webrtc4all extension is not installed. Do you want to install it?\nIMPORTANT: You must restart your browser after the installation.")) {
                            window.location = 'http://code.google.com/p/webrtc4all/downloads/list';
                        }
                        else {
                            // Must do nothing: give the user the chance to accept the extension
                            // window.location = "index.html";
                        }
                    }
                    // break page loading ('window.location' won't stop JS execution)
                    if (!tsk_utils_have_webrtc4all()) {
                        return;
                    }
                }
                else if (navigator.userAgent) {
                    if (navigator.userAgent.toLowerCase().indexOf("safari") > -1 || navigator.userAgent.toLowerCase().indexOf("firefox") > -1 || navigator.userAgent.toLowerCase().indexOf("opera") > -1) {
                        if (confirm("Your browser don't support WebRTC.\nDo you want to install WebRTC4all extension to enjoy audio/video calls?\nIMPORTANT: You must restart your browser after the installation.")) {
                            window.location = 'http://code.google.com/p/webrtc4all/downloads/list';
                        }
                        else {
                            window.location = "index.html";
                        }
                        return;
                    }
                }
            }
            // OSX, Unix, Android, iOS...
            else {
                if (confirm('WebRTC not supported on your browser.\nDo you want to download a WebSocket-capable browser?')) {
                    window.location = 'https://www.google.com/intl/en/chrome/browser/';
                }
                else {
                    window.location = "index.html";
                }
                return;
            }
        }

        // checks for WebSocket support
        if (!tsk_utils_have_websocket() && !tsk_utils_have_webrtc4all()) {
            if (confirm('Your browser don\'t support WebSockets.\nDo you want to download a WebSocket-capable browser?')) {
                window.location = 'https://www.google.com/intl/en/chrome/browser/';
            }
            else {
                window.location = "index.html";
            }
            return;
        }

        // attachs video displays
        if (tsk_utils_have_webrtc4all()) {
            WebRtc4all_SetDisplays(document.getElementById("divVideoLocal"), document.getElementById("divVideoRemote"));
        }

        if (!tsk_utils_have_webrtc()) {
            if (confirm('Your browser don\'t support WebRTC.\naudio/video calls will be disabled.\nDo you want to download a WebRTC-capable browser?')) {
                window.location = 'https://www.google.com/intl/en/chrome/browser/';
            }
        }

        btnRegister.disabled = false;
        document.body.style.cursor = 'default';
    }


    function loadCallOptions() {
        if (window.localStorage) {
            var s_value;
            if ((s_value = window.localStorage.getItem('org.doubango.call.phone_number'))) txtPhoneNumber.value = s_value;
            bDisableVideo = (window.localStorage.getItem('org.doubango.expert.disable_video') == "true");

            txtCallStatus.innerHTML = '<i>Video ' + (bDisableVideo ? 'disabled' : 'enabled') + '</i>';
        }
    }

    function saveCallOptions() {
        if (window.localStorage) {
            window.localStorage.setItem('org.doubango.call.phone_number', txtPhoneNumber.value);
            window.localStorage.setItem('org.doubango.expert.disable_video', bDisableVideo ? "true" : "false");
        }
    }

    function loadCredentials() {
        if (window.localStorage) {
            // IE retuns 'null' if not defined
            var s_value;
            if ((s_value = window.localStorage.getItem('org.doubango.identity.display_name'))) txtDisplayName.value = s_value;
            if ((s_value = window.localStorage.getItem('org.doubango.identity.impi'))) txtPrivateIdentity.value = s_value;
            if ((s_value = window.localStorage.getItem('org.doubango.identity.impu'))) txtPublicIdentity.value = s_value;
            if ((s_value = window.localStorage.getItem('org.doubango.identity.password'))) txtPassword.value = s_value;
            if ((s_value = window.localStorage.getItem('org.doubango.identity.realm'))) txtRealm.value = s_value;
        }
        else {
            //txtDisplayName.value = "1060";
            //txtPrivateIdentity.value = "1060";
            //txtPublicIdentity.value = "sip:1060@doubango.org";
            //txtPassword.value = "1060";
            //txtRealm.value = "doubango.org";
            //txtPhoneNumber.value = "1062";
        }
    };

    function saveCredentials() {
        if (window.localStorage) {
            window.localStorage.setItem('org.doubango.identity.display_name', txtDisplayName.value);
            window.localStorage.setItem('org.doubango.identity.impi', txtPrivateIdentity.value);
            window.localStorage.setItem('org.doubango.identity.impu', txtPublicIdentity.value);
            window.localStorage.setItem('org.doubango.identity.password', txtPassword.value);
            window.localStorage.setItem('org.doubango.identity.realm', txtRealm.value);
        }
    };

    // sends SIP REGISTER request to login
    function sipRegister() {
        // catch exception for IE (DOM not ready)
        try {
            btnRegister.disabled = true;
            if (!txtRealm.value || !txtPrivateIdentity.value || !txtPublicIdentity.value) {
                txtRegStatus.innerHTML = '<b>Please fill madatory fields (*)</b>';
                btnRegister.disabled = false;
                return;
            }
            var o_impu = tsip_uri.prototype.Parse(txtPublicIdentity.value);
            if (!o_impu || !o_impu.s_user_name || !o_impu.s_host) {
                txtRegStatus.innerHTML = "<b>[" + txtPublicIdentity.value + "] is not a valid Public identity</b>";
                btnRegister.disabled = false;
                return;
            }

            // enable notifications if not already done
            if (window.webkitNotifications && window.webkitNotifications.checkPermission() != 0) {
                window.webkitNotifications.requestPermission();
            }

            // save credentials
            saveCredentials();

            // create SIP stack
            var i_port;
            var s_proxy;
            // @NotUsed
            // var b_disable_avpf = (window.localStorage.getItem('org.doubango.expert.disable_avpf') == "true");
            var s_websocket_server_url = window.localStorage ? window.localStorage.getItem('org.doubango.expert.websocket_server_url') : null;
            var s_sip_outboundproxy_url = window.localStorage ? window.localStorage.getItem('org.doubango.expert.sip_outboundproxy_url') : null;
            tsk_utils_log_info("s_websocket_server_url=" + (s_websocket_server_url || "(null)"));
            tsk_utils_log_info("s_sip_outboundproxy_url=" + (s_sip_outboundproxy_url || "(null)"));

            if (!tsk_utils_have_websocket()) {
                // port and host will be updated using the result from DNS SRV(NAPTR(realm))
                i_port = 5060;
                s_proxy = txtRealm.value;
            }
            else {
                // there are at least 5 servers running on the cloud on ports: 4062, 5062, 6062, 7062 and 8062
                // we will connect to one of them and let the balancer to choose the right one (less connected sockets)
                // each port can accept up to 65K connections which means that the cloud can manage 325K active connections
                // the number of port will be increased or decreased based on the current trafic
               <%
                SipConnector[] sipConnectors = (SipConnector[]) getServletContext().getAttribute(
                		"org.mobicents.servlet.sip.SIP_CONNECTORS");
            	for(int q=0; q<sipConnectors.length; q++) {
            		if(sipConnectors[q].getTransport().equalsIgnoreCase("WS")) {
            			out.println("i_port = " + sipConnectors[q].getPort() + ";");
            			out.println("s_proxy = \"" +sipConnectors[q].getIpAddress() + "\";");
            		}
            	}
            	%>
            }
            
            // create a new SIP stack. Not mandatory as it's possible to reuse the same satck
            oSipStack = new tsip_stack(txtRealm.value, txtPrivateIdentity.value, txtPublicIdentity.value, s_proxy, i_port,
                                    tsip_stack.prototype.SetPassword(txtPassword.value),
                                    tsip_stack.prototype.SetDisplayName(txtDisplayName.value),
                                    tsip_stack.prototype.SetWebsocketServerUrl(s_websocket_server_url),
                                    tsip_stack.prototype.SetProxyOutBoundUrl(s_sip_outboundproxy_url),
                                    // not valid? "IM-client/OMA1.0 sipML5-v0.0.0000.0/mozilla/5.0 (windows nt 6.0) applewebkit/537.9 (khtml, like gecko) chrome/23.0.1261.1 safari/537.9"
                                    // tsip_stack.prototype.SetHeader('User-Agent', 'IM-client/OMA1.0 sipML5-v0.0.0000.0/' + (navigator.userAgent || "unknown").toLowerCase()),
                                    tsip_stack.prototype.SetHeader('User-Agent', 'IM-client/OMA1.0 sipML5-v1.0.89.0/'),
                                    tsip_stack.prototype.SetHeader('Organization', 'Doubango Telecom'));
            
            oSipStack.on_event_stack = onSipEventStack;
            oSipStack.on_event_dialog = onSipEventDialog;
            oSipStack.on_event_invite = onSipEventInvite;

            if (oSipStack.start() != 0) {
                txtRegStatus.innerHTML = '<b>Failed to start the SIP stack</b>';
            }
            else return;
        }
        catch (e) {
            txtRegStatus.innerHTML = "<b>2:" + e + "</b>";
        }
        btnRegister.disabled = false;
    }

    // sends SIP REGISTER (expires=0) to logout
    function sipUnRegister() {
        if (oSipStack) {
            oSipStack.stop(); // shutdown all sessions
        }
    }

    // makes a call (SIP INVITE)
    function sipCall() {
        if (oSipStack && !oSipSessionCall && !tsk_string_is_null_or_empty(txtPhoneNumber.value)) {
            btnCall.disabled = true;
            btnHangUp.disabled = false;
            oSipSessionCall = new tsip_session_invite(oSipStack,
                                tsip_session.prototype.SetToStr(txtPhoneNumber.value),
                                tsip_session.prototype.SetCaps("+sip.ice")
                            );
            bDisableVideo = (window.localStorage && window.localStorage.getItem('org.doubango.expert.disable_video') == "true");
            if (oSipSessionCall.call(bDisableVideo ? tmedia_type_e.AUDIO : tmedia_type_e.AUDIO_VIDEO) != 0) {
                oSipSessionCall = null;
                txtCallStatus.value = 'Failed to make call';
                btnCall.disabled = false;
                btnHangUp.disabled = true;
                return;
            }
            saveCallOptions();
        }
        else if (oSipSessionCall) {
            txtCallStatus.innerHTML = '<i>Connecting...</i>';
            oSipSessionCall.accept();
        }
    }

    // transfers the call
    function sipTransfer() {
        if (oSipSessionCall) {
            var s_destination = prompt('Enter destination number', '');
            if (!tsk_string_is_null_or_empty(s_destination)) {
                btnTransfer.disabled = true;
                if (oSipSessionCall.transfer(s_destination) != 0) {
                    txtCallStatus.innerHTML = '<i>Call transfer failed</i>';
                    btnTransfer.disabled = false;
                    return;
                }
                txtCallStatus.innerHTML = '<i>Transfering the call...</i>';
            }
        }
    }
    
    // holds or resumes the call
    function sipToggleHoldResume() {
        if (oSipSessionCall) {
            var i_ret;
            btnHoldResume.disabled = true;
            txtCallStatus.innerHTML = oSipSessionCall.bHeld ? '<i>Resuming the call...</i>' : '<i>Holding the call...</i>';
            i_ret = oSipSessionCall.bHeld ? oSipSessionCall.resume() : oSipSessionCall.hold();
            if (i_ret != 0) {
                txtCallStatus.innerHTML = '<i>Hold / Resume failed</i>';
                btnHoldResume.disabled = false;
                return;
            }
        }
    }

    // terminates the call (SIP BYE or CANCEL)
    function sipHangUp() {
        if (oSipSessionCall) {
            txtCallStatus.innerHTML = '<i>Terminating the call...</i>';
            oSipSessionCall.hangup();
        }
    }

    function startRingTone() {
        try { ringtone.play(); }
        catch (e) { }
    }

    function stopRingTone() {
        try { ringtone.pause(); }
        catch (e) { }
    }

    function startRingbackTone() {
        try { ringbacktone.play(); }
        catch (e) { }
    }

    function stopRingbackTone() {
        try { ringbacktone.pause(); }
        catch (e) { }
    }

    function toggleFullScreen() {
        if (videoRemote.webkitSupportsFullscreen) {
            fullScreen(!videoRemote.webkitDisplayingFullscreen);
        }
        else {
            fullScreen(!bFullScreen);
        }
    }

    function fullScreen(b_fs) {
        bFullScreen = b_fs;
        if (tsk_utils_have_webrtc4native() && bFullScreen && videoRemote.webkitSupportsFullscreen) {
            if (bFullScreen) {
                videoRemote.webkitEnterFullScreen();
            }
            else {
                videoRemote.webkitExitFullscreen();
            }
        }
        else {
            if (tsk_utils_have_webrtc4npapi()) {
                try { __o_display_remote.setFullScreen(b_fs); }
                catch (e) { divVideo.setAttribute("class", b_fs ? "full-screen" : "normal-screen"); }
            }
            else {
                divVideo.setAttribute("class", b_fs ? "full-screen" : "normal-screen");
            }
        }
    }

    function showNotifICall(s_number) {
        // permission already asked when we registered
        if (window.webkitNotifications && window.webkitNotifications.checkPermission() == 0) {
            if (oNotifICall) {
                oNotifICall.cancel();
            }
            oNotifICall = window.webkitNotifications.createNotification('images/sipml-34x39.png', 'Incaming call', 'Incoming call from ' + s_number);
            oNotifICall.onclose = function () { oNotifICall = null; };
            oNotifICall.show();
        }
    }

    function onKeyUp(evt) {
        evt = (evt || window.event);
        if (evt.keyCode == 27) {
            fullScreen(false);
        }
        else if (evt.ctrlKey && evt.shiftKey) { // CTRL + SHIFT
            if (evt.keyCode == 65 || evt.keyCode == 86) { // A (65) or V (86)
                bDisableVideo = (evt.keyCode == 65);
                txtCallStatus.innerHTML = '<i>Video ' + (bDisableVideo ? 'disabled' : 'enabled') + '</i>';
                window.localStorage.setItem('org.doubango.expert.disable_video', bDisableVideo);
            }
        }
    }

    function onDivCallCtrlMouseMove(evt) {
        try { // IE: DOM not ready
            if (tsk_utils_have_stream()) {
                btnCall.disabled = (!tsk_utils_have_stream() || !oSipSessionRegister || !oSipSessionRegister.is_connected());
                document.getElementById("divCallCtrl").onmousemove = null; // unsubscribe
            }
        }
        catch (e) { }
    }

    function uiOnConnectionEvent(b_connected, b_connecting) { // should be enum: connecting, connected, terminating, terminated
        btnRegister.disabled = b_connected || b_connecting;
        btnUnRegister.disabled = !b_connected && !b_connecting;
        btnCall.disabled = false;//!(b_connected && tsk_utils_have_webrtc() && tsk_utils_have_stream());
        btnHangUp.disabled = !oSipSessionCall;
    }

    function uiVideoDisplayEvent(b_local, b_added, o_src_video) {
        if (!bDisableVideo) {
            var o_elt_video = b_local ? videoLocal : videoRemote;

            if (b_added) {
                if (tsk_utils_have_webrtc4all()) {
                    if (b_local) __o_display_local.style.visibility = "visible";
                    else __o_display_remote.style.visibility = "visible";
                   
                }
                else {
                    o_elt_video.src = o_src_video;
                    o_elt_video.style.opacity = 1;
                }
                uiVideoDisplayShowHide(true);
            }
            else {
                if (tsk_utils_have_webrtc4all()) {
                    if (b_local) __o_display_local.style.visibility = "hidden";
                    else __o_display_remote.style.visibility = "hidden";
                }
                else {
                    o_elt_video.style.opacity = 0;
                    if (o_elt_video.src && b_local) videoLocal.src = undefined;  // already revoked: must be done for remote video
                }
                fullScreen(false);
            }
        }
    }

    function uiVideoDisplayShowHide(b_show) {
        if (b_show) {
            tdVideo.style.height = '340px';
            divVideo.style.height = navigator.appName == 'Microsoft Internet Explorer' ? '100%' : '340px';
        }
        else {
            tdVideo.style.height = '0px';
            divVideo.style.height = '0px';
        }
        btnFullScreen.disabled = !b_show;
    }

    // Callback function for SIP Stacks
    function onSipEventStack(evt) {
        // this is a special event shared by all sessions and there is no "e_stack_type"
        // check the 'sip/stack' code
        tsk_utils_log_info(evt.s_phrase);
        switch (evt.i_code) {
            case tsip_event_code_e.STACK_STARTED:
                {
                    // catch exception for IE (DOM not ready)
                    try {
                        // LogIn (REGISTER) as soon as the stack finish starting
                        oSipSessionRegister = new tsip_session_register(oSipStack,
                                                    tsip_session.prototype.SetExpires(200),
                                                    tsip_session.prototype.SetCaps("+g.oma.sip-im"),
                                                    tsip_session.prototype.SetCaps("+audio"),
                                                    tsip_session.prototype.SetCaps("language", "\"en,fr\""));
                        oSipSessionRegister.register();
                    }
                    catch (e) {
                        txtRegStatus.value = txtRegStatus.innerHTML = "<b>1:" + e + "</b>";
                        btnRegister.disabled = false;
                    }
                    break;
                }
            case tsip_event_code_e.STACK_STOPPING:
            case tsip_event_code_e.STACK_STOPPED:
            case tsip_event_code_e.STACK_FAILED_TO_START:
            case tsip_event_code_e.STACK_FAILED_TO_STOP:
                {
                    var b_failure = ((evt.i_code == tsip_event_code_e.STACK_FAILED_TO_START) || (evt.i_code == tsip_event_code_e.STACK_FAILED_TO_STOP));
                    oSipStack = null;
                    oSipSessionRegister = null;
                    oSipSessionCall = null;

                    uiOnConnectionEvent(false, false);

                    stopRingbackTone();
                    stopRingTone();

                    uiVideoDisplayShowHide(false);
                    divCallOptions.style.opacity = 0;

                    txtCallStatus.innerHTML = '';
                    txtRegStatus.innerHTML = b_failure ? "<i>Disconnected: <b>" + evt.s_phrase + "</b></i>" : "<i>Disconnected</i>";
                    break;
                }

            case tsip_event_code_e.STACK_STARTING:
            default:
                {
                    break;
                }
        }
    };

    // Callback function for all SIP dialogs (INVITE, REGISTER, INFO...)
    function onSipEventDialog(evt) {
        // this is special event shared by all sessions and there is no "e_dialog_type"
        // check the 'sip/dialog' code
        tsk_utils_log_info(evt.s_phrase);
        switch (evt.i_code) {
            case tsip_event_code_e.DIALOG_TRANSPORT_ERROR:
            case tsip_event_code_e.DIALOG_GLOBAL_ERROR:
            case tsip_event_code_e.DIALOG_MESSAGE_ERROR:
            case tsip_event_code_e.DIALOG_WEBRTC_ERROR:

            case tsip_event_code_e.DIALOG_REQUEST_INCOMING:
            case tsip_event_code_e.DIALOG_REQUEST_OUTGOING:
            case tsip_event_code_e.DIALOG_REQUEST_CANCELLED:
            case tsip_event_code_e.DIALOG_REQUEST_SENT:
            case tsip_event_code_e.DIALOG_MEDIA_ADDED:
            case tsip_event_code_e.DIALOG_MEDIA_REMOVED:

            default: break;


            case tsip_event_code_e.DIALOG_CONNECTING:
            case tsip_event_code_e.DIALOG_CONNECTED:
                {
                    var b_connected = (evt.i_code == tsip_event_code_e.DIALOG_CONNECTED);

                    if (oSipSessionRegister && evt.get_session().get_id() == oSipSessionRegister.get_id()) {
                        uiOnConnectionEvent(b_connected, !b_connected);
                        txtRegStatus.innerHTML = "<i>" + evt.s_phrase + "</i>";
                    }
                    else if (oSipSessionCall && evt.get_session().get_id() == oSipSessionCall.get_id()) {
                        btnHangUp.value = 'HangUp';
                        btnCall.disabled = true;
                        btnHangUp.disabled = false;
                        btnTransfer.disabled = false;

                        if (b_connected) {
                            stopRingbackTone();
                            stopRingTone();

                            if (oNotifICall) {
                                oNotifICall.cancel();
                                oNotifICall = null;
                            }
                        }

                        txtCallStatus.innerHTML = "<i>" + evt.s_phrase + "</i>";
                        divCallOptions.style.opacity = b_connected ? 1 : 0;

                        if (tsk_utils_have_webrtc4all()) { // IE don't provide stream callback
                            uiVideoDisplayEvent(true, true);
                            uiVideoDisplayEvent(false, true);
                        }
                    }
                    break;
                }
            case tsip_event_code_e.DIALOG_TERMINATING:
            case tsip_event_code_e.DIALOG_TERMINATED:
                {
                    if (oSipSessionRegister && evt.get_session().get_id() == oSipSessionRegister.get_id()) {
                        uiOnConnectionEvent(false, false);

                        oSipSessionCall = null;
                        oSipSessionRegister = null;

                        txtRegStatus.innerHTML = "<i>" + evt.s_phrase + "</i>";                        
                    }
                    else if (oSipSessionCall && evt.get_session().get_id() == oSipSessionCall.get_id()) {
                        btnCall.value = 'Call';
                        btnHangUp.value = 'HangUp';
                        btnHoldResume.value = 'hold';
                        btnCall.disabled = false;
                        btnHangUp.disabled = true;

                        oSipSessionCall = null;

                        stopRingbackTone();
                        stopRingTone();

                        txtCallStatus.innerHTML = "<i>" + evt.s_phrase + "</i>";
                        uiVideoDisplayShowHide(false);
                        divCallOptions.style.opacity = 0;

                        if (oNotifICall) {
                            oNotifICall.cancel();
                            oNotifICall = null;
                        }

                        if (tsk_utils_have_webrtc4all()) { // IE don't provide stream callback
                            uiVideoDisplayEvent(true, false);
                            uiVideoDisplayEvent(false, false);
                        }

                        setTimeout(function () { if (!oSipSessionCall) txtCallStatus.innerHTML = ''; }, 2500);
                    }
                    break;
                }
        }
    };

    // Call back function for SIP INVITE Dialog
    function onSipEventInvite(evt) {
        tsk_utils_log_info(evt.s_phrase);
        
        switch (evt.e_invite_type) {
            case tsip_event_invite_type_e.I_NEW_CALL:
                {
                    if (oSipSessionCall) {
                        // do not accept the incoming call if we're already 'in call'
                        evt.get_session().hangup(); // comment this line for multi-line support
                    }
                    else {
                        oSipSessionCall = evt.get_session();

                        btnCall.value = 'Answer';
                        btnHangUp.value = 'Reject';
                        btnCall.disabled = false;
                        btnHangUp.disabled = false;

                        startRingTone();

                        var s_number = (oSipSessionCall.o_uri_from.s_display_name ? oSipSessionCall.o_uri_from.s_display_name : oSipSessionCall.o_uri_from.s_user_name);
                        txtCallStatus.innerHTML = "<i>Incoming call from [<b>" + s_number + "</b>]</i>";
                        showNotifICall(s_number);
                    }
                    break;
                }

            case tsip_event_invite_type_e.I_ECT_NEW_CALL:
                {
                    oSipSessionTransferCall = evt.get_session();
                    break;
                }

            case tsip_event_invite_type_e.I_AO_REQUEST:
                {
                    if (evt.i_code == 180 || evt.i_code == 183 && evt.get_message().is_response_to_invite()) {
                        startRingbackTone();
                        txtCallStatus.innerHTML = '<i>Remote ringing...</i>';
                    }
                    break;
                }

            case tsip_event_invite_type_e.M_EARLY_MEDIA:
                {
                    stopRingbackTone();
                    stopRingTone();
                    txtCallStatus.innerHTML = '<i>Early media started</i>';
                    break;
                }

            case tsip_event_invite_type_e.M_STREAM_VIDEO_LOCAL_ADDED:
                {
                    uiVideoDisplayEvent(true, true, evt.get_session().get_url_video_local());
                    break;
                }
            case tsip_event_invite_type_e.M_STREAM_VIDEO_LOCAL_REMOVED:
                {
                    uiVideoDisplayEvent(true, false);
                    break;
                }
            case tsip_event_invite_type_e.M_STREAM_VIDEO_REMOTE_ADDED:
                {
                    uiVideoDisplayEvent(false, true, evt.get_session().get_url_video_remote());
                    break;
                }
            case tsip_event_invite_type_e.M_STREAM_VIDEO_REMOTE_REMOVED:
                {
                    uiVideoDisplayEvent(false, false);
                    break;
                }

            case tsip_event_invite_type_e.M_LOCAL_HOLD_OK:
                {
                    if (oSipSessionCall.bTransfering) {
                        oSipSessionCall.bTransfering = false;
                        // this.AVSession.TransferCall(this.transferUri);
                    }
                    btnHoldResume.value = 'Resume';
                    btnHoldResume.disabled = false;
                    txtCallStatus.innerHTML = '<i>Call placed on hold</i>';
                    oSipSessionCall.bHeld = true;
                    break;
                }
            case tsip_event_invite_type_e.M_LOCAL_HOLD_NOK:
                {
                    oSipSessionCall.bTransfering = false;
                    btnHoldResume.value = 'Hold';
                    btnHoldResume.disabled = false;
                    txtCallStatus.innerHTML = '<i>Failed to place remote party on hold</i>';
                    break;
                }
            case tsip_event_invite_type_e.M_LOCAL_RESUME_OK:
                {
                    oSipSessionCall.bTransfering = false;
                    btnHoldResume.value = 'Hold';
                    btnHoldResume.disabled = false;
                    txtCallStatus.innerHTML = '<i>Call taken off hold</i>';
                    oSipSessionCall.bHeld = false;

                    if (tsk_utils_have_webrtc4all()) { // IE don't provide stream callback yet
                        uiVideoDisplayEvent(true, true);
                        uiVideoDisplayEvent(false, true);
                    }
                    break;
                }
            case tsip_event_invite_type_e.M_LOCAL_RESUME_NOK:
                {
                    oSipSessionCall.bTransfering = false;
                    btnHoldResume.disabled = false;
                    txtCallStatus.innerHTML = '<i>Failed to unhold call</i>';
                    break;
                }
            case tsip_event_invite_type_e.M_REMOTE_HOLD:
                {
                    txtCallStatus.innerHTML = '<i>Placed on hold by remote party</i>';
                    break;
                }
            case tsip_event_invite_type_e.M_REMOTE_RESUME:
                {
                    txtCallStatus.innerHTML = '<i>Taken off hold by remote party</i>';
                    break;
                }


            case tsip_event_invite_type_e.O_ECT_TRYING:
                {
                    txtCallStatus.innerHTML = '<i>Call transfer in progress...</i>';
                    break;
                }
            case tsip_event_invite_type_e.O_ECT_ACCEPTED:
                {
                    txtCallStatus.innerHTML = '<i>Call transfer accepted</i>';
                    break;
                }
            case tsip_event_invite_type_e.O_ECT_COMPLETED:
            case tsip_event_invite_type_e.I_ECT_COMPLETED:
                {
                    txtCallStatus.innerHTML = '<i>Call transfer completed</i>';
                    btnTransfer.disabled = false;
                    if (oSipSessionTransferCall) {
                        oSipSessionCall = oSipSessionTransferCall;
                    }
                    oSipSessionTransferCall = null;
                    break;
                }
	        case tsip_event_invite_type_e.O_ECT_FAILED:
	        case tsip_event_invite_type_e.I_ECT_FAILED:
	            {
	                txtCallStatus.innerHTML = '<i>Call transfer failed</i>';
	                btnTransfer.disabled = false;
	                break;
	            }
	        case tsip_event_invite_type_e.O_ECT_NOTIFY:
	        case tsip_event_invite_type_e.I_ECT_NOTIFY:
	            {
	                txtCallStatus.innerHTML = "<i>Call Transfer: <b>" + evt.i_code + " " + evt.s_phrase + "</b></i>";
	                if (evt.i_code >= 300) {
	                    if (oSipSessionCall.bHeld) {
	                        oSipSessionCall.resume();
	                    }
	                    btnTransfer.disabled = false;
	                }
	                break;
	            }
	        case tsip_event_invite_type_e.I_ECT_REQUESTED:
	            {
	                var o_hdr_Refer_To = evt.get_message().get_header(tsip_header_type_e.Refer_To); // header exist: already checked
	                if (o_hdr_Refer_To.o_uri) {
                        var s_message = "Do you accept call transfer to ["+ (o_hdr_Refer_To.s_display_name ? o_hdr_Refer_To.s_display_name : o_hdr_Refer_To.o_uri.s_user_name) + "]?";
                        if (confirm(s_message)) {
                            txtCallStatus.innerHTML = "<i>Call transfer in progress...</i>";
                            oSipSessionCall.transfer_accept();
                            break;
                        }
	                }
	                oSipSessionCall.transfer_reject();
	                break;
	            }

            default: break;
        }
    }

</script>
<body style="cursor:wait">
    <div class="navbar navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
                <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"><span
                    class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
                </a>
                <img alt="sipML5" class="brand" src="./images/sipml-34x39.png" />
                <div class="nav-collapse">
                    <ul class="nav">
                        <li class="active"><a href="index.html?svn=10">Home</a></li>
                    </ul>
                </div>
                <!--/.nav-collapse -->
            </div>
        </div>
    </div>
    <div class="container">
        <div class="row-fluid">
            <div class="span4 well">
                <label style="width: 100%;" align="center" id="txtRegStatus">
                </label>
                <h2>
                    Registration</h2>
                <br />
                <table style='width: 100%'>
                    <tr>
                        <td>
                            <label style="height: 100%">
                                Display Name:</label>
                        </td>
                        <td>
                            <input type="text" style="width: 100%; height: 100%" id="txtDisplayName" value=""
                                placeholder="e.g. John Doe" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label style="height: 100%">
                                Private Identity<sup>*</sup>:</label>
                        </td>
                        <td>
                            <input type="text" style="width: 100%; height: 100%" id="txtPrivateIdentity" value=""
                                placeholder="e.g. +33600000000" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label style="height: 100%">
                                Public Identity<sup>*</sup>:</label>
                        </td>
                        <td>
                            <input type="text" style="width: 100%; height: 100%" id="txtPublicIdentity" value=""
                                placeholder="e.g. sip:+33600000000@doubango.org" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label style="height: 100%">Password:</label>
                        </td>
                        <td>
                            <input type="password" style="width: 100%; height: 100%" id="txtPassword" value="" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label style="height: 100%">Realm<sup>*</sup>:</label>
                        </td>
                        <td>
                            <input type="text" style="width: 100%; height: 100%" id="txtRealm" value="" placeholder="e.g. doubango.org" />
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="right">
                            <input type="button" class="btn-success" id="btnRegister" value="LogIn" disabled onclick='sipRegister();' />
                            &nbsp;
                            <input type="button" class="btn-danger" id="btnUnRegister" value="LogOut" disabled onclick='sipUnRegister();' />
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">
                            <p class="small"><sup>*</sup> <i>Mandatory Field</i></p>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">
                            <a class="btn" href="http://code.google.com/p/sipml5/wiki/Public_SIP_Servers" target="_blank">Need SIP account?</a>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">
                            <a class="btn" href="./expert.htm" target="_blank">Expert mode?</a>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="divCallCtrl" class="span7 well" style='display:table-cell; vertical-align:middle'>
                <label style="width: 100%;" align="center" id="txtCallStatus">
                </label>
                <h2>
                    Call control
                </h2>
                <br />
                <table style='width: 100%;'>
                    <tr>
                        <td style="white-space:nowrap;">
                            <input type="text" style="width: 100%; height:100%" id="txtPhoneNumber" value="" placeholder="Enter phone number to call" />                            
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" align="right">
                            <input type="button" class="btn-primary" style="" id="btnCall" value="Call" onclick='sipCall();' disabled /> &nbsp;
                            <input type="button" class="btn-primary" style="" id="btnHangUp" value="HangUp" onclick='sipHangUp();' disabled />
                        </td>
                    </tr>
                    <tr>
                        <td id="tdVideo" class='tab-video'>
                            <div id="divVideo" class='div-video'>
                                <div id="divVideoRemote" style='border:1px solid #000; height:100%; width:100%';>
                                    <video class="video" width="100%" height="100%" id="video_remote" autoplay="autoplay" style="opacity: 0; 
                                        background-color: #000000; -webkit-transition-property: opacity; -webkit-transition-duration: 2s;">
                                    </video>
                                </div>
                                <div id="divVideoLocal" style='border:0px solid #000'>
                                    <video class="video" width="88px" height="72px" id="video_local" autoplay="autoplay" style="opacity: 0;
                                        margin-top: -80px; margin-left: 5px; background-color: #000000; -webkit-transition-property: opacity;
                                        -webkit-transition-duration: 2s;">
                                    </video>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                       <td align='center'>
                            <div id='divCallOptions' class='call-options' style='opacity: 0; margin-top: 3px'>
                                <input type="button" class="btn" style="" id="btnFullScreen" value="FullScreen" disabled onclick='toggleFullScreen();' /> &nbsp;
                                <input type="button" class="btn" style="" id="btnHoldResume" value="Hold" onclick='sipToggleHoldResume();' /> &nbsp;
                                <input type="button" class="btn" style="" id="btnTransfer" value="Transfer" onclick='sipTransfer();' /> &nbsp;
                                <!--input type="button" class="btn" style="" id="btnKeyPad" value="KeyPad" onclick='toto();' /-->
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        
        <br />
        <footer>
            <p>&copy; Doubango Telecom 2012 <br />
            <i>Inspiring the future</i>
            </p>
            <!-- Creates all ATL/COM objects right now 
                Will open confirmation dialogs if not already done
            -->
            <object id="fakeVideoDisplay" classid="clsid:5C2C407B-09D9-449B-BB83-C39B7802A684" style="visibility:hidden;"> </object>
            <object id="fakeLooper" classid="clsid:7082C446-54A8-4280-A18D-54143846211A" style="visibility:hidden;"> </object>
            <object id="fakeSessionDescription" classid="clsid:DBA9F8E2-F9FB-47CF-8797-986A69A1CA9C" style="visibility:hidden;"> </object>
            <object id="fakeNetTransport" classid="clsid:5A7D84EC-382C-4844-AB3A-9825DBE30DAE" style="visibility:hidden;"> </object>
            <object id="fakePeerConnection" classid="clsid:56D10AD3-8F52-4AA4-854B-41F4D6F9CEA3" style="visibility:hidden;"> </object>
            <!-- 
                NPAPI  browsers: Safari, Opera and Firefox
            -->
            <!--embed id="WebRtc4npapi" type="application/w4a" width='1' height='1' style='visibility:hidden;' /-->
        </footer>
    </div>
    <!-- /container -->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script type="text/javascript" src="./assets/js/jquery.js"></script>
    <script type="text/javascript" src="./assets/js/bootstrap-transition.js"></script>
    <script type="text/javascript" src="./assets/js/bootstrap-alert.js"></script>
    <script type="text/javascript" src="./assets/js/bootstrap-modal.js"></script>
    <script type="text/javascript" src="./assets/js/bootstrap-dropdown.js"></script>
    <script type="text/javascript" src="./assets/js/bootstrap-scrollspy.js"></script>
    <script type="text/javascript" src="./assets/js/bootstrap-tab.js"></script>
    <script type="text/javascript" src="./assets/js/bootstrap-tooltip.js"></script>
    <script type="text/javascript" src="./assets/js/bootstrap-popover.js"></script>
    <script type="text/javascript" src="./assets/js/bootstrap-button.js"></script>
    <script type="text/javascript" src="./assets/js/bootstrap-collapse.js"></script>
    <script type="text/javascript" src="./assets/js/bootstrap-carousel.js"></script>
    <script type="text/javascript" src="./assets/js/bootstrap-typeahead.js"></script>

    <!-- Audios -->
    <audio id="ringtone" loop src="sounds/ringtone.wav" />
    <audio id="ringbacktone" loop src="sounds/ringbacktone.wav" />    

    <!-- 
        Microsoft Internet Explorer extension
        For now we use msi installer as we don't have trusted certificate yet :(
    -->
    <!--object id="webrtc4ieLooper" classid="clsid:7082C446-54A8-4280-A18D-54143846211A" CODEBASE="http://sipml5.org/deploy/webrtc4all.CAB"> </object-->

    <!-- GOOGLE ANALYTICS
    <script type="text/javascript">
        var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
        document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
    </script>

    <script type="text/javascript">
        try {
            var pageTracker = _gat._getTracker("UA-6868621-19");
            pageTracker._trackPageview();
        } catch (err) { }
    </script> -->

</body>
</html>
