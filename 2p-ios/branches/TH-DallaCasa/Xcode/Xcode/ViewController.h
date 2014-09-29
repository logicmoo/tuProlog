//
//  ViewController.h
//  Xcode
//
//  Created by Lorenzo Dalla Casa on 22/05/14.
//  Copyright (c) 2014 Lorenzo Dalla Casa. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ViewController : UIViewController
@property (weak, nonatomic) IBOutlet UILabel *solutionLabel;

@property (weak, nonatomic) IBOutlet UILabel *label;
@property (weak, nonatomic) IBOutlet UIButton *solveButton;
@property (weak, nonatomic) IBOutlet UITextView *theoryTextView;
@property (weak, nonatomic) IBOutlet UIButton *theoryButton;
@property (weak, nonatomic) IBOutlet UIButton *nextButton;
@property (weak, nonatomic) IBOutlet UITextField *goalTextField;
@property (weak, nonatomic) IBOutlet UITextView *warningsTextView;
@property (weak, nonatomic) IBOutlet UITextView *solutionTextView;


- (IBAction)solve:(id)sender;
- (IBAction)hideKeyboard:(id)sender;
- (IBAction)getNextSolution:(id)sender;
- (IBAction)setTheory:(id)sender;
- (IBAction)theoryChanged:(id)sender;

@end
