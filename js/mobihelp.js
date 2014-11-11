function pluginSend(evt, params) {
	NATIVE && NATIVE.plugins && NATIVE.plugins.sendEvent &&
		NATIVE.plugins.sendEvent("MobihelpPlugin", evt,
				JSON.stringify(params || {}));
}

function pluginOn(evt, next) {
	NATIVE && NATIVE.events && NATIVE.events.registerHandler &&
		NATIVE.events.registerHandler(evt, next);
}

function invokeCallbacks(list, clear) {
	// Pop off the first two arguments and keep the rest
	var args = Array.prototype.slice.call(arguments);
	args.shift();
	args.shift();

	// For each callback,
	for (var ii = 0; ii < list.length; ++ii) {
		var next = list[ii];

		// If callback was actually specified,
		if (next) {
			// Run it
			next.apply(null, args);
		}
	}

	// If asked to clear the list too,
	if (clear) {
		list.length = 0;
	}
}

var Mobihelp = Class(function () {
	var unreadNotifCountCB = [];
	
	this.init = function(opts) {
		logger.log("{mobihelp} Registering for events on startup");
		
		pluginOn("unreadNotificationCount", function(evt) {
			logger.log("{unreadNotificationCount} Count :", evt.count);

			invokeCallbacks(unreadNotifCountCB, false, evt);
		});
	}
	
	this.setUserInfo = function (email, fullName) {
		logger.log("{mobihelp} invoked setUserInfo");
		var params = {full_name: fullName, email: email};
		
		pluginSend("setUserInfo", params);
	}
	
	this.setUserEmail = function (email) {
		logger.log("{mobihelp} invoked setUserEmail");
		var params = {email: email};
		
		pluginSend("setUserEmail", params);
	}

	this.setUserFullName = function (fullName) {
		logger.log("{mobihelp} invoked setUserFullName");
		var params = {full_name: fullName};
		
		pluginSend("setUserFullName", params);
	}
	
	this.clearUserData = function () {
		logger.log("{mobihelp} invoked clear user data");
		pluginSend("clearUserData", {});
	}
	
	this.addBreadCrumb = function (breadCrumbText) {
		logger.log("{mobihelp} invoked add breadcrumb");
		var params = {breadcrumb_text: breadCrumbText};
		
		pluginSend("addBreadCrumb", params);
	}
	
	this.addCustomData = function (fieldName, value) {
		logger.log("{mobihelp} invoked add customdata");
		var params = {field_name: fieldName, value: value};
		
		pluginSend("addCustomData", params);
	}
	
	this.appRateDialogue = function () {
		logger.log("{mobihelp} invoked show app rate dialog");
		pluginSend("showAppRateDialog", {});
	}
	
	this.showConversations = function () {
		pluginSend("showConversations", {});
	}
	
	this.showFeedback = function () {
		pluginSend("showFeedback", {});
	}
	
	this.showSupport = function () {
		pluginSend("showSupport", {});
	}

	this.registerUnreadNotifCallback = function (callBack) {
		unreadNotifCountCB.push(callBack);
	}

	this.checkUnreadNotifications = function (async) {
		var params = {};
		
		logger.log("{mobihelp} check unread notifications");
		
		params["async"] = !!async;
		pluginSend("checkUnreadNotifications", params);
	}
});

exports = new Mobihelp();
