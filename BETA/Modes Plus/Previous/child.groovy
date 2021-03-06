/**
 *  **************** Modes Plus Child ****************
 *
 *  Design Usage:
 *  This was designed to use various triggers to control location modes
 *  Building on the ideas of HE 'Mode Manager', this app also has restrictions on when it will activate the mode. 
 *
 *  Copyright 2018 Andrew Parker
 *  
 *  This SmartApp is free!
 *  Donations to support development efforts are accepted via: 
 *
 *  Paypal at: https://www.paypal.me/smartcobra
 *  
 *
 *  I'm very happy for you to use this app without a donation, but if you find it useful then it would be nice to get a 'shout out' on the forum! -  @Cobra
 *  Have an idea to make this app better?  - Please let me know :)
 *
 *  Website: http://hubitat.uk
 *
 *-------------------------------------------------------------------------------------------------------------------
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *-------------------------------------------------------------------------------------------------------------------
 *
 *  If modifying this project, please keep the above header intact and add your comments/credits below - Thank you! -  @Cobra
 *
 *-------------------------------------------------------------------------------------------------------------------
 *
 *  Last Update: 28/10/2018
 *
 *  Changes:
 *
 *  
 *  V1.6.0 - Added configurable actions on restriction switch
 *  V1.5.0 - Added 'Pause' switch to temporarily disable the app
 *  V1.4.0 - Added 'illuminance' trigger
 *  V1.3.0 - Initial Release to the community
 *  V1.2.0 - Revised update checking, with the option for a 'Pushover' message if there is an update
 *  V1.1.1 - Debug 'Between' time checker
 *  V1.1.0 - Added restrictions page and handlers
 *  V1.0.0 - POC
 *
 */



