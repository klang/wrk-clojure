;(ns site
;  (:require [compojure.http.servlet :as servlet])
;  (:require [compojure.http.routes :as routes])
;  (:require [compojure.http.helpers :as http-helpers])
;  (:require [compojure.html :as html])
;  (:require [compojure.html.page-helpers :as page-helpers])
;  (:require [compojure.server.jetty :as jetty])
;  (:require [clojure.contrib.sql :as sql]))

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

;; Server settings
;(jetty/defserver calc-server
		 {:port 8081}
		 "/*" calc)

; Command to start the server
;(jetty/start calc-server)
