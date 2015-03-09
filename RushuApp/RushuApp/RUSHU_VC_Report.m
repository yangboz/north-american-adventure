//
//  RUSHU_VC_Report.m
//  RushuApp
//
//  Created by yangboz on 14-8-28.
//  Copyright (c) 2014年 RUSHU. All rights reserved.
//

#import "RUSHU_VC_Report.h"

@interface RUSHU_VC_Report ()

@end

@implementation RUSHU_VC_Report

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    //1
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    //2
    titleList = [[NSArray alloc]   initWithObjects:@"201408日常",@"上海出差",@"201407日常",nil];
    detailList = [[NSArray alloc]   initWithObjects:@"2014-9-1",@"2014-8-1",@"2014-7-30",nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark -TableViewDelegate
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [titleList count];
}
/*
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 150;
}
*/
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    //
    //    NSDictionary * dict = [self.tweetsArray objectAtIndex:indexPath.row];
    //
    static NSString *cellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    
    //5.1 you do not need this if you have set SettingsCell as identifier in the storyboard (else you can remove the comments on this code)
    if (cell == nil)
    {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:cellIdentifier];
    }
    //
    [cell.textLabel setText:[titleList objectAtIndex:indexPath.row]];
    [cell.detailTextLabel setText:[detailList objectAtIndex:indexPath.row]];
    //
//    UIImage *imageIcon = [UIImage imageNamed:[imagesList objectAtIndex:indexPath.row]];
//    [cell.imageView setImage:imageIcon];
    //
    return cell;
    
}

@end