definition(
    name: "Modes Plus Child",
    namespace: "Cobra",
    author: "AJ Parker",
    description: "This was designed to use various triggers to control location modes",
    category: "My Apps",
    
parent: "Cobra:Modes Plus",
    
    iconUrl: "",
    iconX2Url: ""
)
preferences {

section("") {
        page name: "mainPage", title: "", install: false, uninstall: true, nextPage: "restrictionsPage"
        page name: "restrictionsPage", title: "", install: true, uninstall: true
		}
   
    
}    
    

 def mainPage() {
    dynamicPage(name: "mainPage") {  
        
       display()
        
 section("") {
   input "newMode1", "mode", title: "Which Mode do you want to enter?",  required: true, multiple: false, submitOnChange:true
     if(newMode1){
    input "triggerMode", "enum", required: true, title: "Select Trigger ", submitOnChange: true,  options: ["Button", "Illuminance", "Presence - Arrival", "Presence - Departure", "Sunrise", "Sunset", "Switch", "Time"]
         
          if(triggerMode == "Switch"){input "switch2", "capability.switch", title: "Select Switch", required: true, multiple: false}
          if(triggerMode == "Button"){
                input "button1", "capability.pushableButton", title: "Select Button Device", required: true, multiple: false, submitOnChange: true
              if(button1){
            	input "buttonNumber", "enum", title: "Enter Button Number", required: true, options: ["1", "2", "3", "4", "5"] 
              }
            }
         if(triggerMode == "Presence - Arrival" || triggerMode == "Presence - Departure"){
         input "presence1", "capability.presenceSensor", title: "Select Presence Sensor", required: true, multiple: false
         
         }
          if(triggerMode == "Time"){input (name: "runTime", title: "Time to run", type: "time",  required: true)}       
     }
    	 if(triggerMode == "Sunset"){	
       			input "sunsetOffsetValue", "number", title: "Optional Sunset Offset (Minutes)", required: false
				input "sunsetOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
        }
     if(triggerMode == "Sunrise"){
    			input "sunriseOffsetValue", "number", title: "Optional Sunrise Offset (Minutes)", required: false
				input "sunriseOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
         
     }
     if(triggerMode == "Illuminance"){
         	input "luxDevice", "capability.sensor", title: "Lux Device", required: false
     		input "luxLevel", "number", title: "Lux level to trigger", required: false
         	input "luxMode", "enum", title: "Change mode above  or below  this level", required: true, options: ["Above", "Below"]
     }
         
 }  
    }
    }



 def restrictionsPage() {
       dynamicPage(name: "restrictionsPage") {
           
           section() {
           		mode title: "Run only when in specific mode(s) ", required: false
            }
        

		section() {
        
    input "restrictions1", "bool", title: "Show Optional Time, Day, Sunset/Sunrise & Mode Restrictions", required: true, defaultValue: false, submitOnChange: true
    input "restrictions2", "bool", title: "Show Optional Switch & Presence Restrictions", required: true, defaultValue: false, submitOnChange: true
   
     }

      if(restrictions1 == true){    
     	section() {
    input "fromTime", "time", title: "Allow actions from", required: false
    input "toTime", "time", title: "Allow actions until", required: false
    input "days", "enum", title: "Allow action on these days", required: false, multiple: true, options: ["Monday": "Monday", "Tuesday": "Tuesday", "Wednesday": "Wednesday", "Thursday": "Thursday", "Friday": "Friday", "Saturday": "Saturday", "Sunday": "Sunday"]
    input "modes", "mode", title: "Allow actions when current mode is:", multiple: true, required: false
    input "sunriseSunset", "enum", title: "Sunrise/Sunset Restriction", required: false, submitOnChange: true, options: ["Sunrise","Sunset"] 
      
     if(sunriseSunset == "Sunset"){	
       			input "sunsetOffsetValue", "number", title: "Optional Sunset Offset (Minutes)", required: false
				input "sunsetOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
        }
     if(sunriseSunset == "Sunrise"){
    			input "sunriseOffsetValue", "number", title: "Optional Sunrise Offset (Minutes)", required: false
				input "sunriseOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
         
     }
            
        }       
      }       
      if(restrictions2 == true){
          
          
          
    section() {
     input(name: "enableswitch1", type: "capability.switch", title: "Allow actions when this switch is on/off", required: false, multiple: false, submitOnChange: true)
        if(enableswitch1){ input "enableSwitchMode", "bool", title: "Allow actions only when this switch is..", required: true, defaultValue: false, submitOnChange: true}
            
           
        
    input "restrictPresenceSensor", "capability.presenceSensor", title: "Select presence sensor 1 to restrict action", required: false, multiple: false, submitOnChange: true
    if(restrictPresenceSensor){
   	input "restrictPresenceAction", "bool", title: "   On = Action only when someone is 'Present'  \r\n   Off = Action only when someone is 'NOT Present'  ", required: true, defaultValue: false    
	}
     input "restrictPresenceSensor1", "capability.presenceSensor", title: "Select presence sensor 2 to restrict action", required: false, multiple: false, submitOnChange: true
    if(restrictPresenceSensor1){
   	input "restrictPresenceAction1", "bool", title: "   On = Action only when someone is 'Present'  \r\n   Off = Action only when someone is 'NOT Present'  ", required: true, defaultValue: false    
	}
    
    }
            } 
           
           
          
       section() {
                label title: "Enter a name for this automation", required: false
            }
      section() {
            input "debugMode", "bool", title: "Enable logging", required: true, defaultValue: false
  	        }

           
           
      }          
           
       }

           










def installed() {
    initialize()
}

def updated() {
    unsubscribe()
    initialize()
}

