(defn update-positions [{snake :snake, apple :apple, :as game}]  
  (if (eats? snake apple)    
    (merge game {:apple (create-apple) :snake (move snake :grow)})    
    (merge game {:snake (move snake)})))

