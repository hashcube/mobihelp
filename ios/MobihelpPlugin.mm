#import "MobihelpPlugin.h"

@interface MobihelpPlugin()
@property (nonatomic) BOOL initDone;
@property NSInteger unread_count;
@end

@implementation MobihelpPlugin

// The plugin must call super dealloc.
- (void) dealloc {
	[super dealloc];
}

// The plugin must call super init.
- (id) init {
	self = [super init];
	if (!self) {
		self.initDone = NO;
		return nil;
	}

	return self;
}

- (void) initializeWithManifest:(NSDictionary *)manifest appDelegate:(TeaLeafAppDelegate *)appDelegate {
	@try {
		NSString *appStoreId = @"id";
		NSDictionary *ios = [manifest valueForKey:@"ios"];
		NSString *domain = [ios valueForKey:@"mobihelpDomain"];
		NSString *key = [ios valueForKey:@"mobihelpAppKey"];
		NSString *secret = [ios valueForKey:@"mobihelpAppSecret"];
		int ratePromptCount = [[ios valueForKey:@"mobiHelpReviewPromptCount"] intValue];
		BOOL autoReply = [[ios valueForKey:@"mobihelpAutoReplyEnabled"] boolValue];
		[appStoreId stringByAppendingString:[ios valueForKey:@"appleID"]];

		self.viewController = appDelegate.tealeafViewController;

		MobihelpConfig *config = [[MobihelpConfig alloc]initWithDomain:domain
			withAppKey:key
			andAppSecret:secret];
		config.appStoreId = appStoreId;
		config.launchCountForAppReviewPrompt = ratePromptCount;
		config.enableAutoReply = autoReply;
		[[Mobihelp sharedInstance]initWithConfig:config];
		self.initDone = YES;
	}
	@catch (NSException *exception) {
		NSLOG(@"{mobihelp} Failure to get: %@", exception);
	}
}

- (void) getUnreadNotificationCount: (NSDictionary *)jsonObject {
    NSInteger unread_count = 0;

    @try {
        unread_count = [[Mobihelp sharedInstance] unreadCount];
        [[PluginManager get] dispatchJSEvent:[NSDictionary dictionaryWithObjectsAndKeys:
                                @"mobihelpNotifCount", @"name",
                                [NSString stringWithFormat: @"%ld", unread_count], @"count",
                                nil]];
    }
    @catch (NSException *exception) {
        NSLOG(@"{mobihelp} Failure to get: %@", exception);
    }
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
	[[Mobihelp sharedInstance] presentInbox:self.viewController];
}

- (void) showFeedback: (NSDictionary *)jsonObject {
	[[Mobihelp sharedInstance] presentFeedback:self.viewController];
}

- (void) showSupport: (NSDictionary *)jsonObject {
	[[Mobihelp sharedInstance] presentSupport:self.viewController];
}
@end
