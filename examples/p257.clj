(ns reader.snippet-server
  (:use compojure examples.snippet))

(use 'compojure)

(defroutes snippet-app
  "Create and view snippets."
  (GET "/ping" "pong")
  (GET "/pong" "ping")
  (ANY "*" (page-not-found)))
(run-server {:port 8080}
	    "/*" (servlet snippet-app))