def initialize() {
   	logCheck()
	version()
	pauseOrNot()
    schedule("0 1 0 1/1 * ? *", astroCheck)	// checks sunrise/sunset change at 00.01am every day 
    astroCheck()
    
     if(triggerMode == "Illuminance"){
         if(luxMode){state.luxAction = luxMode}
         if(!luxMode){state.luxAction = false}
   	   subscribe(luxDevice, "illuminance", luxHandler)
   } 
    if(triggerMode == "Sunrise"){
       state.sunRiseGo = true
       state.sunSetGo = false
   }
   if(triggerMode == "Sunset"){
       state.sunSetGo = true
   	   state.sunRiseGo = false
   } 
    
   
    if(triggerMode == "Switch"){subscribe(switch2, "switch.on", evtHandler)}
    if(triggerMode == "Button"){
        if(buttonNumber == '1'){subscribe(button1, "pushed.1", evtHandler)}
        if(buttonNumber == '2'){subscribe(button1, "pushed.2", evtHandler)}
        if(buttonNumber == '3'){subscribe(button1, "pushed.3", evtHandler)}
        if(buttonNumber == '4'){subscribe(button1, "pushed.4", evtHandler)}
        if(buttonNumber == '5'){subscribe(button1, "pushed.5", evtHandler)}
    } 
    if(triggerMode == "Time"){schedule(runTime, evtHandler)}
    if(triggerMode == "Presence - Arrival"){ subscribe(presence1, "presence", presenceHandler1)}
    if(triggerMode == "Presence - Departure"){ subscribe(presence1, "presence", presenceHandler1)}
    if (restrictPresenceSensor){subscribe(restrictPresenceSensor, "presence", restrictPresenceSensorHandler)}
	if (restrictPresenceSensor1){subscribe(restrictPresenceSensor1, "presence", restrictPresence1SensorHandler)}
    if(enableswitch1){subscribe(enableswitch1, "switch", enableSwitch1Handler)}
    if(enableSwitchMode == null){enableSwitchMode = false}
    state.appGo = true
    log.info "Initialised with settings: ${settings}"
    
 //   state.sunGoNow = false // test code
    
} 


def checkRestrictions(){
    checkTime()
    checkDay()
    checkPresence()
    checkPresence1()
    checkMode()
    checkSun()
//    LOGDEBUG("Checking restrictions...state.timeOK == $state.timeOK - state.dayCheck == $state.dayCheck - state.enablecurrS1 == $state.enablecurrS1 - state.presenceRestriction == $state.presenceRestriction -  state.presenceRestriction1 == $state.presenceRestriction1 -  state.modeCheck == $state.modeCheck")
    if(state.timeOK == true && state.dayCheck == true && state.enablecurrS1 == 'on' && state.presenceRestriction == true && state.presenceRestriction1 == true && state.modeCheck == true && state.sunGoNow == true){
        state.appGo = true
        LOGDEBUG("Restrictions ok.. continue...")
            }  
    else {
        LOGDEBUG("One or more restrictions are in place so cannot continue...")
         state.appGo = false
    }
}

def enableSwitch1Handler(evt){
    state.enableInput = evt.value
    
    if(enableSwitchMode == true && state.enableInput == 'off'){
state.enablecurrS1 = 'off'
        LOGDEBUG("Cannot change mode - App disabled by switch")  
    }
     if(enableSwitchMode == true && state.enableInput == 'on'){
state.enablecurrS1 = 'on'
        LOGDEBUG("Switch restriction is OK.. Continue...") 
    }    
     if(enableSwitchMode == false && state.enableInput == 'off'){
state.enablecurrS1 = 'on'
        LOGDEBUG("Switch restriction is OK.. Continue...")  
    }
     if(enableSwitchMode == false && state.enableInput == 'on'){
state.enablecurrS1 = 'off'
        LOGDEBUG("Cannot change mode - App disabled by switch")  
    }    
      
    
LOGDEBUG("Allow by switch is $state.enablecurrS1")
        

}

// Check Mode
def checkMode() {
    LOGDEBUG("Checking mode...")
	def result = !modes || modes.contains(location.mode)
    
    LOGDEBUG("Mode = $result")
    state.modeCheck = result
    return state.modeCheck
 }    






// Check Time Restriction
def checkTime(){
    LOGDEBUG("Checking time...")
def timecheckNow = fromTime
if (timecheckNow != null){
def between = timeOfDayIsBetween(toDateTime(fromTime), toDateTime(toTime), new Date(), location.timeZone)
    if (between) {
    state.timeOK = true
   LOGDEBUG("Time is ok so can continue...")
    
}
else if (!between) {
state.timeOK = false
LOGDEBUG("Time is NOT ok so cannot continue...")
	}
  }
else if (timecheckNow == null){  
state.timeOK = true
  LOGDEBUG("Time restrictions have not been configured -  Continue...")
  }
}


