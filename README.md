Main class: DroneGame  
S key: shoot  
Up arrow key: move the drone up  
Down array key: move the drone down  
Right arrow key: move the drone right  
Left array key: move the drone left    

DroneGame: The main class, which contains all components and also responsible for drawing the count-down timer, the socre, the lives, and detecting and notifying PlaneField when a key is pressed.    

PlaneField: repsonsible for moving the drone and creating timers for MovingPlanes and MovingBullet. It also notifies DroneGame if it gets a collision notification from MovinfPlane.     

MovingBulet: resposible for moving a bullet by using a timer, detecting a collision of the bullet and a plane, and if a collsion happens, draws an explision.    

MovingPlane: responsible for moving a plane by using a timer, detecting a collision of the drone and a plane, if a collision happens, draws an explision and then notifies PlaneFiled that a collision happened.    

Score: represents the score.    

Drone: represents the drone.    

Plane: represents a plane.    

Bullet: represets a bullet.    

Explosion: represents a explosion.    

CountDown: represents the count-down timer.    

Life: represents the lives.    
