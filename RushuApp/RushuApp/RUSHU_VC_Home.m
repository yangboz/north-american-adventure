//
//  RUSHU_VC_Home.m
//  RushuApp
//
//  Created by yangboz on 14-8-28.
//  Copyright (c) 2014年 RUSHU. All rights reserved.
//

#import "RUSHU_VC_Home.h"

@interface RUSHU_VC_Home ()

@end

@implementation RUSHU_VC_Home

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    // Do any additional setup after loading the view, typically from a nib.
    //1
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    //2
    arryList = [[NSArray alloc]   initWithObjects:@"星巴克",@"打车",@"出差",nil];
    imagesList = [[NSArray alloc]   initWithObjects:@"starbucks.jpg",@"taxi.jpeg",@"travel.jpeg",nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark -TableViewDelegate
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [arryList count];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 150;
}

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
    [cell.textLabel setText:[arryList objectAtIndex:indexPath.row]];
//    [cell.detailTextLabel setText:@"via Codigator"];
    //
    UIImage *imageIcon = [UIImage imageNamed:[imagesList objectAtIndex:indexPath.row]];
    [cell.imageView setImage:imageIcon];
    //
    return cell;
    
}
@end