// check days allowed to run
def checkDay(){
    LOGDEBUG("Checking days...")
def daycheckNow = days
if (daycheckNow != null){
 def df = new java.text.SimpleDateFormat("EEEE")
    
    df.setTimeZone(location.timeZone)
    def day = df.format(new Date())
    def dayCheck1 = days.contains(day)
    if (dayCheck1) {

  state.dayCheck = true
LOGDEBUG( " Day ok so can continue...")
 }       
 else {
LOGDEBUG( " Not today!")
 state.dayCheck = false
 }
 }
if (daycheckNow == null){ 
 LOGDEBUG("Day restrictions have not been configured -  Continue...")
 state.dayCheck = true 
} 
}


// Checking Presence restrictions
def restrictPresenceSensorHandler(evt){
state.presencestatus1 = evt.value
LOGDEBUG("state.presencestatus1 = $evt.value")
checkPresence()
}



def checkPresence(){
LOGDEBUG("running checkPresence - restrictPresenceSensor = $restrictPresenceSensor")

if(restrictPresenceSensor){
LOGDEBUG("Presence = $state.presencestatus1")
def actionPresenceRestrict = restrictPresenceAction

if (state.presencestatus1 == "present" && actionPresenceRestrict == true){
LOGDEBUG("Presence ok")
state.presenceRestriction = true
}
else if (state.presencestatus1 == "not present" && actionPresenceRestrict == true){
LOGDEBUG("Presence not ok")
state.presenceRestriction = false
}

if (state.presencestatus1 == "not present" && actionPresenceRestrict == false){
LOGDEBUG("Presence ok")
state.presenceRestriction = true
}
else if (state.presencestatus1 == "present" && actionPresenceRestrict == false){
LOGDEBUG("Presence not ok")
state.presenceRestriction = false
}
}
else if(!restrictPresenceSensor){
state.presenceRestriction = true
LOGDEBUG("Presence sensor restriction not used")
}
}


def restrictPresence1SensorHandler(evt){
state.presencestatus2 = evt.value
LOGDEBUG("state.presencestatus2 = $evt.value")
checkPresence1()

}


def checkPresence1(){
LOGDEBUG("running checkPresence1 - restrictPresenceSensor1 = $restrictPresenceSensor1")

if(restrictPresenceSensor1){
LOGDEBUG("Presence = $state.presencestatus1")
def actionPresenceRestrict1 = restrictPresenceAction1


if (state.presencestatus2 == "present" && actionPresenceRestrict1 == true){
LOGDEBUG("Presence 2 ok")
state.presenceRestriction1 = true
}
else if (state.presencestatus2 == "not present" && actionPresenceRestrict1 == true){
LOGDEBUG("Presence 2 not ok")
state.presenceRestriction1 = false
}

if (state.presencestatus2 == "not present" && actionPresenceRestrict1 == false){
LOGDEBUG("Presence 2 ok")
state.presenceRestriction1 = true
}
else if (state.presencestatus2 == "present" && actionPresenceRestrict1 == false){
LOGDEBUG("Presence 2 not ok")
state.presenceRestriction1 = false
}
}
else if(!restrictPresenceSensor1){
state.presenceRestriction1 = true
LOGDEBUG("Presence sensor 2 restriction not used")
}
}

// Astro restrictions

def checkSun(){
    LOGDEBUG("Checking Sunrise/Sunset restrictions...")
    if(!sunriseSunset){
        state.sunGoNow = true
        LOGDEBUG("No Sunrise/Sunset restrictions in place")
    }
         
    if(sunriseSunset){
        if(sunriseSunset == "Sunset"){	
            if(state.astro == "Set"){
                state.sunGoNow = true
            LOGDEBUG("Sunset OK")
            } 
    		if(state.astro == "Rise"){
                state.sunGoNow = false
             LOGDEBUG("Sunset NOT OK")
            } 
        }
    if(sunriseSunset == "Sunrise"){	
            if(state.astro == "Rise"){
                state.sunGoNow = true
            LOGDEBUG("Sunrise OK")
            } 
    		if(state.astro == "Set"){
                state.sunGoNow = false
             LOGDEBUG("Sunrise NOT OK")
            } 
        }  
    } 
		return state.sunGoNow
}    
    
def luxHandler(evt){
    state.newLux = evt.value.toDouble()
    state.requiredLux = luxLevel.toDouble()
    LOGDEBUG("Recieved Lux:$state.newLux")
    if(state.newLux > state.requiredLux && state.luxAction == "Above"){ evtHandler()}
    if(state.newLux < state.requiredLux && state.luxAction == "Below"){ evtHandler()}
    }
    

