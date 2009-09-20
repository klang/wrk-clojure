;;http://paulbarry.com/articles/2007/12/22/getting-started-with-clojure

(defmulti encounter (fn [x y] [(:Species x) (:Species y)])) 

(defmethod encounter [:Bunny :Lion] [b l] 
  (str (b :name) " runs away from " (l :name))) 

(defmethod encounter [:Lion :Bunny] [l b]
  (str (l :name) " eats " (b :name))) 

(defmethod encounter [:Lion :Lion] [l1 l2]
  (str (l1 :name) " fights with " (l2 :name))) 

(defmethod encounter [:Bunny :Bunny] [b1 b2]
  (str (b1 :name) " mates with " (b2 :name)))

(def bugs {:Species :Bunny, :name "Bugs"}) 
(def betty {:Species :Bunny, :name "Betty"}) 
(def simba {:Species :Lion, :name "Simba"}) 
(def scar {:Species :Lion, :name "Scar"}) 

(println (encounter bugs betty)) 
(println (encounter bugs simba)) 
(println (encounter simba bugs)) 
(println (encounter simba scar))
