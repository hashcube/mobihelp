#import "MobihelpPlugin.h"

@implementation MobihelpPlugin

// The plugin must call super dealloc.
- (void) dealloc {
	[super dealloc];
}

// The plugin must call super init.
- (id) init {
	self = [super init];
	if (!self) {
		return nil;
	}

	return self;
}

- (void) initializeWithManifest:(NSDictionary *)manifest appDelegate:(TeaLeafAppDelegate *)appDelegate {

	NSDictionary *ios = [manifest valueForKey:@"ios"];
	NSString *domain = [ios valueForKey:@"mobihelpDomain"];
	NSString *key = [ios valueForKey:@"mobihelpAppKey"];
	NSString *secret = [ios valueForKey:@"mobihelpAppSecret"];
	self.viewController = appDelegate.tealeafViewController;

	MobihelpConfig *config = [[MobihelpConfig alloc]initWithDomain:domain
		withAppKey:key
		andAppSecret:secret];
	[[Mobihelp sharedInstance]initWithConfig:config];
	[self getUnreadCount];
}

- (void) applicationDidBecomeActive:(UIApplication *)app {
	[self getUnreadCount];
}

- (void) getUnreadCount {
	[[Mobihelp sharedInstance] unreadCountWithCompletion:^(NSInteger count) {
		[[PluginManager get] dispatchJSEvent:[NSDictionary dictionaryWithObjectsAndKeys:
			@"mobihelpNotifCount", @"name",
			[NSString stringWithFormat: @"%ld", (long)count], @"count",
			nil]];
	}];
}

- (void) setUserInfo: (NSDictionary *)jsonObject {
	[[Mobihelp sharedInstance] setEmailAddress:[jsonObject objectForKey:@"email"]];
	[[Mobihelp sharedInstance] setUserName:[jsonObject objectForKey:@"full_name"]];
}

- (void) setUserEmail: (NSDictionary *)jsonObject {
	[[Mobihelp sharedInstance] setEmailAddress:[jsonObject objectForKey:@"email"]];
}

- (void) setUserFullName: (NSDictionary *)jsonObject {
	[[Mobihelp sharedInstance] setUserName:[jsonObject objectForKey:@"full_name"]];
}

- (void) clearUserData: (NSDictionary *)jsonObject {
	[[Mobihelp sharedInstance] clearUserData];
}

- (void) leaveBreadCrumb: (NSDictionary *)jsonObject {
	[[Mobihelp sharedInstance]
		leaveBreadcrumb:[jsonObject objectForKey:@"breadcrumb"]];
}

- (void) addCustomData: (NSDictionary *)jsonObject {
	[[Mobihelp sharedInstance]
		addCustomDataForKey:[jsonObject objectForKey:@"field_name"]
		withValue:[jsonObject objectForKey:@"value"]];
}

- (void) showAppRateDialog: (NSDictionary *)jsonObject {
	[[Mobihelp sharedInstance] launchAppReviewRequest];
}

- (void) showConversations: (NSDictionary *)jsonObject {
}

- (void) showFeedback: (NSDictionary *)jsonObject {
	[[Mobihelp sharedInstance] presentFeedback:self.viewController];
}

- (void) showSupport: (NSDictionary *)jsonObject {
	[[Mobihelp sharedInstance] presentSupport:self.viewController];
}
@end
