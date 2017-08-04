//
//  MenuTransitionManager.swift
//  dolarya
//
//  Created by Ramiro Rinaldi on 10/20/15.
//  Copyright © 2015 El Cronista. All rights reserved.
//

import UIKit

class MenuTransitionManager: NSObject, UIViewControllerAnimatedTransitioning, UIViewControllerTransitioningDelegate {
    var duration = 0.5
    var isPresenting = false
    
    var snapshot:UIView?
    
    func transitionDuration(transitionContext: UIViewControllerContextTransitioning?) -> NSTimeInterval
    {
        return duration
    }
    
    func animateTransition(transitionContext: UIViewControllerContextTransitioning)
    {
        //Get reference to our fromView, toView and container view
        let fromView = transitionContext.viewForKey(UITransitionContextFromViewKey)!
        let toView = transitionContext.viewForKey(UITransitionContextToViewKey)!
        
        //Set up the transform fo sliding
        let container = transitionContext.containerView()
        let moveDown = CGAffineTransformMakeTranslation(0, container!.frame.height)
        let moveUp = CGAffineTransformMakeTranslation(0, container!.frame.height * -1)
        
        //Add both views to the container view
        if(isPresenting)
        {
            toView.transform = moveUp
            snapshot = fromView.snapshotViewAfterScreenUpdates(true)
            container!.addSubview(toView)
            container?.addSubview(snapshot!)
        }
        
        //Perform the animation
        let options = UIViewAnimationOptions()
        UIView.animateWithDuration(duration, delay: 0.0, usingSpringWithDamping: 0.9, initialSpringVelocity: 0.3, options:options, animations: {
            if(self.isPresenting)
            {
                self.snapshot?.transform = moveDown
                toView.transform = CGAffineTransformIdentity
            }
            else
            {
                self.snapshot?.transform = CGAffineTransformIdentity
                fromView.transform = moveUp
            }
            }, completion: { finished in
                transitionContext.completeTransition(true)
                if(!self.isPresenting)
                {
                    self.snapshot?.removeFromSuperview()
                }
        })
    }
    
    func animationControllerForDismissedController(dismissed: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        isPresenting = false
        return self
    }
    
    func animationControllerForPresentedController(presented: UIViewController, presentingController presenting: UIViewController, sourceController source: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        isPresenting = true
        return self
    }
}
