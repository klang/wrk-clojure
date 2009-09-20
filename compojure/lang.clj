(ns lang)
(use 'compojure)

(def op-mapping
     {"add" +
      "subtract" -
      "multiply" *
      "divide" / })

(defn doop [op ls rs]
  "Do the operation on the two values"
  (if (contains? op-mapping op)
    (str ((get op-mapping op) ls rs))
    "Unrecognized operation"))

(defroutes calc
  (GET "/:op/:ls/:rs" 
    (doop (params :op) 
	  (. Float valueOf (params :ls))
	  (. Float valueOf (params :rs))))
  (ANY "/*" "Bad URL"))

(defserver lang-server
   {:port 8082}
   "/*" (servlet calc))

(start lang-server)
;; --> (stop my-server)
