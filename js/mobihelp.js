function pluginSend(evt, params) {
	NATIVE.plugins.sendEvent("MobihelpPlugin", evt,
			JSON.stringify(params || {}));
}

function pluginOn(evt, next) {
	NATIVE.events.registerHandler(evt, next);
}

function invokeCallbacks(list) {
	// Pop off the first two arguments and keep the rest
	var args = Array.prototype.splice.call(arguments, 2),
	    i = 0,
	    len = list.length,
	    next;

	// For each callback,
	for (i = 0; i < len; ++i) {
		next = list[i];

		// If callback was actually specified,
		if (next) {
			// Run it
			next.apply(null, args);
		}
	}
}

exports = new (Class(function () {
	var unread_cb = [];
	
	this.init = function(opts) {
		logger.log("{mobihelp} Registering for events on startup");
		
		pluginOn("mobihelpNotifCount", function(evt) {
			logger.log("{unreadNotificationCount} Count :", evt.count);

			invokeCallbacks(unread_cb, evt);
		});
	}
	
	this.setUserInfo = function (email, full_name) {
		pluginSend("setUserInfo", {full_name: full_name, email: email});
	}
	
	this.setUserEmail = function (email) {
		pluginSend("setUserEmail", {email: email});
	}

	this.setUserFullName = function (full_name) {
		pluginSend("setUserFullName", {full_name: full_name});
	}
	
	this.clearUserData = function () {
		pluginSend("clearUserData", {});
	}
	
	this.leaveBreadCrumb = function (bread_crumb) {
		pluginSend("leaveBreadCrumb", {breadcrumb: bread_crumb});
	}
	
	this.addCustomData = function (name, value) {
		pluginSend("addCustomData", {field_name: name, value: value});
	}
	
	this.showAppRateDialog = function () {
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

	this.registerUnreadNotifCallback = function (cb) {
		unread_cb.push(cb);
	}

	this.checkUnreadNotifications = function (async) {
		pluginSend("checkUnreadNotifications", {async: !!async});
	}
}))();