def astroCheck() {
    state.sunsetOffsetValue1 = sunsetOffsetValue
    state.sunriseOffsetValue1 = sunriseOffsetValue
    
 //   log.warn "state.sunriseOffsetValue1 = $state.sunriseOffsetValue1 - state.sunsetOffsetValue1 = $state.sunsetOffsetValue1"  // test code
    
    if(sunsetOffsetDir == "Before"){state.sunsetOffset1 = -state.sunsetOffsetValue1}
    if(sunsetOffsetDir == "after"){state.sunsetOffset1 = state.sunsetOffsetValue1}
    if(sunriseOffsetDir == "Before"){state.sunriseOffset1 = -state.sunriseOffsetValue1}
    if(sunriseOffsetDir == "after"){state.sunriseOffset1 = state.sunriseOffsetValue1}
    
	def both = getSunriseAndSunset(sunriseOffset: state.sunriseOffset1, sunsetOffset: state.sunsetOffset1)
	 
    
	def now = new Date()
	def riseTime = both.sunrise
	def setTime = both.sunset
	log.debug "riseTime: $riseTime"
	log.debug "setTime: $setTime"
    

		unschedule("sunriseHandler")
		unschedule("sunsetHandler")

		if (riseTime.after(now)) {
			log.info "scheduling sunrise handler for $riseTime"
			runOnce(riseTime, sunriseHandler)
		}

		if (setTime.after(now)) {
			log.info "scheduling sunset handler for $setTime"
			runOnce(setTime, sunsetHandler)
		}

   LOGDEBUG("AstroCheck Complete")
}


def sunsetHandler(evt) {
LOGDEBUG("Sun has set!")
   state.astro = "Set" 
    if(state.sunSetGo == true && triggerMode == "SunSet"){
   evtHandler()
    }
     else{
    LOGDEBUG("Sunset Trigger not configured!")
    }
}


def sunriseHandler(evt) {
LOGDEBUG("Sun has risen!")
    state.astro = "Rise"
    if(state.sunRiseGo == true && triggerMode == "Sunrise"){
     evtHandler()
    }
    else{
    LOGDEBUG("Sunrise Trigger not configured!")
    }
}

private getSunriseOffset() {
	sunriseOffsetValue ? (sunriseOffsetDir == "Before" ? "-$sunriseOffsetValue" : sunriseOffsetValue) : null
}

private getSunsetOffset() {
	sunsetOffsetValue ? (sunsetOffsetDir == "Before" ? "-$sunsetOffsetValue" : sunsetOffsetValue) : null
}


def presenceHandler1(evt){
    state.presenceNow = evt.value

    if(triggerMode == "Presence - Arrival" && state.presenceNow == "present"){evtHandler(evt)}
    if(triggerMode == "Presence - Departure" && state.presenceNow == "not present"){evtHandler(evt)}    
        
}
def evtHandler(evt){
    LOGDEBUG("Running eventHandler... ")
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused" 
    checkRestrictions()
    if(state.appGo == true){
    state.requiredMode = newMode1
    LOGDEBUG("Changing to: $state.requiredMode ")
    setLocationMode(state.requiredMode) 
	}
  }
}





def logCheck(){
state.checkLog = debugMode
    if(state.checkLog == true){log.info "All Logging Enabled"}
    if(state.checkLog == false){log.info "Further Logging Disabled"}
}

