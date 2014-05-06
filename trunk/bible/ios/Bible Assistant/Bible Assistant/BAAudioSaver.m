//
//  BAAudioSaver.m
//  Bible Assistant
//
//  Created by NIX on 14-3-30.
//  Copyright (c) 2014年 nixWork. All rights reserved.
//

#import "BAAudioSaver.h"
#import "BibleAudio.h"
#import "BibleAudio+BAAdditions.h"

@implementation BAAudioSaver

+ (BOOL)saveAudio:(NSData *)audioData ToDiskAndToBibleAudio:(BibleAudio *)bibleAudio
{
	NSString *name    = [[NSUUID UUID] UUIDString];
	//NSString *path	  = [NSString stringWithFormat:@"Documents/%@.mp3", name];
    NSString *path	  = [NSString stringWithFormat:@"Library/Caches/%@.mp3", name];
    
	NSString *filePath = [NSHomeDirectory() stringByAppendingPathComponent:path];
	
    if ([audioData writeToFile:filePath atomically:YES]) {
        NSLog(@"writeToFile filePath: %@", filePath);
        if ([BibleAudio isUserChooseAudioMale]) {
            bibleAudio.audio_male = path;
        } else {
            bibleAudio.audio = path;
        }
	} else {
		[[[UIAlertView alloc] initWithTitle:@"Error"
									message:@"There was an error saving your photo. Try again."
								   delegate:nil
						  cancelButtonTitle:@"OK"
						  otherButtonTitles: nil] show];
		return NO;
	}
	return YES;
}

+ (void)deleteAudioAtPath:(NSString *)path {
	NSError *error;
	NSString *audioToRemove = [NSHomeDirectory() stringByAppendingPathComponent:path];
	[[NSFileManager defaultManager] removeItemAtPath:audioToRemove error:&error];
}

@end