def LOGDEBUG(txt){
    try {
    	if (settings.debugMode) { log.debug("${app.label.replace(" ","_").toUpperCase()}  (App Version: ${state.version}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}


def version(){
    resetBtnName()
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
	updateCheck()  
    checkButtons()
    
    
}

def display(){
  
	if(state.status){
	section{paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br><font face='Lucida Handwriting'>$state.Copyright </font>"}
       
        }
   

    if(state.status != "<b>** This app is no longer supported by $state.author  **</b>"){
     section(){ input "updateBtn", "button", title: "$state.btnName"}
    }
    
    section(){
   //     log.info "app.label = $app.label"
    input "pause1", "bool", title: "Pause This App", required: true, submitOnChange: true, defaultValue: false  
     }
    pauseOrNot()   
    if(state.status != "Current"){
	section{ 
	paragraph "<b>Update Info:</b> <BR>$state.UpdateInfo <BR>$state.updateURI"
     }
    }
	section(" ") {
      input "updateNotification", "bool", title: "Send a 'Pushover' message when an update is available", required: true, defaultValue: false, submitOnChange: true 
      if(updateNotification == true){ input "speaker", "capability.speechSynthesis", title: "PushOver Device", required: true, multiple: true}
    }
    

}

def checkButtons(){
    LOGDEBUG("Running checkButtons")
    appButtonHandler("updateBtn")
}


def appButtonHandler(btn){
    state.btnCall = btn
    if(state.btnCall == "updateBtn"){
       log.info "Checking for updates now..."
        updateCheck()
        pause(3000)
  		state.btnName = state.newBtn
        runIn(2, resetBtnName)
    }
    if(state.btnCall == "updateBtn1"){
    state.btnName1 = "Click Here" 
    httpGet("https://github.com/CobraVmax/Hubitat/tree/master/Apps' target='_blank")
    }
    
}   
def resetBtnName(){
//    log.info "Resetting Update Button Name"
    if(state.status != "Current"){
	state.btnName = state.newBtn
    }
    else{
 state.btnName = "Check For Update" 
    }
}    
    
def pushOverNow(inMsg){
    if(updateNotification == true){  
     newMessage = inMsg
  log.info "Message = $newMessage " 
     state.msg1 = '[L]' + newMessage
	speaker.speak(state.msg1)
    }
}

def pauseOrNot(){
LOGDEBUG(" Calling 'pauseOrNot'...")
    state.pauseNow = pause1
        if(state.pauseNow == true){
            state.pauseApp = true
            if(app.label){
            if(app.label.contains('red')){
                log.warn "Paused"}
            else{app.updateLabel(app.label + ("<font color = 'red'> (Paused) </font>" ))
              log.warn "App Paused - state.pauseApp = $state.pauseApp "   
                }
    
            }
        }
    
     if(state.pauseNow == false){
         state.pauseApp = false
         if(app.label){
     if(app.label.contains('red')){ app.updateLabel(app.label.minus("<font color = 'red'> (Paused) </font>" ))
     LOGDEBUG("App Released - state.pauseApp = $state.pauseApp ")                          
                                  }
         }
  }    
    
}


def updateCheck(){
    setVersion()
	def paramsUD = [uri: "http://update.hubitat.uk/cobra.json"]
       	try {
        httpGet(paramsUD) { respUD ->
 //  log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code 
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def updateUri = (respUD.data.versions.UpdateInfo.GithubFiles.(state.InternalName))
            state.updateURI = updateUri   
            
            def newVerRaw = (respUD.data.versions.Application.(state.InternalName))
            def newVer = (respUD.data.versions.Application.(state.InternalName).replace(".", ""))
       		def currentVer = state.version.replace(".", "")
      		state.UpdateInfo = (respUD.data.versions.UpdateInfo.Application.(state.InternalName))
                state.author = (respUD.data.author)
           
		if(newVer == "NLS"){
            state.status = "<b>** This app is no longer supported by $state.author  **</b>"  
             log.warn "** This app is no longer supported by $state.author **" 
            
      		}           
		else if(currentVer < newVer){
        	state.status = "<b>New Version Available (Version: $newVerRaw)</b>"
        	log.warn "** There is a newer version of this app available  (Version: $newVerRaw) **"
        	log.warn "** $state.UpdateInfo **"
             state.newBtn = state.status
            def updateMsg = "There is a new version of '$state.ExternalName' available (Version: $newVerRaw)"
            pushOverNow(updateMsg)
       		} 
		else{ 
      		state.status = "Current"
       		log.info "You are using the current version of this app"
       		}
      					}
        	} 
        catch (e) {
        	log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
    		}
    if(state.status != "Current"){
		state.newBtn = state.status
        
    }
    else{
        state.newBtn = "No Update Available"
    }
        
        
}


def setVersion(){
		state.version = "1.6.0"	 
		state.InternalName = "ModesPlusChild"
    	state.ExternalName = "Modes Plus Child"
}




